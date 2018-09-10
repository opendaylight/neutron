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
public final class NeutronRouterInterface extends NeutronObject<NeutronRouterInterface> {
    private static final long serialVersionUID = 1L;

    // See OpenStack Network API v2.0 Reference for description of
    // annotated attributes

    @XmlElement(name = "subnet_id")
    String subnetUUID;

    @XmlElement(name = "port_id")
    String portUUID;

    public NeutronRouterInterface() {
    }

    public NeutronRouterInterface(String subnetUUID, String portUUID) {
        this.subnetUUID = subnetUUID;
        this.portUUID = portUUID;
    }

    public String getSubnetUUID() {
        return subnetUUID;
    }

    public void setSubnetUUID(String subnetUUID) {
        this.subnetUUID = subnetUUID;
    }

    public String getPortUUID() {
        return portUUID;
    }

    public void setPortUUID(String portUUID) {
        this.portUUID = portUUID;
    }

    @Override
    protected boolean extractField(String field, NeutronRouterInterface ans) {
        switch (field) {
            case "subnet_id":
                ans.setSubnetUUID(this.getSubnetUUID());
                break;
            case "port_id":
                ans.setPortUUID(this.getPortUUID());
                break;
            default:
                return super.extractField(field, ans);
        }
        return true;
    }

    @Override
    public String toString() {
        return "NeutronRouterInterface [" + "subnetUUID=" + subnetUUID + ", portUUID=" + portUUID + ", id=" + uuid
                + ", tenantID=" + getTenantID() + "]";
    }
}
