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
import javax.xml.bind.JAXBException;
import org.junit.Assert;
import org.junit.Test;

public class NeutronQosJAXBTest {

    private static final String NeutronQosPolicy_sourceJson = "{" + "\"id\": \"d6220bbb-35f3-48ab-8eae-69c60aef3546\","
            + "\"tenant_id\": \"aa902936679e4ea29bfe1158e3450a13\"," + "\"name\": \"jaxb-test\", "
            + " \"shared\": false," + "\"rule_type\": \"rule\", "
            + "\"bandwidth_limit_rules\": [ {\"id\": \"d6220bbb-35f3-48ab-8eae-69c60aef3547\", "
            + "\"tenant_id\": \"aa902936679e4ea29bfe1158e3450a14\"," + "\"max_kbps\": 25, "
            + "\"max_burst_kbps\": 100 } ],"
            + "\"dscp_marking_rules\": [ {\"id\": \"d6220bbb-35f3-48ab-8eae-69c60aef3547\","
            + " \"tenant_id\": \"aa902936679e4ea29bfe1158e3450a14\", " + "\"dscp_mark\": 8 } ] " + "}";

    @Test
    public void test_NeutronQosPolicy_JAXB() throws JAXBException {
        NeutronQosPolicy testObject = new NeutronQosPolicy();
        NeutronQosPolicy neutronObject = (NeutronQosPolicy) JaxbTestHelper.jaxbUnmarshall(testObject,
                NeutronQosPolicy_sourceJson);
        Assert.assertEquals("NeutronQosPolicy JAXB Test 1: Testing id failed", "d6220bbb-35f3-48ab-8eae-69c60aef3546",
                neutronObject.getID());

        Assert.assertEquals("NeutronQosPolicy JAXB Test 2: Testing tenant_id failed",
                "aa902936679e4ea29bfe1158e3450a13", neutronObject.getTenantID());

        Assert.assertEquals("NeutronQosPolicy JAXB Test 3 : Testing Name failed", "jaxb-test",
                neutronObject.getQosPolicyName());

        Assert.assertFalse("NeutronQosPolicy JaxB Test 4 : Testing Shared failed", neutronObject.getPolicyIsShared());

        List<NeutronQosBandwidthRule> bwPolicyRules = neutronObject.getBwLimitRules();

        Assert.assertEquals("NeutronQosPolicy JAXB Test 5.0: Testing Bandwidth Policy length failed", 1,
                bwPolicyRules.size());

        Assert.assertEquals("NeutronQosPolicy JaxB Test 5.1 : Testing ID failed",
                "d6220bbb-35f3-48ab-8eae-69c60aef3547", bwPolicyRules.get(0).uuid);

        Assert.assertEquals("NeutronQosPolicy JaxB Test 5.2 : Testing Tenant ID failed",
                "aa902936679e4ea29bfe1158e3450a14", bwPolicyRules.get(0).tenantID);

        Assert.assertEquals("NeutronQosPolicy JaxB Test 5.3 : Testing Max ingress/Egress value failed",
                new BigInteger("25"), bwPolicyRules.get(0).maxKbps);

        Assert.assertEquals("NeutronQosPolicy JaxB Test 5.4 : Testing Maximum Burst value failed",
                new BigInteger("100"), bwPolicyRules.get(0).maxBurstKbps);

        List<NeutronQosDscpMarkingRule> dscpPolicyRules = neutronObject.getDscpRules();

        Assert.assertEquals("NeutronQosPolicy JAXB Test 6.0: Testing Bandwidth Policy length failed", 1,
                dscpPolicyRules.size());

        Assert.assertEquals("NeutronQosPolicy JaxB Test 6.1 : Testing ID failed",
                "d6220bbb-35f3-48ab-8eae-69c60aef3547", dscpPolicyRules.get(0).uuid);

        Assert.assertEquals("NeutronQosPolicy JaxB Test 6.2 : Testing Tenant ID failed",
                "aa902936679e4ea29bfe1158e3450a14", dscpPolicyRules.get(0).tenantID);

        Assert.assertEquals("NeutronQosPolicy JaxB Test 6.3 : Testing Max ingress/Egress value failed", new Short("8"),
                dscpPolicyRules.get(0).dscpMark);
    }
}
