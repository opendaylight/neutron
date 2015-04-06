/*
 * Copyright IBM Corporation, 2015.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.dummyprovider;

import org.opendaylight.neutron.spi.INeutronVPNIKEPolicyAware;
import org.opendaylight.neutron.spi.NeutronVPNIKEPolicy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class NeutronVPNIKEPolicyDummyProvider implements INeutronVPNIKEPolicyAware {

    private static final Logger logger = LoggerFactory.getLogger(NeutronVPNIKEPolicyDummyProvider.class);

    public NeutronVPNIKEPolicyDummyProvider() {
    }

    public int canCreateNeutronVPNIKEPolicy(NeutronVPNIKEPolicy ikePolicy) {
        return(200);
    }

    public void neutronVPNIKEPolicyCreated(NeutronVPNIKEPolicy ikePolicy) {
        logger.info(ikePolicy.toString());
    }

    public int canUpdateNeutronVPNIKEPolicy(NeutronVPNIKEPolicy delta, NeutronVPNIKEPolicy original) {
        return(200);
    }

    public void neutronVPNIKEPolicyUpdated(NeutronVPNIKEPolicy ikePolicy) {
        logger.info(ikePolicy.toString());
    }

    public int canDeleteNeutronVPNIKEPolicy(NeutronVPNIKEPolicy ikePolicy) {
        return(200);
    }

    public void neutronVPNIKEPolicyDeleted(NeutronVPNIKEPolicy ikePolicy) {
        logger.info(ikePolicy.toString());
    }
}
