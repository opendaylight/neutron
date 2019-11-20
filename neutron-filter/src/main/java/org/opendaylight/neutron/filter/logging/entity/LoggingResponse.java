/*
 * Copyright (c) 2016, 2017 Ericsson India Global Services Pvt Ltd. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.filter.logging.entity;

import java.io.Serializable;
import java.util.Map;

public class LoggingResponse implements Serializable {

    private static final long serialVersionUID = -6692682176015358216L;

    private int status;
    private Map<String, String> headers;
    private char[] body;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public char[] getBody() {
        return body == null ? null : body.clone();
    }

    public void setBody(char[] body) {
        this.body = body != null ? body.clone() : null;
    }
}