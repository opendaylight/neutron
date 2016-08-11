/*
 * Copyright (c) 2015 Intel Corporation  All rights reserved.
 * Copyright (c) 2015 Isaku Yamahata  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.spi;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.yang.types.rev130715.Uuid;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class NeutronObject extends Neutron_ID implements Serializable, INeutronObject {
    private static final long serialVersionUID = 1L;

    @XmlElement (name = "tenant_id")
    String tenantID;

    public NeutronObject() {
        super();
    }

    @Override
    public String getTenantID() {
        if (tenantID != null && tenantID.isEmpty()) {
            // Bug 4775 - Treat empty string tenantId as null, so no attempt is made
            //            to turn it into a uuid.
            return null;
        }
        return tenantID;
    }

    @Override
    public void setTenantID(String tenantID) {
        this.tenantID = tenantID;
    }

    @Override
    @JsonIgnore
    public void setTenantID(Uuid tenantID) {
        this.tenantID = tenantID.getValue().replace("-", "");
    }

    @Override
    public String toString() {
        return "NeutronObject [id=" + uuid + ", tenantID=" + tenantID + "]";
    }

    @Override
    public void initDefaults() {
    }
}
