/*
 * Copyright IBM Corporation, 2015.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.dummyprovider;

import org.opendaylight.neutron.spi.INeutronMeteringLabelRuleAware;
import org.opendaylight.neutron.spi.NeutronMeteringLabelRule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class NeutronMeteringLabelRuleDummyProvider implements INeutronMeteringLabelRuleAware {

    private static final Logger logger = LoggerFactory.getLogger(NeutronMeteringLabelRuleDummyProvider.class);

    public NeutronMeteringLabelRuleDummyProvider() {
    }

    public int canCreateMeteringLabelRule(NeutronMeteringLabelRule meteringLabelRule) {
        return(200);
    }

    public void neutronMeteringLabelRuleCreated(NeutronMeteringLabelRule meteringLabelRule) {
        logger.info(meteringLabelRule.toString());
    }

    public int canDeleteMeteringLabelRule(NeutronMeteringLabelRule meteringLabelRule) {
        return(200);
    }

    public void neutronMeteringLabelRuleDeleted(NeutronMeteringLabelRule meteringLabelRule) {
        logger.info(meteringLabelRule.toString());
    }
}
