/*
 * Copyright (c) 2015 IBM Corporation and others.  All rights reserved.
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
public final class NeutronPort_ExtraDHCPOption implements Serializable {
    private static final long serialVersionUID = 1L;

    @XmlElement(name = "opt_value")
    String value;

    @XmlElement(name = "opt_name")
    String name;

    @XmlElement(name = "ip_version", defaultValue = "4", required = false, nillable = true)
    Integer ipVersion = 4;

    public NeutronPort_ExtraDHCPOption() {
    }

    public NeutronPort_ExtraDHCPOption(String value, String name, Integer ipVersion) {
        this.value = value;
        this.name = name;
        this.ipVersion = ipVersion;
    }

    public String getValue() {
        return (value);
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return (name);
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getIpVersion() {
        return ipVersion;
    }

    public void setIpVersion(Integer ipVersion) {
        this.ipVersion = ipVersion;
    }
}
