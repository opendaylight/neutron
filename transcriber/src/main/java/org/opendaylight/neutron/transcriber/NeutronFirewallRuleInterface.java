/*
 * Copyright (c) 2014, 2015 Red Hat, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.neutron.transcriber;

import com.google.common.collect.ImmutableBiMap;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.neutron.northbound.api.BadRequestException;
import org.opendaylight.neutron.spi.INeutronFirewallRuleCRUD;
import org.opendaylight.neutron.spi.NeutronFirewallRule;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.constants.rev150712.IpVersionBase;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.constants.rev150712.IpVersionV4;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.constants.rev150712.IpVersionV6;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.constants.rev150712.NeutronUtils.FwProtocolMapper;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.fwaas.rev150712.ActionAllow;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.fwaas.rev150712.ActionBase;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.fwaas.rev150712.ActionDeny;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.fwaas.rev150712.FirewallRuleAttributes.Protocol;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.fwaas.rev150712.rules.attributes.FirewallRules;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.fwaas.rev150712.rules.attributes.firewall.rules.FirewallRule;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.fwaas.rev150712.rules.attributes.firewall.rules.FirewallRuleBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.fwaas.rev150712.rules.attributes.firewall.rules.FirewallRuleKey;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.types.rev160517.IpPrefixOrAddress;
import org.ops4j.pax.cdi.api.OsgiServiceProvider;

@Singleton
@OsgiServiceProvider(classes = INeutronFirewallRuleCRUD.class)
public final class NeutronFirewallRuleInterface
        extends AbstractNeutronInterface<FirewallRule, FirewallRules, FirewallRuleKey, NeutronFirewallRule>
        implements INeutronFirewallRuleCRUD {

    private static final ImmutableBiMap<Class<? extends ActionBase>,
            String> ACTION_MAP = new ImmutableBiMap.Builder<Class<? extends ActionBase>, String>()
                    .put(ActionAllow.class, "allow").put(ActionDeny.class, "deny").build();

    private static final ImmutableBiMap<Class<? extends IpVersionBase>,
            Integer> IP_VERSION_MAP = new ImmutableBiMap.Builder<Class<? extends IpVersionBase>, Integer>()
                    .put(IpVersionV4.class, Integer.valueOf(4)).put(IpVersionV6.class, Integer.valueOf(6)).build();

    @Inject
    public NeutronFirewallRuleInterface(DataBroker db) {
        super(FirewallRuleBuilder.class, db);
    }

    @Override
    protected List<FirewallRule> getDataObjectList(FirewallRules rules) {
        return rules.getFirewallRule();
    }

    @Override
    protected NeutronFirewallRule fromMd(FirewallRule rule) {
        final NeutronFirewallRule answer = new NeutronFirewallRule();
        fromMdBaseAttributes(rule, answer);
        if (rule.isShared() != null) {
            answer.setFirewallRuleIsShared(rule.isShared());
        }
        if (rule.isEnabled() != null) {
            answer.setFirewallRuleIsEnabled(rule.isEnabled());
        }
        if (rule.getFirewallPolicyId() != null) {
            answer.setFirewallRulePolicyID(rule.getFirewallPolicyId().getValue());
        }
        if (rule.getProtocol() != null) {
            final Protocol protocol = rule.getProtocol();
            if (protocol.getUint8() != null) {
                // uint8
                answer.setFirewallRuleProtocol(protocol.getUint8().toString());
            } else {
                // symbolic protocol name
                answer.setFirewallRuleProtocol(FwProtocolMapper.getName(protocol.getIdentityref()));
            }
        }
        if (rule.getIpVersion() != null) {
            answer.setFirewallRuleIpVer(IP_VERSION_MAP.get(rule.getIpVersion()));
        }
        if (rule.getSourceIpAddr() != null) {
            answer.setFirewallRuleSrcIpAddr(String.valueOf(rule.getSourceIpAddr().getValue()));
        }
        if (rule.getDestinationIpAddr() != null) {
            answer.setFirewallRuleDstIpAddr(String.valueOf(rule.getDestinationIpAddr().getValue()));
        }
        if (rule.getSourcePortRangeMin() != null) {
            answer.setFirewallRuleSrcPortRangeMin(rule.getSourcePortRangeMin());
        }
        if (rule.getSourcePortRangeMax() != null) {
            answer.setFirewallRuleSrcPortRangeMax(rule.getSourcePortRangeMax());
        }
        if (rule.getDestinationPortRangeMin() != null) {
            answer.setFirewallRuleDstPortRangeMin(rule.getDestinationPortRangeMin());
        }
        if (rule.getDestinationPortRangeMax() != null) {
            answer.setFirewallRuleDstPortRangeMax(rule.getDestinationPortRangeMax());
        }
        if (rule.getPosition() != null) {
            answer.setFirewallRulePosition(Integer.valueOf(rule.getPosition().intValue()));
        }
        if (rule.getAction() != null) {
            answer.setFirewallRuleAction(ACTION_MAP.get(rule.getAction()));
        }
        return answer;
    }

    @Override
    @SuppressWarnings("checkstyle:AvoidHidingCauseException")
    protected FirewallRule toMd(NeutronFirewallRule rule) {
        final FirewallRuleBuilder ruleBuilder = new FirewallRuleBuilder();
        toMdBaseAttributes(rule, ruleBuilder);
        if (rule.getFirewallRuleIsShared() != null) {
            ruleBuilder.setShared(rule.getFirewallRuleIsShared());
        }
        if (rule.getFirewallRuleIsEnabled() != null) {
            ruleBuilder.setEnabled(rule.getFirewallRuleIsEnabled());
        }
        if (rule.getFirewallRulePolicyID() != null) {
            ruleBuilder.setFirewallPolicyId(toUuid(rule.getFirewallRulePolicyID()));
        }
        if (rule.getFirewallRuleProtocol() != null) {
            final String protocolString = rule.getFirewallRuleProtocol();
            try {
                final Protocol protocol = new Protocol(protocolString.toCharArray());
                ruleBuilder.setProtocol(protocol);
            } catch (NumberFormatException e) {
                throw new BadRequestException("Protocol {" + rule.getFirewallRuleProtocol() + "} is not supported");
            }
        }
        if (rule.getFirewallRuleIpVer() != null) {
            final ImmutableBiMap<Integer, Class<? extends IpVersionBase>> mapper = IP_VERSION_MAP.inverse();
            ruleBuilder.setIpVersion(mapper.get(rule.getFirewallRuleIpVer()));
        }
        if (rule.getFirewallRuleSrcIpAddr() != null) {
            final IpPrefixOrAddress ipAddress = new IpPrefixOrAddress(rule.getFirewallRuleSrcIpAddr().toCharArray());
            ruleBuilder.setSourceIpAddr(ipAddress);
        }
        if (rule.getFirewallRuleDstIpAddr() != null) {
            final IpPrefixOrAddress ipAddress = new IpPrefixOrAddress(rule.getFirewallRuleDstIpAddr().toCharArray());
            ruleBuilder.setDestinationIpAddr(ipAddress);
        }
        if (rule.getFirewallRuleSrcPortRangeMin() != null) {
            ruleBuilder.setSourcePortRangeMin(rule.getFirewallRuleSrcPortRangeMin());
        }
        if (rule.getFirewallRuleSrcPortRangeMax() != null) {
            ruleBuilder.setSourcePortRangeMax(rule.getFirewallRuleSrcPortRangeMax());
        }
        if (rule.getFirewallRuleDstPortRangeMin() != null) {
            ruleBuilder.setDestinationPortRangeMin(rule.getFirewallRuleDstPortRangeMin());
        }
        if (rule.getFirewallRuleDstPortRangeMax() != null) {
            ruleBuilder.setDestinationPortRangeMax(rule.getFirewallRuleDstPortRangeMax());
        }
        if (rule.getFirewallRulePosition() != null) {
            ruleBuilder.setPosition(rule.getFirewallRulePosition().shortValue());
        }
        if (rule.getFirewallRuleAction() != null) {
            final ImmutableBiMap<String, Class<? extends ActionBase>> mapper = ACTION_MAP.inverse();
            ruleBuilder.setAction(mapper.get(rule.getFirewallRuleAction()));
        }
        return ruleBuilder.build();
    }
}
