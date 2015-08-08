/*
 * Copyright IBM Corporation, 2015.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.dummyprovider;

import java.net.HttpURLConnection;

import org.opendaylight.neutron.spi.INeutronLoadBalancerAware;
import org.opendaylight.neutron.spi.NeutronLoadBalancer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NeutronLoadBalancerDummyProvider implements INeutronLoadBalancerAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(NeutronLoadBalancerDummyProvider.class);

    public NeutronLoadBalancerDummyProvider() {
    }

    public int canCreateNeutronLoadBalancer(NeutronLoadBalancer loadBalancer) {
        return(HttpURLConnection.HTTP_OK);
    }

    public void neutronLoadBalancerCreated(NeutronLoadBalancer loadBalancer) {
        LOGGER.info(loadBalancer.toString());
    }

    public int canUpdateNeutronLoadBalancer(NeutronLoadBalancer delta, NeutronLoadBalancer original) {
        return(HttpURLConnection.HTTP_OK);
    }

    public void neutronLoadBalancerUpdated(NeutronLoadBalancer loadBalancer) {
        LOGGER.info(loadBalancer.toString());
    }

    public int canDeleteNeutronLoadBalancer(NeutronLoadBalancer loadBalancer) {
        return(HttpURLConnection.HTTP_OK);
    }

    public void neutronLoadBalancerDeleted(NeutronLoadBalancer loadBalancer) {
        LOGGER.info(loadBalancer.getID()+" deleted");
    }
}
