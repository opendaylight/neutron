/*
 * Copyright (c) 2016 Intel Corporation.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.spi;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public final class NeutronQosPolicy extends NeutronObject<NeutronQosPolicy>
        implements Serializable, INeutronObject<NeutronQosPolicy> {
    private static final long serialVersionUID = 1L;

    @XmlElement(name = "name")
    String name;

    @XmlElement(defaultValue = "false", name = "shared")
    Boolean shared;

    @XmlElement(name = "bandwidth_limit_rules")
    List<NeutronQosBandwidthRule> bwLimitRules;

    @XmlElement(name = "dscp_marking_rules")
    List<NeutronQosDscpMarkingRule> dscpRules;

    public String getQosPolicyName() {
        return name;
    }

    public void setQosPolicyName(String qosPolicyName) {
        this.name = qosPolicyName;
    }

    public Boolean getPolicyIsShared() {
        return shared;
    }

    public void setPolicyIsShared(Boolean qosPolicyIsShared) {
        this.shared = qosPolicyIsShared;
    }

    public List<NeutronQosBandwidthRule> getBwLimitRules() {
        return bwLimitRules;
    }

    public void setQosBwLimitRules(List<NeutronQosBandwidthRule> qosBwLimitRules) {
        this.bwLimitRules = qosBwLimitRules;
    }

    public List<NeutronQosDscpMarkingRule> getDscpRules() {
        return dscpRules;
    }

    public void setDscpRules(List<NeutronQosDscpMarkingRule> qosDscpRules) {
        this.dscpRules = qosDscpRules;
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
                ans.setPolicyIsShared(this.getPolicyIsShared());
            }
            if (s.equals("bandwidth_limit_rules")) {
                List<NeutronQosBandwidthRule> qosBwRuleList = new ArrayList<>();
                qosBwRuleList.addAll(this.getBwLimitRules());
                ans.setQosBwLimitRules(qosBwRuleList);
            }
            if (s.equals("dscp_marking_rules")) {
                List<NeutronQosDscpMarkingRule> qosDscpRuleList = new ArrayList<>();
                qosDscpRuleList.addAll(this.getDscpRules());
                ans.setDscpRules(qosDscpRuleList);
            }
        }
        return ans;
    }

    @Override
    public String toString() {
        return "NeutronQosPolicy{" + "qosPolicyUUID='" + uuid + '\'' + ", qosPolicyTenantID='" + tenantID + '\''
                + ", qosPolicyName='" + name + '\'' + ", qosPolicyIsShared='" + shared + '\'' + ", qosBwLimitRules='"
                + bwLimitRules + '\'' + ", qosDscpRules='" + dscpRules + '\'' + '}';
    }
}
