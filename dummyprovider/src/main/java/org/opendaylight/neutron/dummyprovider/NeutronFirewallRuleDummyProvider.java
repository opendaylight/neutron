/*
 * Copyright IBM Corporation, 2015.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.dummyprovider;

import org.opendaylight.neutron.spi.INeutronFirewallRuleAware;
import org.opendaylight.neutron.spi.NeutronFirewallRule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NeutronFirewallRuleDummyProvider implements INeutronFirewallRuleAware {

    private static final Logger logger = LoggerFactory.getLogger(NeutronFirewallDummyProvider.class);

    public int canCreateNeutronFirewallRule(NeutronFirewallRule firewallRule) {
        return(200);
    }

    public void neutronFirewallRuleCreated(NeutronFirewallRule firewallRule) {
        logger.info(firewallRule.toString());
    }

    public int canUpdateNeutronFirewallRule(NeutronFirewallRule delta, NeutronFirewallRule original) {
        return(200);
    }

    public void neutronFirewallRuleUpdated(NeutronFirewallRule firewallRule) {
        logger.info(firewallRule.toString());
    }

    public int canDeleteNeutronFirewallRule(NeutronFirewallRule firewallRule) {
        return(200);
    }

    public void neutronFirewallRuleDeleted(NeutronFirewallRule firewallRule) {
        logger.info(firewallRule.toString());
    }
}
