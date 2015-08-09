/*
 * Copyright (c) 2015 IBM Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.dummyprovider;

import java.net.HttpURLConnection;

import org.opendaylight.neutron.spi.INeutronLoadBalancerPoolAware;
import org.opendaylight.neutron.spi.NeutronLoadBalancerPool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NeutronLoadBalancerPoolDummyProvider implements INeutronLoadBalancerPoolAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(NeutronLoadBalancerPoolDummyProvider.class);

    public NeutronLoadBalancerPoolDummyProvider() { }

    public int canCreateNeutronLoadBalancerPool(NeutronLoadBalancerPool loadBalancerPool) {
        return(HttpURLConnection.HTTP_OK);
    }

    public void neutronLoadBalancerPoolCreated(NeutronLoadBalancerPool loadBalancerPool) {
        LOGGER.info(loadBalancerPool.toString());
    }

    public int canUpdateNeutronLoadBalancerPool(NeutronLoadBalancerPool delta, NeutronLoadBalancerPool original) {
        return(HttpURLConnection.HTTP_OK);
    }

    public void neutronLoadBalancerPoolUpdated(NeutronLoadBalancerPool loadBalancerPool) {
        LOGGER.info(loadBalancerPool.toString());
    }

    public int canDeleteNeutronLoadBalancerPool(NeutronLoadBalancerPool loadBalancerPool) {
        return(HttpURLConnection.HTTP_OK);
    }

    public void neutronLoadBalancerPoolDeleted(NeutronLoadBalancerPool loadBalancerPool) {
        LOGGER.info(loadBalancerPool.getLoadBalancerPoolID()+" deleted");
    }
}
