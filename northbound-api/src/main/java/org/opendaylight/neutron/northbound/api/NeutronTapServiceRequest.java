/*
 * Copyright (c) 2017 Intel, Corp. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.northbound.api;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.opendaylight.neutron.spi.NeutronTapService;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
@SuppressFBWarnings("URF_UNREAD_FIELD")
public final class NeutronTapServiceRequest implements INeutronRequest<NeutronTapService> {
    @XmlElement(name = "tap_service")
    NeutronTapService singleton;

    @XmlElement(name = "tap_services")
    List<NeutronTapService> bulkRequest;

    NeutronTapServiceRequest() {
    }

    NeutronTapServiceRequest(List<NeutronTapService> bulk) {
        bulkRequest = bulk;
    }

    NeutronTapServiceRequest(NeutronTapService taas) {
        singleton = taas;
    }
}
