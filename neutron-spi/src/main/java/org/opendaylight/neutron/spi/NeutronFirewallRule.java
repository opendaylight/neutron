/*
 * Copyright (C) 2015 Red Hat, Inc. and others.  All rights reserved.
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

/**
 * OpenStack Neutron v2.0 Firewall as a service
 * (FWaaS) bindings. See OpenStack Network API
 * v2.0 Reference for description of  the fields.
 * The implemented fields are as follows:
 *
 * <p>
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
public final class NeutronFirewallRule extends NeutronBaseAttributes<NeutronFirewallRule> implements Serializable {
    private static final long serialVersionUID = 1L;

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

    @Override
    protected boolean extractField(String field, NeutronFirewallRule ans) {
        switch (field) {
            case "shared":
                ans.setFirewallRuleIsShared(firewallRuleIsShared);
                break;
            case "firewall_policy_id":
                ans.setFirewallRulePolicyID(this.getFirewallRulePolicyID());
                break;
            case "protocol":
                ans.setFirewallRuleProtocol(this.getFirewallRuleProtocol());
                break;
            case "source_ip_address":
                ans.setFirewallRuleSrcIpAddr(this.getFirewallRuleSrcIpAddr());
                break;
            case "destination_ip_address":
                ans.setFirewallRuleDstIpAddr(this.getFirewallRuleDstIpAddr());
                break;
            case "source_port_range_min":
                ans.setFirewallRuleSrcPortRangeMin(this.getFirewallRuleSrcPortRangeMin());
                break;
            case "source_port_range_max":
                ans.setFirewallRuleSrcPortRangeMax(this.getFirewallRuleSrcPortRangeMax());
                break;
            case "destination_port_range_min":
                ans.setFirewallRuleDstPortRangeMin(this.getFirewallRuleDstPortRangeMin());
                break;
            case "destination_port_range_max":
                ans.setFirewallRuleDstPortRangeMax(this.getFirewallRuleDstPortRangeMax());
                break;
            case "position":
                ans.setFirewallRulePosition(this.getFirewallRulePosition());
                break;
            case "action":
                ans.setFirewallRuleAction(this.getFirewallRuleAction());
                break;
            case "enabled":
                ans.setFirewallRuleIsEnabled(firewallRuleIsEnabled);
                break;
            default:
                return super.extractField(field, ans);
        }
        return true;
    }

    @Override
    public String toString() {
        return "firewallPolicyRules{" + "firewallRuleUUID='" + uuid + '\'' + ", firewallRuleTenantID='" + getTenantID()
                + '\'' + ", firewallRuleName='" + name + '\'' + ", firewallRuleIsShared="
                + firewallRuleIsShared + ", firewallRulePolicyID=" + firewallRulePolicyID + ", firewallRuleProtocol='"
                + firewallRuleProtocol + '\'' + ", firewallRuleIpVer=" + firewallRuleIpVer + ", firewallRuleSrcIpAddr='"
                + firewallRuleSrcIpAddr + '\'' + ", firewallRuleDstIpAddr='" + firewallRuleDstIpAddr + '\''
                + ", firewallRuleSrcPort=" + firewallRuleSrcPortRangeMin + ':' + firewallRuleSrcPortRangeMax
                + ", firewallRuleDstPort=" + firewallRuleDstPortRangeMin + ':' + firewallRuleDstPortRangeMax
                + ", firewallRulePosition=" + firewallRulePosition + ", firewallRuleAction='" + firewallRuleAction
                + '\'' + ", firewallRuleIsEnabled=" + firewallRuleIsEnabled + '}';
    }
}
