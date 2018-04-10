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

public class NeutronRouterNetworkReferenceJAXBTest {

    private static final String NEUTRON_ROUTER_NETWORK_REFERENCE_SOURCE_JSON = "{"
            + "\"network_id\": \"e9330b1f-a2ef-4160-a991-169e56ab17f6\" ," + "\"enable_snat\": false , "
            + "\"external_fixed_ips\": [ { \"ip_address\":\"192.168.111.3\" , "
            + "\"subnet_id\": \"22b44fc2-4ffb-4de4-b0f9-69d58b37ae27\" } ]"
            + "}";

    @Test
    public void test_NeutronRouterNetworkReference_JAXB() throws JAXBException {
        NeutronRouterNetworkReference testObject = JaxbTestHelper
                .jaxbUnmarshall(NeutronRouterNetworkReference.class, NEUTRON_ROUTER_NETWORK_REFERENCE_SOURCE_JSON);

        List<NeutronIps> externalFixedIps = testObject.getExternalFixedIps();

        Assert.assertEquals("NeutronRouterNetworkReference JAXB Test 1: Testing network_id failed",
                "e9330b1f-a2ef-4160-a991-169e56ab17f6", testObject.getNetworkID());

        Assert.assertEquals("NeutronRouterNetworkReference JAXB Test 2: Testing enable_snat failed", false,
                testObject.getEnableSNAT());

        Assert.assertEquals(
                "NeutronRouterNetworkReference JAXB Test 3.1: Testing externalFixedIps list length failed", 1,
                externalFixedIps.size());

        Assert.assertEquals("NeutronRouterNetworkReference JAXB Test 3.2: Testing ip_address value failed",
                "192.168.111.3", externalFixedIps.get(0).ipAddress);

        Assert.assertEquals("NeutronRouterNetworkReference JAXB Test 3.3: Testing subnet_id value failed",
                "22b44fc2-4ffb-4de4-b0f9-69d58b37ae27", externalFixedIps.get(0).subnetUUID);
    }
}
