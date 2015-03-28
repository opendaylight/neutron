/*
 * Copyright IBM Corporation, 2015.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.spi;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class NeutronMeteringLabelRule implements Serializable {
    private static final long serialVersionUID = 1L;

    @XmlElement (name = "id")
    String meteringLabelRuleUUID;

    @XmlElement (name = "direction")
    String meteringLabelRuleDirection;

    @XmlElement (defaultValue = "false", name = "excluded")
    Boolean meteringLabelRuleExcluded;

    @XmlElement (name = "remote_ip_prefix")
    String meteringLabelRuleRemoteIPPrefix;

    @XmlElement (name = "metering_label_id")
    String meteringLabelRuleLabelID;

    /*
     *  getters and setters
     */

    public String getMeteringLabelRuleUUID() {
        return meteringLabelRuleUUID;
    }

    public void setMeteringLabelRuleUUID(String uuid) {
        this.meteringLabelRuleUUID = uuid;
    }

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
    public NeutronMeteringLabelRule() { }

    @Override
    public String toString() {
        return "NeutronMeteringLabelRule [id=" + meteringLabelRuleUUID +
            ", direction=" + meteringLabelRuleDirection +
            ", excluded=" + meteringLabelRuleExcluded +
            ", remote_ip_prefix=" + meteringLabelRuleRemoteIPPrefix +
            ", metering_label_id=" + meteringLabelRuleLabelID + "]";
    }
}
