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

public class NeutronIdJAXBTest {

    private static final String NEUTRON_ID_SOURCE_JSON =
            "{ \"id\": \"4e8e5957-649f-477b-9e5b-f1f75b21c03c\" }";

    @Test
    public void test_NeutronID_JAXB() throws JAXBException {
        NeutronID testObject = JaxbTestHelper.jaxbUnmarshall(NeutronID.class, NEUTRON_ID_SOURCE_JSON);

        Assert.assertEquals("NeutronID JAXB Test 1: Testing id failed", "4e8e5957-649f-477b-9e5b-f1f75b21c03c",
                    testObject.getID());
    }

}
