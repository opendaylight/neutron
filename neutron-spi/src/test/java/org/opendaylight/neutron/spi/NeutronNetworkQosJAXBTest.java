/*
 * Copyright (c) 2016 Intel Corporation  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.neutron.spi;

import javax.xml.bind.JAXBException;
import org.junit.Assert;
import org.junit.Test;

public class NeutronNetworkQosJAXBTest {
    private static final String NEUTRON_NETWORK_QOS_ENABLED_SOURCE_JSON = "{ "
            + "\"subnets\": [ \"3b80198d-4f7b-4f77-9ef5-774d54e17126\" ], " + "\"name\": \"net1\", "
            + "\"admin_state_up\": true, " + "\"tenant_id\": \"9bacb3c5d39d41a79512987f338cf177\", "
            + "\"router:external\": false, " + "\"provider:segmentation_id\": \"2\", "
            + "\"provider:physical_network\": \"8bab8453-1bc9-45af-8c70-f83aa9b50453\", "
            + "\"provider:network_type\": \"vlan\", " + "\"shared\": false, "
            + "\"qos_policy_id\": \"d6220bbb-35f3-48ab-8eae-69c60aef3546\", "
            + "\"id\": \"4e8e5957-649f-477b-9e5b-f1f75b21c03c\" }";

    @Test
    public void test_NeutronNetworkQos_JAXB() throws JAXBException {
        NeutronNetwork dummyObject = new NeutronNetwork();

        NeutronNetwork testObject = (NeutronNetwork) JaxbTestHelper.jaxbUnmarshall(dummyObject,
                NEUTRON_NETWORK_QOS_ENABLED_SOURCE_JSON);
        Assert.assertEquals("NeutronNetwork JAXB Test 1: Testing id failed", "4e8e5957-649f-477b-9e5b-f1f75b21c03c",
                testObject.getID());

        Assert.assertEquals("NeutronNetwork JAXB Test 2: Testing tenant_id failed", "9bacb3c5d39d41a79512987f338cf177",
                testObject.getTenantID());

        Assert.assertEquals("NeutronNetwork JAXB Test 3: Testing physical network id failed",
                "8bab8453-1bc9-45af-8c70-f83aa9b50453", testObject.getProviderPhysicalNetwork());

        Assert.assertEquals("NeutronNetwork JAXB Test 6: Testing name failed", "net1", testObject.getName());

        Assert.assertEquals("NeutronNetwork JAXB Test 7: Testing admin state up failed", true,
                testObject.getAdminStateUp());

        Assert.assertEquals("NeutronNetwork JAXB Test 8: Testing router external failed", false,
                testObject.getRouterExternal());

        Assert.assertEquals("NeutronNetwork JAXB Test 9: Testing provider segmentation id failed", "2",
                testObject.getProviderSegmentationID());

        Assert.assertEquals("NeutronNetwork JAXB Test 10: Testing provider network type id failed", "vlan",
                testObject.getProviderNetworkType());

        Assert.assertEquals("NeutronNetwork JAXB Test 11: Testing shared failed", false, testObject.getShared());

        Assert.assertEquals("NeutronNetwork JAXB Test 12: Testing qos_policy_id failed",
                "d6220bbb-35f3-48ab-8eae-69c60aef3546", testObject.getQosPolicyId());
    }
}
