/*
 * Copyright (c) 2015 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.neutron.transcriber;

import static java.util.Objects.requireNonNull;

import com.google.common.base.Throwables;
import com.google.common.util.concurrent.FluentFuture;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import javax.annotation.PreDestroy;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.opendaylight.infrautils.utils.function.CheckedFunction;
import org.opendaylight.mdsal.binding.api.DataBroker;
import org.opendaylight.mdsal.binding.api.ReadOperations;
import org.opendaylight.mdsal.binding.api.ReadTransaction;
import org.opendaylight.mdsal.binding.api.ReadWriteTransaction;
import org.opendaylight.mdsal.binding.api.WriteTransaction;
import org.opendaylight.mdsal.common.api.LogicalDatastoreType;
import org.opendaylight.mdsal.common.api.OptimisticLockFailedException;
import org.opendaylight.mdsal.common.api.ReadFailedException;
import org.opendaylight.mdsal.common.api.TransactionCommitFailedException;
import org.opendaylight.neutron.spi.INeutronAdminAttributes;
import org.opendaylight.neutron.spi.INeutronBaseAttributes;
import org.opendaylight.neutron.spi.INeutronCRUD;
import org.opendaylight.neutron.spi.INeutronObject;
import org.opendaylight.neutron.spi.NeutronObject;
import org.opendaylight.neutron.spi.ReadFailedRuntimeException;
import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.yang.types.rev130715.Uuid;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.attrs.rev150712.AdminAttributes;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.attrs.rev150712.BaseAttributes;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.attrs.rev150712.IdAttributes;
import org.opendaylight.yangtools.concepts.Builder;
import org.opendaylight.yangtools.yang.binding.Augmentable;
import org.opendaylight.yangtools.yang.binding.ChildOf;
import org.opendaylight.yangtools.yang.binding.DataObject;
import org.opendaylight.yangtools.yang.binding.Identifiable;
import org.opendaylight.yangtools.yang.binding.Identifier;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.opendaylight.yangtools.yang.common.OperationFailedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base class of Transcriber converts data from/to neutron spi to/from md-sal yang model.
 *
 * <pre>
 * {@code
 * V -> U -> T
 *        K: key
 *
 *
 * T(MD-SAL model) <-> S (neutron spi): Neutron northbound
 *                  -> fromMd()
 *                 <-  toMd()
 * }
 * </pre>
 *
 * <p>
 * Example
 * T: Port
 * U: Ports
 * K: PortKey
 * S: NeutronPort
 * V: Neutron
 *
 * <p>
 * @param <T> Target yang model
 * @param <U> parent of T
 * @param <K> key type to indentify T
 * @param <S> Neutron-spi class
 * @param <V> parent of U
 */
