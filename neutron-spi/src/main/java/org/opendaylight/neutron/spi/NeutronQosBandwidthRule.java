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
import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;

@XmlRootElement @XmlAccessorType(XmlAccessType.NONE) public class NeutronQosBandwidthRule
    implements Serializable {
    private static final long serialVersionUID = 1L;
    @XmlElement(name = "id") String qosBandwidthRuleID;
    @XmlElement(name = "tenant_id") String tenantID;
    @XmlElement(name = "qos_policy_id") String qosPolicyID;
    @XmlElement(name = "max_kbps") BigInteger getQosBandwidthMaxKbpsValue;
    @XmlElement(name = "max_burst_kbps") BigInteger qosBandwidthMaxBurstKbps;

    public String getQosBandwidthRuleID() {
        return qosBandwidthRuleID;
    }

    public void setQosBandwidthRuleID(String qosBandwidthRuleID) {
        this.qosBandwidthRuleID = qosBandwidthRuleID;
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

    public BigInteger getQosBandwidthMaxKbpsValue() {
        return getQosBandwidthMaxKbpsValue;
    }

    public void setQosBandwidthMaxValue(BigInteger qosBandwidthMaxValue) {
        this.getQosBandwidthMaxKbpsValue = qosBandwidthMaxValue;
    }

    public BigInteger getQosBandwidthMaxBurstKbps() {
        return qosBandwidthMaxBurstKbps;
    }

    public void setQosBandwidthMaxBurst(BigInteger qosBandwidthMaxBurst) {
        this.qosBandwidthMaxBurstKbps = qosBandwidthMaxBurst;
    }

    public NeutronQosBandwidthRule extractFields(List<String> fields) {
        NeutronQosBandwidthRule ans = new NeutronQosBandwidthRule();
        Iterator<String> i = fields.iterator();
        while (i.hasNext()) {
            String s = i.next();
            if (s.equals("id")) {
                ans.setQosBandwidthRuleID(this.getQosBandwidthRuleID());
            }
            if (s.equals("tenant_id")) {
                ans.setTenantID(this.getTenantID());
            }
            if (s.equals("qos_policy_id")) {
                ans.setQosPolicyID(this.getQosPolicyID());
            }
            if (s.equals("max_kbps")) {
                ans.setQosBandwidthMaxValue(this.getQosBandwidthMaxKbpsValue());
            }
            if (s.equals("max_burst_kbps")) {
                ans.setQosBandwidthMaxBurst(this.getQosBandwidthMaxBurstKbps());
            }
        }
        return ans;
    }

    @Override public String toString() {
        return "qosBandwidthRules{" +
            "qosBandwidthRuleUUID'" + qosBandwidthRuleID + '\'' +
            ", qosBandwidthRuleTenantID='" + tenantID + '\'' +
            ", qosBandwidthRulePolicyID='" + qosPolicyID + '\'' +
            ", qosBandwidthMaxValue='" + getQosBandwidthMaxKbpsValue + '\'' +
            ", qosBandwidthMaxBurst='" + qosBandwidthMaxBurstKbps +
            '}';
    }
}
