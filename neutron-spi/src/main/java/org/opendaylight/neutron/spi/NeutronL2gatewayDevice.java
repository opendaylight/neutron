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

public class NeutronL2gatewayDevice extends Neutron_ID implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = "interfaces")
    List<NeutronL2gatewayDeviceInterface> neutronL2gatewayDeviceInterfaces;

    @XmlElement(name = "device_name")
    String deviceName;

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public List<NeutronL2gatewayDeviceInterface> getNeutronL2gatewayDeviceInterfaces() {
        return neutronL2gatewayDeviceInterfaces;
    }

    public void setNeutronL2gatewayDeviceInterfaces(
            List<NeutronL2gatewayDeviceInterface> neutronL2gatewayDeviceInterfaces) {
        this.neutronL2gatewayDeviceInterfaces = neutronL2gatewayDeviceInterfaces;
    }

    @Override
    public String toString() {
        return "NeutronL2gatewayDevice [" + "id=" + uuid + ", neutronL2gwDeviceInterfaces="
                + neutronL2gatewayDeviceInterfaces + ", deviceName=" + deviceName + "]";
    }
}
