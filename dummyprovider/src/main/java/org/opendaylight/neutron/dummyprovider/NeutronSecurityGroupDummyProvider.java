/*
 * Copyright IBM Corporation, 2015.  All rights reserved.
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

public class NeutronSecurityGroupDummyProvider implements INeutronSecurityGroupAware {

    private static final Logger logger = LoggerFactory.getLogger(NeutronSecurityGroupDummyProvider.class);

    public NeutronSecurityGroupDummyProvider() { }

    public int canCreateNeutronSecurityGroup(NeutronSecurityGroup securityGroup) {
        return(200);
    }

    public void neutronSecurityGroupCreated(NeutronSecurityGroup securityGroup) {
        logger.info(securityGroup.toString());
    }

    public int canUpdateNeutronSecurityGroup(NeutronSecurityGroup delta, NeutronSecurityGroup original) {
        return(200);
    }

    public void neutronSecurityGroupUpdated(NeutronSecurityGroup securityGroup) {
        logger.info(securityGroup.toString());
    }

    public int canDeleteNeutronSecurityGroup(NeutronSecurityGroup securityGroup) {
        return(200);
    }

    public void neutronSecurityGroupDeleted(NeutronSecurityGroup securityGroup) {
        logger.info(securityGroup.toString());
    }
}
