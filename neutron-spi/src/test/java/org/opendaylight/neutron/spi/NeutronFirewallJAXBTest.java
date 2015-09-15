/*
 * Copyright (C) 2015 IBM, Inc.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.spi;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import org.opendaylight.neutron.spi.NeutronFirewall;
import org.opendaylight.neutron.spi.NeutronFirewallPolicy;
import org.opendaylight.neutron.spi.NeutronFirewallRule;
import org.opendaylight.neutron.spi.JaxbTestHelper;

public class NeutronFirewallJAXBTest {

    private static final String NeutronFirewall_sourceJson="{ \"id\": \"d6220bbb-35f3-48ab-8eae-69c60aef3546\", \"tenant_id\": \"aa902936679e4ea29bfe1158e3450a13\", \"name\": \"jaxb-test\", \"description\": \"Test of NeutronFirewall JAXB\", \"admin_state_up\": false, \"status\": \"DOWN\", \"shared\": true, \"firewall_policy_id\": \"83ca694a-eeff-48e5-b2d8-fe5198cf2e86\" }";

    @Test
    public void test_NeutronFirewall_JAXB() {
        NeutronFirewall dummyObject = new NeutronFirewall();
        try {
            NeutronFirewall testObject = (NeutronFirewall) JaxbTestHelper.jaxbUnmarshall(dummyObject, NeutronFirewall_sourceJson);
            Assert.assertEquals("NeutronFirewall JAXB Test 1: Testing id failed",
                  "d6220bbb-35f3-48ab-8eae-69c60aef3546", testObject.getID());

            Assert.assertEquals("NeutronFirewall JAXB Test 2: Testing tenant_id failed",
                  "aa902936679e4ea29bfe1158e3450a13", testObject.getTenantID());

            Assert.assertEquals("NeutronFirewall JAXB Test 3: Testing name failed",
                  "jaxb-test", testObject.getFirewallName());

            Assert.assertEquals("NeutronFirewall JAXB Test 4: Testing description failed",
                  "Test of NeutronFirewall JAXB", testObject.getFirewallDescription());

            Assert.assertFalse("NeutronFirewall JAXB Test 5: Testing admin_state_up failed",
                  testObject.getFirewallAdminStateIsUp());

            Assert.assertEquals("NeutronFirewall JAXB Test 6: Testing status failed",
                  "DOWN", testObject.getFirewallStatus());

            Assert.assertTrue("NeutronFirewall JAXB Test 7: Testing shared failed",
                  testObject.getFirewallIsShared());

            Assert.assertEquals("NeutronFirewall JAXB Test 8: Testing firewall_policy_id failed",
                  "83ca694a-eeff-48e5-b2d8-fe5198cf2e86", testObject.getFirewallPolicyID());
        } catch (Exception e) {
            Assert.assertTrue("Tests failed", false);
        }
    }

    private static final String NeutronFirewallPolicy_sourceJson="{ \"id\": \"d6220bbb-35f3-48ab-8eae-69c60aef3546\", \"tenant_id\": \"aa902936679e4ea29bfe1158e3450a13\", \"name\": \"jaxb-test\", \"description\": \"Test of NeutronFirewallPolicy JAXB\", \"shared\": true, \"audited\": true, \"firewall_rules\": [ \"83ca694a-eeff-48e5-b2d8-fe5198cf2e86\" ] }";

    @Test
    public void test_NeutronFirewallPolicy_JAXB() {
        NeutronFirewallPolicy dummyObject = new NeutronFirewallPolicy();
        try {
            NeutronFirewallPolicy testObject = (NeutronFirewallPolicy) JaxbTestHelper.jaxbUnmarshall(dummyObject, NeutronFirewallPolicy_sourceJson);
            Assert.assertEquals("NeutronFirewallPolicy JAXB Test 1: Testing id failed",
                  "d6220bbb-35f3-48ab-8eae-69c60aef3546", testObject.getID());

            Assert.assertEquals("NeutronFirewallPolicy JAXB Test 2: Testing tenant_id failed",
                  "aa902936679e4ea29bfe1158e3450a13", testObject.getTenantID());

            Assert.assertEquals("NeutronFirewallPolicy JAXB Test 3: Testing name failed",
                  "jaxb-test", testObject.getFirewallPolicyName());

            Assert.assertEquals("NeutronFirewallPolicy JAXB Test 4: Testing description failed",
                  "Test of NeutronFirewallPolicy JAXB", testObject.getFirewallPolicyDescription());

            Assert.assertTrue("NeutronFirewallPolicy JAXB Test 5: Testing shared failed",
                  testObject.getFirewallPolicyIsShared());

            Assert.assertTrue("NeutronFirewallPolicy JAXB Test 6: Testing audited failed",
                  testObject.getFirewallPolicyIsAudited());

            List<String> policyRules = testObject.getFirewallPolicyRules();
            Assert.assertEquals("NeutronFirewallPolicy JAXB Test 7.1: Testing firewall policy rules length failed",
                  1, policyRules.size());
            Assert.assertEquals("NeutronFirewallPolicy JAXB Test 7.2: Testing firewall policy rules content failed", 
                  "83ca694a-eeff-48e5-b2d8-fe5198cf2e86", policyRules.get(0));
        } catch (Exception e) {
            Assert.assertTrue("Tests failed", false);
        }
    }

    private static final String NeutronFirewallRule_sourceJson="{ \"action\": \"allow\", \"description\": \"Test of NeutronFirewallRule JAXB\", \"destination_ip_address\": \"10.10.10.10\", \"destination_port\": \"80\", \"enabled\": true, \"firewall_policy_id\": \"83ca694a-eeff-48e5-b2d8-fe5198cf2e86\", \"id\": \"8722e0e0-9cc9-4490-9660-8c9a5732fbb0\", \"ip_version\": 4, \"name\": \"ALLOW_HTTP\", \"position\": 1, \"protocol\": \"tcp\", \"shared\": false, \"source_ip_address\": \"10.10.10.8\", \"source_port\": null, \"tenant_id\": \"45977fa2dbd7482098dd68d0d8970117\" }";

    @Test
    public void test_NeutronFirewallRule_JAXB() {
        NeutronFirewallRule dummyObject = new NeutronFirewallRule();
        try {
            NeutronFirewallRule testObject = (NeutronFirewallRule) JaxbTestHelper.jaxbUnmarshall(dummyObject, NeutronFirewallRule_sourceJson);
            Assert.assertEquals("NeutronFirewallRule JAXB Test 1: Testing id failed",
                  "8722e0e0-9cc9-4490-9660-8c9a5732fbb0", testObject.getID());

            Assert.assertEquals("NeutronFirewallRule JAXB Test 2: Testing tenant_id failed",
                  "45977fa2dbd7482098dd68d0d8970117", testObject.getTenantID());

            Assert.assertEquals("NeutronFirewallRule JAXB Test 3: Testing name failed",
                  "ALLOW_HTTP", testObject.getFirewallRuleName());

            Assert.assertEquals("NeutronFirewallRule JAXB Test 4: Testing description failed",
                  "Test of NeutronFirewallRule JAXB", testObject.getFirewallRuleDescription());

            Assert.assertEquals("NeutronFirewallRule JAXB Test 5: Testing firewaulPolicyRuleID failed",
                  "83ca694a-eeff-48e5-b2d8-fe5198cf2e86", testObject.getFirewallRulePolicyID());

            Assert.assertFalse("NeutronFirewallRule JAXB Test 6: Testing shared failed",
                  testObject.getFirewallRuleIsShared());

            Assert.assertEquals("NeutronFirewallRule JAXB Test 7: Testing protocol failed",
                  "tcp", testObject.getFirewallRuleProtocol());

            Assert.assertEquals("NeutronFirewallRule JAXB Test 8: Testing IP version failed",
                  new Integer(4), testObject.getFirewallRuleIpVer());

            Assert.assertEquals("NeutronFirewallRule JAXB Test 9: Testing source IP address failed",
                  "10.10.10.8", testObject.getFirewallRuleSrcIpAddr());

            Assert.assertEquals("NeutronFirewallRule JAXB Test 10: Testing destination IP address failed",
                  "10.10.10.10", testObject.getFirewallRuleDstIpAddr());

            Assert.assertEquals("NeutronFirewallRule JAXB Test 11: Testing source port failed",
                  null, testObject.getFirewallRuleSrcPort());

            Assert.assertEquals("NeutronFirewallRule JAXB Test 12: Testing destination port failed",
                  new Integer(80), testObject.getFirewallRuleDstPort());

            Assert.assertEquals("NeutronFirewallRule JAXB Test 13: Testing position failed",
                  new Integer(1), testObject.getFirewallRulePosition());

            Assert.assertEquals("NeutronFirewallRule JAXB Test 14: Testing action",
                  "allow", testObject.getFirewallRuleAction());

            Assert.assertTrue("NeutronFirewallRule JAXB Test 15: Testing enabled failed",
                  testObject.getFirewallRuleIsEnabled());
        } catch (Exception e) {
            Assert.assertTrue("Tests failed", false);
        }
    }
}
