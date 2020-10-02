/*
 * Copyright (c) 2020 Ericsson India Global Services Pvt Ltd. and others. All rights reserved.
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
import org.opendaylight.neutron.spi.NeutronBgpvpnRouterAssociation;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class NeutronBgpvpnRouterAssociationRequest implements INeutronRequest<NeutronBgpvpnRouterAssociation> {

    @XmlElement(name = "bgpvpn_router_association")
    NeutronBgpvpnRouterAssociation singleton;

    @XmlElement(name = "bgpvpn_router_associations")
    List<NeutronBgpvpnRouterAssociation> bulkRequest;

    NeutronBgpvpnRouterAssociationRequest() {
    }

    @SuppressFBWarnings("URF_UNREAD_FIELD")
    NeutronBgpvpnRouterAssociationRequest(NeutronBgpvpnRouterAssociation bgpvpnRouterAssociation) {
        singleton = bgpvpnRouterAssociation;
    }

    @SuppressFBWarnings("URF_UNREAD_FIELD")
    NeutronBgpvpnRouterAssociationRequest(List<NeutronBgpvpnRouterAssociation> bulk) {
        bulkRequest = bulk;
    }
}
