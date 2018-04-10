/*
 * Copyright (c) 2016 Brocade Communications Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.neutron.spi;

import javax.xml.bind.JAXBException;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Anil Vishnoi (avishnoi@Brocade.com).
 */
public class NeutronSFCPortPairGroupJAXBTest {

    private static final String NEUTRON_SFC_PORT_PAIR_GROUP_SOURCE_JSON = "{ " + "\"name\": \"portpair1\", "
            + "\"port_pairs\": [ " + "\"d11e9190-73d4-11e5-b392-2c27d72acb4c\"" + "], "
            + "\"tenant_id\": \"4969c491a3c74ee4af974e6d800c62de\", "
            + "\"id\": \"4e8e5957-649f-477b-9e5b-f1f75b21c03c\" }";

    @Test
    public void test_NeutronSFCPortPairGroup_JAXB() throws JAXBException {
        NeutronSFCPortPairGroup neutronObject = JaxbTestHelper.jaxbUnmarshall(NeutronSFCPortPairGroup.class,
                NEUTRON_SFC_PORT_PAIR_GROUP_SOURCE_JSON);

        Assert.assertEquals("NeutronSFCPortPairGroup JAXB Test 1: Testing id failed",
                "4e8e5957-649f-477b-9e5b-f1f75b21c03c", neutronObject.getID());

        Assert.assertEquals("NeutronSFCPortPairGroup JAXB Test 3: Testing port_pairs failed",
                "d11e9190-73d4-11e5-b392-2c27d72acb4c", neutronObject.getPortPairs().get(0));

        Assert.assertEquals("NeutronSFCPortPairGroup JAXB Test 2: Testing tenant_id failed",
                "4969c491a3c74ee4af974e6d800c62de", neutronObject.getTenantID());
    }
}
