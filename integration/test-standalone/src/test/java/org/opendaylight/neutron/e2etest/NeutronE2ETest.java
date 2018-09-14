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
    public void testNeutronSubnet() throws IOException, InterruptedException {
        NeutronSubnetTests.runTests(baseUrl);
    }

    @Test
    public void testNeutronPort() throws IOException, InterruptedException {
        NeutronPortTests.runTests(baseUrl);
    }

    @Test
    public void testNeutronRouter() throws IOException, InterruptedException {
        NeutronRouterTests.runTests(baseUrl);
    }

    @Test
    public void testNeutronFloatingIp() throws IOException, InterruptedException {
        NeutronFloatingIpTests.runTests(baseUrl);
    }

    @Test
    public void testNeutronSecurityGroup() throws IOException, InterruptedException {
        NeutronSecurityGroupTests.runTests(baseUrl);
    }

    @Test
    public void testNeutronSecurityRules() throws IOException, InterruptedException {
        NeutronSecurityRuleTests.runTests(baseUrl);
    }

    @Test
    public void testNeutronFirewall() throws IOException, InterruptedException {
        NeutronFirewallTests.runTests(baseUrl);
    }

    @Test
    public void testNeutronFirewallPolicy() throws IOException, InterruptedException {
        NeutronFirewallPolicyTests.runTests(baseUrl);
    }

    @Test
    public void testNeutronFirewallRules() throws IOException, InterruptedException {
        NeutronFirewallRuleTests.runTests(baseUrl);
    }

    @Test
    public void testNeutronLoadBalancer() throws IOException, InterruptedException {
        NeutronLoadBalancerTests.runTests(baseUrl);
    }

    @Test
    public void testNeutronLBListener() throws IOException, InterruptedException {
        NeutronLBListenerTests.runTests(baseUrl);
    }

    @Test
    public void testNeutronLBPool() throws IOException, InterruptedException {
        NeutronLBPoolTests.runTests(baseUrl);
    }

    @Test
    public void testNeutronLBPoolMembers() throws IOException, InterruptedException {
        NeutronLBPoolMembersTests.runTests(baseUrl);
    }

    @Test
    public void testNeutronLBHealthMonitor() throws IOException, InterruptedException {
        NeutronLBHealthMonitorTests.runTests(baseUrl);
    }

    @Test
    public void testNeutronMeteringLabel() throws IOException, InterruptedException {
        NeutronMeteringLabelTests.runTests(baseUrl);
    }

    @Test
    public void testNeutronMeteringRule() throws IOException, InterruptedException {
        NeutronMeteringRuleTests.runTests(baseUrl);
    }

    @Test
    public void testNeutronVpnServices() throws IOException, InterruptedException {
        NeutronVpnServicesTests.runTests(baseUrl);
    }

    @Test
    public void testNeutronIpSecPolicies() throws IOException, InterruptedException {
        NeutronIpSecPoliciesTests.runTests(baseUrl);
    }

    @Test
    public void testNeutronIpSecSiteConnection() throws IOException, InterruptedException {
        NeutronIpSecSiteConnectionTests.runTests(baseUrl);
    }

    @Test
    public void testNeutronIKEPolicies() throws IOException, InterruptedException {
        NeutronIKEPoliciesTests.runTests(baseUrl);
    }

    @Test
    public void testNeutronBgpvpn() throws IOException, InterruptedException {
        NeutronBgpvpnTests.runTests(baseUrl);
    }

    @Test
    public void testNeutronL2Gateway() throws IOException, InterruptedException {
        NeutronL2GatewayTests.runTests(baseUrl);
    }

    @Test
    public void testNeutronL2GatewayConnection() throws IOException, InterruptedException {
        NeutronL2GatewayConnectionTests.runTests(baseUrl);
    }

    @Test
    public void testNeutronQosPolicy() throws IOException, InterruptedException {
        NeutronQosPolicyTests.runTests(baseUrl);
    }

    @Test
    public void testNeutronSFCPortPair() throws IOException, InterruptedException {
        NeutronSFCPortPairTests.runTests(baseUrl);
    }

    @Test
    public void testNeutronSFCPortPairGroup() throws IOException, InterruptedException {
        NeutronSFCPortPairGroupTests.runTests(baseUrl);
    }

    @Test
    public void testNeutronSFCPortChain() throws IOException, InterruptedException {
        NeutronSFCPortChainTests.runTests(baseUrl);
    }

    @Test
    public void testNeutronSFCFlowClassifier() throws IOException, InterruptedException {
        NeutronSFCFlowClassifierTests.runTests(baseUrl);
    }

    @Test
    public void testNeutronTrunk() throws IOException, InterruptedException {
        NeutronTrunkTests.runTests(baseUrl);
    }

    @Test
    public void testNeutronRevisionNumber() throws IOException, InterruptedException {
        NeutronRevisionNumberTests.runTests(baseUrl);
    }

    @Test
    public void testNeutronProjectId() throws IOException, InterruptedException {
        NeutronProjectIdTests.runTests(baseUrl);
    }

    // tests related to bugs

    @Test
    public void testNeutronBug3812() throws IOException, InterruptedException {
        NeutronBug3812Tests.runTests(baseUrl);
    }

    @Test
    public void testNeutronPortsIpV6() throws IOException, InterruptedException {
        TempestPortsIpV6TestJSON.runTests(baseUrl);
    }

    @Test
    public void testNeutronBug4027() throws IOException, InterruptedException {
        NeutronBug4027Tests.runTests(baseUrl);
    }
}
