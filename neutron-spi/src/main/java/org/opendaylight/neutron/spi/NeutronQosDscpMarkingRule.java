/*
 * Copyright (c) 2016 Intel Corporation.  All rights reserved.
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
public final class NeutronQosDscpMarkingRule extends NeutronObject<NeutronQosDscpMarkingRule>
        implements Serializable, INeutronObject<NeutronQosDscpMarkingRule> {
    private static final Logger LOGGER = LoggerFactory.getLogger(NeutronQosDscpMarkingRule.class);
    private static final long serialVersionUID = 1L;

    @XmlElement(name = "dscp_mark")
    Short dscpMark;

    public Short getDscpMark() {
        return dscpMark;
    }

    public void setDscpMark(Short dscpMark) {
        this.dscpMark = dscpMark;
    }

    public NeutronQosDscpMarkingRule extractFields(List<String> fields) {
        NeutronQosDscpMarkingRule ans = new NeutronQosDscpMarkingRule();
        for (String s : fields) {
            if (extractField(s, ans)) {
                continue;
            }
            switch (s) {
                case "dscp_mark":
                    ans.setDscpMark(this.getDscpMark());
                    break;
                default:
                    LOGGER.warn("{} is not a NeutronQosDscpMarkingRule suitable field.", s);
                    break;
            }
        }
        return ans;
    }

    @Override
    public String toString() {
        return "qosDscpRules{" + "qosDscpRuleUUID='" + uuid + '\'' + ", qosDscpRuleTenantID='" + tenantID + '\''
                + ", qosDscpRuleDscpMark='" + dscpMark + '\'' + '}';
    }
}
