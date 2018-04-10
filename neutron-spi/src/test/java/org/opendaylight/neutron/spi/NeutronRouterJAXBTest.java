/*
 * Copyright (c) 2015 Tata Consultancy Services.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.spi;

import java.util.List;
import javax.xml.bind.JAXBException;
import org.junit.Assert;
import org.junit.Test;

public class NeutronRouterJAXBTest {

    private static final String NEUTRON_ROUTER_SOURCE_JSON = "{" + "\"id\": \"e9330b1f-a2ef-4160-a991-169e56ab17f5\", "
            + "\"name\": \"jaxb-test\", " + "\"admin_state_up\": false , " + "\"status\": \"ACTIVE\", "
            + "\"tenant_id\": \"aa902936679e4ea29bfe1158e3450a13\", "
            + "\"external_gateway_info\": {\"network_id\": \"e9330b1f-a2ef-4160-a991-169e56ab17f6\" }, "
            + "\"distributed\": false , " + "\"gw_port_id\": \"3b80198d-4f7b-4f77-9ef5-774d54e17127\", "
            + "\"routes\": [ { \"destination\":\"10.0.0.0/24\",\"nexthop\":\"1.1.1.1\" } ] }";

    @Test
    public void test_NeutronRouter_JAXB() throws JAXBException {
        NeutronRouter dummyObject = new NeutronRouter();

        NeutronRouter testObject = (NeutronRouter) JaxbTestHelper.jaxbUnmarshall(dummyObject,
                NEUTRON_ROUTER_SOURCE_JSON);

        NeutronRouterNetworkReference externalGatewayInfo = testObject.getExternalGatewayInfo();
        List<NeutronRoute> routes = testObject.getRoutes();

        Assert.assertEquals("NeutronRouter JAXB Test 1: Testing router_uuid failed",
                "e9330b1f-a2ef-4160-a991-169e56ab17f5", testObject.getID());

        Assert.assertEquals("NeutronRouter JAXB Test 2: Testing name failed", "jaxb-test", testObject.getName());

        Assert.assertEquals("NeutronRouter JAXB Test 3: Testing admin_state_up failed", false,
                testObject.getAdminStateUp());

        Assert.assertEquals("NeutronRouter JAXB Test 4: Testing status failed", "ACTIVE", testObject.getStatus());

        Assert.assertEquals("NeutronRouter JAXB Test 6: Testing externalGatewayInfo failed",
                "e9330b1f-a2ef-4160-a991-169e56ab17f6", externalGatewayInfo.getNetworkID());

        Assert.assertEquals("NeutronRouter JAXB Test 7: Testing distributed failed", false,
                testObject.getDistributed());

        Assert.assertEquals("NeutronRouter JAXB Test 8: Testing gateway_port_id failed",
                "3b80198d-4f7b-4f77-9ef5-774d54e17127", testObject.getGatewayPortId());

        Assert.assertEquals("NeutronRouter JAXB Test 9.1: Testing routes failed", 1, routes.size());

        Assert.assertEquals("NeutronRouter JAXB Test 9.2: Testing routes failed", "10.0.0.0/24",
                routes.get(0).getDestination());

        Assert.assertEquals("NeutronFloatingIp JAXB Test 5: Testing tenant_id failed",
                "aa902936679e4ea29bfe1158e3450a13", testObject.getTenantID());
    }
}
