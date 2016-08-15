/*
 * Copyright (c) 2015 Tata Consultancy Services and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.transcriber;

import java.util.ArrayList;
import java.util.List;
import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.neutron.spi.INeutronVPNIPSECSiteConnectionsCRUD;
import org.opendaylight.neutron.spi.NeutronVPNDeadPeerDetection;
import org.opendaylight.neutron.spi.NeutronVPNIPSECSiteConnection;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.rev150712.Neutron;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.vpnaas.rev150712.ipsecconnection.attributes.DpdBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.vpnaas.rev150712.ipsecconnections.attributes.IpsecSiteConnections;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.vpnaas.rev150712.ipsecconnections.attributes.ipsec.site.connections.Ipsecsiteconnection;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.vpnaas.rev150712.ipsecconnections.attributes.ipsec.site.connections.IpsecsiteconnectionBuilder;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NeutronVPNIPSECSiteConnectionsInterface
        extends AbstractNeutronInterface<Ipsecsiteconnection, IpsecSiteConnections, NeutronVPNIPSECSiteConnection>
        implements INeutronVPNIPSECSiteConnectionsCRUD {
    private static final Logger LOGGER = LoggerFactory.getLogger(NeutronVPNIKEPolicyInterface.class);

    NeutronVPNIPSECSiteConnectionsInterface(DataBroker db) {
        super(db);
    }

    // INeutronVPNIPSECSiteConnectionsCRUD methods
    @Override
    protected List<Ipsecsiteconnection> getDataObjectList(IpsecSiteConnections connections) {
        return connections.getIpsecsiteconnection();
    }

    @Override
    public boolean inUse(String policyID) {
        return !exists(policyID);
    }

    protected NeutronVPNIPSECSiteConnection fromMd(Ipsecsiteconnection ipsecSiteConnection) {
        final NeutronVPNIPSECSiteConnection answer = new NeutronVPNIPSECSiteConnection();
        if (ipsecSiteConnection.getName() != null) {
            answer.setName(ipsecSiteConnection.getName());
        }
        if (ipsecSiteConnection.getTenantId() != null) {
            answer.setTenantID(ipsecSiteConnection.getTenantId());
        }
        answer.setStatus(ipsecSiteConnection.getStatus());
        if (ipsecSiteConnection.isAdminStateUp() != null) {
            answer.setAdminStateUp(ipsecSiteConnection.isAdminStateUp());
        }
        if (ipsecSiteConnection.getPeerAddress() != null) {
            answer.setPeerAddress(ipsecSiteConnection.getPeerAddress());
        }
        if (ipsecSiteConnection.getPeerCidrs() != null) {
            final List<String> peerCidrs = new ArrayList<String>();
            for (final String peerCidr : ipsecSiteConnection.getPeerCidrs()) {
                peerCidrs.add(peerCidr);
            }
            answer.setPeerCidrs(peerCidrs);
        }
        if (ipsecSiteConnection.getPeerId() != null) {
            answer.setPeerID(ipsecSiteConnection.getPeerId());
        }
        if (ipsecSiteConnection.getRouteMode() != null) {
            answer.setRouteMode(ipsecSiteConnection.getRouteMode());
        }
        if (ipsecSiteConnection.getMtu() != null) {
            answer.setMtu((ipsecSiteConnection.getMtu()).intValue());
        }
        if (ipsecSiteConnection.getAuthMode() != null) {
            answer.setAuthMode(ipsecSiteConnection.getAuthMode());
        }
        if (ipsecSiteConnection.getPsk() != null) {
            answer.setPreSharedKey(ipsecSiteConnection.getPsk());
        }
        if (ipsecSiteConnection.getInitiator() != null) {
            answer.setInitiator(ipsecSiteConnection.getInitiator());
        }
        if (ipsecSiteConnection.getIkepolicyId() != null) {
            answer.setIkePolicyID(ipsecSiteConnection.getIkepolicyId().getValue());
        }
        if (ipsecSiteConnection.getIpsecpolicyId() != null) {
            answer.setIpsecPolicyID(ipsecSiteConnection.getIpsecpolicyId().getValue());
        }
        if (ipsecSiteConnection.getVpnserviceId() != null) {
            answer.setVpnServiceID(ipsecSiteConnection.getVpnserviceId().getValue());
        }
        if (ipsecSiteConnection.getDpd() != null) {
            final NeutronVPNDeadPeerDetection deadPeerDetection = new NeutronVPNDeadPeerDetection();
            deadPeerDetection.setAction(ipsecSiteConnection.getDpd().getAction());
            deadPeerDetection.setInterval(ipsecSiteConnection.getDpd().getInterval());
            deadPeerDetection.setTimeout(ipsecSiteConnection.getDpd().getTimeout());
            answer.setDeadPeerDetection(deadPeerDetection);
        }
        if (ipsecSiteConnection.getUuid() != null) {
            answer.setID(ipsecSiteConnection.getUuid().getValue());
        }
        return answer;
    }

    @Override
    protected Ipsecsiteconnection toMd(NeutronVPNIPSECSiteConnection ipsecSiteConnection) {
        final IpsecsiteconnectionBuilder ipsecSiteConnectionBuilder = new IpsecsiteconnectionBuilder();
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
        if (ipsecSiteConnection.getPeerAddress() != null) {
            ipsecSiteConnectionBuilder.setPeerAddress(ipsecSiteConnection.getPeerAddress());
        }
        if (ipsecSiteConnection.getPeerCidrs() != null) {
            final List<String> peerCidrs = new ArrayList<String>();
            for (final String peerCidr : ipsecSiteConnection.getPeerCidrs()) {
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
            final NeutronVPNDeadPeerDetection deadPeerDetection = ipsecSiteConnection.getDeadPeerDetection();
            final DpdBuilder dpdBuilder = new DpdBuilder();
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
    protected Ipsecsiteconnection toMd(String uuid) {
        final IpsecsiteconnectionBuilder ipsecSiteConnectionBuilder = new IpsecsiteconnectionBuilder();
        ipsecSiteConnectionBuilder.setUuid(toUuid(uuid));
        return ipsecSiteConnectionBuilder.build();
    }

    @Override
    protected InstanceIdentifier<IpsecSiteConnections> createInstanceIdentifier() {
        return InstanceIdentifier.create(Neutron.class).child(IpsecSiteConnections.class);
    }

    @Override
    protected InstanceIdentifier<Ipsecsiteconnection> createInstanceIdentifier(
            Ipsecsiteconnection ipsecSiteConnection) {
        return InstanceIdentifier.create(Neutron.class).child(IpsecSiteConnections.class)
                .child(Ipsecsiteconnection.class, ipsecSiteConnection.getKey());
    }
}
