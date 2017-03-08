/*
 * Copyright (c) 2017 Intel Corporation.  All rights reserved.
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

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public final class NeutronTapService extends NeutronBaseAttributes<NeutronTapService> implements Serializable {
    private static final long serialVersionUID = 1L;

    @XmlElement(name = "port_id")
    String tapServicePortID;

    @XmlElement(name = "status")
    String tapServiceStatus;

    public String getTapServicePortID() {
        return tapServicePortID;
    }

    public void setTapServicePortID(String port) {
        this.tapServicePortID = port;
    }

    public String getTapServiceStatus() {
        return tapServiceStatus;
    }

    public void setTapServiceStatus(String status) {
        this.tapServiceStatus = status;
    }

    @Override
    protected boolean extractField(String field, NeutronTapService ans) {
        switch (field) {
            case "port_id":
                ans.setTapServicePortID(this.getTapServicePortID());
                break;
            case "status":
                ans.setTapServiceStatus(this.getTapServiceStatus());
                break;
            default:
                return super.extractField(field, ans);
        }
        return true;
    }

    @Override
    public String toString() {
        return "NeutronTapService[" + "tapServiceUUID='" + uuid + '\'' + ", tapServiceTenantID='" + tenantID + '\''
                + ", tapServiceName='" + name + '\'' + ", tapServicePortID='" + tapServicePortID + '\''
                + ", tapServiceStatus='" + tapServiceStatus + '\'' + ']';
    }
}
