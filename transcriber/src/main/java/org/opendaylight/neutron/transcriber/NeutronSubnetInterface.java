/*
 * Copyright (c) 2013, 2015 IBM Corporation and others.  All rights reserved.
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
import org.opendaylight.neutron.spi.INeutronSubnetCRUD;
import org.opendaylight.neutron.spi.NeutronSubnet;
import org.opendaylight.neutron.spi.NeutronSubnet_HostRoute;
import org.opendaylight.neutron.spi.NeutronSubnetIPAllocationPool;
import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.inet.types.rev100924.IpAddress;
import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.inet.types.rev100924.IpPrefix;
import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.inet.types.rev100924.IpPrefixBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.constants.rev150712.Dhcpv6Base;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.constants.rev150712.Dhcpv6Off;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.constants.rev150712.Dhcpv6Slaac;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.constants.rev150712.Dhcpv6Stateful;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.constants.rev150712.Dhcpv6Stateless;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.constants.rev150712.IpVersionBase;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.constants.rev150712.IpVersionV4;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.constants.rev150712.IpVersionV6;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.rev150712.Neutron;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.subnets.rev150712.subnet.attributes.AllocationPools;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.subnets.rev150712.subnet.attributes.AllocationPoolsBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.subnets.rev150712.subnet.attributes.HostRoutes;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.subnets.rev150712.subnet.attributes.HostRoutesBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.subnets.rev150712.subnets.attributes.Subnets;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.subnets.rev150712.subnets.attributes.subnets.Subnet;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.subnets.rev150712.subnets.attributes.subnets.SubnetBuilder;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableBiMap;

public class NeutronSubnetInterface extends AbstractNeutronInterface<Subnet, Subnets, NeutronSubnet> implements INeutronSubnetCRUD {
    private static final Logger LOGGER = LoggerFactory.getLogger(NeutronSubnetInterface.class);

    private static final int IPV4_VERSION = 4;
    private static final int IPV6_VERSION = 6;

    private static final ImmutableBiMap<Class<? extends IpVersionBase>,Integer> IPV_MAP
            = new ImmutableBiMap.Builder<Class<? extends IpVersionBase>,Integer>()
            .put(IpVersionV4.class,Integer.valueOf(IPV4_VERSION))
            .put(IpVersionV6.class,Integer.valueOf(IPV6_VERSION))
            .build();

    private static final ImmutableBiMap<Class<? extends Dhcpv6Base>,String> DHCPV6_MAP
            = new ImmutableBiMap.Builder<Class<? extends Dhcpv6Base>,String>()
            .put(Dhcpv6Off.class,"off")
            .put(Dhcpv6Stateful.class,"dhcpv6-stateful")
            .put(Dhcpv6Slaac.class,"slaac")
            .put(Dhcpv6Stateless.class,"dhcpv6-stateless")
            .build();

    NeutronSubnetInterface(ProviderContext providerContext) {
        super(providerContext);
    }

    // IfNBSubnetCRUD methods

    @Override
    public boolean subnetExists(String uuid) {
        return exists(uuid);
    }

    @Override
    public NeutronSubnet getSubnet(String uuid) {
        return get(uuid);
    }

    @Override
    protected List<Subnet> getDataObjectList(Subnets subnets) {
        return subnets.getSubnet();
    }

    @Override
    public List<NeutronSubnet> getAllSubnets() {
        return getAll();
    }

    @Override
    public boolean addSubnet(NeutronSubnet input) {
        return add(input);
    }

    @Override
    public boolean removeSubnet(String uuid) {
        return remove(uuid);
    }

    @Override
    public boolean updateSubnet(String uuid, NeutronSubnet delta) {
/* note: because what we get is *not* a delta but (at this point) the updated
 * object, this is much simpler - just replace the value and update the mdsal
 * with it */
        return update(uuid, delta);
    }

    protected NeutronSubnet fromMd(Subnet subnet) {
        NeutronSubnet result = new NeutronSubnet();
        result.setName(subnet.getName());
        result.setTenantID(subnet.getTenantId());
        result.setNetworkUUID(subnet.getNetworkId().getValue());
        result.setIpVersion(IPV_MAP.get(subnet.getIpVersion()));
        result.setCidr(String.valueOf(subnet.getCidr().getValue()));
        if (subnet.getGatewayIp() != null) {
            result.setGatewayIP(String.valueOf(subnet.getGatewayIp().getValue()));
        }
        if (subnet.getIpv6RaMode() != null) {
            result.setIpV6RaMode(DHCPV6_MAP.get(subnet.getIpv6RaMode()));
        }
        if (subnet.getIpv6AddressMode() != null) {
            result.setIpV6AddressMode(DHCPV6_MAP.get(subnet.getIpv6AddressMode()));
        }
        result.setEnableDHCP(subnet.isEnableDhcp());
        if (subnet.getAllocationPools() != null) {
            List<NeutronSubnetIPAllocationPool> allocationPools = new ArrayList<NeutronSubnetIPAllocationPool>();
            for (AllocationPools allocationPool : subnet.getAllocationPools()) {
                NeutronSubnetIPAllocationPool pool = new NeutronSubnetIPAllocationPool();
                pool.setPoolStart(String.valueOf(allocationPool.getStart().getValue()));
                pool.setPoolEnd(String.valueOf(allocationPool.getEnd().getValue()));
                allocationPools.add(pool);
            }
            result.setAllocationPools(allocationPools);
        }
        if (subnet.getDnsNameservers() != null) {
            List<String> dnsNameServers = new ArrayList<String>();
            for (IpAddress dnsNameServer : subnet.getDnsNameservers()) {
                dnsNameServers.add(String.valueOf(dnsNameServer.getValue()));
            }
            result.setDnsNameservers(dnsNameServers);
        }
        if(subnet.getHostRoutes() != null){
            List<NeutronSubnet_HostRoute> hostRoutes = new ArrayList<NeutronSubnet_HostRoute>();
            for(HostRoutes hostRoute : subnet.getHostRoutes()) {
                NeutronSubnet_HostRoute nsHostRoute = new NeutronSubnet_HostRoute();
                nsHostRoute.setDestination(String.valueOf(hostRoute.getDestination().getValue()));
                nsHostRoute.setNextHop(String.valueOf(hostRoute.getNexthop().getValue()));
                hostRoutes.add(nsHostRoute);
            }
            result.setHostRoutes(hostRoutes);
        }
        result.setID(subnet.getUuid().getValue());
        return result;
    }

    protected Subnet toMd(NeutronSubnet subnet) {
        SubnetBuilder subnetBuilder = new SubnetBuilder();
        if (subnet.getName() != null) {
            subnetBuilder.setName(subnet.getName());
        }
        if (subnet.getTenantID() != null) {
            subnetBuilder.setTenantId(toUuid(subnet.getTenantID()));
        }
        if (subnet.getNetworkUUID() != null) {
            subnetBuilder.setNetworkId(toUuid(subnet.getNetworkUUID()));
        }
        if (subnet.getIpVersion() != null) {
            ImmutableBiMap<Integer, Class<? extends IpVersionBase>> mapper =
                    IPV_MAP.inverse();
            subnetBuilder.setIpVersion((Class<? extends IpVersionBase>) mapper.get(subnet
                    .getIpVersion()));
        }
        if (subnet.getCidr() != null) {
            IpPrefix ipPrefix = IpPrefixBuilder.getDefaultInstance(subnet.getCidr());
            subnetBuilder.setCidr(ipPrefix);
        }
        if (subnet.getGatewayIP() != null) {
            IpAddress ipAddress = new IpAddress(subnet.getGatewayIP()
                    .toCharArray());
            subnetBuilder.setGatewayIp(ipAddress);
        }
        if (subnet.getIpV6RaMode() != null) {
            ImmutableBiMap<String, Class<? extends Dhcpv6Base>> mapper =
                    DHCPV6_MAP.inverse();
            subnetBuilder.setIpv6RaMode((Class<? extends Dhcpv6Base>) mapper.get(subnet.getIpV6RaMode()));
        }
        if (subnet.getIpV6AddressMode() != null) {
            ImmutableBiMap<String, Class<? extends Dhcpv6Base>> mapper =
                    DHCPV6_MAP.inverse();
            subnetBuilder.setIpv6AddressMode((Class<? extends Dhcpv6Base>) mapper.get(subnet.getIpV6AddressMode()));
        }
        subnetBuilder.setEnableDhcp(subnet.getEnableDHCP());
        if (subnet.getAllocationPools() != null) {
            List<AllocationPools> allocationPools = new ArrayList<AllocationPools>();
            for (NeutronSubnetIPAllocationPool allocationPool : subnet
                    .getAllocationPools()) {
                AllocationPoolsBuilder builder = new AllocationPoolsBuilder();
                builder.setStart(new IpAddress(allocationPool.getPoolStart().toCharArray()));
                builder.setEnd(new IpAddress(allocationPool.getPoolEnd().toCharArray()));
                AllocationPools temp = builder.build();
                allocationPools.add(temp);
            }
            subnetBuilder.setAllocationPools(allocationPools);
        }
        if (subnet.getDnsNameservers() != null) {
            List<IpAddress> dnsNameServers = new ArrayList<IpAddress>();
            for (String dnsNameServer : subnet.getDnsNameservers()) {
                IpAddress ipAddress = new IpAddress(dnsNameServer.toCharArray());
                dnsNameServers.add(ipAddress);
            }
            subnetBuilder.setDnsNameservers(dnsNameServers);
        }
        if(subnet.getHostRoutes() != null) {
            List<HostRoutes> hostRoutes = new ArrayList<HostRoutes>();
            for(NeutronSubnet_HostRoute hostRoute: subnet.getHostRoutes()) {
                HostRoutesBuilder hrBuilder = new HostRoutesBuilder();
                hrBuilder.setDestination(new IpPrefix(hostRoute.getDestination().toCharArray()));
                hrBuilder.setNexthop(new IpAddress(hostRoute.getNextHop().toCharArray()));
                hostRoutes.add(hrBuilder.build());
            }
            subnetBuilder.setHostRoutes(hostRoutes);
        }
        if (subnet.getID() != null) {
            subnetBuilder.setUuid(toUuid(subnet.getID()));
        } else {
            LOGGER.warn("Attempting to write neutron subnet without UUID");
        }
        return subnetBuilder.build();
    }

    @Override
    protected InstanceIdentifier<Subnet> createInstanceIdentifier(Subnet subnet) {
        return InstanceIdentifier.create(Neutron.class).child(Subnets.class)
                .child(Subnet.class, subnet.getKey());
    }

    @Override
    protected InstanceIdentifier<Subnets> createInstanceIdentifier() {
        return InstanceIdentifier.create(Neutron.class)
                .child(Subnets.class);
    }

    @Override
    protected Subnet toMd(String uuid) {
        SubnetBuilder subnetBuilder = new SubnetBuilder();
        subnetBuilder.setUuid(toUuid(uuid));
        return subnetBuilder.build();
    }

    public static void registerNewInterface(BundleContext context,
                                            ProviderContext providerContext,
                                            List<ServiceRegistration<?>> registrations) {
        NeutronSubnetInterface neutronSubnetInterface = new NeutronSubnetInterface(providerContext);
        ServiceRegistration<INeutronSubnetCRUD> neutronSubnetInterfaceRegistration = context.registerService(INeutronSubnetCRUD.class, neutronSubnetInterface, null);
        if(neutronSubnetInterfaceRegistration != null) {
            registrations.add(neutronSubnetInterfaceRegistration);
        }
    }
}
