/*
 * Copyright (c) 2016 Brocade Communications Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.neutron.spi;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.yang.types.rev130715.Uuid;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)

public class NeutronSFCPortPairGroup extends Neutron_ID implements Serializable, INeutronObject {
    private static final long serialVersionUID = 1L;

    // See OpenStack Networking SFC (networking-sfc) Port Pair Group API v1.0
    // Reference for description of annotated attributes

    @XmlElement(name = "tenant_id")
    String tenantID;

    @XmlElement(name = "name")
    String name;

    @XmlElement(name = "port_pairs")
    List<String> portPairs;

    public NeutronSFCPortPairGroup() {
    }

    @Override
    public String getTenantID() {
        return tenantID;
    }

    @Override
    public void setTenantID(String tenantID) {
        this.tenantID = tenantID;
    }

    @Override @JsonIgnore
    public void setTenantID(Uuid tenantID) {
        this.tenantID = tenantID.getValue().replace("-", "");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getPortPairs() {
        return portPairs;
    }

    public void setPortPairs(List<String> portPairs) {
        this.portPairs = portPairs;
    }

    @Override
    public void initDefaults() {
    }

    /**
     * This method copies selected fields from the object and returns them
     * as a new object, suitable for marshaling.
     *
     * @param fields List of attributes to be extracted
     * @return an OpenStack Neutron SFC Port Pair Group object with only the selected fields
     * populated
     */

    public NeutronSFCPortPairGroup extractFields(List<String> fields) {
        NeutronSFCPortPairGroup ans = new NeutronSFCPortPairGroup();
        Iterator<String> i = fields.iterator();
        while (i.hasNext()) {
            String s = i.next();
            if (s.equals("id")) {
                ans.setID(this.getID());
            }
            if (s.equals("tenant_id")) {
                ans.setTenantID(this.getTenantID());
            }
            if (s.equals("name")) {
                ans.setName(this.getName());
            }
            if (s.equals("port_pairs")) {
                ans.setPortPairs(this.getPortPairs());
            }
        }
        return ans;
    }

    @Override
    public String toString() {
        return "NeutronSFCPortPairGroup[" +
                "tenantID='" + tenantID + '\'' +
                ", name='" + name + '\'' +
                ", portPairs=" + portPairs +
                ']';
    }
}
