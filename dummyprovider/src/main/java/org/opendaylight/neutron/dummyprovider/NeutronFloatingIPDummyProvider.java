/*
 * Copyright IBM Corporation, 2015.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.dummyprovider;

import java.net.HttpURLConnection;

import org.opendaylight.neutron.spi.INeutronFloatingIPAware;
import org.opendaylight.neutron.spi.NeutronFloatingIP;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NeutronFloatingIPDummyProvider implements INeutronFloatingIPAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(NeutronFloatingIPDummyProvider.class);

    public NeutronFloatingIPDummyProvider() {
    }

    public int canCreateFloatingIP(NeutronFloatingIP floatingIP) {
        return(HttpURLConnection.HTTP_OK);
    }

    public void neutronFloatingIPCreated(NeutronFloatingIP floatingIP) {
        LOGGER.info(floatingIP.toString());
    }

    public int canUpdateFloatingIP(NeutronFloatingIP delta, NeutronFloatingIP original) {
        return(HttpURLConnection.HTTP_OK);
    }

    public void neutronFloatingIPUpdated(NeutronFloatingIP floatingIP) {
        LOGGER.info(floatingIP.toString());
    }

    public int canDeleteFloatingIP(NeutronFloatingIP floatingIP) {
        return(HttpURLConnection.HTTP_OK);
    }

    public void neutronFloatingIPDeleted(NeutronFloatingIP floatingIP) {
        LOGGER.info(floatingIP.toString());
    }
}
