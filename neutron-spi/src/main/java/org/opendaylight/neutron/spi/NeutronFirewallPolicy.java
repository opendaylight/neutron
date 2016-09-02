/*
 * Copyright (C) 2014 Red Hat, Inc.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.spi;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * OpenStack Neutron v2.0 Firewall as a service
 * (FWaaS) bindings. See OpenStack Network API
 * v2.0 Reference for description of  the fields.
 * The implemented fields are as follows:
 *
 * id             uuid-str
 * tenant_id      uuid-str
 * name           String
 * shared         Boolean
 * audited        Boolean
 * http://docs.openstack.org/api/openstack-network/2.0/openstack-network.pdf
 *
 */

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class NeutronFirewallPolicy extends NeutronObject<NeutronFirewallPolicy>
        implements Serializable, INeutronObject<NeutronFirewallPolicy> {
    private static final long serialVersionUID = 1L;

    @XmlElement(name = "name")
    String firewallPolicyName;

    @XmlElement(defaultValue = "false", name = "shared")
    Boolean firewallPolicyIsShared;

    @XmlElement(defaultValue = "false", name = "audited")
    Boolean firewallPolicyIsAudited;

    public Boolean getFirewallPolicyIsAudited() {
        return firewallPolicyIsAudited;
    }

    public void setFirewallPolicyIsAudited(Boolean firewallPolicyIsAudited) {
        this.firewallPolicyIsAudited = firewallPolicyIsAudited;
    }

    public Boolean getFirewallPolicyIsShared() {
        return firewallPolicyIsShared;
    }

    public void setFirewallPolicyIsShared(Boolean firewallPolicyIsShared) {
        this.firewallPolicyIsShared = firewallPolicyIsShared;
    }

    public String getFirewallPolicyName() {
        return firewallPolicyName;
    }

    public void setFirewallPolicyName(String firewallPolicyName) {
        this.firewallPolicyName = firewallPolicyName;
    }

    public NeutronFirewallPolicy extractFields(List<String> fields) {
        NeutronFirewallPolicy ans = new NeutronFirewallPolicy();
        Iterator<String> i = fields.iterator();
        while (i.hasNext()) {
            String s = i.next();
            if (s.equals("id")) {
                ans.setID(this.getID());
            }
            if (s.equals("tenant_id")) {
                ans.setTenantID(this.getTenantID());
            }
            if (s.equals("name")) {
                ans.setFirewallPolicyName(this.getFirewallPolicyName());
            }
            if (s.equals("shared")) {
                ans.setFirewallPolicyIsShared(firewallPolicyIsShared);
            }
            if (s.equals("audited")) {
                ans.setFirewallPolicyIsAudited(firewallPolicyIsAudited);
            }
        }
        return ans;
    }

    @Override
    public String toString() {
        return "NeutronFirewallPolicy{" + "firewallPolicyUUID='" + uuid + '\'' + ", firewallPolicyTenantID='" + tenantID
                + '\'' + ", firewallPolicyName='" + firewallPolicyName + '\'' + ", firewallPolicyIsShared="
                + firewallPolicyIsShared + ", firewallPolicyIsAudited='" + firewallPolicyIsAudited + '\'' + '}';
    }
}
