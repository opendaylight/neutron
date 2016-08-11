/*
 * Copyright (c) 2015 IBM Corporation and others.  All rights reserved.
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

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class NeutronVPNService extends NeutronObject implements Serializable, INeutronObject {
    private static final long serialVersionUID = 1L;

    // See OpenStack Network API v2.0 Reference for description of
    // annotated attributes

    @XmlElement (name = "router_id")
    String routerUUID;

    @XmlElement (name = "status")
    String status;

    @XmlElement (name = "name")
    String name;

    @XmlElement (defaultValue = "true", name = "admin_state_up")
    Boolean adminStateUp;

    @XmlElement (name = "subnet_id")
    String subnetUUID;

    public NeutronVPNService() {
    }

    public String getRouterUUID() {
        return routerUUID;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getAdminStateUp() { return adminStateUp; }

    public void setAdminStateUp(boolean newValue) {
        adminStateUp = newValue;
    }

    public String getSubnetUUID() {
        return subnetUUID;
    }

    public void setSubnetUUID(String subnetUUID) {
        this.subnetUUID = subnetUUID;
    }

    public NeutronVPNService extractFields(List<String> fields) {
        NeutronVPNService ans = new NeutronVPNService();
        Iterator<String> i = fields.iterator();
        while (i.hasNext()) {
            String s = i.next();
            if (s.equals("id")) {
                ans.setID(this.getID());
            }
            if (s.equals("admin_state_up")) {
                ans.setAdminStateUp(this.getAdminStateUp());
            }
            if (s.equals("name")) {
                ans.setName(this.getName());
            }
            if (s.equals("router_id")) {
                ans.setRouterUUID(this.getRouterUUID());
            }
            if (s.equals("status")) {
                ans.setStatus(this.getStatus());
            }
            if (s.equals("subnet_id")) {
                ans.setSubnetUUID(this.getSubnetUUID());
            }
            if (s.equals("tenant_id")) {
                ans.setTenantID(this.getTenantID());
            }
        }
        return ans;
    }
}
