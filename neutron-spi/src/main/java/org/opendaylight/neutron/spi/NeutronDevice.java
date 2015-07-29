/*
 * Copyright (c) 2015 Hewlett-Packard Development Company and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.spi;

import java.util.List;
import javax.xml.bind.annotation.XmlElement;

public class NeutronDevice {

    @XmlElement(name = "interfaces")
    List<NeutronDeviceInterface> neutrondeviceInterfaces;

    @XmlElement(name = "device_name")
    String deviceName;

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public List<NeutronDeviceInterface> getNeutrondeviceInterfaces() {
        return neutrondeviceInterfaces;
    }

    public void setNeutrondeviceInterfaces(
                    List<NeutronDeviceInterface> neutrondeviceInterfaces) {
        this.neutrondeviceInterfaces = neutrondeviceInterfaces;
    }
}
