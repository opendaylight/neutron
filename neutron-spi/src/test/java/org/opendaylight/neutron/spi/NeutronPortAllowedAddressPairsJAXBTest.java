/*
 * Copyright (c) 2015 Tata Consultancy Services.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.spi;

import javax.xml.bind.JAXBException;
import org.junit.Assert;
import org.junit.Test;

public class NeutronPortAllowedAddressPairsJAXBTest {

    private static final String NEUTRON_PORT_ALLOWED_ADDRESS_PAIRS_SOURCE_JSON = "{ "
            + "\"ip_address\": \"192.168.199.1\", " + "\"mac_address\": \"fa:16:3e:c9:cb:f0\" }";

    @Test
    public void test_NeutronPortAllowedAddressPairs_JAXB()  throws JAXBException {
        NeutronPortAllowedAddressPairs testObject = JaxbTestHelper
                .jaxbUnmarshall(NeutronPortAllowedAddressPairs.class, NEUTRON_PORT_ALLOWED_ADDRESS_PAIRS_SOURCE_JSON);
        Assert.assertEquals("NeutronPort Allowed Address Pairs JAXB Test 1: Testing ip_address failed",
                "192.168.199.1", testObject.getIpAddress());

        Assert.assertEquals("NeutronPort Allowed Address Pairs JAXB Test 10: Testing mac_address failed",
                "fa:16:3e:c9:cb:f0", testObject.getMacAddress());
    }
}
