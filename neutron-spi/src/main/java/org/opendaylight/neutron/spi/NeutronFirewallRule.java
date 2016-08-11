/*
 * Copyright (C) 2015 Red Hat, Inc. and others.  All rights reserved.
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
 * tenant_id               uuid-str
 * name                    String
 * admin_state_up          Bool
 * shared                  Bool
 * firewall_policy_id      uuid-str
 * protocol                String
 * ip_version              Integer
 * source_ip_address       String (IP addr or CIDR)
 * destination_ip_address  String (IP addr or CIDR)
 * source_port_range_min    Integer
 * source_port_range_max    Integer
 * destination_port_range_min Integer
 * destination_port_range_max Integer
 * position                Integer
 * action                  String
 * enabled                 Bool
 * id                      uuid-str
 * http://docs.openstack.org/api/openstack-network/2.0/openstack-network.pdf
 *
 */

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)

public class NeutronFirewallRule extends NeutronObject implements Serializable, INeutronObject {
    private static final long serialVersionUID = 1L;

    @XmlElement(name = "name")
    String firewallRuleName;

    @XmlElement(defaultValue = "false", name = "shared")
    Boolean firewallRuleIsShared;

    @XmlElement(name = "firewall_policy_id")
    String firewallRulePolicyID;

    @XmlElement(name = "protocol")
    String firewallRuleProtocol;

    @XmlElement(name = "ip_version")
    Integer firewallRuleIpVer;

    @XmlElement(name = "source_ip_address")
    String firewallRuleSrcIpAddr;

    @XmlElement(name = "destination_ip_address")
    String firewallRuleDstIpAddr;

    @XmlElement(name = "source_port_range_min")
    Integer firewallRuleSrcPortRangeMin;

    @XmlElement(name = "source_port_range_max")
    Integer firewallRuleSrcPortRangeMax;

    @XmlElement(name = "destination_port_range_min")
    Integer firewallRuleDstPortRangeMin;

    @XmlElement(name = "destination_port_range_max")
    Integer firewallRuleDstPortRangeMax;

    @XmlElement(name = "position")
    Integer firewallRulePosition;

    @XmlElement(name = "action")
    String firewallRuleAction;

    @XmlElement(name = "enabled")
    Boolean firewallRuleIsEnabled;

    public Boolean getFirewallRuleIsEnabled() {
        return firewallRuleIsEnabled;
    }

    public void setFirewallRuleIsEnabled(Boolean firewallRuleIsEnabled) {
        this.firewallRuleIsEnabled = firewallRuleIsEnabled;
    }

    public String getFirewallRuleAction() {
        return firewallRuleAction;
    }

    public void setFirewallRuleAction(String firewallRuleAction) {
        this.firewallRuleAction = firewallRuleAction;
    }

    public Integer getFirewallRulePosition() {
        return firewallRulePosition;
    }

    public void setFirewallRulePosition(Integer firewallRulePosition) {
        this.firewallRulePosition = firewallRulePosition;
    }

    public Integer getFirewallRuleDstPortRangeMin() {
        return firewallRuleDstPortRangeMin;
    }

    public void setFirewallRuleDstPortRangeMin(Integer firewallRuleDstPortRangeMin) {
        this.firewallRuleDstPortRangeMin = firewallRuleDstPortRangeMin;
    }

    public Integer getFirewallRuleDstPortRangeMax() {
        return firewallRuleDstPortRangeMax;
    }

    public void setFirewallRuleDstPortRangeMax(Integer firewallRuleDstPortRangeMax) {
        this.firewallRuleDstPortRangeMax = firewallRuleDstPortRangeMax;
    }

    public Integer getFirewallRuleSrcPortRangeMin() {
        return firewallRuleSrcPortRangeMin;
    }

    public void setFirewallRuleSrcPortRangeMin(Integer firewallRuleSrcPortRangeMin) {
        this.firewallRuleSrcPortRangeMin = firewallRuleSrcPortRangeMin;
    }

    public Integer getFirewallRuleSrcPortRangeMax() {
        return firewallRuleSrcPortRangeMax;
    }

    public void setFirewallRuleSrcPortRangeMax(Integer firewallRuleSrcPortRangeMax) {
        this.firewallRuleSrcPortRangeMax = firewallRuleSrcPortRangeMax;
    }

    public String getFirewallRuleDstIpAddr() {
        return firewallRuleDstIpAddr;
    }

    public void setFirewallRuleDstIpAddr(String firewallRuleDstIpAddr) {
        this.firewallRuleDstIpAddr = firewallRuleDstIpAddr;
    }

    public String getFirewallRuleSrcIpAddr() {
        return firewallRuleSrcIpAddr;
    }

