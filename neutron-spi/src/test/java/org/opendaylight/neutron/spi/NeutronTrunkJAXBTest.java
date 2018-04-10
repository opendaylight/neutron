/*
 * Copyright (c) 2017 Ericsson India Global Services Pvt Ltd. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.spi;

import javax.xml.bind.JAXBException;
import org.junit.Assert;
import org.junit.Test;

public class NeutronTrunkJAXBTest {

    private static final String NEUTRON_TRUNK_SOURCE_JSON =
            "{\"status\":\"DOWN\",\"name\":\"trunk-jaxb-test\",\"admin_state_up\":true, "
            + "\"tenant_id\":\"cc3641789c8a4304abaa841c64f638d9\", "
            + "\"port_id\":\"60aac28d-1d3a-48d9-99bc-dd4bd62e50f2\", "
            + "\"sub_ports\":[{\"segmentation_type\":\"vlan\", "
            + "\"port_id\":\"dca33436-2a7c-415b-aa35-14769e7834e3\",\"segmentation_id\":101}, "
            + "{\"segmentation_type\":\"vlan\",\"port_id\":\"be28febe-bdff-45cc-8a2d-872d54e62527\", "
            + "\"segmentation_id\":102}],\"id\":\"c935240e-4aa6-496a-841c-d113c54499b9\"}";

    @Test
    public void test_NeutronTrunk_JAXB() throws JAXBException {
        NeutronTrunk neutronObject = JaxbTestHelper.jaxbUnmarshall(NeutronTrunk.class,
                NEUTRON_TRUNK_SOURCE_JSON);
        Assert.assertEquals("NeutronTrunk JAXB Test 1: Testing id failed", "c935240e-4aa6-496a-841c-d113c54499b9",
                neutronObject.getID());

        Assert.assertEquals("NeutronTrunk JAXB Test 2 : Testing Status failed", "DOWN",
                neutronObject.getStatus());

        Assert.assertEquals("NeutronTrunk JAXB Test 3 : Testing Name failed", "trunk-jaxb-test",
                neutronObject.getName());

        Assert.assertTrue("NeutronTrunk JAXB Test 4 : Testing AdminStateUp failed",
                neutronObject.getAdminStateUp());

        Assert.assertEquals("NeutronTrunk JAXB Test 6 : Testing PotId failed", "60aac28d-1d3a-48d9-99bc-dd4bd62e50f2",
                neutronObject.getPortId());

        Assert.assertEquals("NeutronTrunk JAXB Test 7 : Testing SubPorts size failed", 2,
                neutronObject.getSubPorts().size());

        NeutronTrunkSubPort subPort = neutronObject.getSubPorts().get(0);

        Assert.assertEquals("NeutronTrunk JAXB Test 8 : Testing SubPort Segmentation Type failed", "vlan",
                subPort.getSegmentationType());

        Assert.assertEquals("NeutronTrunk JAXB Test 9 : Testing SubPort Segmentation Id failed", "101",
                subPort.getSegmentationId());

        Assert.assertEquals("NeutronTrunk JAXB Test 10 : Testing SubPort Port Id failed",
                "dca33436-2a7c-415b-aa35-14769e7834e3",  subPort.getPortId());

        Assert.assertEquals("NeutronTrunk JAXB Test 5: Testing tenant_id failed",
                "cc3641789c8a4304abaa841c64f638d9", neutronObject.getTenantID());
    }
}
