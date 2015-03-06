/*
 * Copyright IBM Corporation, 2013.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.dummyprovider;

import org.opendaylight.neutron.spi.INeutronFloatingIPAware;
import org.opendaylight.neutron.spi.NeutronFloatingIP;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This interface defines the methods a service that wishes to be aware of Neutron FloatingIPs needs to implement
 *
 */

public class NeutronFloatingIPDummyProvider implements INeutronFloatingIPAware {

    private static final Logger logger = LoggerFactory.getLogger(NeutronFloatingIPDummyProvider.class);

    public NeutronFloatingIPDummyProvider() {
    }

    /**
     * Services provide this interface method to indicate if the specified floatingIP can be created
     *
     * @param floatingIP
     *            instance of proposed new Neutron FloatingIP object
     * @return integer
     *            the return value is understood to be a HTTP status code.  A return value outside of 200 through 299
     *            results in the create operation being interrupted and the returned status value reflected in the
     *            HTTP response.
     */
    public int canCreateFloatingIP(NeutronFloatingIP floatingIP) {
        return(200);
    }

    /**
     * Services provide this interface method for taking action after a floatingIP has been created
     *
     * @param floatingIP
     *            instance of new Neutron FloatingIP object
     * @return void
     */
    public void neutronFloatingIPCreated(NeutronFloatingIP floatingIP) {
        logger.info(floatingIP.toString());
    }

    /**
     * Services provide this interface method to indicate if the specified floatingIP can be changed using the specified
     * delta
     *
     * @param delta
     *            updates to the floatingIP object using patch semantics
     * @param floatingIP
     *            instance of the Neutron FloatingIP object to be updated
     * @return integer
     *            the return value is understood to be a HTTP status code.  A return value outside of 200 through 299
     *            results in the update operation being interrupted and the returned status value reflected in the
     *            HTTP response.
     */
    public int canUpdateFloatingIP(NeutronFloatingIP delta, NeutronFloatingIP original) {
        return(200);
    }

    /**
     * Services provide this interface method for taking action after a floatingIP has been updated
     *
     * @param floatingIP
     *            instance of modified Neutron FloatingIP object
     * @return void
     */
    public void neutronFloatingIPUpdated(NeutronFloatingIP floatingIP) {
        logger.info(floatingIP.toString());
    }

    /**
     * Services provide this interface method to indicate if the specified floatingIP can be deleted
     *
     * @param floatingIP
     *            instance of the Neutron FloatingIP object to be deleted
     * @return integer
     *            the return value is understood to be a HTTP status code.  A return value outside of 200 through 299
     *            results in the delete operation being interrupted and the returned status value reflected in the
     *            HTTP response.
     */
    public int canDeleteFloatingIP(NeutronFloatingIP floatingIP) {
        return(200);
    }

    /**
     * Services provide this interface method for taking action after a floatingIP has been deleted
     *
     * @param floatingIP
     *            instance of deleted Neutron FloatingIP object
     * @return void
     */
    public void neutronFloatingIPDeleted(NeutronFloatingIP floatingIP) {
        logger.info(floatingIP.toString());
    }
}
