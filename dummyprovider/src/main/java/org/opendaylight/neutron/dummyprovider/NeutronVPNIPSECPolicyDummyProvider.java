/*
 * Copyright IBM Corporation, 2015.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.dummyprovider;

import java.net.HttpURLConnection;

import org.opendaylight.neutron.spi.INeutronVPNIPSECPolicyAware;
import org.opendaylight.neutron.spi.NeutronVPNIPSECPolicy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class NeutronVPNIPSECPolicyDummyProvider implements INeutronVPNIPSECPolicyAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(NeutronVPNIPSECPolicyDummyProvider.class);

    public NeutronVPNIPSECPolicyDummyProvider() {
    }

    public int canCreateNeutronVPNIPSECPolicy(NeutronVPNIPSECPolicy ipsecPolicy) {
        return(HttpURLConnection.HTTP_OK);
    }

    public void neutronVPNIPSECPolicyCreated(NeutronVPNIPSECPolicy ipsecPolicy) {
        LOGGER.info(ipsecPolicy.toString());
    }

    public int canUpdateNeutronVPNIPSECPolicy(NeutronVPNIPSECPolicy delta, NeutronVPNIPSECPolicy original) {
        return(HttpURLConnection.HTTP_OK);
    }

    public void neutronVPNIPSECPolicyUpdated(NeutronVPNIPSECPolicy ipsecPolicy) {
        LOGGER.info(ipsecPolicy.toString());
    }

    public int canDeleteNeutronVPNIPSECPolicy(NeutronVPNIPSECPolicy ipsecPolicy) {
        return(HttpURLConnection.HTTP_OK);
    }

    public void neutronVPNIPSECPolicyDeleted(NeutronVPNIPSECPolicy ipsecPolicy) {
        LOGGER.info(ipsecPolicy.toString());
    }
}
