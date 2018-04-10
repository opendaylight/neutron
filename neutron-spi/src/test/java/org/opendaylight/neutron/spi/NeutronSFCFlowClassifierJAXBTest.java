/*
 * Copyright (c) 2016 Brocade Communications Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.neutron.spi;

import java.util.Map;
import javax.xml.bind.JAXBException;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Anil Vishnoi (avishnoi@Brocade.com) on 6/24/16.
 */
public class NeutronSFCFlowClassifierJAXBTest {

    private static final String NEUTRON_SFC_FLOW_CLASSIFIER_SOURCE_JSON = "{ " + "\"name\": \"flowclassifier1\", "
            + "\"ethertype\": \"IPv4\", " + "\"protocol\": \"UDP\", " + "\"source_port_range_min\": 100, "
            + "\"source_port_range_max\": 200, " + "\"destination_port_range_min\": 100, "
            + "\"destination_port_range_max\": 200, " + "\"source_ip_prefix\": \"10.0.0.0/24\", "
            + "\"destination_ip_prefix\": \"11.0.0.0/24\", "
            + "\"logical_source_port\": \"5e8e5957-649f-477b-9e5b-f1f75b21c03c\", "
            + "\"logical_destination_port\": \"6e8e5957-649f-477b-9e5b-f1f75b21c03c\", " + "\"l7_parameters\": [ "
            + "{ " + "\"Key\": \"value\" " + "} " + "], " + "\"tenant_id\": \"4969c491a3c74ee4af974e6d800c62de\", "
            + "\"id\": \"4e8e5957-649f-477b-9e5b-f1f75b21c03c\" }";

    @Test
    public void test_NeutronSFCFlowClassifier_JAXB() throws JAXBException {
        NeutronSFCFlowClassifier neutronObject = JaxbTestHelper.jaxbUnmarshall(NeutronSFCFlowClassifier.class,
                NEUTRON_SFC_FLOW_CLASSIFIER_SOURCE_JSON);

        Assert.assertEquals("NeutronSFCFlowClassifier JAXB Test 1: Testing id failed",
                "4e8e5957-649f-477b-9e5b-f1f75b21c03c", neutronObject.getID());

        Assert.assertEquals("NeutronSFCFlowClassifier JAXB Test 3: Testing ethertype failed", "IPv4",
                neutronObject.getEthertype());

        Assert.assertEquals("NeutronSFCFlowClassifier JAXB Test 4: Testing protocol failed", "UDP",
                neutronObject.getProtocol());

        Assert.assertEquals("NeutronSFCFlowClassifier JAXB Test 5: Testing source_port_range_min failed",
                new Integer(100), neutronObject.getSourcePortRangeMin());

        Assert.assertEquals("NeutronSFCFlowClassifier JAXB Test 6: Testing source_port_range_max failed",
                new Integer(200), neutronObject.getSourcePortRangeMax());

        Assert.assertEquals("NeutronSFCFlowClassifier JAXB Test 7: Testing destination_port_range_min failed",
                new Integer(100), neutronObject.getDestinationPortRangeMin());

        Assert.assertEquals("NeutronSFCFlowClassifier JAXB Test 8: Testing destination_port_range_max failed",
                new Integer(200), neutronObject.getDestinationPortRangeMax());

        Assert.assertEquals("NeutronSFCFlowClassifier JAXB Test 9: Testing source_ip_prefix failed", "10.0.0.0/24",
                neutronObject.getSourceIpPrefix());

        Assert.assertEquals("NeutronSFCFlowClassifier JAXB Test 10: Testing destination_ip_prefix failed",
                "11.0.0.0/24", neutronObject.getDestinationIpPrefix());

        Assert.assertEquals("NeutronSFCFlowClassifier JAXB Test 11: Testing logical_source_port failed",
                "5e8e5957-649f-477b-9e5b-f1f75b21c03c", neutronObject.getLogicalSourcePortUUID());

        Assert.assertEquals("NeutronSFCFlowClassifier JAXB Test 12: Testing logical_destination_port failed",
                "6e8e5957-649f-477b-9e5b-f1f75b21c03c", neutronObject.getLogicalDestinationPortUUID());

        Map<String, String> l7Param = neutronObject.getL7Parameters();
        Assert.assertEquals("NeutronSFCFlowClassifier JAXB Test 13: Testing L7_parameters list length failed", 1,
                l7Param.size());

        Assert.assertEquals("NeutronSFCFlowClassifier JAXB Test 2: Testing tenant_id failed",
                "4969c491a3c74ee4af974e6d800c62de", neutronObject.getTenantID());
    }
}
