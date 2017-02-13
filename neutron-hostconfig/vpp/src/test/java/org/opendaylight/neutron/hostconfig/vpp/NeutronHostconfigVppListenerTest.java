/*
 * Copyright (c) 2017 Cisco Systems, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.hostconfig.vpp;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.opendaylight.controller.md.sal.binding.api.ClusteredDataTreeChangeListener;
import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.controller.md.sal.binding.api.DataObjectModification.ModificationType;
import org.opendaylight.controller.md.sal.binding.api.DataTreeChangeListener;
import org.opendaylight.controller.md.sal.binding.api.DataTreeIdentifier;
import org.opendaylight.controller.md.sal.binding.api.DataTreeModification;
import org.opendaylight.controller.md.sal.binding.api.WriteTransaction;
import org.opendaylight.controller.md.sal.binding.test.AbstractDataBrokerTest;
import org.opendaylight.controller.md.sal.common.api.data.LogicalDatastoreType;
import org.opendaylight.yang.gen.v1.urn.opendaylight.netconf.node.topology.rev150114.NetconfNode;
import org.opendaylight.yang.gen.v1.urn.opendaylight.netconf.node.topology.rev150114.NetconfNodeBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.netconf.node.topology.rev150114.netconf.node.connection.status.AvailableCapabilitiesBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.netconf.node.topology.rev150114.netconf.node.connection.status.available.capabilities.AvailableCapability;
import org.opendaylight.yang.gen.v1.urn.opendaylight.netconf.node.topology.rev150114.netconf.node.connection.status.available.capabilities.AvailableCapabilityBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.hostconfig.rev150712.hostconfig.attributes.Hostconfigs;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.hostconfig.rev150712.hostconfig.attributes.hostconfigs.Hostconfig;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.rev150712.Neutron;
import org.opendaylight.yang.gen.v1.urn.tbd.params.xml.ns.yang.network.topology.rev131021.NetworkTopology;
import org.opendaylight.yang.gen.v1.urn.tbd.params.xml.ns.yang.network.topology.rev131021.NodeId;
import org.opendaylight.yang.gen.v1.urn.tbd.params.xml.ns.yang.network.topology.rev131021.TopologyId;
import org.opendaylight.yang.gen.v1.urn.tbd.params.xml.ns.yang.network.topology.rev131021.network.topology.Topology;
import org.opendaylight.yang.gen.v1.urn.tbd.params.xml.ns.yang.network.topology.rev131021.network.topology.TopologyKey;
import org.opendaylight.yang.gen.v1.urn.tbd.params.xml.ns.yang.network.topology.rev131021.network.topology.topology.Node;
import org.opendaylight.yang.gen.v1.urn.tbd.params.xml.ns.yang.network.topology.rev131021.network.topology.topology.NodeBuilder;
import org.opendaylight.yang.gen.v1.urn.tbd.params.xml.ns.yang.network.topology.rev131021.network.topology.topology.NodeKey;
import org.opendaylight.yangtools.concepts.ListenerRegistration;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;

public class NeutronHostconfigVppListenerTest extends HostconfigsDataBrokerTest implements ClusteredDataTreeChangeListener<Hostconfig>, AutoCloseable {

    private NeutronHostconfigVppListener nl;
    private DataBroker db;
    private ListenerRegistration<DataTreeChangeListener<Hostconfig>> listenerRegistration;
    private static final String V3PO = "(urn:opendaylight:params:xml:ns:yang:v3po?revision=2016-12-14)v3po";
    private static final String INTERFACES =
            "(urn:ietf:params:xml:ns:yang:ietf-interfaces?revision=2014-05-08)ietf-interfaces";
    private static final NodeId NODE_ID = new NodeId("node1");
    private static final String SOCKET_PATH = "/tmp";
    private static final String SOCKET_PREFIX = "socket_";

    public NeutronHostconfigVppListenerTest() {
    }

    @Before
    public void init() {
        db = getDataBroker();
        System.out.println("DataBroker:  " + db);
        nl = new NeutronHostconfigVppListener(db, SOCKET_PATH, SOCKET_PREFIX);
        DataTreeIdentifier<Hostconfig> dataTreeIdentifier =
                new DataTreeIdentifier<>(LogicalDatastoreType.OPERATIONAL, InstanceIdentifier.builder(Neutron.class)
                .child(Hostconfigs.class)
                .child(Hostconfig.class)
                .build());
        listenerRegistration =
                db.registerDataTreeChangeListener(dataTreeIdentifier, NeutronHostconfigVppListenerTest.this);
    }

    @Test
    public void writeNode() {
        WriteTransaction wTx = db.newWriteOnlyTransaction();
        InstanceIdentifier<Node> iid = InstanceIdentifier.builder(NetworkTopology.class)
            .child(Topology.class, new TopologyKey(new TopologyId("topology-netconf")))
            .child(Node.class, new NodeKey(new NodeId("nodeId")))
            .build();
        Node node1 = createNetconfNode(NODE_ID, V3PO, INTERFACES);
        wTx.put(LogicalDatastoreType.OPERATIONAL, iid, node1, true);
        wTx.submit();
    }

    private Node createNetconfNode(NodeId nodeId, String... capabilities) {
        List<AvailableCapability> caps = Arrays.asList(capabilities)
            .stream()
            .map(name -> new AvailableCapabilityBuilder().setCapability(name).build())
            .collect(Collectors.toList());
        NetconfNode netconfNode = new NetconfNodeBuilder()
            .setAvailableCapabilities(new AvailableCapabilitiesBuilder().setAvailableCapability(caps).build()).build();
        return new NodeBuilder().setNodeId(nodeId).addAugmentation(NetconfNode.class, netconfNode).build();
    }

    @Override
    public void onDataTreeChanged(Collection<DataTreeModification<Hostconfig>> dtms) {
        for (DataTreeModification<Hostconfig> dtm : dtms) {
            if (ModificationType.WRITE.equals(dtm.getRootNode().getModificationType())) {
                Hostconfig hc = dtm.getRootNode().getDataAfter();
                Assert.assertEquals(NODE_ID.getValue(), hc.getHostId());
            }
            if (ModificationType.DELETE.equals(dtm.getRootNode().getModificationType())) {
                Hostconfig hc = dtm.getRootNode().getDataBefore();
                Assert.assertEquals(NODE_ID.getValue(), hc.getHostId());
            }
        }
    }

    @Override
    public void close() throws Exception {
        nl.close();
        if (listenerRegistration != null) {
            listenerRegistration.close();
        }
    }
}
