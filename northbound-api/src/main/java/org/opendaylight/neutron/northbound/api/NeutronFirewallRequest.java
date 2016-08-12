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
import org.opendaylight.neutron.spi.NeutronFirewall;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)

public class NeutronFirewallRequest implements INeutronRequest<NeutronFirewall> {
    /**
     * See OpenStack Network API v2.0 Reference for description of
     * http://docs.openstack.org/api/openstack-network/2.0/content/
     */

    @XmlElement(name = "firewall")
    NeutronFirewall singletonFirewall;

    @XmlElement(name = "firewalls")
    List<NeutronFirewall> bulkRequest;

    NeutronFirewallRequest() {
    }

    NeutronFirewallRequest(List<NeutronFirewall> bulk) {
        bulkRequest = bulk;
        singletonFirewall = null;
    }

    NeutronFirewallRequest(NeutronFirewall group) {
        singletonFirewall = group;
    }

    @Override
    public List<NeutronFirewall> getBulk() {
        return bulkRequest;
    }

    @Override
    public NeutronFirewall getSingleton() {
        return singletonFirewall;
    }

    @Override
    public boolean isSingleton() {
        return (singletonFirewall != null);
    }
}
