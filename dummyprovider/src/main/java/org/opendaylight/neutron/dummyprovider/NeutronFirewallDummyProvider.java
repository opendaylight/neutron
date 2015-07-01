/*
 * Copyright IBM Corporation, 2015.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.dummyprovider;

import java.net.HttpURLConnection;

import org.opendaylight.neutron.spi.INeutronFirewallAware;
import org.opendaylight.neutron.spi.NeutronFirewall;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NeutronFirewallDummyProvider implements INeutronFirewallAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(NeutronFirewallDummyProvider.class);

    public NeutronFirewallDummyProvider() {
    }

    public int canCreateNeutronFirewall(NeutronFirewall firewall) {
        return(HttpURLConnection.HTTP_OK);
    }

    public void neutronFirewallCreated(NeutronFirewall firewall) {
        LOGGER.info(firewall.toString());
    }

    public int canUpdateNeutronFirewall(NeutronFirewall delta, NeutronFirewall original) {
        return(HttpURLConnection.HTTP_OK);
    }

    public void neutronFirewallUpdated(NeutronFirewall firewall) {
        LOGGER.info(firewall.toString());
    }

    public int canDeleteNeutronFirewall(NeutronFirewall firewall) {
        return(HttpURLConnection.HTTP_OK);
    }
  
    public void neutronFirewallDeleted(NeutronFirewall firewall) {
        LOGGER.info(firewall.getFirewallUUID()+" deleted");
    }
}
