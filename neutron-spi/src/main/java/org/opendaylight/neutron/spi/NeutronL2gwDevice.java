/*
 * Copyright (c) 2015 Hewlett-Packard Development Company, L.P. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.spi;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class NeutronL2gwDevice implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = "interfaces")
    List<NeutronL2gwDeviceInterface> neutronL2gwDeviceInterfaces;

    @XmlElement(name = "device_name")
    String deviceName;

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public List<NeutronL2gwDeviceInterface> getNeutronL2gwDeviceInterfaces() {
        return neutronL2gwDeviceInterfaces;
    }

    public void setNeutronL2gwDeviceInterfaces(
                    List<NeutronL2gwDeviceInterface> neutronL2gwDeviceInterfaces) {
        this.neutronL2gwDeviceInterfaces = neutronL2gwDeviceInterfaces;
    }
    @Override
    public String toString() {
        return "NeutronL2gwDevice [neutronL2gwDeviceInterfaces="
                + neutronL2gwDeviceInterfaces + ", deviceName=" + deviceName
                + "]";
    }
}
