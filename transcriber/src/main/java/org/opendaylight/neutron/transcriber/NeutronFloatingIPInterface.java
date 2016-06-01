/*
 * Copyright (c) 2013, 2015 IBM Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.transcriber;

import java.util.List;

import org.opendaylight.controller.sal.binding.api.BindingAwareBroker.ProviderContext;
import org.opendaylight.neutron.spi.INeutronFloatingIPCRUD;
import org.opendaylight.neutron.spi.NeutronFloatingIP;
import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.inet.types.rev100924.IpAddress;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.l3.rev150712.floatingips.attributes.Floatingips;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.l3.rev150712.floatingips.attributes.floatingips.Floatingip;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.l3.rev150712.floatingips.attributes.floatingips.FloatingipBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.rev150712.Neutron;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NeutronFloatingIPInterface extends AbstractNeutronInterface<Floatingip, Floatingips, NeutronFloatingIP> implements INeutronFloatingIPCRUD {
    private static final Logger LOGGER = LoggerFactory.getLogger(NeutronFloatingIPInterface.class);

    NeutronFloatingIPInterface(ProviderContext providerContext) {
        super(providerContext);
    }

    // IfNBFloatingIPCRUD interface methods

    @Override
    public boolean floatingIPExists(String uuid) {
        return exists(uuid);
    }

    @Override
    public NeutronFloatingIP getFloatingIP(String uuid) {
        return get(uuid);
    }

    @Override
    protected List<Floatingip> getDataObjectList(Floatingips fips) {
        return fips.getFloatingip();
    }

    @Override
    public List<NeutronFloatingIP> getAllFloatingIPs() {
        return getAll();
    }

    @Override
    public boolean addFloatingIP(NeutronFloatingIP input) {
        return add(input);
    }

    @Override
    public boolean removeFloatingIP(String uuid) {
        return remove(uuid);
    }

    @Override
    public boolean updateFloatingIP(String uuid, NeutronFloatingIP delta) {
        NeutronFloatingIP target = getFloatingIP(uuid);
        if (target == null) {
            return false;
        }
        target.setPortUUID(delta.getPortUUID());
        target.setFixedIPAddress(delta.getFixedIPAddress());
        return updateMd(target);
    }

    @Override
    protected Floatingip toMd(String uuid) {
        FloatingipBuilder floatingipBuilder = new FloatingipBuilder();
        floatingipBuilder.setUuid(toUuid(uuid));
        return floatingipBuilder.build();
    }

    @Override
    protected Floatingip toMd(NeutronFloatingIP floatingIp) {
        FloatingipBuilder floatingipBuilder = new FloatingipBuilder();
        if (floatingIp.getFixedIPAddress() != null) {
            floatingipBuilder.setFixedIpAddress(new IpAddress(floatingIp.getFixedIPAddress().toCharArray()));
        }
        if(floatingIp.getFloatingIPAddress() != null) {
            floatingipBuilder.setFloatingIpAddress(new IpAddress(floatingIp.getFloatingIPAddress().toCharArray()));
        }
        if (floatingIp.getFloatingNetworkUUID() != null) {
            floatingipBuilder.setFloatingNetworkId(toUuid(floatingIp.getFloatingNetworkUUID()));
        }
        if (floatingIp.getPortUUID() != null) {
            floatingipBuilder.setPortId(toUuid(floatingIp.getPortUUID()));
        }
        if (floatingIp.getRouterUUID() != null) {
            floatingipBuilder.setRouterId(toUuid(floatingIp.getRouterUUID()));
        }
        if (floatingIp.getStatus() != null) {
            floatingipBuilder.setStatus(floatingIp.getStatus());
        }
        if (floatingIp.getTenantID() != null) {
            floatingipBuilder.setTenantId(toUuid(floatingIp.getTenantID()));
        }
        if (floatingIp.getID() != null) {
            floatingipBuilder.setUuid(toUuid(floatingIp.getID()));
        }
        else {
            LOGGER.warn("Attempting to write neutron floating IP without UUID");
        }
        return floatingipBuilder.build();
    }

    protected NeutronFloatingIP fromMd(Floatingip fip) {
        NeutronFloatingIP result = new NeutronFloatingIP();
        result.setID(fip.getUuid().getValue());
        if (fip.getFloatingNetworkId() != null) {
            result.setFloatingNetworkUUID(String.valueOf(fip.getFloatingNetworkId().getValue()));
        }
        if (fip.getPortId() != null) {
            result.setPortUUID(String.valueOf(fip.getPortId().getValue()));
        }
        if (fip.getFixedIpAddress() != null ) {
            result.setFixedIPAddress(String.valueOf(fip.getFixedIpAddress().getValue()));
        }
        if (fip.getFloatingIpAddress() != null) {
            result.setFloatingIPAddress(String.valueOf(fip.getFloatingIpAddress().getValue()));
        }
        if (fip.getTenantId() != null) {
            result.setTenantID(fip.getTenantId());
        }
        if (fip.getRouterId() != null) {
            result.setRouterUUID(String.valueOf(fip.getRouterId().getValue()));
        }
        result.setStatus(fip.getStatus());
        return result;
    }

    @Override
    protected InstanceIdentifier<Floatingip> createInstanceIdentifier(
            Floatingip item) {
        return InstanceIdentifier.create(Neutron.class)
                .child(Floatingips.class)
                .child(Floatingip.class,item.getKey());
    }

    @Override
    protected InstanceIdentifier<Floatingips> createInstanceIdentifier() {
        return InstanceIdentifier.create(Neutron.class)
                .child(Floatingips.class);
    }

    public static void registerNewInterface(BundleContext context,
                                            ProviderContext providerContext,
                                            List<ServiceRegistration<?>> registrations) {
        NeutronFloatingIPInterface neutronFloatingIPInterface = new NeutronFloatingIPInterface(providerContext);
        ServiceRegistration<INeutronFloatingIPCRUD> neutronFloatingIPInterfaceRegistration = context.registerService(INeutronFloatingIPCRUD.class, neutronFloatingIPInterface, null);
        if (neutronFloatingIPInterfaceRegistration != null) {
            registrations.add(neutronFloatingIPInterfaceRegistration);
        }
    }
}
