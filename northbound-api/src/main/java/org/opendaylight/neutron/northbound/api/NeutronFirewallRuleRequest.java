/*
 * Copyright (c) 2014, 2015 Red Hat, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.northbound.api;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.opendaylight.neutron.spi.NeutronFirewallRule;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
@SuppressFBWarnings("URF_UNREAD_FIELD")
public final class NeutronFirewallRuleRequest extends NeutronRequest<NeutronFirewallRule> {
    /**
     * See OpenStack Network API v2.0 Reference for description of the following link.
     * http://docs.openstack.org/api/openstack-network/2.0/content/
     *
     */

    @XmlElement(name = "firewall_rule")
    NeutronFirewallRule singleton;

    @XmlElement(name = "firewall_rules")
    List<NeutronFirewallRule> bulkRequest;

    NeutronFirewallRuleRequest() {
    }

    NeutronFirewallRuleRequest(List<NeutronFirewallRule> bulk) {
        bulkRequest = bulk;
    }

    NeutronFirewallRuleRequest(NeutronFirewallRule group) {
        singleton = group;
    }
}
