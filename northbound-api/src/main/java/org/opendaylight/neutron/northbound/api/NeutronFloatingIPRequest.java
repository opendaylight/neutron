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
public class NeutronFloatingIPRequest implements INeutronRequest<NeutronFloatingIP> {
    // See OpenStack Network API v2.0 Reference for description of
    // annotated attributes

    @XmlElement(name = "floatingip")
    NeutronFloatingIP singleton;

    @XmlElement(name = "floatingips")
    List<NeutronFloatingIP> bulkRequest;

    NeutronFloatingIPRequest() {
    }

    NeutronFloatingIPRequest(List<NeutronFloatingIP> bulk) {
        bulkRequest = bulk;
    }

    NeutronFloatingIPRequest(NeutronFloatingIP singleton) {
        this.singleton = singleton;
    }
}
