/*
 * Copyright (c) 2017 Intel Corporation.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.hostconfig.vpp;

import com.google.common.base.Preconditions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.controller.md.sal.binding.api.DataObjectModification;
import org.opendaylight.controller.md.sal.binding.api.ClusteredDataTreeChangeListener;
import org.opendaylight.controller.md.sal.binding.api.DataTreeChangeListener;
import org.opendaylight.controller.md.sal.binding.api.DataTreeIdentifier;
import org.opendaylight.controller.md.sal.binding.api.DataTreeModification;
import org.opendaylight.controller.md.sal.common.api.data.LogicalDatastoreType;
import org.opendaylight.neutron.hostconfig.utils.NeutronHostconfigUtils;
import org.opendaylight.yang.gen.v1.urn.opendaylight.netconf.node.topology.rev150114.NetconfNode;
import org.opendaylight.yang.gen.v1.urn.opendaylight.netconf.node.topology.rev150114.NetconfNodeConnectionStatus;
import org.opendaylight.yang.gen.v1.urn.opendaylight.netconf.node.topology.rev150114.netconf.node.connection.status.available.capabilities.AvailableCapability;
import org.opendaylight.yang.gen.v1.urn.tbd.params.xml.ns.yang.network.topology.rev131021.NetworkTopology;
import org.opendaylight.yang.gen.v1.urn.tbd.params.xml.ns.yang.network.topology.rev131021.TopologyId;
import org.opendaylight.yang.gen.v1.urn.tbd.params.xml.ns.yang.network.topology.rev131021.network.topology.Topology;
import org.opendaylight.yang.gen.v1.urn.tbd.params.xml.ns.yang.network.topology.rev131021.network.topology.TopologyKey;
import org.opendaylight.yang.gen.v1.urn.tbd.params.xml.ns.yang.network.topology.rev131021.network.topology.topology.Node;
import org.opendaylight.yangtools.concepts.ListenerRegistration;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NeutronHostconfigVppListener implements ClusteredDataTreeChangeListener<Node> {
    private static final Logger LOG = LoggerFactory.getLogger(NeutronHostconfigVppListener.class);
    private final DataBroker dataBroker;
    private final NeutronHostconfigUtils neutronHostconfig;
    private ListenerRegistration<DataTreeChangeListener<Node>> listenerRegistration;
    private final ExecutorService executorService = Executors.newFixedThreadPool(1);
    
    private static final TopologyId TOPOLOGY_NETCONF = new TopologyId("topology-netconf");
    private static final String V3PO_CAPABILITY = "(urn:opendaylight:params:xml:ns:yang:v3po?revision=2016-12-14)v3po";
    private static final String INTERFACES_CAPABILITY = "(urn:ietf:params:xml:ns:yang:ietf-interfaces?revision=2014-05-08)ietf-interfaces";
    private final List<String> requiredCapabilities;
    private SocketInfo socketInfo;

    public NeutronHostconfigVppListener(final DataBroker dataBroker, String sPath, String sName) {
        LOG.info("Initializing Neutron-Hostconfig-Vpp-Listener");
        this.dataBroker = Preconditions.checkNotNull(dataBroker);
        this.socketInfo = new SocketInfo(Preconditions.checkNotNull(sPath), Preconditions.checkNotNull(sName));
        this.neutronHostconfig = new NeutronHostconfigUtils(dataBroker);
        requiredCapabilities = new ArrayList<>();
        requiredCapabilities.add(V3PO_CAPABILITY);
        requiredCapabilities.add(INTERFACES_CAPABILITY);
    }

    private void processChanges(Collection<DataTreeModification<Node>> changes) {
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
                    break;
            }
        }
    }

    @Override
    public void onDataTreeChanged(@Nonnull Collection<DataTreeModification<Node>> changes) {
        Preconditions.checkNotNull(changes, "Changes may not be null!");
        executorService.submit(() -> processChanges(changes));
    }

    private InstanceIdentifier<Node> createNodeIdentifier() {
        return InstanceIdentifier.builder(NetworkTopology.class)
                    .child(Topology.class, new TopologyKey(TOPOLOGY_NETCONF))
                    .child(Node.class)
                    .build();
        }

    public void init() {
        LOG.info("{} start", getClass().getSimpleName());
        DataTreeIdentifier<Node> dataTreeIdentifier =
                new DataTreeIdentifier<>(LogicalDatastoreType.OPERATIONAL, createNodeIdentifier());
        LOG.info("Neutron Manager Qos Policy DataChange listener registration {}", dataTreeIdentifier);
        listenerRegistration =
                dataBroker.registerDataTreeChangeListener(dataTreeIdentifier, NeutronHostconfigVppListener.this);
    }

    public void close() throws Exception {
        if (listenerRegistration != null) {
            listenerRegistration.close();
            LOG.trace("HostConfig listener Closed");
        }
    }

    private void updateHostConfig(Node node, NeutronHostconfigUtils.Action action) {
        boolean isValidVppNode = validateVppNode(node);
        if (!isValidVppNode) {
            return;
        }
        for (Map.Entry<String,String> entry :  HostconfigUtil.createHostconfigsDataFor(node.getNodeId(), socketInfo).entrySet()) {
            neutronHostconfig.updateMdsal(neutronHostconfig.buildHostConfigInfo(node.getNodeId().getValue(), entry.getKey(),
                    entry.getValue()), action);
        }
    }

    private boolean validateVppNode(Node node) {
        LOG.info("Registering new node {}", node.getNodeId().getValue());
        NetconfNode netconfNode = getNodeAugmentation(node);
        if (netconfNode == null) {
            return false;
        }
        NetconfNodeConnectionStatus.ConnectionStatus connectionStatus = netconfNode.getConnectionStatus();
        switch (connectionStatus) {
            case Connecting:
                LOG.info("Connecting device {} ...", node.getNodeId().getValue());
                break;
            case Connected:
                if (netconfNode.getAvailableCapabilities() == null
                        || netconfNode.getAvailableCapabilities().getAvailableCapability() == null
                        || netconfNode.getAvailableCapabilities().getAvailableCapability().isEmpty()) {
                    LOG.warn("Node {} does not contain any capabilities", node.getNodeId().getValue());
                    return false;
                }
                if (!capabilityCheck(netconfNode.getAvailableCapabilities().getAvailableCapability())) {
                    LOG.warn("Node {} does not contain all capabilities required by vpp-renderer",
                            node.getNodeId().getValue());
                    return false;
                }
                return true;
            default:
                return false;
        }
        return false;
    }

    private boolean capabilityCheck(final List<AvailableCapability> capabilities) {
        final List<String> availableCapabilities = capabilities.stream()
                .map(AvailableCapability::getCapability)
                .collect(Collectors.toList());
        return requiredCapabilities.stream()
            .allMatch(availableCapabilities::contains);
    }


    private NetconfNode getNodeAugmentation(Node node) {
        NetconfNode netconfNode = node.getAugmentation(NetconfNode.class);
        if (netconfNode == null) {
            LOG.warn("Node {} is not a netconf device", node.getNodeId().getValue());
            return null;
        }
        return netconfNode;
    }
}
