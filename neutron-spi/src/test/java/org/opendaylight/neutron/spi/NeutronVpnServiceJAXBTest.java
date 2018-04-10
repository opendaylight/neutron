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

public class NeutronVpnServiceJAXBTest {

    private static final String NEUTRON_VPN_SERVICE_SOURCE_JSON = "{"
            + "\"router_id\": \"ec8619be-0ba8-4955-8835-3b49ddb76f89\", " + "\"status\": \"PENDING_CREATE\", "
            + "\"name\": \"myservice\", " + "\"admin_state_up\": true, "
            + "\"subnet_id\": \"f4fb4528-ed93-467c-a57b-11c7ea9f963e\", "
            + "\"tenant_id\": \"ccb81365fe36411a9011e90491fe1330\", "
            + "\"id\": \"9faaf49f-dd89-4e39-a8c6-101839aa49bc\", " + "\"description\": \"Updated description\" }";

    @Test
    public void test_NeutronVPNService_JAXB() throws JAXBException {
        NeutronVpnService testObject = JaxbTestHelper.jaxbUnmarshall(NeutronVpnService.class,
                NEUTRON_VPN_SERVICE_SOURCE_JSON);
        Assert.assertEquals("NeutronVpnService JAXB Test 1: Testing router id failed",
                "ec8619be-0ba8-4955-8835-3b49ddb76f89", testObject.getRouterUUID());

        Assert.assertEquals("NeutronVpnService JAXB Test 2: Testing status failed", "PENDING_CREATE",
                testObject.getStatus());

        Assert.assertEquals("NeutronVpnService JAXB Test 3: Testing name failed", "myservice",
                testObject.getName());

        Assert.assertEquals("NeutronVpnService JAXB Test 4: Testing admin state up failed", true,
                testObject.getAdminStateUp());

        Assert.assertEquals("NeutronVpnService JAXB Test 5: Testing Subnet UUID failed",
                "f4fb4528-ed93-467c-a57b-11c7ea9f963e", testObject.getSubnetUUID());

        Assert.assertEquals("NeutronVpnService JAXB Test 6: Testing Tenant Id failed",
                "ccb81365fe36411a9011e90491fe1330", testObject.getTenantID());

        Assert.assertEquals("NeutronVpnService JAXB Test 7: Testing id failed",
                "9faaf49f-dd89-4e39-a8c6-101839aa49bc", testObject.getID());
    }
}
