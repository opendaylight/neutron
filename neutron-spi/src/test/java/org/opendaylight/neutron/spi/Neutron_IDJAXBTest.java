/*
 * Copyright (c) 2015 Tata Consultancy Services.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.spi;

import org.junit.Assert;
import org.junit.Test;

public class Neutron_IDJAXBTest {

    private static final String Neutron_ID_sourceJson =
            "{ \"id\": \"4e8e5957-649f-477b-9e5b-f1f75b21c03c\" }";

    @Test
    public void test_Neutron_ID_JAXB() {
        Neutron_ID neutronObject = new Neutron_ID();
        try {
            Neutron_ID testObject = (Neutron_ID) JaxbTestHelper.jaxbUnmarshall(neutronObject, Neutron_ID_sourceJson);

            Assert.assertEquals("Neutron_ID JAXB Test 1: Testing id failed", "4e8e5957-649f-477b-9e5b-f1f75b21c03c",
                    testObject.getID());

        } catch (Exception e) {
            Assert.fail("Test failed");
        }
    }

}
