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
import org.opendaylight.neutron.spi.NeutronL2Gateway;

public class NeutronL2GatewayRequest {

    @XmlElement(name="l2_gateway")
    NeutronL2Gateway singletonL2gateway;

    @XmlElement(name="l2_gateways")
    List<NeutronL2Gateway> bulkRequest;

    NeutronL2GatewayRequest() {
    }

    NeutronL2GatewayRequest(NeutronL2Gateway l2gateway) {
        this.singletonL2gateway = l2gateway;
    }

    NeutronL2GatewayRequest(List<NeutronL2Gateway> bulk) {
        bulkRequest = bulk;
        singletonL2gateway = null;
    }

    public boolean isSingleton() {
        return (singletonL2gateway != null);
    }

    public NeutronL2Gateway getSingleton() {
        return singletonL2gateway;
    }

    public List<NeutronL2Gateway> getBulk() {
        return bulkRequest;
    }
}
