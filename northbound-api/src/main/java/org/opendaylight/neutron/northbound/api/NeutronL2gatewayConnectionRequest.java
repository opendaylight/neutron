/*
 * Copyright (c) 2015 Hewlett-Packard Development Company, L.P. and others.  All rights reserved.
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
import org.opendaylight.neutron.spi.NeutronL2gatewayConnection;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
@SuppressFBWarnings("URF_UNREAD_FIELD")
public final class NeutronL2gatewayConnectionRequest implements INeutronRequest<NeutronL2gatewayConnection> {
    @XmlElement(name = "l2gateway_connection")
    NeutronL2gatewayConnection singleton;

    @XmlElement(name = "l2gateway_connections")
    List<NeutronL2gatewayConnection> bulkRequest;

    NeutronL2gatewayConnectionRequest() {
    }

    NeutronL2gatewayConnectionRequest(NeutronL2gatewayConnection l2gatewayConnection) {
        this.singleton = l2gatewayConnection;
    }

    NeutronL2gatewayConnectionRequest(List<NeutronL2gatewayConnection> bulk) {
        bulkRequest = bulk;
    }
}
