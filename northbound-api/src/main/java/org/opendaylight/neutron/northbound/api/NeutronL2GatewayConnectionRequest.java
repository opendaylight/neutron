/*
 * Copyright (c) 2015 Hewlett-Packard Development Company and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.northbound.api;

import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import org.opendaylight.neutron.spi.NeutronL2GatewayConnection;

public class NeutronL2GatewayConnectionRequest {
    @XmlElement(name="l2gateway_connection")
    NeutronL2GatewayConnection singletonL2gatewayConnection;

    @XmlElement(name="l2gateway_connections")
    List<NeutronL2GatewayConnection> bulkRequest;

    NeutronL2GatewayConnectionRequest() {
    }

    NeutronL2GatewayConnectionRequest(NeutronL2GatewayConnection l2gatewayConnection) {
        this.singletonL2gatewayConnection = l2gatewayConnection;
    }

    NeutronL2GatewayConnectionRequest(List<NeutronL2GatewayConnection> bulk) {
        bulkRequest = bulk;
        singletonL2gatewayConnection = null;
    }

    public boolean isSingleton() {
        return (singletonL2gatewayConnection != null);
    }

    public NeutronL2GatewayConnection getSingleton() {
        return singletonL2gatewayConnection;
    }

}
