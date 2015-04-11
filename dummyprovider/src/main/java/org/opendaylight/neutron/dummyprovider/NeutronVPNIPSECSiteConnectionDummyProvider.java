/*
 * Copyright IBM Corporation, 2015.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.dummyprovider;

import org.opendaylight.neutron.spi.INeutronVPNIPSECSiteConnectionAware;
import org.opendaylight.neutron.spi.NeutronVPNIPSECSiteConnection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class NeutronVPNIPSECSiteConnectionDummyProvider implements INeutronVPNIPSECSiteConnectionAware {

    private static final Logger logger = LoggerFactory.getLogger(NeutronVPNIPSECSiteConnectionDummyProvider.class);

    public NeutronVPNIPSECSiteConnectionDummyProvider() {
    }

    public int canCreateNeutronVPNIPSECSiteConnection(NeutronVPNIPSECSiteConnection ikeSiteConnection) {
        return(200);
    }

    public void neutronVPNIPSECSiteConnectionCreated(NeutronVPNIPSECSiteConnection ikeSiteConnection) {
        logger.info(ikeSiteConnection.toString());
    }

    public int canUpdateNeutronVPNIPSECSiteConnection(NeutronVPNIPSECSiteConnection delta, NeutronVPNIPSECSiteConnection original) {
        return(200);
    }

    public void neutronVPNIPSECSiteConnectionUpdated(NeutronVPNIPSECSiteConnection ikeSiteConnection) {
        logger.info(ikeSiteConnection.toString());
    }

    public int canDeleteNeutronVPNIPSECSiteConnection(NeutronVPNIPSECSiteConnection ikeSiteConnection) {
        return(200);
    }

    public void neutronVPNIPSECSiteConnectionDeleted(NeutronVPNIPSECSiteConnection ikeSiteConnection) {
        logger.info(ikeSiteConnection.toString());
    }
}
