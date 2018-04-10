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

public class NeutronLoadBalancerPoolJAXBTest {

    private static final String NEUTRON_LOAD_BALANCER_POOL_SOURCE_JSON = "{ " + "\"admin_state_up\": true, "
            + "\"description\": \"simple pool\", " + "\"healthmonitor_id\": \"00066a7b-796b-4f26-9cf9-9e82d248fda7\", "
            + "\"id\": \"12ff63af-4127-4074-a251-bcb2ecc53ebe\", " + "\"lb_algorithm\": \"ROUND_ROBIN\", "
            + "\"listeners\": [ " + "{ " + "\"id\": \"39de4d56-d663-46e5-85a1-5b9d5fa17829\" " + "} " + "], "
            + "\"members\": [], " + "\"name\": \"pool1\", " + "\"protocol\": \"HTTP\", " + "\"session_persistence\": { "
            + "\"cookie_name\": \"my_cookie\", " + "\"type\": \"APP_COOKIE\" " + "}, "
            + "\"tenant_id\": \"1a3e005cf9ce40308c900bcb08e5320c\" " + "} ";

    @Test
    public void test_NeutronLoadBalancerPool_JAXB()  throws JAXBException {
        NeutronLoadBalancerPool testObject = JaxbTestHelper.jaxbUnmarshall(NeutronLoadBalancerPool.class,
                NEUTRON_LOAD_BALANCER_POOL_SOURCE_JSON);
        Assert.assertEquals("NeutronLoadBalancerPool JAXB Test 1: Testing id failed",
                "12ff63af-4127-4074-a251-bcb2ecc53ebe", testObject.getID());

        Assert.assertEquals("NeutronLoadBalancerPool JAXB Test 2: Testing name failed", "pool1",
                testObject.getName());

        Assert.assertTrue("NeutronLoadBalancerPool JAXB Test 3: Testing admin_state_up failed",
                testObject.getLoadBalancerPoolAdminIsStateIsUp());

        Assert.assertEquals("NeutronLoadBalancerPool JAXB Test 5: Testing LbAlgorithm failed", "ROUND_ROBIN",
                testObject.getLoadBalancerPoolLbAlgorithm());

        Assert.assertEquals("NeutronLoadBalancerPool JAXB Test 6: Testing Protocol failed", "HTTP",
                testObject.getLoadBalancerPoolProtocol());

        Assert.assertEquals("NeutronLoadBalancerPool JAXB Test 8: Testing HealthMonitorID failed",
                "00066a7b-796b-4f26-9cf9-9e82d248fda7", testObject.getLoadBalancerPoolHealthMonitorID());

        List<NeutronID> testListeners = testObject.getLoadBalancerPoolListeners();
        Assert.assertEquals("NeutronLoadBalancerPool JAXB Test 9.1: Testing Listeners failed", 1,
                testListeners.size());

        Assert.assertEquals("NeutronLoadBalancerPool JAXB Test 9.2: Testing Listeners failed",
                "39de4d56-d663-46e5-85a1-5b9d5fa17829", testObject.getLoadBalancerPoolListeners().get(0).getID());

        List<NeutronLoadBalancerPoolMember> testMembers = testObject.getLoadBalancerPoolMembers();
        Assert.assertEquals("NeutronLoadBalancerPool JAXB Test 10.1: Testing Listeners failed", 0,
                testMembers.size());

        Assert.assertEquals("NeutronLoadBalancerPool JAXB Test 11: Testing session_persistence cookie_name failed",
                "my_cookie", testObject.getLoadBalancerPoolSessionPersistence().getCookieName());

        Assert.assertEquals("NeutronLoadBalancerPool JAXB Test 12: Testing session_persistence type failed",
                "APP_COOKIE", testObject.getLoadBalancerPoolSessionPersistence().getType());

        Assert.assertEquals("NeutronLoadBalancerPool JAXB Test 7: Testing Tenant_id failed",
                "1a3e005cf9ce40308c900bcb08e5320c", testObject.getTenantID());
    }
}
