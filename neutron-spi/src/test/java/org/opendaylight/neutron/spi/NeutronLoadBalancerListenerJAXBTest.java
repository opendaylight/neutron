/*
 * Copyright (c) 2015 Tata Consultancy Services.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.spi;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBException;
import org.junit.Assert;
import org.junit.Test;

public class NeutronLoadBalancerListenerJAXBTest {

    private static final String NeutronLoadBalancerListener_sourceJson = "{"
            + "\"id\": \"2f245a7b-796b-4f26-9cf9-9e82d248fda7\", "
            + "\"default_pool_id\": \"00005a7b-796b-4f26-9cf9-9e82d248fda7\", " + "\"connection_limit\": 5, "
            + "\"name\": \"LoadBalancerListener\", " + "\"admin_state_up\": \"false\", "
            + "\"description\": \"NeutronLoadBalancerListener_Description\", " + "\"protocol\": \"HTTP\", "
            + "\"protocol_port\": \"80\", " + "\"tenant_id\": \"11145a7b-796b-4f26-9cf9-9e82d248fda7\" }";

    @Test
    public void test_NeutronLoadBalancerListener_JAXB() throws JAXBException {
        NeutronLoadBalancerListener dummyObject = new NeutronLoadBalancerListener();

        NeutronLoadBalancerListener testObject = (NeutronLoadBalancerListener) JaxbTestHelper
                .jaxbUnmarshall(dummyObject, NeutronLoadBalancerListener_sourceJson);
        Assert.assertEquals("NeutronLoadBalancerListener JAXB Test 1: Testing id failed",
                "2f245a7b-796b-4f26-9cf9-9e82d248fda7", testObject.getID());

        Assert.assertEquals("NeutronLoadBalancerListener JAXB Test 2: Testing LoadBalancer defaulti pool ID failed",
                "00005a7b-796b-4f26-9cf9-9e82d248fda7", testObject.getNeutronLoadBalancerListenerDefaultPoolID());

        Assert.assertEquals(
                "NeutronLoadBalancerListener JAXB Test 3: Testing LoadBalancer listener ConnectionLimit failed", 5,
                (long) testObject.getNeutronLoadBalancerListenerConnectionLimit());

        Assert.assertEquals("NeutronLoadBalancerListener JAXB Test 4: Testing LoadBalancer listener name failed",
                "LoadBalancerListener", testObject.getName());

        Assert.assertFalse(
                "NeutronLoadBalancerListener JAXB Test 6: Testing LoadBalancer listener admin_state_up failed",
                testObject.getLoadBalancerListenerAdminStateIsUp());

        Assert.assertEquals(
                "NeutronLoadBalancerListener JAXB Test 7: Testing LoadBalancer listener VipAddress failed", "HTTP",
                testObject.getNeutronLoadBalancerListenerProtocol());

        Assert.assertEquals("NeutronLoadBalancerListener JAXB Test 8: Testing status failed", "80",
                testObject.getNeutronLoadBalancerListenerProtocolPort());

        Assert.assertEquals("NeutronLoadBalancerListener JAXB Test 9: Testing tenant_id failed",
                "11145a7b-796b-4f26-9cf9-9e82d248fda7", testObject.getTenantID());

        Neutron_ID neutron_id = new Neutron_ID();
        neutron_id.setID("2f245a7b-0000-4f26-9cf9-9e82d248fda7");
        List<Neutron_ID> neutronLoadBalancerListenerLoadBalancerIDs = new ArrayList<Neutron_ID>();
        neutronLoadBalancerListenerLoadBalancerIDs.add(neutron_id);
        testObject.setNeutronLoadBalancerListenerLoadBalancerIDs(neutronLoadBalancerListenerLoadBalancerIDs);
        Assert.assertEquals("NeutronLoadBalancerListener JAXB Test 10: Testing LoadBalancerIDs failed",
                "2f245a7b-0000-4f26-9cf9-9e82d248fda7",
                testObject.getNeutronLoadBalancerListenerLoadBalancerIDs().get(0).getID());
    }
}
