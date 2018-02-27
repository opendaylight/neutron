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
import org.opendaylight.neutron.spi.INeutronVpnIpSecPolicyCRUD;
import org.opendaylight.neutron.spi.NeutronVpnIpSecPolicy;
import org.opendaylight.neutron.spi.NeutronVpnLifetime;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.vpnaas.rev150712.ipsecpolicies.attributes.IpsecPolicies;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.vpnaas.rev150712.ipsecpolicies.attributes.ipsec.policies.Ipsecpolicy;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.vpnaas.rev150712.ipsecpolicies.attributes.ipsec.policies.IpsecpolicyBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.vpnaas.rev150712.ipsecpolicies.attributes.ipsec.policies.IpsecpolicyKey;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.vpnaas.rev150712.ipsecpolicy.attributes.LifetimeBuilder;
import org.ops4j.pax.cdi.api.OsgiServiceProvider;

@Singleton
@OsgiServiceProvider(classes = INeutronVpnIpSecPolicyCRUD.class)
public final class NeutronVpnIpSecPolicyInterface
        extends AbstractNeutronInterface<Ipsecpolicy, IpsecPolicies, IpsecpolicyKey, NeutronVpnIpSecPolicy>
        implements INeutronVpnIpSecPolicyCRUD {

    @Inject
    public NeutronVpnIpSecPolicyInterface(DataBroker db) {
        super(IpsecpolicyBuilder.class, db);
    }

    // IfNBVPNIPSECPolicyCRUD methods

    @Override
    protected List<Ipsecpolicy> getDataObjectList(IpsecPolicies policies) {
        return policies.getIpsecpolicy();
    }

    @Override
    protected NeutronVpnIpSecPolicy fromMd(Ipsecpolicy ipsecPolicy) {
        final NeutronVpnIpSecPolicy answer = new NeutronVpnIpSecPolicy();
        fromMdBaseAttributes(ipsecPolicy, answer);
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
            final NeutronVpnLifetime vpnLifetime = new NeutronVpnLifetime();
            vpnLifetime.setUnits(ipsecPolicy.getLifetime().getUnits());
            vpnLifetime.setValue(ipsecPolicy.getLifetime().getValue());
            answer.setLifetime(vpnLifetime);
        }
        return answer;
    }

    @Override
    protected Ipsecpolicy toMd(NeutronVpnIpSecPolicy ipsecPolicy) {
        final IpsecpolicyBuilder ipsecPolicyBuilder = new IpsecpolicyBuilder();
        toMdBaseAttributes(ipsecPolicy, ipsecPolicyBuilder);
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
            final NeutronVpnLifetime vpnLifetime = ipsecPolicy.getLifetime();
            final LifetimeBuilder lifetimeBuilder = new LifetimeBuilder();
            lifetimeBuilder.setUnits(vpnLifetime.getUnits());
            lifetimeBuilder.setValue(vpnLifetime.getValue());
            ipsecPolicyBuilder.setLifetime(lifetimeBuilder.build());
        }
        return ipsecPolicyBuilder.build();
    }
}
