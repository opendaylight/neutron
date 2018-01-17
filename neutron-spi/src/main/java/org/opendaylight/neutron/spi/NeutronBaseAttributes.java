/*
 * Copyright (c) 2016 Intel Corporation  All rights reserved.
 * Copyright (c) 2016 Isaku Yamahata  All rights reserved.
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
public abstract class NeutronBaseAttributes<T extends NeutronBaseAttributes<T>> extends NeutronObject<T>
        implements INeutronBaseAttributes<T> {
    private static final long serialVersionUID = 1L;

    @XmlElement(name = "name")
    String name;

    public NeutronBaseAttributes() {
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    protected boolean extractField(String field, T ans) {
        switch (field) {
            case "name":
                ans.setName(this.getName());
                return true;
            default:
                return super.extractField(field, ans);
        }
    }
}
