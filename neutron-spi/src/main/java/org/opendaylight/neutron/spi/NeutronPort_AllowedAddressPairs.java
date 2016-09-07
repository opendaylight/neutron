/*
 * Copyright (c) 2015 IBM Corporation and others.  All rights reserved.
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
public final class NeutronPort_AllowedAddressPairs implements Serializable {
    private static final long serialVersionUID = 1L;

    @XmlElement(name = "mac_address")
    String macAddress;

    @XmlElement(name = "ip_address")
    String ipAddress;

    public NeutronPort_AllowedAddressPairs() {
    }

    public NeutronPort_AllowedAddressPairs(String macAddress, String ipAddress) {
        this.macAddress = macAddress;
        this.ipAddress = ipAddress;
    }

    public String getMacAddress() {
        return (macAddress);
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getIpAddress() {
        return (ipAddress);
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
}
