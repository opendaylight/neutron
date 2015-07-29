/*
 * Copyright (c) 2015 Hewlett-Packard Development Company, L.P. and others.
 * All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.neutron.transcriber;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.opendaylight.controller.sal.binding.api.BindingAwareBroker.ProviderContext;
import org.opendaylight.neutron.spi.INeutronL2GatewayCRUD;
import org.opendaylight.neutron.spi.INeutronL2GatewayConnectionCRUD;
import org.opendaylight.neutron.spi.NeutronL2GatewayConnection;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.l2gateways.rev141002.l2gatewayconnections.attributes.L2gatewayConnections;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.l2gateways.rev141002.l2gatewayconnections.attributes.l2gatewayconnections.L2gatewayConnection;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.l2gateways.rev141002.l2gatewayconnections.attributes.l2gatewayconnections.L2gatewayConnectionBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.rev150325.Neutron;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NeutronL2GatewayConnectionInterface extends 
AbstractNeutronInterface<L2gatewayConnection, NeutronL2GatewayConnection>
implements INeutronL2GatewayConnectionCRUD {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(NeutronL2GatewayConnectionInterface.class);
    private ConcurrentMap<String, NeutronL2GatewayConnection> neutronL2gatewayConnectionDB =
            new ConcurrentHashMap<String, NeutronL2GatewayConnection>();

    NeutronL2GatewayConnectionInterface(ProviderContext providerContext) {
        super(providerContext);
    }

    @Override
    public boolean neutronL2gatewayConnectionExists(String uuid) {
        return neutronL2gatewayConnectionDB.containsKey(uuid);
    }

    @Override
    public boolean addNeutronL2gatewayConnection(
                                                 NeutronL2GatewayConnection input) {
        if (neutronL2gatewayConnectionExists(input.getL2gatewayConnectionUUID())) {
            return false;
        }
        neutronL2gatewayConnectionDB.putIfAbsent(input
                                                 .getL2gatewayConnectionUUID(), input);
        addMd(input);
        return true;
    }

    @Override
    protected InstanceIdentifier<L2gatewayConnection>
    createInstanceIdentifier(L2gatewayConnection l2gatewayConnection) {
        return InstanceIdentifier.create(Neutron.class)
                .child(L2gatewayConnections.class).child(L2gatewayConnection.class,
                                                         l2gatewayConnection.getKey());
    }

    @Override
    protected L2gatewayConnection toMd(NeutronL2GatewayConnection neutronObject) {
        L2gatewayConnectionBuilder l2gatewayConnectionBuilder=
                new L2gatewayConnectionBuilder();

        if(neutronObject.getL2gatewayConnectionUUID()!=null){
            l2gatewayConnectionBuilder
            .setUuid(toUuid(neutronObject.getL2gatewayConnectionUUID()));
        }
        if(neutronObject.getL2gatewayUUID()!=null){
            l2gatewayConnectionBuilder
            .setL2gatewayId(toUuid(neutronObject.getL2gatewayUUID()));
        }
        if(neutronObject.getNetworkUUID()!=null){
            l2gatewayConnectionBuilder
            .setNetworkId(toUuid(neutronObject.getNetworkUUID()));
        }
        if(neutronObject.getSegmentUUID()!=null){
            l2gatewayConnectionBuilder
            .setSegmentId(toUuid(neutronObject.getSegmentUUID()));
        }
        if(neutronObject.getTenantUUID()!=null){
            l2gatewayConnectionBuilder
            .setTenantId(toUuid(neutronObject.getTenantUUID()));
        }
        if(neutronObject.getPortUUID()!=null){
            l2gatewayConnectionBuilder
            .setPortId(toUuid(neutronObject.getPortUUID()));
        }
        return l2gatewayConnectionBuilder.build();
    }

    @Override
    protected L2gatewayConnection toMd(String uuid) {
        L2gatewayConnectionBuilder l2gatewayConnectionBuilder =
                new L2gatewayConnectionBuilder();
        l2gatewayConnectionBuilder.setUuid(toUuid(uuid));
        return l2gatewayConnectionBuilder.build();
    }

    @Override
    public List<NeutronL2GatewayConnection> getAllL2gatewayConnections() {
        Set<NeutronL2GatewayConnection> allL2gatewayConnections =
                new HashSet<NeutronL2GatewayConnection>();
        for (Entry<String, NeutronL2GatewayConnection> entry 
                : neutronL2gatewayConnectionDB.entrySet()) {
            NeutronL2GatewayConnection l2gatewayConnection = entry.getValue();
            allL2gatewayConnections.add(l2gatewayConnection);
        }
        LOGGER.debug("Exiting getAllL2gatewayConnections, Found {}"
                + " L2gatewayConnections", allL2gatewayConnections.size());
        List<NeutronL2GatewayConnection> ans =
                new ArrayList<NeutronL2GatewayConnection>();
        ans.addAll(allL2gatewayConnections);
        return ans;
    }

    @Override
    public NeutronL2GatewayConnection getNeutronL2gatewayConnection(String uuid) {
        if (!neutronL2gatewayConnectionExists(uuid)) {
            LOGGER.debug("No L2gateway Connection Have Been Defined");
            return null;
        }
        return neutronL2gatewayConnectionDB.get(uuid);
    }

    public static void registerNewInterface(BundleContext context,
                                            ProviderContext providerContext,
                                            List<ServiceRegistration<?>> registrations) {
        NeutronL2GatewayConnectionInterface neutronL2gatewayConnectionInterface = new NeutronL2GatewayConnectionInterface(providerContext);
        ServiceRegistration<INeutronL2GatewayConnectionCRUD> neutronL2gatewayConInterfaceRegistration = context.registerService(INeutronL2GatewayConnectionCRUD.class, neutronL2gatewayConnectionInterface, null);
        if (neutronL2gatewayConInterfaceRegistration != null) {
            registrations.add(neutronL2gatewayConInterfaceRegistration);
        }
    }
}
