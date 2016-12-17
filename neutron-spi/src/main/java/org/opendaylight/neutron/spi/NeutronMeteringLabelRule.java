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

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public final class NeutronMeteringLabelRule extends NeutronObject<NeutronMeteringLabelRule>
        implements Serializable, INeutronObject<NeutronMeteringLabelRule> {
    private static final long serialVersionUID = 1L;

    @XmlElement(name = "direction")
    String meteringLabelRuleDirection;

    @XmlElement(defaultValue = "false", name = "excluded")
    Boolean meteringLabelRuleExcluded;

    @XmlElement(name = "remote_ip_prefix")
    String meteringLabelRuleRemoteIPPrefix;

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

    public String getMeteringLabelRuleRemoteIPPrefix() {
        return meteringLabelRuleRemoteIPPrefix;
    }

    public void setMeteringLabelRuleRemoteIPPrefix(String prefix) {
        this.meteringLabelRuleRemoteIPPrefix = prefix;
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
                + meteringLabelRuleRemoteIPPrefix + ", metering_label_id=" + meteringLabelRuleLabelID + "]";
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
            extractField(s, ans);
            if (s.equals("direction")) {
                ans.setMeteringLabelRuleDirection(this.getMeteringLabelRuleDirection());
            }
            if (s.equals("excluded")) {
                ans.setMeteringLabelRuleExcluded(this.getMeteringLabelRuleExcluded());
            }
            if (s.equals("remote_ip_prefix")) {
                ans.setMeteringLabelRuleRemoteIPPrefix(this.getMeteringLabelRuleRemoteIPPrefix());
            }
            if (s.equals("metering_label_id")) {
                ans.setMeteringLabelRuleLabelID(this.getMeteringLabelRuleLabelID());
            }
        }
        return ans;
    }
}
