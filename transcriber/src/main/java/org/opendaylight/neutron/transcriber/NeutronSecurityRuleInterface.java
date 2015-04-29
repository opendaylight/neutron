/*
 * Copyright (C) 2014 Red Hat, Inc.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 */

package org.opendaylight.neutron.transcriber;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.opendaylight.controller.sal.binding.api.BindingAwareBroker.ProviderContext;
import org.opendaylight.neutron.spi.INeutronSecurityRuleCRUD;
import org.opendaylight.neutron.spi.NeutronSecurityRule;
import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.inet.types.rev100924.IpAddress;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.rev150325.Neutron;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.secgroups.rev141002.SecurityRuleAttrs;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.secgroups.rev141002.security.rules.attributes.SecurityRules;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.secgroups.rev141002.security.rules.attributes.security.rules.SecurityRule;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.secgroups.rev141002.security.rules.attributes.security.rules.SecurityRuleBuilder;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class NeutronSecurityRuleInterface extends AbstractNeutronInterface<SecurityRule, NeutronSecurityRule> implements INeutronSecurityRuleCRUD {
    private static final Logger logger = LoggerFactory.getLogger(NeutronSecurityRuleInterface.class);
    private ConcurrentMap<String, NeutronSecurityRule> securityRuleDB  = new ConcurrentHashMap<String, NeutronSecurityRule>();


    NeutronSecurityRuleInterface(ProviderContext providerContext) {
        super(providerContext);
    }

    // this method uses reflection to update an object from it's delta.
    private boolean overwrite(Object target, Object delta) {
        Method[] methods = target.getClass().getMethods();

        for (Method toMethod : methods) {
            if (toMethod.getDeclaringClass().equals(target.getClass())
                && toMethod.getName().startsWith("set")) {

                String toName = toMethod.getName();
                String fromName = toName.replace("set", "get");

                try {
                    Method fromMethod = delta.getClass().getMethod(fromName);
                    Object value = fromMethod.invoke(delta, (Object[]) null);
                    if (value != null) {
                        toMethod.invoke(target, value);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean neutronSecurityRuleExists(String uuid) {
        return securityRuleDB.containsKey(uuid);
    }

    @Override
    public NeutronSecurityRule getNeutronSecurityRule(String uuid) {
        if (!neutronSecurityRuleExists(uuid)) {
            logger.debug("No Security Rules Have Been Defined");
            return null;
        }
        return securityRuleDB.get(uuid);
    }

    @Override
    public List<NeutronSecurityRule> getAllNeutronSecurityRules() {
        Set<NeutronSecurityRule> allSecurityRules = new HashSet<NeutronSecurityRule>();
        for (Entry<String, NeutronSecurityRule> entry : securityRuleDB.entrySet()) {
            NeutronSecurityRule securityRule = entry.getValue();
            allSecurityRules.add(securityRule);
        }
        logger.debug("Exiting getSecurityRule, Found {} OpenStackSecurityRule", allSecurityRules.size());
        List<NeutronSecurityRule> ans = new ArrayList<NeutronSecurityRule>();
        ans.addAll(allSecurityRules);
        return ans;
    }

    @Override
    public boolean addNeutronSecurityRule(NeutronSecurityRule input) {
        if (neutronSecurityRuleExists(input.getSecurityRuleUUID())) {
            return false;
        }
        securityRuleDB.putIfAbsent(input.getSecurityRuleUUID(), input);
        addMd(input);
        return true;
    }

    @Override
    public boolean removeNeutronSecurityRule(String uuid) {
        if (!neutronSecurityRuleExists(uuid)) {
            return false;
        }
        securityRuleDB.remove(uuid);
        removeMd(toMd(uuid));
        return true;
    }

    @Override
    public boolean updateNeutronSecurityRule(String uuid, NeutronSecurityRule delta) {
        if (!neutronSecurityRuleExists(uuid)) {
            return false;
        }
        NeutronSecurityRule target = securityRuleDB.get(uuid);
        updateMd(delta);
        return overwrite(target, delta);
    }

    @Override
    public boolean neutronSecurityRuleInUse(String securityRuleUUID) {
        return !neutronSecurityRuleExists(securityRuleUUID);
    }

    @Override
    protected SecurityRule toMd(NeutronSecurityRule securityRule) {
        SecurityRuleBuilder securityRuleBuilder = new SecurityRuleBuilder();

        if (securityRule.getSecurityRuleUUID() != null) {
            securityRuleBuilder.setId(toUuid(securityRule.getSecurityRuleUUID()));
        }
        if (securityRule.getSecurityRuleTenantID() != null) {
            securityRuleBuilder.setTenantId(toUuid(securityRule.getSecurityRuleTenantID()));
        }
        if (securityRule.getSecurityRuleDirection() != null) {
            securityRuleBuilder.setDirection(SecurityRuleAttrs.Direction.valueOf(securityRule
                    .getSecurityRuleDirection()));
        }
        if (securityRule.getSecurityRuleGroupID() != null) {
            securityRuleBuilder.setSecurityGroupId(toUuid(securityRule.getSecurityRuleGroupID()));
        }
        if (securityRule.getSecurityRemoteGroupID() != null) {
            securityRuleBuilder.setRemoteGroupId(toUuid(securityRule.getSecurityRemoteGroupID()));
        }
        if (securityRule.getSecurityRuleRemoteIpPrefix() != null) {
            IpAddress ipAddress = new IpAddress(securityRule.getSecurityRuleRemoteIpPrefix().toCharArray());
            securityRuleBuilder.setRemoteIpPrefix(ipAddress);
        }
        if (securityRule.getSecurityRuleProtocol() != null) {
            securityRuleBuilder.setProtocol(SecurityRuleAttrs.Protocol.valueOf(securityRule.getSecurityRuleProtocol()));
        }
        if (securityRule.getSecurityRuleEthertype() != null) {
            securityRuleBuilder.setEthertype(SecurityRuleAttrs.Ethertype.valueOf(securityRule
                    .getSecurityRuleEthertype()));
        }
        if (securityRule.getSecurityRulePortMin() != null) {
            securityRuleBuilder.setPortRangeMin(new Long(securityRule.getSecurityRulePortMin()));
        }
        if (securityRule.getSecurityRulePortMax() != null) {
            securityRuleBuilder.setPortRangeMax(new Long(securityRule.getSecurityRulePortMax()));
        }
        return securityRuleBuilder.build();
    }

    @Override
    protected InstanceIdentifier<SecurityRule> createInstanceIdentifier(SecurityRule securityRule) {
        return InstanceIdentifier.create(Neutron.class).child(SecurityRules.class).child(SecurityRule.class);
    }

    @Override
    protected SecurityRule toMd(String uuid) {
        SecurityRuleBuilder securityRuleBuilder = new SecurityRuleBuilder();
        securityRuleBuilder.setId(toUuid(uuid));
        return securityRuleBuilder.build();
    }
}