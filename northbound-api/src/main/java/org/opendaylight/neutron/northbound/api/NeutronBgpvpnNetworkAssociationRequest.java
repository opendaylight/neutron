/*
 * Copyright (c) 2020 Ericsson India Global Services Pvt Ltd. and others. All rights reserved.
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
import org.opendaylight.neutron.spi.NeutronBgpvpnNetworkAssociation;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class NeutronBgpvpnNetworkAssociationRequest implements INeutronRequest<NeutronBgpvpnNetworkAssociation> {

    @XmlElement(name = "bgpvpn_network_association")
    NeutronBgpvpnNetworkAssociation singleton;

    @XmlElement(name = "bgpvpn_network_associations")
    List<NeutronBgpvpnNetworkAssociation> bulkRequest;

    NeutronBgpvpnNetworkAssociationRequest() {
    }

    @edu.umd.cs.findbugs.annotations.SuppressWarnings("URF_UNREAD_FIELD")
    NeutronBgpvpnNetworkAssociationRequest(NeutronBgpvpnNetworkAssociation bgpvpnNetworkAssociation) {
        this.singleton = bgpvpnNetworkAssociation;
    }

    @edu.umd.cs.findbugs.annotations.SuppressWarnings("URF_UNREAD_FIELD")
    NeutronBgpvpnNetworkAssociationRequest(List<NeutronBgpvpnNetworkAssociation> bulk) {
        this.bulkRequest = bulk;
    }
}
