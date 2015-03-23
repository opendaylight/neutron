/*
 * Copyright IBM Corporation and others, 2013.  All rights reserved.
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
        boolean expectedValue = true;
        boolean actualValue = objectUT.isValidCIDR();
        Assert.assertEquals("isValidCIDR Test 1 failed", expectedValue, actualValue);

        objectUT.setCidr("10.18.0.0/16");
        actualValue = objectUT.isValidCIDR();
        Assert.assertEquals("isValidCIDR Test 2 failed", expectedValue, actualValue);

        expectedValue = false;
        objectUT.setCidr("10.18.0.0/8");
        actualValue = objectUT.isValidCIDR();
        Assert.assertEquals("isValidCIDR Test 3 failed", expectedValue, actualValue);
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
        boolean expectedValue = false;
        boolean actualValue = objectUT.gatewayIP_Pool_overlap();
        Assert.assertEquals("gatewayIP_Pool_overlap Test 1 failed", expectedValue, actualValue);

        objectUT.setGatewayIP("10.18.0.4");
        expectedValue = true;
        actualValue = objectUT.gatewayIP_Pool_overlap();
        Assert.assertEquals("gatewayIP_Pool_overlap Test 2 failed", expectedValue, actualValue);

        objectUT.setGatewayIP("10.18.0.7");
        expectedValue = false;
        actualValue = objectUT.gatewayIP_Pool_overlap();
        Assert.assertEquals("gatewayIP_Pool_overlap Test 3 failed", expectedValue, actualValue);
    }

    @Test
    public void isValidIP_Test() {
        NeutronSubnet objectUT = new NeutronSubnet();
        objectUT.setIpVersion(4);
        objectUT.setCidr("10.18.0.0/24");

        boolean expectedValue = false;
        boolean actualValue = objectUT.isValidIP("10.18.1.1");
        Assert.assertEquals("isValidIP Test 1 failed", expectedValue, actualValue);

        expectedValue = true;
        actualValue = objectUT.isValidIP("10.18.0.1");
        Assert.assertEquals("isValidIP Test 2 failed", expectedValue, actualValue);
    }

    @Test
    public void isIPInUse() {
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
        boolean expectedValue = false;
        boolean actualValue = objectUT.isIPInUse("10.18.0.1");
        Assert.assertEquals("isIPInUse Test 1 failed", expectedValue, actualValue);

        objectUT.setGatewayIPAllocated();
        expectedValue = true;
        actualValue = objectUT.isIPInUse("10.18.0.1");
        Assert.assertEquals("isIPInUse Test 2 failed", expectedValue, actualValue);

        expectedValue = false;
        actualValue = objectUT.isIPInUse("10.18.0.4");
        Assert.assertEquals("isIPInUse Test 3 failed", expectedValue, actualValue);

        expectedValue = true;
        actualValue = objectUT.isIPInUse("10.18.0.10");
        Assert.assertEquals("isIPInUse Test 4 failed", expectedValue, actualValue);

        expectedValue = true;
        actualValue = objectUT.isIPInUse("10.18.1.10");
        Assert.assertEquals("isIPInUse Test 5 failed", expectedValue, actualValue);
    }
}