public abstract class AbstractTranscriberInterface<
        T extends DataObject & Identifiable<K> & ChildOf<? super U>,
        U extends ChildOf<? super V> & Augmentable<U>,
        K extends Identifier<T>, S extends INeutronObject<S>,
        V extends DataObject>
        implements AutoCloseable, INeutronCRUD<S> {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractTranscriberInterface.class);

    // T extends DataObject & Identifiable<K> & ChildOf<? super U> as 0th type argument
    private static final int MD_LIST_CLASS_TYPE_INDEX = 0;
    // U extends ChildOf<? super Neutron> & Augmentable<U> as 1st type argument
    private static final int MD_CONTAINER_CLASS_TYPE_INDEX = 1;
    // V extends DataObject as 4th type argument
    private static final int MD_PARENT_CLASS_TYPE_INDEX = 4;
    // S extends INeutronObject<S> as 3rd type argument
    private static final int NEUTRON_OBJECT_TYPE_INDEX = 3;

    private static final int DEDASHED_UUID_LENGTH = 32;
    private static final int DEDASHED_UUID_START = 0;
    private static final int DEDASHED_UUID_DIV1 = 8;
    private static final int DEDASHED_UUID_DIV2 = 12;
    private static final int DEDASHED_UUID_DIV3 = 16;
    private static final int DEDASHED_UUID_DIV4 = 20;

    private static final int RETRY_MAX = 2;

    private final DataBroker db;

    private final Class<V> mdParentClass;
    private final Class<U> mdContainerClass;
    private final Class<T> mdListClass;

    // Unfortunately odl yangtools doesn't model yang model "uses" as
    // class/interface hierarchy. So we need to resort to use reflection
    // to call setter method.
    private final Class<? extends Builder<T>> builderClass;
    private final Method setUuid;
    private final Method setTenantId;
    private final Method setProjectId;
    private final Method setName;
    private final Method setAdminStateUp;
    private final Method setStatus;
    private final Method setRevisionNumber;

    protected Class<V> getMdParentClass(final Type[] types) {
        @SuppressWarnings("unchecked")
        Class<V> localMdParentClass = (Class<V>) types[MD_PARENT_CLASS_TYPE_INDEX];
        return localMdParentClass;
    }

    protected AbstractTranscriberInterface(Class<? extends Builder<T>> builderClass, DataBroker db) {
        this.db = requireNonNull(db);
        this.builderClass = builderClass;

        ParameterizedType parameterizedType = (ParameterizedType) getClass().getGenericSuperclass();
        Type[] types = parameterizedType.getActualTypeArguments();
        @SuppressWarnings("unchecked")
        Class<T> localMdListClass = (Class<T>) types[MD_LIST_CLASS_TYPE_INDEX];
        mdListClass = localMdListClass;
        @SuppressWarnings("unchecked")
        Class<U> localMdContainerClass = (Class<U>) types[MD_CONTAINER_CLASS_TYPE_INDEX];
        mdContainerClass = localMdContainerClass;
        mdParentClass = getMdParentClass(types);
        @SuppressWarnings("unchecked")
        Class<S> neutronObjectClass = (Class<S>) types[NEUTRON_OBJECT_TYPE_INDEX];
        try {
            setUuid = builderClass.getDeclaredMethod("setUuid", Uuid.class);
            setTenantId = builderClass.getDeclaredMethod("setTenantId", Uuid.class);
            setProjectId = builderClass.getDeclaredMethod("setProjectId", String.class);
            setRevisionNumber = builderClass.getDeclaredMethod("setRevisionNumber", Long.class);
            if (INeutronBaseAttributes.class.isAssignableFrom(neutronObjectClass)) {
                setName = builderClass.getDeclaredMethod("setName", String.class);
            } else {
                setName = null;
            }

            if (INeutronAdminAttributes.class.isAssignableFrom(neutronObjectClass)) {
                setAdminStateUp = builderClass.getDeclaredMethod("setAdminStateUp", Boolean.class);
                setStatus = builderClass.getDeclaredMethod("setStatus", String.class);
            } else {
                setAdminStateUp = null;
                setStatus = null;
            }
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public @NonNull DataBroker getDataBroker() {
        return requireNonNull(db);
    }

    private InstanceIdentifier<T> createInstanceIdentifier(T item) {
        return InstanceIdentifier.create(mdParentClass).child(mdContainerClass).child(mdListClass, item.key());
    }

    private InstanceIdentifier<U> createInstanceIdentifier() {
        return InstanceIdentifier.create(mdParentClass).child(mdContainerClass);
    }

    protected static <S1 extends INeutronObject<S1>, M extends IdAttributes, B extends Builder<M>>
        B toMdIds(INeutronObject<S1> neutronObject, Class<B> builderClass) {
        B builder;
        try {
            builder = builderClass.getDeclaredConstructor().newInstance();

            if (neutronObject.getID() != null) {
                final Method setUuid = builderClass.getMethod("setUuid", Uuid.class);
                setUuid.invoke(builder, toUuid(neutronObject.getID()));
            }
            if (neutronObject.getTenantID() != null && !neutronObject.getTenantID().isEmpty()) {
                final Method setTenantId = builderClass.getMethod("setTenantId", Uuid.class);
                setTenantId.invoke(builder, toUuid(neutronObject.getTenantID()));
            }
            if (neutronObject.getProjectID() != null) {
                final Method setProjectId = builderClass.getMethod("setProjectId", String.class);
                setProjectId.invoke(builder, neutronObject.getTenantID());
            }
            if (neutronObject.getRevisionNumber() != null) {
                final Method setRevisionNumber = builderClass.getMethod("setRevisionNumber", Long.class);
                setRevisionNumber.invoke(builder, neutronObject.getRevisionNumber());
            }
        } catch (ReflectiveOperationException e) {
            throw new IllegalArgumentException(e);
        }
        return builder;
    }

    protected <S1 extends INeutronObject<S1>, M extends IdAttributes, B extends Builder<M>>
        void toMdIds(INeutronObject<S1> neutronObject, B builder) {
        try {
            if (neutronObject.getID() != null) {
                setUuid.invoke(builder, toUuid(neutronObject.getID()));
            } else {
                LOG.warn("Attempting to write neutron object {} without UUID", builderClass.getSimpleName());
            }
            if (neutronObject.getTenantID() != null && !neutronObject.getTenantID().isEmpty()) {
                setTenantId.invoke(builder, toUuid(neutronObject.getTenantID()));
            }
            if (neutronObject.getProjectID() != null) {
                setProjectId.invoke(builder, neutronObject.getTenantID());
            }
            if (neutronObject.getRevisionNumber() != null) {
                setRevisionNumber.invoke(builder, neutronObject.getRevisionNumber());
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new IllegalArgumentException(e);
        }
    }

    protected <S1 extends INeutronObject<S1>>
        void fromMdIds(IdAttributes idAttributes, INeutronObject<S1> answer) {
        if (idAttributes.getUuid() != null) {
            answer.setID(idAttributes.getUuid().getValue());
        }
        if (idAttributes.getTenantId() != null) {
            answer.setTenantID(idAttributes.getTenantId());
        }
        if (idAttributes.getProjectId() != null) {
            answer.setProjectID(idAttributes.getProjectId());
        }
        if (idAttributes.getRevisionNumber() != null) {
            answer.setRevisionNumber(idAttributes.getRevisionNumber());
        }
    }

    protected <S1 extends INeutronBaseAttributes<S1>, M extends BaseAttributes, B extends Builder<M>>
        void toMdBaseAttributes(S1 neutronObject, B builder) {
        toMdIds(neutronObject, builder);
        try {
            if (neutronObject.getName() != null) {
                setName.invoke(builder, neutronObject.getName());
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new IllegalArgumentException(e);
        }
    }

    protected <S1 extends INeutronBaseAttributes<S1>>
        void fromMdBaseAttributes(BaseAttributes baseAttributes, S1 answer) {
        fromMdIds(baseAttributes, answer);
        if (baseAttributes.getName() != null) {
            answer.setName(baseAttributes.getName());
        }
    }

    protected <S1 extends INeutronAdminAttributes<S1>, M extends AdminAttributes, B extends Builder<M>>
        void toMdAdminAttributes(S1 neutronObject, B builder) {
        try {
            if (neutronObject.getAdminStateUp() != null) {
                setAdminStateUp.invoke(builder, neutronObject.getAdminStateUp());
            }
            if (neutronObject.getStatus() != null) {
                setStatus.invoke(builder, neutronObject.getStatus());
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new IllegalArgumentException(e);
        }
    }

    protected <M extends AdminAttributes, S1 extends INeutronAdminAttributes<S1>> void fromMdAdminAttributes(M attr,
            S1 answer) {
        if (attr.getAdminStateUp() != null) {
            answer.setAdminStateUp(attr.getAdminStateUp());
        }
        if (attr.getStatus() != null) {
            answer.setStatus(attr.getStatus());
        }
    }

    protected abstract T toMd(S neutronObject);

    protected T toMd(String uuid) {
        Builder<T> builder;
        try {
            builder = builderClass.getDeclaredConstructor().newInstance();
            setUuid.invoke(builder, toUuid(uuid));
        } catch (ReflectiveOperationException e) {
            // should not happen.
            throw new IllegalArgumentException(e);
        }
        return builder.build();
    }

    protected abstract S fromMd(T dataObject);

    private static <W extends DataObject> W readMd(InstanceIdentifier<W> path, ReadOperations tx)
            throws ReadFailedException {
        final FluentFuture<Optional<W>> future = requireNonNull(tx).read(LogicalDatastoreType.CONFIGURATION, path);
        try {
            return future.get().orElse(null);
        } catch (InterruptedException e) {
            throw new ReadFailedException("Interrupted while waiting for read of " + path, e);
        } catch (ExecutionException e) {
            Throwables.throwIfInstanceOf(e.getCause(), ReadFailedException.class);
            throw new ReadFailedException("Read of " + path + " failed", e);
        }
    }

    protected <W extends DataObject> W readMd(InstanceIdentifier<W> path) throws ReadFailedException {
        try (ReadTransaction tx = getDataBroker().newReadOnlyTransaction()) {
            return readMd(path, tx);
        }
    }

    private void addMd(S neutronObject, WriteTransaction tx) throws TransactionCommitFailedException {
        // TODO think about adding existence logic
        updateMd(neutronObject, tx);
    }

    private void updateMd(S neutronObject, WriteTransaction tx) throws TransactionCommitFailedException {
        requireNonNull(tx);

        final T item = toMd(neutronObject);
        final InstanceIdentifier<T> iid = createInstanceIdentifier(item);
        tx.mergeParentStructurePut(LogicalDatastoreType.CONFIGURATION, iid, item);
        // Check if it's successfully committed, otherwise exception will be thrown.
        checkedCommit(tx);
    }

    private void removeMd(T item, WriteTransaction tx) throws TransactionCommitFailedException {
        requireNonNull(tx);
        final InstanceIdentifier<T> iid = createInstanceIdentifier(item);
        tx.delete(LogicalDatastoreType.CONFIGURATION, iid);
        // Check if it's successfully committed, otherwise exception will be thrown.
        checkedCommit(tx);
    }

    protected static Uuid toUuid(String uuid) {
        requireNonNull(uuid, "uuid");
        Uuid result;
        try {
            result = new Uuid(uuid);
        } catch (final IllegalArgumentException e) {
            // OK... someone didn't follow RFC 4122... lets try this the hard way
            final String dedashed = uuid.replace("-", "");
            if (dedashed.length() == DEDASHED_UUID_LENGTH) {
                final String redashed = dedashed.substring(DEDASHED_UUID_START, DEDASHED_UUID_DIV1) + "-"
                        + dedashed.substring(DEDASHED_UUID_DIV1, DEDASHED_UUID_DIV2) + "-"
                        + dedashed.substring(DEDASHED_UUID_DIV2, DEDASHED_UUID_DIV3) + "-"
                        + dedashed.substring(DEDASHED_UUID_DIV3, DEDASHED_UUID_DIV4) + "-"
                        + dedashed.substring(DEDASHED_UUID_DIV4, DEDASHED_UUID_LENGTH);
                result = new Uuid(redashed);
            } else {
                throw e;
            }
        }
        return result;
    }

    @Override
    @PreDestroy
    public void close() throws Exception {
    }

    @Override
    public boolean exists(String uuid, ReadOperations tx) throws ReadFailedException {
        requireNonNull(tx);
        final T dataObject = readMd(createInstanceIdentifier(toMd(uuid)), tx);
        return dataObject != null;
    }

    private S get(String uuid, ReadTransaction tx) throws ReadFailedException {
        requireNonNull(tx);
        final T dataObject = readMd(createInstanceIdentifier(toMd(uuid)), tx);
        if (dataObject == null) {
            return null;
        }
        return fromMd(dataObject);
    }

    @Override
    public S get(String uuid) throws ReadFailedException {
        try (ReadTransaction tx = getDataBroker().newReadOnlyTransaction()) {
            return get(uuid, tx);
        }
    }

    protected abstract Collection<T> getDataObjectList(U dataObjects);

    private List<S> getAll(ReadTransaction tx) throws ReadFailedException {
        requireNonNull(tx);
        final Set<S> allNeutronObjects = new HashSet<>();
        final U dataObjects = readMd(createInstanceIdentifier(), tx);
        if (dataObjects != null) {
            for (final T dataObject : getDataObjectList(dataObjects)) {
                allNeutronObjects.add(fromMd(dataObject));
            }
        }
        LOG.debug("Exiting _getAll, Found {} OpenStackFirewall", allNeutronObjects.size());
        final List<S> ans = new ArrayList<>();
        ans.addAll(allNeutronObjects);
        return ans;
    }

    @Override
    public List<S> getAll() throws ReadFailedRuntimeException {
        try (ReadTransaction tx = getDataBroker().newReadOnlyTransaction()) {
            try {
                return getAll(tx);
            } catch (ReadFailedException e) {
                throw new ReadFailedRuntimeException(e);
            }
        }
    }

    private Result add(S input, ReadWriteTransaction tx) throws OperationFailedException {
        requireNonNull(tx);
        if (exists(input.getID(), tx)) {
            tx.cancel();
            return Result.AlreadyExists;
        }
        addMd(input, tx);
        return Result.Success;
    }

    @Override
    public Result add(S input) throws OperationFailedException {
        int retries = RETRY_MAX;
        OptimisticLockFailedException lastOptimisticLockFailedException = null;
        while (retries-- >= 0) {
            final ReadWriteTransaction tx = getDataBroker().newReadWriteTransaction();
            try {
                if (areAllDependenciesAvailable(tx, input)) {
                    return add(input, tx);
                } else {
                    return Result.DependencyMissing;
                }
            } catch (TransactionCommitFailedException e) {
                // TODO replace all this with org.opendaylight.genius.infra.RetryingManagedNewTransactionRunner
                if (e instanceof OptimisticLockFailedException) {
                    LOG.debug("Got OptimisticLockFailedException - {} {}", input, retries);
                    lastOptimisticLockFailedException = (OptimisticLockFailedException) e;
                    continue;
                } else {
                    throw e;
                }
            }
        }
        throw lastOptimisticLockFailedException;
    }

    private boolean remove(String uuid, ReadWriteTransaction tx) throws OperationFailedException {
        requireNonNull(tx);
        if (!exists(uuid, tx)) {
            tx.cancel();
            return false;
        }
        removeMd(toMd(uuid), tx);
        return true;
    }

    @Override
    public boolean remove(String uuid) throws OperationFailedException {
        int retries = RETRY_MAX;
        OptimisticLockFailedException lastOptimisticLockFailedException = null;
        while (retries-- >= 0) {
            final ReadWriteTransaction tx = getDataBroker().newReadWriteTransaction();
            try {
                return remove(uuid, tx);
            } catch (TransactionCommitFailedException e) {
                // TODO replace all this with org.opendaylight.genius.infra.RetryingManagedNewTransactionRunner
                if (e instanceof OptimisticLockFailedException) {
                    LOG.debug("Got OptimisticLockFailedException - {} {}", uuid, retries);
                    lastOptimisticLockFailedException = (OptimisticLockFailedException) e;
                    continue;
                } else {
                    throw e;
                }
            }
        }
        throw lastOptimisticLockFailedException;
    }

    private Result update(String uuid, S delta, ReadWriteTransaction tx) throws OperationFailedException {
        requireNonNull(tx);
        if (!exists(uuid, tx)) {
            tx.cancel();
            return Result.DoesNotExist;
        }
        updateMd(delta, tx);
        return Result.Success;
    }

    @Override
    public Result update(String uuid, S delta) throws OperationFailedException {
        int retries = RETRY_MAX;
        OptimisticLockFailedException lastOptimisticLockFailedException = null;
        while (retries-- >= 0) {
            final ReadWriteTransaction tx = getDataBroker().newReadWriteTransaction();
            try {
                if (areAllDependenciesAvailable(tx, delta)) {
                    return update(uuid, delta, tx);
                } else {
                    return Result.DependencyMissing;
                }
            } catch (TransactionCommitFailedException e) {
                // TODO replace all this with org.opendaylight.genius.infra.RetryingManagedNewTransactionRunner
                if (e instanceof OptimisticLockFailedException) {
                    LOG.debug("Got OptimisticLockFailedException - {} {} {}", uuid, delta, retries);
                    lastOptimisticLockFailedException = (OptimisticLockFailedException) e;
                    continue;
                } else {
                    throw e;
                }
            }
        }
        throw lastOptimisticLockFailedException;
    }

    /**
     * Check if this particular (subclass) transcriber's dependencies are met.
     * Default implementation just returns true.  Some but not all transcribers will customize this.
     *
     * <p>Implementations *MUST* use the passed in transaction.  They will typically call the
     * {@link #exists(String, ReadOperations)} method on ANOTHER transcriber with it.
     *
     * <p>Implementations should chain {@link #ifNonNull(Object, CheckedFunction)}, or perform null safe comparisons
     * otherwise, for both optional non-mandatory {@link NeutronObject} as well as mandatory properties which may well
     * be null. Both must mandatory and non-mandatory must be guarded, because modify (update) operation are allowed to
     * contain partial neutron objects with missing fields.
     *
     * @param tx the transaction within which to perform reads to check for dependencies
     * @param neutronObject the incoming main neutron object in which there may be references to dependencies
     *
     * @return true if all dependencies are available and the
     *         {@link #add(INeutronObject)} (or {@link #update(String, INeutronObject)} operation can proceed; false if
     *         there are unmet dependencies, which will cause the add to abort, and a respective
     *         error code returned to the caller.
     *
     * @throws ReadFailedException in case of a data store problem
     */
    protected boolean areAllDependenciesAvailable(ReadOperations tx, S neutronObject) throws ReadFailedException {
        return true;
    }

    /**
     * Utility to perform well readable code of null-safe chains of e.g.
     * {@link #exists(String, ReadOperations)} method calls.
     *
     * @throws ReadFailedException in case of a data store problem
     */
    protected static final <X> boolean ifNonNull(@Nullable X property,
            CheckedFunction<@NonNull X, @NonNull Boolean, ReadFailedException> function) throws ReadFailedException {
        if (property != null) {
            Boolean result = function.apply(property);
            return requireNonNull(result, "result");
        } else {
            // We return true, in line with the default implementation
            // in org.opendaylight.neutron.transcriber.AbstractTranscriberInterface.
            //      areAllDependenciesAvailable(ReadTransaction, S)
            return true;
        }
    }

    protected static final void checkedCommit(WriteTransaction tx) throws TransactionCommitFailedException {
        final FluentFuture<?> future = tx.commit();
        try {
            future.get();
        } catch (InterruptedException e) {
            throw new TransactionCommitFailedException("Interrupted while waiting for commit", e);
        } catch (ExecutionException e) {
            Throwables.throwIfInstanceOf(e.getCause(), TransactionCommitFailedException.class);
            throw new TransactionCommitFailedException("Transaction commit failed", e);
        }
    }
}
