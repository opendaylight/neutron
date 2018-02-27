/*
 * Copyright (c) 2013, 2015 IBM Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.spi;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public final class NeutronFloatingIp extends NeutronObject<NeutronFloatingIp> {
    private static final long serialVersionUID = 1L;

    // See OpenStack Network API v2.0 Reference for description of
    // annotated attributes

    @XmlElement(name = "floating_network_id")
    String floatingNetworkUUID;

    @XmlElement(name = "port_id")
    String portUUID;

    @XmlElement(name = "fixed_ip_address")
    String fixedIpAddress;

    @XmlElement(name = "floating_ip_address")
    String floatingIpAddress;

    @XmlElement(name = "router_id")
    String routerUUID;

    @XmlElement(name = "status")
    String status;

    public NeutronFloatingIp() {
    }

    public String getFloatingNetworkUUID() {
        return floatingNetworkUUID;
    }

    public void setFloatingNetworkUUID(String floatingNetworkUUID) {
        this.floatingNetworkUUID = floatingNetworkUUID;
    }

    public String getPortUUID() {
        return portUUID;
    }

    public String getRouterUUID() {
        return routerUUID;
    }

    public void setPortUUID(String portUUID) {
        this.portUUID = portUUID;
    }

    public String getFixedIpAddress() {
        return fixedIpAddress;
    }

    public void setFixedIpAddress(String fixedIpAddress) {
        this.fixedIpAddress = fixedIpAddress;
    }

    public String getFloatingIpAddress() {
        return floatingIpAddress;
    }

    public void setFloatingIpAddress(String floatingIpAddress) {
        this.floatingIpAddress = floatingIpAddress;
    }

    public void setRouterUUID(String routerUUID) {
        this.routerUUID = routerUUID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    protected boolean extractField(String field, NeutronFloatingIp ans) {
        switch (field) {
            case "floating_network_id":
                ans.setFloatingNetworkUUID(this.getFloatingNetworkUUID());
                break;
            case "port_id":
                ans.setPortUUID(this.getPortUUID());
                break;
            case "fixed_ip_address":
                ans.setFixedIpAddress(this.getFixedIpAddress());
                break;
            case "floating_ip_address":
                ans.setFloatingIpAddress(this.getFloatingIpAddress());
                break;
            case "router_id":
                ans.setRouterUUID(this.getRouterUUID());
                break;
            case "status":
                ans.setStatus(this.getStatus());
                break;
            default:
                return super.extractField(field, ans);
        }
        return true;
    }

    @Override
    public String toString() {
        return "NeutronFloatingIp{" + "fipUUID='" + uuid + '\'' + ", fipFloatingNetworkId='" + floatingNetworkUUID
                + '\'' + ", fipPortUUID='" + portUUID + '\'' + ", fipFixedIpAddress='" + fixedIpAddress + '\''
                + ", fipFloatingIpAddress=" + floatingIpAddress + ", fipTenantId='" + tenantID + '\''
                + ", fipRouterId='" + routerUUID + '\'' + ", fipStatus='" + status + '\'' + '}';
    }
}
