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
public class NeutronSFCPortChainJAXBTest {

    private static final String NEUTRON_SFC_PORT_CHAIN_SOURCE_JSON = "{ " + "\"name\": \"portchain1\", "
            + "\"port_pair_groups\": [ " + "\"4512d643-24fc-4fae-af4b-321c5e2eb3d1\", "
            + "\"4a634d49-76dc-4fae-af4b-321c5e23d651\" " + "], " + "\"flow_classifiers\": [ "
            + "\"4a334cd4-fe9c-4fae-af4b-321c5e2eb051\", " + "\"105a4b0a-73d6-11e5-b392-2c27d72acb4c\" " + "], "
            + "\"chain_parameters\": [ " + "{ " + "\"correlation\": \"value\" " + "} " + "], "
            + "\"tenant_id\": \"4969c491a3c74ee4af974e6d800c62de\", "
            + "\"id\": \"4e8e5957-649f-477b-9e5b-f1f75b21c03c\" }";

    @Test
    public void test_NeutronSFCPortChain_JAXB() throws JAXBException {
        NeutronSFCPortChain neutronObject = JaxbTestHelper.jaxbUnmarshall(NeutronSFCPortChain.class,
                NEUTRON_SFC_PORT_CHAIN_SOURCE_JSON);

        Assert.assertEquals("NeutronSFCPortChain JAXB Test 1: Testing id failed",
                "4e8e5957-649f-477b-9e5b-f1f75b21c03c", neutronObject.getID());

        Assert.assertEquals("NeutronSFCPortChain JAXB Test 3: Testing flow_classifiers failed", 2,
                neutronObject.getFlowClassifiersUUID().size());

        Assert.assertEquals("NeutronSFCPortChain JAXB Test 4: Testing port_pair_groups failed", 2,
                neutronObject.getPortPairGroupsUUID().size());

        Map<String, String> param = neutronObject.getChainParameters();
        Assert.assertEquals("NeutronSFCPortChain JAXB Test 5: Testing chain_parameters list length " + "failed", 1,
                param.size());

        Assert.assertEquals("NeutronSFCPortChain JAXB Test 2: Testing tenant_id failed",
                "4969c491a3c74ee4af974e6d800c62de", neutronObject.getTenantID());
    }
}
