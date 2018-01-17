/*
 * Copyright (c) 2015 IBM Corporation and others.  All rights reserved.
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
public final class NeutronVpnService extends NeutronAdminAttributes<NeutronVpnService> {
    private static final long serialVersionUID = 1L;

    // See OpenStack Network API v2.0 Reference for description of
    // annotated attributes

    @XmlElement(name = "router_id")
    String routerUUID;

    @XmlElement(name = "subnet_id")
    String subnetUUID;

    public NeutronVpnService() {
    }

    public String getRouterUUID() {
        return routerUUID;
    }

    public void setRouterUUID(String routerUUID) {
        this.routerUUID = routerUUID;
    }

    public String getSubnetUUID() {
        return subnetUUID;
    }

    public void setSubnetUUID(String subnetUUID) {
        this.subnetUUID = subnetUUID;
    }

    @Override
    protected boolean extractField(String field, NeutronVpnService ans) {
        switch (field) {
            case "router_id":
                ans.setRouterUUID(this.getRouterUUID());
                break;
            case "subnet_id":
                ans.setSubnetUUID(this.getSubnetUUID());
                break;
            default:
                return super.extractField(field, ans);
        }
        return true;
    }
}
