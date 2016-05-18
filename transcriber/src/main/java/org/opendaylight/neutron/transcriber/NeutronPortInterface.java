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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.opendaylight.controller.sal.binding.api.BindingAwareBroker.ProviderContext;
import org.opendaylight.neutron.spi.INeutronPortCRUD;
import org.opendaylight.neutron.spi.INeutronSecurityGroupCRUD;
import org.opendaylight.neutron.spi.NeutronCRUDInterfaces;
import org.opendaylight.neutron.spi.NeutronPort;
import org.opendaylight.neutron.spi.NeutronPort_AllowedAddressPairs;
import org.opendaylight.neutron.spi.NeutronPort_ExtraDHCPOption;
import org.opendaylight.neutron.spi.NeutronSecurityGroup;
import org.opendaylight.neutron.spi.Neutron_IPs;
import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.inet.types.rev100924.IpAddress;
import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.yang.types.rev130715.MacAddress;
import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.yang.types.rev130715.Uuid;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.binding.rev150712.binding.attributes.VifDetails;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.binding.rev150712.binding.attributes.VifDetailsBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.binding.rev150712.PortBindingExtension;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.binding.rev150712.PortBindingExtensionBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.constants.rev150712.IpVersionBase;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.constants.rev150712.IpVersionV4;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.constants.rev150712.IpVersionV6;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.ports.rev150712.port.attributes.AllowedAddressPairs;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.ports.rev150712.port.attributes.AllowedAddressPairsBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.ports.rev150712.port.attributes.ExtraDhcpOpts;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.ports.rev150712.port.attributes.ExtraDhcpOptsBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.ports.rev150712.port.attributes.FixedIps;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.ports.rev150712.port.attributes.FixedIpsBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.ports.rev150712.ports.attributes.Ports;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.ports.rev150712.ports.attributes.ports.Port;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.ports.rev150712.ports.attributes.ports.PortBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.portsecurity.rev150712.PortSecurityExtension;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.portsecurity.rev150712.PortSecurityExtensionBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.rev150712.Neutron;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.types.rev160517.IpPrefixOrAddress;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableBiMap;

public class NeutronPortInterface extends AbstractNeutronInterface<Port, Ports, NeutronPort> implements INeutronPortCRUD {
    private static final Logger LOGGER = LoggerFactory.getLogger(NeutronPortInterface.class);

    // TODO: consolidate this map with NeutronSubnetInterface.IPV_MAP
    private static final ImmutableBiMap<Class<? extends IpVersionBase>,Integer> IPV_MAP
            = new ImmutableBiMap.Builder<Class<? extends IpVersionBase>,Integer>()
            .put(IpVersionV4.class, Integer.valueOf(4))
            .put(IpVersionV6.class, Integer.valueOf(6))
            .build();

    NeutronPortInterface(ProviderContext providerContext) {
        super(providerContext);
    }

    // IfNBPortCRUD methods

    @Override
    public boolean portExists(String uuid) {
        return exists(uuid);
    }

    @Override
    public NeutronPort getPort(String uuid) {
        return get(uuid);
    }

    @Override
    protected List<Port> getDataObjectList(Ports ports) {
        return ports.getPort();
    }

    @Override
    public List<NeutronPort> getAllPorts() {
        return getAll();
    }

    @Override
    public boolean addPort(NeutronPort input) {
        return add(input);
    }

    @Override
    public boolean removePort(String uuid) {
        return remove(uuid);
    }

    @Override
    public boolean updatePort(String uuid, NeutronPort delta) {
        return update(uuid, delta);
    }

    @Override
    protected InstanceIdentifier<Port> createInstanceIdentifier(Port port) {
        return InstanceIdentifier.create(Neutron.class)
                .child(Ports.class)
                .child(Port.class, port.getKey());
    }

    @Override
    protected InstanceIdentifier<Ports> createInstanceIdentifier() {
        return InstanceIdentifier.create(Neutron.class)
                .child(Ports.class);
    }

