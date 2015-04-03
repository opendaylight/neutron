/*
 * Copyright IBM Corporation and others, 2015.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.spi;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class NeutronLoadBalancerPoolJAXBTest {

    private static final String NeutronLoadBalancerPool_sourceJson = "{"
            + "\"id\": \"2f245a7b-796b-4f26-9cf9-9e82d248fda7\", " + "\"name\": \"LoadBalancerPool\", "
            + "\"description\": \"NeutronLoadBalancerPool_Description\", " + "\"admin_state_up\": \"false\", "
            + "\"lb_algorithm\": \"LEAST_CONNECTIONS\", "
            + "\"healthmonitor_id\": \"00066a7b-796b-4f26-9cf9-9e82d248fda7\", " + "\"protocol\": \"HTTP\", "
            + "\"tenant_id\": \"00075a7b-796b-4f26-9cf9-9e82d248fda7\", "
            + "\"session_persistence\": [ { \"cookie_name\":\"cookie_name\" , \"type\": \"HTTP_COOKIE\" } ]" + "}";

    @Test
    public void test_NeutronLoadBalancerPool_JAXB() {
        NeutronLoadBalancerPool dummyObject = new NeutronLoadBalancerPool();
        try {
            NeutronLoadBalancerPool testObject = (NeutronLoadBalancerPool) JaxbTestHelper.jaxbUnmarshall(dummyObject,
                    NeutronLoadBalancerPool_sourceJson);
            Assert.assertEquals("NeutronLoadBalancerPool JAXB Test 1: Testing id failed",
                    "2f245a7b-796b-4f26-9cf9-9e82d248fda7", testObject.getLoadBalancerPoolID());

            Assert.assertEquals("NeutronLoadBalancerPool JAXB Test 2: Testing name failed", "LoadBalancerPool",
                    testObject.getLoadBalancerPoolName());

            Assert.assertFalse("NeutronLoadBalancerPool JAXB Test 3: Testing admin_state_up failed",
                    testObject.getLoadBalancerPoolAdminIsStateIsUp());

            Assert.assertEquals("NeutronLoadBalancerPool JAXB Test 4: Testing Description failed",
                    "NeutronLoadBalancerPool_Description", testObject.getLoadBalancerPoolDescription());

            Assert.assertEquals("NeutronLoadBalancerPool JAXB Test 5: Testing LbAlgorithm failed", "LEAST_CONNECTIONS",
                    testObject.getLoadBalancerPoolLbAlgorithm());

            Assert.assertEquals("NeutronLoadBalancerPool JAXB Test 6: Testing Protocol failed", "HTTP",
                    testObject.getLoadBalancerPoolProtocol());

            Assert.assertEquals("NeutronLoadBalancerPool JAXB Test 7: Testing Tenant_id failed",
                    "00075a7b-796b-4f26-9cf9-9e82d248fda7", testObject.getLoadBalancerPoolTenantID());

            Assert.assertEquals("NeutronLoadBalancerPool JAXB Test 8: Testing HealthMonitorID failed",
                    "00066a7b-796b-4f26-9cf9-9e82d248fda7", testObject.getNeutronLoadBalancerPoolHealthMonitorID());

            Neutron_ID neutron_id_Listener = new Neutron_ID();
            neutron_id_Listener.setID("00065a7b-796b-4f26-9cf9-9e82d248fda7");
            List<Neutron_ID> neutronLoadBalancerListenerIDs = new ArrayList<Neutron_ID>();
            neutronLoadBalancerListenerIDs.add(neutron_id_Listener);
            testObject.setLoadBalancerPoolListeners(neutronLoadBalancerListenerIDs);
            Assert.assertEquals("NeutronLoadBalancerPool JAXB Test 9: Testing Listeners failed",
                    "00065a7b-796b-4f26-9cf9-9e82d248fda7", testObject.getLoadBalancerPoolListeners().get(0).getID());

            NeutronLoadBalancerPoolMember neutron_member = new NeutronLoadBalancerPoolMember();
            neutron_member.setPoolMemberID("00075a7b-796b-4f26-9cf9-9e82d248fda0");
            List<NeutronLoadBalancerPoolMember> neutronLoadBalancerMemberIDs = new ArrayList<NeutronLoadBalancerPoolMember>();
            neutronLoadBalancerMemberIDs.add(neutron_member);
            testObject.setLoadBalancerPoolMembers(neutronLoadBalancerMemberIDs);
            Assert.assertEquals("NeutronLoadBalancerPool JAXB Test 10: Testing Members failed",
                    "00075a7b-796b-4f26-9cf9-9e82d248fda0", testObject.getLoadBalancerPoolMembers().get(0)
                            .getPoolMemberID());

            Assert.assertEquals("NeutronLoadBalancerPool JAXB Test 11: Testing session_persistence cookie_name failed",
                    "cookie_name", testObject.getLoadBalancerPoolSessionPersistence().getCookieName());

            Assert.assertEquals("NeutronLoadBalancerPool JAXB Test 12: Testing session_persistence type failed",
                    "HTTP_COOKIE", testObject.getLoadBalancerPoolSessionPersistence().getType());

        } catch (Exception e) {
            Assert.fail("Tests failed");
        }
    }
}
