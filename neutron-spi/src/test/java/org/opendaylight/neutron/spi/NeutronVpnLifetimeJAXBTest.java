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

public class NeutronVpnLifetimeJAXBTest {

    private static final String NEUTRON_VPN_LIFETIME_TEST_SOURCE_JSON = "{" + "\"units\": \"seconds\", "
            + "\"value\": 3600 }";

    @Test
    public void test_NeutronVPNLifetime_JAXB() throws JAXBException {
        NeutronVpnLifetime testObject = JaxbTestHelper.jaxbUnmarshall(NeutronVpnLifetime.class,
                NEUTRON_VPN_LIFETIME_TEST_SOURCE_JSON);
        Assert.assertEquals("NeutronVpnLifetime JAXB Test 1: Testing units failed", "seconds",
                testObject.getUnits());

        Assert.assertEquals("NeutronVpnLifetime JAXB Test 2: Testing value failed", new Integer(3600),
                testObject.getValue());
    }
}
