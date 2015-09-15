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
import org.opendaylight.neutron.spi.INeutronFirewallRuleCRUD;
import org.opendaylight.neutron.spi.INeutronObject;
import org.opendaylight.neutron.spi.NeutronFirewallRule;

import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.inet.types.rev100924.IpAddress;
import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.yang.types.rev130715.Uuid;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.fwaas.rev141002.rules.attributes.FirewallRules;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.fwaas.rev141002.rules.attributes.firewall.rules.FirewallRule;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.fwaas.rev141002.rules.attributes.firewall.rules.FirewallRuleBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.rev150325.Neutron;

import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NeutronFirewallRuleInterface extends AbstractNeutronInterface<FirewallRule,NeutronFirewallRule> implements INeutronFirewallRuleCRUD {
    private static final Logger LOGGER = LoggerFactory.getLogger(NeutronFirewallRuleInterface.class);

    NeutronFirewallRuleInterface(ProviderContext providerContext) {
        super(providerContext);
    }

    @Override
    public boolean neutronFirewallRuleExists(String uuid) {
        return exists(uuid);
    }

    @Override
    public NeutronFirewallRule getNeutronFirewallRule(String uuid) {
        return get(uuid);
    }

    @Override
    public List<NeutronFirewallRule> getAll() {
        Set<NeutronFirewallRule> allFirewallRules = new HashSet<NeutronFirewallRule>();
        FirewallRules rules = readMd(createInstanceIdentifier());
        if (rules != null) {
            for (FirewallRule rule: rules.getFirewallRule()) {
                allFirewallRules.add(fromMd(rule));
            }
        }
        LOGGER.debug("Exiting getFirewallRules, Found {} OpenStackFirewallRule", allFirewallRules.size());
        List<NeutronFirewallRule> ans = new ArrayList<NeutronFirewallRule>();
        ans.addAll(allFirewallRules);
        return ans;
    }

    @Override
    public List<NeutronFirewallRule> getAllNeutronFirewallRules() {
        return getAll();
    }

    @Override
    public boolean addNeutronFirewallRule(NeutronFirewallRule input) {
        return add(input);
    }

    @Override
    public boolean removeNeutronFirewallRule(String uuid) {
        return remove(uuid);
    }

    @Override
    public boolean updateNeutronFirewallRule(String uuid, NeutronFirewallRule delta) {
        return update(uuid, delta);
    }

    @Override
    public boolean neutronFirewallRuleInUse(String firewallRuleUUID) {
        return !exists(firewallRuleUUID);
    }

    @Override
    protected InstanceIdentifier<FirewallRule> createInstanceIdentifier(FirewallRule item) {
        return InstanceIdentifier.create(Neutron.class)
                .child(FirewallRules.class)
                .child(FirewallRule.class, item.getKey());
    }

    protected InstanceIdentifier<FirewallRules> createInstanceIdentifier() {
        return InstanceIdentifier.create(Neutron.class)
                .child(FirewallRules.class);
    }

    protected NeutronFirewallRule fromMd(FirewallRule rule) {
        NeutronFirewallRule answer = new NeutronFirewallRule();
        if (rule.getUuid() != null) {
            answer.setID(rule.getUuid().getValue());
        }
        if (rule.getName() != null) {
            answer.setFirewallRuleName(rule.getName());
        }
        if (rule.getTenantId() != null) {
            answer.setFirewallRuleTenantID(rule.getTenantId().getValue().replace("-",""));
        }
        if (rule.getDescr() != null) {
            answer.setFirewallRuleDescription(rule.getDescr());
        }
        if (rule.getStatus() != null) {
            answer.setFirewallRuleStatus(rule.getStatus());
        }
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
            answer.setFirewallRuleProtocol(rule.getProtocol());
        }
        if (rule.getIpVersion() != null) {
            answer.setFirewallRuleIpVer(rule.getIpVersion().intValue());
        }
        if (rule.getSourceIpAddr() != null) {
            answer.setFirewallRuleSrcIpAddr(String.valueOf(rule.getSourceIpAddr().getValue()));
        }
        if (rule.getDestinationIpAddr() != null) {
            answer.setFirewallRuleDstIpAddr(String.valueOf(rule.getDestinationIpAddr().getValue()));
        }
        if (rule.getSourcePort() != null) {
            answer.setFirewallRuleSrcPort(rule.getSourcePort().intValue());
        }
        if (rule.getDestinationPort() != null) {
            answer.setFirewallRuleDstPort(rule.getDestinationPort().intValue());
        }
        if (rule.getPosition() != null) {
            answer.setFirewallRulePosition(rule.getPosition().intValue());
        }
        if (rule.getAction() != null) {
            answer.setFirewallRuleAction(rule.getAction());
        }
        return answer;
    }

    @Override
    protected FirewallRule toMd(NeutronFirewallRule rule) {
        FirewallRuleBuilder ruleBuilder = new FirewallRuleBuilder();
        if (rule.getID() != null) {
            ruleBuilder.setUuid(toUuid(rule.getID()));
        }
        if (rule.getFirewallRuleName() != null) {
            ruleBuilder.setName(rule.getFirewallRuleName());
        }
        if (rule.getFirewallRuleTenantID() != null) {
            ruleBuilder.setTenantId(toUuid(rule.getFirewallRuleTenantID()));
        }
        if (rule.getFirewallRuleDescription() != null) {
            ruleBuilder.setDescr(rule.getFirewallRuleDescription());
        }
        if (rule.getFirewallRuleStatus() != null) {
            ruleBuilder.setStatus(rule.getFirewallRuleStatus());
        }
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
            ruleBuilder.setProtocol(rule.getFirewallRuleProtocol());
        }
        if (rule.getFirewallRuleIpVer() != null) {
            ruleBuilder.setIpVersion(rule.getFirewallRuleIpVer().shortValue());
        }
        if (rule.getFirewallRuleSrcIpAddr() != null) {
            IpAddress ipAddress = new IpAddress(rule.getFirewallRuleSrcIpAddr().toCharArray());
            ruleBuilder.setSourceIpAddr(ipAddress);
        }
        if (rule.getFirewallRuleDstIpAddr() != null) {
            IpAddress ipAddress = new IpAddress(rule.getFirewallRuleDstIpAddr().toCharArray());
            ruleBuilder.setDestinationIpAddr(ipAddress);
        }
        if (rule.getFirewallRuleSrcPort() != null) {
            ruleBuilder.setSourcePort(rule.getFirewallRuleSrcPort().shortValue());
        }
        if (rule.getFirewallRuleDstPort() != null) {
            ruleBuilder.setDestinationPort(rule.getFirewallRuleDstPort().shortValue());
        }
        if (rule.getFirewallRulePosition() != null) {
            ruleBuilder.setPosition(rule.getFirewallRulePosition().shortValue());
        }
        if (rule.getFirewallRuleAction() != null) {
            ruleBuilder.setAction(rule.getFirewallRuleAction());
        }
        return ruleBuilder.build();
    }

    @Override
    protected FirewallRule toMd(String uuid) {
        FirewallRuleBuilder ruleBuilder = new FirewallRuleBuilder();
        ruleBuilder.setUuid(toUuid(uuid));
        return ruleBuilder.build();
    }

    public static void registerNewInterface(BundleContext context,
                                            ProviderContext providerContext,
                                            List<ServiceRegistration<?>> registrations) {
        NeutronFirewallRuleInterface neutronFirewallRuleInterface = new NeutronFirewallRuleInterface(providerContext);
        ServiceRegistration<INeutronFirewallRuleCRUD> neutronFirewallRuleInterfaceRegistration = context.registerService(INeutronFirewallRuleCRUD.class, neutronFirewallRuleInterface, null);
        if(neutronFirewallRuleInterfaceRegistration != null) {
            registrations.add(neutronFirewallRuleInterfaceRegistration);
        }
    }
}
