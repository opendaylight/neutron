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

public class NeutronPortExtraDHCPOptionJAXBTest {

    private static final String NEUTRON_PORT_EXTRA_DHCP_OPTION_SOURCE_JSON = "{"
            + "\"opt_value\": \"123.123.123.456\", "
            + "\"opt_name\": \"server-ip-address\" " + "}";
    private static final String NEUTRON_PORT_EXTRA_DHCP_OPTION_IPV4_SOURCE_JSON = "{"
            + "\"opt_value\": \"123.123.123.456\", " + "\"opt_name\": \"server-ip-address\", "
            + "\"ip_version\": 4" + "}";
    private static final String NEUTRON_PORT_EXTRA_DHCP_OPTION_IPV6_SOURCE_JSON = "{"
            + "\"opt_value\": \"::ffff:123.123.123.456\", "
            + "\"opt_name\": \"server-ip-address\", "
            + "\"ip_version\": 6" + "}";

    @Test
    public void test_NeutronPortExtraDHCPOption_JAXB() throws JAXBException {
        NeutronPortExtraDHCPOption testObject = JaxbTestHelper.jaxbUnmarshall(NeutronPortExtraDHCPOption.class,
                NEUTRON_PORT_EXTRA_DHCP_OPTION_SOURCE_JSON);

        Assert.assertEquals("NeutronPortExtraDHCPOption JAXB Test 1: Testing opt_value failed", "123.123.123.456",
                testObject.getValue());
        Assert.assertEquals("NeutronPortExtraDHCPOption JAXB Test 10: Testing opt_name failed", "server-ip-address",
                testObject.getName());
        Assert.assertEquals("NeutronPortExtraDHCPOption JAXB Test 20: Testing opt_name failed", 4,
                testObject.getIpVersion().intValue());
    }

    @Test
    public void test_NeutronPortExtraDHCPOption_IPv4_JAXB() throws JAXBException {
        NeutronPortExtraDHCPOption testObject = JaxbTestHelper.jaxbUnmarshall(NeutronPortExtraDHCPOption.class,
                NEUTRON_PORT_EXTRA_DHCP_OPTION_IPV4_SOURCE_JSON);

        Assert.assertEquals("NeutronPortExtraDHCPOption_IPv4 JAXB Test 1: Testing opt_value failed", "123.123.123.456",
                testObject.getValue());
        Assert.assertEquals("NeutronPortExtraDHCPOption_IPv4 JAXB Test 10: Testing opt_name failed",
                "server-ip-address", testObject.getName());
        Assert.assertEquals("NeutronPortExtraDHCPOption_IPv4 JAXB Test 20: Testing opt_name failed", 4,
                testObject.getIpVersion().intValue());
    }

    @Test
    public void test_NeutronPortExtraDHCPOption_IPv6_JAXB() throws JAXBException {
        NeutronPortExtraDHCPOption testObject = JaxbTestHelper
                .jaxbUnmarshall(NeutronPortExtraDHCPOption.class, NEUTRON_PORT_EXTRA_DHCP_OPTION_IPV6_SOURCE_JSON);

        Assert.assertEquals("NeutronPortExtraDHCPOption_IPv6 JAXB Test 1: Testing opt_value failed",
                "::ffff:123.123.123.456", testObject.getValue());
        Assert.assertEquals("NeutronPortExtraDHCPOption_IPv6 JAXB Test 10: Testing opt_name failed",
                "server-ip-address", testObject.getName());
        Assert.assertEquals("NeutronPortExtraDHCPOption_IPv6 JAXB Test 20: Testing opt_name failed", 6,
                testObject.getIpVersion().intValue());
    }
}
