/*
 * Copyright IBM Corporation, 2015.  All rights reserved.
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
import org.opendaylight.neutron.spi.INeutronMeteringLabelRuleCRUD;
import org.opendaylight.neutron.spi.NeutronMeteringLabelRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NeutronMeteringLabelRuleInterface extends AbstractNeutronInterface implements INeutronMeteringLabelRuleCRUD {
    private static final Logger logger = LoggerFactory.getLogger(NeutronMeteringLabelRuleInterface.class);
    private ConcurrentMap<String, NeutronMeteringLabelRule> meteringLabelRuleDB = new ConcurrentHashMap<String, NeutronMeteringLabelRule>();



    NeutronMeteringLabelRuleInterface(ProviderContext providerContext) {
        super(providerContext);
    }

    // this method uses reflection to update an object from it's delta.

    private boolean overwrite(Object target, Object delta) {
        Method[] methods = target.getClass().getMethods();

        for(Method toMethod: methods){
            if(toMethod.getDeclaringClass().equals(target.getClass())
                    && toMethod.getName().startsWith("set")){

                String toName = toMethod.getName();
                String fromName = toName.replace("set", "get");

                try {
                    Method fromMethod = delta.getClass().getMethod(fromName);
                    Object value = fromMethod.invoke(delta, (Object[])null);
                    if(value != null){
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

    // IfNBMeteringLabelRuleCRUD methods

    @Override
    public boolean neutronMeteringLabelRuleExists(String uuid) {
        return meteringLabelRuleDB.containsKey(uuid);
    }

    @Override
    public NeutronMeteringLabelRule getNeutronMeteringLabelRule(String uuid) {
        if (!neutronMeteringLabelRuleExists(uuid)) {
            return null;
        }
        return meteringLabelRuleDB.get(uuid);
    }

    @Override
    public List<NeutronMeteringLabelRule> getAllNeutronMeteringLabelRules() {
        Set<NeutronMeteringLabelRule> allMeteringLabelRules = new HashSet<NeutronMeteringLabelRule>();
        for (Entry<String, NeutronMeteringLabelRule> entry : meteringLabelRuleDB.entrySet()) {
            NeutronMeteringLabelRule meteringLabelRule = entry.getValue();
            allMeteringLabelRules.add(meteringLabelRule);
        }
        logger.debug("Exiting getAllMeteringLabelRules, Found {} OpenStackMeteringLabelRules", allMeteringLabelRules.size());
        List<NeutronMeteringLabelRule> ans = new ArrayList<NeutronMeteringLabelRule>();
        ans.addAll(allMeteringLabelRules);
        return ans;
    }

    @Override
    public boolean addNeutronMeteringLabelRule(NeutronMeteringLabelRule input) {
        if (neutronMeteringLabelRuleExists(input.getMeteringLabelRuleUUID())) {
            return false;
        }
        meteringLabelRuleDB.putIfAbsent(input.getMeteringLabelRuleUUID(), input);
      //TODO: add code to find INeutronMeteringLabelRuleAware services and call newtorkCreated on them
        return true;
    }

    @Override
    public boolean removeNeutronMeteringLabelRule(String uuid) {
        if (!neutronMeteringLabelRuleExists(uuid)) {
            return false;
        }
        meteringLabelRuleDB.remove(uuid);
      //TODO: add code to find INeutronMeteringLabelRuleAware services and call newtorkDeleted on them
        return true;
    }

    @Override
    public boolean updateNeutronMeteringLabelRule(String uuid, NeutronMeteringLabelRule delta) {
        if (!neutronMeteringLabelRuleExists(uuid)) {
            return false;
        }
        NeutronMeteringLabelRule target = meteringLabelRuleDB.get(uuid);
        return overwrite(target, delta);
    }

    @Override
    public boolean neutronMeteringLabelRuleInUse(String netUUID) {
        if (!neutronMeteringLabelRuleExists(netUUID)) {
            return true;
        }
        return false;
    }
}
