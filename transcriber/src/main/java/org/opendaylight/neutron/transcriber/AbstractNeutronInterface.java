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
        Preconditions.checkNotNull(db);
        return db;
    }

    public void onTransactionChainFailed(TransactionChain<?, ?> chain, AsyncTransaction<?, ?> transaction, Throwable cause) {
        LOGGER.error("Broken chain {} in TxchainDomWrite, transaction {}, cause {}",
                     chain, transaction.getIdentifier(), cause);
    }

    public void onTransactionChainSuccessful(TransactionChain<?, ?> chain) {
        LOGGER.debug("Chain {} closed successfully", chain);
    }

    protected abstract InstanceIdentifier<T> createInstanceIdentifier(T item);

    protected abstract InstanceIdentifier<U> createInstanceIdentifier();

    protected abstract T toMd(S neutronObject);

    protected abstract T toMd(String uuid);

    protected abstract S fromMd(T dataObject);

    protected <T extends DataObject> T readMd(InstanceIdentifier<T> path) {
        T result = null;
        final ReadOnlyTransaction transaction = getDataBroker().newReadOnlyTransaction();
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

    protected boolean addMd(S neutronObject) {
        return updateMd(neutronObject);
    }

    protected boolean updateMd(S neutronObject) {

        /*
         * retry for transaction conflict.
         * see the comment
         * org.opendaylight.controller.sal.restconf.impl.RestconfImpl#updateConfigurationData
         */
        int retries = 2;
        while (true) {
            WriteTransaction transaction = getDataBroker().newWriteOnlyTransaction();
            T item = toMd(neutronObject);
            InstanceIdentifier<T> iid = createInstanceIdentifier(item);
            transaction.put(LogicalDatastoreType.CONFIGURATION, iid, item, true);
            CheckedFuture<Void, TransactionCommitFailedException> future = transaction.submit();
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                if (e.getCause() instanceof OptimisticLockFailedException) {
                    if (--retries >= 0) {
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

    protected boolean removeMd(T item) {
        WriteTransaction transaction = getDataBroker().newWriteOnlyTransaction();
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

    @Override
    public boolean exists(String uuid) {
        T dataObject = readMd(createInstanceIdentifier(toMd(uuid)));
        return dataObject != null;
    }

    @Override
    public S get(String uuid) {
        T dataObject = readMd(createInstanceIdentifier(toMd(uuid)));
        if (dataObject == null) {
            return null;
        }
        return fromMd(dataObject);
    }

    protected abstract List<T> getDataObjectList(U dataObjects);

    @Override
    public List<S> getAll() {
        Set<S> allNeutronObjects = new HashSet<S>();
        U dataObjects = readMd(createInstanceIdentifier());
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
    public boolean add(S input) {
        if (exists(input.getID())) {
            return false;
        }
        return addMd(input);
    }

    @Override
    public boolean remove(String uuid) {
        if (!exists(uuid)) {
            return false;
        }
        return removeMd(toMd(uuid));
     }

    @Override
    public boolean update(String uuid, S delta) {
        if (!exists(uuid)) {
            return false;
        }
        return updateMd(delta);
    }

    @Override
    public boolean inUse(String uuid) {
        return false;
    }
}
