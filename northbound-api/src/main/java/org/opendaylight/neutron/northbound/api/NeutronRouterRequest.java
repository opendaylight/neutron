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
import org.opendaylight.neutron.spi.NeutronRouter;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)

public class NeutronRouterRequest implements INeutronRequest<NeutronRouter> {
    // See OpenStack Network API v2.0 Reference for description of
    // annotated attributes

    @XmlElement(name = "router")
    NeutronRouter singletonRouter;

    @XmlElement(name = "routers")
    List<NeutronRouter> bulkRequest;

    NeutronRouterRequest() {
    }

    NeutronRouterRequest(List<NeutronRouter> bulk) {
        bulkRequest = bulk;
        singletonRouter = null;
    }

    NeutronRouterRequest(NeutronRouter router) {
        singletonRouter = router;
    }

    @Override
    public List<NeutronRouter> getBulk() {
        return bulkRequest;
    }

    @Override
    public NeutronRouter getSingleton() {
        return singletonRouter;
    }

    @Override
    public boolean isSingleton() {
        return (singletonRouter != null);
    }
}
