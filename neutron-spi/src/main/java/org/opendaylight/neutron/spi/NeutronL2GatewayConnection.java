/*
 * Copyright (c) 2015 Hewlett-Packard Development Company, L.P. and others.
 * All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse License v1.0 which accompanies this distribution,
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

    @XmlElement(name = "l2gateway_id")
    String l2gatewayUUID;

    @XmlElement(name = "network_id")
    String networkUUID;

    @XmlElement(name = "segment_id")
    String segmentUUID;

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

    public String getSegmentUUID() {
        return segmentUUID;
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

    public void setSegmentUUID(String segmentUUID) {
        this.segmentUUID = segmentUUID;
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
            if (s.equals("l2gateway_id")) {
                ans.setL2gatewayUUID(this.getL2gatewayUUID());
            }
            if (s.equals("network_id")) {
                ans.setNetworkUUID(this.getNetworkUUID());
            }
            if (s.equals("segment_id")) {
                ans.setSegmentUUID(this.getSegmentUUID());
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
                ", l2gateway_id=" + l2gatewayUUID +
                ", network_id=" + networkUUID +
                ", segment_id=" + segmentUUID +
                ", port_id=" + portUUID +
                "]";
    }
}
