/*
 * Copyright (C) 2015 IBM, Inc.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.dummyprovider;

import org.opendaylight.neutron.spi.INeutronLoadBalancerHealthMonitorAware;
import org.opendaylight.neutron.spi.NeutronLoadBalancerHealthMonitor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NeutronLoadBalancerHealthMonitorDummyProvider implements INeutronLoadBalancerHealthMonitorAware {

    private static final Logger logger = LoggerFactory.getLogger(NeutronLoadBalancerHealthMonitorDummyProvider.class);

    public NeutronLoadBalancerHealthMonitorDummyProvider() {
    }

    public int canCreateNeutronLoadBalancerHealthMonitor(NeutronLoadBalancerHealthMonitor loadBalancerHealthMonitor) {
        return(200);
    }

    public void neutronLoadBalancerHealthMonitorCreated(NeutronLoadBalancerHealthMonitor loadBalancerHealthMonitor) {
        logger.info(loadBalancerHealthMonitor.toString());
    }

    public int canUpdateNeutronLoadBalancerHealthMonitor(NeutronLoadBalancerHealthMonitor delta,
            NeutronLoadBalancerHealthMonitor original) {
        return(200);
    }

    public void neutronLoadBalancerHealthMonitorUpdated(NeutronLoadBalancerHealthMonitor loadBalancerHealthMonitor) {
        logger.info(loadBalancerHealthMonitor.toString());
    }

    public int canDeleteNeutronLoadBalancerHealthMonitor(NeutronLoadBalancerHealthMonitor loadBalancerHealthMonitor) {
        return(200);
    }

    public void neutronLoadBalancerHealthMonitorDeleted(NeutronLoadBalancerHealthMonitor loadBalancerHealthMonitor) {
        logger.info(loadBalancerHealthMonitor.toString());
    }

}
