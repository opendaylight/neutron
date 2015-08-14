/*
 * Copyright (c) 2015 IBM Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.dummyprovider;

import java.net.HttpURLConnection;

import org.opendaylight.neutron.spi.INeutronMeteringLabelRuleAware;
import org.opendaylight.neutron.spi.NeutronMeteringLabelRule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class NeutronMeteringLabelRuleDummyProvider implements INeutronMeteringLabelRuleAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(NeutronMeteringLabelRuleDummyProvider.class);

    public NeutronMeteringLabelRuleDummyProvider() {
    }

    public int canCreateMeteringLabelRule(NeutronMeteringLabelRule meteringLabelRule) {
        return(HttpURLConnection.HTTP_OK);
    }

    public void neutronMeteringLabelRuleCreated(NeutronMeteringLabelRule meteringLabelRule) {
        LOGGER.info(meteringLabelRule.toString());
    }

    public int canDeleteMeteringLabelRule(NeutronMeteringLabelRule meteringLabelRule) {
        return(HttpURLConnection.HTTP_OK);
    }

    public void neutronMeteringLabelRuleDeleted(NeutronMeteringLabelRule meteringLabelRule) {
        LOGGER.info(meteringLabelRule.getID()+" deleted");
    }
}
