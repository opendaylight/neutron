/*
 * Copyright (c) 2015 Hewlett-Packard Development Company, L.P. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.northbound.api;

import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import org.opendaylight.neutron.spi.NeutronL2gateway;

public class NeutronL2gatewayRequest implements INeutronRequest<NeutronL2gateway> {

    @XmlElement(name = "l2_gateway")
    NeutronL2gateway singletonL2gateway;

    @XmlElement(name = "l2_gateways")
    List<NeutronL2gateway> bulkRequest;

    NeutronL2gatewayRequest() {
    }

    NeutronL2gatewayRequest(NeutronL2gateway l2gateway) {
        this.singletonL2gateway = l2gateway;
    }

    NeutronL2gatewayRequest(List<NeutronL2gateway> bulk) {
        bulkRequest = bulk;
        singletonL2gateway = null;
    }

    @Override
    public boolean isSingleton() {
        return (singletonL2gateway != null);
    }

    @Override
    public NeutronL2gateway getSingleton() {
        return singletonL2gateway;
    }

    @Override
    public List<NeutronL2gateway> getBulk() {
        return bulkRequest;
    }

}
