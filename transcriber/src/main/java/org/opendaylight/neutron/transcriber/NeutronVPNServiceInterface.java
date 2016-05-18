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

import org.opendaylight.controller.sal.binding.api.BindingAwareBroker.ProviderContext;
import org.opendaylight.neutron.spi.INeutronVPNServiceCRUD;
import org.opendaylight.neutron.spi.NeutronVPNService;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.vpnaas.rev150712.vpnservices.attributes.VpnServices;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.vpnaas.rev150712.vpnservices.attributes.vpn.services.Vpnservice;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.vpnaas.rev150712.vpnservices.attributes.vpn.services.VpnserviceBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.rev150712.Neutron;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NeutronVPNServiceInterface extends AbstractNeutronInterface<Vpnservice, VpnServices, NeutronVPNService> implements INeutronVPNServiceCRUD {
    private static final Logger LOGGER = LoggerFactory.getLogger(NeutronVPNServiceInterface.class);

    NeutronVPNServiceInterface(ProviderContext providerContext) {
        super(providerContext);
    }

    @Override
    public boolean neutronVPNServiceExists(String uuid) {
        return exists(uuid);
    }

    @Override
    public NeutronVPNService getVPNService(String uuid) {
        return get(uuid);
    }

    @Override
    protected List<Vpnservice> getDataObjectList(VpnServices services) {
        return services.getVpnservice();
    }

    @Override
    public List<NeutronVPNService> getAllVPNService() {
        return getAll();
    }

    @Override
    public boolean addVPNService(NeutronVPNService input) {
        return add(input);
    }

    @Override
    public boolean removeVPNService(String uuid) {
        return remove(uuid);
    }

    @Override
    public boolean updateVPNService(String uuid, NeutronVPNService delta) {
        return update(uuid, delta);
    }

    @Override
    public boolean neutronVPNServiceInUse(String uuid) {
        return !exists(uuid);
    }

    protected NeutronVPNService fromMd(Vpnservice vpnService) {
        NeutronVPNService answer = new NeutronVPNService();
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
        VpnserviceBuilder vpnServiceBuilder = new VpnserviceBuilder();
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
    protected InstanceIdentifier<Vpnservice> createInstanceIdentifier(Vpnservice vpnService) {
        return InstanceIdentifier.create(Neutron.class)
                 .child(VpnServices.class)
                 .child(Vpnservice.class, vpnService.getKey());
    }

    @Override
    protected InstanceIdentifier<VpnServices> createInstanceIdentifier() {
        return InstanceIdentifier.create(Neutron.class)
                 .child(VpnServices.class);
    }

    @Override
    protected Vpnservice toMd(String uuid) {
        VpnserviceBuilder vpnServiceBuilder = new VpnserviceBuilder();
        vpnServiceBuilder.setUuid(toUuid(uuid));
        return vpnServiceBuilder.build();
    }

    public static void registerNewInterface(BundleContext context,
                                            ProviderContext providerContext,
                                            List<ServiceRegistration<?>> registrations) {
        NeutronVPNServiceInterface neutronVPNServiceInterface = new NeutronVPNServiceInterface(providerContext);
        ServiceRegistration<INeutronVPNServiceCRUD> neutronVPNServiceInterfaceRegistration = context.registerService(INeutronVPNServiceCRUD.class, neutronVPNServiceInterface, null);
        if(neutronVPNServiceInterfaceRegistration != null) {
            registrations.add(neutronVPNServiceInterfaceRegistration);
        }
    }
}
