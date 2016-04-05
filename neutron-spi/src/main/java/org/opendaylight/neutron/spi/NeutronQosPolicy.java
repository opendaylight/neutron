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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@XmlRootElement @XmlAccessorType(XmlAccessType.NONE)

public class NeutronQosPolicy extends NeutronObject implements Serializable, INeutronObject {
    private static final long serialVersionUID = 1L;

    @XmlElement(name = "name") String qosPolicyName;

    @XmlElement(defaultValue = "false", name = "shared") Boolean qosPolicyIsShared;

    @XmlElement(name = "bandwidth_limit_rules") List<NeutronQosBandwidthRule> qosBwLimitRules;

    @XmlElement(name = "dscp_marking_rules") List<NeutronQosDscpMarkingRule> qosDscpRules;

    public String getQosPolicyName() {
        return qosPolicyName;
    }

    public void setQosPolicyName(String qosPolicyName) {
        this.qosPolicyName = qosPolicyName;
    }

    public Boolean getQosPolicyIsShared() {
        return qosPolicyIsShared;
    }

    public void setQosPolicyIsShared(Boolean qosPolicyIsShared) {
        this.qosPolicyIsShared = qosPolicyIsShared;
    }

    public List<NeutronQosBandwidthRule> getQosBwLimitRules() {
        return qosBwLimitRules;
    }

    public void setQosBwLimitRules(List<NeutronQosBandwidthRule> qosBwLimitRules) {
        this.qosBwLimitRules = qosBwLimitRules;
    }

    public List<NeutronQosDscpMarkingRule> getQosDscpRules() {
        return qosDscpRules;
    }

    public void setQosDscpRules(List<NeutronQosDscpMarkingRule> qosDscpRules) {
        this.qosDscpRules = qosDscpRules;
    }

    public NeutronQosPolicy extractFields(List<String> fields) {
        NeutronQosPolicy ans = new NeutronQosPolicy();
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
                ans.setQosPolicyName(this.getQosPolicyName());
            }
            if (s.equals("shared")) {
                ans.setQosPolicyIsShared(this.getQosPolicyIsShared());
            }
            if (s.equals("bandwidth_limit_rules")) {
                List<NeutronQosBandwidthRule> qosBwRuleList =
                    new ArrayList<NeutronQosBandwidthRule>();
                qosBwRuleList.addAll(this.getQosBwLimitRules());
                ans.setQosBwLimitRules(qosBwRuleList);
            }
            if (s.equals("dscp_marking_rules")) {
                List<NeutronQosDscpMarkingRule> qosDscpRuleList = new ArrayList<NeutronQosDscpMarkingRule>();
                qosDscpRuleList.addAll(this.getQosDscpRules());
                ans.setQosDscpRules(qosDscpRuleList);
            }
        }
        return ans;
    }

    @Override public String toString() {
        return "NeutronQosPolicy{" +
            "qosPolicyUUID='" + uuid + '\'' +
            ", qosPolicyTenantID='" + tenantID + '\'' +
            ", qosPolicyName='" + qosPolicyName + '\'' +
            ", qosPolicyIsShared='" + qosPolicyIsShared + '\'' +
            ", qosBwLimitRules='" + qosBwLimitRules + '\'' +
            ", qosDscpRules='" + qosDscpRules + '\'' +
            '}';
    }
}
