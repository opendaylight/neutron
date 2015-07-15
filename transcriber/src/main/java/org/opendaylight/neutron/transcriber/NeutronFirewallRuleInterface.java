/*
 * Copyright (C) 2014 Red Hat, Inc.
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
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.opendaylight.controller.sal.binding.api.BindingAwareBroker.ProviderContext;
import org.opendaylight.neutron.spi.INeutronFirewallRuleCRUD;
import org.opendaylight.neutron.spi.NeutronFirewallRule;
import org.opendaylight.yangtools.yang.binding.DataObject;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NeutronFirewallRuleInterface extends AbstractNeutronInterface implements INeutronFirewallRuleCRUD {
    private static final Logger LOGGER = LoggerFactory.getLogger(NeutronFirewallRuleInterface.class);

    private ConcurrentMap<String, NeutronFirewallRule> firewallRuleDB = new ConcurrentHashMap<String, NeutronFirewallRule>();


    NeutronFirewallRuleInterface(ProviderContext providerContext) {
        super(providerContext);
    }

    @Override
    public boolean neutronFirewallRuleExists(String uuid) {
        return firewallRuleDB.containsKey(uuid);
    }

    @Override
    public NeutronFirewallRule getNeutronFirewallRule(String uuid) {
        if (!neutronFirewallRuleExists(uuid)) {
            LOGGER.debug("No Firewall Rule Have Been Defined");
            return null;
        }
        return firewallRuleDB.get(uuid);
    }

    @Override
    public List<NeutronFirewallRule> getAllNeutronFirewallRules() {
        Set<NeutronFirewallRule> allFirewallRules = new HashSet<NeutronFirewallRule>();
        for (Entry<String, NeutronFirewallRule> entry : firewallRuleDB.entrySet()) {
            NeutronFirewallRule firewallRule = entry.getValue();
            allFirewallRules.add(firewallRule);
        }
        LOGGER.debug("Exiting getFirewallRules, Found {} OpenStackFirewallRule", allFirewallRules.size());
        List<NeutronFirewallRule> ans = new ArrayList<NeutronFirewallRule>();
        ans.addAll(allFirewallRules);
        return ans;
    }

    @Override
    public boolean addNeutronFirewallRule(NeutronFirewallRule input) {
        if (neutronFirewallRuleExists(input.getFirewallRuleUUID())) {
            return false;
        }
        firewallRuleDB.putIfAbsent(input.getFirewallRuleUUID(), input);
        return true;
    }

    @Override
    public boolean removeNeutronFirewallRule(String uuid) {
        if (!neutronFirewallRuleExists(uuid)) {
            return false;
        }
        firewallRuleDB.remove(uuid);
        return true;
    }

    @Override
    public boolean updateNeutronFirewallRule(String uuid, NeutronFirewallRule delta) {
        if (!neutronFirewallRuleExists(uuid)) {
            return false;
        }
        NeutronFirewallRule target = firewallRuleDB.get(uuid);
        return overwrite(target, delta);
    }

    @Override
    public boolean neutronFirewallRuleInUse(String firewallRuleUUID) {
        return !neutronFirewallRuleExists(firewallRuleUUID);
    }

    @Override
    protected InstanceIdentifier createInstanceIdentifier(DataObject item) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected DataObject toMd(Object neutronObject) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected DataObject toMd(String uuid) {
        // TODO Auto-generated method stub
        return null;
    }

    public static void registerNewInterface(BundleContext context,
                                            ProviderContext providerContext,
                                            List<ServiceRegistration<?>> registrations) throws Exception {
        NeutronFirewallRuleInterface neutronFirewallRuleInterface = new NeutronFirewallRuleInterface(providerContext);
        ServiceRegistration<INeutronFirewallRuleCRUD> neutronFirewallRuleInterfaceRegistration = context.registerService(INeutronFirewallRuleCRUD.class, neutronFirewallRuleInterface, null);
        if(neutronFirewallRuleInterfaceRegistration != null) {
            registrations.add(neutronFirewallRuleInterfaceRegistration);
        }
    }
}
