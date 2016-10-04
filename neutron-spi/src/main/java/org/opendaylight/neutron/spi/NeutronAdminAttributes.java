/*
 * Copyright (c) 2016 Intel Corporation  All rights reserved.
 * Copyright (c) 2016 Isaku Yamahata  All rights reserved.
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
public abstract class NeutronAdminAttributes<T extends NeutronAdminAttributes> extends NeutronBaseAttributes<T>
        implements Serializable, INeutronAdminAttributes<T> {
    private static final long serialVersionUID = 1L;

    @XmlElement(defaultValue = "true", name = "admin_state_up")
    Boolean adminStateUp;

    @XmlElement(name = "status")
    String status;

    public NeutronAdminAttributes() {
        super();
    }

    public Boolean getAdminStateUp() {
        return adminStateUp;
    }

    public void setAdminStateUp(Boolean adminStateUp) {
        this.adminStateUp = adminStateUp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    protected void extractField(String field, T ans) {
        super.extractField(field, ans);
        if (field.equals("admin_state_up")) {
            ans.setAdminStateUp(this.getAdminStateUp());
        }
        if (field.equals("status")) {
            ans.setStatus(this.getStatus());
        }
    }
}
