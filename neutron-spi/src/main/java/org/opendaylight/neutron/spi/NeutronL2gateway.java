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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@XmlRootElement(name = "l2gateway")
public final class NeutronL2gateway extends NeutronBaseAttributes<NeutronL2gateway>
        implements Serializable {
    private static final Logger LOGGER = LoggerFactory.getLogger(NeutronL2gateway.class);
    private static final long serialVersionUID = 1L;

    @XmlElement(name = "devices")
    List<NeutronL2gatewayDevice> l2gatewayDevices;

    public List<NeutronL2gatewayDevice> getNeutronL2gatewayDevices() {
        return l2gatewayDevices;
    }

    public void setNeutronL2gatewayDevices(List<NeutronL2gatewayDevice> l2gatewayDevices) {
        this.l2gatewayDevices = l2gatewayDevices;
    }

    public NeutronL2gateway extractFields(List<String> fields) {
        NeutronL2gateway ans = new NeutronL2gateway();
        for (String s : fields) {
            if (extractField(s, ans)) {
                continue;
            }
            switch (s) {
                case "devices":
                    List<NeutronL2gatewayDevice> devices = new ArrayList<NeutronL2gatewayDevice>();
                    devices.addAll(this.getNeutronL2gatewayDevices());
                    ans.setNeutronL2gatewayDevices(devices);
                    break;
                default:
                    LOGGER.warn("{} is not a NeutronL2gateway suitable field.", s);
                    break;
            }
        }
        return ans;
    }

    @Override
    public String toString() {
        return "NeutronL2Gateway [" + "id = " + uuid + ", name = " + name + ", tenant_id = " + tenantID
                + ", devices = " + l2gatewayDevices + "]";
    }

}
