/*
 * Copyright (c) 2013, 2015 IBM Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.neutron.transcriber;

import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.apache.aries.blueprint.annotation.service.Service;
import org.opendaylight.mdsal.binding.api.DataBroker;
import org.opendaylight.neutron.spi.INeutronPortCRUD;
import org.opendaylight.neutron.spi.NeutronIps;
import org.opendaylight.neutron.spi.NeutronPort;
import org.opendaylight.neutron.spi.NeutronPortAllowedAddressPairs;
import org.opendaylight.neutron.spi.NeutronPortExtraDHCPOption;
import org.opendaylight.neutron.spi.NeutronSecurityGroup;
import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.inet.types.rev130715.IpAddressBuilder;
import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.yang.types.rev130715.MacAddress;
import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.yang.types.rev130715.Uuid;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.binding.rev150712.PortBindingExtension;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.binding.rev150712.PortBindingExtensionBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.binding.rev150712.binding.attributes.VifDetails;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.binding.rev150712.binding.attributes.VifDetailsBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.binding.rev150712.binding.attributes.VifDetailsKey;
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
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.types.rev160517.IpPrefixOrAddressBuilder;
import org.opendaylight.yangtools.yang.binding.util.BindingMap;

@Singleton
@Service(classes = INeutronPortCRUD.class)
public final class NeutronPortInterface extends AbstractNeutronInterface<Port, Ports, PortKey, NeutronPort>
        implements INeutronPortCRUD {

    // TODO: consolidate this map with NeutronSubnetInterface.IPV_MAP
    private static final ImmutableBiMap<Class<? extends IpVersionBase>,
            Integer> IPV_MAP = new ImmutableBiMap.Builder<Class<? extends IpVersionBase>, Integer>()
                    .put(IpVersionV4.class, 4).put(IpVersionV6.class, 6).build();

    @Inject
    public NeutronPortInterface(DataBroker db) {
        super(PortBuilder.class, db);
    }

    // IfNBPortCRUD methods
    @Override
    protected Collection<Port> getDataObjectList(Ports ports) {
        return ports.nonnullPort().values();
    }

    protected void addExtensions(Port port, NeutronPort result) {
        final PortBindingExtension binding = port.augmentation(PortBindingExtension.class);
        result.setBindinghostID(binding.getHostId());

        final Map<VifDetailsKey, VifDetails> vifDetails = binding.getVifDetails();
        if (vifDetails != null) {
            final Map<String, String> details = Maps.newHashMapWithExpectedSize(vifDetails.size());
            for (final VifDetails vifDetail : vifDetails.values()) {
                details.put(vifDetail.getDetailsKey(), vifDetail.getValue());
            }
            result.setVIFDetails(details);
        }
        result.setProfile(binding.getProfile());
        result.setBindingvifType(binding.getVifType());
        result.setBindingvnicType(binding.getVnicType());
    }

    private static void portSecurityExtension(Port port, NeutronPort result) {
        final PortSecurityExtension portSecurity = port.augmentation(PortSecurityExtension.class);
        if (portSecurity != null && portSecurity.getPortSecurityEnabled() != null) {
            result.setPortSecurityEnabled(portSecurity.getPortSecurityEnabled());
        }
    }

    private static void qosExtension(Port port, NeutronPort result) {
        final QosPortExtension qos = port.augmentation(QosPortExtension.class);
        if (qos != null && qos.getQosPolicyId() != null) {
            result.setQosPolicyId(qos.getQosPolicyId().getValue());
        }
    }

    @Override
    protected NeutronPort fromMd(Port port) {
        final NeutronPort result = new NeutronPort();
        fromMdBaseAttributes(port, result);
        fromMdAdminAttributes(port, result);
        if (port.getAllowedAddressPairs() != null) {
            final List<NeutronPortAllowedAddressPairs> pairs = new ArrayList<>();
            for (final AllowedAddressPairs mdPair : port.getAllowedAddressPairs().values()) {
                final NeutronPortAllowedAddressPairs pair = new NeutronPortAllowedAddressPairs();
                pair.setIpAddress(mdPair.getIpAddress().stringValue());
                pair.setMacAddress(mdPair.getMacAddress().getValue());
                pairs.add(pair);
            }
            result.setAllowedAddressPairs(pairs);
        }
        result.setDeviceID(port.getDeviceId());
        result.setDeviceOwner(port.getDeviceOwner());
        if (port.getExtraDhcpOpts() != null) {
            final List<NeutronPortExtraDHCPOption> options = new ArrayList<>();
            for (final ExtraDhcpOpts opt : port.getExtraDhcpOpts().values()) {
                final NeutronPortExtraDHCPOption arg = new NeutronPortExtraDHCPOption();
                arg.setName(opt.getOptName());
                arg.setValue(opt.getOptValue());
                arg.setIpVersion(IPV_MAP.get(opt.getIpVersion()));
                options.add(arg);
            }
            result.setExtraDHCPOptions(options);
        }
        if (port.getFixedIps() != null) {
            final List<NeutronIps> ips = new ArrayList<>();
            for (final FixedIps mdIp : port.getFixedIps().values()) {
                final NeutronIps ip = new NeutronIps();
                ip.setIpAddress(mdIp.getIpAddress().stringValue());
                ip.setSubnetUUID(mdIp.getSubnetId().getValue());
                ips.add(ip);
            }
            result.setFixedIps(ips);
        }
        result.setMacAddress(port.getMacAddress().getValue());
        result.setNetworkUUID(String.valueOf(port.getNetworkId().getValue()));
        if (port.getSecurityGroups() != null) {
            final Set<NeutronSecurityGroup> allGroups = new HashSet<>();
            for (final Uuid sgUuid : port.getSecurityGroups()) {
                final NeutronSecurityGroup sg = new NeutronSecurityGroup();
                sg.setID(sgUuid.getValue());
                allGroups.add(sg);
            }
            final List<NeutronSecurityGroup> groups = new ArrayList<>();
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
            bindingBuilder.setVifDetails(neutronPort.getVIFDetails().entrySet().stream()
                .map(vifDetail -> {
                    final VifDetailsBuilder vifDetailsBuilder = new VifDetailsBuilder();
                    if (vifDetail.getKey() != null) {
                        vifDetailsBuilder.setDetailsKey(vifDetail.getKey());
                    }
                    if (vifDetail.getValue() != null) {
                        vifDetailsBuilder.setValue(vifDetail.getValue());
                    }
                    return vifDetailsBuilder.build();
                })
                .collect(BindingMap.toOrderedMap()));
        }
        if (neutronPort.getBindingvifType() != null) {
            bindingBuilder.setVifType(neutronPort.getBindingvifType());
        }
        if (neutronPort.getBindingvnicType() != null) {
            bindingBuilder.setVnicType(neutronPort.getBindingvnicType());
        }
        if (neutronPort.getProfile() != null) {
            bindingBuilder.setProfile(neutronPort.getProfile());
        }

        final PortSecurityExtensionBuilder portSecurityBuilder = new PortSecurityExtensionBuilder();
        if (neutronPort.getPortSecurityEnabled() != null) {
            portSecurityBuilder.setPortSecurityEnabled(neutronPort.getPortSecurityEnabled());
        }

        final PortBuilder portBuilder = new PortBuilder();
        toMdBaseAttributes(neutronPort, portBuilder);
        toMdAdminAttributes(neutronPort, portBuilder);
        portBuilder.addAugmentation(bindingBuilder.build());
        portBuilder.addAugmentation(portSecurityBuilder.build());
        if (neutronPort.getAllowedAddressPairs() != null) {
            portBuilder.setAllowedAddressPairs(neutronPort.getAllowedAddressPairs().stream()
                .map(allowedAddressPairs -> new AllowedAddressPairsBuilder()
                    .setIpAddress(IpPrefixOrAddressBuilder.getDefaultInstance(allowedAddressPairs.getIpAddress()))
                    .setMacAddress(new MacAddress(allowedAddressPairs.getMacAddress()))
                    .build())
                .collect(BindingMap.toOrderedMap()));
        }
        if (neutronPort.getDeviceID() != null) {
            portBuilder.setDeviceId(neutronPort.getDeviceID());
        }
        if (neutronPort.getDeviceOwner() != null) {
            portBuilder.setDeviceOwner(neutronPort.getDeviceOwner());
        }
        if (neutronPort.getExtraDHCPOptions() != null) {
            final ImmutableBiMap<Integer, Class<? extends IpVersionBase>> mapper = IPV_MAP.inverse();
            portBuilder.setExtraDhcpOpts(neutronPort.getExtraDHCPOptions().stream()
                .map(extraDHCPOption -> {
                    Integer ipVersion = extraDHCPOption.getIpVersion();
                    if (ipVersion == null) {
                        // default as v4 for neutron api evolves
                        ipVersion = 4;
                    }
                    return new ExtraDhcpOptsBuilder()
                        .setOptName(extraDHCPOption.getName())
                        .setOptValue(extraDHCPOption.getValue())
                        .setIpVersion(mapper.get(ipVersion))
                        .build();
                })
                .collect(BindingMap.toOrderedMap()));
        }
        if (neutronPort.getFixedIps() != null) {
            portBuilder.setFixedIps(neutronPort.getFixedIps().stream()
                .map(neutronIPs -> new FixedIpsBuilder()
                    .setIpAddress(IpAddressBuilder.getDefaultInstance(neutronIPs.getIpAddress()))
                    .setSubnetId(toUuid(neutronIPs.getSubnetUUID()))
                    .build())
                .collect(BindingMap.toOrderedMap()));
        }
        if (neutronPort.getMacAddress() != null) {
            portBuilder.setMacAddress(new MacAddress(neutronPort.getMacAddress()));
        }
        if (neutronPort.getNetworkUUID() != null) {
            portBuilder.setNetworkId(toUuid(neutronPort.getNetworkUUID()));
        }
        if (neutronPort.getSecurityGroups() != null) {
            final List<Uuid> listSecurityGroups = new ArrayList<>();
            for (final NeutronSecurityGroup neutronSecurityGroup : neutronPort.getSecurityGroups()) {
                listSecurityGroups.add(toUuid(neutronSecurityGroup.getID()));
            }
            portBuilder.setSecurityGroups(listSecurityGroups);
        }
        if (neutronPort.getQosPolicyId() != null) {
            portBuilder.addAugmentation(new QosPortExtensionBuilder()
                .setQosPolicyId(toUuid(neutronPort.getQosPolicyId()))
                .build());
        }
        return portBuilder.build();
    }
}
