/*
 * Copyright IBM Corporation, 2013.  All rights reserved.
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
import org.opendaylight.neutron.spi.INeutronFloatingIPCRUD;
import org.opendaylight.neutron.spi.INeutronNetworkCRUD;
import org.opendaylight.neutron.spi.INeutronPortCRUD;
import org.opendaylight.neutron.spi.INeutronSubnetCRUD;
import org.opendaylight.neutron.spi.NeutronCRUDInterfaces;
import org.opendaylight.neutron.spi.NeutronFloatingIP;
import org.opendaylight.neutron.spi.NeutronPort;
import org.opendaylight.neutron.spi.NeutronSubnet;
import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.inet.types.rev100924.IpAddress;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.l3.rev141002.floatingips.attributes.Floatingips;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.l3.rev141002.floatingips.attributes.floatingips.Floatingip;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.l3.rev141002.floatingips.attributes.floatingips.FloatingipBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.l3.rev141002.l3.floatingip.attributes.FixedIpAddress;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.l3.rev141002.l3.floatingip.attributes.FixedIpAddressBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.rev150325.Neutron;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NeutronFloatingIPInterface extends AbstractNeutronInterface<Floatingip, NeutronFloatingIP> implements INeutronFloatingIPCRUD {
    private static final Logger LOGGER = LoggerFactory.getLogger(NeutronFloatingIPInterface.class);

    private ConcurrentMap<String, NeutronFloatingIP> floatingIPDB  = new ConcurrentHashMap<String, NeutronFloatingIP>();

    NeutronFloatingIPInterface(ProviderContext providerContext) {
        super(providerContext);
    }

    // IfNBFloatingIPCRUD interface methods

    @Override
    public boolean floatingIPExists(String uuid) {
        return floatingIPDB.containsKey(uuid);
    }

    @Override
    public NeutronFloatingIP getFloatingIP(String uuid) {
        if (!floatingIPExists(uuid)) {
            return null;
        }
        return floatingIPDB.get(uuid);
    }

    @Override
    public List<NeutronFloatingIP> getAllFloatingIPs() {
        Set<NeutronFloatingIP> allIPs = new HashSet<NeutronFloatingIP>();
        for (Entry<String, NeutronFloatingIP> entry : floatingIPDB.entrySet()) {
            NeutronFloatingIP floatingip = entry.getValue();
            allIPs.add(floatingip);
        }
        LOGGER.debug("Exiting getAllFloatingIPs, Found {} FloatingIPs", allIPs.size());
        List<NeutronFloatingIP> ans = new ArrayList<NeutronFloatingIP>();
        ans.addAll(allIPs);
        return ans;
    }

    @Override
    public boolean addFloatingIP(NeutronFloatingIP input) {
        NeutronCRUDInterfaces interfaces = new NeutronCRUDInterfaces()
            .fetchINeutronNetworkCRUD(this)
            .fetchINeutronSubnetCRUD(this)
            .fetchINeutronPortCRUD(this);
        INeutronNetworkCRUD networkCRUD = interfaces.getNetworkInterface();
        INeutronSubnetCRUD subnetCRUD = interfaces.getSubnetInterface();
        INeutronPortCRUD portCRUD = interfaces.getPortInterface();

        if (floatingIPExists(input.getID())) {
            return false;
        }
        //if floating_ip_address isn't there, allocate from the subnet pool
        NeutronSubnet subnet = subnetCRUD.getSubnet(networkCRUD.getNetwork(input.getFloatingNetworkUUID()).getSubnets().get(0));
        if (input.getFloatingIPAddress() == null) {
            input.setFloatingIPAddress(subnet.getLowAddr());
        }
        floatingIPDB.putIfAbsent(input.getID(), input);
        return true;
    }

    @Override
    public boolean removeFloatingIP(String uuid) {
        NeutronCRUDInterfaces interfaces = new NeutronCRUDInterfaces()
            .fetchINeutronPortCRUD(this);
        INeutronPortCRUD portCRUD = interfaces.getPortInterface();

        if (!floatingIPExists(uuid)) {
            return false;
        }
        floatingIPDB.remove(uuid);
        return true;
    }

    @Override
    public boolean updateFloatingIP(String uuid, NeutronFloatingIP delta) {
        NeutronCRUDInterfaces interfaces = new NeutronCRUDInterfaces()
            .fetchINeutronPortCRUD(this);
        INeutronPortCRUD portCRUD = interfaces.getPortInterface();

        if (!floatingIPExists(uuid)) {
            return false;
        }
        NeutronFloatingIP target = floatingIPDB.get(uuid);
        target.setPortUUID(delta.getPortUUID());
        target.setFixedIPAddress(delta.getFixedIPAddress());
        return true;
    }

    @Override
    protected Floatingip toMd(String uuid) {
        FloatingipBuilder floatingipBuilder = new FloatingipBuilder();
        floatingipBuilder.setUuid(toUuid(uuid));
        return floatingipBuilder.build();
    }

    @Override
    protected InstanceIdentifier<Floatingip> createInstanceIdentifier(
            Floatingip item) {
        return InstanceIdentifier.create(Neutron.class)
                .child(Floatingips.class)
                .child(Floatingip.class,item.getKey());
    }

    @Override
    protected Floatingip toMd(NeutronFloatingIP floatingIp) {
        FloatingipBuilder floatingipBuilder = new FloatingipBuilder();
        if (floatingIp.getFixedIPAddress() != null) {
            List<FixedIpAddress> listFixedIpAddress = new ArrayList<FixedIpAddress>();
            FixedIpAddressBuilder fixedIpAddressBuilder = new FixedIpAddressBuilder();
            fixedIpAddressBuilder.setIpAddress(new IpAddress(floatingIp.getFixedIPAddress().toCharArray()));
            listFixedIpAddress.add(fixedIpAddressBuilder.build());
            floatingipBuilder.setFixedIpAddress(listFixedIpAddress );
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
        if (floatingIp.getTenantUUID() != null) {
            floatingipBuilder.setTenantId(toUuid(floatingIp.getTenantUUID()));
        }
        if (floatingIp.getFloatingIPUUID() != null) {
            floatingipBuilder.setUuid(toUuid(floatingIp.getFloatingIPUUID()));
        }
        else {
            LOGGER.warn("Attempting to write neutron floating IP without UUID");
        }
        return floatingipBuilder.build();
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
