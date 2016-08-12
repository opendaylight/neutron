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
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.rev150712.Neutron;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 */

public class NeutronFirewallPolicyInterface
        extends AbstractNeutronInterface<FirewallPolicy, FirewallPolicies, NeutronFirewallPolicy>
        implements INeutronFirewallPolicyCRUD {
    private static final Logger LOGGER = LoggerFactory.getLogger(NeutronFirewallPolicyInterface.class);

    NeutronFirewallPolicyInterface(DataBroker db) {
        super(db);
    }

    @Override
    protected List<FirewallPolicy> getDataObjectList(FirewallPolicies policies) {
        return policies.getFirewallPolicy();
    }

    @Override
    protected InstanceIdentifier<FirewallPolicy> createInstanceIdentifier(FirewallPolicy item) {
        return InstanceIdentifier.create(Neutron.class).child(FirewallPolicies.class).child(FirewallPolicy.class,
                item.getKey());
    }

    @Override
    protected InstanceIdentifier<FirewallPolicies> createInstanceIdentifier() {
        return InstanceIdentifier.create(Neutron.class).child(FirewallPolicies.class);
    }

    protected NeutronFirewallPolicy fromMd(FirewallPolicy policy) {
        final NeutronFirewallPolicy answer = new NeutronFirewallPolicy();
        if (policy.getUuid() != null) {
            answer.setID(policy.getUuid().getValue());
        }
        if (policy.getName() != null) {
            answer.setFirewallPolicyName(policy.getName());
        }
        if (policy.getTenantId() != null) {
            answer.setTenantID(policy.getTenantId());
        }
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
        if (policy.getID() != null) {
            policyBuilder.setUuid(toUuid(policy.getID()));
        }
        if (policy.getFirewallPolicyName() != null) {
            policyBuilder.setName(policy.getFirewallPolicyName());
        }
        if (policy.getTenantID() != null) {
            policyBuilder.setTenantId(toUuid(policy.getTenantID()));
        }
        if (policy.getFirewallPolicyIsShared() != null) {
            policyBuilder.setShared(policy.getFirewallPolicyIsShared());
        }
        if (policy.getFirewallPolicyIsAudited() != null) {
            policyBuilder.setAudited(policy.getFirewallPolicyIsAudited());
        }
        return policyBuilder.build();
    }

    @Override
    protected FirewallPolicy toMd(String uuid) {
        final FirewallPolicyBuilder policyBuilder = new FirewallPolicyBuilder();
        policyBuilder.setUuid(toUuid(uuid));
        return policyBuilder.build();
    }
}
