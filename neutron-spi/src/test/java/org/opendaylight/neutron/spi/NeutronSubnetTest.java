/*
 * Copyright (c) 2015 IBM Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.spi;

import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

public class NeutronSubnetTest {

    @Test
    public void isValidCIDRTest() {
        NeutronSubnet objectUT = new NeutronSubnet();
        objectUT.setIpVersion(4);
        objectUT.setCidr("10.18.0.0/24");
        Assert.assertTrue("isValidCIDR Test 1: Testing valid CIDR (10.18.0.0/24) failed", objectUT.isValidCIDR());

        objectUT.setCidr("10.18.0.0/16");
        Assert.assertTrue("isValidCIDR Test 2: Testing valid CIDR (10.18.0.0/16) failed", objectUT.isValidCIDR());

        objectUT.setCidr("10.18.0.0/8");
        Assert.assertFalse("isValidCIDR Negative Test 1: Testing invalid CIDR (10.18.0.0/8) failed",
                objectUT.isValidCIDR());

        objectUT.setIpVersion(6);
        objectUT.setCidr("2015:0:0:0:0:0:0:0/24");
        Assert.assertTrue("isValidCIDR Test 1: Testing valid V6 CIDR (2015:0:0:0:0:0:0:0/24) failed",
                objectUT.isValidCIDR());
        objectUT.setCidr("2015:0:0:0:0:0:0:1/24");
        Assert.assertFalse("isValidCIDR Negative Test 1: Testing invalid CIDR (2015:0:0:0:0:0:0:1) failed",
                objectUT.isValidCIDR());
    }

    @Test
    public void gatewayIp_PoolOverlapTest() {
        NeutronSubnet objectUT = new NeutronSubnet();
        objectUT.setIpVersion(4);
        objectUT.setCidr("10.18.0.0/16");

        NeutronSubnetIpAllocationPool allocationPool = new NeutronSubnetIpAllocationPool();
        allocationPool.setPoolStart("10.18.0.2");
        allocationPool.setPoolEnd("10.18.0.6");
        List<NeutronSubnetIpAllocationPool> pools = new ArrayList<NeutronSubnetIpAllocationPool>();
        pools.add(allocationPool);
        objectUT.setAllocationPools(pools);

        objectUT.setGatewayIp("10.18.0.1");
        Assert.assertFalse("gatewayIp_Pool_overlap Test 1: test with address below allocation pool failed",
                objectUT.gatewayIp_Pool_overlap());

        objectUT.setGatewayIp("10.18.0.4");
        Assert.assertTrue("gatewayIp_Pool_overlap Test 2: test with address in allocation pool failed",
                objectUT.gatewayIp_Pool_overlap());

        objectUT.setGatewayIp("10.18.0.7");
        Assert.assertFalse("gatewayIp_Pool_overlap Test 3: test with address above allocation pool failed",
                objectUT.gatewayIp_Pool_overlap());

        objectUT.setIpVersion(6);
        objectUT.setCidr("2015::0/64");

        allocationPool = new NeutronSubnetIpAllocationPool();
        allocationPool.setPoolStart("2015::2");
        allocationPool.setPoolEnd("2015::6");
        pools = new ArrayList<NeutronSubnetIpAllocationPool>();
        pools.add(allocationPool);
        objectUT.setAllocationPools(pools);

        objectUT.setGatewayIp("2015::1");
        Assert.assertFalse("gatewayIp_Pool_overlap v6 Test 1: test with address below allocation pool failed",
                objectUT.gatewayIp_Pool_overlap());

        objectUT.setGatewayIp("2015::4");
        Assert.assertTrue("gatewayIp_Pool_overlap v6 Test 2: test with address in allocation pool failed",
                objectUT.gatewayIp_Pool_overlap());

        objectUT.setGatewayIp("2015::7");
        Assert.assertFalse("gatewayIp_Pool_overlap v6 Test 3: test with address above allocation pool failed",
                objectUT.gatewayIp_Pool_overlap());
    }

    @Test
    public void isValidIpTest() {
        NeutronSubnet objectUT = new NeutronSubnet();
        objectUT.setIpVersion(4);
        objectUT.setCidr("10.18.0.0/24");

        Assert.assertFalse("isValidIp Negative Test 1: test of IP address outside of CIDR block failed",
                objectUT.isValidIp("10.18.1.1"));

        Assert.assertTrue("isValidIp Test 1: test of IP address within CIDR block failed",
                objectUT.isValidIp("10.18.0.1"));

        objectUT.setIpVersion(6);
        objectUT.setCidr("2015::0/64");

        Assert.assertFalse("isValidIp v6 Negative Test 1: test of IP address outside of CIDR block failed",
                objectUT.isValidIp("2015:0:0:1:0:0:0:1"));

        Assert.assertTrue("isValidIp v6 Test 1: test of IP address within CIDR block failed",
                objectUT.isValidIp("2015:0:0:0:1:0:0:1"));
    }

    @Test
    public void getLowAddrTest() {
        NeutronSubnet objectUT = new NeutronSubnet();
        objectUT.setIpVersion(4);
        objectUT.setCidr("10.18.0.0/24");

        NeutronSubnetIpAllocationPool allocationPool = new NeutronSubnetIpAllocationPool();
        allocationPool.setPoolStart("10.18.0.2");
        allocationPool.setPoolEnd("10.18.0.6");
        List<NeutronSubnetIpAllocationPool> pools = new ArrayList<NeutronSubnetIpAllocationPool>();
        pools.add(allocationPool);
        allocationPool = new NeutronSubnetIpAllocationPool();
        allocationPool.setPoolStart("10.18.0.10");
        allocationPool.setPoolEnd("10.18.0.15");
        objectUT.setAllocationPools(pools);

        Assert.assertEquals("getLowAddr Test 1: test of returned address", "10.18.0.2", objectUT.getLowAddr());

        objectUT.setIpVersion(6);
        objectUT.setCidr("2015::0/24");

        allocationPool = new NeutronSubnetIpAllocationPool();
        allocationPool.setPoolStart("2015::2");
        allocationPool.setPoolEnd("2015::6");
        pools = new ArrayList<NeutronSubnetIpAllocationPool>();
        pools.add(allocationPool);
        allocationPool = new NeutronSubnetIpAllocationPool();
        allocationPool.setPoolStart("2015::10");
        allocationPool.setPoolEnd("2015::15");
        objectUT.setAllocationPools(pools);

        Assert.assertEquals("getLowAddr v6 Test 1: test of returned address", "2015::2", objectUT.getLowAddr());
    }
}
