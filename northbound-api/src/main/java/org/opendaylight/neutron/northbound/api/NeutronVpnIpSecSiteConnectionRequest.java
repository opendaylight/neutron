/*
 * Copyright (c) 2015 IBM Corporation and others.  All rights reserved.
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
import org.opendaylight.neutron.spi.NeutronVpnIpSecSiteConnection;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
@SuppressFBWarnings("URF_UNREAD_FIELD")
public final class NeutronVpnIpSecSiteConnectionRequest extends NeutronRequest<NeutronVpnIpSecSiteConnection> {
    @XmlElement(name = "ipsec_site_connection")
    NeutronVpnIpSecSiteConnection singleton;

    @XmlElement(name = "ipsec_site_connections")
    List<NeutronVpnIpSecSiteConnection> bulkRequest;

    NeutronVpnIpSecSiteConnectionRequest() {
    }

    NeutronVpnIpSecSiteConnectionRequest(NeutronVpnIpSecSiteConnection connection) {
        singleton = connection;
    }

    NeutronVpnIpSecSiteConnectionRequest(List<NeutronVpnIpSecSiteConnection> connections) {
        bulkRequest = connections;
    }
}
