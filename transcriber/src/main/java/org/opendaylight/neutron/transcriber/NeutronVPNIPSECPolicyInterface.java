/*
 * Copyright (c) 2015 IBM Corporation and others.  All rights reserved.
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
import org.opendaylight.neutron.spi.INeutronVPNIPSECPolicyCRUD;
import org.opendaylight.neutron.spi.NeutronVPNIPSECPolicy;
import org.opendaylight.neutron.spi.NeutronVPNLifetime;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.vpnaas.rev141002.ipsecpolicy.attributes.LifetimeBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.vpnaas.rev141002.ipsecpolicies.attributes.IpsecPolicies;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.vpnaas.rev141002.ipsecpolicies.attributes.ipsec.policies.Ipsecpolicy;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.vpnaas.rev141002.ipsecpolicies.attributes.ipsec.policies.IpsecpolicyBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.rev150325.Neutron;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NeutronVPNIPSECPolicyInterface extends AbstractNeutronInterface<Ipsecpolicy, NeutronVPNIPSECPolicy> implements INeutronVPNIPSECPolicyCRUD {
    private static final Logger LOGGER = LoggerFactory.getLogger(NeutronVPNIPSECPolicyInterface.class);
    private ConcurrentMap<String, NeutronVPNIPSECPolicy> meteringLabelRuleDB = new ConcurrentHashMap<String, NeutronVPNIPSECPolicy>();


    NeutronVPNIPSECPolicyInterface(ProviderContext providerContext) {
        super(providerContext);
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
    protected Ipsecpolicy toMd(NeutronVPNIPSECPolicy ipsecPolicy) {
        IpsecpolicyBuilder ipsecPolicyBuilder = new IpsecpolicyBuilder();
        if (ipsecPolicy.getName() != null) {
            ipsecPolicyBuilder.setName(ipsecPolicy.getName());
        }
        if (ipsecPolicy.getTenantID() != null) {
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
    protected InstanceIdentifier<Ipsecpolicy> createInstanceIdentifier(Ipsecpolicy ipsecPolicy) {
        return InstanceIdentifier.create(Neutron.class)
                 .child(IpsecPolicies.class)
                 .child(Ipsecpolicy.class, ipsecPolicy.getKey());
    }


    @Override
    protected Ipsecpolicy toMd(String uuid) {
        IpsecpolicyBuilder ipsecPolicyBuilder = new IpsecpolicyBuilder();
        ipsecPolicyBuilder.setUuid(toUuid(uuid));
        return ipsecPolicyBuilder.build();
    }

    public static void registerNewInterface(BundleContext context,
                                            ProviderContext providerContext,
                                            List<ServiceRegistration<?>> registrations) {
        NeutronVPNIPSECPolicyInterface neutronVPNIPSECPolicyInterface = new NeutronVPNIPSECPolicyInterface(providerContext);
        ServiceRegistration<INeutronVPNIPSECPolicyCRUD> neutronVPNIPSECPolicyInterfaceRegistration = context.registerService(INeutronVPNIPSECPolicyCRUD.class, neutronVPNIPSECPolicyInterface, null);
        if(neutronVPNIPSECPolicyInterfaceRegistration != null) {
            registrations.add(neutronVPNIPSECPolicyInterfaceRegistration);
        }
    }
}
