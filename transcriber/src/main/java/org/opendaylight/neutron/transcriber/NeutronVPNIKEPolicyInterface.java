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
import org.opendaylight.neutron.spi.NeutronVPNLifetime;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.vpnaas.rev141002.ikepolicy.attrs.LifetimeBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.vpnaas.rev141002.vpnaas.attributes.IkePolicies;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.vpnaas.rev141002.vpnaas.attributes.ike.policies.IkePolicy;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.vpnaas.rev141002.vpnaas.attributes.ike.policies.IkePolicyBuilder;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NeutronVPNIKEPolicyInterface extends AbstractNeutronInterface<IkePolicy, NeutronVPNIKEPolicy> implements INeutronVPNIKEPolicyCRUD {
    private static final Logger LOGGER = LoggerFactory.getLogger(NeutronVPNIKEPolicyInterface.class);
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
                    LOGGER.error(e.getMessage());
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
        LOGGER.debug("Exiting getAllVPNIKEPolicies, Found {} OpenStackVPNIKEPolicies", allVPNIKEPolicies.size());
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
        addMd(input);
      //TODO: add code to find INeutronVPNIKEPolicyAware services and call newtorkCreated on them
        return true;
    }

    @Override
    public boolean removeNeutronVPNIKEPolicy(String uuid) {
        if (!neutronVPNIKEPolicyExists(uuid)) {
            return false;
        }
        meteringLabelRuleDB.remove(uuid);
        removeMd(toMd(uuid));
      //TODO: add code to find INeutronVPNIKEPolicyAware services and call newtorkDeleted on them
        return true;
    }

    @Override
    public boolean updateNeutronVPNIKEPolicy(String uuid, NeutronVPNIKEPolicy delta) {
        if (!neutronVPNIKEPolicyExists(uuid)) {
            return false;
        }
        NeutronVPNIKEPolicy target = meteringLabelRuleDB.get(uuid);
        boolean rc = overwrite(target, delta);
        if (rc) {
            updateMd(meteringLabelRuleDB.get(uuid));
        }
        return rc;
    }

    @Override
    public boolean neutronVPNIKEPolicyInUse(String netUUID) {
        if (!neutronVPNIKEPolicyExists(netUUID)) {
            return true;
        }
        return false;
    }


    @Override
    protected IkePolicy toMd(NeutronVPNIKEPolicy ikePolicy) {
        IkePolicyBuilder ikePolicyBuilder = new IkePolicyBuilder();
        if (ikePolicy.getName() != null) {
            ikePolicyBuilder.setName(ikePolicy.getName());
        }
        if (ikePolicy.getTenantID() != null && !ikePolicy.getTenantID().isEmpty()) {
            ikePolicyBuilder.setTenantId(toUuid(ikePolicy.getTenantID()));
        }
        if (ikePolicy.getDescription() != null) {
            ikePolicyBuilder.setDescr(ikePolicy.getDescription());
        }
        if (ikePolicy.getAuthAlgorithm() != null) {
            ikePolicyBuilder.setAuthAlgorithm(ikePolicy.getAuthAlgorithm());
        }
        if (ikePolicy.getEncryptionAlgorithm() != null) {
            ikePolicyBuilder.setEncryptionAlgorithm(ikePolicy.getEncryptionAlgorithm());
        }
        if (ikePolicy.getPhase1NegotiationMode() != null) {
            ikePolicyBuilder.setPhaseNegotiationMode(ikePolicy.getPhase1NegotiationMode());
        }
        if (ikePolicy.getPerfectForwardSecrecy() != null) {
            ikePolicyBuilder.setPfs(ikePolicy.getPerfectForwardSecrecy());
        }
        if (ikePolicy.getIkeVersion() != null) {
            ikePolicyBuilder.setIkeVersion(ikePolicy.getIkeVersion());
        }
        if (ikePolicy.getLifetime() !=null) {
            NeutronVPNLifetime vpnLifetime = ikePolicy.getLifetime();
            LifetimeBuilder lifetimeBuilder = new LifetimeBuilder();
            lifetimeBuilder.setUnits(vpnLifetime.getUnits());
            lifetimeBuilder.setValue(vpnLifetime.getValue());
            ikePolicyBuilder.setLifetime(lifetimeBuilder.build());
        }
        if (ikePolicy.getID() != null) {
            ikePolicyBuilder.setUuid(toUuid(ikePolicy.getID()));
        } else {
            LOGGER.warn("Attempting to write neutron vpnIKEPolicy without UUID");
        }
        return ikePolicyBuilder.build();
    }


    @Override
    protected InstanceIdentifier<IkePolicy> createInstanceIdentifier(IkePolicy ikePolicy) {
        return InstanceIdentifier.create(IkePolicies.class).child(IkePolicy.class, ikePolicy.getKey());
    }


    @Override
    protected IkePolicy toMd(String uuid) {
        IkePolicyBuilder ikePolicyBuilder = new IkePolicyBuilder();
        ikePolicyBuilder.setUuid(toUuid(uuid));
        return ikePolicyBuilder.build();
    }
}
