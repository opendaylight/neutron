/*
 * Copyright Tata Consultancy Services, 2015.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.spi;

import org.junit.Assert;
import org.junit.Test;

import org.opendaylight.neutron.spi.NeutronVPNService;

public class NeutronVPNServiceJAXBTest {

    private static final String NeutronVPNService_sourceJson = "{" +
        "\"router_id\": \"ec8619be-0ba8-4955-8835-3b49ddb76f89\", " +
        "\"status\": \"PENDING_CREATE\", " +
        "\"name\": \"myservice\", " +
        "\"admin_state_up\": true, " +
        "\"subnet_id\": \"f4fb4528-ed93-467c-a57b-11c7ea9f963e\", " +
        "\"tenant_id\": \"ccb81365fe36411a9011e90491fe1330\", " +
        "\"id\": \"9faaf49f-dd89-4e39-a8c6-101839aa49bc\", " +
        "\"description\": \"Updated description\" }";

    @Test
    public void test_NeutronVPNService_JAXB() {
        NeutronVPNService dummyObject = new NeutronVPNService();
        try {
            NeutronVPNService testObject = (NeutronVPNService) JaxbTestHelper.jaxbUnmarshall(dummyObject, NeutronVPNService_sourceJson);
            Assert.assertEquals("NeutronVPNService JAXB Test 1: Testing router id failed",
                  "ec8619be-0ba8-4955-8835-3b49ddb76f89", testObject.getRouterUUID());

            Assert.assertEquals("NeutronVPNService JAXB Test 2: Testing status failed",
                  "PENDING_CREATE", testObject.getStatus());

            Assert.assertEquals("NeutronVPNService JAXB Test 3: Testing name failed",
                  "myservice", testObject.getName());

            Assert.assertEquals("NeutronVPNService JAXB Test 4: Testing admin state up failed",
                  true, testObject.getAdminStateUp());

            Assert.assertEquals("NeutronVPNService JAXB Test 5: Testing Subnet UUID failed",
                  "f4fb4528-ed93-467c-a57b-11c7ea9f963e", testObject.getSubnetUUID());

            Assert.assertEquals("NeutronVPNService JAXB Test 6: Testing Tenant Id failed",
                  "ccb81365fe36411a9011e90491fe1330", testObject.getTenantID());

            Assert.assertEquals("NeutronVPNService JAXB Test 7: Testing id failed",
                  "9faaf49f-dd89-4e39-a8c6-101839aa49bc", testObject.getID());
        }
        catch (Exception e) {
            Assert.fail("Tests failed");
        }
    }
}
