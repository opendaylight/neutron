/*
 * Copyright (c) 2019 Ericsson India Global Services Pvt Ltd. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.spi;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "bgpvpn_router_association")
@XmlAccessorType(XmlAccessType.NONE)
public final class NeutronBgpvpnRouterAssociation extends NeutronAdminAttributes<NeutronBgpvpnRouterAssociation>
        implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = "bgpvpn_id")
    String bgpvpnId;

    @XmlElement(name = "router_id")
    String routerId;

    public void initDefaults() {
    }

    public String getBgpvpnId() {
        return bgpvpnId;
    }

    public void setBgpvpnId(String bgpvpnId) {
        this.bgpvpnId = bgpvpnId;
    }

    public String getRouterId() {
        return routerId;
    }

    public void setRouterId(String routerId) {
        this.routerId = routerId;
    }

    @Override
    protected boolean extractField(String field, NeutronBgpvpnRouterAssociation ans) {
        switch (field) {
            case "bgpvpn_id":
                ans.setBgpvpnId(this.getBgpvpnId());
                break;
            case "router_id":
                ans.setRouterId(this.getRouterId());
                break;
            default:
                return super.extractField(field, ans);
        }
        return true;
    }

    @Override
    public String toString() {
        return "NeutronBgpvpnRouterAssociation [bgpvpnRouterAssociationUUID=" + uuid + ", bgpvpnId=" + bgpvpnId
                + ", routerId=" + routerId + "]";
    }
}
