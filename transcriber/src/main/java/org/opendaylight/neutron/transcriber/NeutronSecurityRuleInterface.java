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
import org.opendaylight.neutron.spi.INeutronSecurityGroupCRUD;
import org.opendaylight.neutron.spi.INeutronSecurityRuleCRUD;
import org.opendaylight.neutron.spi.NeutronCRUDInterfaces;
import org.opendaylight.neutron.spi.NeutronSecurityGroup;
import org.opendaylight.neutron.spi.NeutronSecurityRule;
import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.inet.types.rev100924.IpPrefix;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.rev150325.Neutron;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.secgroups.rev141002.SecurityRuleAttrs;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.secgroups.rev141002.security.rules.attributes.SecurityRules;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.secgroups.rev141002.security.rules.attributes.security.rules.SecurityRule;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.secgroups.rev141002.security.rules.attributes.security.rules.SecurityRuleBuilder;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class NeutronSecurityRuleInterface extends AbstractNeutronInterface<SecurityRule, NeutronSecurityRule> implements INeutronSecurityRuleCRUD {
    private static final Logger LOGGER = LoggerFactory.getLogger(NeutronSecurityRuleInterface.class);
    private ConcurrentMap<String, NeutronSecurityRule> securityRuleDB = new ConcurrentHashMap<String, NeutronSecurityRule>();


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
                    LOGGER.error(e.getMessage());
                    return false;
                }
            }
        }
        return true;
    }

    private void updateSecGroupRuleInSecurityGroup(NeutronSecurityRule input) {
        INeutronSecurityGroupCRUD sgCrud = NeutronCRUDInterfaces.getINeutronSecurityGroupCRUD(this);
        NeutronSecurityGroup sg = sgCrud.getNeutronSecurityGroup(input.getSecurityRuleGroupID());
        if(sg != null && sg.getSecurityRules() != null) {
            for(NeutronSecurityRule sgr :sg.getSecurityRules()) {
                if(sgr.getSecurityRuleUUID() != null && sgr.getSecurityRuleUUID().equals(input.getSecurityRuleUUID())) {
                    int index = sg.getSecurityRules().indexOf(sgr);
                    sg.getSecurityRules().set(index, input);
                }
            }
        }
        if (sg != null) {
            sg.getSecurityRules().add(input);
        }
    }

    private void removeSecGroupRuleFromSecurityGroup(NeutronSecurityRule input) {
        INeutronSecurityGroupCRUD sgCrud = NeutronCRUDInterfaces.getINeutronSecurityGroupCRUD(this);
        NeutronSecurityGroup sg = sgCrud.getNeutronSecurityGroup(input.getSecurityRuleGroupID());
        if(sg != null && sg.getSecurityRules() != null) {
            List<NeutronSecurityRule> toRemove = new ArrayList<NeutronSecurityRule>();
            for(NeutronSecurityRule sgr :sg.getSecurityRules()) {
                if(sgr.getSecurityRuleUUID() != null && sgr.getSecurityRuleUUID().equals(input.getSecurityRuleUUID())) {
                    toRemove.add(sgr);
                }
            }
            sg.getSecurityRules().removeAll(toRemove);
        }
    }

    @Override
    public boolean neutronSecurityRuleExists(String uuid) {
        return securityRuleDB.containsKey(uuid);
    }

    @Override
    public NeutronSecurityRule getNeutronSecurityRule(String uuid) {
        if (!neutronSecurityRuleExists(uuid)) {
            LOGGER.debug("No Security Rules Have Been Defined");
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
        LOGGER.debug("Exiting getSecurityRule, Found {} OpenStackSecurityRule", allSecurityRules.size());
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
        updateSecGroupRuleInSecurityGroup(input);
        addMd(input);
        return true;
    }

    @Override
    public boolean removeNeutronSecurityRule(String uuid) {
        if (!neutronSecurityRuleExists(uuid)) {
            return false;
        }
        removeSecGroupRuleFromSecurityGroup(securityRuleDB.get(uuid));
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
        boolean rc = overwrite(target, delta);
        updateSecGroupRuleInSecurityGroup(securityRuleDB.get(uuid));
        if (rc) {
            updateMd(securityRuleDB.get(uuid));
        }
        return rc;
    }

    @Override
    public boolean neutronSecurityRuleInUse(String securityRuleUUID) {
        return !neutronSecurityRuleExists(securityRuleUUID);
    }

    @Override
    protected SecurityRule toMd(NeutronSecurityRule securityRule) {
        SecurityRuleBuilder securityRuleBuilder = new SecurityRuleBuilder();

        if (securityRule.getSecurityRuleTenantID() != null) {
            securityRuleBuilder.setTenantId(toUuid(securityRule.getSecurityRuleTenantID()));
        }
        if (securityRule.getSecurityRuleDirection() != null) {
            boolean foundMatch = false;
            for (SecurityRuleAttrs.Direction direction : SecurityRuleAttrs.Direction.values()) {
                if (direction.toString().equalsIgnoreCase(securityRule.getSecurityRuleDirection())) {
                    securityRuleBuilder.setDirection(direction);
                    foundMatch = true;
                    break;
                }
            }
            if (!foundMatch) {
                LOGGER.warn("Unable to find direction value for {}", securityRule.getSecurityRuleDirection());
            }
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
            boolean foundMatch = false;
            for (SecurityRuleAttrs.Protocol.Enumeration protocol : SecurityRuleAttrs.Protocol.Enumeration.values()) {
                if (protocol.toString().equalsIgnoreCase(securityRule.getSecurityRuleProtocol())) {
                    securityRuleBuilder.setProtocol(new SecurityRuleAttrs.Protocol(protocol));
                    foundMatch = true;
                    break;
                }
            }
            if (!foundMatch) {
                try {
                    java.lang.Short protocol = Short.valueOf(securityRule.getSecurityRuleProtocol());
                    securityRuleBuilder.setProtocol(new SecurityRuleAttrs.Protocol(protocol));
                } catch (NumberFormatException e) {
                    LOGGER.warn("Unable to find protocol value for {}", securityRule.getSecurityRuleProtocol());
                }
            }
        }
        if (securityRule.getSecurityRuleEthertype() != null) {
            boolean foundMatch = false;
            for (SecurityRuleAttrs.Ethertype etherType : SecurityRuleAttrs.Ethertype.values()) {
                if (etherType.toString().equalsIgnoreCase(securityRule.getSecurityRuleEthertype())) {
                    securityRuleBuilder.setEthertype(etherType);
                    foundMatch = true;
                    break;
                }
            }
            if (!foundMatch) {
                LOGGER.warn("Unable to find ethertype value for {}", securityRule.getSecurityRuleEthertype());
            }
        }
        if (securityRule.getSecurityRulePortMin() != null) {
            securityRuleBuilder.setPortRangeMin(new Integer(securityRule.getSecurityRulePortMin()));
        }
        if (securityRule.getSecurityRulePortMax() != null) {
            securityRuleBuilder.setPortRangeMax(new Integer(securityRule.getSecurityRulePortMax()));
        }
        if (securityRule.getSecurityRuleUUID() != null) {
            securityRuleBuilder.setId(toUuid(securityRule.getSecurityRuleUUID()));
        } else {
            LOGGER.warn("Attempting to write neutron securityRule without UUID");
        }
        return securityRuleBuilder.build();
    }

    @Override
    protected InstanceIdentifier<SecurityRule> createInstanceIdentifier(SecurityRule securityRule) {
        return InstanceIdentifier.create(Neutron.class).child(SecurityRules.class).child(SecurityRule.class,
                securityRule.getKey());
    }

    @Override
    protected SecurityRule toMd(String uuid) {
        SecurityRuleBuilder securityRuleBuilder = new SecurityRuleBuilder();
        securityRuleBuilder.setId(toUuid(uuid));
        return securityRuleBuilder.build();
    }
}