    protected void addExtensions(Port port, NeutronPort result) {
        PortBindingExtension binding = port.getAugmentation(PortBindingExtension.class);
        result.setBindinghostID(binding.getHostId());
        if (binding.getVifDetails() != null) {
            Map<String, String> details = new HashMap<String, String>(binding.getVifDetails().size());
            for (VifDetails vifDetail : binding.getVifDetails()) {
                details.put(vifDetail.getDetailsKey(), vifDetail.getValue());
            }
            result.setVIFDetails(details);
        }
        result.setBindingvifType(binding.getVifType());
        result.setBindingvnicType(binding.getVnicType());
    }

    private void portSecurityExtension(Port port, NeutronPort result) {
        PortSecurityExtension portSecurity = port.getAugmentation(PortSecurityExtension.class);
        if(portSecurity != null && portSecurity.isPortSecurityEnabled() != null) {
            result.setPortSecurityEnabled(portSecurity.isPortSecurityEnabled());
        }
    }

    protected NeutronPort fromMd(Port port) {
        NeutronPort result = new NeutronPort();
        result.setAdminStateUp(port.isAdminStateUp());
        if (port.getAllowedAddressPairs() != null) {
            List<NeutronPort_AllowedAddressPairs> pairs = new ArrayList<NeutronPort_AllowedAddressPairs>();
            for (AllowedAddressPairs mdPair : port.getAllowedAddressPairs()) {
                NeutronPort_AllowedAddressPairs pair = new NeutronPort_AllowedAddressPairs();
                pair.setIpAddress(mdPair.getIpAddress().getValue().toString());
                pair.setMacAddress(mdPair.getMacAddress().getValue());
                pairs.add(pair);
            }
            result.setAllowedAddressPairs(pairs);
        }
        result.setDeviceID(port.getDeviceId());
        result.setDeviceOwner(port.getDeviceOwner());
        if (port.getExtraDhcpOpts() != null) {
            List<NeutronPort_ExtraDHCPOption> options = new ArrayList<NeutronPort_ExtraDHCPOption>();
            for (ExtraDhcpOpts opt : port.getExtraDhcpOpts()) {
                NeutronPort_ExtraDHCPOption arg = new NeutronPort_ExtraDHCPOption();
                arg.setName(opt.getOptName());
                arg.setValue(opt.getOptValue());
                arg.setIpVersion(IPV_MAP.get(opt.getIpVersion()));
                options.add(arg);
            }
            result.setExtraDHCPOptions(options);
        }
        if (port.getFixedIps() != null) {
            List<Neutron_IPs> ips = new ArrayList<Neutron_IPs>();
            for (FixedIps mdIP : port.getFixedIps()) {
                Neutron_IPs ip = new Neutron_IPs();
                ip.setIpAddress(String.valueOf(mdIP.getIpAddress().getValue()));
                ip.setSubnetUUID(mdIP.getSubnetId().getValue());
                ips.add(ip);
            }
            result.setFixedIPs(ips);
        }
        result.setMacAddress(port.getMacAddress().getValue());
        result.setName(port.getName());
        result.setNetworkUUID(String.valueOf(port.getNetworkId().getValue()));
        if (port.getSecurityGroups() != null) {
            Set<NeutronSecurityGroup> allGroups = new HashSet<NeutronSecurityGroup>();
            NeutronCRUDInterfaces interfaces = new NeutronCRUDInterfaces().fetchINeutronSecurityGroupCRUD(this);
            INeutronSecurityGroupCRUD sgIf = interfaces.getSecurityGroupInterface();
            for (Uuid sgUuid : port.getSecurityGroups()) {
                allGroups.add(sgIf.getNeutronSecurityGroup(sgUuid.getValue()));
            }
            List<NeutronSecurityGroup> groups = new ArrayList<NeutronSecurityGroup>();
            groups.addAll(allGroups);
            result.setSecurityGroups(groups);
        }
        result.setStatus(port.getStatus());
        if (port.getTenantId() != null) {
            result.setTenantID(port.getTenantId());
        }
        result.setID(port.getUuid().getValue());
        addExtensions(port, result);
        portSecurityExtension(port, result);
        return result;
    }

