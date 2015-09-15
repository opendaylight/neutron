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
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * OpenStack Neutron v2.0 Firewall as a service
 * (FWaaS) bindings. See OpenStack Network API
 * v2.0 Reference for description of  the fields.
 * The implemented fields are as follows:
 *
 * id             uuid-str
 * tenant_id      uuid-str
 * name           String
 * description    String
 * shared         Boolean
 * firewall_rules List
 * audited        Boolean
 * http://docs.openstack.org/api/openstack-network/2.0/openstack-network.pdf
 *
 */

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)

public class NeutronFirewallPolicy extends NeutronObject implements Serializable, INeutronObject {
    private static final long serialVersionUID = 1L;

    @XmlElement (name = "name")
    String firewallPolicyName;

    @XmlElement (name = "description")
    String firewallPolicyDescription;

    @XmlElement (defaultValue = "false", name = "shared")
    Boolean firewallPolicyIsShared;

    @XmlElement (name = "firewall_rules")
    List<String> firewallPolicyRules;

    @XmlElement (defaultValue = "false", name = "audited")
    Boolean firewallPolicyIsAudited;

    public Boolean getFirewallPolicyIsAudited() {
        return firewallPolicyIsAudited;
    }

    public void setFirewallPolicyIsAudited(Boolean firewallPolicyIsAudited) {
        this.firewallPolicyIsAudited = firewallPolicyIsAudited;
    }

    public void setFirewallPolicyRules(List<String> firewallPolicyRules) {
        this.firewallPolicyRules = firewallPolicyRules;
    }

    public List<String> getFirewallPolicyRules() {
        return firewallPolicyRules;
    }

    public Boolean getFirewallPolicyIsShared() {
        return firewallPolicyIsShared;
    }

    public void setFirewallPolicyIsShared(Boolean firewallPolicyIsShared) {
        this.firewallPolicyIsShared = firewallPolicyIsShared;
    }

    public String getFirewallPolicyDescription() {
        return firewallPolicyDescription;
    }

    public void setFirewallPolicyDescription(String firewallPolicyDescription) {
        this.firewallPolicyDescription = firewallPolicyDescription;
    }

    public String getFirewallPolicyName() {
        return firewallPolicyName;
    }

    public void setFirewallPolicyName(String firewallPolicyName) {
        this.firewallPolicyName = firewallPolicyName;
    }

    @Deprecated
    public String getFirewallPolicyTenantID() {
        return getTenantID();
    }

    @Deprecated
    public void setFirewallPolicyTenantID(String tenantID) {
        setTenantID(tenantID);
    }

    // @deprecated use getID()
    public String getFirewallPolicyUUID() {
        return getID();
    }

    // @deprecated use setID()
    public void setFirewallPolicyUUID(String uuid) {
        setID(uuid);
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
            if(s.equals("description")) {
                ans.setFirewallPolicyDescription(this.getFirewallPolicyDescription());
            }
            if (s.equals("shared")) {
                ans.setFirewallPolicyIsShared(firewallPolicyIsShared);
            }
            if (s.equals("firewall_rules")) {
                List<String> firewallRuleList = new ArrayList<String>();
                firewallRuleList.addAll(this.getFirewallPolicyRules());
                ans.setFirewallPolicyRules(firewallRuleList);
            }
            if (s.equals("audited")) {
                ans.setFirewallPolicyIsAudited(firewallPolicyIsAudited);
            }
        }
        return ans;
    }

    @Override
    public String toString() {
        return "NeutronFirewallPolicy{" +
            "firewallPolicyUUID='" + uuid + '\'' +
            ", firewallPolicyTenantID='" + tenantID + '\'' +
            ", firewallPolicyName='" + firewallPolicyName + '\'' +
            ", firewallPolicyDescription='" + firewallPolicyDescription + '\'' +
            ", firewallPolicyIsShared=" + firewallPolicyIsShared +
            ", firewallPolicyRules=" + firewallPolicyRules +
            ", firewallPolicyIsAudited='" + firewallPolicyIsAudited + '\'' +
            '}';
    }
}
