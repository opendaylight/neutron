/*
 * Copyright (C) 2016 Intel, Corp.  All rights reserved.
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
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
class NeutronQosPolicy extends NeutronObject implements Serializable, INeutronObject {
    private static final long serialVersionUID = 1L;

    @XmlElement(name = "name")
    String qosPolicyName;

    @XmlElement(name = "description")
    String qosPolicyDescription;

    @XmlElement(defaultValue = "false", name = "shared")
    Boolean qosPolicyIsShared;

    public String getQosPolicyName() {
        return qosPolicyName;
    }

    public void setQosPolicyName(String QosPolicyName) {
        this.qosPolicyName = qosPolicyName;
    }

    public String getQosPolicyDescription() {
        return qosPolicyDescription;
    }

    public void setQosPolicyDescription(String qosPolicyDescription) {
        this.qosPolicyDescription = qosPolicyDescription;
    }

    public Boolean getQosPolicyShared() {
        return qosPolicyIsShared;
    }

    public void setQosPolicyShared(Boolean qosPolicyIsShared) {
        this.qosPolicyIsShared = qosPolicyIsShared;
    }

    public NeutronQosPolicy extractFields(List<String> fields) {
        NeutronQosPolicy ans = new NeutronQosPolicy();
        Iterator<String> i = fields.iterator();
        while (i.hasNext()) {
            String s = i.next();
            if (s.equals("qosPolicyName")) {
                ans.setQosPolicyName(this.getQosPolicyName());
            }
            if (s.equals("qosPolicyDescription")) {
                ans.setQosPolicyDescription(this.getQosPolicyDescription());
            }
            if (s.equals("shared")) {
                ans.setQosPolicyShared(qosPolicyIsShared);
            }
        }
            return ans;
        }
        @Override
        public String toString () {
            return "NeutronQosPolicy{" +
                    "qosPolicyUUID='" + uuid + '\'' +
                    ", qosPolicyName='" + qosPolicyName + '\'' +
                    ", qosPolicyDescription='" + qosPolicyDescription + '\'' +
                    ", qosPolicyIsShared=" + qosPolicyIsShared +
                    '}';
        }
    }