/*
 * Copyright (c) 2015 Hewlett-Packard Development Company, L.P. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.transcriber;

import java.util.List;
import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.neutron.spi.INeutronL2gatewayConnectionCRUD;
import org.opendaylight.neutron.spi.NeutronL2gatewayConnection;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.l2gateways.rev150712.l2gateway.connections.attributes.L2gatewayConnections;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.l2gateways.rev150712.l2gateway.connections.attributes.l2gatewayconnections.L2gatewayConnection;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.l2gateways.rev150712.l2gateway.connections.attributes.l2gatewayconnections.L2gatewayConnectionBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.rev150712.Neutron;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NeutronL2gatewayConnectionInterface extends
AbstractNeutronInterface<L2gatewayConnection, L2gatewayConnections, NeutronL2gatewayConnection>
implements INeutronL2gatewayConnectionCRUD {

    private static final Logger LOGGER = LoggerFactory.getLogger(NeutronL2gatewayConnectionInterface.class);

    NeutronL2gatewayConnectionInterface(DataBroker db) {
        super(db);
    }

    @Override
    protected List<L2gatewayConnection> getDataObjectList(L2gatewayConnections l2gatewayConnections) {
        return l2gatewayConnections.getL2gatewayConnection();
    }

    @Override
    public boolean inUse(String l2gatewayConnectionID) {
        return !exists(l2gatewayConnectionID);
    }

    @Override
    protected InstanceIdentifier<L2gatewayConnection>
    createInstanceIdentifier(L2gatewayConnection l2gatewayConnection) {
        return InstanceIdentifier.create(Neutron.class).child(L2gatewayConnections.class)
                .child(L2gatewayConnection.class,l2gatewayConnection.getKey());
    }

    @Override
    protected InstanceIdentifier<L2gatewayConnections> createInstanceIdentifier(){
        return InstanceIdentifier.create(Neutron.class).child(L2gatewayConnections.class);
    }

    @Override
    protected NeutronL2gatewayConnection fromMd(L2gatewayConnection l2gatewayConnection){
        final NeutronL2gatewayConnection result = new NeutronL2gatewayConnection();
        if (l2gatewayConnection.getUuid() != null){
            result.setID(l2gatewayConnection.getUuid().getValue());
        }
        if (l2gatewayConnection.getKey().getUuid() != null){
            result.setID(l2gatewayConnection.getKey().getUuid().getValue());
        }
        if (l2gatewayConnection.getL2gatewayId().getValue() != null){
            result.setL2gatewayID(String.valueOf(l2gatewayConnection.getL2gatewayId().getValue()));
        }
        if (l2gatewayConnection.getTenantId() != null){
            result.setTenantID(l2gatewayConnection.getTenantId());
        }
        if (l2gatewayConnection.getNetworkId().getValue() != null){
            result.setNetworkID(String.valueOf(l2gatewayConnection.getNetworkId().getValue()));
        }
        if (l2gatewayConnection.getSegmentId() != null){
            result.setSegmentID(Integer.valueOf(l2gatewayConnection.getSegmentId()));
        }
        if (l2gatewayConnection.getPortId() != null){
            result.setPortID(String.valueOf(l2gatewayConnection.getPortId().getValue()));
        }
        return result;
    }

    @Override
    protected L2gatewayConnection toMd(NeutronL2gatewayConnection neutronObject) {
        final L2gatewayConnectionBuilder l2gatewayConnectionBuilder =
                new L2gatewayConnectionBuilder();
        if (neutronObject.getID() != null){
            l2gatewayConnectionBuilder.setUuid(toUuid(neutronObject.getID()));
        }
        if (neutronObject.getL2gatewayID() != null){
            l2gatewayConnectionBuilder.setL2gatewayId(toUuid(neutronObject.getL2gatewayID()));
        }
        if (neutronObject.getNetworkID() != null){
            l2gatewayConnectionBuilder.setNetworkId(toUuid(neutronObject.getNetworkID()));
        }
        if (neutronObject.getSegmentID() != null){
            l2gatewayConnectionBuilder.setSegmentId((neutronObject.getSegmentID()));
        }
        if (neutronObject.getTenantID() != null){
            l2gatewayConnectionBuilder.setTenantId(toUuid(neutronObject.getTenantID()));
        }
        if (neutronObject.getPortID() != null){
            l2gatewayConnectionBuilder.setPortId(toUuid(neutronObject.getPortID()));
        }
        return l2gatewayConnectionBuilder.build();
    }

    @Override
    protected L2gatewayConnection toMd(String uuid) {
        final L2gatewayConnectionBuilder l2gatewayConnectionBuilder = new L2gatewayConnectionBuilder();
        l2gatewayConnectionBuilder.setUuid(toUuid(uuid));
        return l2gatewayConnectionBuilder.build();
    }
}
