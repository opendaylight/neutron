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

public class NeutronMeteringLabelRuleJAXBTest {

    private static final String NEUTRON_METERING_LABEL_RULE_SOURCE_JSON = "{ "
            + "\"metering_label_id\": \"e131d186-b02d-4c0b-83d5-0c0725c4f812\", "
            + "\"remote_ip_prefix\": \"10.0.0.0/24\", " + "\"direction\": \"ingress\", " + "\"excluded\": false , "
            + "\"id\": \"4e8e5957-649f-477b-9e5b-f1f75b21c03c\" }";

    @Test
    public void test_NeutronMeteringLabelRule_JAXB()  throws JAXBException {
        NeutronMeteringLabelRule testObject = JaxbTestHelper
                .jaxbUnmarshall(NeutronMeteringLabelRule.class, NEUTRON_METERING_LABEL_RULE_SOURCE_JSON);
        Assert.assertEquals("NeutronMeteringLabelRule JAXB Test 1: Testing metering_label_id failed",
                "e131d186-b02d-4c0b-83d5-0c0725c4f812", testObject.getMeteringLabelRuleLabelID());

        Assert.assertEquals("NeutronMeteringLabelRule JAXB Test 2: Testing remote_ip_prefix failed", "10.0.0.0/24",
                testObject.getMeteringLabelRuleRemoteIpPrefix());

        Assert.assertEquals("NeutronMeteringLabelRule JAXB Test 3: Testing direction failed", "ingress",
                testObject.getMeteringLabelRuleDirection());

        Assert.assertEquals("NeutronMeteringLabelRule JAXB Test 4: Testing excluded failed", false,
                testObject.getMeteringLabelRuleExcluded());

        Assert.assertEquals("NeutronMeteringLabelRule JAXB Test 5: Testing id failed",
                "4e8e5957-649f-477b-9e5b-f1f75b21c03c", testObject.getID());
    }
}
