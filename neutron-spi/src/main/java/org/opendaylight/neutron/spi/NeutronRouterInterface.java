/*
 * Copyright (c) 2013, 2015 IBM Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.spi;

import java.io.Serializable;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public final class NeutronRouterInterface extends NeutronObject<NeutronRouterInterface>
        implements Serializable, INeutronObject<NeutronRouterInterface> {
    private static final Logger LOGGER = LoggerFactory.getLogger(NeutronRouterInterface.class);
    private static final long serialVersionUID = 1L;

    // See OpenStack Network API v2.0 Reference for description of
    // annotated attributes

    @XmlElement(name = "subnet_id")
    String subnetUUID;

    @XmlElement(name = "port_id")
    String portUUID;

    public NeutronRouterInterface() {
    }

    public NeutronRouterInterface(String subnetUUID, String portUUID) {
        this.subnetUUID = subnetUUID;
        this.portUUID = portUUID;
    }

    public String getSubnetUUID() {
        return subnetUUID;
    }

    public void setSubnetUUID(String subnetUUID) {
        this.subnetUUID = subnetUUID;
    }

    public String getPortUUID() {
        return portUUID;
    }

    public void setPortUUID(String portUUID) {
        this.portUUID = portUUID;
    }

    @Override
    public NeutronRouterInterface extractFields(List<String> fields) {
        NeutronRouterInterface ans = new NeutronRouterInterface();
        for (String s : fields) {
            if (extractField(s, ans)) {
                continue;
            }
            switch (s) {
                case "subnet_id":
                    ans.setSubnetUUID(this.getSubnetUUID());
                    break;
                case "port_id":
                    ans.setPortUUID(this.getPortUUID());
                    break;
                default:
                    LOGGER.warn("{} is not a NeutronRouterInterface suitable field.", s);
                    break;
            }
        }
        return ans;
    }

    @Override
    public String toString() {
        return "NeutronRouterInterface [" + "subnetUUID=" + subnetUUID + ", portUUID=" + portUUID + ", id=" + uuid
                + ", tenantID=" + tenantID + "]";
    }
}
