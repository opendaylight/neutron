/*
 * Copyright (c) 2016 Brocade Communications Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.neutron.spi;

import java.util.Map;
import javax.xml.bind.JAXBException;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Anil Vishnoi (avishnoi@Brocade.com).
 */
public class NeutronSFCPortPairJAXBTest {

    private static final String NEUTRON_SFC_PORT_PAIR_SOURCE_JSON = "{ " + "\"name\": \"portpair1\", "
            + "\"ingress\": \"5e8e5957-649f-477b-9e5b-f1f75b21c03c\", "
            + "\"egress\": \"6e8e5957-649f-477b-9e5b-f1f75b21c03c\", " + "\"service_function_parameters\": [ " + "{ "
            + "\"correlation\": \"value\" " + "} " + "], " + "\"tenant_id\": \"4969c491a3c74ee4af974e6d800c62de\", "
            + "\"id\": \"4e8e5957-649f-477b-9e5b-f1f75b21c03c\" }";

    @Test
    public void test_NeutronSFCPortPair_JAXB() throws JAXBException {
        NeutronSFCPortPair neutronObject = JaxbTestHelper.jaxbUnmarshall(NeutronSFCPortPair.class,
                NEUTRON_SFC_PORT_PAIR_SOURCE_JSON);

        Assert.assertEquals("NeutronSFCPortPair JAXB Test 1: Testing id failed", "4e8e5957-649f-477b-9e5b-f1f75b21c03c",
                neutronObject.getID());

        Assert.assertEquals("NeutronSFCPortPair JAXB Test 3: Testing ingress failed",
                "5e8e5957-649f-477b-9e5b-f1f75b21c03c", neutronObject.getIngressPortUUID());

        Assert.assertEquals("NeutronSFCPortPair JAXB Test 4: Testing egress failed",
                "6e8e5957-649f-477b-9e5b-f1f75b21c03c", neutronObject.getEgressPortUUID());

        Map<String, String> param = neutronObject.getServiceFunctionParameters();
        Assert.assertEquals(
                "NeutronSFCPortPair JAXB Test 5: Testing service_function_parameters list length " + "failed", 1,
                param.size());

        Assert.assertEquals("NeutronSFCPortPair JAXB Test 2: Testing tenant_id failed",
                "4969c491a3c74ee4af974e6d800c62de", neutronObject.getTenantID());
    }
}
