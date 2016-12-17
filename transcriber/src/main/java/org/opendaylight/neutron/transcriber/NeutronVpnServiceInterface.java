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
import org.opendaylight.neutron.spi.INeutronVpnServiceCRUD;
import org.opendaylight.neutron.spi.NeutronVpnService;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.vpnaas.rev150712.vpnservices.attributes.VpnServices;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.vpnaas.rev150712.vpnservices.attributes.vpn.services.Vpnservice;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.vpnaas.rev150712.vpnservices.attributes.vpn.services.VpnserviceBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.vpnaas.rev150712.vpnservices.attributes.vpn.services.VpnserviceKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class NeutronVpnServiceInterface
        extends AbstractNeutronInterface<Vpnservice, VpnServices, VpnserviceKey, NeutronVpnService>
        implements INeutronVpnServiceCRUD {
    private static final Logger LOGGER = LoggerFactory.getLogger(NeutronVpnServiceInterface.class);

    NeutronVpnServiceInterface(DataBroker db) {
        super(VpnserviceBuilder.class, db);
    }

    @Override
    protected List<Vpnservice> getDataObjectList(VpnServices services) {
        return services.getVpnservice();
    }

    protected NeutronVpnService fromMd(Vpnservice vpnService) {
        final NeutronVpnService answer = new NeutronVpnService();
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
    protected Vpnservice toMd(NeutronVpnService vpnService) {
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
