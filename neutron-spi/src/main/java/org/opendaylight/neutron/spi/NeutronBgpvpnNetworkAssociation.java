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

@XmlRootElement(name = "bgpvpn_network_association")
@XmlAccessorType(XmlAccessType.NONE)
public final class NeutronBgpvpnNetworkAssociation extends NeutronAdminAttributes<NeutronBgpvpnNetworkAssociation>
        implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = "bgpvpn_id")
    String bgpvpnId;

    @XmlElement(name = "network_id")
    String networkIds;

   public void initDefaults() {
    }

    public String getBgpvpnId() {
        return bgpvpnId;
    }

    public void setBgpvpnId(String bgpvpnId) {
        this.bgpvpnId = bgpvpnId;
    }

    public String getNetworkIds() {
        return networkIds;
    }

    public void setNetworkIds(String networkIds) {
        this.networkIds = networkIds;
    }

    @Override
    protected boolean extractField(String field, NeutronBgpvpnNetworkAssociation ans) {
        switch (field) {
            case "bgpvpn_id":
                ans.setBgpvpnId(this.getBgpvpnId());
                break;
            case "network_id":
                ans.setNetworkIds(this.getNetworkIds());
                break;
            default:
                return super.extractField(field, ans);
        }
        return true;
    }

    @Override
    public String toString() {
        return "NeutronBgpvpnNetworkAssociation [bgpvpnNetworkAssociationUUID=" + uuid + ", bgpvpnId=" + bgpvpnId
                + ", networkId=" + networkIds + "]";
    }
}

