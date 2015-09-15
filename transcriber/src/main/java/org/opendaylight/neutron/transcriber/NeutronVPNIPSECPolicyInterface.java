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

    NeutronVPNIPSECPolicyInterface(ProviderContext providerContext) {
        super(providerContext);
    }

    // IfNBVPNIPSECPolicyCRUD methods

    @Override
    public boolean neutronVPNIPSECPolicyExists(String uuid) {
        return exists(uuid);
    }

    @Override
    public NeutronVPNIPSECPolicy getNeutronVPNIPSECPolicy(String uuid) {
        return get(uuid);
    }

    @Override
    public List<NeutronVPNIPSECPolicy> getAll() {
        Set<NeutronVPNIPSECPolicy> allVPNIPSECPolicies = new HashSet<NeutronVPNIPSECPolicy>();
        IpsecPolicies policies = readMd(createInstanceIdentifier());
        if (policies != null) {
            for (Ipsecpolicy policy: policies.getIpsecpolicy()) {
                allVPNIPSECPolicies.add(fromMd(policy));
            }
        }
        LOGGER.debug("Exiting getAllVPNIPSECPolicies, Found {} OpenStackVPNIPSECPolicies", allVPNIPSECPolicies.size());
        List<NeutronVPNIPSECPolicy> ans = new ArrayList<NeutronVPNIPSECPolicy>();
        ans.addAll(allVPNIPSECPolicies);
        return ans;
    }

    @Override
    public List<NeutronVPNIPSECPolicy> getAllNeutronVPNIPSECPolicies() {
        return getAll();
    }

    @Override
    public boolean addNeutronVPNIPSECPolicy(NeutronVPNIPSECPolicy input) {
        return add(input);
    }

    @Override
    public boolean removeNeutronVPNIPSECPolicy(String uuid) {
        return remove(uuid);
    }

    @Override
    public boolean updateNeutronVPNIPSECPolicy(String uuid, NeutronVPNIPSECPolicy delta) {
        return update(uuid, delta);
    }

    @Override
    public boolean neutronVPNIPSECPolicyInUse(String netUUID) {
        return !exists(netUUID);
    }

    protected NeutronVPNIPSECPolicy fromMd(Ipsecpolicy ipsecPolicy) {
        NeutronVPNIPSECPolicy answer = new NeutronVPNIPSECPolicy();
        if (ipsecPolicy.getName() != null) {
            answer.setName(ipsecPolicy.getName());
        }
        if (ipsecPolicy.getTenantId() != null) {
            answer.setTenantID(ipsecPolicy.getTenantId().getValue().replace("-",""));
        }
        if (ipsecPolicy.getDescr() != null) {
            answer.setDescription(ipsecPolicy.getDescr());
        }
        if (ipsecPolicy.getAuthAlgorithm() != null) {
            answer.setAuthAlgorithm(ipsecPolicy.getAuthAlgorithm());
        }
        if (ipsecPolicy.getEncryptionAlgorithm() != null) {
            answer.setEncryptionAlgorithm(ipsecPolicy.getEncryptionAlgorithm());
        }
        if (ipsecPolicy.getTransformProtocol() != null) {
            answer.setTransformProtocol(ipsecPolicy.getTransformProtocol());
        }
        if (ipsecPolicy.getEncapsulationMode() != null) {
            answer.setEncapsulationMode(ipsecPolicy.getEncapsulationMode());
        }
        if (ipsecPolicy.getPfs() != null) {
            answer.setPerfectForwardSecrecy(ipsecPolicy.getPfs());
        }
        if (ipsecPolicy.getLifetime() !=null) {
            NeutronVPNLifetime vpnLifetime = new NeutronVPNLifetime();
            vpnLifetime.setUnits(ipsecPolicy.getLifetime().getUnits());
            vpnLifetime.setValue(ipsecPolicy.getLifetime().getValue());
            answer.setLifetime(vpnLifetime);
        }
        if (ipsecPolicy.getUuid() != null) {
            answer.setID(ipsecPolicy.getUuid().getValue());
        }
        return answer;
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

    protected InstanceIdentifier<IpsecPolicies> createInstanceIdentifier() {
        return InstanceIdentifier.create(Neutron.class)
                 .child(IpsecPolicies.class);
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
