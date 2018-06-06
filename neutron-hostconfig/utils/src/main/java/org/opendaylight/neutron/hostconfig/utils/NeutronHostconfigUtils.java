/*
 * Copyright (c) 2017 Intel Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.hostconfig.utils;

import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.controller.md.sal.binding.api.WriteTransaction;
import org.opendaylight.controller.md.sal.common.api.data.LogicalDatastoreType;
import org.opendaylight.controller.md.sal.common.api.data.TransactionCommitFailedException;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.hostconfig.rev150712.hostconfig.attributes.Hostconfigs;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.hostconfig.rev150712.hostconfig.attributes.hostconfigs.Hostconfig;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.hostconfig.rev150712.hostconfig.attributes.hostconfigs.HostconfigBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.rev150712.Neutron;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NeutronHostconfigUtils {
    private static final Logger LOG = LoggerFactory.getLogger(NeutronHostconfigUtils.class);
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
                writeTx.submit().checkedGet();
                LOG.trace("Hostconfig updated for node {}", hostConfig.getHostId());
                break;
            case DELETE:
                final WriteTransaction delTx = dataBroker.newWriteOnlyTransaction();
                hostConfigId = createInstanceIdentifier(hostConfig);
                delTx.delete(LogicalDatastoreType.OPERATIONAL, hostConfigId);
                LOG.trace("Hostconfig deleted for node {}", hostConfig.getHostId());
                delTx.submit().checkedGet();
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

    private InstanceIdentifier<Hostconfig> createInstanceIdentifier(Hostconfig hostconfig) {
        return InstanceIdentifier.create(Neutron.class).child(Hostconfigs.class)
                .child(Hostconfig.class, hostconfig.key());
    }
}
