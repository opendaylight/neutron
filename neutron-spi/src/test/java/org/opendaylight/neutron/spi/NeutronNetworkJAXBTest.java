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
import org.opendaylight.neutron.spi.NeutronNetwork;

public class NeutronNetworkJAXBTest {

    private static final String NeutronNetwork_SingleProvider_sourceJson="{ " +
         "\"status\": \"ACTIVE\", " +
        "\"subnets\": [ \"3b80198d-4f7b-4f77-9ef5-774d54e17126\" ], " +
        "\"name\": \"net1\", " +
        "\"admin_state_up\": true, " +
        "\"tenant_id\": \"9bacb3c5d39d41a79512987f338cf177\", " +
        "\"router:external\": false, " +
        "\"provider:segmentation_id\": \"2\", " +
        "\"provider:physical_network\": \"8bab8453-1bc9-45af-8c70-f83aa9b50453\", " +
        "\"provider:network_type\": \"vlan\", " +
        "\"shared\": false, " +
        "\"id\": \"4e8e5957-649f-477b-9e5b-f1f75b21c03c\" }";

    @Test
    public void test_NeutronNetwork_SingleProvider_JAXB() {
        NeutronNetwork dummyObject = new NeutronNetwork();
        try {
            NeutronNetwork testObject = (NeutronNetwork) JaxbTestHelper.jaxbUnmarshall(dummyObject, NeutronNetwork_SingleProvider_sourceJson);
            Assert.assertEquals("NeutronNetwork JAXB Test 1: Testing id failed",
                  "4e8e5957-649f-477b-9e5b-f1f75b21c03c", testObject.getID());

            Assert.assertEquals("NeutronNetwork JAXB Test 2: Testing tenant_id failed",
                  "9bacb3c5d39d41a79512987f338cf177", testObject.getTenantID());

            Assert.assertEquals("NeutronNetwork JAXB Test 3: Testing physical network id failed",
                  "8bab8453-1bc9-45af-8c70-f83aa9b50453", testObject.getProviderPhysicalNetwork());

            Assert.assertEquals("NeutronNetwork JAXB Test 4: Testing status failed",
                  "ACTIVE", testObject.getStatus());

            Assert.assertEquals("NeutronNetwork JAXB Test 6: Testing name failed",
                                "net1", testObject.getNetworkName());

            Assert.assertEquals("NeutronNetwork JAXB Test 7: Testing admin state up failed",
                  true, testObject.getAdminStateUp());

            Assert.assertEquals("NeutronNetwork JAXB Test 8: Testing router external failed",
                  false, testObject.getRouterExternal());

            Assert.assertEquals("NeutronNetwork JAXB Test 9: Testing provider segmentation id failed",
                  "2", testObject.getProviderSegmentationID());

            Assert.assertEquals("NeutronNetwork JAXB Test 10: Testing provider network type id failed",
                  "vlan", testObject.getProviderNetworkType());

            Assert.assertEquals("NeutronNetwork JAXB Test 11: Testing shared failed",
                  false, testObject.getShared());
        } catch (Exception e) {
            Assert.assertTrue("Tests failed", false);
        }
    }

    private static final String NeutronNetwork_MultipleProvider_sourceJson="{" +
        "\"status\": \"ACTIVE\", " +
        "\"subnets\": [ \"3b80198d-4f7b-4f77-9ef5-774d54e17126\" ], " +
        "\"name\": \"net1\", " +
        "\"admin_state_up\": true, " +
        "\"tenant_id\": \"9bacb3c5d39d41a79512987f338cf177\", " +
        "\"router:external\": false, " +
        "\"segments\": [ { " +
            "\"provider:segmentation_id\": \"2\", " +
            "\"provider:physical_network\": \"8bab8453-1bc9-45af-8c70-f83aa9b50453\", " +
            "\"provider:network_type\": \"vlan\" }, " +
        "{ \"provider:segmentation_id\": null, " +
            "\"provider:physical_network\": \"8bab8453-1bc9-45af-8c70-f83aa9b50454\", " +
            "\"provider:network_type\": \"stt\" } ], " +
        "\"shared\": false, " +
        "\"id\": \"4e8e5957-649f-477b-9e5b-f1f75b21c03c\" }";

    @Test
    public void test_NeutronNetwork_MultipleProvider_JAXB() {
        NeutronNetwork dummyObject = new NeutronNetwork();
        try {
            NeutronNetwork testObject = (NeutronNetwork) JaxbTestHelper.jaxbUnmarshall(dummyObject, NeutronNetwork_MultipleProvider_sourceJson);
            Assert.assertEquals("NeutronNetwork JAXB Test 1: Testing id failed",
                  "4e8e5957-649f-477b-9e5b-f1f75b21c03c", testObject.getID());

            Assert.assertEquals("NeutronNetwork JAXB Test 2: Testing tenant_id failed",
                  "9bacb3c5d39d41a79512987f338cf177", testObject.getTenantID());

            Assert.assertEquals("NeutronNetwork JAXB Test 3: Testing status failed",
                  "ACTIVE", testObject.getStatus());

            Assert.assertEquals("NeutronNetwork JAXB Test 5: Testing name failed",
                                "net1", testObject.getNetworkName());

            Assert.assertEquals("NeutronNetwork JAXB Test 6: Testing admin state up failed",
                  true, testObject.getAdminStateUp());

            Assert.assertEquals("NeutronNetwork JAXB Test 7: Testing router external failed",
                  false, testObject.getRouterExternal());

            Assert.assertEquals("NeutronNetwork JAXB Test 8: Testing shared failed",
                  false, testObject.getShared());

            List<NeutronNetwork_Segment> segments = testObject.getSegments();
            Assert.assertEquals("NeutronNetwork JAXB Test 9.1: Testing segments list length failed",
                  2, segments.size());

            Assert.assertEquals("NeutronNetwork JAXB Test 9.2: Testing segment index 0 segmentation id failed",
                                "2", segments.get(0).getProviderSegmentationID());

            Assert.assertEquals("NeutronNetwork JAXB Test 9.3: Testing segment index 0 physical network failed",
                                "8bab8453-1bc9-45af-8c70-f83aa9b50453", segments.get(0).getProviderPhysicalNetwork());

            Assert.assertEquals("NeutronNetwork JAXB Test 9.4: Testing segment index 0 network type failed",
                                "vlan", segments.get(0).getProviderNetworkType());

            Assert.assertEquals("NeutronNetwork JAXB Test 9.5: Testing segment index 1 segmentation id failed",
                                null, segments.get(1).getProviderSegmentationID());

            Assert.assertEquals("NeutronNetwork JAXB Test 9.3: Testing segment index 1 physical network failed",
                                "8bab8453-1bc9-45af-8c70-f83aa9b50454", segments.get(1).getProviderPhysicalNetwork());

            Assert.assertEquals("NeutronNetwork JAXB Test 9.4: Testing segment index 1 network type failed",
                                "stt", segments.get(1).getProviderNetworkType());
        } catch (Exception e) {
            Assert.assertTrue("Tests failed", false);
        }
    }
}
