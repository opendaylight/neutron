/*
 * Copyright (c) 2013, 2015 IBM Corporation and others.  All rights reserved.
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
import org.opendaylight.neutron.spi.NeutronFloatingIP;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class NeutronFloatingIPRequest
    implements INeutronRequest<NeutronFloatingIP> {
    // See OpenStack Network API v2.0 Reference for description of
    // annotated attributes

    @XmlElement(name="floatingip")
    NeutronFloatingIP singletonFloatingIP;

    @XmlElement(name="floatingips")
    List<NeutronFloatingIP> bulkRequest;

    NeutronFloatingIPRequest() {
    }

    NeutronFloatingIPRequest(List<NeutronFloatingIP> bulk) {
        bulkRequest = bulk;
        singletonFloatingIP = null;
    }

    NeutronFloatingIPRequest(NeutronFloatingIP singleton) {
        bulkRequest = null;
        singletonFloatingIP = singleton;
    }

    @Override
    public List<NeutronFloatingIP> getBulk() {
        return bulkRequest;
    }

    @Override
    public NeutronFloatingIP getSingleton() {
        return singletonFloatingIP;
    }

    @Override
    public boolean isSingleton() {
        return (singletonFloatingIP != null);
    }
}
