/*
 * Copyright (c) 2016 Intel Corporation.  All rights reserved.
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
import java.util.Iterator;
import java.util.List;
import java.lang.*;

@XmlRootElement @XmlAccessorType(XmlAccessType.NONE)

public class NeutronQosDscpMarkingRule implements Serializable {
    private static final long serialVersionUID = 1L;

    @XmlElement(name = "id") String qosDscpRuleID;

    @XmlElement(name = "tenant_id") String tenantID;

    @XmlElement(name = "qos_policy_id") String qosPolicyID;

    @XmlElement(name = "dscp_mark") Short qosDscpMark;

    public String getQosDscpRuleID() {
        return qosDscpRuleID;
    }

    public void setQosDscpRuleID(String qosDscpRuleID) {
        this.qosDscpRuleID = qosDscpRuleID;
    }

    public String getTenantID() {
        return tenantID;
    }

    public void setTenantID(String tenantID) {
        this.tenantID = tenantID;
    }

    public String getQosPolicyID() {
        return qosPolicyID;
    }

    public void setQosPolicyID(String qosPolicyID) {
        this.qosPolicyID = qosPolicyID;
    }

    public Short getQosDscpMark() {
        return qosDscpMark;
    }

    public void setQosDscpMark(Short qosDscpMark) {
        this.qosDscpMark = qosDscpMark;
    }

    public NeutronQosDscpMarkingRule extractFields(List<String> fields) {
        NeutronQosDscpMarkingRule ans = new NeutronQosDscpMarkingRule();
        Iterator<String> i = fields.iterator();
        while (i.hasNext()) {
            String s = i.next();
            if (s.equals("id")) {
                ans.setQosDscpRuleID(this.getQosDscpRuleID());
            }
            if (s.equals("tenant_id")) {
                ans.setTenantID(this.getTenantID());
            }
            if (s.equals("qos_policy_id")) {
                ans.setQosPolicyID(this.getQosPolicyID());
            }
            if (s.equals("dscp_mark")) {
                ans.setQosDscpMark(this.getQosDscpMark());
            }
        }
        return ans;
    }

    @Override public String toString() {
        return "qosDscpRules{" +
            "qosDscpRuleUUID'" + qosDscpRuleID + '\'' +
            "qosDscpRuleTenantID= '" + tenantID + '\'' +
            "qosDscpRulePolicyID= '" + qosPolicyID + '\'' +
            "qosDscpRuleDscpMark= '" + qosDscpMark +
            '}';
    }
}
