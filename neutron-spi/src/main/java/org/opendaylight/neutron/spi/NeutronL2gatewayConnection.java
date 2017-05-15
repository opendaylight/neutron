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
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@XmlRootElement(name = "l2gatewayConnection")
@XmlAccessorType(XmlAccessType.NONE)
public final class NeutronL2gatewayConnection extends NeutronObject<NeutronL2gatewayConnection>
        implements Serializable, INeutronObject<NeutronL2gatewayConnection> {
    private static final Logger LOGGER = LoggerFactory.getLogger(NeutronL2gatewayConnection.class);
    private static final long serialVersionUID = 1L;

    @XmlElement(name = "gateway_id")
    String l2gatewayID;

    @XmlElement(name = "network_id")
    String networkID;

    @XmlElement(name = "segmentation_id")
    Integer segmentID;

    @XmlElement(name = "port_id")
    String portID;

    public String getL2gatewayID() {
        return l2gatewayID;
    }

    public String getNetworkID() {
        return networkID;
    }

    public Integer getSegmentID() {
        return segmentID;
    }

    public void setL2gatewayID(String l2gatewayID) {
        this.l2gatewayID = l2gatewayID;
    }

    public void setNetworkID(String networkID) {
        this.networkID = networkID;
    }

    public void setSegmentID(Integer segmentID) {
        this.segmentID = segmentID;
    }

    public String getPortID() {
        return portID;
    }

    public void setPortID(String portID) {
        this.portID = portID;
    }

    public NeutronL2gatewayConnection extractFields(List<String> fields) {
        NeutronL2gatewayConnection ans = new NeutronL2gatewayConnection();
        for (String s : fields) {
            if (extractField(s, ans)) {
                continue;
            }
            switch (s) {
                case "connection_id":
                    ans.setID(this.getID());
                    break;
                case "gateway_id":
                    ans.setL2gatewayID(this.getL2gatewayID());
                    break;
                case "network_id":
                    ans.setNetworkID(this.getNetworkID());
                    break;
                case "segmentation_id":
                    ans.setSegmentID(this.getSegmentID());
                    break;
                case "port_id":
                    ans.setPortID(this.getPortID());
                    break;
                default:
                    LOGGER.warn("{} is not a NeutronL2gatewayConnection suitable field.", s);
                    break;
            }
        }
        return ans;
    }

    @Override
    public String toString() {
        return "NeutronL2GatewayConnection [" + "tenant_id = " + tenantID + ", connection_id = " + uuid
                + ", gateway_id = " + l2gatewayID + ", network_id = " + networkID + ", segmentation_id = " + segmentID
                + ", port_id = " + portID + "]";
    }

}
