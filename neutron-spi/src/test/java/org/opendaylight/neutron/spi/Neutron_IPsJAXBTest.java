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

public class Neutron_IPsJAXBTest {

    private static final String Neutron_IPs_sourceJson =
            "{ \"ip_address\": \"192.168.111.3\", " + "\"subnet_id\": \"22b44fc2-4ffb-4de4-b0f9-69d58b37ae27\" }";

    @Test
    public void test_Neutron_IPs_JAXB() throws JAXBException {
        Neutron_IPs neutronObject = new Neutron_IPs();
        Neutron_IPs testObject = (Neutron_IPs) JaxbTestHelper.jaxbUnmarshall(neutronObject, Neutron_IPs_sourceJson);

        Assert.assertEquals("Neutron_IPs JAXB Test 1: Testing ip_address failed", "192.168.111.3",
                    testObject.getIpAddress());

        Assert.assertEquals("Neutron_IPs JAXB Test 2: Testing subnet_id failed",
                    "22b44fc2-4ffb-4de4-b0f9-69d58b37ae27", testObject.getSubnetUUID());
    }
}
