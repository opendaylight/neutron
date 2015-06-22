/*
 * Copyright (C) 2014 Red Hat, Inc.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.transcriber;

import org.opendaylight.controller.sal.binding.api.BindingAwareBroker.ProviderContext;
import org.opendaylight.neutron.spi.INeutronFirewallCRUD;
import org.opendaylight.neutron.spi.NeutronFirewall;
import org.opendaylight.yangtools.yang.binding.DataObject;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Deprecated as all Neutron FWaaS is experimental and so doesn't meet
 * the scope of neutron northbound
 *
 * @deprecated
 */

public class NeutronFirewallInterface extends AbstractNeutronInterface implements INeutronFirewallCRUD {

    private static final Logger LOGGER = LoggerFactory.getLogger(NeutronFirewallInterface.class);

    private ConcurrentMap<String, NeutronFirewall> firewallDB  = new ConcurrentHashMap<String, NeutronFirewall>();

    NeutronFirewallInterface() { super(); }

    NeutronFirewallInterface(ProviderContext providerContext) {
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
                    LOGGER.error("Overwrite via reflection", e);
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean neutronFirewallExists(String uuid) {
        return firewallDB.containsKey(uuid);
    }

    @Override
    public NeutronFirewall getNeutronFirewall(String uuid) {
        if (!neutronFirewallExists(uuid)) {
            LOGGER.debug("No Firewall Have Been Defined");
            return null;
        }
        return firewallDB.get(uuid);
    }

    @Override
    public List<NeutronFirewall> getAllNeutronFirewalls() {
        Set<NeutronFirewall> allFirewalls = new HashSet<NeutronFirewall>();
        for (Entry<String, NeutronFirewall> entry : firewallDB.entrySet()) {
            NeutronFirewall firewall = entry.getValue();
            allFirewalls.add(firewall);
        }
        LOGGER.debug("Exiting getFirewalls, Found {} OpenStackFirewall", allFirewalls.size());
        List<NeutronFirewall> ans = new ArrayList<NeutronFirewall>();
        ans.addAll(allFirewalls);
        return ans;
    }

    @Override
    public boolean addNeutronFirewall(NeutronFirewall input) {
        if (neutronFirewallExists(input.getFirewallUUID())) {
            return false;
        }
        firewallDB.putIfAbsent(input.getFirewallUUID(), input);
        return true;
    }

    @Override
    public boolean removeNeutronFirewall(String uuid) {
        if (!neutronFirewallExists(uuid)) {
            return false;
        }
        firewallDB.remove(uuid);
        return true;
    }

    @Override
    public boolean updateNeutronFirewall(String uuid, NeutronFirewall delta) {
        if (!neutronFirewallExists(uuid)) {
            return false;
        }
        NeutronFirewall target = firewallDB.get(uuid);
        return overwrite(target, delta);
    }

    @Override
    public boolean neutronFirewallInUse(String firewallUUID) {
        return !neutronFirewallExists(firewallUUID);
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


}
