/*
 * Copyright (c) 2018 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.neutron.e2etest;

import java.io.IOException;
import javax.inject.Inject;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.opendaylight.aaa.web.WebServer;
import org.opendaylight.infrautils.inject.guice.testutils.AnnotationsModule;
import org.opendaylight.infrautils.inject.guice.testutils.GuiceRule;
import org.opendaylight.infrautils.testutils.ClasspathHellDuplicatesCheckRule;

/**
 * Neutron "end to end" (component) test.
 *
 * <p>This is similar to the ITNeutronE2E, but does the same without OSGi and Karaf.
 *
 * @author Michael Vorburger.ch
 */
public class NeutronE2ETest {

//    public static @ClassRule
//    ClasspathHellDuplicatesCheckRule jHades = new ClasspathHellDuplicatesCheckRule();

    public @Rule
    GuiceRule guice = new GuiceRule(NeutronTestWiring.class, AnnotationsModule.class);

    public @Inject
    WebServer webServer;

    private String baseUrl;

    @Before
    public void setup() {
        this.baseUrl = webServer.getBaseURL() + "/controller/nb/v2/neutron";
    }

    @Test
    public void testNeutronNetwork() throws IOException, InterruptedException {
        NeutronNetworkTests.runTests(baseUrl);
    }

    @Test
    public void testNeutronSubnet() {
        NeutronSubnetTests.runTests(baseUrl);
    }

    @Test
    public void testNeutronPort() {
        NeutronPortTests.runTests(baseUrl);
    }

    @Test
    public void testNeutronRouter() {
        NeutronRouterTests.runTests(baseUrl);
    }

    @Test
    public void testNeutronFloatingIp() {
        NeutronFloatingIpTests.runTests(baseUrl);
    }

    @Test
    public void testNeutronSecurityGroup() {
        NeutronSecurityGroupTests.runTests(baseUrl);
    }

    @Test
    public void testNeutronSecurityRules() {
        NeutronSecurityRuleTests.runTests(baseUrl);
    }

    @Test
    public void testNeutronFirewall() {
        NeutronFirewallTests.runTests(baseUrl);
    }

    @Test
    public void testNeutronFirewallPolicy() {
        NeutronFirewallPolicyTests.runTests(baseUrl);
    }

    @Test
    public void testNeutronFirewallRules() {
        NeutronFirewallRuleTests.runTests(baseUrl);
    }

    @Test
    public void testNeutronLoadBalancer() {
        NeutronLoadBalancerTests.runTests(baseUrl);
    }

    @Test
    public void testNeutronLBListener() {
        NeutronLBListenerTests.runTests(baseUrl);
    }

    @Test
    public void testNeutronLBPool() {
        NeutronLBPoolTests.runTests(baseUrl);
    }

    @Test
    public void testNeutronLBPoolMembers() {
        NeutronLBPoolMembersTests.runTests(baseUrl);
    }

    @Test
    public void testNeutronLBHealthMonitor() {
        NeutronLBHealthMonitorTests.runTests(baseUrl);
    }

    @Test
    public void testNeutronMeteringLabel() {
        NeutronMeteringLabelTests.runTests(baseUrl);
    }

    @Test
    public void testNeutronMeteringRule() {
        NeutronMeteringRuleTests.runTests(baseUrl);
    }

    @Test
    public void testNeutronVpnServices() {
        NeutronVpnServicesTests.runTests(baseUrl);
    }

    @Test
    public void testNeutronIpSecPolicies() {
        NeutronIpSecPoliciesTests.runTests(baseUrl);
    }

    @Test
    public void testNeutronIpSecSiteConnection() {
        NeutronIpSecSiteConnectionTests.runTests(baseUrl);
    }

    @Test
    public void testNeutronIKEPolicies() {
        NeutronIKEPoliciesTests.runTests(baseUrl);
    }

    @Test
    public void testNeutronBgpvpn() {
        NeutronBgpvpnTests.runTests(baseUrl);
    }

    @Test
    public void testNeutronL2Gateway() {
        NeutronL2GatewayTests.runTests(baseUrl);
    }

    @Test
    public void testNeutronL2GatewayConnection() {
        NeutronL2GatewayConnectionTests.runTests(baseUrl);
    }

    @Test
    public void testNeutronQosPolicy() {
        NeutronQosPolicyTests.runTests(baseUrl);
    }

    @Test
    public void testNeutronSFCPortPair() {
        NeutronSFCPortPairTests.runTests(baseUrl);
    }

    @Test
    public void testNeutronSFCPortPairGroup() {
        NeutronSFCPortPairGroupTests.runTests(baseUrl);
    }

    @Test
    public void testNeutronSFCPortChain() {
        NeutronSFCPortChainTests.runTests(baseUrl);
    }

    @Test
    public void testNeutronSFCFlowClassifier() {
        NeutronSFCFlowClassifierTests.runTests(baseUrl);
    }

    @Test
    public void testNeutronTrunk() {
        NeutronTrunkTests.runTests(baseUrl);
    }

    @Test
    public void testNeutronRevisionNumber() {
        NeutronRevisionNumberTests.runTests(baseUrl);
    }

    @Test
    public void testNeutronProjectId() {
        NeutronProjectIdTests.runTests(baseUrl);
    }

    // tests related to bugs

    @Test
    public void testNeutronBug3812() {
        NeutronBug3812Tests.runTests(baseUrl);
    }

    @Test
    public void testNeutronPortsIpV6() {
        TempestPortsIpV6TestJSON.runTests(baseUrl);
    }

    @Test
    public void testNeutronBug4027() {
        NeutronBug4027Tests.runTests(baseUrl);
    }
}
