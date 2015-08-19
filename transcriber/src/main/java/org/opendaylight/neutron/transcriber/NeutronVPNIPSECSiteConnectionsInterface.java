/*
 * Copyright (c) 2015 Tata Consultancy Services and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.transcriber;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.opendaylight.controller.sal.binding.api.BindingAwareBroker.ProviderContext;
import org.opendaylight.neutron.spi.INeutronVPNIPSECSiteConnectionsCRUD;
import org.opendaylight.neutron.spi.NeutronVPNDeadPeerDetection;
import org.opendaylight.neutron.spi.NeutronVPNIPSECSiteConnection;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.vpnaas.rev141002.ipsecconnection.attributes.DpdBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.vpnaas.rev141002.ipsecconnections.attributes.IpsecSiteConnections;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.vpnaas.rev141002.ipsecconnections.attributes.ipsec.site.connections.Ipsecsiteconnection;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.vpnaas.rev141002.ipsecconnections.attributes.ipsec.site.connections.IpsecsiteconnectionBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.rev150325.Neutron;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NeutronVPNIPSECSiteConnectionsInterface extends AbstractNeutronInterface<Ipsecsiteconnection, NeutronVPNIPSECSiteConnection> implements INeutronVPNIPSECSiteConnectionsCRUD {
    private static final Logger LOGGER = LoggerFactory.getLogger(NeutronVPNIKEPolicyInterface.class);
    private ConcurrentMap<String, NeutronVPNIPSECSiteConnection> neutronVPNIPSECSiteConnectionDB = new ConcurrentHashMap<String, NeutronVPNIPSECSiteConnection>();


    NeutronVPNIPSECSiteConnectionsInterface(ProviderContext providerContext) {
        super(providerContext);
    }

    // INeutronVPNIPSECSiteConnectionsCRUD methods

    @Override
    public boolean neutronVPNIPSECSiteConnectionsExists(String policyID) {
        return neutronVPNIPSECSiteConnectionDB.containsKey(policyID);
    }

    @Override
    public NeutronVPNIPSECSiteConnection getNeutronVPNIPSECSiteConnections(String policyID) {
        if (!neutronVPNIPSECSiteConnectionsExists(policyID)) {
            return null;
        }
        return neutronVPNIPSECSiteConnectionDB.get(policyID);
    }

    @Override
    public List<NeutronVPNIPSECSiteConnection> getAllNeutronVPNIPSECSiteConnections() {
        Set<NeutronVPNIPSECSiteConnection> allNeutronVPNIPSECSiteConnections = new HashSet<NeutronVPNIPSECSiteConnection>();
        for (Entry<String, NeutronVPNIPSECSiteConnection> entry : neutronVPNIPSECSiteConnectionDB.entrySet()) {
            NeutronVPNIPSECSiteConnection meteringLabelRule = entry.getValue();
            allNeutronVPNIPSECSiteConnections.add(meteringLabelRule);
        }
        LOGGER.debug("Exiting getAllNeutronVPNIPSECSiteConnections, Found {} OpenStackVPNIPSECSiteConnections", allNeutronVPNIPSECSiteConnections.size());
        List<NeutronVPNIPSECSiteConnection> ans = new ArrayList<NeutronVPNIPSECSiteConnection>();
        ans.addAll(allNeutronVPNIPSECSiteConnections);
        return ans;
    }

    @Override
    public boolean addNeutronVPNIPSECSiteConnections(NeutronVPNIPSECSiteConnection input) {
        if (neutronVPNIPSECSiteConnectionsExists(input.getID())) {
            return false;
        }
        neutronVPNIPSECSiteConnectionDB.putIfAbsent(input.getID(), input);
        addMd(input);
        return true;
    }

    @Override
    public boolean removeNeutronVPNIPSECSiteConnections(String policyID) {
        if (!neutronVPNIPSECSiteConnectionsExists(policyID)) {
            return false;
        }
        neutronVPNIPSECSiteConnectionDB.remove(policyID);
        removeMd(toMd(policyID));
        return true;
    }

    @Override
    public boolean updateNeutronVPNIPSECSiteConnections(String policyID, NeutronVPNIPSECSiteConnection delta) {
        if (!neutronVPNIPSECSiteConnectionsExists(policyID)) {
            return false;
        }
        NeutronVPNIPSECSiteConnection target = neutronVPNIPSECSiteConnectionDB.get(policyID);
        boolean rc = overwrite(target, delta);
        if (rc) {
            updateMd(neutronVPNIPSECSiteConnectionDB.get(policyID));
        }
        return rc;
    }

    @Override
    public boolean neutronVPNIPSECSiteConnectionsInUse(String policyID) {
        if (!neutronVPNIPSECSiteConnectionsExists(policyID)) {
            return true;
        }
        return false;
    }

    @Override
    protected Ipsecsiteconnection toMd(NeutronVPNIPSECSiteConnection ipsecSiteConnection) {
        IpsecsiteconnectionBuilder ipsecSiteConnectionBuilder = new IpsecsiteconnectionBuilder();
        if (ipsecSiteConnection.getName() != null) {
            ipsecSiteConnectionBuilder.setName(ipsecSiteConnection.getName());
        }
        if (ipsecSiteConnection.getTenantID() != null) {
            ipsecSiteConnectionBuilder.setTenantId(toUuid(ipsecSiteConnection.getTenantID()));
        }
        ipsecSiteConnectionBuilder.setStatus(ipsecSiteConnection.getStatus());
        if (ipsecSiteConnection.getAdminStateUp() != null) {
            ipsecSiteConnectionBuilder.setAdminStateUp(ipsecSiteConnection.getAdminStateUp());
        }
        if (ipsecSiteConnection.getDescription() != null) {
            ipsecSiteConnectionBuilder.setDescr(ipsecSiteConnection.getDescription());
        }
        if (ipsecSiteConnection.getPeerAddress() != null) {
            ipsecSiteConnectionBuilder.setPeerAddress(ipsecSiteConnection.getPeerAddress());
        }
        if (ipsecSiteConnection.getPeerCidrs() != null) {
            List<String> peerCidrs = new ArrayList<String>();
            for( String peerCidr : ipsecSiteConnection.getPeerCidrs()) {
                peerCidrs.add(peerCidr);
            }
            ipsecSiteConnectionBuilder.setPeerCidrs(peerCidrs);
        }
        if (ipsecSiteConnection.getPeerID() != null) {
            ipsecSiteConnectionBuilder.setPeerId(ipsecSiteConnection.getPeerID());
        }
        if (ipsecSiteConnection.getRouteMode() != null) {
            ipsecSiteConnectionBuilder.setRouteMode(ipsecSiteConnection.getRouteMode());
        }
        if (ipsecSiteConnection.getMtu() != null) {
            ipsecSiteConnectionBuilder.setMtu((ipsecSiteConnection.getMtu()).shortValue());
        }
        if (ipsecSiteConnection.getAuthMode() != null) {
            ipsecSiteConnectionBuilder.setAuthMode(ipsecSiteConnection.getAuthMode());
        }
        if (ipsecSiteConnection.getPreSharedKey() != null) {
            ipsecSiteConnectionBuilder.setPsk(ipsecSiteConnection.getPreSharedKey());
        }
        if (ipsecSiteConnection.getInitiator() != null) {
            ipsecSiteConnectionBuilder.setInitiator(ipsecSiteConnection.getInitiator());
        }
        if (ipsecSiteConnection.getIkePolicyID() != null) {
            ipsecSiteConnectionBuilder.setIkepolicyId(toUuid(ipsecSiteConnection.getIkePolicyID()));
        }
        if (ipsecSiteConnection.getIpsecPolicyID() != null) {
            ipsecSiteConnectionBuilder.setIpsecpolicyId(toUuid(ipsecSiteConnection.getIpsecPolicyID()));
        }
        if (ipsecSiteConnection.getVpnServiceID() != null) {
            ipsecSiteConnectionBuilder.setVpnserviceId(toUuid(ipsecSiteConnection.getVpnServiceID()));
        }
        if (ipsecSiteConnection.getDeadPeerDetection() != null) {
            NeutronVPNDeadPeerDetection deadPeerDetection = ipsecSiteConnection.getDeadPeerDetection();
            DpdBuilder dpdBuilder = new DpdBuilder();
            dpdBuilder.setAction(deadPeerDetection.getAction());
            dpdBuilder.setInterval(deadPeerDetection.getInterval());
            dpdBuilder.setTimeout(deadPeerDetection.getTimeout());
            ipsecSiteConnectionBuilder.setDpd(dpdBuilder.build());
        }
        if (ipsecSiteConnection.getID() != null) {
            ipsecSiteConnectionBuilder.setUuid(toUuid(ipsecSiteConnection.getID()));
        } else {
            LOGGER.warn("Attempting to write neutron vpnIPSECSiteConnection without UUID");
        }
        return ipsecSiteConnectionBuilder.build();
    }

    @Override
    protected InstanceIdentifier<Ipsecsiteconnection> createInstanceIdentifier(Ipsecsiteconnection ipsecSiteConnection) {
        return InstanceIdentifier.create(Neutron.class)
                 .child(IpsecSiteConnections.class)
                 .child(Ipsecsiteconnection.class, ipsecSiteConnection.getKey());
    }

    @Override
    protected Ipsecsiteconnection toMd(String uuid) {
        IpsecsiteconnectionBuilder ipsecSiteConnectionBuilder = new IpsecsiteconnectionBuilder();
        ipsecSiteConnectionBuilder.setUuid(toUuid(uuid));
        return ipsecSiteConnectionBuilder.build();
    }

    public static void registerNewInterface(BundleContext context,
                                            ProviderContext providerContext,
                                            List<ServiceRegistration<?>> registrations) {
        NeutronVPNIPSECSiteConnectionsInterface neutronVPNIPSECSiteConnectionsInterface = new NeutronVPNIPSECSiteConnectionsInterface(providerContext);
        ServiceRegistration<INeutronVPNIPSECSiteConnectionsCRUD> neutronVPNIPSECSiteConnectionsInterfaceRegistration = context.registerService(INeutronVPNIPSECSiteConnectionsCRUD.class, neutronVPNIPSECSiteConnectionsInterface, null);
        if (neutronVPNIPSECSiteConnectionsInterfaceRegistration != null) {
            registrations.add(neutronVPNIPSECSiteConnectionsInterfaceRegistration);
        }
    }
}
