/*
 * Copyright (c) 2015 IBM Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.dummyprovider;

import java.net.HttpURLConnection;

import org.opendaylight.neutron.spi.INeutronSubnetAware;
import org.opendaylight.neutron.spi.NeutronSubnet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class NeutronSubnetDummyProvider implements INeutronSubnetAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(NeutronSubnetDummyProvider.class);

    public NeutronSubnetDummyProvider() {
    }

    public int canCreateSubnet(NeutronSubnet subnet) {
        return(HttpURLConnection.HTTP_OK);
    }

    public void neutronSubnetCreated(NeutronSubnet subnet) {
        LOGGER.info(subnet.toString());
    }

    public int canUpdateSubnet(NeutronSubnet delta, NeutronSubnet original) {
        return(HttpURLConnection.HTTP_OK);
    }

    public void neutronSubnetUpdated(NeutronSubnet subnet) {
        LOGGER.info(subnet.toString());
    }

    public int canDeleteSubnet(NeutronSubnet subnet) {
        return(HttpURLConnection.HTTP_OK);
    }

    public void neutronSubnetDeleted(NeutronSubnet subnet) {
        LOGGER.info(subnet.getID()+" deleted");
    }

}
