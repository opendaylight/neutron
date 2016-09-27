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
public final class NeutronRouter extends NeutronObject<NeutronRouter>
        implements Serializable, INeutronObject<NeutronRouter> {
    private static final long serialVersionUID = 1L;

    // See OpenStack Network API v2.0 Reference for description of
    // annotated attributes
    @XmlElement(name = "name")
    String name;

    @XmlElement(defaultValue = "true", name = "admin_state_up")
    Boolean adminStateUp;

    @XmlElement(name = "status")
    String status;

    @XmlElement(name = "external_gateway_info", nillable = true)
    NeutronRouter_NetworkReference externalGatewayInfo;

    @XmlElement(name = "distributed")
    Boolean distributed;

    @XmlElement(name = "gw_port_id", nillable = true)
    String gatewayPortId;

    @XmlElement(name = "routes")
    List<NeutronRoute> routes;

    public NeutronRouter() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isAdminStateUp() {
        if (adminStateUp == null) {
            return true;
        }
        return adminStateUp;
    }

    public Boolean getAdminStateUp() {
        return adminStateUp;
    }

    public void setAdminStateUp(Boolean newValue) {
        adminStateUp = newValue;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public NeutronRouter_NetworkReference getExternalGatewayInfo() {
        return externalGatewayInfo;
    }

    public void setExternalGatewayInfo(NeutronRouter_NetworkReference externalGatewayInfo) {
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

    /**
     * This method copies selected fields from the object and returns them
     * as a new object, suitable for marshaling.
     *
     * @param fields
     *            List of attributes to be extracted
     * @return an OpenStackRouters object with only the selected fields
     * populated
     */
    public NeutronRouter extractFields(List<String> fields) {
        NeutronRouter ans = new NeutronRouter();
        for (String s : fields) {
            extractField(s, ans);
            if (s.equals("name")) {
                ans.setName(this.getName());
            }
            if (s.equals("admin_state_up")) {
                ans.setAdminStateUp(this.getAdminStateUp());
            }
            if (s.equals("status")) {
                ans.setStatus(this.getStatus());
            }
            if (s.equals("external_gateway_info")) {
                ans.setExternalGatewayInfo(this.getExternalGatewayInfo());
            }
            if (s.equals("distributed")) {
                ans.setDistributed(this.getDistributed());
            }
            if (s.equals("gw_port_id")) {
                ans.setGatewayPortId(this.getGatewayPortId());
            }
            if (s.equals("routes")) {
                ans.setRoutes(this.getRoutes());
            }
        }
        return ans;
    }

    @Override
    public void initDefaults() {
        adminStateUp = true;
    }

    @Override
    public String toString() {
        return "NeutronRouter [" + "id=" + uuid + ", name=" + name + ", adminStateUp=" + adminStateUp + ", status="
                + status + ", tenantID=" + tenantID + ", external_gateway_info=" + externalGatewayInfo
                + ", distributed=" + distributed + ", gw_port_id=" + gatewayPortId + ", routes=" + routes + "]";
    }

}
