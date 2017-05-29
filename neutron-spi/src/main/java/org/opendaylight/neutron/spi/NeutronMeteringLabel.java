/*
 * Copyright (c) 2015 IBM Corporation and others.  All rights reserved.
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public final class NeutronMeteringLabel extends NeutronBaseAttributes<NeutronMeteringLabel> implements Serializable {
    private static final Logger LOG = LoggerFactory.getLogger(NeutronLoadBalancerPoolMember.class);
    private static final long serialVersionUID = 1L;

    @XmlElement(defaultValue = "false", name = "shared")
    Boolean shared;

    /*
     * getters and setters
     */

    public Boolean getMeteringLabelShared() {
        return shared;
    }

    public void setMeteringLabelShared(Boolean shared) {
        this.shared = shared;
    }

    /*
     *  constructor
     */
    public NeutronMeteringLabel() {
    }

    @Override
    public String toString() {
        return "NeutronMeteringLabel [id=" + uuid + ", name=" + name + ", tenant_id=" + tenantID
                + ", shared=" + shared + "]";
    }

    /**
     * This method copies selected fields from the object and returns them
     * as a new object, suitable for marshaling.
     *
     * @param fields
     *            List of attributes to be extracted
     * @return a NeutronMeteringLabel object with only the selected fields
     *             populated
     */
    public NeutronMeteringLabel extractFields(List<String> fields) {
        NeutronMeteringLabel ans = new NeutronMeteringLabel();
        for (String s : fields) {
            if (extractField(s, ans)) {
                continue;
            }
            switch (s) {
                case "shared":
                    ans.setMeteringLabelShared(this.getMeteringLabelShared());
                    break;
                default:
                    LOG.warn("{} is not a NeutronMeteringLabel suitable field.", s);
                    break;
            }
        }
        return ans;
    }
}
