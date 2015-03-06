/*
 * Copyright (C) 2015 IBM, Inc.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.dummyprovider;

import org.opendaylight.neutron.spi.INeutronFirewallAware;
import org.opendaylight.neutron.spi.NeutronFirewall;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NeutronFirewallDummyProvider implements INeutronFirewallAware {

    private static final Logger logger = LoggerFactory.getLogger(NeutronFirewallDummyProvider.class);

    public NeutronFirewallDummyProvider() {
    }

    public int canCreateNeutronFirewall(NeutronFirewall firewall) {
        return(200);
    }

    public void neutronFirewallCreated(NeutronFirewall firewall) {
        logger.info(firewall.toString());
    }

    public int canUpdateNeutronFirewall(NeutronFirewall delta, NeutronFirewall original) {
        return(200);
    }

    public void neutronFirewallUpdated(NeutronFirewall firewall) {
        logger.info(firewall.toString());
    }

    public int canDeleteNeutronFirewall(NeutronFirewall firewall) {
        return(200);
    }
  
    public void neutronFirewallDeleted(NeutronFirewall firewall) {
        logger.info(firewall.toString());
    }
}
