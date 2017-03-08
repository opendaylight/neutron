/*
 * Copyright (C) 2017 Intel, Corp.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.spi;

import javax.xml.bind.JAXBException;
import org.junit.Assert;
import org.junit.Test;

public class NeutronTapFlowJAXBTest {

    private static final String NEUTRON_TAP_FLOW_SOURCE_JSON = "{"
            + "\"id\": \"f6220bbb-35f3-48ab-8eae-69c60aef3546\","
            + "\"tenant_id\": \"aa902936679e4ea29bfe1158e3450a13\","
            + "\"name\": \"tap-flow-test\","
            + "\"tap_service_id\": \"b6440bbb-35f3-48ab-8eae-69c60aef3546\","
            + "\"source_port\": \"411d75c3-4da8-4267-8aed-6dbb19a3dcfe\","
            + "\"direction\": \"BOTH\" " + "}";

    @Test
    public void test_NeutronTapFlow_JAXB() throws JAXBException {
        NeutronTapFlow testObject = new NeutronTapFlow();
        NeutronTapFlow neutronObject = (NeutronTapFlow) JaxbTestHelper.jaxbUnmarshall(testObject,
                NEUTRON_TAP_FLOW_SOURCE_JSON);
        Assert.assertEquals("NeutronTapFlow JAXB Test 1: Testing id failed", "f6220bbb-35f3-48ab-8eae-69c60aef3546",
                neutronObject.getID());

        Assert.assertEquals("NeutronTapFlow JAXB Test 2: Testing tenant_id failed",
                "aa902936679e4ea29bfe1158e3450a13", neutronObject.getTenantID());

        Assert.assertEquals("NeutronTapFlow JAXB Test 3 : Testing name failed", "tap-flow-test",
                neutronObject.getName());

        Assert.assertEquals("NeutronTapFlow JAXB Test 4 : Testing tap_service_id failed",
                "b6440bbb-35f3-48ab-8eae-69c60aef3546", neutronObject.getTapFlowServiceID());

        Assert.assertEquals("NeutronTapFlow JAXB Test 5 : Testing source_port failed",
                "411d75c3-4da8-4267-8aed-6dbb19a3dcfe", neutronObject.getTapFlowSourcePort());

        Assert.assertEquals("NeutronTapFlow JAXB Test 6: Testing direction failed",
                "BOTH", neutronObject.getTapFlowDirection());

    }
}
