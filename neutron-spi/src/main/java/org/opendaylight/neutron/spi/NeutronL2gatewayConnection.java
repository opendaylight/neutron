/*
 * Copyright (c) 2015 Hewlett-Packard Development Company, L.P. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.spi;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "l2gatewayConnection")
@XmlAccessorType(XmlAccessType.NONE)

public class NeutronL2gatewayConnection extends NeutronObject implements Serializable, INeutronObject {
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
        Iterator<String> i = fields.iterator();
        while (i.hasNext()) {
            String s = i.next();
            if (s.equals("tenant_id")) {
                ans.setTenantID(this.getTenantID());
            }
            if (s.equals("connection_id")) {
                ans.setID(this.getID());
            }
            if (s.equals("gateway_id")) {
                ans.setL2gatewayID(this.getL2gatewayID());
            }
            if (s.equals("network_id")) {
                ans.setNetworkID(this.getNetworkID());
            }
            if (s.equals("segmentation_id")) {
                ans.setSegmentID(this.getSegmentID());
            }
            if (s.equals("port_id")) {
                ans.setPortID(this.getPortID());
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
