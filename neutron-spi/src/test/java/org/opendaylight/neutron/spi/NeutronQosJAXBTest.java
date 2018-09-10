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

    private static final String NEUTRON_QOS_POLICY_SOURCE_JSON = "{"
            + "\"id\": \"d6220bbb-35f3-48ab-8eae-69c60aef3546\","
            + "\"tenant_id\": \"aa902936679e4ea29bfe1158e3450a13\"," + "\"name\": \"jaxb-test\", "
            + " \"shared\": false," + "\"rule_type\": \"rule\", "
            + "\"bandwidth_limit_rules\": [ {\"id\": \"d6220bbb-35f3-48ab-8eae-69c60aef3547\", "
            + "\"tenant_id\": \"aa902936679e4ea29bfe1158e3450a14\"," + "\"max_kbps\": 25, "
            + "\"max_burst_kbps\": 100 } ],"
            + "\"dscp_marking_rules\": [ {\"id\": \"d6220bbb-35f3-48ab-8eae-69c60aef3547\","
            + " \"tenant_id\": \"aa902936679e4ea29bfe1158e3450a14\", " + "\"dscp_mark\": 8 } ],"
            + "\"minimum_bandwidth_rules\": [ {\"id\": \"d6220bbb-35f3-48ab-8eae-69c60aef3547\","
            + "\"tenant_id\": \"aa902936679e4ea29bfe1158e3450a14\", " + "\"min_kbps\": 20,"
            + "\"direction\": \"egress\" } ]" + "}";

    @Test
    public void test_NeutronQosPolicy_JAXB() throws JAXBException {
        NeutronQosPolicy testObject = new NeutronQosPolicy();
        NeutronQosPolicy neutronObject = (NeutronQosPolicy) JaxbTestHelper.jaxbUnmarshall(testObject,
                NEUTRON_QOS_POLICY_SOURCE_JSON);
        Assert.assertEquals("NeutronQosPolicy JAXB Test 1: Testing id failed", "d6220bbb-35f3-48ab-8eae-69c60aef3546",
                neutronObject.getID());

        Assert.assertEquals("NeutronQosPolicy JAXB Test 2: Testing tenant_id failed",
                "aa902936679e4ea29bfe1158e3450a13", neutronObject.getTenantID());

        Assert.assertEquals("NeutronQosPolicy JAXB Test 3 : Testing Name failed", "jaxb-test",
                neutronObject.getName());

        Assert.assertFalse("NeutronQosPolicy JaxB Test 4 : Testing Shared failed", neutronObject.getPolicyIsShared());

        List<NeutronQosBandwidthLimitRule> bandwidthLimitPolicyRules = neutronObject.getBandwidthLimitRules();

        Assert.assertEquals("NeutronQosPolicy JAXB Test 5.0: Testing Bandwidth Policy length failed", 1,
                bandwidthLimitPolicyRules.size());

        Assert.assertEquals("NeutronQosPolicy JaxB Test 5.1 : Testing ID failed",
                "d6220bbb-35f3-48ab-8eae-69c60aef3547", bandwidthLimitPolicyRules.get(0).uuid);

        Assert.assertEquals("NeutronQosPolicy JaxB Test 5.2 : Testing Tenant ID failed",
                "aa902936679e4ea29bfe1158e3450a14", bandwidthLimitPolicyRules.get(0).getTenantID());

        Assert.assertEquals("NeutronQosPolicy JaxB Test 5.3 : Testing Max ingress/Egress value failed",
                new BigInteger("25"), bandwidthLimitPolicyRules.get(0).maxKbps);

        Assert.assertEquals("NeutronQosPolicy JaxB Test 5.4 : Testing Maximum Burst value failed",
                new BigInteger("100"), bandwidthLimitPolicyRules.get(0).maxBurstKbps);

        List<NeutronQosDscpMarkingRule> dscpMarkingPolicyRules = neutronObject.getDscpMarkingRules();

        Assert.assertEquals("NeutronQosPolicy JAXB Test 6.0: Testing Bandwidth Policy length failed", 1,
                dscpMarkingPolicyRules.size());

        Assert.assertEquals("NeutronQosPolicy JaxB Test 6.1 : Testing ID failed",
                "d6220bbb-35f3-48ab-8eae-69c60aef3547", dscpMarkingPolicyRules.get(0).uuid);

        Assert.assertEquals("NeutronQosPolicy JaxB Test 6.2 : Testing Tenant ID failed",
                "aa902936679e4ea29bfe1158e3450a14", dscpMarkingPolicyRules.get(0).getTenantID());

        Assert.assertEquals("NeutronQosPolicy JaxB Test 6.3 : Testing Max ingress/Egress value failed",
            new Short("8"), dscpMarkingPolicyRules.get(0).dscpMark);

        List<NeutronQosMinimumBandwidthRule> minBandwidthLimitRules = neutronObject.getMinimumBandwidthRules();

        Assert.assertEquals("NeutronQosPolicy JAXB Test 7.0: Testing Bandwidth Policy length failed", 1,
            minBandwidthLimitRules.size());

        Assert.assertEquals("NeutronQosPolicy JaxB Test 7.2 : Testing Tenant ID failed",
            "aa902936679e4ea29bfe1158e3450a14", minBandwidthLimitRules.get(0).getTenantID());

        Assert.assertEquals("NeutronQosPolicy JaxB Test 7.3 : Testing Minimum Bandwidth failed",
            new BigInteger("20"), minBandwidthLimitRules.get(0).minKbps);

        Assert.assertEquals("NeutronQosPolicy JaxB Test 7.4 : Testing Direction failed", "egress",
            minBandwidthLimitRules.get(0).direction);
    }
}
