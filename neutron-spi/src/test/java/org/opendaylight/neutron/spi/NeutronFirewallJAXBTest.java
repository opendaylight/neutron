/*
 * Copyright (C) 2015 IBM, Inc.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.spi;

import javax.xml.bind.JAXBException;
import org.junit.Assert;
import org.junit.Test;

public class NeutronFirewallJAXBTest {

    private static final String NEUTRON_FIREWALL_SOURCE_JSON = "{ \"id\": \"d6220bbb-35f3-48ab-8eae-69c60aef3546\", "
            + "\"tenant_id\": \"aa902936679e4ea29bfe1158e3450a13\", \"name\": \"jaxb-test\", "
            + "\"description\": \"Test of NeutronFirewall JAXB\", \"admin_state_up\": false, \"status\": \"DOWN\", "
            + "\"shared\": true, \"firewall_policy_id\": \"83ca694a-eeff-48e5-b2d8-fe5198cf2e86\" }";

    @Test
    public void test_NeutronFirewall_JAXB() throws JAXBException {
        NeutronFirewall dummyObject = new NeutronFirewall();

        NeutronFirewall testObject = (NeutronFirewall) JaxbTestHelper.jaxbUnmarshall(dummyObject,
                NEUTRON_FIREWALL_SOURCE_JSON);
        Assert.assertEquals("NeutronFirewall JAXB Test 1: Testing id failed",
                "d6220bbb-35f3-48ab-8eae-69c60aef3546", testObject.getID());

        Assert.assertEquals("NeutronFirewall JAXB Test 3: Testing name failed", "jaxb-test",
                testObject.getName());

        Assert.assertFalse("NeutronFirewall JAXB Test 5: Testing admin_state_up failed",
                testObject.getFirewallAdminStateIsUp());

        Assert.assertTrue("NeutronFirewall JAXB Test 7: Testing shared failed", testObject.getFirewallIsShared());

        Assert.assertEquals("NeutronFirewall JAXB Test 8: Testing firewall_policy_id failed",
                "83ca694a-eeff-48e5-b2d8-fe5198cf2e86", testObject.getFirewallPolicyID());

        Assert.assertEquals("NeutronFirewall JAXB Test 2: Testing tenant_id failed",
                "aa902936679e4ea29bfe1158e3450a13", testObject.getTenantID());
    }

    private static final String NEUTRON_FIREWALL_POLICY_SOURCE_JSON =
            "{ \"id\": \"d6220bbb-35f3-48ab-8eae-69c60aef3546\", "
            + "\"tenant_id\": \"aa902936679e4ea29bfe1158e3450a13\", \"name\": \"jaxb-test\", "
            + "\"description\": \"Test of NeutronFirewallPolicy JAXB\", \"shared\": true, \"audited\": true, "
            + "\"firewall_rules\": [ \"83ca694a-eeff-48e5-b2d8-fe5198cf2e86\" ] }";

    @Test
    public void test_NeutronFirewallPolicy_JAXB() throws JAXBException {
        NeutronFirewallPolicy dummyObject = new NeutronFirewallPolicy();

        NeutronFirewallPolicy testObject = (NeutronFirewallPolicy) JaxbTestHelper.jaxbUnmarshall(dummyObject,
                NEUTRON_FIREWALL_POLICY_SOURCE_JSON);
        Assert.assertEquals("NeutronFirewallPolicy JAXB Test 1: Testing id failed",
                "d6220bbb-35f3-48ab-8eae-69c60aef3546", testObject.getID());

        Assert.assertEquals("NeutronFirewallPolicy JAXB Test 3: Testing name failed", "jaxb-test",
                testObject.getName());

        Assert.assertTrue("NeutronFirewallPolicy JAXB Test 5: Testing shared failed",
                testObject.getFirewallPolicyIsShared());

        Assert.assertTrue("NeutronFirewallPolicy JAXB Test 6: Testing audited failed",
                testObject.getFirewallPolicyIsAudited());

        Assert.assertEquals("NeutronFirewallPolicy JAXB Test 2: Testing tenant_id failed",
                "aa902936679e4ea29bfe1158e3450a13", testObject.getTenantID());
    }

    private static final String NEUTRON_FIREWALL_RULE_SOURCE_JSON = "{ \"action\": \"allow\", "
            + "\"description\": \"Test of NeutronFirewallRule JAXB\", \"destination_ip_address\": \"10.10.10.10\", "
            + "\"destination_port_range_min\": \"80\", \"destination_port_range_max\": \"81\", \"enabled\": true, "
            + "\"firewall_policy_id\": \"83ca694a-eeff-48e5-b2d8-fe5198cf2e86\", "
            + "\"id\": \"8722e0e0-9cc9-4490-9660-8c9a5732fbb0\", \"ip_version\": 4, \"name\": \"ALLOW_HTTP\", "
            + "\"position\": 1, \"protocol\": \"tcp\", \"shared\": false, \"source_ip_address\": \"10.10.10.8\", "
            + "\"source_port\": null, \"tenant_id\": \"45977fa2dbd7482098dd68d0d8970117\" }";

    @Test
    public void test_NeutronFirewallRule_JAXB() throws JAXBException {
        NeutronFirewallRule dummyObject = new NeutronFirewallRule();

        NeutronFirewallRule testObject = (NeutronFirewallRule) JaxbTestHelper.jaxbUnmarshall(dummyObject,
                NEUTRON_FIREWALL_RULE_SOURCE_JSON);
        Assert.assertEquals("NeutronFirewallRule JAXB Test 1: Testing id failed",
                "8722e0e0-9cc9-4490-9660-8c9a5732fbb0", testObject.getID());

        Assert.assertEquals("NeutronFirewallRule JAXB Test 3: Testing name failed", "ALLOW_HTTP",
                testObject.getName());

        Assert.assertEquals("NeutronFirewallRule JAXB Test 5: Testing firewaulPolicyRuleID failed",
                "83ca694a-eeff-48e5-b2d8-fe5198cf2e86", testObject.getFirewallRulePolicyID());

        Assert.assertFalse("NeutronFirewallRule JAXB Test 6: Testing shared failed",
                testObject.getFirewallRuleIsShared());

        Assert.assertEquals("NeutronFirewallRule JAXB Test 7: Testing protocol failed", "tcp",
                testObject.getFirewallRuleProtocol());

        Assert.assertEquals("NeutronFirewallRule JAXB Test 8: Testing IP version failed", new Integer(4),
                testObject.getFirewallRuleIpVer());

        Assert.assertEquals("NeutronFirewallRule JAXB Test 9: Testing source IP address failed", "10.10.10.8",
                testObject.getFirewallRuleSrcIpAddr());

        Assert.assertEquals("NeutronFirewallRule JAXB Test 10: Testing destination IP address failed",
                "10.10.10.10", testObject.getFirewallRuleDstIpAddr());

        Assert.assertEquals("NeutronFirewallRule JAXB Test 11.1: Testing source port min failed", null,
                testObject.getFirewallRuleSrcPortRangeMin());

        Assert.assertEquals("NeutronFirewallRule JAXB Test 11.2: Testing source port failed", null,
                testObject.getFirewallRuleSrcPortRangeMax());

        Assert.assertEquals("NeutronFirewallRule JAXB Test 12.1: Testing destination port min failed",
                new Integer(80), testObject.getFirewallRuleDstPortRangeMin());

        Assert.assertEquals("NeutronFirewallRule JAXB Test 12.2: Testing destination port max failed",
                new Integer(81), testObject.getFirewallRuleDstPortRangeMax());

        Assert.assertEquals("NeutronFirewallRule JAXB Test 13: Testing position failed", new Integer(1),
                testObject.getFirewallRulePosition());

        Assert.assertEquals("NeutronFirewallRule JAXB Test 14: Testing action", "allow",
                testObject.getFirewallRuleAction());

        Assert.assertTrue("NeutronFirewallRule JAXB Test 15: Testing enabled failed",
                testObject.getFirewallRuleIsEnabled());

        Assert.assertEquals("NeutronFirewallRule JAXB Test 2: Testing tenant_id failed",
                "45977fa2dbd7482098dd68d0d8970117", testObject.getTenantID());
    }
}
