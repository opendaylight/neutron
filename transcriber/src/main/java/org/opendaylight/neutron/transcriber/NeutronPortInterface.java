/*
 * Copyright IBM Corporation, 2013.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.transcriber;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.opendaylight.controller.sal.binding.api.BindingAwareBroker.ProviderContext;
import org.opendaylight.neutron.spi.INeutronNetworkCRUD;
import org.opendaylight.neutron.spi.INeutronPortCRUD;
import org.opendaylight.neutron.spi.INeutronSubnetCRUD;
import org.opendaylight.neutron.spi.NeutronCRUDInterfaces;
import org.opendaylight.neutron.spi.NeutronNetwork;
import org.opendaylight.neutron.spi.NeutronPort;
import org.opendaylight.neutron.spi.NeutronPort_AllowedAddressPairs;
import org.opendaylight.neutron.spi.NeutronPort_ExtraDHCPOption;
import org.opendaylight.neutron.spi.NeutronSecurityGroup;
import org.opendaylight.neutron.spi.NeutronSubnet;
import org.opendaylight.neutron.spi.Neutron_IPs;
import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.inet.types.rev100924.IpAddress;
import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.yang.types.rev130715.Uuid;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.ports.rev141002.port.attrs.AllowedAddressPairs;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.ports.rev141002.port.attrs.AllowedAddressPairsBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.ports.rev141002.port.attrs.ExtraDhcpOpts;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.ports.rev141002.port.attrs.ExtraDhcpOptsBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.ports.rev141002.port.attrs.FixedIps;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.ports.rev141002.port.attrs.FixedIpsBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.ports.rev141002.ports.attributes.Ports;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.ports.rev141002.ports.attributes.ports.Port;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.ports.rev141002.ports.attributes.ports.PortBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.rev150325.Neutron;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NeutronPortInterface extends AbstractNeutronInterface<Port, NeutronPort> implements INeutronPortCRUD {
    private static final Logger LOGGER = LoggerFactory.getLogger(NeutronPortInterface.class);
    private ConcurrentMap<String, NeutronPort> portDB = new ConcurrentHashMap<String, NeutronPort>();


    NeutronPortInterface(ProviderContext providerContext) {
        super(providerContext);
    }

    // this method uses reflection to update an object from it's delta.

    private boolean overwrite(Object target, Object delta) {
        Method[] methods = target.getClass().getMethods();

        for(Method toMethod: methods){
            if(toMethod.getDeclaringClass().equals(target.getClass())
                    && toMethod.getName().startsWith("set")){

                String toName = toMethod.getName();
                String fromName = toName.replace("set", "get");

                try {
                    Method fromMethod = delta.getClass().getMethod(fromName);
                    Object value = fromMethod.invoke(delta, (Object[])null);
                    if(value != null){
                        toMethod.invoke(target, value);
                    }
                } catch (Exception e) {
                    LOGGER.error(e.getMessage());
                    return false;
                }
            }
        }
        return true;
    }

    // IfNBPortCRUD methods

    @Override
    public boolean portExists(String uuid) {
        return portDB.containsKey(uuid);
    }

    @Override
    public NeutronPort getPort(String uuid) {
        if (!portExists(uuid)) {
            return null;
        }
        return portDB.get(uuid);
    }

    @Override
    public List<NeutronPort> getAllPorts() {
        Set<NeutronPort> allPorts = new HashSet<NeutronPort>();
        for (Entry<String, NeutronPort> entry : portDB.entrySet()) {
            NeutronPort port = entry.getValue();
            allPorts.add(port);
        }
        LOGGER.debug("Exiting getAllPorts, Found {} OpenStackPorts", allPorts.size());
        List<NeutronPort> ans = new ArrayList<NeutronPort>();
        ans.addAll(allPorts);
        return ans;
    }

    @Override
    public boolean addPort(NeutronPort input) {
        if (portExists(input.getID())) {
            return false;
        }
        portDB.putIfAbsent(input.getID(), input);
        // if there are no fixed IPs, allocate one for each subnet in the network
        INeutronSubnetCRUD systemCRUD = NeutronCRUDInterfaces.getINeutronSubnetCRUD(this);
        if (input.getFixedIPs() == null){
           input.setFixedIPs(new ArrayList<Neutron_IPs>());
        }
        if (input.getFixedIPs().size() == 0) {
            List<Neutron_IPs> list = input.getFixedIPs();
            Iterator<NeutronSubnet> subnetIterator = systemCRUD.getAllSubnets().iterator();
            while (subnetIterator.hasNext()) {
                NeutronSubnet subnet = subnetIterator.next();
                if (subnet.getNetworkUUID().equals(input.getNetworkUUID())) {
                    list.add(new Neutron_IPs(subnet.getID()));
                }
            }
        }
        Iterator<Neutron_IPs> fixedIPIterator = input.getFixedIPs().iterator();
        while (fixedIPIterator.hasNext()) {
            Neutron_IPs ip = fixedIPIterator.next();
            NeutronSubnet subnet = systemCRUD.getSubnet(ip.getSubnetUUID());
            if (ip.getIpAddress() == null) {
                ip.setIpAddress(subnet.getLowAddr());
            }
            if (ip.getIpAddress().equals(subnet.getGatewayIP())) {
                subnet.setGatewayIPAllocated();
            }
            subnet.addPort(input);
        }
        INeutronNetworkCRUD networkIf = NeutronCRUDInterfaces.getINeutronNetworkCRUD(this);

        NeutronNetwork network = networkIf.getNetwork(input.getNetworkUUID());
        network.addPort(input);
        return true;
    }

    @Override
    public boolean removePort(String uuid) {
        if (!portExists(uuid)) {
            return false;
        }
        NeutronPort port = getPort(uuid);
        portDB.remove(uuid);
        INeutronNetworkCRUD networkCRUD = NeutronCRUDInterfaces.getINeutronNetworkCRUD(this);
        INeutronSubnetCRUD systemCRUD = NeutronCRUDInterfaces.getINeutronSubnetCRUD(this);

        NeutronNetwork network = networkCRUD.getNetwork(port.getNetworkUUID());
        network.removePort(port);
        Iterator<Neutron_IPs> fixedIPIterator = port.getFixedIPs().iterator();
        while (fixedIPIterator.hasNext()) {
            Neutron_IPs ip = fixedIPIterator.next();
            NeutronSubnet subnet = systemCRUD.getSubnet(ip.getSubnetUUID());
            if (subnet != null) {
                if (ip.getIpAddress().equals(subnet.getGatewayIP())) {
                    subnet.resetGatewayIPAllocated();
                }
                subnet.removePort(port);
            }
        }
        return true;
    }

    @Override
    public boolean updatePort(String uuid, NeutronPort delta) {
        if (!portExists(uuid)) {
            return false;
        }
        NeutronPort target = portDB.get(uuid);
        if (delta.getFixedIPs() != null) {
            NeutronPort port = getPort(uuid);
            INeutronSubnetCRUD systemCRUD = NeutronCRUDInterfaces.getINeutronSubnetCRUD(this);
            for (Neutron_IPs ip: delta.getFixedIPs()) {
                NeutronSubnet subnet = systemCRUD.getSubnet(ip.getSubnetUUID());
                if (ip.getIpAddress() == null) {
                    ip.setIpAddress(subnet.getLowAddr());
                }
            }
        }
        return overwrite(target, delta);
    }

    @Override
    public boolean macInUse(String macAddress) {
        List<NeutronPort> ports = getAllPorts();
        Iterator<NeutronPort> portIterator = ports.iterator();
        while (portIterator.hasNext()) {
            NeutronPort port = portIterator.next();
            if (macAddress.equalsIgnoreCase(port.getMacAddress())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public NeutronPort getGatewayPort(String subnetUUID) {
        INeutronSubnetCRUD systemCRUD = NeutronCRUDInterfaces.getINeutronSubnetCRUD(this);
        NeutronSubnet subnet = systemCRUD.getSubnet(subnetUUID);
        Iterator<NeutronPort> portIterator = getAllPorts().iterator();
        while (portIterator.hasNext()) {
            NeutronPort port = portIterator.next();
            List<Neutron_IPs> fixedIPs = port.getFixedIPs();
            if (fixedIPs.size() == 1) {
                if (subnet.getGatewayIP().equals(fixedIPs.get(0).getIpAddress())) {
                    return port;
                }
            }
        }
        return null;
    }

    @Override
    protected InstanceIdentifier<Port> createInstanceIdentifier(Port port) {
        return InstanceIdentifier.create(Neutron.class)
                .child(Ports.class)
                .child(Port.class, port.getKey());
    }

    @Override
    protected Port toMd(NeutronPort neutronPort) {
        PortBuilder portBuilder = new PortBuilder();
        portBuilder.setAdminStateUp(neutronPort.isAdminStateUp());
        if(neutronPort.getAllowedAddressPairs() != null) {
            List<AllowedAddressPairs> listAllowedAddressPairs = new ArrayList<AllowedAddressPairs>();
            for (NeutronPort_AllowedAddressPairs allowedAddressPairs : neutronPort.getAllowedAddressPairs()) {
                    AllowedAddressPairsBuilder allowedAddressPairsBuilder = new AllowedAddressPairsBuilder();
                    allowedAddressPairsBuilder.setIpAddress(allowedAddressPairs.getIpAddress());
                    allowedAddressPairsBuilder.setMacAddress(allowedAddressPairs.getMacAddress());
                    allowedAddressPairsBuilder.setPortId(allowedAddressPairs.getPortID());
                    listAllowedAddressPairs.add(allowedAddressPairsBuilder.build());
            }
            portBuilder.setAllowedAddressPairs(listAllowedAddressPairs);
        }
        if (neutronPort.getBindinghostID() != null) {
            portBuilder.setBindingProfile(neutronPort.getBindinghostID());
        }
        if (neutronPort.getDeviceID() != null) {
            portBuilder.setDeviceId(toUuid(neutronPort.getDeviceID()));
        }
        if (neutronPort.getDeviceOwner() != null) {
        portBuilder.setDeviceOwner(neutronPort.getDeviceOwner());
        }
        if (neutronPort.getExtraDHCPOptions() != null) {
            List<ExtraDhcpOpts> listExtraDHCPOptions = new ArrayList<ExtraDhcpOpts>();
            for (NeutronPort_ExtraDHCPOption extraDHCPOption : neutronPort.getExtraDHCPOptions()) {
                ExtraDhcpOptsBuilder extraDHCPOptsBuilder = new ExtraDhcpOptsBuilder();
                extraDHCPOptsBuilder.setOptName(extraDHCPOption.getName());
                extraDHCPOptsBuilder.setOptValue(extraDHCPOption.getValue());
                listExtraDHCPOptions.add(extraDHCPOptsBuilder.build());
            }
            portBuilder.setExtraDhcpOpts(listExtraDHCPOptions);
        }
        if (neutronPort.getFixedIPs() != null) {
            List<FixedIps> listNeutronIPs = new ArrayList<FixedIps>();
            for (Neutron_IPs neutron_IPs : neutronPort.getFixedIPs()) {
                FixedIpsBuilder fixedIpsBuilder = new FixedIpsBuilder();
                fixedIpsBuilder.setIpAddress(new IpAddress(neutron_IPs.getIpAddress().toCharArray()));
                fixedIpsBuilder.setSubnetId(toUuid(neutron_IPs.getSubnetUUID()));
                listNeutronIPs.add(fixedIpsBuilder.build());
            }
            portBuilder.setFixedIps(listNeutronIPs);
        }
        if (neutronPort.getMacAddress() != null) {
            portBuilder.setMacAddress(neutronPort.getMacAddress());
        }
        if (neutronPort.getName() != null) {
        portBuilder.setName(neutronPort.getName());
        }
        if (neutronPort.getNetworkUUID() != null) {
        portBuilder.setNetworkId(toUuid(neutronPort.getNetworkUUID()));
        }
        if (neutronPort.getSecurityGroups() != null) {
            List<Uuid> listSecurityGroups = new ArrayList<Uuid>();
            for (NeutronSecurityGroup neutronSecurityGroup : neutronPort.getSecurityGroups()) {
                listSecurityGroups.add(toUuid(neutronSecurityGroup.getSecurityGroupUUID()));
            }
            portBuilder.setSecurityGroups(listSecurityGroups);
        }
        if (neutronPort.getStatus() != null) {
            portBuilder.setStatus(neutronPort.getStatus());
        }
        if (neutronPort.getTenantID() != null && !neutronPort.getTenantID().isEmpty()) {
            portBuilder.setTenantId(toUuid(neutronPort.getTenantID()));
        }
        if (neutronPort.getPortUUID() != null) {
            portBuilder.setUuid(toUuid(neutronPort.getPortUUID()));
        } else {
            LOGGER.warn("Attempting to write neutron port without UUID");
        }
        return portBuilder.build();
    }

    @Override
    protected Port toMd(String uuid) {
        PortBuilder portBuilder = new PortBuilder();
        portBuilder.setUuid(toUuid(uuid));
        return portBuilder.build();
    }
}
