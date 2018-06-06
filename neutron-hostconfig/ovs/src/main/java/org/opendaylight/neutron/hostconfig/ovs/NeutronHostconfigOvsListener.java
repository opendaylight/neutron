/*
 * Copyright (c) 2017 Intel Corporation.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.hostconfig.ovs;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.opendaylight.controller.md.sal.binding.api.ClusteredDataTreeChangeListener;
import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.controller.md.sal.binding.api.DataObjectModification;
import org.opendaylight.controller.md.sal.binding.api.DataTreeChangeListener;
import org.opendaylight.controller.md.sal.binding.api.DataTreeIdentifier;
import org.opendaylight.controller.md.sal.binding.api.DataTreeModification;
import org.opendaylight.controller.md.sal.common.api.data.LogicalDatastoreType;
import org.opendaylight.controller.md.sal.common.api.data.TransactionCommitFailedException;
import org.opendaylight.neutron.hostconfig.utils.NeutronHostconfigUtils;
import org.opendaylight.ovsdb.utils.mdsal.utils.MdsalUtils;
import org.opendaylight.ovsdb.utils.southbound.utils.SouthboundUtils;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.ovsdb.rev150105.OvsdbNodeAugmentation;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.ovsdb.rev150105.ovsdb.node.attributes.OpenvswitchExternalIds;
import org.opendaylight.yang.gen.v1.urn.tbd.params.xml.ns.yang.network.topology.rev131021.NetworkTopology;
import org.opendaylight.yang.gen.v1.urn.tbd.params.xml.ns.yang.network.topology.rev131021.network.topology.Topology;
import org.opendaylight.yang.gen.v1.urn.tbd.params.xml.ns.yang.network.topology.rev131021.network.topology.TopologyKey;
import org.opendaylight.yang.gen.v1.urn.tbd.params.xml.ns.yang.network.topology.rev131021.network.topology.topology.Node;
import org.opendaylight.yangtools.concepts.ListenerRegistration;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class NeutronHostconfigOvsListener implements ClusteredDataTreeChangeListener<Node> {
    private static final Logger LOG = LoggerFactory.getLogger(NeutronHostconfigOvsListener.class);
    private final DataBroker dataBroker;
    private final SouthboundUtils southboundUtils;
    private final NeutronHostconfigUtils neutronHostconfig;
    private ListenerRegistration<DataTreeChangeListener<Node>> listenerRegistration;
    private static final String OS_HOST_CONFIG_HOST_ID_KEY = "odl_os_hostconfig_hostid";
    private static final String OS_HOST_CONFIG_CONFIG_KEY_PREFIX = "odl_os_hostconfig_config_odl_";
    private static int HOST_TYPE_STR_LEN = 8;

    @Inject
    public NeutronHostconfigOvsListener(final DataBroker dataBroker) {
        this.dataBroker = dataBroker;
        MdsalUtils mdsalUtils = new MdsalUtils(dataBroker);
        this.southboundUtils = new SouthboundUtils(mdsalUtils);
        this.neutronHostconfig = new NeutronHostconfigUtils(dataBroker);
    }

    private void processChanges(Collection<DataTreeModification<Node>> changes)
            throws TransactionCommitFailedException {
        LOG.info("onDataTreeChanged: Received Data Tree Changed ...", changes);
        for (DataTreeModification<Node> change : changes) {
            final InstanceIdentifier<Node> key = change.getRootPath().getRootIdentifier();
            final DataObjectModification<Node> mod = change.getRootNode();
            LOG.info("onDataTreeChanged: Received Data Tree Changed Update of Type={} for Key={}",
                    mod.getModificationType(), key);
            switch (mod.getModificationType()) {
                case DELETE:
                    updateHostConfig(mod.getDataAfter(), NeutronHostconfigUtils.Action.DELETE);
                    break;
                case SUBTREE_MODIFIED:
                    updateHostConfig(mod.getDataAfter(), NeutronHostconfigUtils.Action.UPDATE);
                    break;
                case WRITE:
                    updateHostConfig(mod.getDataAfter(), NeutronHostconfigUtils.Action.ADD);
                    break;
                default:
                    LOG.error("onDataTreeChanged: Invalid modification type={}",
                            mod.getModificationType());
                    break;
            }
        }
    }

    @Override
    public void onDataTreeChanged(@Nonnull Collection<DataTreeModification<Node>> changes) {
        Preconditions.checkNotNull(changes, "Changes may not be null!");
        try {
            processChanges(changes);
        } catch (TransactionCommitFailedException e) {
            LOG.error("Transaction commit failed; ignorining changes: ", changes, e);
        }
    }

    private InstanceIdentifier<Node> createNodeIdentifier() {
        return InstanceIdentifier
                .create(NetworkTopology.class)
                .child(Topology.class, new TopologyKey(SouthboundUtils.OVSDB_TOPOLOGY_ID))
                .child(Node.class);
    }

    @PostConstruct
    public void init() {
        LOG.info("{} start", getClass().getSimpleName());
        DataTreeIdentifier<Node> dataTreeIdentifier =
                new DataTreeIdentifier<>(LogicalDatastoreType.OPERATIONAL, createNodeIdentifier());
        LOG.info("Neutron Hostconfig DataChange listener registration {}", dataTreeIdentifier);
        listenerRegistration = dataBroker.registerDataTreeChangeListener(dataTreeIdentifier, this);
    }

    @PreDestroy
    public void close() throws Exception {
        if (listenerRegistration != null) {
            listenerRegistration.close();
            LOG.trace("HostConfig listener Closed");
        }
    }

    private void updateHostConfig(Node node, NeutronHostconfigUtils.Action action)
            throws TransactionCommitFailedException {
        String hostId = getExternalId(node, OS_HOST_CONFIG_HOST_ID_KEY);
        if (hostId == null) {
            return;
        }
        for (Map.Entry<String, String> entry : extractHostConfig(node).entrySet()) {
            neutronHostconfig.updateMdsal(neutronHostconfig.buildHostConfigInfo(hostId, entry.getKey(),
                    entry.getValue()), action);
        }
    }

    private Map<String, String> extractHostConfig(Node node) {
        Map<String, String> config = Maps.newHashMap();
        OvsdbNodeAugmentation ovsdbNode = getOvsdbNodeAugmentation(node);
        if (ovsdbNode != null && ovsdbNode.getOpenvswitchExternalIds() != null) {
            for (OpenvswitchExternalIds openvswitchExternalIds : ovsdbNode.getOpenvswitchExternalIds()) {
                if (openvswitchExternalIds.getExternalIdKey().startsWith(OS_HOST_CONFIG_CONFIG_KEY_PREFIX)) {
                    // Extract the host type. Max 8 characters after
                    // suffix OS_HOST_CONFIG_CONFIG_KEY_PREFIX.length()
                    String hostType = openvswitchExternalIds.getExternalIdKey().substring(
                            OS_HOST_CONFIG_CONFIG_KEY_PREFIX.length());
                    if (hostType.length() > 0) {
                        if (hostType.length() > HOST_TYPE_STR_LEN) {
                            hostType = hostType.substring(0, HOST_TYPE_STR_LEN);
                        }
                        hostType = "ODL " + hostType.toUpperCase(Locale.ROOT);
                        if (null != openvswitchExternalIds.getExternalIdValue()) {
                            config.put(hostType, openvswitchExternalIds.getExternalIdValue());
                        }
                    }
                }
            }
        }
        return config;
    }

    private String getExternalId(Node node, String key) {
        OvsdbNodeAugmentation ovsdbNode = getOvsdbNodeAugmentation(node);
        if (ovsdbNode != null && ovsdbNode.getOpenvswitchExternalIds() != null) {
            for (OpenvswitchExternalIds openvswitchExternalIds : ovsdbNode.getOpenvswitchExternalIds()) {
                if (openvswitchExternalIds.getExternalIdKey().equals(key)) {
                    return openvswitchExternalIds.getExternalIdValue();
                }
            }
        }
        return null;
    }

    private OvsdbNodeAugmentation getOvsdbNodeAugmentation(Node node) {
        OvsdbNodeAugmentation ovsdbNode = southboundUtils.extractOvsdbNode(node);
        if (ovsdbNode == null) {
            Node nodeFromReadOvsdbNode = southboundUtils.readOvsdbNode(node);
            if (nodeFromReadOvsdbNode != null) {
                ovsdbNode = southboundUtils.extractOvsdbNode(nodeFromReadOvsdbNode);
            }
        }
        return ovsdbNode;
    }
}
