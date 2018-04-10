/*
 * Copyright (c) 2015 Tata Consultancy Services.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.spi;

import javax.xml.bind.JAXBException;
import org.junit.Assert;
import org.junit.Test;

public class NeutronVpnDeadPeerDetectionJAXBTest {

    private static final String NEUTRON_VPN_DEAD_PEER_DETECTION_SOURCE_JSON = "{" + "\"action\": \"hold\", "
            + "\"interval\": 30, " + "\"timeout\": 120 }";

    @Test
    public void test_NeutronVPNDeadPeerDetection_JAXB() throws JAXBException {
        NeutronVpnDeadPeerDetection testObject = JaxbTestHelper
                .jaxbUnmarshall(NeutronVpnDeadPeerDetection.class, NEUTRON_VPN_DEAD_PEER_DETECTION_SOURCE_JSON);
        Assert.assertEquals("NeutronVpnDeadPeerDetection JAXB Test 1: Testing action failed", "hold",
                testObject.getAction());

        Assert.assertEquals("NeutronVpnDeadPeerDetection JAXB Test 2: Testing interval failed", new Integer(30),
                testObject.getInterval());

        Assert.assertEquals("NeutronVpnDeadPeerDetection JAXB Test 3: Testing timeout failed", new Integer(120),
                testObject.getTimeout());
    }
}
