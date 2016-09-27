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

/**
 * OpenStack Neutron v2.0 Security Group bindings.
 * See OpenStack Network API v2.0 Reference for description of
 * annotated attributes. The current fields are as follows:
 * <p>
 * id                   uuid-str unique ID for the security group.
 * name                 String name of the security group.
 * tenant_id            uuid-str Owner of security rule..
 */

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public final class NeutronSecurityGroup extends NeutronObject<NeutronSecurityGroup>
        implements Serializable, INeutronObject<NeutronSecurityGroup> {
    private static final long serialVersionUID = 1L;

    @XmlElement(name = "name")
    String securityGroupName;

    public NeutronSecurityGroup() {
    }

    public String getSecurityGroupName() {
        return securityGroupName;
    }

    public void setSecurityGroupName(String securityGroupName) {
        this.securityGroupName = securityGroupName;
    }

    public NeutronSecurityGroup extractFields(List<String> fields) {
        NeutronSecurityGroup ans = new NeutronSecurityGroup();
        for (String s : fields) {
            extractField(s, ans);
            if (s.equals("name")) {
                ans.setSecurityGroupName(this.getSecurityGroupName());
            }
        }
        return ans;
    }

    @Override
    public String toString() {
        return "NeutronSecurityGroup{" + "securityGroupUUID='" + uuid + '\'' + ", securityGroupName='"
                + securityGroupName + '\'' + ", securityGroupTenantID='" + tenantID + '\'' + "]";
    }

    @Override
    public void initDefaults() {
        //TODO verify no defaults values are nessecary required.
    }
}
