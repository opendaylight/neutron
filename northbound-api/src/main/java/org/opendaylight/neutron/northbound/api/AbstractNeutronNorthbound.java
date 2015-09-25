/*
 * Copyright (c) 2015 Intel Corporation  All rights reserved.
 * Copyright (c) 2015 Isaku Yamahata  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.northbound.api;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractNeutronNorthbound {
    private static final Logger LOGGER = LoggerFactory
        .getLogger(AbstractNeutronNorthbound.class);

    protected static final int HTTP_OK_BOTTOM = 200;
    protected static final int HTTP_OK_TOP = 299;

    private static final String INTERFACE_NAME_BASE = " CRUD Interface";
    private static final String UUID_NO_EXIST_BASE = " UUID does not exist.";
    protected static final String NO_PROVIDERS = "No providers registered.  Please try again later";
    protected static final String NO_PROVIDER_LIST = "Couldn't get providers list.  Please try again later";

    protected static final String serviceUnavailable(String resourceName) {
        return resourceName + INTERFACE_NAME_BASE + RestMessages.SERVICEUNAVAILABLE.toString();
    }
    protected static final String uuidNoExist(String resourceName) {
        return resourceName + UUID_NO_EXIST_BASE;
    }

    protected interface Remover {
        abstract public boolean remove(String uuid);
    }

    public void deleteUuid(String resourceName, String uuid, Remover remover) {
        boolean exist = false;
        try {
            exist = remover.remove(uuid);
        } catch (Exception e) {
            LOGGER.debug("exception during remove {} {} {}",
                         resourceName, uuid, e);
            throw new InternalServerErrorException("Could not delete " +
                                                   resourceName);
        }
        if (!exist) {
            throw new ResourceNotFoundException(uuidNoExist(resourceName));
        }
    }
}
