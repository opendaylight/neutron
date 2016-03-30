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

import org.opendaylight.controller.md.sal.binding.api.BindingTransactionChain;
import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.controller.md.sal.binding.api.ReadOnlyTransaction;
import org.opendaylight.controller.md.sal.binding.api.WriteTransaction;
import org.opendaylight.controller.md.sal.common.api.data.AsyncTransaction;
import org.opendaylight.controller.md.sal.common.api.data.LogicalDatastoreType;
import org.opendaylight.controller.md.sal.common.api.data.OptimisticLockFailedException;
import org.opendaylight.controller.md.sal.common.api.data.ReadFailedException;
import org.opendaylight.controller.md.sal.common.api.data.TransactionChain;
import org.opendaylight.controller.md.sal.common.api.data.TransactionChainListener;
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

public abstract class AbstractNeutronInterface<T extends DataObject, U extends ChildOf<? extends DataObject> & Augmentable<U>, S extends INeutronObject> implements AutoCloseable, INeutronCRUD<S>, TransactionChainListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractNeutronInterface.class);
    private static final int DEDASHED_UUID_LENGTH = 32;
    private static final int DEDASHED_UUID_START = 0;
    private static final int DEDASHED_UUID_DIV1 = 8;
    private static final int DEDASHED_UUID_DIV2 = 12;
    private static final int DEDASHED_UUID_DIV3 = 16;
    private static final int DEDASHED_UUID_DIV4 = 20;

    private DataBroker db;

    AbstractNeutronInterface(ProviderContext providerContext) {
        this.db = providerContext.getSALService(DataBroker.class);
    }

    public DataBroker getDataBroker() {
        return db;
    }

    public void onTransactionChainFailed(TransactionChain<?, ?> chain, AsyncTransaction<?, ?> transaction, Throwable cause) {
        LOGGER.error("Broken chain {} in TxchainDomWrite, transaction {}, cause {}",
                     chain, transaction.getIdentifier(), cause);
    }

    public void onTransactionChainSuccessful(TransactionChain<?, ?> chain) {
        LOGGER.debug("Chain {} closed successfully", chain);
    }

    protected BindingTransactionChain createTransactionChain() {
        return getDataBroker().createTransactionChain(this);
    }

    protected interface Action0<U> {
        public U action(DataBroker db);
    }

    protected <U> U txWrapper0(Action0<U> action) {
        return action.action(getDataBroker());
    }

    protected interface Action1<U, V> {
        public U action(V input, DataBroker db);
    }

    protected <U, V> U txWrapper1(V input, Action1<U, V> action) {
        return action.action(input, getDataBroker());
    }

    protected interface Action2<U, V, W> {
        public U action(V input0, W input1, DataBroker db);
    }

    protected <U, V, W> U txWrapper2(V input0, W input1,
                                     Action2<U, V, W> action) {
        return action.action(input0, input1, getDataBroker());
    }

    protected abstract InstanceIdentifier<T> createInstanceIdentifier(T item);

    protected abstract InstanceIdentifier<U> createInstanceIdentifier();

    protected abstract T toMd(S neutronObject);

    protected abstract T toMd(String uuid);

    protected abstract S fromMd(T dataObject);

    protected <T extends DataObject> T readMd(InstanceIdentifier<T> path, DataBroker db) {
        Preconditions.checkNotNull(db);
        T result = null;
        final ReadOnlyTransaction transaction = db.newReadOnlyTransaction();
        CheckedFuture<Optional<T>, ReadFailedException> future = transaction.read(LogicalDatastoreType.CONFIGURATION, path);
        if (future != null) {
            Optional<T> optional;
            try {
                optional = future.checkedGet();
                if (optional.isPresent()) {
                    result = optional.get();
                }
            } catch (ReadFailedException e) {
                LOGGER.warn("Failed to read {}", path, e);
            }
        }
        transaction.close();
        return result;
    }

    protected <T extends DataObject> T readMd(InstanceIdentifier<T> path) {
        return txWrapper1(path,
                             new Action1<T, InstanceIdentifier<T>>() {
                                 @Override
                                 public T action(InstanceIdentifier<T> path, DataBroker db) {
                                     return readMd(path, db);
                                 }
                             });
    }

    protected boolean addMd(S neutronObject, DataBroker db) {
        // TODO think about adding existence logic
        return updateMd(neutronObject, db);
    }

    protected boolean addMd(S neutronObject) {
        return txWrapper1(neutronObject,
                             new Action1<Boolean, S>() {
                                 @Override
                                 public Boolean action(S path, DataBroker db) {
                                     return addMd(path, db);
                                 }
                             }).booleanValue();
    }

    protected boolean updateMd(S neutronObject, DataBroker db) {
        Preconditions.checkNotNull(db);

        /*
         * retry for transaction conflict.
         * see the comment
         * org.opendaylight.controller.sal.restconf.impl.RestconfImpl#updateConfigurationData
         */
        int retries = 2;
        while (true) {
            WriteTransaction transaction = db.newWriteOnlyTransaction();
            T item = toMd(neutronObject);
            InstanceIdentifier<T> iid = createInstanceIdentifier(item);
            transaction.put(LogicalDatastoreType.CONFIGURATION, iid, item, true);
            CheckedFuture<Void, TransactionCommitFailedException> future = transaction.submit();
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                if (e.getCause() instanceof OptimisticLockFailedException) {
                    if(--retries >= 0) {
                        LOGGER.debug("Got OptimisticLockFailedException - trying again {}", neutronObject);
                        continue;
                    }
                    LOGGER.warn("Got OptimisticLockFailedException on last try - failing {}", neutronObject);
                }
                LOGGER.warn("Transation failed ", e);
                return false;
            }
            break;
        }
        return true;
    }

    protected boolean updateMd(S neutronObject) {
        return txWrapper1(neutronObject,
                             new Action1<Boolean, S>() {
                                 @Override
                                 public Boolean action(S neutronObject, DataBroker db) {
                                     return updateMd(neutronObject, db);
                                 }
                             }).booleanValue();
    }

    private boolean removeMd(T item, DataBroker db) {
        Preconditions.checkNotNull(db);
        WriteTransaction transaction = db.newWriteOnlyTransaction();
        InstanceIdentifier<T> iid = createInstanceIdentifier(item);
        transaction.delete(LogicalDatastoreType.CONFIGURATION, iid);
        CheckedFuture<Void, TransactionCommitFailedException> future = transaction.submit();
        try {
            future.get();
        } catch (InterruptedException | ExecutionException e) {
            LOGGER.warn("Transaction failed ",e);
            return false;
        }
        return true;
    }

    protected boolean removeMd(T item) {
        return txWrapper1(item,
                             new Action1<Boolean, T>() {
                                 @Override
                                 public Boolean action(T item, DataBroker db) {
                                     return removeMd(item, db);
                                 }
                             }).booleanValue();
    }

    protected Uuid toUuid(String uuid) {
        Preconditions.checkNotNull(uuid);
        Uuid result;
        try {
            result = new Uuid(uuid);
        } catch(IllegalArgumentException e) {
            // OK... someone didn't follow RFC 4122... lets try this the hard way
            String dedashed = uuid.replace("-", "");
            if(dedashed.length() == DEDASHED_UUID_LENGTH) {
                String redashed = dedashed.substring(DEDASHED_UUID_START, DEDASHED_UUID_DIV1)
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
        Method[] methods = target.getClass().getMethods();

        for(Method toMethod: methods){
            if(toMethod.getDeclaringClass().equals(target.getClass())
                    && toMethod.getName().startsWith("set")){

                String toName = toMethod.getName();
                String fromName = toName.replace("set", "get");

                try {
                    Method fromMethod = delta.getClass().getMethod(fromName);
                    Object value = fromMethod.invoke(delta, (Object[])null);
                    if(value != null){
                        toMethod.invoke(target, value);
                    }
                } catch (Exception e) {
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

    protected boolean exists(String uuid, DataBroker db) {
        Preconditions.checkNotNull(db);
        T dataObject = readMd(createInstanceIdentifier(toMd(uuid)), db);
        return dataObject != null;
    }

    @Override
    public boolean exists(String uuid) {
        return txWrapper1(uuid,
                             new Action1<Boolean, String>() {
                                 @Override
                                 public Boolean action(String uuid, DataBroker db) {
                                     return exists(uuid, db);
                                 }
                             }).booleanValue();
    }

    protected S get(String uuid, DataBroker db) {
        Preconditions.checkNotNull(db);
        T dataObject = readMd(createInstanceIdentifier(toMd(uuid)), db);
        if (dataObject == null) {
            return null;
        }
        return fromMd(dataObject);
    }

    @Override
    public S get(String uuid) {
        return txWrapper1(uuid,
                             new Action1<S, String>() {
                                 @Override
                                 public S action(String uuid, DataBroker db) {
                                     return get(uuid, db);
                                 }
                             });
    }

    protected abstract List<T> getDataObjectList(U dataObjects);

    protected List<S> getAll(DataBroker db) {
        Preconditions.checkNotNull(db);
        Set<S> allNeutronObjects = new HashSet<S>();
        U dataObjects = readMd(createInstanceIdentifier(), db);
        if (dataObjects != null) {
            for (T dataObject: getDataObjectList(dataObjects)) {
                allNeutronObjects.add(fromMd(dataObject));
            }
        }
        LOGGER.debug("Exiting _getAll, Found {} OpenStackFirewall", allNeutronObjects.size());
        List<S> ans = new ArrayList<S>();
        ans.addAll(allNeutronObjects);
        return ans;
    }

    @Override
    public List<S> getAll() {
        return txWrapper0(new Action0<List<S>>() {
                                 @Override
                                 public List<S> action(DataBroker db) {
                                     return getAll(db);
                                 }
                             });
    }

    protected boolean add(S input, DataBroker db) {
        Preconditions.checkNotNull(db);
        if (exists(input.getID(), db)) {
            return false;
        }
        addMd(input, db);
        return true;
    }

    @Override
    public boolean add(S input) {
        return txWrapper1(input,
                             new Action1<Boolean, S>() {
                                 @Override
                                 public Boolean action(S input, DataBroker db) {
                                     return add(input, db);
                                 }
                             }).booleanValue();
    }

    protected boolean remove(String uuid, DataBroker db) {
        Preconditions.checkNotNull(db);
        if (!exists(uuid, db)) {
            return false;
        }
        return removeMd(toMd(uuid), db);
    }

    @Override
    public boolean remove(String uuid) {
        return txWrapper1(uuid,
                             new Action1<Boolean, String>() {
                                 @Override
                                 public Boolean action(String uuid, DataBroker db) {
                                     return remove(uuid, db);
                                 }
                             }).booleanValue();
     }

    protected boolean update(String uuid, S delta, DataBroker db) {
        Preconditions.checkNotNull(db);
        if (!exists(uuid, db)) {
            return false;
        }
        updateMd(delta, db);
        return true;
    }

    @Override
    public boolean update(String uuid, S delta) {
        return txWrapper2(uuid, delta,
                             new Action2<Boolean, String, S>() {
                                 @Override
                                 public Boolean action(String uuid, S delta, DataBroker db) {
                                     return update(uuid, delta, db);
                                 }
                             }).booleanValue();
    }

    @Override
    public boolean inUse(String uuid) {
        return false;
    }
}
