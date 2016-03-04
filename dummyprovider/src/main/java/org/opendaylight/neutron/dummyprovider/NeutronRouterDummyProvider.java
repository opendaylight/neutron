/*
 * Copyright (c) 2015 IBM Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.dummyprovider;

import java.net.HttpURLConnection;

import org.opendaylight.neutron.spi.INeutronRouterAware;
import org.opendaylight.neutron.spi.NeutronRouter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NeutronRouterDummyProvider implements INeutronRouterAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(NeutronRouterDummyProvider.class);

    public NeutronRouterDummyProvider() {
    }

    public int canCreateRouter(NeutronRouter router) {
        return(HttpURLConnection.HTTP_OK);
    }

    public void neutronRouterCreated(NeutronRouter router) {
        LOGGER.info(router.toString());
    }

    public int canUpdateRouter(NeutronRouter delta, NeutronRouter original) {
        return(HttpURLConnection.HTTP_OK);
    }

    public void neutronRouterUpdated(NeutronRouter router) {
        LOGGER.info(router.toString());
    }

    public int canDeleteRouter(NeutronRouter router) {
        return(HttpURLConnection.HTTP_OK);
    }

    public void neutronRouterDeleted(NeutronRouter router) {
        LOGGER.info(router.getID()+" deleted");
    }
}
