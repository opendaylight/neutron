/*
 * Copyright (c) 2015 IBM Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.neutron.transcriber;

import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.neutron.spi.INeutronVpnIkePolicyCRUD;
import org.opendaylight.neutron.spi.NeutronVpnIkePolicy;
import org.opendaylight.neutron.spi.NeutronVpnLifetime;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.vpnaas.rev150712.ikepolicies.attributes.IkePolicies;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.vpnaas.rev150712.ikepolicies.attributes.ike.policies.Ikepolicy;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.vpnaas.rev150712.ikepolicies.attributes.ike.policies.IkepolicyBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.vpnaas.rev150712.ikepolicies.attributes.ike.policies.IkepolicyKey;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.vpnaas.rev150712.ikepolicy.attributes.LifetimeBuilder;
import org.ops4j.pax.cdi.api.OsgiServiceProvider;

@Singleton
@OsgiServiceProvider(classes = INeutronVpnIkePolicyCRUD.class)
public final class NeutronVpnIkePolicyInterface
        extends AbstractNeutronInterface<Ikepolicy, IkePolicies, IkepolicyKey, NeutronVpnIkePolicy>
        implements INeutronVpnIkePolicyCRUD {

    @Inject
    public NeutronVpnIkePolicyInterface(DataBroker db) {
        super(IkepolicyBuilder.class, db);
    }

    // IfNBVPNIKEPolicyCRUD methods

    @Override
    protected List<Ikepolicy> getDataObjectList(IkePolicies policies) {
        return policies.getIkepolicy();
    }

    @Override
    protected NeutronVpnIkePolicy fromMd(Ikepolicy ikePolicy) {
        final NeutronVpnIkePolicy answer = new NeutronVpnIkePolicy();
        fromMdBaseAttributes(ikePolicy, answer);
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
            final NeutronVpnLifetime vpnLifetime = new NeutronVpnLifetime();
            ikePolicy.getLifetime();
            vpnLifetime.setUnits(ikePolicy.getLifetime().getUnits());
            vpnLifetime.setValue(ikePolicy.getLifetime().getValue());
            answer.setLifetime(vpnLifetime);
        }
        return answer;
    }

    @Override
    protected Ikepolicy toMd(NeutronVpnIkePolicy ikePolicy) {
        final IkepolicyBuilder ikePolicyBuilder = new IkepolicyBuilder();
        toMdBaseAttributes(ikePolicy, ikePolicyBuilder);
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
            final NeutronVpnLifetime vpnLifetime = ikePolicy.getLifetime();
            final LifetimeBuilder lifetimeBuilder = new LifetimeBuilder();
            lifetimeBuilder.setUnits(vpnLifetime.getUnits());
            lifetimeBuilder.setValue(vpnLifetime.getValue());
            ikePolicyBuilder.setLifetime(lifetimeBuilder.build());
        }
        return ikePolicyBuilder.build();
    }
}
