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

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)

public class NeutronQosBandWidthRule extends NeutronObject implements Serializable, INeutronObject {
    private static final long serialVersionUID = 1L;

    @XmlElement(name = "id")
    String qosBandWidthRuleID;

    @XmlElement(name = "policy_id")
    String qosPolicyID;

    @XmlElement(name = "max_kbps")
    String qosBandWidthMaxValue;

    @XmlElement(name =  "max_burst_kbps")
    String qosBandWidthMaxBurst;

    public String getQosBandWidthRuleID() {return qosBandWidthRuleID; }

    public void setQosBandWidthRuleID(String qosBandWidthRuleID) {this.qosBandWidthRuleID = qosBandWidthRuleID; }

    public String getQosPolicyID() {return qosPolicyID; }

    public void setQosPolicyID(String qosPolicyID) {this.qosPolicyID =qosPolicyID; }

    public String getQosBandWidthMaxValue() {return qosBandWidthMaxValue; }

    public void setQosBandWidthMaxValue(String qosBandWidthMaxValue) {this.qosBandWidthMaxValue = qosBandWidthMaxValue; }

    public String getQosBandWidthMaxBurst() {return qosBandWidthMaxBurst;}

    public void setQosBandWidthMaxBurst(String qosBandWidthMaxBurst) {this.qosBandWidthMaxBurst = qosBandWidthMaxBurst; }

    public NeutronQosBandWidthRule extractFields(List<String> fields) {
        NeutronQosBandWidthRule ans = new NeutronQosBandWidthRule();
        Iterator<String> i = fields.iterator();
        while(i.hasNext()) {
            String s = i.next();
            if(s.equals("id")) {
                ans.setQosBandWidthRuleID(this.getQosBandWidthRuleID());
            }
            if(s.equals("tenant_id")){
                ans.setTenantID(this.getTenantID());
            }
            if(s.equals("policy_id")){
                ans.setQosPolicyID(this.getQosPolicyID());
            }
            if(s.equals("max_kbps")){
                ans.setQosBandWidthMaxValue(this.getQosBandWidthMaxValue());
            }
            if(s.equals("max_burst_kbps")){
                ans.setQosBandWidthMaxBurst(this.getQosBandWidthMaxBurst());
            }
        }
        return ans;
    }

    @Override
    public String toString() {
        return "qosBandWidthRules{" +
            "qosBandWidthRuleUUID'" + uuid + '\'' +
            ", qosBandWidthRuleTenantID='" + tenantID + '\'' +
            ", qosBandWidthRulePolicyID='" + qosPolicyID + '\'' +
            ", qosBandWidthMaxValue='" + qosBandWidthMaxValue + '\'' +
            ", qosBandWidthMaxBurst='" + qosBandWidthMaxBurst +
            '}';
    }
    @Override
    public void initDefaults(){

    }


}
