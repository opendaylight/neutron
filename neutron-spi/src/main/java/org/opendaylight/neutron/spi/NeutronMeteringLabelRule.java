/*
 * Copyright (c) 2015 IBM Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.spi;

import java.io.Serializable;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public final class NeutronMeteringLabelRule extends NeutronObject<NeutronMeteringLabelRule>
        implements Serializable, INeutronObject<NeutronMeteringLabelRule> {
    private static final Logger LOG = LoggerFactory.getLogger(NeutronMeteringLabelRule.class);
    private static final long serialVersionUID = 1L;

    @XmlElement(name = "direction")
    String meteringLabelRuleDirection;

    @XmlElement(defaultValue = "false", name = "excluded")
    Boolean meteringLabelRuleExcluded;

    @XmlElement(name = "remote_ip_prefix")
    String meteringLabelRuleRemoteIpPrefix;

    @XmlElement(name = "metering_label_id")
    String meteringLabelRuleLabelID;

    /*
     *  getters and setters
     */

    public String getMeteringLabelRuleDirection() {
        return meteringLabelRuleDirection;
    }

    public void setMeteringLabelRuleDirection(String direction) {
        this.meteringLabelRuleDirection = direction;
    }

    public Boolean getMeteringLabelRuleExcluded() {
        return meteringLabelRuleExcluded;
    }

    public void setMeteringLabelRuleExcluded(Boolean excluded) {
        this.meteringLabelRuleExcluded = excluded;
    }

    public String getMeteringLabelRuleRemoteIpPrefix() {
        return meteringLabelRuleRemoteIpPrefix;
    }

    public void setMeteringLabelRuleRemoteIpPrefix(String prefix) {
        this.meteringLabelRuleRemoteIpPrefix = prefix;
    }

    public String getMeteringLabelRuleLabelID() {
        return meteringLabelRuleLabelID;
    }

    public void setMeteringLabelRuleLabelID(String meteringLabelID) {
        this.meteringLabelRuleLabelID = meteringLabelID;
    }

    /*
     *  constructor
     */
    public NeutronMeteringLabelRule() {
    }

    @Override
    public String toString() {
        return "NeutronMeteringLabelRule [id=" + uuid + ", tenantID=" + tenantID + ", direction="
                + meteringLabelRuleDirection + ", excluded=" + meteringLabelRuleExcluded + ", remote_ip_prefix="
                + meteringLabelRuleRemoteIpPrefix + ", metering_label_id=" + meteringLabelRuleLabelID + "]";
    }

    /**
     * This method copies selected fields from the object and returns them
     * as a new object, suitable for marshaling.
     *
     * @param fields
     *            List of attributes to be extracted
     * @return a NeutronMeteringLabelRule object with only the selected fields
     *             populated
     */
    public NeutronMeteringLabelRule extractFields(List<String> fields) {
        NeutronMeteringLabelRule ans = new NeutronMeteringLabelRule();
        for (String s : fields) {
            if (extractField(s, ans)) {
                continue;
            }
            switch (s) {
                case "direction":
                    ans.setMeteringLabelRuleDirection(this.getMeteringLabelRuleDirection());
                    break;
                case "excluded":
                    ans.setMeteringLabelRuleExcluded(this.getMeteringLabelRuleExcluded());
                    break;
                case "remote_ip_prefix":
                    ans.setMeteringLabelRuleRemoteIpPrefix(this.getMeteringLabelRuleRemoteIpPrefix());
                    break;
                case "metering_label_id":
                    ans.setMeteringLabelRuleLabelID(this.getMeteringLabelRuleLabelID());
                    break;
                default:
                    LOG.warn("{} is not a NeutronMeteringLabelRule suitable field.", s);
                    break;
            }
        }
        return ans;
    }
}
