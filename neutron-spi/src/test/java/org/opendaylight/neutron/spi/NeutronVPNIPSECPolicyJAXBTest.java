/*
 * Copyright Tata Consultancy Services, 2015.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.spi;

import org.junit.Assert;
import org.junit.Test;

import org.opendaylight.neutron.spi.NeutronVPNIPSECPolicy;

public class NeutronVPNIPSECPolicyJAXBTest {

    private static final String NeutronVPNIPSECPolicy_SingleProvider_sourceJson = "{" +
        "\"id\": \"5291b189-fd84-46e5-84bd-78f40c05d69c\", " +
        "\"tenant_id\": \"ccb81365fe36411a9011e90491fe1330\", " +
        "\"name\": \"ipsecpolicy1\", " +
        "\"description\": \"update description\", " +
        "\"transform_protocol\": \"esp\", " +
        "\"encapsulation_mode\": \"tunnel\", " +
        "\"auth_algorithm\": \"sha1\", " +
        "\"encryption_algorithm\": \"aes-128\", " +
        "\"pfs\": \"group5\", " +
        "\"lifetime\": { " + "\"units\": \"seconds\", " + "\"value\": 3600 " + "} }";

    @Test
    public void test_NeutronVPNIPSECPolicy_JAXB() {
        NeutronVPNIPSECPolicy dummyObject = new NeutronVPNIPSECPolicy();
        try {
            NeutronVPNIPSECPolicy testObject = (NeutronVPNIPSECPolicy) JaxbTestHelper.jaxbUnmarshall(dummyObject, NeutronVPNIPSECPolicy_SingleProvider_sourceJson);
            Assert.assertEquals("NeutronVPNIPSECPolicy JAXB Test 1: Testing id failed",
                  "5291b189-fd84-46e5-84bd-78f40c05d69c", testObject.getID());

            Assert.assertEquals("NeutronVPNIPSECPolicy JAXB Test 2: Testing tenant id failed",
                  "ccb81365fe36411a9011e90491fe1330", testObject.getTenantID());

            Assert.assertEquals("NeutronVPNIPSECPolicy JAXB Test 3: Testing name failed",
                  "ipsecpolicy1", testObject.getName());

            Assert.assertEquals("NeutronVPNIPSECPolicy JAXB Test 5: Testing transform protocol failed",
                  "esp", testObject.getTransformProtocol());

            Assert.assertEquals("NeutronVPNIPSECPolicy JAXB Test 6: Testing encapsulation mode failed",
                  "tunnel", testObject.getEncapsulationMode());

            Assert.assertEquals("NeutronVPNIPSECPolicy JAXB Test 7: Testing authorization algorithm failed",
                  "sha1", testObject.getAuthAlgorithm());

            Assert.assertEquals("NeutronVPNIPSECPolicy JAXB Test 8: Testing encryption algorithm failed",
                  "aes-128", testObject.getEncryptionAlgorithm());

            Assert.assertEquals("NeutronVPNIPSECPolicy JAXB Test 9: Testing PerfectForwardSecrecy failed",
                  "group5", testObject.getPerfectForwardSecrecy());

            Assert.assertEquals("NeutronVPNIPSECPolicy JAXB Test 10: Testing Lifetime unit value failed",
                  "seconds", testObject.getLifetime().getUnits());

            Assert.assertEquals("NeutronVPNIPSECPolicy JAXB Test 11: Testing Lifetime value failed",
                  new Integer(3600), testObject.getLifetime().getValue());
        } catch (Exception e) {
            Assert.fail("Tests failed");
        }
    }
}
