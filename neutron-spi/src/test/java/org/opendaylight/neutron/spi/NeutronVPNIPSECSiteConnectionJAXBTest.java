/*
 * Copyright (c) 2015 Tata Consultancy Services.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.spi;

import java.util.List;
import org.junit.Assert;
import org.junit.Test;

public class NeutronVPNIPSECSiteConnectionJAXBTest {

    private static final String NeutronVPNIPSECSiteConnection_sourceJson = "{"
            + "\"id\": \"cbc152a0-7e93-4f98-9f04-b085a4bf2511\", "
            + "\"tenant_id\": \"ccb81365fe36411a9011e90491fe1330\", " + "\"name\": \"myvpn\", "
            + "\"description\": \"Updated description\", " + "\"peer_address\": \"172.24.4.226\", "
            + "\"peer_id\": \"172.24.4.226\", " + "\"peer_cidrs\": [\"10.1.0.0/24\"], " + "\"route_mode\": \"static\", "
            + "\"mtu\": 1500 ," + "\"auth_mode\": \"psk\", " + "\"psk\": \"secret\","
            + "\"initiator\": \"bi-directional\", " + "\"admin_state_up\": true , " + "\"status\": \"PENDING_CREATE\", "
            + "\"ikepolicy_id\": \"bf5612ac-15fb-460c-9b3d-6453da2fafa2\", "
            + "\"ipsecpolicy_id\": \"8ba867b2-67eb-4835-bb61-c226804a1584\", "
            + "\"vpnservice_id\": \"c2f3178d-5530-4c4a-89fc-050ecd552636\", " + "\"dpd\": { " + "\"action\": \"hold\", "
            + "\"interval\": 30 ," + "\"timeout\": 120 " + "} }";

    @Test
    public void test_NeutronVPNIPSECSiteConnection_JAXB() {
        NeutronVPNIPSECSiteConnection dummyObject = new NeutronVPNIPSECSiteConnection();
        try {

            NeutronVPNIPSECSiteConnection testObject = (NeutronVPNIPSECSiteConnection) JaxbTestHelper
                    .jaxbUnmarshall(dummyObject, NeutronVPNIPSECSiteConnection_sourceJson);
            Assert.assertEquals("NeutronVPNIPSECSiteConnection JAXB Test 1: Testing id failed",
                    "cbc152a0-7e93-4f98-9f04-b085a4bf2511", testObject.getID());

            Assert.assertEquals("NeutronVPNIPSECSiteConnection JAXB Test 2: Testing tenant id failed",
                    "ccb81365fe36411a9011e90491fe1330", testObject.getTenantID());

            Assert.assertEquals("NeutronVPNIPSECSiteConnection JAXB Test 3: Testing name failed", "myvpn",
                    testObject.getName());

            Assert.assertEquals("NeutronVPNIPSECSiteConnection JAXB Test 5: Testing peer address failed",
                    "172.24.4.226", testObject.getPeerAddress());

            Assert.assertEquals("NeutronVPNIPSECSiteConnection JAXB Test 6: Testing peer id failed", "172.24.4.226",
                    testObject.getPeerID());

            List<String> peerCidrs = testObject.getPeerCidrs();

            Assert.assertEquals("NeutronVPNIPSECSiteConnection JAXB Test 7: Testing peerCidrs size failed", 1,
                    peerCidrs.size());

            Assert.assertEquals("NeutronVPNIPSECSiteConnection Test 8.1: Testing peerCidrs value failed", "10.1.0.0/24",
                    peerCidrs.get(0));

            Assert.assertEquals("NeutronVPNIPSECSiteConnection JAXB Test 8.2: Testing list value failed", "static",
                    testObject.getRouteMode());

            Assert.assertEquals("NeutronVPNIPSECSiteConnection JAXB Test 9: Testing mtu failed", 1500,
                    (long) testObject.getMtu());

            Assert.assertEquals("NeutronVPNIPSECSiteConnection JAXB Test 10: Testing authmode failed", "psk",
                    testObject.getAuthMode());

            Assert.assertEquals("NeutronVPNIPSECSiteConnection JAXB Test 11: Testing presharedkey failed", "secret",
                    testObject.getPreSharedKey());

            Assert.assertEquals("NeutronVPNIPSECSiteConnection JAXB Test 12: Testing initiator failed",
                    "bi-directional", testObject.getInitiator());

            Assert.assertEquals("NeutronVPNIPSECSiteConnection JAXB Test 13: Testing Admin state failed", true,
                    testObject.getAdminStateUp());

            Assert.assertEquals("NeutronVPNIPSECSiteConnection JAXB Test 14: Testing status failed", "PENDING_CREATE",
                    testObject.getStatus());

            Assert.assertEquals("NeutronVPNIPSECSiteConnection JAXB Test 15: Testing IkePolicyID failed",
                    "bf5612ac-15fb-460c-9b3d-6453da2fafa2", testObject.getIkePolicyID());

            Assert.assertEquals("NeutronVPNIPSECSiteConnection JAXB Test 16: Testing IpsecPolicyID failed",
                    "8ba867b2-67eb-4835-bb61-c226804a1584", testObject.getIpsecPolicyID());

            Assert.assertEquals("NeutronVPNIPSECSiteConnection JAXB Test 17: Testing VpnServiceID failed",
                    "c2f3178d-5530-4c4a-89fc-050ecd552636", testObject.getVpnServiceID());

            Assert.assertEquals("NeutronVPNIPSECSiteConnection JAXB Test 18.1: Testing DeadPeerDetection failed",
                    "hold", testObject.getDeadPeerDetection().getAction());

            Assert.assertEquals("NeutronVPNIPSECSiteConnection JAXB Test 18.2: Testing DeadPeerDetection failed",
                    new Integer(30), testObject.getDeadPeerDetection().getInterval());

            Assert.assertEquals("NeutronVPNIPSECSiteConnection JAXB Test 18.3: Testing DeadPeerDetection failed",
                    new Integer(120), testObject.getDeadPeerDetection().getTimeout());
        } catch (Exception e) {
            Assert.fail("Tests failed");
        }
    }
}
