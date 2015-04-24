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
import org.opendaylight.neutron.spi.INeutronVPNIKEPolicyCRUD;
import org.opendaylight.neutron.spi.NeutronVPNIKEPolicy;
import org.opendaylight.yangtools.yang.binding.DataObject;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NeutronVPNIKEPolicyInterface extends AbstractNeutronInterface implements INeutronVPNIKEPolicyCRUD {
    private static final Logger logger = LoggerFactory.getLogger(NeutronVPNIKEPolicyInterface.class);
    private ConcurrentMap<String, NeutronVPNIKEPolicy> meteringLabelRuleDB = new ConcurrentHashMap<String, NeutronVPNIKEPolicy>();


    NeutronVPNIKEPolicyInterface(ProviderContext providerContext) {
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

    // IfNBVPNIKEPolicyCRUD methods

    @Override
    public boolean neutronVPNIKEPolicyExists(String uuid) {
        return meteringLabelRuleDB.containsKey(uuid);
    }

    @Override
    public NeutronVPNIKEPolicy getNeutronVPNIKEPolicy(String uuid) {
        if (!neutronVPNIKEPolicyExists(uuid)) {
            return null;
        }
        return meteringLabelRuleDB.get(uuid);
    }

    @Override
    public List<NeutronVPNIKEPolicy> getAllNeutronVPNIKEPolicies() {
        Set<NeutronVPNIKEPolicy> allVPNIKEPolicies = new HashSet<NeutronVPNIKEPolicy>();
        for (Entry<String, NeutronVPNIKEPolicy> entry : meteringLabelRuleDB.entrySet()) {
            NeutronVPNIKEPolicy meteringLabelRule = entry.getValue();
            allVPNIKEPolicies.add(meteringLabelRule);
        }
        logger.debug("Exiting getAllVPNIKEPolicies, Found {} OpenStackVPNIKEPolicies", allVPNIKEPolicies.size());
        List<NeutronVPNIKEPolicy> ans = new ArrayList<NeutronVPNIKEPolicy>();
        ans.addAll(allVPNIKEPolicies);
        return ans;
    }

    @Override
    public boolean addNeutronVPNIKEPolicy(NeutronVPNIKEPolicy input) {
        if (neutronVPNIKEPolicyExists(input.getID())) {
            return false;
        }
        meteringLabelRuleDB.putIfAbsent(input.getID(), input);
      //TODO: add code to find INeutronVPNIKEPolicyAware services and call newtorkCreated on them
        return true;
    }

    @Override
    public boolean removeNeutronVPNIKEPolicy(String uuid) {
        if (!neutronVPNIKEPolicyExists(uuid)) {
            return false;
        }
        meteringLabelRuleDB.remove(uuid);
      //TODO: add code to find INeutronVPNIKEPolicyAware services and call newtorkDeleted on them
        return true;
    }

    @Override
    public boolean updateNeutronVPNIKEPolicy(String uuid, NeutronVPNIKEPolicy delta) {
        if (!neutronVPNIKEPolicyExists(uuid)) {
            return false;
        }
        NeutronVPNIKEPolicy target = meteringLabelRuleDB.get(uuid);
        return overwrite(target, delta);
    }

    @Override
    public boolean neutronVPNIKEPolicyInUse(String netUUID) {
        if (!neutronVPNIKEPolicyExists(netUUID)) {
            return true;
        }
        return false;
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
