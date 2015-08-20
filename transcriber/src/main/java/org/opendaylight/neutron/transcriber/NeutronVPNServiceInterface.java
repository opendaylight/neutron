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
import org.opendaylight.neutron.spi.INeutronVPNServiceCRUD;
import org.opendaylight.neutron.spi.NeutronVPNService;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.vpnaas.rev141002.vpnservices.attributes.VpnServices;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.vpnaas.rev141002.vpnservices.attributes.vpn.services.Vpnservice;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.vpnaas.rev141002.vpnservices.attributes.vpn.services.VpnserviceBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.rev150325.Neutron;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NeutronVPNServiceInterface extends AbstractNeutronInterface<Vpnservice,NeutronVPNService> implements INeutronVPNServiceCRUD {
    private static final Logger LOGGER = LoggerFactory.getLogger(NeutronVPNServiceInterface.class);
    private ConcurrentMap<String, NeutronVPNService> vpnServiceDB = new ConcurrentHashMap<String, NeutronVPNService>();


    NeutronVPNServiceInterface(ProviderContext providerContext) {
        super(providerContext);
    }

    @Override
    public boolean neutronVPNServiceExists(String uuid) {
        return vpnServiceDB.containsKey(uuid);
    }

    @Override
    public NeutronVPNService getVPNService(String uuid) {
        if (!neutronVPNServiceExists(uuid)) {
            LOGGER.debug("No VPNService Have Been Defined");
            return null;
        }
        return vpnServiceDB.get(uuid);
    }

    @Override
    public List<NeutronVPNService> getAllVPNService() {
        Set<NeutronVPNService> allVPNService = new HashSet<NeutronVPNService>();
        for (Entry<String, NeutronVPNService> entry : vpnServiceDB.entrySet()) {
            NeutronVPNService VPNService = entry.getValue();
            allVPNService.add(VPNService);
        }
        LOGGER.debug("Exiting getVPNService, Found {} OpenStackVPNService", allVPNService.size());
        List<NeutronVPNService> ans = new ArrayList<NeutronVPNService>();
        ans.addAll(allVPNService);
        return ans;
    }

    @Override
    public boolean addVPNService(NeutronVPNService input) {
        if (neutronVPNServiceExists(input.getID())) {
            return false;
        }
        vpnServiceDB.putIfAbsent(input.getID(), input);
        addMd(input);
        return true;
    }

    @Override
    public boolean removeVPNService(String uuid) {
        if (!neutronVPNServiceExists(uuid)) {
            return false;
        }
        vpnServiceDB.remove(uuid);
        removeMd(toMd(uuid));
        return true;
    }

    @Override
    public boolean updateVPNService(String uuid, NeutronVPNService delta) {
        if (!neutronVPNServiceExists(uuid)) {
            return false;
        }
        NeutronVPNService target = vpnServiceDB.get(uuid);
        boolean rc = overwrite(target, delta);
        if (rc) {
            updateMd(vpnServiceDB.get(uuid));
        }
        return rc;
    }

    @Override
    public boolean neutronVPNServiceInUse(String uuid) {
        return !neutronVPNServiceExists(uuid);
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
        if (vpnService.getDescription() != null) {
            vpnServiceBuilder.setDescr(vpnService.getDescription());
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
