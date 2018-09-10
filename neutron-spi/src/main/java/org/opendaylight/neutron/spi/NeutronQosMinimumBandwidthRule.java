/*
 * Copyright (c) 2017 Intel Corporation.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.spi;
import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public final class NeutronQosMinimumBandwidthRule extends NeutronObject<NeutronQosMinimumBandwidthRule> {
    private static final long serialVersionUID = 1L;

    @XmlElement(name = "min_kbps")
    BigInteger minKbps;

    @XmlElement(defaultValue = "egress", name = "direction")
    String direction;

    public BigInteger getMinKbps() {
        return minKbps;
    }

    public void setMinKbps(BigInteger minKbps) {
        this.minKbps = minKbps;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    @Override
    public boolean extractField(String field, NeutronQosMinimumBandwidthRule ans) {
        switch (field) {
            case "min_kbps":
                ans.setMinKbps(this.getMinKbps());
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
        return "qosMinimumBandwidthRules{" + "qosMinimumBandwidthRuleUUID='" + uuid + '\'' + ","
                + " qosMinimumBandwidthRuleTenantID='" + getTenantID()
                + '\'' + ", qosMinimumBandwidthMinValue='" + minKbps
                + '\'' + ", qosMinimumBandwidthDirection='" + direction + '\''
                + '}';
    }
}
