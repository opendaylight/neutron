/*
 * Copyright IBM Corporation, 2015.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.dummyprovider;

import org.opendaylight.neutron.spi.INeutronPortAware;
import org.opendaylight.neutron.spi.NeutronPort;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NeutronPortDummyProvider implements INeutronPortAware {

    private static final Logger logger = LoggerFactory.getLogger(NeutronPortDummyProvider.class);

    public NeutronPortDummyProvider() {
    }

    public int canCreatePort(NeutronPort port) {
        return(200);
    }

    public void neutronPortCreated(NeutronPort port) {
        logger.info(port.toString());
    }

    public int canUpdatePort(NeutronPort delta, NeutronPort original) {
        return(200);
    }

    public void neutronPortUpdated(NeutronPort port) {
        logger.info(port.toString());
    }

    public int canDeletePort(NeutronPort port) {
        return(200);
    }

    public void neutronPortDeleted(NeutronPort port) {
        logger.info(port.toString());
    }
}
