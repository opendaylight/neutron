/*
 * Copyright (C) 2015 IBM, Inc.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.spi;

import org.junit.Assert;
import org.junit.Test;

public class NeutronRouter_NetworkReferenceJAXBTest {

    private static final String NeutronRouter_NetworkReference_sourceJson = "{" +
        "\"network_id\": \"e9330b1f-a2ef-4160-a991-169e56ab17f6\" }";

    @Test
    public void test_NeutronRouter_NetworkReference_JAXB() {
        NeutronRouter_NetworkReference dummyObject = new NeutronRouter_NetworkReference();
        try {
            NeutronRouter_NetworkReference testObject = (NeutronRouter_NetworkReference) JaxbTestHelper.jaxbUnmarshall(
                    dummyObject, NeutronRouter_NetworkReference_sourceJson);

            Assert.assertEquals("NeutronRouter_NetworkReference JAXB Test 1: Testing subnet_id failed",
                    "e9330b1f-a2ef-4160-a991-169e56ab17f6", testObject.getNetworkID());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
