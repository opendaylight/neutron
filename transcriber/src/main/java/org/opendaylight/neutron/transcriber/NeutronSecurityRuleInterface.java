/*
 * Copyright (C) 2014 Red Hat, Inc.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 */

package org.opendaylight.neutron.transcriber;

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
import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.inet.types.rev100924.IpAddress;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.constants.rev160807.DirectionBase;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.constants.rev160807.DirectionEgress;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.constants.rev160807.DirectionIngress;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.constants.rev160807.EthertypeBase;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.constants.rev160807.EthertypeV4;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.constants.rev160807.EthertypeV6;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.constants.rev160807.ProtocolBase;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.constants.rev160807.ProtocolHttp;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.constants.rev160807.ProtocolHttps;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.constants.rev160807.ProtocolIcmp;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.constants.rev160807.ProtocolTcp;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.rev150325.Neutron;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.secgroups.rev141002.security.rules.attributes.SecurityRules;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.secgroups.rev141002.security.rules.attributes.security.rules.SecurityRule;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.secgroups.rev141002.security.rules.attributes.security.rules.SecurityRuleBuilder;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableBiMap;


public class NeutronSecurityRuleInterface extends AbstractNeutronInterface<SecurityRule, NeutronSecurityRule> implements INeutronSecurityRuleCRUD {
    private static final Logger LOGGER = LoggerFactory.getLogger(NeutronSecurityRuleInterface.class);
    private ConcurrentMap<String, NeutronSecurityRule> securityRuleDB = new ConcurrentHashMap<String, NeutronSecurityRule>();

    private static final ImmutableBiMap<Class<? extends DirectionBase>,String> DIRECTION_MAP
            = new ImmutableBiMap.Builder<Class<? extends DirectionBase>,String>()
            .put(DirectionEgress.class,"egress")
            .put(DirectionIngress.class,"ingress")
            .build();
    private static final ImmutableBiMap<Class<? extends ProtocolBase>,String> PROTOCOL_MAP
            = new ImmutableBiMap.Builder<Class<? extends ProtocolBase>,String>()
            .put(ProtocolHttp.class,"HTTP")
            .put(ProtocolHttps.class,"HTTPS")
            .put(ProtocolIcmp.class,"ICMP")
            .put(ProtocolTcp.class,"TCP")
            .build();
    private static final ImmutableBiMap<Class<? extends EthertypeBase>,String> ETHERTYPE_MAP
            = new ImmutableBiMap.Builder<Class<? extends EthertypeBase>,String>()
            .put(EthertypeV4.class,"v4")
            .put(EthertypeV6.class,"v6")
            .build();

    NeutronSecurityRuleInterface(ProviderContext providerContext) {
        super(providerContext);
    }

    private void updateSecGroupRuleInSecurityGroup(NeutronSecurityRule input) {
        NeutronCRUDInterfaces interfaces = new NeutronCRUDInterfaces()
            .fetchINeutronSecurityGroupCRUD(this);
        INeutronSecurityGroupCRUD sgCrud = interfaces.getSecurityGroupInterface();
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
        NeutronCRUDInterfaces interfaces = new NeutronCRUDInterfaces()
            .fetchINeutronSecurityGroupCRUD(this);
        INeutronSecurityGroupCRUD sgCrud = interfaces.getSecurityGroupInterface();
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
            IpAddress ipAddress = new IpAddress(securityRule.getSecurityRuleRemoteIpPrefix().toCharArray());
            securityRuleBuilder.setRemoteIpPrefix(ipAddress);
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