    @Override
    protected Port toMd(NeutronPort neutronPort) {
        PortBindingExtensionBuilder bindingBuilder = new PortBindingExtensionBuilder();
        if (neutronPort.getBindinghostID() != null) {
            bindingBuilder.setHostId(neutronPort.getBindinghostID());
        }
        if (neutronPort.getVIFDetails() != null) {
            Map<String, String> vifDetails = neutronPort.getVIFDetails();
            List<VifDetails> listVifDetail = new ArrayList<VifDetails>(vifDetails.size());
            for (Map.Entry<String, String> vifDetail : vifDetails.entrySet()) {
                VifDetailsBuilder vifDetailsBuilder = new VifDetailsBuilder();
                if (vifDetail.getKey() != null) {
                    vifDetailsBuilder.setDetailsKey(vifDetail.getKey());
                }
                if (vifDetail.getValue() != null) {
                    vifDetailsBuilder.setValue(vifDetail.getValue());
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

        PortSecurityExtensionBuilder portSecurityBuilder = new PortSecurityExtensionBuilder();
        if (neutronPort.getPortSecurityEnabled() != null) {
            portSecurityBuilder.setPortSecurityEnabled(neutronPort.getPortSecurityEnabled());
        }

        PortBuilder portBuilder = new PortBuilder();
        portBuilder.addAugmentation(PortBindingExtension.class,
                                    bindingBuilder.build());
        portBuilder.addAugmentation(PortSecurityExtension.class, portSecurityBuilder.build());
        portBuilder.setAdminStateUp(neutronPort.isAdminStateUp());
        if(neutronPort.getAllowedAddressPairs() != null) {
            List<AllowedAddressPairs> listAllowedAddressPairs = new ArrayList<AllowedAddressPairs>();
            for (NeutronPort_AllowedAddressPairs allowedAddressPairs : neutronPort.getAllowedAddressPairs()) {
                    AllowedAddressPairsBuilder allowedAddressPairsBuilder = new AllowedAddressPairsBuilder();
                    allowedAddressPairsBuilder.setIpAddress(new IpPrefixOrAddress(allowedAddressPairs.getIpAddress().toCharArray()));
                    allowedAddressPairsBuilder.setMacAddress(new MacAddress(allowedAddressPairs.getMacAddress()));
                    listAllowedAddressPairs.add(allowedAddressPairsBuilder.build());
            }
            portBuilder.setAllowedAddressPairs(listAllowedAddressPairs);
        }
        if (neutronPort.getDeviceID() != null) {
            portBuilder.setDeviceId(neutronPort.getDeviceID());
        }
        if (neutronPort.getDeviceOwner() != null) {
            portBuilder.setDeviceOwner(neutronPort.getDeviceOwner());
        }
        if (neutronPort.getExtraDHCPOptions() != null) {
            List<ExtraDhcpOpts> listExtraDHCPOptions = new ArrayList<ExtraDhcpOpts>();
            ImmutableBiMap<Integer, Class<? extends IpVersionBase>> mapper = IPV_MAP.inverse();
            for (NeutronPort_ExtraDHCPOption extraDHCPOption : neutronPort.getExtraDHCPOptions()) {
                ExtraDhcpOptsBuilder extraDHCPOptsBuilder = new ExtraDhcpOptsBuilder();
                extraDHCPOptsBuilder.setOptName(extraDHCPOption.getName());
                extraDHCPOptsBuilder.setOptValue(extraDHCPOption.getValue());
                Integer ipVersion = extraDHCPOption.getIpVersion();
                if (ipVersion == null) {
                    ipVersion = 4;      // default as v4 for neutron api evolves
                }
                extraDHCPOptsBuilder.setIpVersion((Class<? extends IpVersionBase>)mapper.get(ipVersion));
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
            portBuilder.setMacAddress(new MacAddress(neutronPort.getMacAddress()));
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
                listSecurityGroups.add(toUuid(neutronSecurityGroup.getID()));
            }
            portBuilder.setSecurityGroups(listSecurityGroups);
        }
        if (neutronPort.getStatus() != null) {
            portBuilder.setStatus(neutronPort.getStatus());
        }
        if (neutronPort.getTenantID() != null) {
            portBuilder.setTenantId(toUuid(neutronPort.getTenantID()));
        }
        if (neutronPort.getID() != null) {
            portBuilder.setUuid(toUuid(neutronPort.getID()));
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
