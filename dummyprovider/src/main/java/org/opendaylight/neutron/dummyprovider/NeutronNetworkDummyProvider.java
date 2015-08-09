/*
 * Copyright (c) 2015 IBM Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.dummyprovider;

import java.net.HttpURLConnection;

import org.opendaylight.neutron.spi.INeutronNetworkAware;
import org.opendaylight.neutron.spi.NeutronNetwork;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class NeutronNetworkDummyProvider implements INeutronNetworkAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(NeutronNetworkDummyProvider.class);

    public NeutronNetworkDummyProvider() {
    }

    public int canCreateNetwork(NeutronNetwork network) {
        return(HttpURLConnection.HTTP_OK);
    }

    public void neutronNetworkCreated(NeutronNetwork network) {
        LOGGER.info(network.toString());
    }

    public int canUpdateNetwork(NeutronNetwork delta, NeutronNetwork original) {
        return(HttpURLConnection.HTTP_OK);
    }

    public void neutronNetworkUpdated(NeutronNetwork network) {
        LOGGER.info(network.toString());
    }

    public int canDeleteNetwork(NeutronNetwork network) {
        return(HttpURLConnection.HTTP_OK);
    }

    public void neutronNetworkDeleted(NeutronNetwork network) {
        LOGGER.info(network.getID()+" deleted");
    }
}
