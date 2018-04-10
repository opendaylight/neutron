/*
 * Copyright (c) 2016 Intel Corporation  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.neutron.spi;

import java.util.Arrays;
import java.util.List;
import javax.xml.bind.JAXBException;
import org.junit.Assert;
import org.junit.Test;

public class NeutronSubnetJAXBTest {

    private static final String NEUTRON_SUBNET_ENABLED_SOURCE_JSON = "{"
        + "\"service_types\": [], "
        + "\"description\": \"\", "
        + "\"enable_dhcp\": true, "
        + "\"network_id\": \"76d1c7a9-a559-49dd-926e-f3b80571eaab\", "
        + "\"tenant_id\": \"379ffe2b9cda498d9e17b319733ec889\", "
        + "\"created_at\": \"2016-09-01T19:32:47\", "
        + "\"dns_nameservers\": [\"2001:4860:4860::8844\", \"2001:4860:4860::8888\"], "
        + "\"updated_at\": \"2016-09-01T19:32:47\", "
        + "\"gateway_ip\": \"2003::1\", "
        + "\"ipv6_ra_mode\": null, "
        + "\"allocation_pools\": [{\"start\": \"2003::3\", \"end\": \"2003::7\"}], "
        + "\"host_routes\": [{\"nexthop\": \"2003::1\", \"destination\": \"2001::/64\"}], "
        + "\"ip_version\": 6, "
        + "\"ipv6_address_mode\": null, "
        + "\"cidr\": \"2003::/64\", "
        + "\"revision\": 2, "
        + "\"id\": \"dd4320eb-a56b-412b-ae83-fc5ac2a5e6f6\", "
        + "\"subnetpool_id\": null, "
        + "\"name\": \"\""
        + "}";

    @Test
    public void test_NeutronSubnet_JAXB() throws JAXBException {
        NeutronSubnet dummyObject = new NeutronSubnet();

        NeutronSubnet testObject = (NeutronSubnet) JaxbTestHelper.jaxbUnmarshall(dummyObject,
                NEUTRON_SUBNET_ENABLED_SOURCE_JSON);
        Assert.assertEquals("NeutronSubnet JAXB Test 1: Testing id failed",
                            "dd4320eb-a56b-412b-ae83-fc5ac2a5e6f6", testObject.getID());
        Assert.assertEquals("NeutronSubnet JAXB Test 3: Testing name failed", "", testObject.getName());
        Assert.assertEquals("NeutronSubnet JAXB Test 4: Testing enabled_dhcp failed", true, testObject.isEnableDHCP());
        Assert.assertEquals("NeutronSubnet JAXB Test 5: Testing network_id failed",
                            "76d1c7a9-a559-49dd-926e-f3b80571eaab", testObject.getNetworkUUID());

        List<String> dnsNameservers = Arrays.asList("2001:4860:4860::8844", "2001:4860:4860::8888");
        Assert.assertEquals("NeutronSubnet JAXB Test 6: Testing dns nameservers failed",
                            dnsNameservers, testObject.getDnsNameservers());

        Assert.assertEquals("NeutronSubnet JAXB Test 7: Testing gateway_ip failed",
                            "2003::1", testObject.getGatewayIp());
        Assert.assertEquals("NeutronSubnet JAXB Test 8: Testing ipv6_ra_mode failed",
                            null, testObject.getIpV6RaMode());
        Assert.assertEquals("NeutronSubnet JAXB Test 9: Testing ip_version failed",
                            6, testObject.getIpVersion().intValue());
        Assert.assertEquals("NeutronSubnet JAXB Test 10: Testing ipv6_address_mode failed",
                            null, testObject.getIpV6AddressMode());
        Assert.assertEquals("NeutronSubnet JAXB Test 10: Testing cidr failed",
                            "2003::/64", testObject.getCidr());

        Assert.assertEquals("NeutronSubnet JAXB Test 2: Testing tenant_id failed",
                "379ffe2b9cda498d9e17b319733ec889", testObject.getTenantID());
    }
}
