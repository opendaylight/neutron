/*
 * Copyright IBM Corporation, 2015.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.dummyprovider;

import java.net.HttpURLConnection;

import org.opendaylight.neutron.spi.INeutronFirewallPolicyAware;
import org.opendaylight.neutron.spi.NeutronFirewallPolicy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class NeutronFirewallPolicyDummyProvider implements INeutronFirewallPolicyAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(NeutronFirewallPolicyDummyProvider.class);

    public NeutronFirewallPolicyDummyProvider() {
    }

    public int canCreateNeutronFirewallPolicy(NeutronFirewallPolicy firewallPolicy) {
        return(HttpURLConnection.HTTP_OK);
    }

    public void neutronFirewallPolicyCreated(NeutronFirewallPolicy firewallPolicy) {
        LOGGER.info(firewallPolicy.toString());
    }

    public int canUpdateNeutronFirewallPolicy(NeutronFirewallPolicy delta, NeutronFirewallPolicy original) {
        return(HttpURLConnection.HTTP_OK);
    }

    public void neutronFirewallPolicyUpdated(NeutronFirewallPolicy firewallPolicy) {
        LOGGER.info(firewallPolicy.toString());
    }

    public int canDeleteNeutronFirewallPolicy(NeutronFirewallPolicy firewallPolicy) {
        return(HttpURLConnection.HTTP_OK);
    }

    public void neutronFirewallPolicyDeleted(NeutronFirewallPolicy firewallPolicy) {
        LOGGER.info(firewallPolicy.toString());
    }
}
