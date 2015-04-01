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

import org.opendaylight.neutron.spi.JaxbTestHelper;
import org.opendaylight.neutron.spi.NeutronPort_ExtraDHCPOption;

public class NeutronPort_ExtraDHCPOptionJAXBTest {

    private static final String NeutronPort_ExtraDHCPOption_sourceJson = "{" + "\"opt_value\": \"123.123.123.456\", "
            + "\"opt_name\": \"server-ip-address\" }";

    @Test
    public void test_NeutronPort_ExtraDHCPOption_JAXB() {
        NeutronPort_ExtraDHCPOption portObject = new NeutronPort_ExtraDHCPOption();
        try {
            NeutronPort_ExtraDHCPOption testObject = (NeutronPort_ExtraDHCPOption) JaxbTestHelper.jaxbUnmarshall(
                    portObject, NeutronPort_ExtraDHCPOption_sourceJson);
            Assert.assertEquals("NeutronPort_ExtraDHCPOption JAXB Test 1: Testing opt_value failed", "123.123.123.456",
                    testObject.getValue());

            Assert.assertEquals("NeutronPort_ExtraDHCPOption JAXB Test 10: Testing opt_name failed",
                    "server-ip-address", testObject.getName());

        } catch (Exception e) {
            e.printStackTrace();
            Assert.assertFalse("Tests Failed", true);
        }
    }

}
