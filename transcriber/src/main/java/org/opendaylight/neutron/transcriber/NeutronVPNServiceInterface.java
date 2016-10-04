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
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.vpnaas.rev150712.vpnservices.attributes.VpnServices;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.vpnaas.rev150712.vpnservices.attributes.vpn.services.Vpnservice;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.vpnaas.rev150712.vpnservices.attributes.vpn.services.VpnserviceBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.vpnaas.rev150712.vpnservices.attributes.vpn.services.VpnserviceKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class NeutronVPNServiceInterface
        extends AbstractNeutronInterface<Vpnservice, VpnServices, VpnserviceKey, NeutronVPNService>
        implements INeutronVPNServiceCRUD {
    private static final Logger LOGGER = LoggerFactory.getLogger(NeutronVPNServiceInterface.class);

    NeutronVPNServiceInterface(DataBroker db) {
        super(VpnserviceBuilder.class, db);
    }

    @Override
    protected List<Vpnservice> getDataObjectList(VpnServices services) {
        return services.getVpnservice();
    }

    protected NeutronVPNService fromMd(Vpnservice vpnService) {
        final NeutronVPNService answer = new NeutronVPNService();
        fromMdAdminAttributes(vpnService, answer);
        if (vpnService.getSubnetId() != null) {
            answer.setSubnetUUID(vpnService.getSubnetId().getValue());
        }
        if (vpnService.getRouterId() != null) {
            answer.setRouterUUID(vpnService.getRouterId().getValue());
        }
        return answer;
    }

    @Override
    protected Vpnservice toMd(NeutronVPNService vpnService) {
        final VpnserviceBuilder vpnServiceBuilder = new VpnserviceBuilder();
        toMdAdminAttributes(vpnService, vpnServiceBuilder);
        if (vpnService.getSubnetUUID() != null) {
            vpnServiceBuilder.setSubnetId(toUuid(vpnService.getSubnetUUID()));
        }
        if (vpnService.getRouterUUID() != null) {
            vpnServiceBuilder.setRouterId(toUuid(vpnService.getRouterUUID()));
        }
        return vpnServiceBuilder.build();
    }
}
