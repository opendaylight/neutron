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

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)

public class NeutronQosPolicy extends NeutronObject implements Serializable, INeutronObject {
    private static final long serialVersionUID = 1L;

    @XmlElement (name = "name")
    String qosPolicyName;

    @XmlElement (defaultValue = "false", name = "shared")
    String qosPolicyIsShared;

    @XmlElement (name = "rule_type")
    String qosRuleType;

    @XmlElement (name = "bandwidth_limit_rules")
    List<String> qosBwLimitRules;

    @XmlElement (name = "dscp_marking_rules")
    List<String> qosDscpRules;

    public String getQosPolicyName() { return qosPolicyName; }

    public void setQosPolicyName(String qosPolicyName) {this.qosPolicyName = qosPolicyName ;}

    public String getQosPolicyIsShared() {return qosPolicyIsShared; }

    public void setQosPolicyIsShared(String qosPolicyIsShared) {this.qosPolicyIsShared = qosPolicyIsShared; }

    public String getQosRuleType() {return qosRuleType; }

    public void setQosRuleType(String qosRuleType) {this.qosRuleType = qosRuleType; }

    public List<String> getQosBwLimitRules() {return qosBwLimitRules; }

    public void setQosBwLimitRules(List<String> qosBwLimitRules) {this.qosBwLimitRules = qosBwLimitRules; }

    public List<String> getQosDscpRules() {return qosDscpRules; }

    public void setQosDscpRules(List<String> qosDscpRules) {this.qosDscpRules = qosDscpRules; }

    public NeutronQosPolicy extractFields(List<String> fields) {
        NeutronQosPolicy ans = new  NeutronQosPolicy();
        Iterator<String> i = fields.iterator();
        while (i.hasNext()) {
            String s = i.next();
            if(s.equals("id")){
                ans.setID(this.getID());
            }
            if(s.equals("tenant_id")){
                ans.setTenantID(this.getTenantID());
            }
            if(s.equals("name")){
                ans.setQosPolicyName(this.getQosPolicyName());
            }
            if(s.equals("shared")){
                ans.setQosPolicyIsShared(this.getQosPolicyIsShared());
            }
            if(s.equals("rule_type")){
                ans.setQosRuleType(this.getQosRuleType());
            }
            if(s.equals("bandwidth_limit_rules")){
                List<String> qosBwRuleList = new ArrayList<String>();
                qosBwRuleList.addAll(this.getQosBwLimitRules());
                ans.setQosBwLimitRules(qosBwRuleList);
            }
            if(s.equals("dscp_marking_rules")){
                List<String> qosDscpRuleList = new ArrayList<String>();
                qosDscpRuleList.addAll(this.getQosDscpRules());
                ans.setQosBwLimitRules(qosDscpRuleList);
            }
        }
        return ans;
    }

    @Override
    public String toString() {
        return "NeutronQosPolicy{" +
            "qosPolicyUUID='" + uuid + '\'' +
            ", qosPolicyTenantID='" + tenantID + '\'' +
            ", qosPolicyName='" + qosPolicyName + '\'' +
            ", qosPolicyIsShared='" + qosPolicyIsShared + '\'' +
            ", qosRuleType='" + qosRuleType + '\'' +
            ", qosBwLimitRules='" + qosBwLimitRules + '\'' +
            ", qosDscpRules='" + qosDscpRules + '\'' +
            '}';
    }
}
