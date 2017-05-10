/*
 * Copyright (c) 2014, 2015 Red Hat, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.transcriber;

import java.util.List;
import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.neutron.spi.INeutronFirewallPolicyCRUD;
import org.opendaylight.neutron.spi.NeutronFirewallPolicy;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.fwaas.rev150712.policies.attributes.FirewallPolicies;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.fwaas.rev150712.policies.attributes.firewall.policies.FirewallPolicy;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.fwaas.rev150712.policies.attributes.firewall.policies.FirewallPolicyBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.fwaas.rev150712.policies.attributes.firewall.policies.FirewallPolicyKey;


public final class NeutronFirewallPolicyInterface
        extends AbstractNeutronInterface<FirewallPolicy, FirewallPolicies, FirewallPolicyKey, NeutronFirewallPolicy>
        implements INeutronFirewallPolicyCRUD {
    NeutronFirewallPolicyInterface(DataBroker db) {
        super(FirewallPolicyBuilder.class, db);
    }

    @Override
    protected List<FirewallPolicy> getDataObjectList(FirewallPolicies policies) {
        return policies.getFirewallPolicy();
    }

    protected NeutronFirewallPolicy fromMd(FirewallPolicy policy) {
        final NeutronFirewallPolicy answer = new NeutronFirewallPolicy();
        fromMdBaseAttributes(policy, answer);
        if (policy.isShared() != null) {
            answer.setFirewallPolicyIsShared(policy.isShared());
        }
        if (policy.isAudited() != null) {
            answer.setFirewallPolicyIsAudited(policy.isAudited());
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
