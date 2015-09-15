/*
 * Copyright (C) 2015 IBM, Inc.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.spi;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import org.opendaylight.neutron.spi.JaxbTestHelper;
import org.opendaylight.neutron.spi.NeutronFloatingIP;

public class NeutronFloatingIPJAXBTest {

    private static final String NeutronFloatingIP_sourceJson="{" +
        "\"fixed_ip_address\": \"10.0.0.3\", " +
        "\"floating_ip_address\": \"172.24.4.228\", " +
        "\"floating_network_id\": \"376da547-b977-4cfe-9cba-275c80debf57\", " +
        "\"id\": \"2f245a7b-796b-4f26-9cf9-9e82d248fda7\", " +
        "\"port_id\": \"ce705c24-c1ef-408a-bda3-7bbd946164ab\", " +
        "\"router_id\": \"d23abc8d-2991-4a55-ba98-2aaea84cc72f\", " +
        "\"status\": \"ACTIVE\", " +
        "\"tenant_id\": \"4969c491a3c74ee4af974e6d800c62de\" }";

    @Test
    public void test_NeutronFloatingIP_JAXB() {
        NeutronFloatingIP dummyObject = new NeutronFloatingIP();
        try {
            NeutronFloatingIP testObject = (NeutronFloatingIP) JaxbTestHelper.jaxbUnmarshall(dummyObject, NeutronFloatingIP_sourceJson);
            Assert.assertEquals("NeutronFloatingIP JAXB Test 1: Testing id failed",
                  "2f245a7b-796b-4f26-9cf9-9e82d248fda7", testObject.getID());

            Assert.assertEquals("NeutronFloatingIP JAXB Test 2: Testing tenant_id failed",
                  "4969c491a3c74ee4af974e6d800c62de", testObject.getTenantID());

            Assert.assertEquals("NeutronFloatingIP JAXB Test 3: Testing router id failed",
                  "d23abc8d-2991-4a55-ba98-2aaea84cc72f", testObject.getRouterUUID());

            Assert.assertEquals("NeutronFloatingIP JAXB Test 4: Testing port id failed",
                  "ce705c24-c1ef-408a-bda3-7bbd946164ab", testObject.getPortUUID());

            Assert.assertEquals("NeutronFloatingIP JAXB Test 5: Testing floating network id failed",
                  "376da547-b977-4cfe-9cba-275c80debf57", testObject.getFloatingNetworkUUID());

            Assert.assertEquals("NeutronFloatingIP JAXB Test 6: Testing floating ip address failed",
                  "172.24.4.228", testObject.getFloatingIPAddress());

            Assert.assertEquals("NeutronFloatingIP JAXB Test 7: Testing fixed ip address failed",
                   "10.0.0.3", testObject.getFixedIPAddress());

            Assert.assertEquals("NeutronFloatingIP JAXB Test 8: Testing status failed",
                  "ACTIVE", testObject.getStatus());
        } catch (Exception e) {
            Assert.assertTrue("Tests failed", false);
        }
    }
}
