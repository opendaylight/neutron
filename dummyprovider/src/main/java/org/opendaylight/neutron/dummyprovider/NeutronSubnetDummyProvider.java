/*
 * Copyright IBM Corporation, 2015.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.dummyprovider;

import org.opendaylight.neutron.spi.INeutronSubnetAware;
import org.opendaylight.neutron.spi.NeutronSubnet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class NeutronSubnetDummyProvider implements INeutronSubnetAware {

    private static final Logger logger = LoggerFactory.getLogger(NeutronSubnetDummyProvider.class);

    public NeutronSubnetDummyProvider() {
    }

    public int canCreateSubnet(NeutronSubnet subnet) {
        return(200);
    }

    public void neutronSubnetCreated(NeutronSubnet subnet) {
        logger.info(subnet.toString());
    }

    public int canUpdateSubnet(NeutronSubnet delta, NeutronSubnet original) {
        return(200);
    }

    public void neutronSubnetUpdated(NeutronSubnet subnet) {
        logger.info(subnet.toString());
    }

    public int canDeleteSubnet(NeutronSubnet subnet) {
        return(200);
    }

    public void neutronSubnetDeleted(NeutronSubnet subnet) {
        logger.info(subnet.toString());
    }

}
