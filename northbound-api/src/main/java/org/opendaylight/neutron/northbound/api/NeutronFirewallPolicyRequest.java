/*
 * Copyright (c) 2014, 2015 Red Hat, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.northbound.api;

import org.opendaylight.neutron.spi.NeutronFirewallPolicy;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;


@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)

public class NeutronFirewallPolicyRequest
    implements INeutronRequest<NeutronFirewallPolicy> {
    /**
     * See OpenStack Network API v2.0 Reference for description of
     * http://docs.openstack.org/api/openstack-network/2.0/content/
     *
     */

    @XmlElement(name="firewall_policy")
    NeutronFirewallPolicy singletonFirewallPolicy;

    @XmlElement(name="firewall_policies")
    List<NeutronFirewallPolicy> bulkRequest;

    NeutronFirewallPolicyRequest() {
    }

    NeutronFirewallPolicyRequest(List<NeutronFirewallPolicy> bulk) {
        bulkRequest = bulk;
        singletonFirewallPolicy = null;
    }

    NeutronFirewallPolicyRequest(NeutronFirewallPolicy group) {
        singletonFirewallPolicy = group;
    }

    public List<NeutronFirewallPolicy> getBulk() {
        return bulkRequest;
    }

    public NeutronFirewallPolicy getSingleton() {
        return singletonFirewallPolicy;
    }

    public boolean isSingleton() {
        return (singletonFirewallPolicy != null);
    }
}
