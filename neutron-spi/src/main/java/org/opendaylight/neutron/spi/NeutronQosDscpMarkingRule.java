/*
 * Copyright (c) 2016 Intel Corporation.  All rights reserved.
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
public final class NeutronQosDscpMarkingRule extends NeutronObject<NeutronQosDscpMarkingRule>
        implements Serializable, INeutronObject<NeutronQosDscpMarkingRule> {
    private static final long serialVersionUID = 1L;

    @XmlElement(name = "dscp_mark")
    Short dscpMark;

    public Short getDscpMark() {
        return dscpMark;
    }

    public void setDscpMark(Short dscpMark) {
        this.dscpMark = dscpMark;
    }

    @Override
    protected boolean extractField(String field, NeutronQosDscpMarkingRule ans) {
        switch (field) {
            case "dscp_mark":
                ans.setDscpMark(this.getDscpMark());
                break;
            default:
                return super.extractField(field, ans);
        }
        return true;
    }

    @Override
    public String toString() {
        return "qosDscpMarkingRules{" + "qosDscpMarkingRuleUUID='" + uuid + '\'' + ", qosDscpMarkingRuleTenantID='"
            + getTenantID() + '\'' + ", qosDscpMarkingRuleDscpMark='" + dscpMark
            + '\''
            + '}';
    }
}
