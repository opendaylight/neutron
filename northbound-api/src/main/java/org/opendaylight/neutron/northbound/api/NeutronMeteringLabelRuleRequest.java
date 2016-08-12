/*
 * Copyright (c) 2013, 2015 IBM Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.northbound.api;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.opendaylight.neutron.spi.NeutronMeteringLabelRule;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)

public class NeutronMeteringLabelRuleRequest implements INeutronRequest<NeutronMeteringLabelRule> {
    // See OpenStack Network API v2.0 Reference for description of
    // annotated attributes

    @XmlElement(name = "metering_label_rule")
    NeutronMeteringLabelRule singletonMeteringLabelRule;

    @XmlElement(name = "metering_label_rules")
    List<NeutronMeteringLabelRule> bulkMeteringLabelRules;

    NeutronMeteringLabelRuleRequest() {
    }

    NeutronMeteringLabelRuleRequest(NeutronMeteringLabelRule rule) {
        singletonMeteringLabelRule = rule;
        bulkMeteringLabelRules = null;
    }

    NeutronMeteringLabelRuleRequest(List<NeutronMeteringLabelRule> bulk) {
        bulkMeteringLabelRules = bulk;
        singletonMeteringLabelRule = null;
    }

    @Override
    public NeutronMeteringLabelRule getSingleton() {
        return singletonMeteringLabelRule;
    }

    @Override
    public boolean isSingleton() {
        return (singletonMeteringLabelRule != null);
    }

    @Override
    public List<NeutronMeteringLabelRule> getBulk() {
        return bulkMeteringLabelRules;
    }

}
