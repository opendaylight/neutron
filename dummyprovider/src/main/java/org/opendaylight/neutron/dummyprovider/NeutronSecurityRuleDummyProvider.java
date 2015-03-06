/*
 * Copyright (C) 2014 Red Hat, Inc.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 */

package org.opendaylight.neutron.dummyprovider;

import org.opendaylight.neutron.spi.INeutronSecurityRuleAware;
import org.opendaylight.neutron.spi.NeutronSecurityRule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This interface defines the methods required to be aware of Neutron Security Rules
 */

public class NeutronSecurityRuleDummyProvider implements INeutronSecurityRuleAware {

    private static final Logger logger = LoggerFactory.getLogger(NeutronSecurityRuleDummyProvider.class);

    public NeutronSecurityRuleDummyProvider() {
    }

    /**
     * Services provide this interface method to indicate if the specified security rule can be created
     *
     * @param securityRule instance of proposed new Neutron Security Rule object
     * @return integer
     * the return value is understood to be a HTTP status code.  A return value outside of 200 through 299
     * results in the create operation being interrupted and the returned status value reflected in the
     * HTTP response.
     */
    public int canCreateNeutronSecurityRule(NeutronSecurityRule securityRule) {
        return(200);
    }

    /**
     * Services provide this interface method for taking action after a security rule has been created
     *
     * @param securityRule instance of new Neutron Security Rule object
     * @return void
     */
    public void neutronSecurityRuleCreated(NeutronSecurityRule securityRule) {
        logger.info(securityRule.toString());
    }

    /**
     * Services provide this interface method to indicate if the specified security rule can be changed using the specified
     * delta
     *
     * @param delta    updates to the security rule object using patch semantics
     * @param original instance of the Neutron Security Rule object to be updated
     * @return integer
     * the return value is understood to be a HTTP status code.  A return value outside of 200 through 299
     * results in the update operation being interrupted and the returned status value reflected in the
     * HTTP response.
     */
    public int canUpdateNeutronSecurityRule(NeutronSecurityRule delta, NeutronSecurityRule original) {
        return(200);
    }

    /**
     * Services provide this interface method for taking action after a security rule has been updated
     *
     * @param securityRule instance of modified Neutron Security Rule object
     * @return void
     */
    public void neutronSecurityRuleUpdated(NeutronSecurityRule securityRule) {
        logger.info(securityRule.toString());
    }

    /**
     * Services provide this interface method to indicate if the specified security rule can be deleted
     *
     * @param securityRule instance of the Neutron Security Rule object to be deleted
     * @return integer
     * the return value is understood to be a HTTP status code.  A return value outside of 200 through 299
     * results in the delete operation being interrupted and the returned status value reflected in the
     * HTTP response.
     */
    public int canDeleteNeutronSecurityRule(NeutronSecurityRule securityRule) {
        return(200);
    }

    /**
     * Services provide this interface method for taking action after a security rule has been deleted
     *
     * @param securityRule instance of deleted Neutron Security Rule object
     * @return void
     */
    public void neutronSecurityRuleDeleted(NeutronSecurityRule securityRule) {
        logger.info(securityRule.toString());
    }
}
