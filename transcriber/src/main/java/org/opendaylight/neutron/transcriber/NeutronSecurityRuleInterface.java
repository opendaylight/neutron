/*
 * Copyright (c) 2014, 2015 Red Hat, Inc. and others.  All rights reserved.
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
import org.opendaylight.neutron.spi.INeutronSecurityRuleCRUD;
import org.opendaylight.neutron.spi.NeutronSecurityRule;
import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.inet.types.rev100924.IpPrefix;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.constants.rev150712.DirectionBase;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.constants.rev150712.DirectionEgress;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.constants.rev150712.DirectionIngress;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.constants.rev150712.EthertypeBase;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.constants.rev150712.EthertypeV4;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.constants.rev150712.EthertypeV6;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.constants.rev150712.ProtocolBase;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.constants.rev150712.ProtocolHttp;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.constants.rev150712.ProtocolHttps;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.constants.rev150712.ProtocolIcmp;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.constants.rev150712.ProtocolIcmpV6;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.constants.rev150712.ProtocolTcp;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.constants.rev150712.ProtocolUdp;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.rev150712.Neutron;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.secgroups.rev150712.security.rules.attributes.SecurityRules;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.secgroups.rev150712.security.rules.attributes.security.rules.SecurityRule;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.secgroups.rev150712.security.rules.attributes.security.rules.SecurityRuleBuilder;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableBiMap;


public class NeutronSecurityRuleInterface extends AbstractNeutronInterface<SecurityRule, SecurityRules, NeutronSecurityRule> implements INeutronSecurityRuleCRUD {
    private static final Logger LOGGER = LoggerFactory.getLogger(NeutronSecurityRuleInterface.class);

    private static final ImmutableBiMap<Class<? extends DirectionBase>,String> DIRECTION_MAP
            = new ImmutableBiMap.Builder<Class<? extends DirectionBase>,String>()
            .put(DirectionEgress.class,"egress")
            .put(DirectionIngress.class,"ingress")
            .build();
    private static final ImmutableBiMap<Class<? extends ProtocolBase>,String> PROTOCOL_MAP
            = new ImmutableBiMap.Builder<Class<? extends ProtocolBase>,String>()
            .put(ProtocolIcmp.class,"icmp")
            .put(ProtocolTcp.class,"tcp")
            .put(ProtocolUdp.class,"udp")
            .put(ProtocolIcmpV6.class,"icmpv6")
            .build();
    private static final ImmutableBiMap<Class<? extends EthertypeBase>,String> ETHERTYPE_MAP
            = new ImmutableBiMap.Builder<Class<? extends EthertypeBase>,String>()
            .put(EthertypeV4.class,"IPv4")
            .put(EthertypeV6.class,"IPv6")
            .build();

    NeutronSecurityRuleInterface(ProviderContext providerContext) {
        super(providerContext);
    }

    @Override
    public boolean neutronSecurityRuleExists(String uuid) {
        return exists(uuid);
    }

    @Override
    public NeutronSecurityRule getNeutronSecurityRule(String uuid) {
        return get(uuid);
    }

    @Override
    protected List<SecurityRule> getDataObjectList(SecurityRules rules) {
        return rules.getSecurityRule();
    }

    @Override
    public List<NeutronSecurityRule> getAllNeutronSecurityRules() {
        return getAll();
    }

    @Override
    public boolean addNeutronSecurityRule(NeutronSecurityRule input) {
        return add(input);
    }

    @Override
    public boolean removeNeutronSecurityRule(String uuid) {
        return remove(uuid);
    }

    @Override
    public boolean updateNeutronSecurityRule(String uuid, NeutronSecurityRule delta) {
        return update(uuid, delta);
    }

    @Override
    public boolean neutronSecurityRuleInUse(String securityRuleUUID) {
        return !exists(securityRuleUUID);
    }

    protected NeutronSecurityRule fromMd(SecurityRule rule) {
        NeutronSecurityRule answer = new NeutronSecurityRule();
        if (rule.getTenantId() != null) {
            answer.setTenantID(rule.getTenantId());
        }
        if (rule.getDirection() != null) {
            answer.setSecurityRuleDirection(DIRECTION_MAP.get(rule.getDirection()));
        }
        if (rule.getSecurityGroupId() != null) {
            answer.setSecurityRuleGroupID(rule.getSecurityGroupId().getValue());
        }
        if (rule.getRemoteGroupId() != null) {
            answer.setSecurityRemoteGroupID(rule.getRemoteGroupId().getValue());
        }
        if (rule.getRemoteIpPrefix() != null) {
            answer.setSecurityRuleRemoteIpPrefix(new String(rule.getRemoteIpPrefix().getValue()));
        }
        if (rule.getProtocol() != null) {
            answer.setSecurityRuleProtocol(PROTOCOL_MAP.get(rule.getProtocol()));
        }
        if (rule.getEthertype() != null) {
            answer.setSecurityRuleEthertype(ETHERTYPE_MAP.get(rule.getEthertype()));
        }
        if (rule.getPortRangeMin() != null) {
            answer.setSecurityRulePortMin(Integer.valueOf(rule.getPortRangeMin()));
        }
        if (rule.getPortRangeMax() != null) {
            answer.setSecurityRulePortMax(Integer.valueOf(rule.getPortRangeMax()));
        }
        if (rule.getUuid() != null) {
            answer.setID(rule.getUuid().getValue());
        }
        return answer;
    }

    @Override
    protected SecurityRule toMd(NeutronSecurityRule securityRule) {
        SecurityRuleBuilder securityRuleBuilder = new SecurityRuleBuilder();

        if (securityRule.getTenantID() != null) {
            securityRuleBuilder.setTenantId(toUuid(securityRule.getTenantID()));
        }
        if (securityRule.getSecurityRuleDirection() != null) {
            ImmutableBiMap<String, Class<? extends DirectionBase>> mapper =
                    DIRECTION_MAP.inverse();
            securityRuleBuilder.setDirection((Class<? extends DirectionBase>) mapper.get(securityRule.getSecurityRuleDirection()));
        }
        if (securityRule.getSecurityRuleGroupID() != null) {
            securityRuleBuilder.setSecurityGroupId(toUuid(securityRule.getSecurityRuleGroupID()));
        }
        if (securityRule.getSecurityRemoteGroupID() != null) {
            securityRuleBuilder.setRemoteGroupId(toUuid(securityRule.getSecurityRemoteGroupID()));
        }
        if (securityRule.getSecurityRuleRemoteIpPrefix() != null) {
            IpPrefix ipPrefix = new IpPrefix(securityRule.getSecurityRuleRemoteIpPrefix().toCharArray());
            securityRuleBuilder.setRemoteIpPrefix(ipPrefix);
        }
        if (securityRule.getSecurityRuleProtocol() != null) {
            ImmutableBiMap<String, Class<? extends ProtocolBase>> mapper =
                    PROTOCOL_MAP.inverse();
            securityRuleBuilder.setProtocol((Class<? extends ProtocolBase>) mapper.get(securityRule.getSecurityRuleProtocol()));
        }
        if (securityRule.getSecurityRuleEthertype() != null) {
            ImmutableBiMap<String, Class<? extends EthertypeBase>> mapper =
                    ETHERTYPE_MAP.inverse();
            securityRuleBuilder.setEthertype((Class<? extends EthertypeBase>) mapper.get(securityRule.getSecurityRuleEthertype()));
        }
        if (securityRule.getSecurityRulePortMin() != null) {
            securityRuleBuilder.setPortRangeMin(Integer.valueOf(securityRule.getSecurityRulePortMin()));
        }
        if (securityRule.getSecurityRulePortMax() != null) {
            securityRuleBuilder.setPortRangeMax(Integer.valueOf(securityRule.getSecurityRulePortMax()));
        }
        if (securityRule.getID() != null) {
            securityRuleBuilder.setUuid(toUuid(securityRule.getID()));
        } else {
            LOGGER.warn("Attempting to write neutron securityRule without UUID");
        }
        return securityRuleBuilder.build();
    }

    @Override
    protected InstanceIdentifier<SecurityRule> createInstanceIdentifier(SecurityRule securityRule) {
        return InstanceIdentifier.create(Neutron.class)
            .child(SecurityRules.class).child(SecurityRule.class,
                                              securityRule.getKey());
    }

    @Override
    protected InstanceIdentifier<SecurityRules> createInstanceIdentifier() {
        return InstanceIdentifier.create(Neutron.class)
            .child(SecurityRules.class);
    }

    @Override
    protected SecurityRule toMd(String uuid) {
        SecurityRuleBuilder securityRuleBuilder = new SecurityRuleBuilder();
        securityRuleBuilder.setUuid(toUuid(uuid));
        return securityRuleBuilder.build();
    }

    public static void registerNewInterface(BundleContext context,
                                            ProviderContext providerContext,
                                            List<ServiceRegistration<?>> registrations) {
        NeutronSecurityRuleInterface neutronSecurityRuleInterface = new NeutronSecurityRuleInterface(providerContext);
        ServiceRegistration<INeutronSecurityRuleCRUD> neutronSecurityRuleInterfaceRegistration = context.registerService(INeutronSecurityRuleCRUD.class, neutronSecurityRuleInterface, null);
        if(neutronSecurityRuleInterfaceRegistration != null) {
            registrations.add(neutronSecurityRuleInterfaceRegistration);
        }
    }
}
