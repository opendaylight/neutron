/*
 * Copyright (c) 2018 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.neutron.e2etest;

import java.io.IOException;

public final class NeutronAllTests {

    private NeutronAllTests() { }

    public static void testNeutron(String baseURL) throws IOException, InterruptedException {
        NeutronNetworkTests.runTests(baseURL);
        NeutronSubnetTests.runTests(baseURL);
        NeutronPortTests.runTests(baseURL);
        NeutronRouterTests.runTests(baseURL);
        NeutronFloatingIpTests.runTests(baseURL);
        NeutronSecurityGroupTests.runTests(baseURL);
        NeutronSecurityRuleTests.runTests(baseURL);
        NeutronFirewallTests.runTests(baseURL);
        NeutronFirewallPolicyTests.runTests(baseURL);
        NeutronFirewallRuleTests.runTests(baseURL);
        NeutronLoadBalancerTests.runTests(baseURL);
        NeutronLBListenerTests.runTests(baseURL);
        NeutronLBPoolTests.runTests(baseURL);
        NeutronLBPoolMembersTests.runTests(baseURL);
        NeutronLBHealthMonitorTests.runTests(baseURL);
        NeutronMeteringLabelTests.runTests(baseURL);
        NeutronMeteringRuleTests.runTests(baseURL);
        NeutronVpnServicesTests.runTests(baseURL);
        NeutronIpSecPoliciesTests.runTests(baseURL);
        NeutronIpSecSiteConnectionTests.runTests(baseURL);
        NeutronIKEPoliciesTests.runTests(baseURL);
        NeutronBgpvpnTests.runTests(baseURL);
        NeutronL2GatewayTests.runTests(baseURL);
        NeutronL2GatewayConnectionTests.runTests(baseURL);
        NeutronQosPolicyTests.runTests(baseURL);
        NeutronSFCPortPairTests.runTests(baseURL);
        NeutronSFCPortPairGroupTests.runTests(baseURL);
        NeutronSFCPortChainTests.runTests(baseURL);
        NeutronSFCFlowClassifierTests.runTests(baseURL);
        NeutronTrunkTests.runTests(baseURL);
        NeutronRevisionNumberTests.runTests(baseURL);
        NeutronProjectIdTests.runTests(baseURL);
        // tests related to bugs
        NeutronBug3812Tests.runTests(baseURL);
        TempestPortsIpV6TestJSON.runTests(baseURL);
        NeutronBug4027Tests.runTests(baseURL);
    }

}
