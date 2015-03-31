/*
 * Copyright IBM Corporation, 2015.  All rights reserved.
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

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class NeutronVPNService implements Serializable {
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
    Boolean adminStateUp;             // admin state up (true/false)

    @XmlElement (name = "subnet_id")
    String subnetUUID;

    @XmlElement (name = "tenant_id")
    String tenantID;

    @XmlElement (name = "id")
    String id;

    @XmlElement (name = "description")
    String description;

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

    public String getTenantID() {
        return tenantID;
    }

    public void setTenantID(String tenantID) {
        this.tenantID = tenantID;
    }

    public String getID() {
        return id;
    }

    public void setID(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
