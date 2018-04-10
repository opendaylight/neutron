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

public class NeutronMeteringLabelJAXBTest {

    private static final String NEUTRON_METERING_LABEL_SOURCE_JSON = "{ "
            + "\"id\": \"4e8e5957-649f-477b-9e5b-f1f75b21c03c\", " + "\"name\": \"net1\", "
            + "\"description\": \"Provides allowed address pairs\", "
            + "\"tenant_id\": \"9bacb3c5d39d41a79512987f338cf177\"}";

    @Test
    public void test_NeutronMeteringLabel_JAXB()  throws JAXBException {
        NeutronMeteringLabel testObject = JaxbTestHelper.jaxbUnmarshall(NeutronMeteringLabel.class,
                NEUTRON_METERING_LABEL_SOURCE_JSON);
        Assert.assertEquals("NeutronMeteringLabel JAXB Test 1: Testing id failed",
                "4e8e5957-649f-477b-9e5b-f1f75b21c03c", testObject.getID());

        Assert.assertEquals("NeutronMeteringLabel JAXB Test 2: Testing name failed", "net1",
                testObject.getName());

        Assert.assertEquals("NeutronMeteringLabel JAXB Test 4: Testing tenant_id failed",
                "9bacb3c5d39d41a79512987f338cf177", testObject.getTenantID());
    }
}
