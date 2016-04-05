/*
 * Copyright (C) 2016 Intel, Corp.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.neutron.spi;

import java.math.BigInteger;
import java.util.List;
import java.lang.*;

import org.junit.Assert;
import org.junit.Test;


public class NeutronQosJAXBTest {

    private static final String NeutronQosPolicy_sourceJson =
        "{" + "\"id\": \"d6220bbb-35f3-48ab-8eae-69c60aef3546\","
            + "\"tenant_id\": \"aa902936679e4ea29bfe1158e3450a13\"," + "\"name\" : \"jaxb-test\", "
            + " \"shared\": false," + "\"rule_type\" : \"rule\", "
            + "\"bandwidth_limit_rules\" : [ {\"id\":\"d6220bbb-35f3-48ab-8eae-69c60aef3547\", \"tenant_id\": \"aa902936679e4ea29bfe1158e3450a14\", \"qos_policy_id\": \"aa902936679e4ea29bfe1158e3450a14\",\"max_kbps\": 15, \"max_burst_kbps\": 25 } ],"
            + "\"dscp_marking_rules\" : [ {\"id\":\"d6220bbb-35f3-48ab-8eae-69c60aef3547\", \"tenant_id\": \"aa902936679e4ea29bfe1158e3450a14\", \"qos_policy_id\": \"aa902936679e4ea29bfe1158e3450a14\", \"dscp_mark\": 4 } ] } ";


    @Test public void test_NeutronQosPolicy_JAXB() {
        NeutronQosPolicy testObject = new NeutronQosPolicy();
        try {
            NeutronQosPolicy neutronObject = (NeutronQosPolicy) JaxbTestHelper
                .jaxbUnmarshall(testObject, NeutronQosPolicy_sourceJson);
            Assert.assertEquals("NeutronQosPolicy JAXB Test 1: Testing id failed",
                "d6220bbb-35f3-48ab-8eae-69c60aef3546", neutronObject.getID());

            Assert.assertEquals("NeutronQosPolicy JAXB Test 2: Testing tenant_id failed",
                "aa902936679e4ea29bfe1158e3450a13", neutronObject.getTenantID());

            Assert.assertEquals("NeutronQosPolicy JAXB Test 3 : Testing Name failed", "jaxb-test",
                neutronObject.getQosPolicyName());

            Assert.assertFalse("NeutronQosPolicy JaxB Test 4 : Testing Shared failed",
                neutronObject.getQosPolicyIsShared());

            List<NeutronQosBandwidthRule> bwPolicyRules = neutronObject.getQosBwLimitRules();

            Assert.assertEquals(
                "NeutronQosPolicy JAXB Test 5.0: Testing Bandwidth Policy length failed", 1,
                bwPolicyRules.size());

            Assert.assertEquals("NeutronQosPolicy JaxB Test 6 : Testing ID failed",
                "d6220bbb-35f3-48ab-8eae-69c60aef3547", bwPolicyRules.get(0).qosBandwidthRuleID);

            Assert.assertEquals("NeutronQosPolicy JaxB Test 6.1 : Testing Tenant ID failed",
                "aa902936679e4ea29bfe1158e3450a14", bwPolicyRules.get(0).tenantID);

            Assert.assertEquals("NeutronQosPolicy JaxB Test 6.2 : Testing Qos Policy ID failed",
                "aa902936679e4ea29bfe1158e3450a14", bwPolicyRules.get(0).qosPolicyID);

            Assert.assertEquals(
                "NeutronQosPolicy JaxB Test 6.3 : Testing Max ingress/Egress value failed",
                new BigInteger("15"), bwPolicyRules.get(0).getQosBandwidthMaxKbpsValue);

            Assert
                .assertEquals("NeutronQosPolicy JaxB Test 6.4 : Testing Maximum Burst value failed",
                    new BigInteger("25"), bwPolicyRules.get(0).qosBandwidthMaxBurstKbps);

            List<NeutronQosDscpMarkingRule> dscpPolicyRules = neutronObject.getQosDscpRules();

            Assert.assertEquals(
                "NeutronQosPolicy JAXB Test 5.0: Testing Bandwidth Policy length failed", 1,
                dscpPolicyRules.size());

            Assert.assertEquals("NeutronQosPolicy JaxB Test 6 : Testing ID failed",
                "d6220bbb-35f3-48ab-8eae-69c60aef3547", dscpPolicyRules.get(0).qosDscpRuleID);

            Assert.assertEquals("NeutronQosPolicy JaxB Test 6.1 : Testing Tenant ID failed",
                "aa902936679e4ea29bfe1158e3450a14", dscpPolicyRules.get(0).tenantID);

            Assert.assertEquals("NeutronQosPolicy JaxB Test 6.2 : Testing Qos Policy ID failed",
                "aa902936679e4ea29bfe1158e3450a14", dscpPolicyRules.get(0).qosPolicyID);

            Assert.assertEquals(
                "NeutronQosPolicy JaxB Test 6.3 : Testing Max ingress/Egress value failed",
                new Short("4"), dscpPolicyRules.get(0).qosDscpMark);

        } catch (Exception e) {
            Assert.assertTrue("Tests failed", false);
        }
    }
}
