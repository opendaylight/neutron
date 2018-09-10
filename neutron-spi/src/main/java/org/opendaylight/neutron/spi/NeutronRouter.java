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

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public final class NeutronRouter extends NeutronAdminAttributes<NeutronRouter>
        implements Serializable {
    private static final long serialVersionUID = 1L;

    // See OpenStack Network API v2.0 Reference for description of
    // annotated attributes
    @XmlElement(name = "external_gateway_info", nillable = true)
    NeutronRouterNetworkReference externalGatewayInfo;

    @XmlElement(name = "distributed")
    Boolean distributed;

    @XmlElement(name = "gw_port_id", nillable = true)
    String gatewayPortId;

    @XmlElement(name = "routes")
    List<NeutronRoute> routes;

    public NeutronRouter() {
    }

    public boolean isAdminStateUp() {
        if (adminStateUp == null) {
            return true;
        }
        return adminStateUp;
    }

    public NeutronRouterNetworkReference getExternalGatewayInfo() {
        return externalGatewayInfo;
    }

    public void setExternalGatewayInfo(NeutronRouterNetworkReference externalGatewayInfo) {
        this.externalGatewayInfo = externalGatewayInfo;
    }

    public Boolean getDistributed() {
        return distributed;
    }

    public void setDistributed(Boolean distributed) {
        this.distributed = distributed;
    }

    public String getGatewayPortId() {
        return gatewayPortId;
    }

    public void setGatewayPortId(String gatewayPortId) {
        this.gatewayPortId = gatewayPortId;
    }

    public List<NeutronRoute> getRoutes() {
        return routes;
    }

    public void setRoutes(List<NeutronRoute> routes) {
        this.routes = routes;
    }

    @Override
    protected boolean extractField(String field, NeutronRouter ans) {
        switch (field) {
            case "external_gateway_info":
                ans.setExternalGatewayInfo(this.getExternalGatewayInfo());
                break;
            case "distributed":
                ans.setDistributed(this.getDistributed());
                break;
            case "gw_port_id":
                ans.setGatewayPortId(this.getGatewayPortId());
                break;
            case "routes":
                ans.setRoutes(this.getRoutes());
                break;
            default:
                return super.extractField(field, ans);
        }
        return true;
    }

    @Override
    public String toString() {
        return "NeutronRouter [" + "id=" + uuid + ", name=" + name + ", adminStateUp=" + adminStateUp + ", status="
                + status + ", tenantID=" + getTenantID() + ", external_gateway_info=" + externalGatewayInfo
                + ", distributed=" + distributed + ", gw_port_id=" + gatewayPortId + ", routes=" + routes + "]";
    }

}
