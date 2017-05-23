/*
 * Copyright (C) 2014 Red Hat, Inc.  All rights reserved.
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

/**
 * OpenStack Neutron v2.0 Firewall as a service
 * (FWaaS) bindings. See OpenStack Network API
 * v2.0 Reference for description of  the fields.
 * The implemented fields are as follows:
 *
 * <p>
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
public final class NeutronFirewallPolicy extends NeutronBaseAttributes<NeutronFirewallPolicy> implements Serializable {
    private static final Logger LOGGER = LoggerFactory.getLogger(NeutronFirewallPolicy.class);
    private static final long serialVersionUID = 1L;

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

    public NeutronFirewallPolicy extractFields(List<String> fields) {
        NeutronFirewallPolicy ans = new NeutronFirewallPolicy();
        for (String s : fields) {
            if (extractField(s, ans)) {
                continue;
            }
            switch (s) {
                case "shared":
                    ans.setFirewallPolicyIsShared(firewallPolicyIsShared);
                    break;
                case "audited":
                    ans.setFirewallPolicyIsAudited(firewallPolicyIsAudited);
                    break;
                default:
                    LOGGER.warn("{} is not an NeutronFirewallPolicy suitable field.", s);
                    break;
            }
        }
        return ans;
    }

    @Override
    public String toString() {
        return "NeutronFirewallPolicy{" + "firewallPolicyUUID='" + uuid + '\'' + ", firewallPolicyTenantID='" + tenantID
                + '\'' + ", firewallPolicyName='" + name + '\'' + ", firewallPolicyIsShared="
                + firewallPolicyIsShared + ", firewallPolicyIsAudited='" + firewallPolicyIsAudited + '\'' + '}';
    }
}
