/*
 * Copyright (c) 2017 Cisco Systems, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.hostconfig.vpp;

import com.google.common.base.Preconditions;

public class SocketInfo {

    private final String socketPath;
    private final String socketPrefix;
    private final String vhostuserMode;
    private static final String PORT_ID = "$PORT_ID";

    public SocketInfo(String socketPath, String socketPrefix, String vhostuserMode) {
        this.socketPath = Preconditions.checkNotNull(socketPath);
        this.socketPrefix = Preconditions.checkNotNull(socketPrefix);
        this.vhostuserMode = Preconditions.checkNotNull(vhostuserMode);
    }

    public String getSocketPath() {
        return socketPath;
    }

    public String getSocketPrefix() {
        return socketPrefix;
    }

    public String getVhostuserMode() {
        return vhostuserMode;
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
        result = prime * result + ((vhostuserMode == null) ? 0 : vhostuserMode.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        SocketInfo other = (SocketInfo) obj;
        if (socketPath == null) {
            if (other.socketPath != null) {
                return false;
            }
        } else if (!socketPath.equals(other.socketPath)) {
            return false;
        }
        if (socketPrefix == null) {
            if (other.socketPrefix != null) {
                return false;
            }
        } else if (!socketPrefix.equals(other.socketPrefix)) {
            return false;
        }
        if (vhostuserMode == null) {
            if (other.vhostuserMode != null) {
                return false;
            }
        } else if (!vhostuserMode.equals(other.vhostuserMode)) {
            return false;
        }
        return true;
    }
}
