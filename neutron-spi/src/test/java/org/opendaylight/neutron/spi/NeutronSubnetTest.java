/*
 * Copyright IBM Corporation and others, 2015.  All rights reserved.
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

import org.opendaylight.neutron.spi.NeutronSubnet;
import org.opendaylight.neutron.spi.NeutronSubnet_IPAllocationPool;

public class NeutronSubnetTest {

    @Test
    public void isValidCIDRTest() {
        NeutronSubnet objectUT = new NeutronSubnet();
        objectUT.setIpVersion(4);
        objectUT.setCidr("10.18.0.0/24");
        Assert.assertTrue("isValidCIDR Test 1: Testing valid CIDR (10.18.0.0/24) failed",
              objectUT.isValidCIDR());

        objectUT.setCidr("10.18.0.0/16");
        Assert.assertTrue("isValidCIDR Test 2: Testing valid CIDR (10.18.0.0/16) failed",
              objectUT.isValidCIDR());

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
    public void gatewayIP_PoolOverlapTest() {
        NeutronSubnet objectUT = new NeutronSubnet();
        objectUT.setIpVersion(4);
        objectUT.setCidr("10.18.0.0/16");

        NeutronSubnet_IPAllocationPool allocationPool = new NeutronSubnet_IPAllocationPool();
        allocationPool.setPoolStart("10.18.0.2");
        allocationPool.setPoolEnd("10.18.0.6");
        List<NeutronSubnet_IPAllocationPool> aPools = new ArrayList<NeutronSubnet_IPAllocationPool>();
        aPools.add(allocationPool);
        objectUT.setAllocationPools(aPools);

        objectUT.setGatewayIP("10.18.0.1");
        Assert.assertFalse("gatewayIP_Pool_overlap Test 1: test with address below allocation pool failed",
              objectUT.gatewayIP_Pool_overlap());

        objectUT.setGatewayIP("10.18.0.4");
        Assert.assertTrue("gatewayIP_Pool_overlap Test 2: test with address in allocation pool failed",
              objectUT.gatewayIP_Pool_overlap());

        objectUT.setGatewayIP("10.18.0.7");
        Assert.assertFalse("gatewayIP_Pool_overlap Test 3: test with address above allocation pool failed",
              objectUT.gatewayIP_Pool_overlap());

        objectUT.setIpVersion(6);
        objectUT.setCidr("2015::0/64");

        allocationPool = new NeutronSubnet_IPAllocationPool();
        allocationPool.setPoolStart("2015::2");
        allocationPool.setPoolEnd("2015::6");
        aPools = new ArrayList<NeutronSubnet_IPAllocationPool>();
        aPools.add(allocationPool);
        objectUT.setAllocationPools(aPools);

        objectUT.setGatewayIP("2015::1");
        Assert.assertFalse("gatewayIP_Pool_overlap v6 Test 1: test with address below allocation pool failed",
              objectUT.gatewayIP_Pool_overlap());

        objectUT.setGatewayIP("2015::4");
        Assert.assertTrue("gatewayIP_Pool_overlap v6 Test 2: test with address in allocation pool failed",
              objectUT.gatewayIP_Pool_overlap());

        objectUT.setGatewayIP("2015::7");
        Assert.assertFalse("gatewayIP_Pool_overlap v6 Test 3: test with address above allocation pool failed",
              objectUT.gatewayIP_Pool_overlap());
    }

    @Test
    public void isValidIPTest() {
        NeutronSubnet objectUT = new NeutronSubnet();
        objectUT.setIpVersion(4);
        objectUT.setCidr("10.18.0.0/24");

        Assert.assertFalse("isValidIP Negative Test 1: test of IP address outside of CIDR block failed",
              objectUT.isValidIP("10.18.1.1"));

        Assert.assertTrue("isValidIP Test 1: test of IP address within CIDR block failed",
              objectUT.isValidIP("10.18.0.1"));

        objectUT.setIpVersion(6);
        objectUT.setCidr("2015::0/64");

        Assert.assertFalse("isValidIP v6 Negative Test 1: test of IP address outside of CIDR block failed",
              objectUT.isValidIP("2015:0:0:1:0:0:0:1"));

        Assert.assertTrue("isValidIP v6 Test 1: test of IP address within CIDR block failed",
              objectUT.isValidIP("2015:0:0:0:1:0:0:1"));
    }

    @Test
    public void isIPInUseTest() {
        NeutronSubnet objectUT = new NeutronSubnet();
        objectUT.setIpVersion(4);
        objectUT.setCidr("10.18.0.0/24");

        NeutronSubnet_IPAllocationPool allocationPool = new NeutronSubnet_IPAllocationPool();
        allocationPool.setPoolStart("10.18.0.2");
        allocationPool.setPoolEnd("10.18.0.6");
        List<NeutronSubnet_IPAllocationPool> aPools = new ArrayList<NeutronSubnet_IPAllocationPool>();
        aPools.add(allocationPool);
        objectUT.setAllocationPools(aPools);

        objectUT.setGatewayIP("10.18.0.1");
        objectUT.resetGatewayIPAllocated();
        Assert.assertFalse("isIPInUse Test 1: test of unallocated gateway IP address failed",
              objectUT.isIPInUse("10.18.0.1"));

        objectUT.setGatewayIPAllocated();
        Assert.assertTrue("isIPInUse Test 2: test of allocated gateway IP address failed",
              objectUT.isIPInUse("10.18.0.1"));

        Assert.assertFalse("isIPInUse Test 3: test of address in allocation pool failed",
              objectUT.isIPInUse("10.18.0.4"));

        Assert.assertTrue("isIPInUse Test 4: test of address outside of allocation pool failed",
              objectUT.isIPInUse("10.18.0.10"));

        Assert.assertTrue("isIPInUse Test 5: test of address outside of CIDR block failed",
              objectUT.isIPInUse("10.18.1.10"));

        objectUT.setIpVersion(6);
        objectUT.setCidr("2015::0/64");

        allocationPool = new NeutronSubnet_IPAllocationPool();
        allocationPool.setPoolStart("2015::2");
        allocationPool.setPoolEnd("2015::6");
        aPools = new ArrayList<NeutronSubnet_IPAllocationPool>();
        aPools.add(allocationPool);
        objectUT.setAllocationPools(aPools);

        objectUT.setGatewayIP("2015::1");
        objectUT.resetGatewayIPAllocated();
        Assert.assertFalse("isIPInUse v6 Test 1: test of unallocated gateway IP address failed",
              objectUT.isIPInUse("2015::1"));

        objectUT.setGatewayIPAllocated();
        Assert.assertTrue("isIPInUse v6 Test 2: test of allocated gateway IP address failed",
              objectUT.isIPInUse("2015::1"));

        Assert.assertFalse("isIPInUse v6 Test 3: test of address in allocation pool failed",
              objectUT.isIPInUse("2015::4"));

        Assert.assertTrue("isIPInUse v6 Test 4: test of address outside of allocation pool failed",
              objectUT.isIPInUse("2015::10"));

        Assert.assertTrue("isIPInUse v6 Test 5: test of address outside of CIDR block failed",
              objectUT.isIPInUse("2016::10"));
    }

    @Test
    public void getLowAddrTest() {
        NeutronSubnet objectUT = new NeutronSubnet();
        objectUT.setIpVersion(4);
        objectUT.setCidr("10.18.0.0/24");

        NeutronSubnet_IPAllocationPool allocationPool = new NeutronSubnet_IPAllocationPool();
        allocationPool.setPoolStart("10.18.0.2");
        allocationPool.setPoolEnd("10.18.0.6");
        List<NeutronSubnet_IPAllocationPool> aPools = new ArrayList<NeutronSubnet_IPAllocationPool>();
        aPools.add(allocationPool);
        allocationPool = new NeutronSubnet_IPAllocationPool();
        allocationPool.setPoolStart("10.18.0.10");
        allocationPool.setPoolEnd("10.18.0.15");
        objectUT.setAllocationPools(aPools);

        Assert.assertEquals("getLowAddr Test 1: test of returned address",
              "10.18.0.2", objectUT.getLowAddr());

        objectUT.setIpVersion(6);
        objectUT.setCidr("2015::0/24");

        allocationPool = new NeutronSubnet_IPAllocationPool();
        allocationPool.setPoolStart("2015::2");
        allocationPool.setPoolEnd("2015::6");
        aPools = new ArrayList<NeutronSubnet_IPAllocationPool>();
        aPools.add(allocationPool);
        allocationPool = new NeutronSubnet_IPAllocationPool();
        allocationPool.setPoolStart("2015::10");
        allocationPool.setPoolEnd("2015::15");
        objectUT.setAllocationPools(aPools);

        Assert.assertEquals("getLowAddr v6 Test 1: test of returned address",
              "2015::2", objectUT.getLowAddr());
    }
}
