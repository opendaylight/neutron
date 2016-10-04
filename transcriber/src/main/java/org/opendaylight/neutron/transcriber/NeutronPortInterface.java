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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.neutron.spi.INeutronPortCRUD;
import org.opendaylight.neutron.spi.NeutronPort;
import org.opendaylight.neutron.spi.NeutronPort_AllowedAddressPairs;
import org.opendaylight.neutron.spi.NeutronPort_ExtraDHCPOption;
import org.opendaylight.neutron.spi.NeutronSecurityGroup;
import org.opendaylight.neutron.spi.Neutron_IPs;
import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.inet.types.rev130715.IpAddress;
import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.yang.types.rev130715.MacAddress;
import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.yang.types.rev130715.Uuid;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.binding.rev150712.PortBindingExtension;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.binding.rev150712.PortBindingExtensionBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.binding.rev150712.binding.attributes.VifDetails;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.binding.rev150712.binding.attributes.VifDetailsBuilder;
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
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.ports.rev150712.ports.attributes.ports.PortKey;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.portsecurity.rev150712.PortSecurityExtension;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.portsecurity.rev150712.PortSecurityExtensionBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.qos.ext.rev160613.QosPortExtension;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.qos.ext.rev160613.QosPortExtensionBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.types.rev160517.IpPrefixOrAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class NeutronPortInterface extends AbstractNeutronInterface<Port, Ports, PortKey, NeutronPort>
        implements INeutronPortCRUD {
    private static final Logger LOGGER = LoggerFactory.getLogger(NeutronPortInterface.class);

    // TODO: consolidate this map with NeutronSubnetInterface.IPV_MAP
    private static final ImmutableBiMap<Class<? extends IpVersionBase>,
            Integer> IPV_MAP = new ImmutableBiMap.Builder<Class<? extends IpVersionBase>, Integer>()
                    .put(IpVersionV4.class, Integer.valueOf(4)).put(IpVersionV6.class, Integer.valueOf(6)).build();

    NeutronPortInterface(DataBroker db) {
        super(PortBuilder.class, db);
    }

    // IfNBPortCRUD methods
    @Override
    protected List<Port> getDataObjectList(Ports ports) {
        return ports.getPort();
    }

    protected void addExtensions(Port port, NeutronPort result) {
        final PortBindingExtension binding = port.getAugmentation(PortBindingExtension.class);
        result.setBindinghostID(binding.getHostId());
        if (binding.getVifDetails() != null) {
            final Map<String, String> details = new HashMap<String, String>(binding.getVifDetails().size());
            for (final VifDetails vifDetail : binding.getVifDetails()) {
                details.put(vifDetail.getDetailsKey(), vifDetail.getValue());
            }
            result.setVIFDetails(details);
        }
        result.setBindingvifType(binding.getVifType());
        result.setBindingvnicType(binding.getVnicType());
    }

    private void portSecurityExtension(Port port, NeutronPort result) {
        final PortSecurityExtension portSecurity = port.getAugmentation(PortSecurityExtension.class);
        if (portSecurity != null && portSecurity.isPortSecurityEnabled() != null) {
            result.setPortSecurityEnabled(portSecurity.isPortSecurityEnabled());
        }
    }

    private void qosExtension(Port port, NeutronPort result) {
        final QosPortExtension qos = port.getAugmentation(QosPortExtension.class);
        if (qos != null && qos.getQosPolicyId() != null) {
            result.setQosPolicyId(qos.getQosPolicyId().getValue());
        }
    }

    protected NeutronPort fromMd(Port port) {
        final NeutronPort result = new NeutronPort();
        fromMdAdminAttributes(port, result);
        if (port.getAllowedAddressPairs() != null) {
            final List<NeutronPort_AllowedAddressPairs> pairs = new ArrayList<NeutronPort_AllowedAddressPairs>();
            for (final AllowedAddressPairs mdPair : port.getAllowedAddressPairs()) {
                final NeutronPort_AllowedAddressPairs pair = new NeutronPort_AllowedAddressPairs();
                pair.setIpAddress(String.valueOf(mdPair.getIpAddress().getValue()));
                pair.setMacAddress(mdPair.getMacAddress().getValue());
                pairs.add(pair);
            }
            result.setAllowedAddressPairs(pairs);
        }
        result.setDeviceID(port.getDeviceId());
        result.setDeviceOwner(port.getDeviceOwner());
        if (port.getExtraDhcpOpts() != null) {
            final List<NeutronPort_ExtraDHCPOption> options = new ArrayList<NeutronPort_ExtraDHCPOption>();
            for (final ExtraDhcpOpts opt : port.getExtraDhcpOpts()) {
                final NeutronPort_ExtraDHCPOption arg = new NeutronPort_ExtraDHCPOption();
                arg.setName(opt.getOptName());
                arg.setValue(opt.getOptValue());
                arg.setIpVersion(IPV_MAP.get(opt.getIpVersion()));
                options.add(arg);
            }
            result.setExtraDHCPOptions(options);
        }
        if (port.getFixedIps() != null) {
            final List<Neutron_IPs> ips = new ArrayList<Neutron_IPs>();
            for (final FixedIps mdIP : port.getFixedIps()) {
                final Neutron_IPs ip = new Neutron_IPs();
                ip.setIpAddress(String.valueOf(mdIP.getIpAddress().getValue()));
                ip.setSubnetUUID(mdIP.getSubnetId().getValue());
                ips.add(ip);
            }
            result.setFixedIPs(ips);
        }
        result.setMacAddress(port.getMacAddress().getValue());
        result.setNetworkUUID(String.valueOf(port.getNetworkId().getValue()));
        if (port.getSecurityGroups() != null) {
            final Set<NeutronSecurityGroup> allGroups = new HashSet<NeutronSecurityGroup>();
            for (final Uuid sgUuid : port.getSecurityGroups()) {
                final NeutronSecurityGroup sg = new NeutronSecurityGroup();
                sg.setID(sgUuid.getValue());
                allGroups.add(sg);
            }
            final List<NeutronSecurityGroup> groups = new ArrayList<NeutronSecurityGroup>();
            groups.addAll(allGroups);
            result.setSecurityGroups(groups);
        }
        addExtensions(port, result);
        portSecurityExtension(port, result);
        qosExtension(port, result);
        return result;
    }

    @Override
    protected Port toMd(NeutronPort neutronPort) {
        final PortBindingExtensionBuilder bindingBuilder = new PortBindingExtensionBuilder();
        if (neutronPort.getBindinghostID() != null) {
            bindingBuilder.setHostId(neutronPort.getBindinghostID());
        }
        if (neutronPort.getVIFDetails() != null) {
            final Map<String, String> vifDetails = neutronPort.getVIFDetails();
            final List<VifDetails> listVifDetail = new ArrayList<VifDetails>(vifDetails.size());
            for (final Map.Entry<String, String> vifDetail : vifDetails.entrySet()) {
                final VifDetailsBuilder vifDetailsBuilder = new VifDetailsBuilder();
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

        final PortSecurityExtensionBuilder portSecurityBuilder = new PortSecurityExtensionBuilder();
        if (neutronPort.getPortSecurityEnabled() != null) {
            portSecurityBuilder.setPortSecurityEnabled(neutronPort.getPortSecurityEnabled());
        }

        final PortBuilder portBuilder = new PortBuilder();
        toMdAdminAttributes(neutronPort, portBuilder);
        portBuilder.addAugmentation(PortBindingExtension.class, bindingBuilder.build());
        portBuilder.addAugmentation(PortSecurityExtension.class, portSecurityBuilder.build());
        if (neutronPort.getAllowedAddressPairs() != null) {
            final List<AllowedAddressPairs> listAllowedAddressPairs = new ArrayList<AllowedAddressPairs>();
            for (final NeutronPort_AllowedAddressPairs allowedAddressPairs : neutronPort.getAllowedAddressPairs()) {
                final AllowedAddressPairsBuilder allowedAddressPairsBuilder = new AllowedAddressPairsBuilder();
                allowedAddressPairsBuilder
                        .setIpAddress(new IpPrefixOrAddress(allowedAddressPairs.getIpAddress().toCharArray()));
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
            final List<ExtraDhcpOpts> listExtraDHCPOptions = new ArrayList<ExtraDhcpOpts>();
            final ImmutableBiMap<Integer, Class<? extends IpVersionBase>> mapper = IPV_MAP.inverse();
            for (final NeutronPort_ExtraDHCPOption extraDHCPOption : neutronPort.getExtraDHCPOptions()) {
                final ExtraDhcpOptsBuilder extraDHCPOptsBuilder = new ExtraDhcpOptsBuilder();
                extraDHCPOptsBuilder.setOptName(extraDHCPOption.getName());
                extraDHCPOptsBuilder.setOptValue(extraDHCPOption.getValue());
                Integer ipVersion = extraDHCPOption.getIpVersion();
                if (ipVersion == null) {
                    ipVersion = 4; // default as v4 for neutron api evolves
                }
                extraDHCPOptsBuilder.setIpVersion((Class<? extends IpVersionBase>) mapper.get(ipVersion));
                listExtraDHCPOptions.add(extraDHCPOptsBuilder.build());
            }
            portBuilder.setExtraDhcpOpts(listExtraDHCPOptions);
        }
        if (neutronPort.getFixedIPs() != null) {
            final List<FixedIps> listNeutronIPs = new ArrayList<FixedIps>();
            for (final Neutron_IPs neutron_IPs : neutronPort.getFixedIPs()) {
                final FixedIpsBuilder fixedIpsBuilder = new FixedIpsBuilder();
                fixedIpsBuilder.setIpAddress(new IpAddress(neutron_IPs.getIpAddress().toCharArray()));
                fixedIpsBuilder.setSubnetId(toUuid(neutron_IPs.getSubnetUUID()));
                listNeutronIPs.add(fixedIpsBuilder.build());
            }
            portBuilder.setFixedIps(listNeutronIPs);
        }
        if (neutronPort.getMacAddress() != null) {
            portBuilder.setMacAddress(new MacAddress(neutronPort.getMacAddress()));
        }
        if (neutronPort.getNetworkUUID() != null) {
            portBuilder.setNetworkId(toUuid(neutronPort.getNetworkUUID()));
        }
        if (neutronPort.getSecurityGroups() != null) {
            final List<Uuid> listSecurityGroups = new ArrayList<Uuid>();
            for (final NeutronSecurityGroup neutronSecurityGroup : neutronPort.getSecurityGroups()) {
                listSecurityGroups.add(toUuid(neutronSecurityGroup.getID()));
            }
            portBuilder.setSecurityGroups(listSecurityGroups);
        }
        if (neutronPort.getQosPolicyId() != null) {
            final QosPortExtensionBuilder qosExtensionBuilder = new QosPortExtensionBuilder();
            qosExtensionBuilder.setQosPolicyId(toUuid(neutronPort.getQosPolicyId()));
            portBuilder.addAugmentation(QosPortExtension.class, qosExtensionBuilder.build());
        }
        return portBuilder.build();
    }
}
