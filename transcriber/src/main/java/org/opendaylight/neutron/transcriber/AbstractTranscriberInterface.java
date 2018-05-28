/*
 * Copyright (c) 2015 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.transcriber;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.CheckedFuture;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import javax.annotation.PreDestroy;
import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.controller.md.sal.binding.api.ReadOnlyTransaction;
import org.opendaylight.controller.md.sal.binding.api.ReadTransaction;
import org.opendaylight.controller.md.sal.binding.api.ReadWriteTransaction;
import org.opendaylight.controller.md.sal.binding.api.WriteTransaction;
import org.opendaylight.controller.md.sal.common.api.data.LogicalDatastoreType;
import org.opendaylight.controller.md.sal.common.api.data.OptimisticLockFailedException;
import org.opendaylight.controller.md.sal.common.api.data.ReadFailedException;
import org.opendaylight.controller.md.sal.common.api.data.TransactionCommitFailedException;
import org.opendaylight.neutron.spi.INeutronAdminAttributes;
import org.opendaylight.neutron.spi.INeutronBaseAttributes;
import org.opendaylight.neutron.spi.INeutronCRUD;
import org.opendaylight.neutron.spi.INeutronObject;
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

    private static final LogicalDatastoreType DATASTORE_TYPE = LogicalDatastoreType.CONFIGURATION;

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
        this.db = Preconditions.checkNotNull(db);
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

    public DataBroker getDataBroker() {
        Preconditions.checkNotNull(db);
        return db;
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
            builder = builderClass.newInstance();

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
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException
                | NoSuchMethodException e) {
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

    protected <S1 extends INeutronAdminAttributes<S1>, M extends BaseAttributes & AdminAttributes, B extends Builder<M>>
        void toMdAdminAttributes(S1 neutronObject, B builder) {
        toMdBaseAttributes(neutronObject, builder);
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

    protected <M extends BaseAttributes & AdminAttributes, S1 extends INeutronAdminAttributes<S1>>
        void fromMdAdminAttributes(M attr, S1 answer) {
        fromMdBaseAttributes(attr, answer);
        if (attr.isAdminStateUp() != null) {
            answer.setAdminStateUp(attr.isAdminStateUp());
        }
        if (attr.getStatus() != null) {
            answer.setStatus(attr.getStatus());
        }
    }

    protected abstract T toMd(S neutronObject);

    protected T toMd(String uuid) {
        Builder<T> builder;
        try {
            builder = builderClass.newInstance();
            setUuid.invoke(builder, toUuid(uuid));
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            // should not happen.
            throw new IllegalArgumentException(e);
        }
        return builder.build();
    }

    protected abstract S fromMd(T dataObject);

    private <W extends DataObject> W readMd(InstanceIdentifier<W> path, ReadTransaction tx) {
        Preconditions.checkNotNull(tx);
        W result = null;
        final CheckedFuture<Optional<W>,
                ReadFailedException> future = tx.read(DATASTORE_TYPE, path);
        if (future != null) {
            Optional<W> optional;
            try {
                optional = future.checkedGet();
                if (optional.isPresent()) {
                    result = optional.get();
                }
            } catch (final ReadFailedException e) {
                LOG.warn("Failed to read {}", path, e);
            }
        }
        return result;
    }

    protected <W extends DataObject> W readMd(InstanceIdentifier<W> path) {
        try (ReadOnlyTransaction tx = getDataBroker().newReadOnlyTransaction()) {
            return readMd(path, tx);
        }
    }

    private void addMd(S neutronObject, WriteTransaction tx) throws InterruptedException, ExecutionException {
        // TODO think about adding existence logic
        updateMd(neutronObject, tx);
    }

    protected boolean addMd(S neutronObject) {
        try {
            final WriteTransaction tx = getDataBroker().newWriteOnlyTransaction();
            addMd(neutronObject, tx);
            return true;
        } catch (InterruptedException | ExecutionException e) {
            LOG.warn("Transaction failed", e);
        }
        return false;
    }

    private void updateMd(S neutronObject, WriteTransaction tx) throws InterruptedException, ExecutionException {
        Preconditions.checkNotNull(tx);

        final T item = toMd(neutronObject);
        final InstanceIdentifier<T> iid = createInstanceIdentifier(item);
        tx.put(DATASTORE_TYPE, iid, item, true);
        final CheckedFuture<Void, TransactionCommitFailedException> future = tx.submit();
        // Check if it's successfully committed, otherwise exception will be thrown.
        future.get();
    }

    protected boolean updateMd(S neutronObject) {
        int retries = RETRY_MAX;
        while (retries-- >= 0) {
            try {
                final WriteTransaction tx = getDataBroker().newWriteOnlyTransaction();
                updateMd(neutronObject, tx);
                return true;
            } catch (InterruptedException | ExecutionException e) {
                if (e.getCause() instanceof OptimisticLockFailedException) {
                    LOG.warn("Got OptimisticLockFailedException - {} {}", neutronObject, retries);
                    continue;
                }
                // TODO: rethrow exception. don't mask exception
                LOG.error("Transaction failed", e);
            }
            break;
        }
        return false;
    }

    private void removeMd(T item, WriteTransaction tx) throws InterruptedException, ExecutionException {
        Preconditions.checkNotNull(tx);
        final InstanceIdentifier<T> iid = createInstanceIdentifier(item);
        tx.delete(DATASTORE_TYPE, iid);
        final CheckedFuture<Void, TransactionCommitFailedException> future = tx.submit();
        // Check if it's successfully committed, otherwise exception will be thrown.
        future.get();
    }

    protected boolean removeMd(T item) {
        final ReadWriteTransaction tx = getDataBroker().newReadWriteTransaction();
        try {
            removeMd(item, tx);
            return true;
        } catch (InterruptedException | ExecutionException e) {
            LOG.warn("Transaction failed", e);
        }
        return false;
    }

    protected static Uuid toUuid(String uuid) {
        Preconditions.checkNotNull(uuid);
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

    // TODO @Override
    public boolean exists(String uuid, ReadTransaction tx) {
        Preconditions.checkNotNull(tx);
        final T dataObject = readMd(createInstanceIdentifier(toMd(uuid)), tx);
        return dataObject != null;
    }

    @Override
    public boolean exists(String uuid) {
        try (ReadOnlyTransaction tx = getDataBroker().newReadOnlyTransaction()) {
            return exists(uuid, tx);
        }
    }

    private S get(String uuid, ReadTransaction tx) {
        Preconditions.checkNotNull(tx);
        final T dataObject = readMd(createInstanceIdentifier(toMd(uuid)), tx);
        if (dataObject == null) {
            return null;
        }
        return fromMd(dataObject);
    }

    @Override
    public S get(String uuid) {
        try (ReadOnlyTransaction tx = getDataBroker().newReadOnlyTransaction()) {
            return get(uuid, tx);
        }
    }

    protected abstract List<T> getDataObjectList(U dataObjects);

    private List<S> getAll(ReadTransaction tx) {
        Preconditions.checkNotNull(tx);
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
    public List<S> getAll() {
        try (ReadOnlyTransaction tx = getDataBroker().newReadOnlyTransaction()) {
            return getAll(tx);
        }
    }

    private Result add(S input, ReadWriteTransaction tx) throws InterruptedException, ExecutionException {
        Preconditions.checkNotNull(tx);
        if (exists(input.getID(), tx)) {
            tx.cancel();
            return Result.AlreadyExists;
        }
        addMd(input, tx);
        return Result.Success;
    }

    @Override
    public Result add(S input) {
        int retries = RETRY_MAX;
        while (retries-- >= 0) {
            final ReadWriteTransaction tx = getDataBroker().newReadWriteTransaction();
            try {
                if (areAllDependenciesAvailable(tx, input)) {
                    return add(input, tx);
                } else {
                    return Result.DependencyMissing;
                }
            } catch (InterruptedException | ExecutionException e) {
                // TODO replace all this with org.opendaylight.genius.infra.RetryingManagedNewTransactionRunner
                if (e.getCause() instanceof OptimisticLockFailedException) {
                    LOG.warn("Got OptimisticLockFailedException - {} {}", input, retries);
                    continue;
                }
                // TODO: rethrow exception. don't mask exception
                LOG.error("Transaction failed", e);
            }
            break;
        }
        // TODO remove when re-throwing, and remove Result.Exception completely
        return Result.Exception;
    }

    private boolean remove(String uuid, ReadWriteTransaction tx) throws InterruptedException, ExecutionException {
        Preconditions.checkNotNull(tx);
        if (!exists(uuid, tx)) {
            tx.cancel();
            return false;
        }
        removeMd(toMd(uuid), tx);
        return true;
    }

    @Override
    public boolean remove(String uuid) {
        int retries = RETRY_MAX;
        while (retries-- >= 0) {
            final ReadWriteTransaction tx = getDataBroker().newReadWriteTransaction();
            try {
                return remove(uuid, tx);
            } catch (InterruptedException | ExecutionException e) {
                if (e.getCause() instanceof OptimisticLockFailedException) {
                    LOG.warn("Got OptimisticLockFailedException - {} {}", uuid, retries);
                    continue;
                }
                // TODO: rethrow exception. don't mask exception
                LOG.error("Transaction failed", e);
            }
            break;
        }
        return false;
    }

    private boolean update(String uuid, S delta, ReadWriteTransaction tx)
            throws InterruptedException, ExecutionException {
        Preconditions.checkNotNull(tx);
        if (!exists(uuid, tx)) {
            tx.cancel();
            return false;
        }
        updateMd(delta, tx);
        return true;
    }

    @Override
    public boolean update(String uuid, S delta) {
        int retries = RETRY_MAX;
        while (retries-- >= 0) {
            final ReadWriteTransaction tx = getDataBroker().newReadWriteTransaction();
            try {
                return update(uuid, delta, tx);
            } catch (InterruptedException | ExecutionException e) {
                if (e.getCause() instanceof OptimisticLockFailedException) {
                    LOG.warn("Got OptimisticLockFailedException - {} {} {}", uuid, delta, retries);
                    continue;
                }
                // TODO: rethrow exception. don't mask exception
                LOG.error("Transaction failed", e);
            }
            break;
        }
        return false;
    }

    /**
     * Check if this particular (subclass) transcriber's dependencies are met.
     * Default implementation just returns true.  Some but not all transcribers will customize this.
     *
     * <p>Implementations *MUST* use the passed in transaction.  They will typically call the
     * {@link #exists(String, ReadTransaction)} method on ANOTHER transcriber with it.
     * Care must be taken to use that method instead of the {@link #exists(String)} without a transaction argument.
     *
     * @param tx the transaction within which to perform reads to check for dependencies
     * @param neutronObject the incoming main neutron object in which there may be references to dependencies
     *
     * @return true if all dependencies are available and
     *         {@link #add(INeutronObject)} operation can proceed; false if there
     *         are unmet dependencies, which will cause the add to abort, and a respective
     *         error code returned to the caller.
     */
    protected boolean areAllDependenciesAvailable(ReadTransaction tx, S neutronObject) {
        return true;
    }
}
