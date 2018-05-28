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
import org.opendaylight.controller.md.sal.binding.api.ReadTransaction;
import org.opendaylight.neutron.northbound.api.BadRequestException;
import org.opendaylight.neutron.spi.INeutronSecurityRuleCRUD;
import org.opendaylight.neutron.spi.NeutronSecurityRule;
import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.inet.types.rev130715.IpPrefix;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.constants.rev150712.EthertypeBase;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.constants.rev150712.EthertypeV4;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.constants.rev150712.EthertypeV6;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.constants.rev150712.NeutronUtils.DirectionMapper;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.constants.rev150712.NeutronUtils.ProtocolMapper;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.secgroups.rev150712.SecurityRuleAttributes.Protocol;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.secgroups.rev150712.security.rules.attributes.SecurityRules;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.secgroups.rev150712.security.rules.attributes.security.rules.SecurityRule;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.secgroups.rev150712.security.rules.attributes.security.rules.SecurityRuleBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.secgroups.rev150712.security.rules.attributes.security.rules.SecurityRuleKey;
import org.ops4j.pax.cdi.api.OsgiServiceProvider;

@Singleton
@OsgiServiceProvider(classes = INeutronSecurityRuleCRUD.class)
public final class NeutronSecurityRuleInterface extends
        AbstractNeutronInterface<SecurityRule, SecurityRules, SecurityRuleKey, NeutronSecurityRule>
        implements INeutronSecurityRuleCRUD {

    private static final ImmutableBiMap<Class<? extends EthertypeBase>,
            String> ETHERTYPE_MAP = new ImmutableBiMap.Builder<Class<? extends EthertypeBase>, String>()
                    .put(EthertypeV4.class, "IPv4").put(EthertypeV6.class, "IPv6").build();

    private final NeutronSecurityGroupInterface securityGroupInterface;

    @Inject
    public NeutronSecurityRuleInterface(DataBroker db, NeutronSecurityGroupInterface securityGroupInterface) {
        super(SecurityRuleBuilder.class, db);
        this.securityGroupInterface = securityGroupInterface;
    }

    @Override
    protected List<SecurityRule> getDataObjectList(SecurityRules rules) {
        return rules.getSecurityRule();
    }

    @Override
    protected NeutronSecurityRule fromMd(SecurityRule rule) {
        final NeutronSecurityRule answer = new NeutronSecurityRule();
        fromMdIds(rule, answer);
        if (rule.getDirection() != null) {
            answer.setSecurityRuleDirection(DirectionMapper.getDirectionString(rule.getDirection()));
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
            final Protocol protocol = rule.getProtocol();
            if (protocol.getUint8() != null) {
                // uint8
                answer.setSecurityRuleProtocol(protocol.getUint8().toString());
            } else {
                // symbolic protocol name
                answer.setSecurityRuleProtocol(ProtocolMapper.getName(protocol.getIdentityref()));
            }
        }
        if (rule.getEthertype() != null) {
            answer.setSecurityRuleEthertype(ETHERTYPE_MAP.get(rule.getEthertype()));
        }
        if (rule.getPortRangeMin() != null) {
            answer.setSecurityRulePortMin(rule.getPortRangeMin());
        }
        if (rule.getPortRangeMax() != null) {
            answer.setSecurityRulePortMax(rule.getPortRangeMax());
        }
        return answer;
    }

    @Override
    @SuppressWarnings("checkstyle:AvoidHidingCauseException")
    protected SecurityRule toMd(NeutronSecurityRule securityRule) {
        final SecurityRuleBuilder securityRuleBuilder = new SecurityRuleBuilder();
        toMdIds(securityRule, securityRuleBuilder);
        if (securityRule.getSecurityRuleDirection() != null) {
            securityRuleBuilder
                    .setDirection(DirectionMapper.get(securityRule.getSecurityRuleDirection()));
        }
        if (securityRule.getSecurityRuleGroupID() != null) {
            securityRuleBuilder.setSecurityGroupId(toUuid(securityRule.getSecurityRuleGroupID()));
        }
        if (securityRule.getSecurityRemoteGroupID() != null) {
            securityRuleBuilder.setRemoteGroupId(toUuid(securityRule.getSecurityRemoteGroupID()));
        }
        if (securityRule.getSecurityRuleRemoteIpPrefix() != null) {
            final IpPrefix ipPrefix = new IpPrefix(securityRule.getSecurityRuleRemoteIpPrefix().toCharArray());
            securityRuleBuilder.setRemoteIpPrefix(ipPrefix);
        }
        if (securityRule.getSecurityRuleProtocol() != null) {
            final String protocolString = securityRule.getSecurityRuleProtocol();
            try {
                final Protocol protocol = new Protocol(protocolString.toCharArray());
                securityRuleBuilder.setProtocol(protocol);
            } catch (NumberFormatException e) {
                throw new BadRequestException("Protocol {" + securityRule.getSecurityRuleProtocol()
                        + "} is not supported");
            }
        }
        if (securityRule.getSecurityRuleEthertype() != null) {
            final ImmutableBiMap<String, Class<? extends EthertypeBase>> mapper = ETHERTYPE_MAP.inverse();
            securityRuleBuilder
                    .setEthertype(mapper.get(securityRule.getSecurityRuleEthertype()));
        }
        if (securityRule.getSecurityRulePortMin() != null) {
            securityRuleBuilder.setPortRangeMin(securityRule.getSecurityRulePortMin());
        }
        if (securityRule.getSecurityRulePortMax() != null) {
            securityRuleBuilder.setPortRangeMax(securityRule.getSecurityRulePortMax());
        }
        return securityRuleBuilder.build();
    }

    // TODO doc (on parent, no here): /** Implementations *MUST* use the exists() methods with a Tx! */
    // TODO @Override
    boolean areAllDependenciesAvailable(ReadTransaction tx, NeutronSecurityRule securityRule) {
        return securityGroupInterface.exists(securityRule.getSecurityRuleGroupID(), tx);
        // TODO but must null check:    && securityGroupInterface.exists(securityRule.getSecurityRemoteGroupID(), tx);
    }
}
