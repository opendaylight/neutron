/*
 * Copyright (c) 2017 Cisco Systems, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.hostconfig.vpp;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.opendaylight.controller.md.sal.binding.api.ReadOnlyTransaction;
import org.opendaylight.controller.md.sal.binding.api.WriteTransaction;
import org.opendaylight.controller.md.sal.common.api.data.AsyncDataBroker.DataChangeScope;
import org.opendaylight.controller.md.sal.common.api.data.LogicalDatastoreType;
import org.opendaylight.yang.gen.v1.urn.opendaylight.netconf.node.topology.rev150114.NetconfNode;
import org.opendaylight.yang.gen.v1.urn.opendaylight.netconf.node.topology.rev150114.NetconfNodeBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.netconf.node.topology.rev150114.NetconfNodeConnectionStatus.ConnectionStatus;
import org.opendaylight.yang.gen.v1.urn.opendaylight.netconf.node.topology.rev150114.netconf.node.connection.status.AvailableCapabilitiesBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.netconf.node.topology.rev150114.netconf.node.connection.status.available.capabilities.AvailableCapability;
import org.opendaylight.yang.gen.v1.urn.opendaylight.netconf.node.topology.rev150114.netconf.node.connection.status.available.capabilities.AvailableCapabilityBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.hostconfig.rev150712.hostconfig.attributes.Hostconfigs;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.hostconfig.rev150712.hostconfig.attributes.hostconfigs.Hostconfig;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.hostconfig.rev150712.hostconfig.attributes.hostconfigs.HostconfigKey;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.rev150712.Neutron;
import org.opendaylight.yang.gen.v1.urn.tbd.params.xml.ns.yang.network.topology.rev131021.NetworkTopology;
import org.opendaylight.yang.gen.v1.urn.tbd.params.xml.ns.yang.network.topology.rev131021.NodeId;
import org.opendaylight.yang.gen.v1.urn.tbd.params.xml.ns.yang.network.topology.rev131021.TopologyId;
import org.opendaylight.yang.gen.v1.urn.tbd.params.xml.ns.yang.network.topology.rev131021.network.topology.Topology;
import org.opendaylight.yang.gen.v1.urn.tbd.params.xml.ns.yang.network.topology.rev131021.network.topology.TopologyKey;
import org.opendaylight.yang.gen.v1.urn.tbd.params.xml.ns.yang.network.topology.rev131021.network.topology.topology.Node;
import org.opendaylight.yang.gen.v1.urn.tbd.params.xml.ns.yang.network.topology.rev131021.network.topology.topology.NodeBuilder;
import org.opendaylight.yang.gen.v1.urn.tbd.params.xml.ns.yang.network.topology.rev131021.network.topology.topology.NodeKey;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;

public class NeutronHostconfigVppListenerTest extends HostconfigsDataBrokerTest {

    private NeutronHostconfigVppListener neutronHostconfigVppListener;
    private static final String V3PO = "(urn:opendaylight:params:xml:ns:yang:v3po?revision=2016-12-14)v3po";
    private static final String INTERFACES =
            "(urn:ietf:params:xml:ns:yang:ietf-interfaces?revision=2014-05-08)ietf-interfaces";
    private static final NodeId NODE_ID = new NodeId("node1");
    private static final String SOCKET_PATH = "/tmp";
    private static final String SOCKET_PREFIX = "socket_";
    private static final String VHOSTUSER_MODE = "server";

    @Before
    public void init() throws InterruptedException, ExecutionException {
        neutronHostconfigVppListener =
                new NeutronHostconfigVppListener(getDataBroker(), SOCKET_PATH, SOCKET_PREFIX, VHOSTUSER_MODE);
        neutronHostconfigVppListener.init();
    }

    @Test
    public void testPutCreateParentsSuccess() throws Exception {
        InstanceIdentifier<Node> iid = InstanceIdentifier.builder(NetworkTopology.class)
            .child(Topology.class, new TopologyKey(new TopologyId("topology-netconf")))
            .child(Node.class, new NodeKey(new NodeId(NODE_ID)))
            .build();
        Node node1 = createNetconfNode(NODE_ID, V3PO, INTERFACES);
        TestListener listener = createListener(LogicalDatastoreType.OPERATIONAL, hostConfigIid(NODE_ID),
                DataChangeScope.BASE);
        WriteTransaction writeTx = getDataBroker().newWriteOnlyTransaction();
        writeTx.put(LogicalDatastoreType.OPERATIONAL, iid, node1, true);
        writeTx.submit().get();
        listener.event();
        ReadOnlyTransaction tx = getDataBroker().newReadOnlyTransaction();
        Assert.assertTrue(tx.read(LogicalDatastoreType.OPERATIONAL, hostConfigIid(NODE_ID)).get().isPresent());
    }

    private InstanceIdentifier<Hostconfig> hostConfigIid(@Nonnull NodeId nodeId) {
        return InstanceIdentifier.builder(Neutron.class)
            .child(Hostconfigs.class)
            .child(Hostconfig.class, new HostconfigKey(nodeId.getValue(), HostconfigUtil.L2_HOST_TYPE))
            .build();
    }

    private Node createNetconfNode(NodeId nodeId, String... capabilities) {
        List<AvailableCapability> caps = Arrays.asList(capabilities)
            .stream()
            .map(name -> new AvailableCapabilityBuilder().setCapability(name).build())
            .collect(Collectors.toList());
        NetconfNode netconfNode = new NetconfNodeBuilder()
            .setConnectionStatus(ConnectionStatus.Connected)
            .setAvailableCapabilities(new AvailableCapabilitiesBuilder().setAvailableCapability(caps).build()).build();
        return new NodeBuilder().setNodeId(nodeId).addAugmentation(NetconfNode.class, netconfNode).build();
    }
}
