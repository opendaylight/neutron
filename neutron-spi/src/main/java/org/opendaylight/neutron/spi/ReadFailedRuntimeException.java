/*
 * Copyright (c) 2018 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.neutron.spi;

import org.opendaylight.controller.md.sal.common.api.data.ReadFailedException;

/**
 * {@link ReadFailedException} as an unchecked RuntimeException.
 * See also org.opendaylight.neutron.northbound.api.DatastoreOperationFailedWebApplicationException.
 *
 * @author Michael Vorburger.ch
 */
public class ReadFailedRuntimeException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ReadFailedRuntimeException(ReadFailedException cause) {
        super(cause);
    }
}
