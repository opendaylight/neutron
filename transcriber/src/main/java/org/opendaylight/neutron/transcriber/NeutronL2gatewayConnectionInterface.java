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
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.l2gateways.rev150712.l2gateway.connections.attributes.l2gatewayconnections.L2gatewayConnectionKey;

public final class NeutronL2gatewayConnectionInterface
        extends AbstractNeutronInterface<L2gatewayConnection, L2gatewayConnections, L2gatewayConnectionKey,
                                         NeutronL2gatewayConnection>
        implements INeutronL2gatewayConnectionCRUD {

    NeutronL2gatewayConnectionInterface(DataBroker db) {
        super(L2gatewayConnectionBuilder.class, db);
    }

    @Override
    protected List<L2gatewayConnection> getDataObjectList(L2gatewayConnections l2gatewayConnections) {
        return l2gatewayConnections.getL2gatewayConnection();
    }

    @Override
    protected NeutronL2gatewayConnection fromMd(L2gatewayConnection l2gatewayConnection) {
        final NeutronL2gatewayConnection result = new NeutronL2gatewayConnection();
        fromMdBaseAttributes(l2gatewayConnection, result);

        if (l2gatewayConnection.getL2gatewayId().getValue() != null) {
            result.setL2gatewayID(String.valueOf(l2gatewayConnection.getL2gatewayId().getValue()));
        }
        if (l2gatewayConnection.getNetworkId().getValue() != null) {
            result.setNetworkID(String.valueOf(l2gatewayConnection.getNetworkId().getValue()));
        }
        if (l2gatewayConnection.getSegmentId() != null) {
            result.setSegmentID(Integer.valueOf(l2gatewayConnection.getSegmentId()));
        }
        if (l2gatewayConnection.getPortId() != null) {
            result.setPortID(String.valueOf(l2gatewayConnection.getPortId().getValue()));
        }
        return result;
    }

    @Override
    protected L2gatewayConnection toMd(NeutronL2gatewayConnection neutronObject) {
        final L2gatewayConnectionBuilder l2gatewayConnectionBuilder = new L2gatewayConnectionBuilder();
        toMdBaseAttributes(neutronObject, l2gatewayConnectionBuilder);

        if (neutronObject.getL2gatewayID() != null) {
            l2gatewayConnectionBuilder.setL2gatewayId(toUuid(neutronObject.getL2gatewayID()));
        }
        if (neutronObject.getNetworkID() != null) {
            l2gatewayConnectionBuilder.setNetworkId(toUuid(neutronObject.getNetworkID()));
        }
        if (neutronObject.getSegmentID() != null) {
            l2gatewayConnectionBuilder.setSegmentId((neutronObject.getSegmentID()));
        }
        if (neutronObject.getPortID() != null) {
            l2gatewayConnectionBuilder.setPortId(toUuid(neutronObject.getPortID()));
        }
        return l2gatewayConnectionBuilder.build();
    }
}
