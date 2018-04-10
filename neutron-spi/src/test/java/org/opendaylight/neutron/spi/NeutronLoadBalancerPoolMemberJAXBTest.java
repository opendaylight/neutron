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

public class NeutronLoadBalancerPoolMemberJAXBTest {

    private static final String NEUTRON_LOAD_BALANCER_POOL_MEMBER_SOURCE_JSON = "{"
            + "\"id\": \"2f245a7b-796b-4f26-9cf9-9e82d248fda7\", " + "\"address\": \"10.0.0.8\", "
            + "\"protocol_port\": 80, " + "\"admin_state_up\": \"false\", " + "\"weight\": 1, "
            + "\"subnet_id\": \"10045a7b-0000-4f26-9cf9-9e82d248fda7\", "
            + "\"tenant_id\": \"00045a7b-796b-4f26-9cf9-9e82d248fda7\" }";

    @Test
    public void test_NeutronLoadBalancerPoolMember_JAXB()  throws JAXBException {
        NeutronLoadBalancerPoolMember testObject = JaxbTestHelper
                .jaxbUnmarshall(NeutronLoadBalancerPoolMember.class, NEUTRON_LOAD_BALANCER_POOL_MEMBER_SOURCE_JSON);
        Assert.assertEquals("NeutronLoadBalancerPoolMember JAXB Test 1: Testing id failed",
                "2f245a7b-796b-4f26-9cf9-9e82d248fda7", testObject.getID());

        Assert.assertEquals("NeutronLoadBalancerPoolMember JAXB Test 2: Testing  address failed", "10.0.0.8",
                testObject.getPoolMemberAddress());

        Assert.assertEquals("NeutronLoadBalancerPoolMember JAXB Test 3: Testing  protocol_port failed", 80,
                (long) testObject.getPoolMemberProtoPort());

        Assert.assertEquals("NeutronLoadBalancerPoolMember JAXB Test 4: Testing  weight failed", 1,
                (long) testObject.getPoolMemberWeight());

        Assert.assertFalse("NeutronLoadBalancerPoolMember JAXB Test 5: Testing  admin_state_up failed",
                testObject.getPoolMemberAdminStateIsUp());

        Assert.assertEquals("NeutronLoadBalancerPoolMember JAXB Test 6: Testing  subnet_id  failed",
                "10045a7b-0000-4f26-9cf9-9e82d248fda7", testObject.getPoolMemberSubnetID());

        Assert.assertEquals("NeutronLoadBalancerPoolMember JAXB Test 7: Testing  tenant_id  failed",
                "00045a7b-796b-4f26-9cf9-9e82d248fda7", testObject.getTenantID());

    }

}
