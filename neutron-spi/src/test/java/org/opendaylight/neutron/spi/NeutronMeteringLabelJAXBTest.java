/*
 * Copyright Tata Consultancy Services, 2015.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.spi;

import org.junit.Assert;
import org.junit.Test;

import org.opendaylight.neutron.spi.JaxbTestHelper;
import org.opendaylight.neutron.spi.NeutronMeteringLabel;

public class NeutronMeteringLabelJAXBTest {

    private static final String NeutronMeteringLabel_sourceJson = "{ "
            + "\"id\": \"4e8e5957-649f-477b-9e5b-f1f75b21c03c\", " + "\"name\": \"net1\", "
            + "\"description\": \"Provides allowed address pairs\", "
            + "\"tenant_id\": \"9bacb3c5d39d41a79512987f338cf177\"}";

    @Test
    public void test_NeutronMeteringLabel_JAXB() {
        NeutronMeteringLabel meteringObject = new NeutronMeteringLabel();
        try {
            NeutronMeteringLabel testObject = (NeutronMeteringLabel) JaxbTestHelper.jaxbUnmarshall(meteringObject,
                    NeutronMeteringLabel_sourceJson);
            Assert.assertEquals("NeutronMeteringLabel JAXB Test 1: Testing id failed",
                    "4e8e5957-649f-477b-9e5b-f1f75b21c03c", testObject.getID());

            Assert.assertEquals("NeutronMeteringLabel JAXB Test 2: Testing name failed", "net1",
                    testObject.getMeteringLabelName());

            Assert.assertEquals("NeutronMeteringLabel JAXB Test 4: Testing tenant_id failed",
                    "9bacb3c5d39d41a79512987f338cf177", testObject.getTenantID());
        } catch (Exception e) {
            Assert.fail("Test failed");
        }
    }
}
