/*
 * Copyright (c) 2016 Intel, Corp. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.northbound.api;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.opendaylight.neutron.spi.NeutronQosPolicy;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class NeutronQosPolicyRequest implements INeutronRequest<NeutronQosPolicy> {

    @XmlElement(name = "policy")
    NeutronQosPolicy singletonQosPolicy;

    @XmlElement(name = "policies")
    List<NeutronQosPolicy> bulkRequest;

    NeutronQosPolicyRequest() {
    }

    NeutronQosPolicyRequest(List<NeutronQosPolicy> bulk) {
        bulkRequest = bulk;
        singletonQosPolicy = null;
    }

    NeutronQosPolicyRequest(NeutronQosPolicy qos) {
        singletonQosPolicy = qos;
    }

    @Override
    public List<NeutronQosPolicy> getBulk() {
        return bulkRequest;
    }

    @Override
    public NeutronQosPolicy getSingleton() {
        return singletonQosPolicy;
    }

    @Override
    public boolean isSingleton() {
        return (singletonQosPolicy != null);
    }
}
