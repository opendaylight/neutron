/*
 * Copyright IBM Corporation, 2015.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.dummyprovider;

import org.opendaylight.neutron.spi.INeutronLoadBalancerListenerAware;
import org.opendaylight.neutron.spi.NeutronLoadBalancerListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NeutronLoadBalancerListenerDummyProvider implements INeutronLoadBalancerListenerAware {

    private static final Logger logger = LoggerFactory.getLogger(NeutronLoadBalancerListenerDummyProvider.class);

    public NeutronLoadBalancerListenerDummyProvider() {
    }

    public int canCreateNeutronLoadBalancerListener(NeutronLoadBalancerListener loadBalancerListener) {
        return(200);
    }

    public void neutronLoadBalancerListenerCreated(NeutronLoadBalancerListener loadBalancerListener) {
        logger.info(loadBalancerListener.toString());
    }

    public int canUpdateNeutronLoadBalancerListener(NeutronLoadBalancerListener delta,
            NeutronLoadBalancerListener original) {
        return(200);
    }

    public void neutronLoadBalancerListenerUpdated(NeutronLoadBalancerListener loadBalancerListener) {
        logger.info(loadBalancerListener.toString());
    }

    public int canDeleteNeutronLoadBalancerListener(NeutronLoadBalancerListener loadBalancerListener) {
        return(200);
    }

    public void neutronLoadBalancerListenerDeleted(NeutronLoadBalancerListener loadBalancerListener) {
        logger.info(loadBalancerListener.toString());
    }
}
