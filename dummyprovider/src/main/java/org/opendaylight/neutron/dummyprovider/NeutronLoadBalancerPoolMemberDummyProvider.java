/*
 * Copyright IBM Corporation, 2015.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.dummyprovider;

import org.opendaylight.neutron.spi.INeutronLoadBalancerPoolMemberAware;
import org.opendaylight.neutron.spi.NeutronLoadBalancerPoolMember;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NeutronLoadBalancerPoolMemberDummyProvider implements INeutronLoadBalancerPoolMemberAware {

    private static final Logger logger = LoggerFactory.getLogger(NeutronLoadBalancerPoolMemberDummyProvider.class);

    public NeutronLoadBalancerPoolMemberDummyProvider() {
    }

    public int canCreateNeutronLoadBalancerPoolMember(NeutronLoadBalancerPoolMember loadBalancerPoolMember) {
        return(200);
    }

    public void neutronLoadBalancerPoolMemberCreated(NeutronLoadBalancerPoolMember loadBalancerPoolMember) {
        logger.info(loadBalancerPoolMember.toString());
    }

    public int canUpdateNeutronLoadBalancerPoolMember(NeutronLoadBalancerPoolMember delta,
            NeutronLoadBalancerPoolMember original) {
        return(200);
    }

    public void neutronLoadBalancerPoolMemberUpdated(NeutronLoadBalancerPoolMember loadBalancerPoolMember) {
        logger.info(loadBalancerPoolMember.toString());
    }

    public int canDeleteNeutronLoadBalancerPoolMember(NeutronLoadBalancerPoolMember loadBalancerPoolMember) {
        return(200);
    }

    public void neutronLoadBalancerPoolMemberDeleted(NeutronLoadBalancerPoolMember loadBalancerPoolMember) {
        logger.info(loadBalancerPoolMember.toString());
    }
}
