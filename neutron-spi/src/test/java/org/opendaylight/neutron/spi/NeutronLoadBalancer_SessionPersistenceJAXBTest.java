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

public class NeutronLoadBalancer_SessionPersistenceJAXBTest {

    private static final String NeutronLoadBalancer_SessionPersistence_sourceJson = "{ "
            + "\"cookie_name\": \"NeutronLoadBalancer_SessionPersistence_Cookie\", " + "\"type\": \"HTTP_COOKIE\" }";

    @Test
    public void test_NeutronLoadBalancer_SessionPersistence_JAXB() throws JAXBException {
        NeutronLoadBalancer_SessionPersistence dummyObject = new NeutronLoadBalancer_SessionPersistence();

        NeutronLoadBalancer_SessionPersistence testObject = (NeutronLoadBalancer_SessionPersistence) JaxbTestHelper
                .jaxbUnmarshall(dummyObject, NeutronLoadBalancer_SessionPersistence_sourceJson);
        Assert.assertEquals("NeutronLoadBalancer JAXB Test 1: Testing id failed",
                "NeutronLoadBalancer_SessionPersistence_Cookie", testObject.getCookieName());

        Assert.assertEquals("NeutronLoadBalancer JAXB Test 2: Testing LoadBalancer Name failed", "HTTP_COOKIE",
                testObject.getType());
    }
}
