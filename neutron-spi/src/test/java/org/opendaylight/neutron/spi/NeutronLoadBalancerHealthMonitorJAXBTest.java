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

public class NeutronLoadBalancerHealthMonitorJAXBTest {

    private static final String NeutronLoadBalancerHealthMonitor_sourceJson = "{"
            + "\"id\": \"2f245a7b-796b-4f26-9cf9-9e82d248fda7\", " + "\"type\": \"HTTP\", " + "\"delay\": 1, "
            + "\"timeout\": 1, " + "\"admin_state_up\": \"false\", " + "\"max_retries\": 5, "
            + "\"http_method\": \"get\", " + "\"url_path\": \"/index.html\", " + "\"expected_codes\": \"200,201,202\", "
            + "\"tenant_id\": \"00045a7b-796b-4f26-9cf9-9e82d248fda7\" }";

    @Test
    public void test_NeutronLoadBalancerHealthMonitor_JAXB() throws JAXBException {
        NeutronLoadBalancerHealthMonitor dummyObject = new NeutronLoadBalancerHealthMonitor();

        NeutronLoadBalancerHealthMonitor testObject = (NeutronLoadBalancerHealthMonitor) JaxbTestHelper
                .jaxbUnmarshall(dummyObject, NeutronLoadBalancerHealthMonitor_sourceJson);
        Assert.assertEquals("NeutronLoadBalancerHealthMonitor JAXB Test 1: Testing id failed",
                "2f245a7b-796b-4f26-9cf9-9e82d248fda7", testObject.getID());

        Assert.assertEquals("NeutronLoadBalancerHealthMonitor JAXB Test 2: Testing  Type failed", "HTTP",
                testObject.getLoadBalancerHealthMonitorType());

        Assert.assertEquals("NeutronLoadBalancerHealthMonitor JAXB Test 3: Testing  delay failed", 1,
                (long) testObject.getLoadBalancerHealthMonitorDelay());

        Assert.assertEquals("NeutronLoadBalancerHealthMonitor JAXB Test 4: Testing  timeout failed", 1,
                (long) testObject.getLoadBalancerHealthMonitorTimeout());

        Assert.assertFalse("NeutronLoadBalancerHealthMonitor JAXB Test 5: Testing  admin_state_up failed",
                testObject.getLoadBalancerHealthMonitorAdminStateIsUp());

        Assert.assertEquals("NeutronLoadBalancerHealthMonitor JAXB Test 6: Testing  max_retries  failed", 5,
                (long) testObject.getLoadBalancerHealthMonitorMaxRetries());

        Assert.assertEquals("NeutronLoadBalancerHealthMonitor JAXB Test 7: Testing  http_methods failed", "get",
                testObject.getLoadBalancerHealthMonitorHttpMethod());

        Assert.assertEquals("NeutronLoadBalancerHealthMonitor JAXB Test 8: Testing  url_path failed", "/index.html",
                testObject.getLoadBalancerHealthMonitorUrlPath());

        Assert.assertEquals("NeutronLoadBalancerHealthMonitor JAXB Test 9: Testing  expected_codes failed",
                "200,201,202", testObject.getLoadBalancerHealthMonitorExpectedCodes());

        Assert.assertEquals("NeutronLoadBalancerHealthMonitor JAXB Test 10: Testing tenant_id failed",
                "00045a7b-796b-4f26-9cf9-9e82d248fda7", testObject.getTenantID());

        Neutron_ID neutron_id = new Neutron_ID();
        neutron_id.setID("2f245a7b-0000-4f26-9cf9-9e82d248fda7");
        List<Neutron_ID> loadBalancerHealthMonitorPool = new ArrayList<Neutron_ID>();
        loadBalancerHealthMonitorPool.add(neutron_id);
        testObject.setLoadBalancerHealthMonitorPools(loadBalancerHealthMonitorPool);
        Assert.assertEquals("NeutronLoadBalancerHealthMonitor JAXB Test 11: Testing Pools failed",
                "2f245a7b-0000-4f26-9cf9-9e82d248fda7",
                testObject.getLoadBalancerHealthMonitorPools().get(0).getID());
    }
}
