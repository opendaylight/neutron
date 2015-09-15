/*
 * Copyright Tata Consultancy Services, 2015.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.spi;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class NeutronSecurityGroupJAXBTest {
    private static final String NeutronSecurityGroup_sourceJson = "{" +
        "\"id\": \"2076db17-a522-4506-91de-c6dd8e837028\", " +
        "\"name\": \"new-webservers\", " +
        "\"description\": \"security group for webservers\", " +
        "\"tenant_id\": \"b4f50856753b4dc6afee5fa6b9b6c550\", " +
        "\"security_group_rules\": [ { \"id\": \"2bc0accf-312e-429a-956e-e4407625eb62\" , " +
                                       "\"direction\": \"ingress\" , " +
                                       "\"protocol\": \"tcp\" , " +
                                       "\"port_range_min\": 80 , " +
                                       "\"port_range_max\": 80 , " +
                                       "\"ethertype\": \"IPv4\" ," +
                                       "\"remote_ip_prefix\": \"0.0.0.0/0\" , " +
                                       "\"remote_group_id\": \"85cc3048-abc3-43cc-89b3-377341426ac5\" , " +
                                       "\"security_group_id\": \"a7734e61-b545-452d-a3cd-0189cbd9747a\" , " +
                                       "\"tenant_id\": \"e4f50856753b4dc6afee5fa6b9b6c550\" } ]" +
        "}";
    @Test
    public void test_NeutronSecurityGroup_JAXB() {
        NeutronSecurityGroup dummyObject = new NeutronSecurityGroup();
        try {
            NeutronSecurityGroup testObject = (NeutronSecurityGroup) JaxbTestHelper.jaxbUnmarshall(dummyObject, NeutronSecurityGroup_sourceJson);
            List<NeutronSecurityRule> securityRules = testObject.getSecurityRules();
            Assert.assertEquals("NeutronSecurityGroup JAXB Test 1: Testing id failed",
                  "2076db17-a522-4506-91de-c6dd8e837028", testObject.getID());

            Assert.assertEquals("NeutronSecurityGroup JAXB Test 2: Testing direction failed",
                  "new-webservers", testObject.getSecurityGroupName());

            Assert.assertEquals("NeutronSecurityGroup JAXB Test 3: Testing protocol failed",
                  "security group for webservers", testObject.getSecurityGroupDescription());

            Assert.assertEquals("NeutronSecurityGroup JAXB Test 4: Testing port range min failed",
                    "b4f50856753b4dc6afee5fa6b9b6c550", testObject.getTenantID());

            Assert.assertEquals("NeutronSecurityGroup JAXB Test 5.1: Testing security group rules list length failed",
                    1, securityRules.size());
            Assert.assertEquals("NeutronSecurityGroup JAXB Test 5.2: Testing security group rules id value failed",
                    "2bc0accf-312e-429a-956e-e4407625eb62", securityRules.get(0).uuid);
            Assert.assertEquals("NeutronSecurityGroup JAXB Test 5.3: Testing security group rules direction value failed",
                    "ingress", securityRules.get(0).securityRuleDirection);
            Assert.assertEquals("NeutronSecurityGroup JAXB Test 5.4: Testing security group rules protocol value failed",
                    "tcp", securityRules.get(0).securityRuleProtocol);
            Assert.assertEquals("NeutronSecurityGroup JAXB Test 5.5: Testing security group rules port range min value failed",
                    new Integer(80), securityRules.get(0).securityRulePortMin);
            Assert.assertEquals("NeutronSecurityGroup JAXB Test 5.6: Testing security group rules port range max value failed",
                    new Integer(80), securityRules.get(0).securityRulePortMax);
            Assert.assertEquals("NeutronSecurityGroup JAXB Test 5.7: Testing security group rules ethertype value failed",
                    "IPv4", securityRules.get(0).securityRuleEthertype);
            Assert.assertEquals("NeutronSecurityGroup JAXB Test 5.8: Testing security group rules remote ip prefix value failed",
                    "0.0.0.0/0", securityRules.get(0).securityRuleRemoteIpPrefix);
            Assert.assertEquals("NeutronSecurityGroup JAXB Test 5.9: Testing security group rules remote group id value failed",
                    "85cc3048-abc3-43cc-89b3-377341426ac5", securityRules.get(0).securityRemoteGroupID);
            Assert.assertEquals("NeutronSecurityGroup JAXB Test 5.10: Testing security group rules security group id value failed",
                    "a7734e61-b545-452d-a3cd-0189cbd9747a", securityRules.get(0).securityRuleGroupID);
            Assert.assertEquals("NeutronSecurityGroup JAXB Test 5.11: Testing security group rules tenant id value failed",
                    "e4f50856753b4dc6afee5fa6b9b6c550", securityRules.get(0).tenantID);
        } catch (Exception e) {
            Assert.fail("Tests failed");
        }
    }
}
