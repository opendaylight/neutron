/*
 * Copyright (c) 2015 IBM Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.dummyprovider;

import java.net.HttpURLConnection;

import org.opendaylight.neutron.spi.INeutronSecurityGroupAware;
import org.opendaylight.neutron.spi.NeutronSecurityGroup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NeutronSecurityGroupDummyProvider implements INeutronSecurityGroupAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(NeutronSecurityGroupDummyProvider.class);

    public NeutronSecurityGroupDummyProvider() { }

    public int canCreateNeutronSecurityGroup(NeutronSecurityGroup securityGroup) {
        return(HttpURLConnection.HTTP_OK);
    }

    public void neutronSecurityGroupCreated(NeutronSecurityGroup securityGroup) {
        LOGGER.info(securityGroup.toString());
    }

    public int canUpdateNeutronSecurityGroup(NeutronSecurityGroup delta, NeutronSecurityGroup original) {
        return(HttpURLConnection.HTTP_OK);
    }

    public void neutronSecurityGroupUpdated(NeutronSecurityGroup securityGroup) {
        LOGGER.info(securityGroup.toString());
    }

    public int canDeleteNeutronSecurityGroup(NeutronSecurityGroup securityGroup) {
        return(HttpURLConnection.HTTP_OK);
    }

    public void neutronSecurityGroupDeleted(NeutronSecurityGroup securityGroup) {
        LOGGER.info(securityGroup.getID()+" deleted");
    }
}
