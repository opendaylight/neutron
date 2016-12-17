/*
 * Copyright (c) 2013, 2015 IBM Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.spi;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public final class NeutronIPs implements Serializable {
    private static final long serialVersionUID = 1L;

    // See OpenStack Network API v2.0 Reference for description of
    // annotated attributes

    @XmlElement(name = "ip_address")
    String ipAddress;

    @XmlElement(name = "subnet_id")
    String subnetUUID;

    public NeutronIPs() {
    }

    public NeutronIPs(String uuid) {
        subnetUUID = uuid;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getSubnetUUID() {
        return subnetUUID;
    }

    public void setSubnetUUID(String subnetUUID) {
        this.subnetUUID = subnetUUID;
    }

    @Override
    public String toString() {
        return "NeutronIPs{" + "ipAddress='" + ipAddress + '\'' + ", subnetUUID='" + subnetUUID + '\'' + "}";
    }
}
