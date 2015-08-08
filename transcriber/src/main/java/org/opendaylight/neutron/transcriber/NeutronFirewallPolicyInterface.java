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
import org.opendaylight.neutron.spi.INeutronFirewallPolicyCRUD;
import org.opendaylight.neutron.spi.INeutronObject;
import org.opendaylight.neutron.spi.NeutronFirewallPolicy;
import org.opendaylight.yangtools.yang.binding.DataObject;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 */

public class NeutronFirewallPolicyInterface extends AbstractNeutronInterface implements INeutronFirewallPolicyCRUD {
    private static final Logger LOGGER = LoggerFactory.getLogger(NeutronFirewallPolicyInterface.class);

    private ConcurrentMap<String, NeutronFirewallPolicy> firewallPolicyDB  = new ConcurrentHashMap<String, NeutronFirewallPolicy>();


    NeutronFirewallPolicyInterface(ProviderContext providerContext) {
        super(providerContext);
    }

    @Override
    public boolean neutronFirewallPolicyExists(String uuid) {
        return firewallPolicyDB.containsKey(uuid);
    }

    @Override
    public NeutronFirewallPolicy getNeutronFirewallPolicy(String uuid) {
        if (!neutronFirewallPolicyExists(uuid)) {
            LOGGER.debug("No Firewall Rule Have Been Defined");
            return null;
        }
        return firewallPolicyDB.get(uuid);
    }

    @Override
    public List<NeutronFirewallPolicy> getAllNeutronFirewallPolicies() {
        Set<NeutronFirewallPolicy> allFirewallPolicies = new HashSet<NeutronFirewallPolicy>();
        for (Entry<String, NeutronFirewallPolicy> entry : firewallPolicyDB.entrySet()) {
            NeutronFirewallPolicy firewallPolicy = entry.getValue();
            allFirewallPolicies.add(firewallPolicy);
        }
        LOGGER.debug("Exiting getFirewallPolicies, Found {} OpenStackFirewallPolicy", allFirewallPolicies.size());
        List<NeutronFirewallPolicy> ans = new ArrayList<NeutronFirewallPolicy>();
        ans.addAll(allFirewallPolicies);
        return ans;
    }

    @Override
    public boolean addNeutronFirewallPolicy(NeutronFirewallPolicy input) {
        if (neutronFirewallPolicyExists(input.getID())) {
            return false;
        }
        firewallPolicyDB.putIfAbsent(input.getID(), input);
        return true;
    }

    @Override
    public boolean removeNeutronFirewallPolicy(String uuid) {
        if (!neutronFirewallPolicyExists(uuid)) {
            return false;
        }
        firewallPolicyDB.remove(uuid);
        return true;
    }

    @Override
    public boolean updateNeutronFirewallPolicy(String uuid, NeutronFirewallPolicy delta) {
        if (!neutronFirewallPolicyExists(uuid)) {
            return false;
        }
        NeutronFirewallPolicy target = firewallPolicyDB.get(uuid);
        return overwrite(target, delta);
    }

    @Override
    public boolean neutronFirewallPolicyInUse(String firewallPolicyUUID) {
        return !neutronFirewallPolicyExists(firewallPolicyUUID);
    }

    @Override
    protected InstanceIdentifier createInstanceIdentifier(DataObject item) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected DataObject toMd(INeutronObject neutronObject) {
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
                                            List<ServiceRegistration<?>> registrations) {
        NeutronFirewallPolicyInterface neutronFirewallPolicyInterface = new NeutronFirewallPolicyInterface(providerContext);
        ServiceRegistration<INeutronFirewallPolicyCRUD> neutronFirewallPolicyInterfaceRegistration = context.registerService(INeutronFirewallPolicyCRUD.class, neutronFirewallPolicyInterface, null);
        if(neutronFirewallPolicyInterfaceRegistration != null) {
            registrations.add(neutronFirewallPolicyInterfaceRegistration);
        }
    }
}
