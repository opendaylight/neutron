/*
 * Copyright (c) 2015 Tata Consultancy Services and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.transcriber;

import java.util.List;
import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.neutron.spi.INeutronVPNServiceCRUD;
import org.opendaylight.neutron.spi.NeutronVPNService;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.rev150712.Neutron;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.vpnaas.rev150712.vpnservices.attributes.VpnServices;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.vpnaas.rev150712.vpnservices.attributes.vpn.services.Vpnservice;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.vpnaas.rev150712.vpnservices.attributes.vpn.services.VpnserviceBuilder;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class NeutronVPNServiceInterface
        extends AbstractNeutronInterface<Vpnservice, VpnServices, NeutronVPNService>
        implements INeutronVPNServiceCRUD {
    private static final Logger LOGGER = LoggerFactory.getLogger(NeutronVPNServiceInterface.class);

    NeutronVPNServiceInterface(DataBroker db) {
        super(db);
    }

    @Override
    protected List<Vpnservice> getDataObjectList(VpnServices services) {
        return services.getVpnservice();
    }

    @Override
    public boolean inUse(String uuid) {
        return !exists(uuid);
    }

    protected NeutronVPNService fromMd(Vpnservice vpnService) {
        final NeutronVPNService answer = new NeutronVPNService();
        if (vpnService.getName() != null) {
            answer.setName(vpnService.getName());
        }
        if (vpnService.getTenantId() != null) {
            answer.setTenantID(vpnService.getTenantId());
        }
        if (vpnService.getStatus() != null) {
            answer.setStatus(vpnService.getStatus());
        }
        if (vpnService.getSubnetId() != null) {
            answer.setSubnetUUID(vpnService.getSubnetId().getValue());
        }
        if (vpnService.getRouterId() != null) {
            answer.setRouterUUID(vpnService.getRouterId().getValue());
        }
        answer.setAdminStateUp(vpnService.isAdminStateUp());
        if (vpnService.getUuid() != null) {
            answer.setID(vpnService.getUuid().getValue());
        }
        return answer;
    }

    @Override
    protected Vpnservice toMd(NeutronVPNService vpnService) {
        final VpnserviceBuilder vpnServiceBuilder = new VpnserviceBuilder();
        if (vpnService.getName() != null) {
            vpnServiceBuilder.setName(vpnService.getName());
        }
        if (vpnService.getTenantID() != null) {
            vpnServiceBuilder.setTenantId(toUuid(vpnService.getTenantID()));
        }
        if (vpnService.getStatus() != null) {
            vpnServiceBuilder.setStatus(vpnService.getStatus());
        }
        if (vpnService.getSubnetUUID() != null) {
            vpnServiceBuilder.setSubnetId(toUuid(vpnService.getSubnetUUID()));
        }
        if (vpnService.getRouterUUID() != null) {
            vpnServiceBuilder.setRouterId(toUuid(vpnService.getRouterUUID()));
        }
        vpnServiceBuilder.setAdminStateUp(vpnService.getAdminStateUp());
        if (vpnService.getID() != null) {
            vpnServiceBuilder.setUuid(toUuid(vpnService.getID()));
        } else {
            LOGGER.warn("Attempting to write neutron vpnService without UUID");
        }
        return vpnServiceBuilder.build();
    }

    @Override
    protected Vpnservice toMd(String uuid) {
        final VpnserviceBuilder vpnServiceBuilder = new VpnserviceBuilder();
        vpnServiceBuilder.setUuid(toUuid(uuid));
        return vpnServiceBuilder.build();
    }

    @Override
    protected InstanceIdentifier<Vpnservice> createInstanceIdentifier(Vpnservice vpnService) {
        return InstanceIdentifier.create(Neutron.class).child(VpnServices.class).child(Vpnservice.class,
                vpnService.getKey());
    }

    @Override
    protected InstanceIdentifier<VpnServices> createInstanceIdentifier() {
        return InstanceIdentifier.create(Neutron.class).child(VpnServices.class);
    }

}
