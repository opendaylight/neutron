/*
 * Copyright (c) 2017 Intel Corporation.  All rights reserved.
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

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public final class NeutronTapFlow extends NeutronBaseAttributes<NeutronTapFlow> {
    private static final long serialVersionUID = 1L;

    @XmlElement(name = "tap_service_id")
    String tapServiceID;

    @XmlElement(name = "direction")
    String tapFlowDirection;

    @XmlElement(name = "source_port")
    String tapFlowSourcePort;

    public String getTapFlowServiceID() {
        return tapServiceID;
    }

    public void setTapFlowServiceID(String tapService) {
        this.tapServiceID = tapService;
    }

    public String getTapFlowDirection() {
        return tapFlowDirection;
    }

    public void setTapFlowDirection(String direction) {
        this.tapFlowDirection = direction;
    }

    public String getTapFlowSourcePort() {
        return tapFlowSourcePort;
    }

    public void setTapFlowSourcePort(String sourcePort) {
        this.tapFlowSourcePort = sourcePort;
    }

    @Override
    protected boolean extractField(String field, NeutronTapFlow ans) {
        switch (field) {
            case "source_port":
                ans.setTapFlowSourcePort(this.getTapFlowSourcePort());
                break;
            case "direction":
                ans.setTapFlowDirection(this.getTapFlowDirection());
                break;
            case "tap_service_id":
                ans.setTapFlowServiceID(this.getTapFlowServiceID());
                break;
            default:
                return super.extractField(field, ans);
        }
        return true;
    }

    @Override
    public String toString() {
        return "NeutronTapFlow[" + "tapFlowUUID='" + uuid + '\'' + ", tapFlowTenantID='" + tenantID + '\''
                + ", tapFlowName='" + name + '\'' + ", tapFlowServiceID='" + tapServiceID + '\''
                + ", tapFlowSourcePort='" + tapFlowSourcePort + '\''
                + ", tapFlowDirection='" + tapFlowDirection + '\'' + ']';
    }
}
