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
import org.opendaylight.neutron.spi.INeutronVPNIKEPolicyCRUD;
import org.opendaylight.neutron.spi.NeutronVPNIKEPolicy;
import org.opendaylight.neutron.spi.NeutronVPNLifetime;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.rev150712.Neutron;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.vpnaas.rev150712.ikepolicies.attributes.IkePolicies;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.vpnaas.rev150712.ikepolicies.attributes.ike.policies.Ikepolicy;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.vpnaas.rev150712.ikepolicies.attributes.ike.policies.IkepolicyBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.vpnaas.rev150712.ikepolicy.attributes.LifetimeBuilder;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NeutronVPNIKEPolicyInterface extends AbstractNeutronInterface<Ikepolicy, IkePolicies, NeutronVPNIKEPolicy>
        implements INeutronVPNIKEPolicyCRUD {
    private static final Logger LOGGER = LoggerFactory.getLogger(NeutronVPNIKEPolicyInterface.class);

    NeutronVPNIKEPolicyInterface(DataBroker db) {
        super(db);
    }

    // IfNBVPNIKEPolicyCRUD methods

    @Override
    protected List<Ikepolicy> getDataObjectList(IkePolicies policies) {
        return policies.getIkepolicy();
    }

    protected NeutronVPNIKEPolicy fromMd(Ikepolicy ikePolicy) {
        final NeutronVPNIKEPolicy answer = new NeutronVPNIKEPolicy();
        if (ikePolicy.getName() != null) {
            answer.setName(ikePolicy.getName());
        }
        if (ikePolicy.getTenantId() != null) {
            answer.setTenantID(ikePolicy.getTenantId());
        }
        if (ikePolicy.getAuthAlgorithm() != null) {
            answer.setAuthAlgorithm(ikePolicy.getAuthAlgorithm());
        }
        if (ikePolicy.getEncryptionAlgorithm() != null) {
            answer.setEncryptionAlgorithm(ikePolicy.getEncryptionAlgorithm());
        }
        if (ikePolicy.getPhaseNegotiationMode() != null) {
            answer.setPhase1NegotiationMode(ikePolicy.getPhaseNegotiationMode());
        }
        if (ikePolicy.getPfs() != null) {
            answer.setPerfectForwardSecrecy(ikePolicy.getPfs());
        }
        if (ikePolicy.getIkeVersion() != null) {
            answer.setIkeVersion(ikePolicy.getIkeVersion());
        }
        if (ikePolicy.getLifetime() != null) {
            final NeutronVPNLifetime vpnLifetime = new NeutronVPNLifetime();
            ikePolicy.getLifetime();
            vpnLifetime.setUnits(ikePolicy.getLifetime().getUnits());
            vpnLifetime.setValue(ikePolicy.getLifetime().getValue());
            answer.setLifetime(vpnLifetime);
        }
        if (ikePolicy.getUuid() != null) {
            answer.setID(ikePolicy.getUuid().getValue());
        }
        return answer;
    }

    @Override
    protected Ikepolicy toMd(NeutronVPNIKEPolicy ikePolicy) {
        final IkepolicyBuilder ikePolicyBuilder = new IkepolicyBuilder();
        if (ikePolicy.getName() != null) {
            ikePolicyBuilder.setName(ikePolicy.getName());
        }
        if (ikePolicy.getTenantID() != null) {
            ikePolicyBuilder.setTenantId(toUuid(ikePolicy.getTenantID()));
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
        if (ikePolicy.getLifetime() != null) {
            final NeutronVPNLifetime vpnLifetime = ikePolicy.getLifetime();
            final LifetimeBuilder lifetimeBuilder = new LifetimeBuilder();
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
    protected InstanceIdentifier<Ikepolicy> createInstanceIdentifier(Ikepolicy ikePolicy) {
        return InstanceIdentifier.create(Neutron.class).child(IkePolicies.class).child(Ikepolicy.class,
                ikePolicy.getKey());
    }

    @Override
    protected InstanceIdentifier<IkePolicies> createInstanceIdentifier() {
        return InstanceIdentifier.create(Neutron.class).child(IkePolicies.class);
    }

    @Override
    protected Ikepolicy toMd(String uuid) {
        final IkepolicyBuilder ikePolicyBuilder = new IkepolicyBuilder();
        ikePolicyBuilder.setUuid(toUuid(uuid));
        return ikePolicyBuilder.build();
    }
}
