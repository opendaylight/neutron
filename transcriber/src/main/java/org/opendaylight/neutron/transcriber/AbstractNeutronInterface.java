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

    public BindingTransactionChain createTransactionChain() {
        return getDataBroker().createTransactionChain(this);
    }

    protected interface Action0<U> {
        public U action(BindingTransactionChain chain);
    }

    protected <U> U chainWrapper0(BindingTransactionChain chain,
                                  Action0<U> action) {
        if (chain != null) {
            return action.action(chain);
        }
        try (BindingTransactionChain newChain = this.createTransactionChain()) {
            return action.action(newChain);
        }
    }

    protected interface Action1<U, V> {
        public U action(V input, BindingTransactionChain chain);
    }

    protected <U, V> U chainWrapper1(V input, BindingTransactionChain chain,
                                     Action1<U, V> action) {
        if (chain != null) {
            return action.action(input, chain);
        }
        try (BindingTransactionChain newChain = this.createTransactionChain()) {
            return action.action(input, newChain);
        }
    }

    protected interface Action2<U, V, W> {
        public U action(V input0, W input1, BindingTransactionChain chain);
    }

    protected <U, V, W> U chainWrapper2(V input0, W input1,
                                        BindingTransactionChain chain,
                                        Action2<U, V, W> action) {
        if (chain != null) {
            return action.action(input0, input1, chain);
        }
        try (BindingTransactionChain newChain = this.createTransactionChain()) {
            return action.action(input0, input1, newChain);
        }
    }

    protected abstract InstanceIdentifier<T> createInstanceIdentifier(T item);

    protected abstract InstanceIdentifier<U> createInstanceIdentifier();

    protected abstract T toMd(S neutronObject);

    protected abstract T toMd(String uuid);

    protected abstract S fromMd(T dataObject);

    private <T extends DataObject> T _readMd(InstanceIdentifier<T> path, BindingTransactionChain chain) {
        Preconditions.checkNotNull(chain);
        T result = null;
        final ReadOnlyTransaction transaction = chain.newReadOnlyTransaction();
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

    protected <T extends DataObject> T readMd(InstanceIdentifier<T> path, BindingTransactionChain chain) {
        return chainWrapper1(path, chain,
                             new Action1<T, InstanceIdentifier<T>>() {
                                 @Override
                                 public T action(InstanceIdentifier<T> path, BindingTransactionChain chain) {
                                     return _readMd(path, chain);
                                 }
                             });
    }

    protected <T extends DataObject> T readMd(InstanceIdentifier<T> path) {
        return readMd(path, null);
    }

    protected boolean addMd(S neutronObject, BindingTransactionChain chain) {
        // TODO think about adding existence logic
        return updateMd(neutronObject, chain);
    }

    protected boolean addMd(S neutronObject) {
        return addMd(neutronObject, null);
    }

   protected boolean _updateMd(S neutronObject, BindingTransactionChain chain) {
        Preconditions.checkNotNull(chain);

        /*
         * retry for transaction conflict.
         * see the comment
         * org.opendaylight.controller.sal.restconf.impl.RestconfImpl#updateConfigurationData
         */
        int retries = 2;
        while (true) {
            WriteTransaction transaction = chain.newWriteOnlyTransaction();
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

    protected boolean updateMd(S neutronObject, BindingTransactionChain chain) {
        return chainWrapper1(neutronObject, chain,
                             new Action1<Boolean, S>() {
                                 @Override
                                 public Boolean action(S neutronObject, BindingTransactionChain chain) {
                                     return _updateMd(neutronObject, chain);
                                 }
                             }).booleanValue();
    }

    protected boolean updateMd(S neutronObject) {
        return updateMd(neutronObject, null);
    }

    private boolean _removeMd(T item, BindingTransactionChain chain) {
        Preconditions.checkNotNull(chain);
        WriteTransaction transaction = chain.newWriteOnlyTransaction();
        InstanceIdentifier<T> iid = createInstanceIdentifier(item);
        transaction.delete(LogicalDatastoreType.CONFIGURATION, iid);
        CheckedFuture<Void, TransactionCommitFailedException> future = transaction.submit();
        try {
            future.get();
        } catch (InterruptedException | ExecutionException e) {
            LOGGER.warn("Transation failed ",e);
            return false;
        }
        return true;
    }

    protected boolean removeMd(T item, BindingTransactionChain chain) {
        return chainWrapper1(item, chain,
                             new Action1<Boolean, T>() {
                                 @Override
                                 public Boolean action(T item, BindingTransactionChain chain) {
                                     return _removeMd(item, chain);
                                 }
                             }).booleanValue();
    }

    protected boolean removeMd(T item) {
        return removeMd(item, null);
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

    protected boolean _exists(String uuid, BindingTransactionChain chain) {
        Preconditions.checkNotNull(chain);
        T dataObject = readMd(createInstanceIdentifier(toMd(uuid)), chain);
        return dataObject != null;
    }

    @Override
    public boolean exists(String uuid, BindingTransactionChain chain) {
        return chainWrapper1(uuid, chain,
                             new Action1<Boolean, String>() {
                                 @Override
                                 public Boolean action(String uuid, BindingTransactionChain chain) {
                                     return _exists(uuid, chain);
                                 }
                             }).booleanValue();
    }

    protected S _get(String uuid, BindingTransactionChain chain) {
        Preconditions.checkNotNull(chain);
        T dataObject = readMd(createInstanceIdentifier(toMd(uuid)), chain);
        if (dataObject == null) {
            return null;
        }
        return fromMd(dataObject);
    }

    @Override
    public S get(String uuid, BindingTransactionChain chain) {
        return chainWrapper1(uuid, chain,
                             new Action1<S, String>() {
                                 @Override
                                 public S action(String uuid, BindingTransactionChain chain) {
                                     return _get(uuid, chain);
                                 }
                             });
    }

    protected abstract List<T> getDataObjectList(U dataObjects);

    protected List<S> _getAll(BindingTransactionChain chain) {
        Preconditions.checkNotNull(chain);

        Set<S> allNeutronObjects = new HashSet<S>();
        U dataObjects = readMd(createInstanceIdentifier(), chain);
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
    public List<S> getAll(BindingTransactionChain chain) {
        return chainWrapper0(chain,
                             new Action0<List<S>>() {
                                 @Override
                                 public List<S> action(BindingTransactionChain chain) {
                                     return _getAll(chain);
                                 }
                             });
    }

    protected boolean _add(S input, BindingTransactionChain chain) {
        Preconditions.checkNotNull(chain);
        if (exists(input.getID(), chain)) {
            return false;
        }
        addMd(input, chain);
        return true;
    }

    @Override
    public boolean add(S input, BindingTransactionChain chain) {
        return chainWrapper1(input, chain,
                             new Action1<Boolean, S>() {
                                 @Override
                                 public Boolean action(S input, BindingTransactionChain chain) {
                                     return _add(input, chain);
                                 }
                             }).booleanValue();
    }

    protected boolean _remove(String uuid, BindingTransactionChain chain) {
        Preconditions.checkNotNull(chain);
        if (!exists(uuid, chain)) {
            return false;
        }
        return removeMd(toMd(uuid), chain);
    }

    @Override
    public boolean remove(String uuid, BindingTransactionChain chain) {
        return chainWrapper1(uuid, chain,
                             new Action1<Boolean, String>() {
                                 @Override
                                 public Boolean action(String uuid, BindingTransactionChain chain) {
                                     return _remove(uuid, chain);
                                 }
                             }).booleanValue();
    }

    protected boolean _update(String uuid, S delta, BindingTransactionChain chain) {
        Preconditions.checkNotNull(chain);
        if (!exists(uuid, chain)) {
            return false;
        }
        updateMd(delta, chain);
        return true;
    }

    @Override
    public boolean update(String uuid, S delta, BindingTransactionChain chain) {
        return chainWrapper2(uuid, delta, chain,
                             new Action2<Boolean, String, S>() {
                                 @Override
                                 public Boolean action(String uuid, S delta, BindingTransactionChain chain) {
                                     return _update(uuid, delta, chain);
                                 }
                             }).booleanValue();
    }

    @Override
    public boolean inUse(String uuid, BindingTransactionChain chain) {
        return false;
    }
}
