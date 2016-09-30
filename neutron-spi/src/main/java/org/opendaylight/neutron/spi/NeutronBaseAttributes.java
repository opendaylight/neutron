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
public abstract class NeutronBaseAttributes<T extends NeutronBaseAttributes> extends NeutronObject<T>
        implements Serializable, INeutronBaseAttributes<T> {
    private static final long serialVersionUID = 1L;

    @XmlElement(name = "name")
    String name;

    public NeutronBaseAttributes() {
        super();
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
    protected void extractField(String field, T ans) {
        super.extractField(field, ans);
        if (field.equals("name")) {
            ans.setName(this.getName());
        }
    }
}
