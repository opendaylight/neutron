/*
 * Copyright (c) 2015 Ericsson India Global Services Pvt Ltd. and others. All rights reserved.
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

import org.opendaylight.neutron.spi.NeutronBgpvpn;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class NeutronBgpvpnRequest implements INeutronRequest<NeutronBgpvpn> {
    // See OpenStack Network API v2.0 Reference for description of
    // annotated attributes

    @XmlElement(name="bgpvpn")
    NeutronBgpvpn singletonBgpvpn;

    @XmlElement(name="bgpvpns")
    List<NeutronBgpvpn> bulkRequest;


    NeutronBgpvpnRequest() {
    }

    NeutronBgpvpnRequest(NeutronBgpvpn bgpvpn) {
        singletonBgpvpn = bgpvpn;
    }

    NeutronBgpvpnRequest(List<NeutronBgpvpn> bulk) {
        bulkRequest = bulk;
        singletonBgpvpn = null;
    }

    @Override
    public NeutronBgpvpn getSingleton() {
        return singletonBgpvpn;
    }

    @Override
    public boolean isSingleton() {
        return (singletonBgpvpn != null);
    }

    @Override
    public List<NeutronBgpvpn> getBulk() {
        return bulkRequest;
    }
}
