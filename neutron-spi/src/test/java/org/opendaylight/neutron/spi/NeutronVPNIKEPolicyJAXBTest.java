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

public class NeutronVPNIKEPolicyJAXBTest {
    private static final String NeutronVPNIKEPolicy_sourceJson = "{"
            + "\"id\": \"5522aff7-1b3c-48dd-9c3c-b50f016b73db\", "
            + "\"tenant_id\": \"ccb81365fe36411a9011e90491fe1330\", " + "\"name\": \"ikepolicy1\", "
            + "\"description\": \"updated description\", " + "\"auth_algorithm\": \"sha1\", "
            + "\"encryption_algorithm\": \"aes-256\", " + "\"phase1_negotiation_mode\": \"main\", "
            + "\"ike_version\": \"v1\", " + "\"pfs\": \"group5\", " + "\"lifetime\": { " + "\"units\": \"seconds\", "
            + "\"value\": 3600 " + "} }";

    @Test
    public void test_NeutronVPNIKEPolicy_JAXB() throws JAXBException {
        NeutronVPNIKEPolicy dummyObject = new NeutronVPNIKEPolicy();

        NeutronVPNIKEPolicy testObject = (NeutronVPNIKEPolicy) JaxbTestHelper.jaxbUnmarshall(dummyObject,
                NeutronVPNIKEPolicy_sourceJson);
        Assert.assertEquals("NeutronVPNIKEPolicy JAXB Test 1: Testing id failed",
                "5522aff7-1b3c-48dd-9c3c-b50f016b73db", testObject.getID());

        Assert.assertEquals("NeutronVPNIKEPolicy JAXB Test 2: Testing tenant id failed",
                "ccb81365fe36411a9011e90491fe1330", testObject.getTenantID());

        Assert.assertEquals("NeutronVPNIKEPolicy JAXB Test 3: Testing name failed", "ikepolicy1",
                testObject.getName());

        Assert.assertEquals("NeutronVPNIKEPolicy JAXB Test 5: Testing auth algorithm failed", "sha1",
                testObject.getAuthAlgorithm());

        Assert.assertEquals("NeutronVPNIKEPolicy JAXB Test 6: Testing Encryption algorithm failed", "aes-256",
                testObject.getEncryptionAlgorithm());

        Assert.assertEquals("NeutronVPNIKEPolicy JAXB Test 7: Testing phase1 negotiation mode failed", "main",
                testObject.getPhase1NegotiationMode());

        Assert.assertEquals("NeutronVPNIKEPolicy JAXB Test 8: Testing ike version failed", "v1",
                testObject.getIkeVersion());

        Assert.assertEquals("NeutronVPNIKEPolicy JAXB Test 9: Testing pfs failed", "group5",
                testObject.getPerfectForwardSecrecy());

        Assert.assertEquals("NeutronVPNIKEPolicy JAXB Test 10.1: Testing lifetime units value failed", "seconds",
                testObject.getLifetime().getUnits());

        Assert.assertEquals("NeutronVPNIKEPolicy JAXB Test 10.2: Testing lifetime values failed", new Integer(3600),
                testObject.getLifetime().getValue());
    }
}
