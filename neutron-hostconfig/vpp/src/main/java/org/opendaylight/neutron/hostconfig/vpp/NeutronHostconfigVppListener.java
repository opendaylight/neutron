/*
 * Copyright (c) 2017 Intel Corporation. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.hostconfig.vpp;

import com.google.common.base.Preconditions;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import org.opendaylight.controller.md.sal.binding.api.ClusteredDataTreeChangeListener;
import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.controller.md.sal.binding.api.DataObjectModification;
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
import org.opendaylight.yangtools.yang.common.QName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NeutronHostconfigVppListener implements ClusteredDataTreeChangeListener<Node> {

    private static final Logger LOG = LoggerFactory.getLogger(NeutronHostconfigVppListener.class);
    private final DataBroker dataBroker;
    private final NeutronHostconfigUtils neutronHostconfig;
    private ListenerRegistration<DataTreeChangeListener<Node>> listenerRegistration;
    private final ExecutorService executorService = Executors.newFixedThreadPool(1);

    private static final TopologyId TOPOLOGY_NETCONF = new TopologyId("topology-netconf");
    private static final QName V3PO_CAPABILITY = QName.create(URI.create("urn:opendaylight:params:xml:ns:yang:v3po"),
            NeutronHostconfigUtils.parseDate("2016-12-14"), "v3po");
    private static final QName INTERFACES_CAPABILITY =
            QName.create(URI.create("urn:ietf:params:xml:ns:yang:ietf-interfaces"),
                    NeutronHostconfigUtils.parseDate("2014-05-08"), "ietf-interfaces");
    private final List<QName> requiredCapabilities;
    private SocketInfo socketInfo;

    public NeutronHostconfigVppListener(final DataBroker dataBroker, String spath, String sname, String vhostMode) {
        LOG.info("Initializing Neutron-Hostconfig-Vpp-Listener");
        this.dataBroker = Preconditions.checkNotNull(dataBroker);
        vhostMode = Preconditions.checkNotNull(vhostMode).toLowerCase();
        Preconditions.checkArgument(vhostMode.equals("server") || vhostMode.equals("client"),
                "Supported values for vhostuser-mode are client and server.");
        this.socketInfo =
                new SocketInfo(Preconditions.checkNotNull(spath), Preconditions.checkNotNull(sname), vhostMode);
        this.neutronHostconfig = new NeutronHostconfigUtils(dataBroker);
        requiredCapabilities = new ArrayList<>();
        requiredCapabilities.add(V3PO_CAPABILITY);
        requiredCapabilities.add(INTERFACES_CAPABILITY);
    }

    @Override
    public void onDataTreeChanged(@Nonnull Collection<DataTreeModification<Node>> changes) {
        LOG.info("onDataTreeChanged: Received Data Tree Changed ...", changes);
        executorService.submit(() -> {
            for (DataTreeModification<Node> change : Preconditions.checkNotNull(changes, "Changes may not be null!")) {
                final InstanceIdentifier<Node> key = change.getRootPath().getRootIdentifier();
                final DataObjectModification<Node> mod = change.getRootNode();
                LOG.info("onDataTreeChanged: Received Data Tree Changed Update of Type={} for Key={}",
                        mod.getModificationType(), key);
                switch (mod.getModificationType()) {
                    case DELETE:
                        updateHostConfig(mod.getDataBefore(), NeutronHostconfigUtils.Action.DELETE);
                        break;
                    case SUBTREE_MODIFIED:
                        updateHostConfig(mod.getDataAfter(), NeutronHostconfigUtils.Action.UPDATE);
                        break;
                    case WRITE:
                        updateHostConfig(mod.getDataAfter(), NeutronHostconfigUtils.Action.ADD);
                        break;
                    default:
                }
            }
        });
    }

    public void init() {
        LOG.info("Initializing {}", getClass().getSimpleName());
        DataTreeIdentifier<Node> dataTreeIdentifier = new DataTreeIdentifier<>(LogicalDatastoreType.OPERATIONAL,
                InstanceIdentifier.builder(NetworkTopology.class)
                    .child(Topology.class, new TopologyKey(TOPOLOGY_NETCONF))
                    .child(Node.class)
                    .build());
        listenerRegistration =
                dataBroker.registerDataTreeChangeListener(dataTreeIdentifier, NeutronHostconfigVppListener.this);
        LOG.info("Registered listener to netconf nodes {}.", dataTreeIdentifier.getRootIdentifier());
    }

    private void updateHostConfig(Node node, NeutronHostconfigUtils.Action action) {
        boolean isValidVppNode = validateVppNode(node);
        if (!isValidVppNode) {
            return;
        }
        for (Map.Entry<String, String> entry : HostconfigUtil.createHostconfigsDataFor(node.getNodeId(), socketInfo)
            .entrySet()) {
            neutronHostconfig.updateMdsal(neutronHostconfig.buildHostConfigInfo(node.getNodeId().getValue(),
                    entry.getKey(), entry.getValue()), action);
            LOG.info("Updated hostconfig for node {}. Action: {}.", node.getKey(), action);
        }
    }

    private boolean validateVppNode(Node node) {
        LOG.info("Registering new node {}", node.getNodeId().getValue());
        NetconfNode netconfNode = node.getAugmentation(NetconfNode.class);
        if (netconfNode == null) {
            LOG.warn("Node {} is not a netconf device", node.getNodeId().getValue());
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
                    break;
                }
                if (!capabilityCheck(netconfNode.getAvailableCapabilities().getAvailableCapability())) {
                    LOG.warn("Node {} does not contain all capabilities required by vpp-renderer",
                            node.getNodeId().getValue());
                    break;
                }
                LOG.info("VPP node connected {}", node.getNodeId().getValue());
                return true;
            case UnableToConnect:
                LOG.warn("Unable to connect to node {}.", node.getNodeId().getValue());
                break;
            default:
        }
        return false;
    }

    private boolean capabilityCheck(final List<AvailableCapability> capabilities) {
        final List<String> availableCapabilities =
                capabilities.stream().map(AvailableCapability::getCapability).collect(Collectors.toList());
        return requiredCapabilities.stream().map(QName::toString).allMatch(availableCapabilities::contains);
    }

    public void close() throws Exception {
        if (listenerRegistration != null) {
            listenerRegistration.close();
            LOG.info("HostConfig listener Closed");
        }
    }
}
