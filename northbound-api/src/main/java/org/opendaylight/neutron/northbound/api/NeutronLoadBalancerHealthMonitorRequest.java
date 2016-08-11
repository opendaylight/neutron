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
import org.opendaylight.neutron.spi.NeutronLoadBalancerHealthMonitor;


@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)

public class NeutronLoadBalancerHealthMonitorRequest
    implements INeutronRequest<NeutronLoadBalancerHealthMonitor> {
    /**
     * See OpenStack Network API v2.0 Reference for description of
     * http://docs.openstack.org/api/openstack-network/2.0/content/
     */

    @XmlElement(name="healthmonitor")
    NeutronLoadBalancerHealthMonitor singletonLoadBalancerHealthMonitor;

    @XmlElement(name="healthmonitors")
    List<NeutronLoadBalancerHealthMonitor> bulkRequest;

    NeutronLoadBalancerHealthMonitorRequest() {
    }

    NeutronLoadBalancerHealthMonitorRequest(List<NeutronLoadBalancerHealthMonitor> bulk) {
        bulkRequest = bulk;
        singletonLoadBalancerHealthMonitor = null;
    }

    NeutronLoadBalancerHealthMonitorRequest(NeutronLoadBalancerHealthMonitor group) {
        singletonLoadBalancerHealthMonitor = group;
    }

    @Override
    public List<NeutronLoadBalancerHealthMonitor> getBulk() {
        return bulkRequest;
    }

    @Override
    public NeutronLoadBalancerHealthMonitor getSingleton() {
        return singletonLoadBalancerHealthMonitor;
    }

    @Override
    public boolean isSingleton() {
        return (singletonLoadBalancerHealthMonitor != null);
    }
}
