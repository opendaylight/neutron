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

public class NeutronTapServiceJAXBTest {

    private static final String NEUTRON_TAP_SERVICE_SOURCE_JSON = "{"
            + "\"id\": \"b6440bbb-35f3-48ab-8eae-69c60aef3546\","
            + "\"tenant_id\": \"aa902936679e4ea29bfe1158e3450a13\","
            + "\"name\": \"tap-service-test\","
            + "\"port_id\": \"311d75c3-4da8-4267-8aed-6dbb19a3dcfe\" " + "}";

    @Test
    public void test_NeutronTapService_JAXB() throws JAXBException {
        NeutronTapService testObject = new NeutronTapService();
        NeutronTapService neutronObject = (NeutronTapService) JaxbTestHelper.jaxbUnmarshall(testObject,
                NEUTRON_TAP_SERVICE_SOURCE_JSON);
        Assert.assertEquals("NeutronTapService JAXB Test 1: Testing id failed", "b6440bbb-35f3-48ab-8eae-69c60aef3546",
                neutronObject.getID());

        Assert.assertEquals("NeutronTapService JAXB Test 2: Testing tenant_id failed",
                "aa902936679e4ea29bfe1158e3450a13", neutronObject.getTenantID());

        Assert.assertEquals("NeutronTapService JAXB Test 3 : Testing name failed", "tap-service-test",
                neutronObject.getName());

        Assert.assertEquals("NeutronTapService JAXB Test 4 : Testing port_id failed",
                "311d75c3-4da8-4267-8aed-6dbb19a3dcfe", neutronObject.getTapServicePortID());

    }
}
