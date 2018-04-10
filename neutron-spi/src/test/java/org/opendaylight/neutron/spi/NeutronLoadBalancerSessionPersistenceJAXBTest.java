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

public class NeutronLoadBalancerSessionPersistenceJAXBTest {

    private static final String NEUTRON_LOAD_BALANCER_SESSION_PERSISTENCE_SOURCE_JSON = "{ "
            + "\"cookie_name\": \"NeutronLoadBalancerSessionPersistence_Cookie\", " + "\"type\": \"HTTP_COOKIE\" }";

    @Test
    public void test_NeutronLoadBalancerSessionPersistence_JAXB() throws JAXBException {
        NeutronLoadBalancerSessionPersistence testObject = JaxbTestHelper.jaxbUnmarshall(
                NeutronLoadBalancerSessionPersistence.class, NEUTRON_LOAD_BALANCER_SESSION_PERSISTENCE_SOURCE_JSON);
        Assert.assertEquals("NeutronLoadBalancer JAXB Test 1: Testing id failed",
                "NeutronLoadBalancerSessionPersistence_Cookie", testObject.getCookieName());

        Assert.assertEquals("NeutronLoadBalancer JAXB Test 2: Testing LoadBalancer Name failed", "HTTP_COOKIE",
                testObject.getType());
    }
}
