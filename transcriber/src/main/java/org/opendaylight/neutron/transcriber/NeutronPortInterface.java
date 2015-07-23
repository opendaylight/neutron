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
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.opendaylight.controller.sal.binding.api.BindingAwareBroker.ProviderContext;
import org.opendaylight.neutron.spi.INeutronPortCRUD;
import org.opendaylight.neutron.spi.INeutronSubnetCRUD;
import org.opendaylight.neutron.spi.NeutronCRUDInterfaces;
import org.opendaylight.neutron.spi.NeutronPort;
import org.opendaylight.neutron.spi.NeutronPort_AllowedAddressPairs;
import org.opendaylight.neutron.spi.NeutronPort_ExtraDHCPOption;
import org.opendaylight.neutron.spi.NeutronPort_VIFDetail;
import org.opendaylight.neutron.spi.NeutronSecurityGroup;
import org.opendaylight.neutron.spi.NeutronSubnet;
import org.opendaylight.neutron.spi.Neutron_IPs;
import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.inet.types.rev100924.IpAddress;
import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.yang.types.rev130715.Uuid;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.binding.rev141002.binding.attributes.VifDetails;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.binding.rev141002.binding.attributes.VifDetailsBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.binding.rev141002.PortBindingExtension;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.binding.rev141002.PortBindingExtensionBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.ports.rev141002.port.attributes.AllowedAddressPairs;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.ports.rev141002.port.attributes.AllowedAddressPairsBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.ports.rev141002.port.attributes.ExtraDhcpOpts;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.ports.rev141002.port.attributes.ExtraDhcpOptsBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.ports.rev141002.port.attributes.FixedIps;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.ports.rev141002.port.attributes.FixedIpsBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.ports.rev141002.ports.attributes.Ports;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.ports.rev141002.ports.attributes.ports.Port;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.ports.rev141002.ports.attributes.ports.PortBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.rev150325.Neutron;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NeutronPortInterface extends AbstractNeutronInterface<Port, NeutronPort> implements INeutronPortCRUD {
    private static final Logger LOGGER = LoggerFactory.getLogger(NeutronPortInterface.class);

    NeutronPortInterface(ProviderContext providerContext) {
        super(providerContext);
    }

    // IfNBPortCRUD methods

    @Override
    public boolean portExists(String uuid) {
        Port port = readMd(createInstanceIdentifier(toMd(uuid)));
        if (port == null) {
            return false;
        }
        return true;
    }

    @Override
    public NeutronPort getPort(String uuid) {
        Port port = readMd(createInstanceIdentifier(toMd(uuid)));
        if (port == null) {
            return null;
        }
        return fromMd(port);
    }

    @Override
    public List<NeutronPort> getAllPorts() {
        Set<NeutronPort> allPorts = new HashSet<NeutronPort>();
	Ports ports = readMd(createInstanceIdentifier());
        if (ports != null) {
            for (Port port : ports.getPort()) {
                allPorts.add(fromMd(port));
            }
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
        addMd(input);
        return true;
    }

    @Override
    public boolean removePort(String uuid) {
        if (!portExists(uuid)) {
            return false;
        }
        return removeMd(toMd(uuid));
    }

    @Override
    public boolean updatePort(String uuid, NeutronPort delta) {
        if (!portExists(uuid)) {
            return false;
        }
        updateMd(delta);
        return true;
    }

    // @deprecated, will be removed in Boron
    @Override
    public boolean macInUse(String macAddress) {
        return false;
    }

    // @deprecated, will be removed in Boron
    @Override
    public NeutronPort getGatewayPort(String subnetUUID) {
        return null;
    }

    @Override
    protected InstanceIdentifier<Port> createInstanceIdentifier(Port port) {
        return InstanceIdentifier.create(Neutron.class)
                .child(Ports.class)
                .child(Port.class, port.getKey());
    }

    protected InstanceIdentifier<Ports> createInstanceIdentifier() {
        return InstanceIdentifier.create(Neutron.class)
                .child(Ports.class);
    }

    protected NeutronPort fromMd(Port port) {
        NeutronPort result = new NeutronPort();
        return result;
    }

    @Override
    protected Port toMd(NeutronPort neutronPort) {
        PortBindingExtensionBuilder bindingBuilder = new PortBindingExtensionBuilder();
        if (neutronPort.getBindinghostID() != null) {
            bindingBuilder.setHostId(neutronPort.getBindinghostID());
        }
        if (neutronPort.getVIFDetail() != null) {
            List<VifDetails> listVifDetail = new ArrayList<VifDetails>();
            for (NeutronPort_VIFDetail detail: neutronPort.getVIFDetail()) {
                VifDetailsBuilder vifDetailsBuilder = new VifDetailsBuilder();
                if (detail.getPortFilter() != null) {
                    vifDetailsBuilder.setPortFilter(detail.getPortFilter());
                } 
                if (detail.getOvsHybridPlug() != null) {
                    vifDetailsBuilder.setOvsHybridPlug(detail.getOvsHybridPlug());
                } 
                listVifDetail.add(vifDetailsBuilder.build());
            }
            bindingBuilder.setVifDetails(listVifDetail);
        }
        if (neutronPort.getBindingvifType() != null) {
            bindingBuilder.setVifType(neutronPort.getBindingvifType());
        }
        if (neutronPort.getBindingvnicType() != null) {
            bindingBuilder.setVnicType(neutronPort.getBindingvnicType());
        }

        PortBuilder portBuilder = new PortBuilder();
        portBuilder.addAugmentation(PortBindingExtension.class,
                                    bindingBuilder.build());
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
            portBuilder.setDeviceId(neutronPort.getDeviceID());
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
        if (neutronPort.getTenantID() != null) {
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

    public static void registerNewInterface(BundleContext context,
                                            ProviderContext providerContext,
                                            List<ServiceRegistration<?>> registrations) {
        NeutronPortInterface neutronPortInterface = new NeutronPortInterface(providerContext);
        ServiceRegistration<INeutronPortCRUD> neutronPortInterfaceRegistration = context.registerService(INeutronPortCRUD.class, neutronPortInterface, null);
        if(neutronPortInterfaceRegistration != null) {
            registrations.add(neutronPortInterfaceRegistration);
        }
    }
}
