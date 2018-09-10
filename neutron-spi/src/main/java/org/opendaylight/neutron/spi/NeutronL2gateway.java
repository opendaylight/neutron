/*
 * Copyright (c) 2015 Hewlett-Packard Development Company, L.P. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.spi;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "l2gateway")
public final class NeutronL2gateway extends NeutronBaseAttributes<NeutronL2gateway> {
    private static final long serialVersionUID = 1L;

    @XmlElement(name = "devices")
    List<NeutronL2gatewayDevice> l2gatewayDevices;

    public List<NeutronL2gatewayDevice> getNeutronL2gatewayDevices() {
        return l2gatewayDevices;
    }

    public void setNeutronL2gatewayDevices(List<NeutronL2gatewayDevice> newL2gatewayDevices) {
        this.l2gatewayDevices = newL2gatewayDevices;
    }

    @Override
    protected boolean extractField(String field, NeutronL2gateway ans) {
        switch (field) {
            case "devices":
                List<NeutronL2gatewayDevice> devices = new ArrayList<>();
                devices.addAll(this.getNeutronL2gatewayDevices());
                ans.setNeutronL2gatewayDevices(devices);
                break;
            default:
                return super.extractField(field, ans);
        }
        return true;
    }

    @Override
    public String toString() {
        return "NeutronL2Gateway [" + "id = " + uuid + ", name = " + name + ", tenant_id = " + getTenantID()
                + ", devices = " + l2gatewayDevices + "]";
    }

}
