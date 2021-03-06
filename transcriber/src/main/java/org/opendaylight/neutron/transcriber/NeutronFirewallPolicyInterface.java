/*
 * Copyright (c) 2014, 2015 Red Hat, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.neutron.transcriber;

import java.util.Collection;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.apache.aries.blueprint.annotation.service.Service;
import org.opendaylight.mdsal.binding.api.DataBroker;
import org.opendaylight.neutron.spi.INeutronFirewallPolicyCRUD;
import org.opendaylight.neutron.spi.NeutronFirewallPolicy;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.fwaas.rev150712.policies.attributes.FirewallPolicies;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.fwaas.rev150712.policies.attributes.firewall.policies.FirewallPolicy;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.fwaas.rev150712.policies.attributes.firewall.policies.FirewallPolicyBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.fwaas.rev150712.policies.attributes.firewall.policies.FirewallPolicyKey;

@Singleton
@Service(classes = INeutronFirewallPolicyCRUD.class)
public final class NeutronFirewallPolicyInterface
        extends AbstractNeutronInterface<FirewallPolicy, FirewallPolicies, FirewallPolicyKey, NeutronFirewallPolicy>
        implements INeutronFirewallPolicyCRUD {

    @Inject
    public NeutronFirewallPolicyInterface(DataBroker db) {
        super(FirewallPolicyBuilder.class, db);
    }

    @Override
    protected Collection<FirewallPolicy> getDataObjectList(FirewallPolicies policies) {
        return policies.nonnullFirewallPolicy().values();
    }

    @Override
    protected NeutronFirewallPolicy fromMd(FirewallPolicy policy) {
        final NeutronFirewallPolicy answer = new NeutronFirewallPolicy();
        fromMdBaseAttributes(policy, answer);
        if (policy.getShared() != null) {
            answer.setFirewallPolicyIsShared(policy.getShared());
        }
        if (policy.getAudited() != null) {
            answer.setFirewallPolicyIsAudited(policy.getAudited());
        }
        return answer;
    }

    @Override
    protected FirewallPolicy toMd(NeutronFirewallPolicy policy) {
        final FirewallPolicyBuilder policyBuilder = new FirewallPolicyBuilder();
        toMdBaseAttributes(policy, policyBuilder);
        if (policy.getFirewallPolicyIsShared() != null) {
            policyBuilder.setShared(policy.getFirewallPolicyIsShared());
        }
        if (policy.getFirewallPolicyIsAudited() != null) {
            policyBuilder.setAudited(policy.getFirewallPolicyIsAudited());
        }
        return policyBuilder.build();
    }
}
