/*
 * Copyright (c) 2014, 2015 Red Hat, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.northbound.api;

import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import org.opendaylight.neutron.spi.NeutronLoadBalancerPoolMember;

public class NeutronLoadBalancerPoolMemberRequest
    implements INeutronRequest<NeutronLoadBalancerPoolMember> {

    /**
     * See OpenStack Network API v2.0 Reference for description of
     * http://docs.openstack.org/api/openstack-network/2.0/content/
     */

    @XmlElement(name="member")
    NeutronLoadBalancerPoolMember singletonLoadBalancerPoolMember;

    @XmlElement(name="members")
    List<NeutronLoadBalancerPoolMember> bulkRequest;

    NeutronLoadBalancerPoolMemberRequest() {
    }

    NeutronLoadBalancerPoolMemberRequest(List<NeutronLoadBalancerPoolMember> bulk) {
        bulkRequest = bulk;
        singletonLoadBalancerPoolMember = null;
    }

    NeutronLoadBalancerPoolMemberRequest(NeutronLoadBalancerPoolMember group) {
        singletonLoadBalancerPoolMember = group;
    }

    @Override
    public List<NeutronLoadBalancerPoolMember> getBulk() {
        return bulkRequest;
    }

    @Override
    public NeutronLoadBalancerPoolMember getSingleton() {
        return singletonLoadBalancerPoolMember;
    }

    @Override
    public boolean isSingleton() {
        return (singletonLoadBalancerPoolMember != null);
    }
}
