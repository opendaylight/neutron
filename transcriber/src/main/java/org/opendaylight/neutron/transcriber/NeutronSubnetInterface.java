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
import org.opendaylight.neutron.spi.INeutronNetworkCRUD;
import org.opendaylight.neutron.spi.INeutronSubnetCRUD;
import org.opendaylight.neutron.spi.NeutronCRUDInterfaces;
import org.opendaylight.neutron.spi.NeutronNetwork;
import org.opendaylight.neutron.spi.NeutronSubnet;
import org.opendaylight.neutron.spi.NeutronSubnetIPAllocationPool;
import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.inet.types.rev100924.IpAddress;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.rev150325.Neutron;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.subnets.rev141002.SubnetAttributes;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.subnets.rev141002.subnet.attributes.AllocationPools;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.subnets.rev141002.subnet.attributes.AllocationPoolsBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.subnets.rev141002.subnets.attributes.Subnets;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.subnets.rev141002.subnets.attributes.subnets.Subnet;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.subnets.rev141002.subnets.attributes.subnets.SubnetBuilder;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NeutronSubnetInterface extends AbstractNeutronInterface<Subnet, NeutronSubnet> implements INeutronSubnetCRUD {
    private static final Logger LOGGER = LoggerFactory.getLogger(NeutronSubnetInterface.class);
    private ConcurrentMap<String, NeutronSubnet> subnetDB  = new ConcurrentHashMap<String, NeutronSubnet>();


    NeutronSubnetInterface(ProviderContext providerContext) {
        super(providerContext);
    }

    // IfNBSubnetCRUD methods

    @Override
    public boolean subnetExists(String uuid) {
        return subnetDB.containsKey(uuid);
    }

    @Override
    public NeutronSubnet getSubnet(String uuid) {
        if (!subnetExists(uuid)) {
            return null;
        }
        return subnetDB.get(uuid);
    }

    @Override
    public List<NeutronSubnet> getAllSubnets() {
        Set<NeutronSubnet> allSubnets = new HashSet<NeutronSubnet>();
        for (Entry<String, NeutronSubnet> entry : subnetDB.entrySet()) {
            NeutronSubnet subnet = entry.getValue();
            allSubnets.add(subnet);
        }
        LOGGER.debug("Exiting getAllSubnets, Found {} OpenStackSubnets", allSubnets.size());
        List<NeutronSubnet> ans = new ArrayList<NeutronSubnet>();
        ans.addAll(allSubnets);
        return ans;
    }

    @Override
    public boolean addSubnet(NeutronSubnet input) {
        String id = input.getID();
        if (subnetExists(id)) {
            return false;
        }
        subnetDB.putIfAbsent(id, input);
        addMd(input);
        NeutronCRUDInterfaces interfaces = new NeutronCRUDInterfaces()
            .fetchINeutronNetworkCRUD(this);
        INeutronNetworkCRUD networkIf = interfaces.getNetworkInterface();

        NeutronNetwork targetNet = networkIf.getNetwork(input.getNetworkUUID());
        targetNet.addSubnet(id);
        return true;
    }

    @Override
    public boolean removeSubnet(String uuid) {
        if (!subnetExists(uuid)) {
            return false;
        }
        NeutronSubnet target = subnetDB.get(uuid);
        NeutronCRUDInterfaces interfaces = new NeutronCRUDInterfaces()
            .fetchINeutronNetworkCRUD(this);
        INeutronNetworkCRUD networkIf = interfaces.getNetworkInterface();

        NeutronNetwork targetNet = networkIf.getNetwork(target.getNetworkUUID());
        targetNet.removeSubnet(uuid);
        subnetDB.remove(uuid);
        removeMd(toMd(uuid));
        return true;
    }

    @Override
    public boolean updateSubnet(String uuid, NeutronSubnet delta) {
        if (!subnetExists(uuid)) {
            return false;
        }
/* note: because what we get is *not* a delta but (at this point) the updated
 * object, this is much simpler - just replace the value and update the mdsal
 * with it */
        subnetDB.replace(uuid, delta);
        updateMd(delta);
        return true;
    }

    @Override
    public boolean subnetInUse(String subnetUUID) {
        if (!subnetExists(subnetUUID)) {
            return true;
        }
        NeutronSubnet target = subnetDB.get(subnetUUID);
	// note: we want to ignore the DHCP ports, so...
        return (target.getPortsInSubnet("network:dhcp").size() > 0);
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
                        subnetBuilder.setIpVersion(SubnetAttributes.IpVersion.forValue(subnet
                                        .getIpVersion()));
                }
                if (subnet.getCidr() != null) {
                        subnetBuilder.setCidr(subnet.getCidr());
                }
                if (subnet.getGatewayIP() != null) {
                        IpAddress ipAddress = new IpAddress(subnet.getGatewayIP()
                                        .toCharArray());
                        subnetBuilder.setGatewayIp(ipAddress);
                }
                if (subnet.getIpV6RaMode() != null) {
                    boolean foundMatch = false;
                    for (SubnetAttributes.Ipv6RaMode ipv6RaMode : SubnetAttributes.Ipv6RaMode.values()) {
                        if (ipv6RaMode.toString().equalsIgnoreCase(subnet.getIpV6RaMode())) {
                            subnetBuilder.setIpv6RaMode(ipv6RaMode);
                            foundMatch = true;
                            break;
                        }
                    }
                    if (! foundMatch) {
                        LOGGER.warn("Unable to find Ipv6RaMode value for {}", subnet.getIpV6RaMode());
                    }
                }
                if (subnet.getIpV6AddressMode() != null) {
                    boolean foundMatch = false;
                    for (SubnetAttributes.Ipv6AddressMode ipv6AddressMode : SubnetAttributes.Ipv6AddressMode.values()) {
                        if (ipv6AddressMode.toString().equalsIgnoreCase(subnet.getIpV6AddressMode())) {
                            subnetBuilder.setIpv6AddressMode(ipv6AddressMode);
                            foundMatch = true;
                            break;
                        }
                    }
                    if (! foundMatch) {
                        LOGGER.warn("Unable to find IpV6AddressMode value for {}", subnet.getIpV6AddressMode());
                    }
                }
                subnetBuilder.setEnableDhcp(subnet.getEnableDHCP());
                if (subnet.getAllocationPools() != null) {
                        List<AllocationPools> allocationPools = new ArrayList<AllocationPools>();
                        for (NeutronSubnetIPAllocationPool allocationPool : subnet
                                        .getAllocationPools()) {
                                AllocationPoolsBuilder builder = new AllocationPoolsBuilder();
                                builder.setStart(allocationPool.getPoolStart());
                                builder.setEnd(allocationPool.getPoolEnd());
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
        protected Subnet toMd(String uuid) {
                SubnetBuilder subnetBuilder = new SubnetBuilder();
                subnetBuilder.setUuid(toUuid(uuid));
                return subnetBuilder.build();
        }

    public static void registerNewInterface(BundleContext context,
                                            ProviderContext providerContext,
                                            List<ServiceRegistration<?>> registrations) throws Exception {
        NeutronSubnetInterface neutronSubnetInterface = new NeutronSubnetInterface(providerContext);
        ServiceRegistration<INeutronSubnetCRUD> neutronSubnetInterfaceRegistration = context.registerService(INeutronSubnetCRUD.class, neutronSubnetInterface, null);
        if(neutronSubnetInterfaceRegistration != null) {
            registrations.add(neutronSubnetInterfaceRegistration);
        }
    }
}
