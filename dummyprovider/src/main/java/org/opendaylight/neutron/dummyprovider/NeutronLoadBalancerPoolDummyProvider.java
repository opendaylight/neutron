/*
 * Copyright (C) 2015 IBM, Inc.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.dummyprovider;

import org.opendaylight.neutron.spi.INeutronLoadBalancerPoolAware;
import org.opendaylight.neutron.spi.NeutronLoadBalancerPool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NeutronLoadBalancerPoolDummyProvider implements INeutronLoadBalancerPoolAware {

    private static final Logger logger = LoggerFactory.getLogger(NeutronLoadBalancerPoolDummyProvider.class);

    public NeutronLoadBalancerPoolDummyProvider() { }

    public int canCreateNeutronLoadBalancerPool(NeutronLoadBalancerPool loadBalancerPool) {
        return(200);
    }

    public void neutronLoadBalancerPoolCreated(NeutronLoadBalancerPool loadBalancerPool) {
        logger.info(loadBalancerPool.toString());
    }

    public int canUpdateNeutronLoadBalancerPool(NeutronLoadBalancerPool delta, NeutronLoadBalancerPool original) {
        return(200);
    }

    public void neutronLoadBalancerPoolUpdated(NeutronLoadBalancerPool loadBalancerPool) {
        logger.info(loadBalancerPool.toString());
    }

    public int canDeleteNeutronLoadBalancerPool(NeutronLoadBalancerPool loadBalancerPool) {
        return(200);
    }

    public void neutronLoadBalancerPoolDeleted(NeutronLoadBalancerPool loadBalancerPool) {
        logger.info(loadBalancerPool.toString());
    }
}
