/*
 * Copyright IBM Corporation, 2015.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.dummyprovider;

import java.net.HttpURLConnection;

import org.opendaylight.neutron.spi.INeutronLoadBalancerHealthMonitorAware;
import org.opendaylight.neutron.spi.NeutronLoadBalancerHealthMonitor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NeutronLoadBalancerHealthMonitorDummyProvider implements INeutronLoadBalancerHealthMonitorAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(NeutronLoadBalancerHealthMonitorDummyProvider.class);

    public NeutronLoadBalancerHealthMonitorDummyProvider() {
    }

    public int canCreateNeutronLoadBalancerHealthMonitor(NeutronLoadBalancerHealthMonitor loadBalancerHealthMonitor) {
        return(HttpURLConnection.HTTP_OK);
    }

    public void neutronLoadBalancerHealthMonitorCreated(NeutronLoadBalancerHealthMonitor loadBalancerHealthMonitor) {
        LOGGER.info(loadBalancerHealthMonitor.toString());
    }

    public int canUpdateNeutronLoadBalancerHealthMonitor(NeutronLoadBalancerHealthMonitor delta,
            NeutronLoadBalancerHealthMonitor original) {
        return(HttpURLConnection.HTTP_OK);
    }

    public void neutronLoadBalancerHealthMonitorUpdated(NeutronLoadBalancerHealthMonitor loadBalancerHealthMonitor) {
        LOGGER.info(loadBalancerHealthMonitor.toString());
    }

    public int canDeleteNeutronLoadBalancerHealthMonitor(NeutronLoadBalancerHealthMonitor loadBalancerHealthMonitor) {
        return(HttpURLConnection.HTTP_OK);
    }

    public void neutronLoadBalancerHealthMonitorDeleted(NeutronLoadBalancerHealthMonitor loadBalancerHealthMonitor) {
        LOGGER.info(loadBalancerHealthMonitor.getLoadBalancerHealthMonitorID()+" deleted");
    }
}
