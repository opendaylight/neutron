/*
 * Copyright IBM Corporation, 2015.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.dummyprovider;

import java.net.HttpURLConnection;

import org.opendaylight.neutron.spi.INeutronFirewallRuleAware;
import org.opendaylight.neutron.spi.NeutronFirewallRule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NeutronFirewallRuleDummyProvider implements INeutronFirewallRuleAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(NeutronFirewallDummyProvider.class);

    public int canCreateNeutronFirewallRule(NeutronFirewallRule firewallRule) {
        return(HttpURLConnection.HTTP_OK);
    }

    public void neutronFirewallRuleCreated(NeutronFirewallRule firewallRule) {
        LOGGER.info(firewallRule.toString());
    }

    public int canUpdateNeutronFirewallRule(NeutronFirewallRule delta, NeutronFirewallRule original) {
        return(HttpURLConnection.HTTP_OK);
    }

    public void neutronFirewallRuleUpdated(NeutronFirewallRule firewallRule) {
        LOGGER.info(firewallRule.toString());
    }

    public int canDeleteNeutronFirewallRule(NeutronFirewallRule firewallRule) {
        return(HttpURLConnection.HTTP_OK);
    }

    public void neutronFirewallRuleDeleted(NeutronFirewallRule firewallRule) {
        LOGGER.info(firewallRule.toString());
    }
}
