/*
 * Copyright (c) 2015 IBM Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.transcriber;

import java.util.List;
import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.neutron.spi.INeutronVPNIPSECPolicyCRUD;
import org.opendaylight.neutron.spi.NeutronVPNIPSECPolicy;
import org.opendaylight.neutron.spi.NeutronVPNLifetime;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.rev150712.Neutron;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.vpnaas.rev150712.ipsecpolicies.attributes.IpsecPolicies;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.vpnaas.rev150712.ipsecpolicies.attributes.ipsec.policies.Ipsecpolicy;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.vpnaas.rev150712.ipsecpolicies.attributes.ipsec.policies.IpsecpolicyBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.vpnaas.rev150712.ipsecpolicy.attributes.LifetimeBuilder;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NeutronVPNIPSECPolicyInterface
        extends AbstractNeutronInterface<Ipsecpolicy, IpsecPolicies, NeutronVPNIPSECPolicy>
        implements INeutronVPNIPSECPolicyCRUD {
    private static final Logger LOGGER = LoggerFactory.getLogger(NeutronVPNIPSECPolicyInterface.class);

    NeutronVPNIPSECPolicyInterface(DataBroker db) {
        super(db);
    }

    // IfNBVPNIPSECPolicyCRUD methods

    @Override
    protected List<Ipsecpolicy> getDataObjectList(IpsecPolicies policies) {
        return policies.getIpsecpolicy();
    }

    protected NeutronVPNIPSECPolicy fromMd(Ipsecpolicy ipsecPolicy) {
        final NeutronVPNIPSECPolicy answer = new NeutronVPNIPSECPolicy();
        if (ipsecPolicy.getName() != null) {
            answer.setName(ipsecPolicy.getName());
        }
        if (ipsecPolicy.getTenantId() != null) {
            answer.setTenantID(ipsecPolicy.getTenantId());
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
        if (ipsecPolicy.getLifetime() != null) {
            final NeutronVPNLifetime vpnLifetime = new NeutronVPNLifetime();
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
        final IpsecpolicyBuilder ipsecPolicyBuilder = new IpsecpolicyBuilder();
        if (ipsecPolicy.getName() != null) {
            ipsecPolicyBuilder.setName(ipsecPolicy.getName());
        }
        if (ipsecPolicy.getTenantID() != null) {
            ipsecPolicyBuilder.setTenantId(toUuid(ipsecPolicy.getTenantID()));
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
        if (ipsecPolicy.getLifetime() != null) {
            final NeutronVPNLifetime vpnLifetime = ipsecPolicy.getLifetime();
            final LifetimeBuilder lifetimeBuilder = new LifetimeBuilder();
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
    protected Ipsecpolicy toMd(String uuid) {
        final IpsecpolicyBuilder ipsecPolicyBuilder = new IpsecpolicyBuilder();
        ipsecPolicyBuilder.setUuid(toUuid(uuid));
        return ipsecPolicyBuilder.build();
    }

    @Override
    protected InstanceIdentifier<Ipsecpolicy> createInstanceIdentifier(Ipsecpolicy ipsecPolicy) {
        return InstanceIdentifier.create(Neutron.class).child(IpsecPolicies.class).child(Ipsecpolicy.class,
                ipsecPolicy.getKey());
    }

    @Override
    protected InstanceIdentifier<IpsecPolicies> createInstanceIdentifier() {
        return InstanceIdentifier.create(Neutron.class).child(IpsecPolicies.class);
    }
}
