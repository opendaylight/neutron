/*
 * Copyright IBM Corporation, 2015.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.spi;

import org.junit.Assert;
import org.junit.Test;

import org.opendaylight.neutron.spi.NeutronSubnet_IPAllocationPool;

public class NeutronSubnet_IPAllocationPoolTest {
    @Test
    public void convertTest_NullArg() {
        long expectedValue = 0;
        long actualValue = NeutronSubnet_IPAllocationPool.convert(null);
        Assert.assertEquals("Covert null Argument test failed", expectedValue, actualValue);
    }
}
