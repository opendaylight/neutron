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
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public final class NeutronQosPolicy extends NeutronBaseAttributes<NeutronQosPolicy> implements Serializable {
    private static final long serialVersionUID = 1L;

    @XmlElement(defaultValue = "false", name = "shared")
    Boolean shared;

    @XmlElement(name = "bandwidth_limit_rules")
    List<NeutronQosBandwidthLimitRule> bwLimitRules;

    @XmlElement(name = "dscp_marking_rules")
    List<NeutronQosDscpMarkingRule> dscpRules;

    @XmlElement(name = "minimum_bandwidth_rules")
    List<NeutronQosMinimumBandwidthRule> minBwRules;

    public Boolean getPolicyIsShared() {
        return shared;
    }

    public void setPolicyIsShared(Boolean qosPolicyIsShared) {
        this.shared = qosPolicyIsShared;
    }

    public List<NeutronQosBandwidthLimitRule> getBwLimitRules() {
        return bwLimitRules;
    }

    public void setQosBwLimitRules(List<NeutronQosBandwidthLimitRule> qosBwLimitRules) {
        this.bwLimitRules = qosBwLimitRules;
    }

    public List<NeutronQosDscpMarkingRule> getDscpRules() {
        return dscpRules;
    }

    public void setDscpRules(List<NeutronQosDscpMarkingRule> qosDscpRules) {
        this.dscpRules = qosDscpRules;
    }

    public List<NeutronQosMinimumBandwidthRule> getMinBwRules() {
        return minBwRules;
    }

    public void setMinBwRules(List<NeutronQosMinimumBandwidthRule> qosMinBwRules) {
        this.minBwRules = qosMinBwRules;
    }

    @Override
    protected boolean extractField(String field, NeutronQosPolicy ans) {
        switch (field) {
            case "shared":
                ans.setPolicyIsShared(this.getPolicyIsShared());
                break;
            case "bandwidth_limit_rules":
                List<NeutronQosBandwidthLimitRule> qosBwRuleList = new ArrayList<>();
                qosBwRuleList.addAll(this.getBwLimitRules());
                ans.setQosBwLimitRules(qosBwRuleList);
                break;
            case "dscp_marking_rules":
                List<NeutronQosDscpMarkingRule> qosDscpRuleList = new ArrayList<>();
                qosDscpRuleList.addAll(this.getDscpRules());
                ans.setDscpRules(qosDscpRuleList);
                break;
            case "minimum_bandwidth_rules":
                List<NeutronQosMinimumBandwidthRule> qosMinBwRuleList = new ArrayList<>();
                qosMinBwRuleList.addAll(this.getMinBwRules());
                ans.setMinBwRules(qosMinBwRuleList);
                break;
            default:
                return super.extractField(field, ans);
        }
        return true;
    }

    @Override
    public String toString() {
        return "NeutronQosPolicy{" + "qosPolicyUUID='" + uuid + '\'' + ", qosPolicyTenantID='" + tenantID + '\''
                + ", qosPolicyName='" + name + '\'' + ", qosPolicyIsShared='" + shared + '\'' + ", qosBwLimitRules='"
                + bwLimitRules + '\'' + ", qosDscpRules='" + dscpRules + '\'' + ", qosMinBwRules='"
                + minBwRules + '\''
                + '}';
    }
}

