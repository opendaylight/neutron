/*
 * Copyright (C) 2014 Red Hat, Inc.
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

    /**
     * Services provide this interface method to indicate if the specified loadBalancerPoolMember can be created
     *
     * @param loadBalancerPoolMember
     *            instance of proposed new LoadBalancerPool object
     * @return integer
     *            the return value is understood to be a HTTP status code.  A return value outside of 200 through 299
     *            results in the create operation being interrupted and the returned status value reflected in the
     *            HTTP response.
     */
    public int canCreateNeutronLoadBalancerPoolMember(NeutronLoadBalancerPoolMember loadBalancerPoolMember) {
        return(200);
    }

    /**
     * Services provide this interface method for taking action after a loadBalancerPoolMember has been created
     *
     * @param loadBalancerPoolMember
     *            instance of new LoadBalancerPool object
     * @return void
     */
    public void neutronLoadBalancerPoolMemberCreated(NeutronLoadBalancerPoolMember loadBalancerPoolMember) {
        logger.info(loadBalancerPoolMember.toString());
    }

    /**
     * Services provide this interface method to indicate if the specified loadBalancerPoolMember can be changed using the specified
     * delta
     *
     * @param delta
     *            updates to the loadBalancerPoolMember object using patch semantics
     * @param original
     *            instance of the LoadBalancerPool object to be updated
     * @return integer
     *            the return value is understood to be a HTTP status code.  A return value outside of 200 through 299
     *            results in the update operation being interrupted and the returned status value reflected in the
     *            HTTP response.
     */
    public int canUpdateNeutronLoadBalancerPoolMember(NeutronLoadBalancerPoolMember delta,
            NeutronLoadBalancerPoolMember original) {
        return(200);
    }

    /**
     * Services provide this interface method for taking action after a loadBalancerPoolMember has been updated
     *
     * @param loadBalancerPoolMember
     *            instance of modified LoadBalancerPool object
     * @return void
     */
    public void neutronLoadBalancerPoolMemberUpdated(NeutronLoadBalancerPoolMember loadBalancerPoolMember) {
        logger.info(loadBalancerPoolMember.toString());
    }

    /**
     * Services provide this interface method to indicate if the specified loadBalancerPoolMember can be deleted
     *
     * @param loadBalancerPoolMember
     *            instance of the LoadBalancerPool object to be deleted
     * @return integer
     *            the return value is understood to be a HTTP status code.  A return value outside of 200 through 299
     *            results in the delete operation being interrupted and the returned status value reflected in the
     *            HTTP response.
     */
    public int canDeleteNeutronLoadBalancerPoolMember(NeutronLoadBalancerPoolMember loadBalancerPoolMember) {
        return(200);
    }

    /**
     * Services provide this interface method for taking action after a loadBalancerPoolMember has been deleted
     *
     * @param loadBalancerPoolMember
     *            instance of deleted LoadBalancerPool object
     * @return void
     */
    public void neutronLoadBalancerPoolMemberDeleted(NeutronLoadBalancerPoolMember loadBalancerPoolMember) {
        logger.info(loadBalancerPoolMember.toString());
    }
}
