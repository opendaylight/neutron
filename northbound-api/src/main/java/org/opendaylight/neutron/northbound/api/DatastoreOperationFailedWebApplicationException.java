/*
 * Copyright (c) 2018 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.neutron.northbound.api;

import javax.ws.rs.WebApplicationException;
import org.opendaylight.neutron.spi.ReadFailedRuntimeException;
import org.opendaylight.yangtools.yang.common.OperationFailedException;

/**
 * JAX RS specific exception wrapping an ODL MDSAL datastore read/validate/write operation failure.
 *
 * @see ReadFailedRuntimeException
 *
 * @author Michael Vorburger.ch
 */
public class DatastoreOperationFailedWebApplicationException extends WebApplicationException {
    private static final long serialVersionUID = 1L;

    public DatastoreOperationFailedWebApplicationException(OperationFailedException cause) {
        super(cause);
    }

}
