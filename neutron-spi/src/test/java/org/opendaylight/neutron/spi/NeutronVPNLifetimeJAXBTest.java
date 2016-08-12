/*
 * Copyright (c) 2015 Tata Consultancy Services.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.spi;

import org.junit.Assert;
import org.junit.Test;

public class NeutronVPNLifetimeJAXBTest {

    private static final String NeutronVPNLifetimeTest_sourceJson = "{" + "\"units\": \"seconds\", "
            + "\"value\": 3600 }";

    @Test
    public void test_NeutronVPNLifetime_JAXB() {
        NeutronVPNLifetime dummyObject = new NeutronVPNLifetime();
        try {
            NeutronVPNLifetime testObject = (NeutronVPNLifetime) JaxbTestHelper.jaxbUnmarshall(dummyObject,
                    NeutronVPNLifetimeTest_sourceJson);
            Assert.assertEquals("NeutronVPNLifetime JAXB Test 1: Testing units failed", "seconds",
                    testObject.getUnits());

            Assert.assertEquals("NeutronVPNLifetime JAXB Test 2: Testing value failed", new Integer(3600),
                    testObject.getValue());
        } catch (Exception e) {
            Assert.fail("Tests failed");
        }
    }
}
