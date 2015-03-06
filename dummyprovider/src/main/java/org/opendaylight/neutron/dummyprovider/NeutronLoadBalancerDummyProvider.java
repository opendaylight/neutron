/*
 * Copyright (C) 2015 IBM, Inc.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.dummyprovider;

import org.opendaylight.neutron.spi.INeutronLoadBalancerAware;
import org.opendaylight.neutron.spi.NeutronLoadBalancer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NeutronLoadBalancerDummyProvider implements INeutronLoadBalancerAware {

    private static final Logger logger = LoggerFactory.getLogger(NeutronLoadBalancerDummyProvider.class);

    public NeutronLoadBalancerDummyProvider() {
    }

    public int canCreateNeutronLoadBalancer(NeutronLoadBalancer loadBalancer) {
        return(200);
    }

    public void neutronLoadBalancerCreated(NeutronLoadBalancer loadBalancer) {
        logger.info(loadBalancer.toString());
    }

    public int canUpdateNeutronLoadBalancer(NeutronLoadBalancer delta, NeutronLoadBalancer original) {
        return(200);
    }

    public void neutronLoadBalancerUpdated(NeutronLoadBalancer loadBalancer) {
        logger.info(loadBalancer.toString());
    }

    public int canDeleteNeutronLoadBalancer(NeutronLoadBalancer loadBalancer) {
        return(200);
    }

    public void neutronLoadBalancerDeleted(NeutronLoadBalancer loadBalancer) {
        logger.info(loadBalancer.toString());
    }
}
