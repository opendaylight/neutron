/*
 * Copyright (c) 2014, 2015 Red Hat, Inc. and others.  All rights reserved.
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
import org.opendaylight.neutron.spi.NeutronSecurityRule;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)

public class NeutronSecurityRuleRequest implements INeutronRequest<NeutronSecurityRule> {
    /**
     * See OpenStack Network API v2.0 Reference for a
     * description of annotated attributes and operations
     */

    @XmlElement(name = "security_group_rule")
    NeutronSecurityRule singletonSecurityRule;

    @XmlElement(name = "security_group_rules")
    List<NeutronSecurityRule> bulkRequest;

    NeutronSecurityRuleRequest() {
    }

    NeutronSecurityRuleRequest(List<NeutronSecurityRule> bulk) {
        bulkRequest = bulk;
        singletonSecurityRule = null;
    }

    NeutronSecurityRuleRequest(NeutronSecurityRule rule) {
        singletonSecurityRule = rule;
    }

    @Override
    public NeutronSecurityRule getSingleton() {
        return singletonSecurityRule;
    }

    @Override
    public boolean isSingleton() {
        return (singletonSecurityRule != null);
    }

    @Override
    public List<NeutronSecurityRule> getBulk() {
        return bulkRequest;
    }

}
