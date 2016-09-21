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

public class NeutronNetwork_SegmentJAXBTest {

    private static final String NeutronNetwork_Segment_sourceJson = "{ " + "\"provider:network_type\": \"vlan\", "
            + "\"provider:physical_network\": \"physnet1\", " + "\"provider:segmentation_id\": \"1001\" }";

    @Test
    public void test_NeutronNetwork_Segment_JAXB()  throws JAXBException {
        NeutronNetwork_Segment segmentObject = new NeutronNetwork_Segment();

        NeutronNetwork_Segment testObject = (NeutronNetwork_Segment) JaxbTestHelper.jaxbUnmarshall(segmentObject,
                NeutronNetwork_Segment_sourceJson);

        Assert.assertEquals("NeutronNetwork_Segment JAXB Test 1: Testing provider:network_type failed", "vlan",
                testObject.getProviderNetworkType());

        Assert.assertEquals("NeutronNetwork_Segment JAXB Test 2: Testing provider:physical_network failed",
                "physnet1", testObject.getProviderPhysicalNetwork());

        Assert.assertEquals("NeutronNetwork_Segment JAXB Test 3: Testing provider:segmentation_id failed", "1001",
                testObject.getProviderSegmentationID());
    }
}
