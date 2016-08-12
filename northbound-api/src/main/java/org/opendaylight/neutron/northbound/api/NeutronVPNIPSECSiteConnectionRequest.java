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
import org.opendaylight.neutron.spi.NeutronVPNIPSECSiteConnection;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class NeutronVPNIPSECSiteConnectionRequest implements INeutronRequest<NeutronVPNIPSECSiteConnection> {

    @XmlElement(name = "ipsec_site_connection")
    NeutronVPNIPSECSiteConnection singletonVPNIPSECSiteConnection;

    @XmlElement(name = "ipsec_site_connections")
    List<NeutronVPNIPSECSiteConnection> bulkVPNIPSECSiteConnections;

    NeutronVPNIPSECSiteConnectionRequest() {
    }

    NeutronVPNIPSECSiteConnectionRequest(NeutronVPNIPSECSiteConnection connection) {
        singletonVPNIPSECSiteConnection = connection;
        bulkVPNIPSECSiteConnections = null;
    }

    NeutronVPNIPSECSiteConnectionRequest(List<NeutronVPNIPSECSiteConnection> connections) {
        singletonVPNIPSECSiteConnection = null;
        bulkVPNIPSECSiteConnections = connections;
    }

    @Override
    public NeutronVPNIPSECSiteConnection getSingleton() {
        return singletonVPNIPSECSiteConnection;
    }

    @Override
    public List<NeutronVPNIPSECSiteConnection> getBulk() {
        return bulkVPNIPSECSiteConnections;
    }

    @Override
    public boolean isSingleton() {
        return (singletonVPNIPSECSiteConnection != null);
    }
}
