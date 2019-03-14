/*
 * Copyright (c) 2017 Intel Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.hostconfig.utils;

import com.google.common.base.Throwables;
import java.util.concurrent.ExecutionException;
import org.opendaylight.mdsal.binding.api.DataBroker;
import org.opendaylight.mdsal.binding.api.WriteTransaction;
import org.opendaylight.mdsal.common.api.LogicalDatastoreType;
import org.opendaylight.mdsal.common.api.TransactionCommitFailedException;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.hostconfig.rev150712.hostconfig.attributes.Hostconfigs;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.hostconfig.rev150712.hostconfig.attributes.hostconfigs.Hostconfig;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.hostconfig.rev150712.hostconfig.attributes.hostconfigs.HostconfigBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.rev150712.Neutron;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NeutronHostconfigUtils {
    private static final Logger LOG = LoggerFactory.getLogger(NeutronHostconfigUtils.class);
    private static final InstanceIdentifier<Hostconfigs> HOSTCONFIGS = InstanceIdentifier.builder(Neutron.class)
            .child(Hostconfigs.class).build();

    private final DataBroker dataBroker;

    public enum Action {
        ADD,
        UPDATE,
        DELETE
    }

    public NeutronHostconfigUtils(final DataBroker dataBroker) {
        this.dataBroker = dataBroker;
    }

    public void updateMdsal(Hostconfig hostConfig, Action action) throws TransactionCommitFailedException {
        InstanceIdentifier<Hostconfig> hostConfigId;
        if (hostConfig == null) {
            return;
        }
        switch (action) {
            case ADD:
            case UPDATE:
                final WriteTransaction writeTx = dataBroker.newWriteOnlyTransaction();
                hostConfigId = createInstanceIdentifier(hostConfig);
                writeTx.put(LogicalDatastoreType.OPERATIONAL, hostConfigId, hostConfig, true);
                commit(writeTx);
                LOG.trace("Hostconfig updated for node {}", hostConfig.getHostId());
                break;
            case DELETE:
                final WriteTransaction delTx = dataBroker.newWriteOnlyTransaction();
                hostConfigId = createInstanceIdentifier(hostConfig);
                delTx.delete(LogicalDatastoreType.OPERATIONAL, hostConfigId);
                commit(delTx);
                LOG.trace("Hostconfig deleted for node {}", hostConfig.getHostId());
                break;
            default:
                break;
        }
    }

    public Hostconfig buildHostConfigInfo(String hostId, String hostType, String hostConfig) {
        HostconfigBuilder hostconfigBuilder = new HostconfigBuilder();
        hostconfigBuilder.setHostId(hostId);
        hostconfigBuilder.setHostType(hostType);
        hostconfigBuilder.setConfig(hostConfig);
        return hostconfigBuilder.build();
    }

    private static void commit(WriteTransaction tx) throws TransactionCommitFailedException {
        try {
            tx.commit().get();
        } catch (InterruptedException e) {
            throw new TransactionCommitFailedException("Interrupted while waiting", e);
        } catch (ExecutionException e) {
            LOG.debug("Commit failed", e);
            Throwables.throwIfInstanceOf(e.getCause(), TransactionCommitFailedException.class);
            throw new TransactionCommitFailedException("Commit failed", e);
        }
    }

    private static InstanceIdentifier<Hostconfig> createInstanceIdentifier(Hostconfig hostconfig) {
        return HOSTCONFIGS.child(Hostconfig.class, hostconfig.key());
    }
}
