/*
 * Copyright (c) 2015 IBM Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.dummyprovider;

import java.net.HttpURLConnection;

import org.opendaylight.neutron.spi.INeutronSecurityRuleAware;
import org.opendaylight.neutron.spi.NeutronSecurityRule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NeutronSecurityRuleDummyProvider implements INeutronSecurityRuleAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(NeutronSecurityRuleDummyProvider.class);

    public NeutronSecurityRuleDummyProvider() {
    }

    public int canCreateNeutronSecurityRule(NeutronSecurityRule securityRule) {
        return(HttpURLConnection.HTTP_OK);
    }

    public void neutronSecurityRuleCreated(NeutronSecurityRule securityRule) {
        LOGGER.info(securityRule.toString());
    }

    public int canUpdateNeutronSecurityRule(NeutronSecurityRule delta, NeutronSecurityRule original) {
        return(HttpURLConnection.HTTP_OK);
    }

    public void neutronSecurityRuleUpdated(NeutronSecurityRule securityRule) {
        LOGGER.info(securityRule.toString());
    }

    public int canDeleteNeutronSecurityRule(NeutronSecurityRule securityRule) {
        return(HttpURLConnection.HTTP_OK);
    }

    public void neutronSecurityRuleDeleted(NeutronSecurityRule securityRule) {
        LOGGER.info(securityRule.getID()+" deleted");
    }
}
