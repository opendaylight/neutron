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
public class NeutronMeteringLabel extends NeutronObject implements Serializable, INeutronObject {
    private static final long serialVersionUID = 1L;

    @XmlElement (name = "name")
    String meteringLabelName;

    @XmlElement (defaultValue = "false", name = "shared")
    Boolean shared;

    /*
     * getters and setters
     */

    public String getMeteringLabelName() {
        return meteringLabelName;
    }

    public void setMeteringLabelName(String name) {
        this.meteringLabelName = name;
    }

    public Boolean getMeteringLabelShared() {
        return shared;
    }

    public void setMeteringLabelShared(Boolean shared) {
        this.shared = shared;
    }

    /*
     *  constructor
     */
    public NeutronMeteringLabel() { }

    @Override
    public String toString() {
        return "NeutronMeteringLabel [id=" + uuid +
            ", name=" + meteringLabelName +
            ", tenant_id=" + tenantID +
            ", shared=" + shared +
            "]";
    }

    /**
     * This method copies selected fields from the object and returns them
     * as a new object, suitable for marshaling.
     *
     * @param fields
     *            List of attributes to be extracted
     * @return a NeutronMeteringLabel object with only the selected fields
     * populated
     */
    public NeutronMeteringLabel extractFields(List<String> fields) {
        NeutronMeteringLabel ans = new NeutronMeteringLabel();
        Iterator<String> i = fields.iterator();
        while (i.hasNext()) {
            String s = i.next();
            if (s.equals("id")) {
                ans.setID(this.getID());
            }
            if (s.equals("name")) {
                ans.setMeteringLabelName(this.getMeteringLabelName());
            }
            if (s.equals("tenant_id")) {
                ans.setTenantID(this.getTenantID());
            }
            if (s.equals("shared")) {
                ans.setMeteringLabelShared(this.getMeteringLabelShared());
            }
        }
        return ans;
    }
}
