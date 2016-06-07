/*
 * Copyright (c) 2015 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.transcriber;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import java.util.concurrent.ExecutionException;

import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.controller.md.sal.binding.api.ReadOnlyTransaction;
import org.opendaylight.controller.md.sal.binding.api.ReadTransaction;
import org.opendaylight.controller.md.sal.binding.api.ReadWriteTransaction;
import org.opendaylight.controller.md.sal.binding.api.WriteTransaction;
import org.opendaylight.controller.md.sal.common.api.data.LogicalDatastoreType;
import org.opendaylight.controller.md.sal.common.api.data.OptimisticLockFailedException;
import org.opendaylight.controller.md.sal.common.api.data.ReadFailedException;
import org.opendaylight.controller.md.sal.common.api.data.TransactionCommitFailedException;
import org.opendaylight.controller.sal.binding.api.BindingAwareBroker.ProviderContext;
import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.yang.types.rev130715.Uuid;
import org.opendaylight.yangtools.yang.binding.Augmentable;
import org.opendaylight.yangtools.yang.binding.ChildOf;
import org.opendaylight.yangtools.yang.binding.DataObject;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.CheckedFuture;

import org.opendaylight.neutron.spi.INeutronCRUD;
import org.opendaylight.neutron.spi.INeutronObject;

public abstract class AbstractNeutronInterface<T extends DataObject, U extends ChildOf<? extends DataObject> & Augmentable<U>, S extends INeutronObject> implements AutoCloseable, INeutronCRUD<S> {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractNeutronInterface.class);
    private static final int DEDASHED_UUID_LENGTH = 32;
    private static final int DEDASHED_UUID_START = 0;
    private static final int DEDASHED_UUID_DIV1 = 8;
    private static final int DEDASHED_UUID_DIV2 = 12;
    private static final int DEDASHED_UUID_DIV3 = 16;
    private static final int DEDASHED_UUID_DIV4 = 20;

    private static final int RETRY_MAX = 2;

    private final DataBroker db;

    AbstractNeutronInterface(ProviderContext providerContext) {
        this.db = providerContext.getSALService(DataBroker.class);
    }

    public DataBroker getDataBroker() {
        Preconditions.checkNotNull(db);
        return db;
    }

    protected abstract InstanceIdentifier<T> createInstanceIdentifier(T item);

    protected abstract InstanceIdentifier<U> createInstanceIdentifier();

    protected abstract T toMd(S neutronObject);

    protected abstract T toMd(String uuid);

    protected abstract S fromMd(T dataObject);

    private <T extends DataObject> T readMd(InstanceIdentifier<T> path, ReadTransaction tx) {
        Preconditions.checkNotNull(tx);
        T result = null;
        final CheckedFuture<Optional<T>, ReadFailedException> future = tx.read(LogicalDatastoreType.CONFIGURATION, path);
        if (future != null) {
            Optional<T> optional;
            try {
                optional = future.checkedGet();
                if (optional.isPresent()) {
                    result = optional.get();
                }
            } catch (final ReadFailedException e) {
                LOGGER.warn("Failed to read {}", path, e);
            }
        }
        return result;
    }

    protected <T extends DataObject> T readMd(InstanceIdentifier<T> path) {
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
            LOGGER.warn("Transaction failed", e);
        }
        return false;
    }

    private void updateMd(S neutronObject, WriteTransaction tx) throws InterruptedException, ExecutionException {
        Preconditions.checkNotNull(tx);

        final T item = toMd(neutronObject);
        final InstanceIdentifier<T> iid = createInstanceIdentifier(item);
        tx.put(LogicalDatastoreType.CONFIGURATION, iid, item,true);
        final CheckedFuture<Void, TransactionCommitFailedException> future = tx.submit();
        future.get();   // Check if it's successfuly committed, otherwise exception will be thrown.
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
                    LOGGER.warn("Got OptimisticLockFailedException - {} {}", neutronObject, retries);
                    continue;
                }
                // TODO: rethrow exception. don't mask exception
                LOGGER.error("Transaction failed", e);
            }
            break;
        }
        return false;
    }

    private void removeMd(T item, WriteTransaction tx) throws InterruptedException, ExecutionException {
        Preconditions.checkNotNull(tx);
        final InstanceIdentifier<T> iid = createInstanceIdentifier(item);
        tx.delete(LogicalDatastoreType.CONFIGURATION, iid);
        final CheckedFuture<Void, TransactionCommitFailedException> future = tx.submit();
        future.get();  // Check if it's successfuly committed, otherwise exception will be thrown.
    }

    protected boolean removeMd(T item) {
        final ReadWriteTransaction tx = getDataBroker().newReadWriteTransaction();
        try {
            removeMd(item, tx);
            return true;
        } catch (InterruptedException | ExecutionException e) {
            LOGGER.warn("Transaction failed", e);
        }
        return false;
    }

    protected Uuid toUuid(String uuid) {
        Preconditions.checkNotNull(uuid);
        Uuid result;
        try {
            result = new Uuid(uuid);
        } catch(final IllegalArgumentException e) {
            // OK... someone didn't follow RFC 4122... lets try this the hard way
            final String dedashed = uuid.replace("-", "");
            if(dedashed.length() == DEDASHED_UUID_LENGTH) {
                final String redashed = dedashed.substring(DEDASHED_UUID_START, DEDASHED_UUID_DIV1)
                        + "-"
                        + dedashed.substring(DEDASHED_UUID_DIV1, DEDASHED_UUID_DIV2)
                        + "-"
                        + dedashed.substring(DEDASHED_UUID_DIV2, DEDASHED_UUID_DIV3)
                        + "-"
                        + dedashed.substring(DEDASHED_UUID_DIV3, DEDASHED_UUID_DIV4)
                        + "-"
                        + dedashed.substring(DEDASHED_UUID_DIV4, DEDASHED_UUID_LENGTH);
                result = new Uuid(redashed);
            } else {
                throw e;
            }
        }
        return result;
    }

    // this method uses reflection to update an object from it's delta.

    protected boolean overwrite(Object target, Object delta) {
        final Method[] methods = target.getClass().getMethods();

        for(final Method toMethod: methods){
            if(toMethod.getDeclaringClass().equals(target.getClass())
                    && toMethod.getName().startsWith("set")){

                final String toName = toMethod.getName();
                final String fromName = toName.replace("set", "get");

                try {
                    final Method fromMethod = delta.getClass().getMethod(fromName);
                    final Object value = fromMethod.invoke(delta, (Object[])null);
                    if(value != null){
                        toMethod.invoke(target, value);
                    }
                } catch (final Exception e) {
                    LOGGER.error("Error in overwrite", e);
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void close() throws Exception {
        // TODO Auto-generated method stub

    }

    private boolean exists(String uuid, ReadTransaction tx) {
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
        final Set<S> allNeutronObjects = new HashSet<S>();
        final U dataObjects = readMd(createInstanceIdentifier(), tx);
        if (dataObjects != null) {
            for (final T dataObject: getDataObjectList(dataObjects)) {
                allNeutronObjects.add(fromMd(dataObject));
            }
        }
        LOGGER.debug("Exiting _getAll, Found {} OpenStackFirewall", allNeutronObjects.size());
        final List<S> ans = new ArrayList<S>();
        ans.addAll(allNeutronObjects);
        return ans;
    }

    @Override
    public List<S> getAll() {
        try (ReadOnlyTransaction tx = getDataBroker().newReadOnlyTransaction()) {
            return getAll(tx);
        }
    }

    private boolean add(S input, ReadWriteTransaction tx) throws InterruptedException, ExecutionException {
        Preconditions.checkNotNull(tx);
        if (exists(input.getID(), tx)) {
            tx.cancel();
            return false;
        }
        addMd(input, tx);
        return true;
    }

    @Override
    public boolean add(S input) {
        int retries = RETRY_MAX;
        while (retries-- >= 0) {
            final ReadWriteTransaction tx = getDataBroker().newReadWriteTransaction();
            try {
                return add(input, tx);
            } catch (InterruptedException | ExecutionException e) {
                if (e.getCause() instanceof OptimisticLockFailedException) {
                    LOGGER.warn("Got OptimisticLockFailedException - {} {}", input, retries);
                    continue;
                }
                // TODO: rethrow exception. don't mask exception
                LOGGER.error("Transaction failed", e);
            }
            break;
        }
        return false;
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
                    LOGGER.warn("Got OptimisticLockFailedException - {} {}", uuid, retries);
                    continue;
                }
                // TODO: rethrow exception. don't mask exception
                LOGGER.error("Transaction failed", e);
            }
            break;
        }
        return false;
     }

    private boolean update(String uuid, S delta, ReadWriteTransaction tx) throws InterruptedException, ExecutionException {
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
                    LOGGER.warn("Got OptimisticLockFailedException - {} {} {}", uuid, delta, retries);
                    continue;
                }
                // TODO: rethrow exception. don't mask exception
                LOGGER.error("Transaction failed", e);
            }
            break;
        }
        return false;
    }

    @Override
    public boolean inUse(String uuid) {
        return false;
    }
}
