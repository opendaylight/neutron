/*
 * Copyright IBM Corporation, 2013.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.spi;

import org.opendaylight.neutron.spi.INeutronPortAware;
import org.opendaylight.neutron.spi.NeutronPort;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This interface defines the methods a service that wishes to be aware of Neutron Ports needs to implement
 *
 */

public class NeutronPortDummyProvider implements INeutronPortAware {

    private static final Logger logger = LoggerFactory.getLogger(NeutronPortDummyProvider.class);

    public NeutronPortDummyProvider() {
    }

    /**
     * Services provide this interface method to indicate if the specified port can be created
     *
     * @param port
     *            instance of proposed new Neutron Port object
     * @return integer
     *            the return value is understood to be a HTTP status code.  A return value outside of 200 through 299
     *            results in the create operation being interrupted and the returned status value reflected in the
     *            HTTP response.
     */
    public int canCreatePort(NeutronPort port) {
        return(200);
    }

    /**
     * Services provide this interface method for taking action after a port has been created
     *
     * @param port
     *            instance of new Neutron Port object
     * @return void
     */
    public void neutronPortCreated(NeutronPort port) {
        logger.info(port.toString());
    }

    /**
     * Services provide this interface method to indicate if the specified port can be changed using the specified
     * delta
     *
     * @param delta
     *            updates to the port object using patch semantics
     * @param port
     *            instance of the Neutron Port object to be updated
     * @return integer
     *            the return value is understood to be a HTTP status code.  A return value outside of 200 through 299
     *            results in the update operation being interrupted and the returned status value reflected in the
     *            HTTP response.
     */
    public int canUpdatePort(NeutronPort delta, NeutronPort original) {
        return(200);
    }

    /**
     * Services provide this interface method for taking action after a port has been updated
     *
     * @param port
     *            instance of modified Neutron Port object
     * @return void
     */
    public void neutronPortUpdated(NeutronPort port) {
        logger.info(port.toString());
    }

    /**
     * Services provide this interface method to indicate if the specified port can be deleted
     *
     * @param port
     *            instance of the Neutron Port object to be deleted
     * @return integer
     *            the return value is understood to be a HTTP status code.  A return value outside of 200 through 299
     *            results in the delete operation being interrupted and the returned status value reflected in the
     *            HTTP response.
     */
    public int canDeletePort(NeutronPort port) {
        return(200);
    }

    /**
     * Services provide this interface method for taking action after a port has been deleted
     *
     * @param port
     *            instance of deleted Port Network object
     * @return void
     */
    public void neutronPortDeleted(NeutronPort port) {
        logger.info(port.toString());
    }
}
