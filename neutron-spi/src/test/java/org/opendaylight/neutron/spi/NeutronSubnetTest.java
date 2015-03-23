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
        Assert.assertEquals("isValidCIDR Test 1: Testing valid CIDR (10.18.0.0/24) failed",
              true, objectUT.isValidCIDR());

        objectUT.setCidr("10.18.0.0/16");
        Assert.assertEquals("isValidCIDR Test 2: Testing valid CIDR (10.18.0.0/16) failed",
              true, objectUT.isValidCIDR());

        objectUT.setCidr("10.18.0.0/8");
        Assert.assertEquals("isValidCIDR Negative Test 1: Testing invalid CIDR (10.18.0.0/8) failed",
              false, objectUT.isValidCIDR());
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
        Assert.assertEquals("gatewayIP_Pool_overlap Test 1: test with address below allocation pool failed",
              false, objectUT.gatewayIP_Pool_overlap());

        objectUT.setGatewayIP("10.18.0.4");
        Assert.assertEquals("gatewayIP_Pool_overlap Test 2: test with address in allocation pool failed",
              true, objectUT.gatewayIP_Pool_overlap());

        objectUT.setGatewayIP("10.18.0.7");
        Assert.assertEquals("gatewayIP_Pool_overlap Test 3: test with address above allocation pool failed",
              false, objectUT.gatewayIP_Pool_overlap());
    }

    @Test
    public void isValidIPTest() {
        NeutronSubnet objectUT = new NeutronSubnet();
        objectUT.setIpVersion(4);
        objectUT.setCidr("10.18.0.0/24");

        Assert.assertEquals("isValidIP Negative Test 1: test of IP address outside of CIDR block failed",
              false, objectUT.isValidIP("10.18.1.1"));

        Assert.assertEquals("isValidIP Test 1: test of IP address within CIDR block failed",
              true, objectUT.isValidIP("10.18.0.1"));
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
        Assert.assertEquals("isIPInUse Test 1: test of unallocated gateway IP address failed",
              false, objectUT.isIPInUse("10.18.0.1"));

        objectUT.setGatewayIPAllocated();
        Assert.assertEquals("isIPInUse Test 2: test of allocated gateway IP address failed",
              true, objectUT.isIPInUse("10.18.0.1"));

        Assert.assertEquals("isIPInUse Test 3: test of address in allocation pool failed",
              false, objectUT.isIPInUse("10.18.0.4"));

        Assert.assertEquals("isIPInUse Test 4: test of address outside of allocation pool failed",
              true, objectUT.isIPInUse("10.18.0.10"));

        Assert.assertEquals("isIPInUse Test 5: test of address outside of CIDR block failed",
              true, objectUT.isIPInUse("10.18.1.10"));
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
        objectUT.setAllocationPools(aPools);

        Assert.assertEquals("getLowAddr Test 1: test of returned address",
              "10.18.0.2", objectUT.getLowAddr()); 
    }

    @Test
    public void allocate_releaseIPTests() {
        NeutronSubnet objectUT = new NeutronSubnet();
        objectUT.setIpVersion(4);
        objectUT.setCidr("10.18.0.0/24");

        NeutronSubnet_IPAllocationPool allocationPool = new NeutronSubnet_IPAllocationPool();
        allocationPool.setPoolStart("10.18.0.2");
        allocationPool.setPoolEnd("10.18.0.6");
        List<NeutronSubnet_IPAllocationPool> aPools = new ArrayList<NeutronSubnet_IPAllocationPool>();
        aPools.add(allocationPool);
        objectUT.setAllocationPools(aPools);

        String ipAddress = objectUT.getLowAddr();
        objectUT.allocateIP(ipAddress);
        aPools = objectUT.getAllocationPools(); 
        Assert.assertEquals("allocateIP Test 1: test of resulting allocation pools size failed",
              1, aPools.size());

        Assert.assertEquals("allocateIP Test 2: test of resulting allocation pools start address failed",
              "10.18.0.3", aPools.get(0).getPoolStart());

        Assert.assertEquals("allocateIP Test 3: test of resulting allocation pools end address failed",
              "10.18.0.6", aPools.get(0).getPoolEnd());

        objectUT.releaseIP(ipAddress);
        aPools = objectUT.getAllocationPools(); 
        Assert.assertEquals("releaseIP Test 1: test of resulting allocation pools size failed",
              1, aPools.size());

        Assert.assertEquals("releaseIP Test 2: test of resulting allocation pool 1 start address failed",
              "10.18.0.2", aPools.get(0).getPoolStart());

        Assert.assertEquals("releaseIP Test 3: test of resulting allocation pool 1 end address failed",
              "10.18.0.6", aPools.get(0).getPoolEnd());

        objectUT.releaseIP("10.18.0.20");
        aPools = objectUT.getAllocationPools(); 
        Assert.assertEquals("releaseIP Test 4: test of resulting allocation pools size failed",
              2, aPools.size());

        Assert.assertEquals("releaseIP Test 5: test of resulting allocation pool 1 start address failed",
              "10.18.0.20", aPools.get(1).getPoolStart());

        Assert.assertEquals("releaseIP Test 6: test of resulting allocation pool 1 end address failed",
              "10.18.0.20", aPools.get(1).getPoolEnd());

        objectUT.allocateIP("10.18.0.20");
        aPools = objectUT.getAllocationPools(); 
        Assert.assertEquals("allocateIP Test 4: test of resulting allocation pools size failed",
              1, aPools.size());
    }
}
