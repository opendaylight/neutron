/*
 * Copyright (c) 2015 Tata Consultancy Services.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.spi;

import org.junit.Assert;
import org.junit.Test;

public class NeutronVPNDeadPeerDetectionJAXBTest {

    private static final String NeutronVPNDeadPeerDetection_sourceJson = "{" + "\"action\": \"hold\", "
            + "\"interval\": 30, " + "\"timeout\": 120 }";

    @Test
    public void test_NeutronVPNDeadPeerDetection_JAXB() {
        NeutronVPNDeadPeerDetection dummyObject = new NeutronVPNDeadPeerDetection();
        try {
            NeutronVPNDeadPeerDetection testObject = (NeutronVPNDeadPeerDetection) JaxbTestHelper
                    .jaxbUnmarshall(dummyObject, NeutronVPNDeadPeerDetection_sourceJson);
            Assert.assertEquals("NeutronVPNDeadPeerDetection JAXB Test 1: Testing action failed", "hold",
                    testObject.getAction());

            Assert.assertEquals("NeutronVPNDeadPeerDetection JAXB Test 2: Testing interval failed", new Integer(30),
                    testObject.getInterval());

            Assert.assertEquals("NeutronVPNDeadPeerDetection JAXB Test 3: Testing timeout failed", new Integer(120),
                    testObject.getTimeout());
        } catch (Exception e) {
            Assert.fail("Tests failed");
        }
    }
}
