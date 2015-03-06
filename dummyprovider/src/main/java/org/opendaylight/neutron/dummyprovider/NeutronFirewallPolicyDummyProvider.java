/*
 * Copyright (C) 2015 IBM, Inc.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.spi;

import org.opendaylight.neutron.spi.INeutronFirewallPolicyAware;
import org.opendaylight.neutron.spi.NeutronFirewallPolicy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class NeutronFirewallPolicyDummyProvider implements INeutronFirewallPolicyAware {

    private static final Logger logger = LoggerFactory.getLogger(NeutronFirewallPolicyDummyProvider.class);

    public NeutronFirewallPolicyDummyProvider() {
    }

    public int canCreateNeutronFirewallPolicy(NeutronFirewallPolicy firewallPolicy) {
        return(200);
    }

    public void neutronFirewallPolicyCreated(NeutronFirewallPolicy firewallPolicy) {
        logger.info(firewallPolicy.toString());
    }

    public int canUpdateNeutronFirewallPolicy(NeutronFirewallPolicy delta, NeutronFirewallPolicy original) {
        return(200);
    }

    public void neutronFirewallPolicyUpdated(NeutronFirewallPolicy firewallPolicy) {
        logger.info(firewallPolicy.toString());
    }

    public int canDeleteNeutronFirewallPolicy(NeutronFirewallPolicy firewallPolicy) {
        return(200);
    }

    public void neutronFirewallPolicyDeleted(NeutronFirewallPolicy firewallPolicy) {
        logger.info(firewallPolicy.toString());
    }
}
