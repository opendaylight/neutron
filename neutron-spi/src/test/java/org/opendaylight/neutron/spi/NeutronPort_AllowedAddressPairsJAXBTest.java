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
import org.opendaylight.neutron.spi.NeutronPort_AllowedAddressPairs;

public class NeutronPort_AllowedAddressPairsJAXBTest {

    private static final String NeutronPort_AllowedAddressPairs_sourceJson = "{ "
            + "\"ip_address\": \"192.168.199.1\", " + "\"port_id\": \"65c0ee9f-d634-4522-8954-51021b570b0d\", "
            + "\"mac_address\": \"fa:16:3e:c9:cb:f0\" }";

    @Test
    public void test_NeutronPort_AllowedAddressPairs_JAXB() {
        NeutronPort_AllowedAddressPairs portObject = new NeutronPort_AllowedAddressPairs();
        try {
            NeutronPort_AllowedAddressPairs testObject = (NeutronPort_AllowedAddressPairs) JaxbTestHelper
                    .jaxbUnmarshall(portObject, NeutronPort_AllowedAddressPairs_sourceJson);
            Assert.assertEquals("NeutronPort Allowed Address Pairs JAXB Test 1: Testing ip_address failed",
                    "192.168.199.1", testObject.getIpAddress());

            Assert.assertEquals("NeutronPort Allowed Address Pairs JAXB Test 2: Testing port_id failed",
                    "65c0ee9f-d634-4522-8954-51021b570b0d", testObject.getPortID());

            Assert.assertEquals("NeutronPort Allowed Address Pairs JAXB Test 10: Testing mac_address failed",
                    "fa:16:3e:c9:cb:f0", testObject.getMacAddress());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
