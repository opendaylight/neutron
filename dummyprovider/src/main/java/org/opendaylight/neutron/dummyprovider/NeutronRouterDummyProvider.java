/*
 * Copyright IBM Corporation, 2015.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.dummyprovider;

import org.opendaylight.neutron.spi.INeutronRouterAware;
import org.opendaylight.neutron.spi.NeutronRouter;
import org.opendaylight.neutron.spi.NeutronRouter_Interface;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NeutronRouterDummyProvider implements INeutronRouterAware {

    private static final Logger logger = LoggerFactory.getLogger(NeutronRouterDummyProvider.class);

    public NeutronRouterDummyProvider() {
    }

    public int canCreateRouter(NeutronRouter router) {
        return(200);
    }

    public void neutronRouterCreated(NeutronRouter router) {
        logger.info(router.toString());
    }

    public int canUpdateRouter(NeutronRouter delta, NeutronRouter original) {
        return(200);
    }

    public void neutronRouterUpdated(NeutronRouter router) {
        logger.info(router.toString());
    }

    public int canDeleteRouter(NeutronRouter router) {
        return(200);
    }

    public void neutronRouterDeleted(NeutronRouter router) {
        logger.info(router.toString());
    }

    public int canAttachInterface(NeutronRouter router, NeutronRouter_Interface routerInterface) {
        return(200);
    }

    public void neutronRouterInterfaceAttached(NeutronRouter router, NeutronRouter_Interface routerInterface) {
        logger.info(router.toString());
        logger.info(routerInterface.toString());
    }

    public int canDetachInterface(NeutronRouter router, NeutronRouter_Interface routerInterface) {
        return(200);
    }

    public void neutronRouterInterfaceDetached(NeutronRouter router, NeutronRouter_Interface routerInterface) {
        logger.info(router.toString());
        logger.info(routerInterface.toString());
    }
}
