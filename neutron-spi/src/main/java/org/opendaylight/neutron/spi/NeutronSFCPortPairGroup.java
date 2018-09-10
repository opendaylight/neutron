/*
 * Copyright (c) 2016 Brocade Communications Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.neutron.spi;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public final class NeutronSFCPortPairGroup extends NeutronBaseAttributes<NeutronSFCPortPairGroup> {
    private static final long serialVersionUID = 1L;

    // See OpenStack Networking SFC (networking-sfc) Port Pair Group API v1.0
    // Reference for description of annotated attributes

    @XmlElement(name = "port_pairs")
    List<String> portPairs;

    public NeutronSFCPortPairGroup() {
    }

    public List<String> getPortPairs() {
        return portPairs;
    }

    public void setPortPairs(List<String> portPairs) {
        this.portPairs = portPairs;
    }

    @Override
    protected boolean extractField(String field, NeutronSFCPortPairGroup ans) {
        switch (field) {
            case "port_pairs":
                ans.setPortPairs(this.getPortPairs());
                break;
            default:
                return super.extractField(field, ans);
        }
        return true;
    }

    @Override
    public String toString() {
        return "NeutronSFCPortPairGroup[" + "tenantID='" + getTenantID() + '\'' + ", name='" + name + '\''
                + ", portPairs=" + portPairs + ']';
    }
}
