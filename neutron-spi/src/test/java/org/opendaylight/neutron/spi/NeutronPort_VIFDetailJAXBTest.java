/*
 * Copyright (C) 2015 IBM, Inc.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.spi;

import org.junit.Assert;
import org.junit.Test;
import org.opendaylight.neutron.spi.JaxbTestHelper;
import org.opendaylight.neutron.spi.NeutronPort_VIFDetail;

public class NeutronPort_VIFDetailJAXBTest {

    private static final String NeutronPort_VIFDetail_sourceJson = "{" + "\"port_filter\": true, "
            + "\"ovs_hybrid_plug\": true }";

    @Test
    public void test_NeutronPort_VIFDetail_JAXB() {
        NeutronPort_VIFDetail portObject = new NeutronPort_VIFDetail();
        try {
            NeutronPort_VIFDetail testObject = (NeutronPort_VIFDetail) JaxbTestHelper.jaxbUnmarshall(portObject,
                    NeutronPort_VIFDetail_sourceJson);

            Assert.assertEquals("NeutronPort_VIFDetail JAXB Test 1: Testing port_filter failed", true,
                    testObject.getPortID());

            Assert.assertEquals("NeutronPort_VIFDetail JAXB Test 10: Testing ovs_hybrid_plug failed", true,
                    testObject.getMacAddress());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
