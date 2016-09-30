/*
 * Copyright (c) 2013, 2015 IBM Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.transcriber;

import com.google.common.collect.ImmutableBiMap;
import java.util.ArrayList;
import java.util.List;
import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.neutron.spi.INeutronSubnetCRUD;
import org.opendaylight.neutron.spi.NeutronRoute;
import org.opendaylight.neutron.spi.NeutronSubnet;
import org.opendaylight.neutron.spi.NeutronSubnetIPAllocationPool;
import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.inet.types.rev130715.IpAddress;
import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.inet.types.rev130715.IpPrefix;
import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.inet.types.rev130715.IpPrefixBuilder;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class NeutronSubnetInterface extends AbstractNeutronInterface<Subnet, Subnets, NeutronSubnet>
        implements INeutronSubnetCRUD {
    private static final Logger LOGGER = LoggerFactory.getLogger(NeutronSubnetInterface.class);

    private static final int IPV4_VERSION = 4;
    private static final int IPV6_VERSION = 6;

    private static final ImmutableBiMap<Class<? extends IpVersionBase>,
            Integer> IPV_MAP = new ImmutableBiMap.Builder<Class<? extends IpVersionBase>, Integer>()
                    .put(IpVersionV4.class, Integer.valueOf(IPV4_VERSION))
                    .put(IpVersionV6.class, Integer.valueOf(IPV6_VERSION)).build();

    private static final ImmutableBiMap<Class<? extends Dhcpv6Base>,
            String> DHCPV6_MAP = new ImmutableBiMap.Builder<Class<? extends Dhcpv6Base>, String>()
                    .put(Dhcpv6Off.class, "off").put(Dhcpv6Stateful.class, "dhcpv6-stateful")
                    .put(Dhcpv6Slaac.class, "slaac").put(Dhcpv6Stateless.class, "dhcpv6-stateless").build();

    NeutronSubnetInterface(DataBroker db) {
        super(SubnetBuilder.class, db);
    }

    // IfNBSubnetCRUD methods

    @Override
    protected List<Subnet> getDataObjectList(Subnets subnets) {
        return subnets.getSubnet();
    }

    protected NeutronSubnet fromMd(Subnet subnet) {
        final NeutronSubnet result = new NeutronSubnet();
        fromMdBaseAttributes(subnet, result);
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
            final List<NeutronSubnetIPAllocationPool> allocationPools = new ArrayList<NeutronSubnetIPAllocationPool>();
            for (final AllocationPools allocationPool : subnet.getAllocationPools()) {
                final NeutronSubnetIPAllocationPool pool = new NeutronSubnetIPAllocationPool();
                pool.setPoolStart(String.valueOf(allocationPool.getStart().getValue()));
                pool.setPoolEnd(String.valueOf(allocationPool.getEnd().getValue()));
                allocationPools.add(pool);
            }
            result.setAllocationPools(allocationPools);
        }
        if (subnet.getDnsNameservers() != null) {
            final List<String> dnsNameServers = new ArrayList<String>();
            for (final IpAddress dnsNameServer : subnet.getDnsNameservers()) {
                dnsNameServers.add(String.valueOf(dnsNameServer.getValue()));
            }
            result.setDnsNameservers(dnsNameServers);
        }
        if (subnet.getHostRoutes() != null) {
            final List<NeutronRoute> hostRoutes = new ArrayList<NeutronRoute>();
            for (final HostRoutes hostRoute : subnet.getHostRoutes()) {
                final NeutronRoute nsHostRoute = new NeutronRoute();
                nsHostRoute.setDestination(String.valueOf(hostRoute.getDestination().getValue()));
                nsHostRoute.setNextHop(String.valueOf(hostRoute.getNexthop().getValue()));
                hostRoutes.add(nsHostRoute);
            }
            result.setHostRoutes(hostRoutes);
        }
        return result;
    }

    protected Subnet toMd(NeutronSubnet subnet) {
        final SubnetBuilder subnetBuilder = new SubnetBuilder();
        toMdBaseAttributes(subnet, subnetBuilder);
        if (subnet.getNetworkUUID() != null) {
            subnetBuilder.setNetworkId(toUuid(subnet.getNetworkUUID()));
        }
        if (subnet.getIpVersion() != null) {
            final ImmutableBiMap<Integer, Class<? extends IpVersionBase>> mapper = IPV_MAP.inverse();
            subnetBuilder.setIpVersion((Class<? extends IpVersionBase>) mapper.get(subnet.getIpVersion()));
        }
        if (subnet.getCidr() != null) {
            final IpPrefix ipPrefix = IpPrefixBuilder.getDefaultInstance(subnet.getCidr());
            subnetBuilder.setCidr(ipPrefix);
        }
        if (subnet.getGatewayIP() != null) {
            final IpAddress ipAddress = new IpAddress(subnet.getGatewayIP().toCharArray());
            subnetBuilder.setGatewayIp(ipAddress);
        }
        if (subnet.getIpV6RaMode() != null) {
            final ImmutableBiMap<String, Class<? extends Dhcpv6Base>> mapper = DHCPV6_MAP.inverse();
            subnetBuilder.setIpv6RaMode((Class<? extends Dhcpv6Base>) mapper.get(subnet.getIpV6RaMode()));
        }
        if (subnet.getIpV6AddressMode() != null) {
            final ImmutableBiMap<String, Class<? extends Dhcpv6Base>> mapper = DHCPV6_MAP.inverse();
            subnetBuilder.setIpv6AddressMode((Class<? extends Dhcpv6Base>) mapper.get(subnet.getIpV6AddressMode()));
        }
        subnetBuilder.setEnableDhcp(subnet.getEnableDHCP());
        if (subnet.getAllocationPools() != null) {
            final List<AllocationPools> allocationPools = new ArrayList<AllocationPools>();
            for (final NeutronSubnetIPAllocationPool allocationPool : subnet.getAllocationPools()) {
                final AllocationPoolsBuilder builder = new AllocationPoolsBuilder();
                builder.setStart(new IpAddress(allocationPool.getPoolStart().toCharArray()));
                builder.setEnd(new IpAddress(allocationPool.getPoolEnd().toCharArray()));
                final AllocationPools temp = builder.build();
                allocationPools.add(temp);
            }
            subnetBuilder.setAllocationPools(allocationPools);
        }
        if (subnet.getDnsNameservers() != null) {
            final List<IpAddress> dnsNameServers = new ArrayList<IpAddress>();
            for (final String dnsNameServer : subnet.getDnsNameservers()) {
                final IpAddress ipAddress = new IpAddress(dnsNameServer.toCharArray());
                dnsNameServers.add(ipAddress);
            }
            subnetBuilder.setDnsNameservers(dnsNameServers);
        }
        if (subnet.getHostRoutes() != null) {
            final List<HostRoutes> hostRoutes = new ArrayList<HostRoutes>();
            for (final NeutronRoute hostRoute : subnet.getHostRoutes()) {
                final HostRoutesBuilder hrBuilder = new HostRoutesBuilder();
                hrBuilder.setDestination(new IpPrefix(hostRoute.getDestination().toCharArray()));
                hrBuilder.setNexthop(new IpAddress(hostRoute.getNextHop().toCharArray()));
                hostRoutes.add(hrBuilder.build());
            }
            subnetBuilder.setHostRoutes(hostRoutes);
        }
        return subnetBuilder.build();
    }

    @Override
    protected InstanceIdentifier<Subnet> createInstanceIdentifier(Subnet subnet) {
        return InstanceIdentifier.create(Neutron.class).child(Subnets.class).child(Subnet.class, subnet.getKey());
    }
}
