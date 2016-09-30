/*
 * Copyright (c) 2015 Hewlett-Packard Development Company, L.P. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.spi;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "l2gateway")
public final class NeutronL2gateway extends NeutronBaseAttributes<NeutronL2gateway>
        implements Serializable {
    private static final long serialVersionUID = 1L;

    @XmlElement(name = "devices")
    List<NeutronL2gatewayDevice> neutronL2gatewayDevices;

    public List<NeutronL2gatewayDevice> getNeutronL2gatewayDevices() {
        return neutronL2gatewayDevices;
    }

    public void setNeutronL2gatewayDevices(List<NeutronL2gatewayDevice> neutronL2gatewayDevices) {
        this.neutronL2gatewayDevices = neutronL2gatewayDevices;
    }

    public NeutronL2gateway extractFields(List<String> fields) {
        NeutronL2gateway ans = new NeutronL2gateway();
        for (String s : fields) {
            extractField(s, ans);
            if (s.equals("devices")) {
                List<NeutronL2gatewayDevice> devices = new ArrayList<NeutronL2gatewayDevice>();
                devices.addAll(this.getNeutronL2gatewayDevices());
                ans.setNeutronL2gatewayDevices(devices);
            }
        }
        return ans;
    }

    @Override
    public String toString() {
        return "NeutronL2Gateway [" + "id = " + uuid + ", name = " + name + ", tenant_id = " + tenantID
                + ", devices = " + neutronL2gatewayDevices + "]";
    }

}
