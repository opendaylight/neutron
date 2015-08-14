/*
 * Copyright (c) 2015 IBM Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.dummyprovider;

import java.net.HttpURLConnection;

import org.opendaylight.neutron.spi.INeutronMeteringLabelAware;
import org.opendaylight.neutron.spi.NeutronMeteringLabel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class NeutronMeteringLabelDummyProvider implements INeutronMeteringLabelAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(NeutronMeteringLabelDummyProvider.class);

    public NeutronMeteringLabelDummyProvider() {
    }

    public int canCreateMeteringLabel(NeutronMeteringLabel meteringLabel) {
        return(HttpURLConnection.HTTP_OK);
    }

    public void neutronMeteringLabelCreated(NeutronMeteringLabel meteringLabel) {
        LOGGER.info(meteringLabel.toString());
    }

    public int canDeleteMeteringLabel(NeutronMeteringLabel meteringLabel) {
        return(HttpURLConnection.HTTP_OK);
    }

    public void neutronMeteringLabelDeleted(NeutronMeteringLabel meteringLabel) {
        LOGGER.info(meteringLabel.getID()+" deleted");
    }
}
