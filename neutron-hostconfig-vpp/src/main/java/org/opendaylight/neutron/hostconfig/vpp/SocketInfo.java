/*
 * Copyright (c) 2016 Cisco Systems, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.hostconfig.vpp;

public class SocketInfo {

    private String socketPath;
    private String socketPrefix;
    private final String PORT_ID = "$PORT_ID";

    public SocketInfo(String socketPath, String socketPrefix) {
        this.socketPath = socketPath;
        this.socketPrefix = socketPrefix;
    }

    public String getSocketPath() {
        return socketPath;
    }

    public String getSocketPrefix() {
        return socketPrefix;
    }

    public String getVhostUserSocket() {
        return new StringBuilder().append(socketPath).append(socketPrefix).append(PORT_ID).toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((socketPath == null) ? 0 : socketPath.hashCode());
        result = prime * result + ((socketPrefix == null) ? 0 : socketPrefix.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SocketInfo other = (SocketInfo) obj;
        if (socketPath == null) {
            if (other.socketPath != null)
                return false;
        } else if (!socketPath.equals(other.socketPath))
            return false;
        if (socketPrefix == null) {
            if (other.socketPrefix != null)
                return false;
        } else if (!socketPrefix.equals(other.socketPrefix))
            return false;
        return true;
    }
}
