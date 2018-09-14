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
import javax.inject.Inject;
import javax.inject.Singleton;
import org.apache.aries.blueprint.annotation.service.Service;
import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.neutron.spi.INeutronVpnIpSecSiteConnectionsCRUD;
import org.opendaylight.neutron.spi.NeutronVpnDeadPeerDetection;
import org.opendaylight.neutron.spi.NeutronVpnIpSecSiteConnection;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.vpnaas.rev150712.ipsecconnection.attributes.DpdBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.vpnaas.rev150712.ipsecconnections.attributes.IpsecSiteConnections;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.vpnaas.rev150712.ipsecconnections.attributes.ipsec.site.connections.Ipsecsiteconnection;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.vpnaas.rev150712.ipsecconnections.attributes.ipsec.site.connections.IpsecsiteconnectionBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.vpnaas.rev150712.ipsecconnections.attributes.ipsec.site.connections.IpsecsiteconnectionKey;

@Singleton
@Service(classes = INeutronVpnIpSecSiteConnectionsCRUD.class)
public final class NeutronVpnIpSecSiteConnectionsInterface
        extends AbstractNeutronInterface<Ipsecsiteconnection, IpsecSiteConnections, IpsecsiteconnectionKey,
        NeutronVpnIpSecSiteConnection>
        implements INeutronVpnIpSecSiteConnectionsCRUD {

    @Inject
    public NeutronVpnIpSecSiteConnectionsInterface(DataBroker db) {
        super(IpsecsiteconnectionBuilder.class, db);
    }

    // INeutronVpnIpSecSiteConnectionsCRUD methods
    @Override
    protected List<Ipsecsiteconnection> getDataObjectList(IpsecSiteConnections connections) {
        return connections.getIpsecsiteconnection();
    }

    @Override
    protected NeutronVpnIpSecSiteConnection fromMd(Ipsecsiteconnection ipsecSiteConnection) {
        final NeutronVpnIpSecSiteConnection answer = new NeutronVpnIpSecSiteConnection();
        fromMdAdminAttributes(ipsecSiteConnection, answer);
        if (ipsecSiteConnection.getPeerAddress() != null) {
            answer.setPeerAddress(ipsecSiteConnection.getPeerAddress());
        }
        if (ipsecSiteConnection.getPeerCidrs() != null) {
            final List<String> peerCidrs = new ArrayList<>();
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
            answer.setMtu(ipsecSiteConnection.getMtu().intValue());
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
            final NeutronVpnDeadPeerDetection deadPeerDetection = new NeutronVpnDeadPeerDetection();
            deadPeerDetection.setAction(ipsecSiteConnection.getDpd().getAction());
            deadPeerDetection.setInterval(ipsecSiteConnection.getDpd().getInterval());
            deadPeerDetection.setTimeout(ipsecSiteConnection.getDpd().getTimeout());
            answer.setDeadPeerDetection(deadPeerDetection);
        }
        return answer;
    }

    @Override
    protected Ipsecsiteconnection toMd(NeutronVpnIpSecSiteConnection ipsecSiteConnection) {
        final IpsecsiteconnectionBuilder ipsecSiteConnectionBuilder = new IpsecsiteconnectionBuilder();
        toMdAdminAttributes(ipsecSiteConnection, ipsecSiteConnectionBuilder);
        if (ipsecSiteConnection.getPeerAddress() != null) {
            ipsecSiteConnectionBuilder.setPeerAddress(ipsecSiteConnection.getPeerAddress());
        }
        if (ipsecSiteConnection.getPeerCidrs() != null) {
            final List<String> peerCidrs = new ArrayList<>();
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
            ipsecSiteConnectionBuilder.setMtu(ipsecSiteConnection.getMtu().shortValue());
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
            final NeutronVpnDeadPeerDetection deadPeerDetection = ipsecSiteConnection.getDeadPeerDetection();
            final DpdBuilder dpdBuilder = new DpdBuilder();
            dpdBuilder.setAction(deadPeerDetection.getAction());
            dpdBuilder.setInterval(deadPeerDetection.getInterval());
            dpdBuilder.setTimeout(deadPeerDetection.getTimeout());
            ipsecSiteConnectionBuilder.setDpd(dpdBuilder.build());
        }
        return ipsecSiteConnectionBuilder.build();
    }
}
