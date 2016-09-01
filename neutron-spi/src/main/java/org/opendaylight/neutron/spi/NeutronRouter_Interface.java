/*
 * Copyright (c) 2013, 2015 IBM Corporation and others.  All rights reserved.
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
public class NeutronRouter_Interface extends NeutronObject<NeutronRouter_Interface>
        implements Serializable, INeutronObject<NeutronRouter_Interface> {
    private static final long serialVersionUID = 1L;

    // See OpenStack Network API v2.0 Reference for description of
    // annotated attributes

    @XmlElement(name = "subnet_id")
    String subnetUUID;

    @XmlElement(name = "port_id")
    String portUUID;

    public NeutronRouter_Interface() {
    }

    public NeutronRouter_Interface(String subnetUUID, String portUUID) {
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
    public NeutronRouter_Interface extractFields(List<String> fields) {
        NeutronRouter_Interface ans = new NeutronRouter_Interface();
        Iterator<String> i = fields.iterator();
        while (i.hasNext()) {
            String s = i.next();
            if (s.equals("id")) {
                ans.setID(this.getID());
            }
            if (s.equals("tenant_id")) {
                ans.setTenantID(this.getTenantID());
            }
            if (s.equals("subnet_id")) {
                ans.setSubnetUUID(this.getSubnetUUID());
            }
            if (s.equals("port_id")) {
                ans.setPortUUID(this.getPortUUID());
            }
        }
        return ans;
    }

    @Override
    public String toString() {
        return "NeutronRouterInterface [" + "subnetUUID=" + subnetUUID + ", portUUID=" + portUUID + ", id=" + uuid
                + ", tenantID=" + tenantID + "]";
    }
}
