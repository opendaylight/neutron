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
public class NeutronMeteringLabel implements Serializable {
    private static final long serialVersionUID = 1L;

    @XmlElement (name = "id")
    String meteringLabelUUID;

    @XmlElement (name = "name")
    String meteringLabelName;

    @XmlElement (name = "tenant_id")
    String tenantID;

    @XmlElement (name = "description")
    String description;

    /*
     * getters and setters
     */

    public String getMeteringLabelUUID() {
        return meteringLabelUUID;
    }

    public void setMeteringLabelUUID(String uuid) {
        this.meteringLabelUUID = uuid;
    }

    public String getMeteringLabelName() {
        return meteringLabelName;
    }

    public void setMeteringLabelName(String name) {
        this.meteringLabelName = name;
    }

    public String getMeteringTenantID() {
        return tenantID;
    }

    public void setMeteringTenantID(String tenantID) {
        this.tenantID = tenantID;
    }

    public String getMeteringDescription() {
        return description;
    }

    public void setMeteringDescription(String description) {
        this.description = description;
    }

    /*
     *  constructor
     */
    public NeutronMeteringLabel() { }

    @Override
    public String toString() {
        return "NeutronMeteringLabel [id=" + meteringLabelUUID +
            ", name=" + meteringLabelName +
            ", description=" + description +
            ", tenant_id=" + tenantID + "]";
    }

}
