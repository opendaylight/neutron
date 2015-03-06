/*
 * Copyright (C) 2014 Red Hat, Inc.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 */

package org.opendaylight.neutron.dummyprovider;

import org.opendaylight.neutron.spi.INeutronSecurityGroupAware;
import org.opendaylight.neutron.spi.NeutronSecurityGroup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This interface defines the methods a service that wishes to be aware of Neutron Security Groups needs to implement
 */

public class NeutronSecurityGroupDummyProvider implements INeutronSecurityGroupAware {

    private static final Logger logger = LoggerFactory.getLogger(NeutronSecurityGroupDummyProvider.class);

    public NeutronSecurityGroupDummyProvider() { }

    /**
     * Services provide this interface method to indicate if the specified security group can be created
     *
     * @param securityGroup instance of proposed new Neutron Security Group object
     * @return integer
     * the return value is understood to be a HTTP status code.  A return value outside of 200 through 299
     * results in the create operation being interrupted and the returned status value reflected in the
     * HTTP response.
     */
    public int canCreateNeutronSecurityGroup(NeutronSecurityGroup securityGroup) {
        return(200);
    }

    /**
     * Services provide this interface method for taking action after a security group has been created
     *
     * @param securityGroup instance of new Neutron Security Group object
     * @return void
     */
    public void neutronSecurityGroupCreated(NeutronSecurityGroup securityGroup) {
        logger.info(securityGroup.toString());
    }

    /**
     * Services provide this interface method to indicate if the specified security group can be changed using the specified
     * delta
     *
     * @param delta    updates to the security group object using patch semantics
     * @param original instance of the Neutron Security Group object to be updated
     * @return integer
     * the return value is understood to be a HTTP status code.  A return value outside of 200 through 299
     * results in the update operation being interrupted and the returned status value reflected in the
     * HTTP response.
     */
    public int canUpdateNeutronSecurityGroup(NeutronSecurityGroup delta, NeutronSecurityGroup original) {
        return(200);
    }

    /**
     * Services provide this interface method for taking action after a security group has been updated
     *
     * @param securityGroup instance of modified Neutron Security Group object
     * @return void
     */
    public void neutronSecurityGroupUpdated(NeutronSecurityGroup securityGroup) {
        logger.info(securityGroup.toString());
    }

    /**
     * Services provide this interface method to indicate if the specified security group can be deleted
     *
     * @param securityGroup instance of the Neutron Security Group object to be deleted
     * @return integer
     * the return value is understood to be a HTTP status code.  A return value outside of 200 through 299
     * results in the delete operation being interrupted and the returned status value reflected in the
     * HTTP response.
     */
    public int canDeleteNeutronSecurityGroup(NeutronSecurityGroup securityGroup) {
        return(200);
    }

    /**
     * Services provide this interface method for taking action after a security group has been deleted
     *
     * @param securityGroup instance of deleted Neutron Security Group object
     * @return void
     */
    public void neutronSecurityGroupDeleted(NeutronSecurityGroup securityGroup) {
        logger.info(securityGroup.toString());
    }
}
