/*
 * Copyright (c) 2015 IBM Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.dummyprovider;

import java.net.HttpURLConnection;

import org.opendaylight.neutron.spi.INeutronVPNIKEPolicyAware;
import org.opendaylight.neutron.spi.NeutronVPNIKEPolicy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class NeutronVPNIKEPolicyDummyProvider implements INeutronVPNIKEPolicyAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(NeutronVPNIKEPolicyDummyProvider.class);

    public NeutronVPNIKEPolicyDummyProvider() {
    }

    public int canCreateNeutronVPNIKEPolicy(NeutronVPNIKEPolicy ikePolicy) {
        return(HttpURLConnection.HTTP_OK);
    }

    public void neutronVPNIKEPolicyCreated(NeutronVPNIKEPolicy ikePolicy) {
        LOGGER.info(ikePolicy.toString());
    }

    public int canUpdateNeutronVPNIKEPolicy(NeutronVPNIKEPolicy delta, NeutronVPNIKEPolicy original) {
        return(HttpURLConnection.HTTP_OK);
    }

    public void neutronVPNIKEPolicyUpdated(NeutronVPNIKEPolicy ikePolicy) {
        LOGGER.info(ikePolicy.toString());
    }

    public int canDeleteNeutronVPNIKEPolicy(NeutronVPNIKEPolicy ikePolicy) {
        return(HttpURLConnection.HTTP_OK);
    }

    public void neutronVPNIKEPolicyDeleted(NeutronVPNIKEPolicy ikePolicy) {
        LOGGER.info(ikePolicy.getID()+" deleted");
    }
}
