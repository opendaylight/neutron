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
    List<NeutronQosBandwidthLimitRule> bandwidthLimitRules;

    @XmlElement(name = "dscp_marking_rules")
    List<NeutronQosDscpMarkingRule> dscpMarkingRules;

    @XmlElement(name = "minimum_bandwidth_rules")
    List<NeutronQosMinimumBandwidthRule> minimumBandwidthRules;

    public Boolean getPolicyIsShared() {
        return shared;
    }

    public void setPolicyIsShared(Boolean qosPolicyIsShared) {
        this.shared = qosPolicyIsShared;
    }

    public List<NeutronQosBandwidthLimitRule> getBandwidthLimitRules() {
        return bandwidthLimitRules;
    }

    public void setQosBandwidthLimitRules(List<NeutronQosBandwidthLimitRule> qosBandwidthLimitRules) {
        this.bandwidthLimitRules = qosBandwidthLimitRules;
    }

    public List<NeutronQosDscpMarkingRule> getDscpMarkingRules() {
        return dscpMarkingRules;
    }

    public void setDscpMarkingRules(List<NeutronQosDscpMarkingRule> qosDscpMarkingRules) {
        this.dscpMarkingRules = qosDscpMarkingRules;
    }

    public List<NeutronQosMinimumBandwidthRule> getMinimumBandwidthRules() {
        return minimumBandwidthRules;
    }

    public void setMinimumBandwidthRules(List<NeutronQosMinimumBandwidthRule> qosMinimumBandwidthRules) {
        this.minimumBandwidthRules = qosMinimumBandwidthRules;
    }

    @Override
    protected boolean extractField(String field, NeutronQosPolicy ans) {
        switch (field) {
            case "shared":
                ans.setPolicyIsShared(this.getPolicyIsShared());
                break;
            case "bandwidth_limit_rules":
                List<NeutronQosBandwidthLimitRule> qosBandwidthRuleList = new ArrayList<>();
                qosBandwidthRuleList.addAll(this.getBandwidthLimitRules());
                ans.setQosBandwidthLimitRules(qosBandwidthRuleList);
                break;
            case "dscp_marking_rules":
                List<NeutronQosDscpMarkingRule> qosDscpMarkingRuleList = new ArrayList<>();
                qosDscpMarkingRuleList.addAll(this.getDscpMarkingRules());
                ans.setDscpMarkingRules(qosDscpMarkingRuleList);
                break;
            case "minimum_bandwidth_rules":
                List<NeutronQosMinimumBandwidthRule> qosMinimumBandwidthRuleList = new ArrayList<>();
                qosMinimumBandwidthRuleList.addAll(this.getMinimumBandwidthRules());
                ans.setMinimumBandwidthRules(qosMinimumBandwidthRuleList);
                break;
            default:
                return super.extractField(field, ans);
        }
        return true;
    }

    @Override
    public String toString() {
        return "NeutronQosPolicy{" + "qosPolicyUUID='" + uuid + '\'' + ", qosPolicyTenantID='" + tenantID + '\''
                + ", qosPolicyName='" + name + '\'' + ", qosPolicyIsShared='" + shared + '\''
                + ", qosbandwidthLimitRules='" + bandwidthLimitRules + '\'' + ", qosDscpMarkingRules='"
                + dscpMarkingRules + '\'' + ", qosMinimumBandwidthRules='" + minimumBandwidthRules + '\''
                + '}';
    }
}
