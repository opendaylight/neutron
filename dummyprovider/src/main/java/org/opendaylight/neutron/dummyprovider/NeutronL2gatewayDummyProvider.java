/*
 * Copyright (c) 2015 Hewlett-Packard Development Company, L.P. and others.
 * All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.neutron.dummyprovider;

import java.net.HttpURLConnection;
import org.opendaylight.neutron.spi.INeutronL2gatewayAware;
import org.opendaylight.neutron.spi.NeutronL2Gateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NeutronL2gatewayDummyProvider implements INeutronL2gatewayAware{
    private static final Logger LOGGER = LoggerFactory
            .getLogger(NeutronL2gatewayDummyProvider.class);

    @Override
    public int canCreateNeutronL2gateway(NeutronL2Gateway l2Gateway) {
        return(HttpURLConnection.HTTP_OK);
    }

    @Override
    public void neutronL2gatewayCreated(NeutronL2Gateway l2Gateway) {
        LOGGER.info(l2Gateway.toString());
    }
}
