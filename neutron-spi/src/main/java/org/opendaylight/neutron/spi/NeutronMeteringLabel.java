/*
 * Copyright (c) 2015 IBM Corporation and others.  All rights reserved.
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
public final class NeutronMeteringLabel extends NeutronBaseAttributes<NeutronMeteringLabel> {
    private static final long serialVersionUID = 1L;

    @XmlElement(defaultValue = "false", name = "shared")
    Boolean shared;

    /*
     * getters and setters
     */

    public Boolean getMeteringLabelShared() {
        return shared;
    }

    public void setMeteringLabelShared(Boolean value) {
        this.shared = value;
    }

    /*
     *  constructor
     */
    public NeutronMeteringLabel() {
    }

    @Override
    public String toString() {
        return "NeutronMeteringLabel [id=" + uuid + ", name=" + name + ", tenant_id=" + getTenantID()
                + ", shared=" + shared + "]";
    }

    @Override
    protected boolean extractField(String field, NeutronMeteringLabel ans) {
        switch (field) {
            case "shared":
                ans.setMeteringLabelShared(this.getMeteringLabelShared());
                break;
            default:
                return super.extractField(field, ans);
        }
        return true;
    }
}
