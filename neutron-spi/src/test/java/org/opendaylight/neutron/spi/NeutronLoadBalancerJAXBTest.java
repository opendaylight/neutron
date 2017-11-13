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

public class NeutronLoadBalancerJAXBTest {

    private static final String NEUTRON_LOAD_BALANCER_SOURCE_JSON = "{"
            + "\"id\": \"2f245a7b-796b-4f26-9cf9-9e82d248fda7\", " + "\"name\": \"NeutronLoadBalancer\", "
            + "\"description\": \"NeutronLoadBalancer_Description\", "
            + "\"admin_state_up\": \"false\", " + "\"vip_address\": \"10.0.0.3\", "
            + "\"vip_subnet_id\": \"d23abc8d-2991-4a55-ba98-2aaea84cc72f\", "
            + "\"tenant_id\": \"4969c491a3c74ee4af974e6d800c62de\" }";

    @Test
    public void test_NeutronLoadBalancer_JAXB() throws JAXBException {
        NeutronLoadBalancer dummyObject = new NeutronLoadBalancer();

        NeutronLoadBalancer testObject = (NeutronLoadBalancer) JaxbTestHelper.jaxbUnmarshall(dummyObject,
                NEUTRON_LOAD_BALANCER_SOURCE_JSON);
        Assert.assertEquals("NeutronLoadBalancer JAXB Test 1: Testing id failed",
                "2f245a7b-796b-4f26-9cf9-9e82d248fda7", testObject.uuid);

        Assert.assertEquals("NeutronLoadBalancer JAXB Test 2: Testing LoadBalancer Name failed",
                "NeutronLoadBalancer", testObject.getName());

        Assert.assertFalse("NeutronLoadBalancer JAXB Test 5: Testing Admin state up failed",
                testObject.getAdminStateUp());

        Assert.assertEquals("NeutronLoadBalancer JAXB Test 6: Testing LoadBalancer VipAddress failed", "10.0.0.3",
                testObject.getLoadBalancerVipAddress());

        Assert.assertEquals("NeutronLoadBalancer JAXB Test 7: Testing router id failed",
                "d23abc8d-2991-4a55-ba98-2aaea84cc72f", testObject.loadBalancerVipSubnetID);

        Assert.assertEquals("NeutronLoadBalancer JAXB Test 8: Testing tenant_id failed",
                "4969c491a3c74ee4af974e6d800c62de", testObject.getTenantID());
    }
}
