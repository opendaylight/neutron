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
import org.opendaylight.neutron.spi.INeutronVPNIPSECPolicyCRUD;
import org.opendaylight.neutron.spi.NeutronVPNIPSECPolicy;
import org.opendaylight.neutron.spi.NeutronVPNLifetime;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.vpnaas.rev141002.ipsecpolicy.attrs.LifetimeBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.vpnaas.rev141002.vpnaas.attributes.IpsecPolicies;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.vpnaas.rev141002.vpnaas.attributes.ipsec.policies.IpsecPolicy;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.vpnaas.rev141002.vpnaas.attributes.ipsec.policies.IpsecPolicyBuilder;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NeutronVPNIPSECPolicyInterface extends AbstractNeutronInterface<IpsecPolicy, NeutronVPNIPSECPolicy> implements INeutronVPNIPSECPolicyCRUD {
    private static final Logger LOGGER = LoggerFactory.getLogger(NeutronVPNIPSECPolicyInterface.class);
    private ConcurrentMap<String, NeutronVPNIPSECPolicy> meteringLabelRuleDB = new ConcurrentHashMap<String, NeutronVPNIPSECPolicy>();


    NeutronVPNIPSECPolicyInterface(ProviderContext providerContext) {
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

    // IfNBVPNIPSECPolicyCRUD methods

    @Override
    public boolean neutronVPNIPSECPolicyExists(String uuid) {
        return meteringLabelRuleDB.containsKey(uuid);
    }

    @Override
    public NeutronVPNIPSECPolicy getNeutronVPNIPSECPolicy(String uuid) {
        if (!neutronVPNIPSECPolicyExists(uuid)) {
            return null;
        }
        return meteringLabelRuleDB.get(uuid);
    }

    @Override
    public List<NeutronVPNIPSECPolicy> getAllNeutronVPNIPSECPolicies() {
        Set<NeutronVPNIPSECPolicy> allVPNIPSECPolicies = new HashSet<NeutronVPNIPSECPolicy>();
        for (Entry<String, NeutronVPNIPSECPolicy> entry : meteringLabelRuleDB.entrySet()) {
            NeutronVPNIPSECPolicy meteringLabelRule = entry.getValue();
            allVPNIPSECPolicies.add(meteringLabelRule);
        }
        LOGGER.debug("Exiting getAllVPNIPSECPolicies, Found {} OpenStackVPNIPSECPolicies", allVPNIPSECPolicies.size());
        List<NeutronVPNIPSECPolicy> ans = new ArrayList<NeutronVPNIPSECPolicy>();
        ans.addAll(allVPNIPSECPolicies);
        return ans;
    }

    @Override
    public boolean addNeutronVPNIPSECPolicy(NeutronVPNIPSECPolicy input) {
        if (neutronVPNIPSECPolicyExists(input.getID())) {
            return false;
        }
        meteringLabelRuleDB.putIfAbsent(input.getID(), input);
        addMd(input);
      //TODO: add code to find INeutronVPNIPSECPolicyAware services and call newtorkCreated on them
        return true;
    }

    @Override
    public boolean removeNeutronVPNIPSECPolicy(String uuid) {
        if (!neutronVPNIPSECPolicyExists(uuid)) {
            return false;
        }
        meteringLabelRuleDB.remove(uuid);
        removeMd(toMd(uuid));
      //TODO: add code to find INeutronVPNIPSECPolicyAware services and call newtorkDeleted on them
        return true;
    }

    @Override
    public boolean updateNeutronVPNIPSECPolicy(String uuid, NeutronVPNIPSECPolicy delta) {
        if (!neutronVPNIPSECPolicyExists(uuid)) {
            return false;
        }
        NeutronVPNIPSECPolicy target = meteringLabelRuleDB.get(uuid);
        boolean rc = overwrite(target, delta);
        if (rc) {
            updateMd(meteringLabelRuleDB.get(uuid));
        }
        return rc;
    }

    @Override
    public boolean neutronVPNIPSECPolicyInUse(String netUUID) {
        if (!neutronVPNIPSECPolicyExists(netUUID)) {
            return true;
        }
        return false;
    }


    @Override
    protected IpsecPolicy toMd(NeutronVPNIPSECPolicy ipsecPolicy) {
        IpsecPolicyBuilder ipsecPolicyBuilder = new IpsecPolicyBuilder();
        if (ipsecPolicy.getName() != null) {
            ipsecPolicyBuilder.setName(ipsecPolicy.getName());
        }
        if (ipsecPolicy.getTenantID() != null && !ipsecPolicy.getTenantID().isEmpty()) {
            ipsecPolicyBuilder.setTenantId(toUuid(ipsecPolicy.getTenantID()));
        }
        if (ipsecPolicy.getDescription() != null) {
            ipsecPolicyBuilder.setDescr(ipsecPolicy.getDescription());
        }
        if (ipsecPolicy.getAuthAlgorithm() != null) {
            ipsecPolicyBuilder.setAuthAlgorithm(ipsecPolicy.getAuthAlgorithm());
        }
        if (ipsecPolicy.getEncryptionAlgorithm() != null) {
            ipsecPolicyBuilder.setEncryptionAlgorithm(ipsecPolicy.getEncryptionAlgorithm());
        }
        if (ipsecPolicy.getTransformProtocol() != null) {
            ipsecPolicyBuilder.setTransformProtocol(ipsecPolicy.getTransformProtocol());
        }
        if (ipsecPolicy.getEncapsulationMode() != null) {
            ipsecPolicyBuilder.setEncapsulationMode(ipsecPolicy.getEncapsulationMode());
        }
        if (ipsecPolicy.getPerfectForwardSecrecy() != null) {
            ipsecPolicyBuilder.setPfs(ipsecPolicy.getPerfectForwardSecrecy());
        }
        if (ipsecPolicy.getLifetime() !=null) {
            NeutronVPNLifetime vpnLifetime = ipsecPolicy.getLifetime();
            LifetimeBuilder lifetimeBuilder = new LifetimeBuilder();
            lifetimeBuilder.setUnits(vpnLifetime.getUnits());
            lifetimeBuilder.setValue(vpnLifetime.getValue());
            ipsecPolicyBuilder.setLifetime(lifetimeBuilder.build());
        }
        if (ipsecPolicy.getID() != null) {
            ipsecPolicyBuilder.setUuid(toUuid(ipsecPolicy.getID()));
        } else {
            LOGGER.warn("Attempting to write neutron vpnIPSECPolicy without UUID");
        }
        return ipsecPolicyBuilder.build();
    }


    @Override
    protected InstanceIdentifier<IpsecPolicy> createInstanceIdentifier(IpsecPolicy ipsecPolicy) {
        return InstanceIdentifier.create(IpsecPolicies.class).child(IpsecPolicy.class, ipsecPolicy.getKey());
    }


    @Override
    protected IpsecPolicy toMd(String uuid) {
        IpsecPolicyBuilder ipsecPolicyBuilder = new IpsecPolicyBuilder();
        ipsecPolicyBuilder.setUuid(toUuid(uuid));
        return ipsecPolicyBuilder.build();
    }
}
