/*
 * Copyright (c) 2014, 2015 Red Hat, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.northbound.api;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.opendaylight.neutron.spi.NeutronLoadBalancerListener;


@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)

public class NeutronLoadBalancerListenerRequest
    implements INeutronRequest<NeutronLoadBalancerListener> {
    /**
     * See OpenStack Network API v2.0 Reference for description of
     * http://docs.openstack.org/api/openstack-network/2.0/content/
     */

    @XmlElement(name="listener")
    NeutronLoadBalancerListener singletonLoadBalancerListener;

    @XmlElement(name="listeners")
    List<NeutronLoadBalancerListener> bulkRequest;

    NeutronLoadBalancerListenerRequest() {
    }

    NeutronLoadBalancerListenerRequest(List<NeutronLoadBalancerListener> bulk) {
        bulkRequest = bulk;
        singletonLoadBalancerListener = null;
    }

    NeutronLoadBalancerListenerRequest(NeutronLoadBalancerListener group) {
        singletonLoadBalancerListener = group;
    }

    @Override
    public List<NeutronLoadBalancerListener> getBulk() {
        return bulkRequest;
    }

    @Override
    public NeutronLoadBalancerListener getSingleton() {
        return singletonLoadBalancerListener;
    }

    @Override
    public boolean isSingleton() {
        return (singletonLoadBalancerListener != null);
    }
}
