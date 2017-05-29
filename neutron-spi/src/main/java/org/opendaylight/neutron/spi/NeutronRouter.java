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
public final class NeutronRouter extends NeutronAdminAttributes<NeutronRouter>
        implements Serializable {
    private static final Logger LOG = LoggerFactory.getLogger(NeutronRouter.class);
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

    /**
     * This method copies selected fields from the object and returns them
     * as a new object, suitable for marshaling.
     *
     * @param fields
     *            List of attributes to be extracted
     * @return an OpenStackRouters object with only the selected fields
     *             populated
     */
    public NeutronRouter extractFields(List<String> fields) {
        NeutronRouter ans = new NeutronRouter();
        for (String s : fields) {
            if (extractField(s, ans)) {
                continue;
            }
            switch (s) {
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
                    LOG.warn("{} is not a NeutronRouter suitable field.", s);
                    break;
            }
        }
        return ans;
    }

    @Override
    public String toString() {
        return "NeutronRouter [" + "id=" + uuid + ", name=" + name + ", adminStateUp=" + adminStateUp + ", status="
                + status + ", tenantID=" + tenantID + ", external_gateway_info=" + externalGatewayInfo
                + ", distributed=" + distributed + ", gw_port_id=" + gatewayPortId + ", routes=" + routes + "]";
    }

}
