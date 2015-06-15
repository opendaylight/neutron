/*
 * Copyright IBM Corporation, 2015.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.dummyprovider;

import java.net.HttpURLConnection;

import org.opendaylight.neutron.spi.INeutronLoadBalancerPoolMemberAware;
import org.opendaylight.neutron.spi.NeutronLoadBalancerPoolMember;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NeutronLoadBalancerPoolMemberDummyProvider implements INeutronLoadBalancerPoolMemberAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(NeutronLoadBalancerPoolMemberDummyProvider.class);

    public NeutronLoadBalancerPoolMemberDummyProvider() {
    }

    public int canCreateNeutronLoadBalancerPoolMember(NeutronLoadBalancerPoolMember loadBalancerPoolMember) {
        return(HttpURLConnection.HTTP_OK);
    }

    public void neutronLoadBalancerPoolMemberCreated(NeutronLoadBalancerPoolMember loadBalancerPoolMember) {
        LOGGER.info(loadBalancerPoolMember.toString());
    }

    public int canUpdateNeutronLoadBalancerPoolMember(NeutronLoadBalancerPoolMember delta,
            NeutronLoadBalancerPoolMember original) {
        return(HttpURLConnection.HTTP_OK);
    }

    public void neutronLoadBalancerPoolMemberUpdated(NeutronLoadBalancerPoolMember loadBalancerPoolMember) {
        LOGGER.info(loadBalancerPoolMember.toString());
    }

    public int canDeleteNeutronLoadBalancerPoolMember(NeutronLoadBalancerPoolMember loadBalancerPoolMember) {
        return(HttpURLConnection.HTTP_OK);
    }

    public void neutronLoadBalancerPoolMemberDeleted(NeutronLoadBalancerPoolMember loadBalancerPoolMember) {
        LOGGER.info(loadBalancerPoolMember.toString());
    }
}
