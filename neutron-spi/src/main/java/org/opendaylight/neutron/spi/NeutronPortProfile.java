/*
 * Copyright (c) 2017 Mellanox Corporation.  All rights reserved.
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

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public final class NeutronPortProfile implements Serializable {
    private static final long serialVersionUID = 1L;

    @XmlElement(name = "capabilities")
    List<String> capabilities;

    public NeutronPortProfile() {
        this.capabilities = null;
    }

    public NeutronPortProfile(List<String> capabilities) {
        this.capabilities = capabilities;
    }

    public List<String> getCapabilities() {
        return capabilities;
    }

    public void setCapabilities(List<String> capabilities) {
        this.capabilities = capabilities;
    }
}
