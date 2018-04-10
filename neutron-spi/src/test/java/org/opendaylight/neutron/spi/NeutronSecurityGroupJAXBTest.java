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

public class NeutronSecurityGroupJAXBTest {
    private static final String NEUTRON_SECURITY_GROUP_SOURCE_JSON = "{"
            + "\"id\": \"2076db17-a522-4506-91de-c6dd8e837028\", " + "\"name\": \"new-webservers\", "
            + "\"description\": \"security group for webservers\", "
            + "\"tenant_id\": \"b4f50856753b4dc6afee5fa6b9b6c550\", "
            + "\"security_group_rules\": [ { \"id\": \"2bc0accf-312e-429a-956e-e4407625eb62\" , "
            + "\"direction\": \"ingress\" , " + "\"protocol\": \"tcp\" , " + "\"port_range_min\": 80 , "
            + "\"port_range_max\": 80 , " + "\"ethertype\": \"IPv4\" ," + "\"remote_ip_prefix\": \"0.0.0.0/0\" , "
            + "\"remote_group_id\": \"85cc3048-abc3-43cc-89b3-377341426ac5\" , "
            + "\"security_group_id\": \"a7734e61-b545-452d-a3cd-0189cbd9747a\" , "
            + "\"tenant_id\": \"e4f50856753b4dc6afee5fa6b9b6c550\" } ]" + "}";

    @Test
    public void test_NeutronSecurityGroup_JAXB() throws JAXBException {
        NeutronSecurityGroup testObject = JaxbTestHelper.jaxbUnmarshall(NeutronSecurityGroup.class,
                NEUTRON_SECURITY_GROUP_SOURCE_JSON);
        Assert.assertEquals("NeutronSecurityGroup JAXB Test 1: Testing id failed",
                "2076db17-a522-4506-91de-c6dd8e837028", testObject.getID());

        Assert.assertEquals("NeutronSecurityGroup JAXB Test 2: Testing direction failed", "new-webservers",
                testObject.getName());

        Assert.assertEquals("NeutronSecurityGroup JAXB Test 4: Testing port range min failed",
                "b4f50856753b4dc6afee5fa6b9b6c550", testObject.getTenantID());
    }
}
