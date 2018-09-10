/*
 * Copyright (C) 2014 Red Hat, Inc.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.spi;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * OpenStack Neutron v2.0 Firewall as a service
 * (FWaaS) bindings. See OpenStack Network API
 * v2.0 Reference for description of  the fields:
 * Implemented fields are as follows:
 *
 * <p>
 * id                 uuid-str
 * tenant_id          uuid-str
 * name               String
 * admin_state_up     Bool
 * shared             Bool
 * firewall_policy_id uuid-str
 * http://docs.openstack.org/api/openstack-network/2.0/openstack-network.pdf
 *
 */

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public final class NeutronFirewall extends NeutronBaseAttributes<NeutronFirewall> {
    private static final long serialVersionUID = 1L;

    @XmlElement(defaultValue = "true", name = "admin_state_up")
    Boolean firewallAdminStateIsUp;

    @XmlElement(defaultValue = "false", name = "shared")
    Boolean firewallIsShared;

    @XmlElement(name = "firewall_policy_id")
    String firewallPolicyID;

    public Boolean getFirewallAdminStateIsUp() {
        return firewallAdminStateIsUp;
    }

    public void setFirewallAdminStateIsUp(Boolean firewallAdminStateIsUp) {
        this.firewallAdminStateIsUp = firewallAdminStateIsUp;
    }

    public Boolean getFirewallIsShared() {
        return firewallIsShared;
    }

    public void setFirewallIsShared(Boolean firewallIsShared) {
        this.firewallIsShared = firewallIsShared;
    }

    public String getFirewallPolicyID() {
        return firewallPolicyID;
    }

    public void setFirewallPolicyID(String firewallPolicy) {
        this.firewallPolicyID = firewallPolicy;
    }

    @Override
    protected boolean extractField(String field, NeutronFirewall ans) {
        switch (field) {
            case "admin_state_up":
                ans.setFirewallAdminStateIsUp(firewallAdminStateIsUp);
                break;
            case "shared":
                ans.setFirewallIsShared(firewallIsShared);
                break;
            case "firewall_policy_id":
                ans.setFirewallPolicyID(this.getFirewallPolicyID());
                break;
            default:
                return super.extractField(field, ans);
        }
        return true;
    }

    @Override
    public String toString() {
        return "NeutronFirewall{" + "firewallUUID='" + uuid + '\'' + ", firewallTenantID='" + getTenantID() + '\''
                + ", firewallName='" + name + '\'' + ", firewallAdminStateIsUp=" + firewallAdminStateIsUp
                + ", firewallIsShared=" + firewallIsShared + ", firewallRulePolicyID=" + firewallPolicyID + '}';
    }
}
