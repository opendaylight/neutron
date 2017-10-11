/*
 * Copyright (c) 2016, 2017 Intel Corporation.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.spi;

import java.io.Serializable;
import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public final class NeutronQosBandwidthLimitRule extends NeutronObject<NeutronQosBandwidthLimitRule>
        implements Serializable, INeutronObject<NeutronQosBandwidthLimitRule> {
    private static final long serialVersionUID = 1L;

    @XmlElement(name = "max_kbps")
    BigInteger maxKbps;

    @XmlElement(name = "max_burst_kbps")
    BigInteger maxBurstKbps;

    @XmlElement(defaultValue = "", name = "direction")
    String direction;

    public BigInteger getMaxKbps() {
        return maxKbps;
    }

    public void setMaxKbps(BigInteger maxKbps) {
        this.maxKbps = maxKbps;
    }

    public BigInteger getMaxBurstKbps() {
        return maxBurstKbps;
    }

    public void setMaxBurstKbps(BigInteger maxBurstKbps) {
        this.maxBurstKbps = maxBurstKbps;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    @Override
    public boolean extractField(String field, NeutronQosBandwidthLimitRule ans) {
        switch (field) {
            case "max_kbps":
                ans.setMaxKbps(this.getMaxKbps());
                break;
            case "max_burst_kbps":
                ans.setMaxBurstKbps(this.getMaxBurstKbps());
                break;
            case "direction":
                ans.setDirection(this.getDirection());
                break;
            default:
                return super.extractField(field, ans);
        }
        return true;
    }

    @Override
    public String toString() {
        return "qosBandwidthLimitRules{" + "qosBandwidthLimitRuleUUID='" + uuid + '\''
            + ", qosBandwidthLimitRuleTenantID='" + tenantID + '\'' + ", qosBandwidthLimitRuleMaxValue='" + maxKbps
            + '\'' + ", qosBandwidthLimitRuleMaxBurst='" + maxBurstKbps + '\'' + ",qosBandwidthLimitDirection='"
            + direction + '\''
            + '}';
    }
}
