/*
 * Copyright (c) 2013 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.northbound.api;

import java.net.HttpURLConnection;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Status Code 501 (Not Implemented Error)
 *
 * <p>
 * The function is not implemented.
 *
 */
public class UnimplementedException extends WebApplicationException {
    private static final long serialVersionUID = 1L;

    /**
     * Constructor for the NOT_IMPLEMENTED custom handler
     *
     * @param string Error message to specify further the
     *            SERVICE_UNAVAILABLE response
     *
     */
    public UnimplementedException(String string) {
        super(Response.status(HttpURLConnection.HTTP_NOT_IMPLEMENTED).entity(string).type(MediaType.TEXT_PLAIN)
                .build());
    }
}