    public void setFirewallRuleSrcIpAddr(String firewallRuleSrcIpAddr) {
        this.firewallRuleSrcIpAddr = firewallRuleSrcIpAddr;
    }

    public Integer getFirewallRuleIpVer() {
        return firewallRuleIpVer;
    }

    public void setFirewallRuleIpVer(Integer firewallRuleIpVer) {
        this.firewallRuleIpVer = firewallRuleIpVer;
    }

    public String getFirewallRuleProtocol() {
        return firewallRuleProtocol;
    }

    public void setFirewallRuleProtocol(String firewallRuleProtocol) {
        this.firewallRuleProtocol = firewallRuleProtocol;
    }

    public String getFirewallRulePolicyID() {
        return firewallRulePolicyID;
    }

    public void setFirewallRulePolicyID(String firewallRulePolicyID) {
        this.firewallRulePolicyID = firewallRulePolicyID;
    }

    public Boolean getFirewallRuleIsShared() {
        return firewallRuleIsShared;
    }

    public void setFirewallRuleIsShared(Boolean firewallRuleIsShared) {
        this.firewallRuleIsShared = firewallRuleIsShared;
    }

    public String getFirewallRuleName() {
        return firewallRuleName;
    }

    public void setFirewallRuleName(String firewallRuleName) {
        this.firewallRuleName = firewallRuleName;
    }

    public NeutronFirewallRule extractFields(List<String> fields) {
        NeutronFirewallRule ans = new NeutronFirewallRule();
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
                ans.setFirewallRuleName(this.getFirewallRuleName());
            }
            if (s.equals("shared")) {
                ans.setFirewallRuleIsShared(firewallRuleIsShared);
            }
            if (s.equals("firewall_policy_id")) {
                ans.setFirewallRulePolicyID(this.getFirewallRulePolicyID());
            }
            if (s.equals("protocol")) {
                ans.setFirewallRuleProtocol(this.getFirewallRuleProtocol());
            }
            if (s.equals("source_ip_address")) {
                ans.setFirewallRuleSrcIpAddr(this.getFirewallRuleSrcIpAddr());
            }
            if (s.equals("destination_ip_address")) {
                ans.setFirewallRuleDstIpAddr(this.getFirewallRuleDstIpAddr());
            }
            if (s.equals("source_port_range_min")) {
                ans.setFirewallRuleSrcPortRangeMin(this.getFirewallRuleSrcPortRangeMin());
            }
            if (s.equals("source_port_range_max")) {
                ans.setFirewallRuleSrcPortRangeMax(this.getFirewallRuleSrcPortRangeMax());
            }
            if (s.equals("destination_port_range_min")) {
                ans.setFirewallRuleDstPortRangeMin(this.getFirewallRuleDstPortRangeMin());
            }
            if (s.equals("destination_port_range_max")) {
                ans.setFirewallRuleDstPortRangeMax(this.getFirewallRuleDstPortRangeMax());
            }
            if (s.equals("position")) {
                ans.setFirewallRulePosition(this.getFirewallRulePosition());
            }
            if (s.equals("action")) {
                ans.setFirewallRuleAction(this.getFirewallRuleAction());
            }
            if (s.equals("enabled")) {
                ans.setFirewallRuleIsEnabled(firewallRuleIsEnabled);
            }

        }
        return ans;
    }

    @Override
    public String toString() {
        return "firewallPolicyRules{" +
            "firewallRuleUUID='" + uuid + '\'' +
            ", firewallRuleTenantID='" + tenantID + '\'' +
            ", firewallRuleName='" + firewallRuleName + '\'' +
            ", firewallRuleIsShared=" + firewallRuleIsShared +
            ", firewallRulePolicyID=" + firewallRulePolicyID +
            ", firewallRuleProtocol='" + firewallRuleProtocol + '\'' +
            ", firewallRuleIpVer=" + firewallRuleIpVer +
            ", firewallRuleSrcIpAddr='" + firewallRuleSrcIpAddr + '\'' +
            ", firewallRuleDstIpAddr='" + firewallRuleDstIpAddr + '\'' +
            ", firewallRuleSrcPort=" + firewallRuleSrcPortRangeMin + ':' + firewallRuleSrcPortRangeMax +
            ", firewallRuleDstPort=" + firewallRuleDstPortRangeMin + ':' + firewallRuleDstPortRangeMax +
            ", firewallRulePosition=" + firewallRulePosition +
            ", firewallRuleAction='" + firewallRuleAction + '\'' +
            ", firewallRuleIsEnabled=" + firewallRuleIsEnabled +
            '}';
    }

    @Override
    public void initDefaults() {
    }
}
