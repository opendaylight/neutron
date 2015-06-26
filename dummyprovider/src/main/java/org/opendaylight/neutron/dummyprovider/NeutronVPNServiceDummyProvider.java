/*
 * Copyright IBM Corporation, 2015.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.dummyprovider;

import java.net.HttpURLConnection;

import org.opendaylight.neutron.spi.INeutronVPNServiceAware;
import org.opendaylight.neutron.spi.NeutronVPNService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class NeutronVPNServiceDummyProvider implements INeutronVPNServiceAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(NeutronVPNServiceDummyProvider.class);

    public NeutronVPNServiceDummyProvider() {
    }

    public int canCreateNeutronVPNService(NeutronVPNService vpnService) {
        return(HttpURLConnection.HTTP_OK);
    }

    public void neutronVPNServiceCreated(NeutronVPNService vpnService) {
        LOGGER.info(vpnService.toString());
    }

    public int canUpdateNeutronVPNService(NeutronVPNService delta, NeutronVPNService original) {
        return(HttpURLConnection.HTTP_OK);
    }

    public void neutronVPNServiceUpdated(NeutronVPNService vpnService) {
        LOGGER.info(vpnService.toString());
    }

    public int canDeleteNeutronVPNService(NeutronVPNService vpnService) {
        return(HttpURLConnection.HTTP_OK);
    }

    public void neutronVPNServiceDeleted(NeutronVPNService vpnService) {
        LOGGER.info(vpnService.toString());
    }
}
