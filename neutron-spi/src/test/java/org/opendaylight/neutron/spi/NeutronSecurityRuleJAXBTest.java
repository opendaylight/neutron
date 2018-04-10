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

public class NeutronSecurityRuleJAXBTest {

    private static final String NEUTRON_SECURITY_RULE_SOURCE_JSON = "{"
            + "\"id\": \"2bc0accf-312e-429a-956e-e4407625eb62\", " + "\"direction\": \"ingress\", "
            + "\"protocol\": \"tcp\", " + "\"port_range_min\": 80, " + "\"port_range_max\": 80, "
            + "\"ethertype\": \"IPv4\", " + "\"remote_ip_prefix\": \"0.0.0.0/0\", "
            + "\"remote_group_id\": \"85cc3048-abc3-43cc-89b3-377341426ac5\", "
            + "\"security_group_id\": \"a7734e61-b545-452d-a3cd-0189cbd9747a\", "
            + "\"tenant_id\": \"e4f50856753b4dc6afee5fa6b9b6c550\" }";

    private static final String NEUTRON_SECURITY_RULE_PROTOCOL_NUMBER_SOURCE_JSON = "{"
            + "\"id\": \"2bc0accf-312e-429a-956e-e4407625eb62\", " + "\"direction\": \"ingress\", "
            + "\"protocol\": 6, " + "\"port_range_min\": 80, " + "\"port_range_max\": 80, "
            + "\"ethertype\": \"IPv4\", " + "\"remote_ip_prefix\": \"0.0.0.0/0\", "
            + "\"remote_group_id\": \"85cc3048-abc3-43cc-89b3-377341426ac5\", "
            + "\"security_group_id\": \"a7734e61-b545-452d-a3cd-0189cbd9747a\", "
            + "\"tenant_id\": \"e4f50856753b4dc6afee5fa6b9b6c550\" }";

    private static final String NEUTRON_SECURITY_RULE_PROTOCOL_NUMBER_STRING_SOURCE_JSON = "{"
            + "\"id\": \"2bc0accf-312e-429a-956e-e4407625eb62\", " + "\"direction\": \"ingress\", "
            + "\"protocol\": \"6\", " + "\"port_range_min\": 80, " + "\"port_range_max\": 80, "
            + "\"ethertype\": \"IPv4\", " + "\"remote_ip_prefix\": \"0.0.0.0/0\", "
            + "\"remote_group_id\": \"85cc3048-abc3-43cc-89b3-377341426ac5\", "
            + "\"security_group_id\": \"a7734e61-b545-452d-a3cd-0189cbd9747a\", "
            + "\"tenant_id\": \"e4f50856753b4dc6afee5fa6b9b6c550\" }";

    @Test
    public void test_NeutronSecurityRule_JAXB() throws JAXBException {
        NeutronSecurityRule testObject = JaxbTestHelper.jaxbUnmarshall(NeutronSecurityRule.class,
                NEUTRON_SECURITY_RULE_SOURCE_JSON);
        Assert.assertEquals("NeutronSecurityRule JAXB Test 1: Testing id failed",
                "2bc0accf-312e-429a-956e-e4407625eb62", testObject.getID());

        Assert.assertEquals("NeutronSecurityRule JAXB Test 2: Testing direction failed", "ingress",
                testObject.getSecurityRuleDirection());

        Assert.assertEquals("NeutronSecurityRule JAXB Test 3: Testing protocol failed", "tcp",
                testObject.getSecurityRuleProtocol());

        Assert.assertEquals("NeutronSecurityRule JAXB Test 4: Testing port range min failed", new Integer(80),
                testObject.getSecurityRulePortMin());

        Assert.assertEquals("NeutronSecurityRule JAXB Test 5: Testing port range max failed", new Integer(80),
                testObject.getSecurityRulePortMax());

        Assert.assertEquals("NeutronSecurityRule JAXB Test 6: Testing ethertype failed", "IPv4",
                testObject.getSecurityRuleEthertype());

        Assert.assertEquals("NeutronSecurityRule JAXB Test 7: Testing remote ip prefix failed", "0.0.0.0/0",
                testObject.getSecurityRuleRemoteIpPrefix());

        Assert.assertEquals("NeutronSecurityRule JAXB Test 8: Testing remote group id failed",
                "85cc3048-abc3-43cc-89b3-377341426ac5", testObject.getSecurityRemoteGroupID());

        Assert.assertEquals("NeutronSecurityRule JAXB Test 9: Testing security group id failed",
                "a7734e61-b545-452d-a3cd-0189cbd9747a", testObject.getSecurityRuleGroupID());

        Assert.assertEquals("NeutronSecurityRule JAXB Test 10: Testing tenant id failed",
                "e4f50856753b4dc6afee5fa6b9b6c550", testObject.getTenantID());
    }

    @Test
    public void test_NeutronSecurityRuleProtocolNumber_JAXB() throws JAXBException {
        NeutronSecurityRule testObject = JaxbTestHelper.jaxbUnmarshall(NeutronSecurityRule.class,
                NEUTRON_SECURITY_RULE_PROTOCOL_NUMBER_SOURCE_JSON);
        Assert.assertEquals("NeutronSecurityRuleProtocolNumber JAXB: Testing protocol failed", "6",
                testObject.getSecurityRuleProtocol());
    }

    @Test
    public void test_NeutronSecurityRuleProtocolNumberString_JAXB() throws JAXBException {
        NeutronSecurityRule testObject = JaxbTestHelper.jaxbUnmarshall(NeutronSecurityRule.class,
                NEUTRON_SECURITY_RULE_PROTOCOL_NUMBER_STRING_SOURCE_JSON);
        Assert.assertEquals("NeutronSecurityRuleProtocolNumberString JAXB: Testing protocol failed", "6",
                testObject.getSecurityRuleProtocol());
    }
}
