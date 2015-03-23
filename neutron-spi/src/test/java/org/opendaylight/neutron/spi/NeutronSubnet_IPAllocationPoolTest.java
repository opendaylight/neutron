/*
 * Copyright IBM Corporation, 2015.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.spi;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import org.opendaylight.neutron.spi.NeutronSubnet_IPAllocationPool;

public class NeutronSubnet_IPAllocationPoolTest {
    @Test
    public void convertTest() {
        Assert.assertEquals("Convert Test 1: null Argument failed",
              0, NeutronSubnet_IPAllocationPool.convert(null));

        Assert.assertEquals("Convert Test 2: convert of 32.20.10.0 failed",
              538184192, NeutronSubnet_IPAllocationPool.convert("32.20.10.0"));

        Assert.assertEquals("Convert Test 3: convert of 192.168.2.140 failed",
              3232236172L, NeutronSubnet_IPAllocationPool.convert("192.168.2.140"));
    }

    @Test
    public void longtoIPTest() {
        Assert.assertEquals("longtoIP Test 1: convert of 538184192L failed",
              "32.20.10.0", NeutronSubnet_IPAllocationPool.longtoIP(538184192L));

        Assert.assertEquals("longtoIP Test 2: convert of 3232236172L failed",
              "192.168.2.140", NeutronSubnet_IPAllocationPool.longtoIP(3232236172L));
    }

    @Test
    public void containsTest() {
        NeutronSubnet_IPAllocationPool apUT = new NeutronSubnet_IPAllocationPool();
        apUT.setPoolStart("10.18.0.2");
        apUT.setPoolEnd("10.18.0.4");

        Assert.assertFalse("Contains Test 1: address one below pool start failed",
              apUT.contains("10.18.0.1"));

        Assert.assertTrue("Contains Test 2: address == pool start failed",
              apUT.contains("10.18.0.2"));

        Assert.assertTrue("Contains Test 3: adress in pool failed",
              apUT.contains("10.18.0.3"));

        Assert.assertTrue("Contains Test 4: address == pool end failed",
              apUT.contains("10.18.0.4"));

        Assert.assertFalse("Contains Test 5: one above pool end failed",
              apUT.contains("10.18.0.5"));
    }

    @Test
    public void splitPoolTest() {
        NeutronSubnet_IPAllocationPool apUT = new NeutronSubnet_IPAllocationPool();
        apUT.setPoolStart("10.18.0.2");
        apUT.setPoolEnd("10.18.0.6");
        List<NeutronSubnet_IPAllocationPool> result = apUT.splitPool("10.18.0.2");
        Assert.assertEquals("splitPool Test 1.1: address == pool start (result size) failed",
              1, result.size());
        Assert.assertEquals("splitPool Test 1.2: address == pool start (pool start) failed",
              "10.18.0.3", result.get(0).getPoolStart());
        Assert.assertEquals("splitPool Test 1.3: address == pool start (pool end) failed",
              "10.18.0.6", result.get(0).getPoolEnd());

        apUT = new NeutronSubnet_IPAllocationPool();
        apUT.setPoolStart("10.18.0.2");
        apUT.setPoolEnd("10.18.0.6");
        result = apUT.splitPool("10.18.0.3");
        Assert.assertEquals("splitPool Test 2.1: address one above pool start (result size) failed",
              2, result.size());
        Assert.assertEquals("splitPool Test 2.2: address one above pool start (pool 1 start) failed",
              "10.18.0.2", result.get(0).getPoolStart());
        Assert.assertEquals("splitPool Test 2.3: address one above pool start (pool 1 end) failed",
              "10.18.0.2", result.get(0).getPoolEnd());
        Assert.assertEquals("splitPool Test 2.4: address one above pool start (pool 2 start) failed",
              "10.18.0.4", result.get(1).getPoolStart());
        Assert.assertEquals("splitPool Test 2.5: address one above pool start (pool 2 end) failed",
              "10.18.0.6", result.get(1).getPoolEnd());

        apUT = new NeutronSubnet_IPAllocationPool();
        apUT.setPoolStart("10.18.0.2");
        apUT.setPoolEnd("10.18.0.6");
        result = apUT.splitPool("10.18.0.4");
        Assert.assertEquals("splitPool Test 3.1: address one above pool start (result size) failed",
              2, result.size());
        Assert.assertEquals("splitPool Test 3.2: address one above pool start (pool 1 start) failed",
              "10.18.0.2", result.get(0).getPoolStart());
        Assert.assertEquals("splitPool Test 3.3: address one above pool start (pool 1 end) failed",
              "10.18.0.3", result.get(0).getPoolEnd());
        Assert.assertEquals("splitPool Test 3.4: address one above pool start (pool 2 start) failed",
              "10.18.0.5", result.get(1).getPoolStart());
        Assert.assertEquals("splitPool Test 3.5: address one above pool start (pool 2 end) failed",
              "10.18.0.6", result.get(1).getPoolEnd());

        apUT = new NeutronSubnet_IPAllocationPool();
        apUT.setPoolStart("10.18.0.2");
        apUT.setPoolEnd("10.18.0.6");
        result = apUT.splitPool("10.18.0.5");
        Assert.assertEquals("splitPool Test 4.1: address one above pool start (result size) failed",
              2, result.size());
        Assert.assertEquals("splitPool Test 4.2: address one above pool start (pool 1 start) failed",
              "10.18.0.2", result.get(0).getPoolStart());
        Assert.assertEquals("splitPool Test 4.3: address one above pool start (pool 1 end) failed",
              "10.18.0.4", result.get(0).getPoolEnd());
        Assert.assertEquals("splitPool Test 4.4: address one above pool start (pool 2 start) failed",
              "10.18.0.6", result.get(1).getPoolStart());
        Assert.assertEquals("splitPool Test 4.5: address one above pool start (pool 2 end) failed",
              "10.18.0.6", result.get(1).getPoolEnd());

        apUT = new NeutronSubnet_IPAllocationPool();
        apUT.setPoolStart("10.18.0.2");
        apUT.setPoolEnd("10.18.0.6");
        result = apUT.splitPool("10.18.0.6");
        Assert.assertEquals("splitPool Test 5.1: address == pool start (result size) failed",
              1, result.size());
        Assert.assertEquals("splitPool Test 5.2: address == pool start (pool start) failed",
              "10.18.0.2", result.get(0).getPoolStart());
        Assert.assertEquals("splitPool Test 5.3: address == pool start (pool end) failed",
              "10.18.0.5", result.get(0).getPoolEnd());
    }
}
