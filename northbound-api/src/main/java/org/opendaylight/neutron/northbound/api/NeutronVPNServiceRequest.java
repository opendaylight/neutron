/*
 * Copyright (c) 2015 IBM Corporation and others.  All rights reserved.
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
import org.opendaylight.neutron.spi.NeutronVPNService;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class NeutronVPNServiceRequest implements INeutronRequest<NeutronVPNService> {

    @XmlElement(name = "vpnservice")
    NeutronVPNService singletonVPNService;

    @XmlElement(name = "vpnservices")
    List<NeutronVPNService> bulkVPNServices;

    NeutronVPNServiceRequest() {
    }

    NeutronVPNServiceRequest(NeutronVPNService service) {
        singletonVPNService = service;
        bulkVPNServices = null;
    }

    NeutronVPNServiceRequest(List<NeutronVPNService> services) {
        singletonVPNService = null;
        bulkVPNServices = services;
    }

    @Override
    public NeutronVPNService getSingleton() {
        return singletonVPNService;
    }

    @Override
    public List<NeutronVPNService> getBulk() {
        return bulkVPNServices;
    }

    @Override
    public boolean isSingleton() {
        return (singletonVPNService != null);
    }
}
