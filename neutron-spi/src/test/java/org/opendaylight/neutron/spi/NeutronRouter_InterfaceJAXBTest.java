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

public class NeutronRouter_InterfaceJAXBTest {
    private static final String NEUTRON_ROUTER_INTERFACE_SOURCE_JSON = "{"
            + "\"subnet_id\": \"3b80198d-4f7b-4f77-9ef5-774d54e17126\", "
            + "\"port_id\": \"65c0ee9f-d634-4522-8954-51021b570b0d\", "
            + "\"id\": \"7370da7f-234e-3f7a-ed12-e384131d886d\" }";

    @Test
    public void test_NeutronRouter_Interface_JAXB() throws JAXBException {
        NeutronRouter_Interface dummyObject = new NeutronRouter_Interface();

        NeutronRouter_Interface testObject = (NeutronRouter_Interface) JaxbTestHelper.jaxbUnmarshall(dummyObject,
                NEUTRON_ROUTER_INTERFACE_SOURCE_JSON);

        Assert.assertEquals("NeutronRouter_Interface JAXB Test 1: Testing subnet_id failed",
                "3b80198d-4f7b-4f77-9ef5-774d54e17126", testObject.getSubnetUUID());

        Assert.assertEquals("NeutronRouter_Interface JAXB Test 2: Testing port_id failed",
                "65c0ee9f-d634-4522-8954-51021b570b0d", testObject.portUUID);

        Assert.assertEquals("NeutronRouter_Interface JAXB Test 3: Testing id failed",
                "7370da7f-234e-3f7a-ed12-e384131d886d", testObject.getID());
    }
}
