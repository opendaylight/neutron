/*
 * Copyright (c) 2015 Hewlett-Packard Development Company and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.spi;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "l2gatewayConnections")
public class NeutronL2GatewayConnection implements Serializable, INeutronObject {
    private static final long serialVersionUID = 1L;

    @XmlElement (name = "tenant_id")
    String tenantUUID;

    @XmlElement (name = "connection_id")
    String l2gatewayConnectionUUID;

    @XmlElement(name = "gateway_id")
    String l2gatewayUUID;

    @XmlElement(name = "network_id")
    String networkUUID;

    @XmlElement(name = "default_segmentation_id")
    Integer segmentID;

    @XmlElement(name = "port_id")
    String portUUID;

    @Override
    public String getID() {
        return l2gatewayConnectionUUID;
    }

    @Override
    public void setID(String id) {
        this.l2gatewayConnectionUUID=id;
    }

    public String getL2gatewayConnectionUUID() {
        return l2gatewayConnectionUUID;
    }

    public String getL2gatewayUUID() {
        return l2gatewayUUID;
    }

    public String getNetworkUUID() {
        return networkUUID;
    }

    public Integer getSegmentID() {
        return segmentID;
    }

    public String getTenantUUID() {
        return tenantUUID;
    }

    public void setL2gatewayConnectionUUID(String l2gatewayConnectionUUID) {
        this.l2gatewayConnectionUUID = l2gatewayConnectionUUID;
    }

    public void setL2gatewayUUID(String l2gatewayUUID) {
        this.l2gatewayUUID = l2gatewayUUID;
    }

    public void setNetworkUUID(String networkUUID) {
        this.networkUUID = networkUUID;
    }

    public void setSegmentID(Integer segmentID) {
        this.segmentID = segmentID;
    }

    public void setTenantUUID(String tenantUUID) {
        this.tenantUUID = tenantUUID;
    }

    public String getPortUUID() {
        return portUUID;
    }

    public void setPortUUID(String portUUID) {
        this.portUUID = portUUID;
    }

    public NeutronL2GatewayConnection extractFields(List<String> fields) {
        NeutronL2GatewayConnection ans = new NeutronL2GatewayConnection();
        Iterator<String> i = fields.iterator();
        while (i.hasNext()) {
            String s = i.next();
            if (s.equals("tenant_id")) {
                ans.setTenantUUID(this.getTenantUUID());
            }
            if (s.equals("connection_id")) {
                ans.setL2gatewayConnectionUUID(this.getL2gatewayConnectionUUID());
            }
            if (s.equals("gateway_id")) {
                ans.setL2gatewayUUID(this.getL2gatewayUUID());
            }
            if (s.equals("network_id")) {
                ans.setNetworkUUID(this.getNetworkUUID());
            }
            if (s.equals("default_segmentation_id")) {
                ans.setSegmentID(this.getSegmentID());
            }
            if (s.equals("port_id")) {
                ans.setPortUUID(this.getPortUUID());
            }
        }
        return ans;
    }

    @Override
    public String toString() {
        return "NeutronL2GatewayConnection [" +
                "tenant_id=" + tenantUUID +
                ", connection_id=" + l2gatewayConnectionUUID +
                ", gateway_id=" + l2gatewayUUID +
                ", network_id=" + networkUUID +
                ", default_segmentation_id=" + segmentID +
                ", port_id=" + portUUID +
                "]";
    }

}
