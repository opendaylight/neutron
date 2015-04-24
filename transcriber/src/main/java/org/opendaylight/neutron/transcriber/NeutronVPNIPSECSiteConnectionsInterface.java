/*
 * Copyright Tata Consultancy Services, 2015.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
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
import org.opendaylight.neutron.spi.INeutronVPNIPSECSiteConnectionsCRUD;
import org.opendaylight.neutron.spi.NeutronVPNIPSECSiteConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NeutronVPNIPSECSiteConnectionsInterface extends AbstractNeutronInterface implements INeutronVPNIPSECSiteConnectionsCRUD {
    private static final Logger logger = LoggerFactory.getLogger(NeutronVPNIKEPolicyInterface.class);
    private ConcurrentMap<String, NeutronVPNIPSECSiteConnection> neutronVPNIPSECSiteConnectionDB = new ConcurrentHashMap<String, NeutronVPNIPSECSiteConnection>();


    NeutronVPNIPSECSiteConnectionsInterface(ProviderContext providerContext) {
        super(providerContext);
    }

    // this method uses reflection to update an object from it's delta.

    private boolean overwrite(Object target, Object delta) {
        Method[] methods = target.getClass().getMethods();

        for (Method toMethod : methods) {
            if (toMethod.getDeclaringClass().equals(target.getClass()) && toMethod.getName().startsWith("set")) {

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

    // INeutronVPNIPSECSiteConnectionsCRUD methods

    @Override
    public boolean neutronVPNIPSECSiteConnectionsExists(String policyID) {
        return neutronVPNIPSECSiteConnectionDB.containsKey(policyID);
    }

    @Override
    public NeutronVPNIPSECSiteConnection getNeutronVPNIPSECSiteConnections(String policyID) {
        if (!neutronVPNIPSECSiteConnectionsExists(policyID)) {
            return null;
        }
        return neutronVPNIPSECSiteConnectionDB.get(policyID);
    }

    @Override
    public List<NeutronVPNIPSECSiteConnection> getAllNeutronVPNIPSECSiteConnections() {
        Set<NeutronVPNIPSECSiteConnection> allNeutronVPNIPSECSiteConnections = new HashSet<NeutronVPNIPSECSiteConnection>();
        for (Entry<String, NeutronVPNIPSECSiteConnection> entry : neutronVPNIPSECSiteConnectionDB.entrySet()) {
            NeutronVPNIPSECSiteConnection meteringLabelRule = entry.getValue();
            allNeutronVPNIPSECSiteConnections.add(meteringLabelRule);
        }
        logger.debug("Exiting getAllNeutronVPNIPSECSiteConnections, Found {} OpenStackVPNIPSECSiteConnections", allNeutronVPNIPSECSiteConnections.size());
        List<NeutronVPNIPSECSiteConnection> ans = new ArrayList<NeutronVPNIPSECSiteConnection>();
        ans.addAll(allNeutronVPNIPSECSiteConnections);
        return ans;
    }

    @Override
    public boolean addNeutronVPNIPSECSiteConnections(NeutronVPNIPSECSiteConnection input) {
        if (neutronVPNIPSECSiteConnectionsExists(input.getID())) {
            return false;
        }
        neutronVPNIPSECSiteConnectionDB.putIfAbsent(input.getID(), input);
        return true;
    }

    @Override
    public boolean removeNeutronVPNIPSECSiteConnections(String policyID) {
        if (!neutronVPNIPSECSiteConnectionsExists(policyID)) {
            return false;
        }
        neutronVPNIPSECSiteConnectionDB.remove(policyID);
        return true;
    }

    @Override
    public boolean updateNeutronVPNIPSECSiteConnections(String policyID, NeutronVPNIPSECSiteConnection delta) {
        if (!neutronVPNIPSECSiteConnectionsExists(policyID)) {
            return false;
        }
        NeutronVPNIPSECSiteConnection target = neutronVPNIPSECSiteConnectionDB.get(policyID);
        return overwrite(target, delta);
    }

    @Override
    public boolean neutronVPNIPSECSiteConnectionsInUse(String policyID) {
        if (!neutronVPNIPSECSiteConnectionsExists(policyID)) {
            return true;
        }
        return false;
    }
}