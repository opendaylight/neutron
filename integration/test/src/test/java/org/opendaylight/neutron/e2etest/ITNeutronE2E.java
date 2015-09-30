/*
 * Copyright (C) 2015 IBM, Inc.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.e2etest;

import static org.ops4j.pax.exam.CoreOptions.maven;
import static org.ops4j.pax.exam.CoreOptions.vmOption;
import static org.ops4j.pax.exam.CoreOptions.when;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.configureConsole;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.debugConfiguration;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.features;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.karafDistributionConfiguration;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.keepRuntimeFolder;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.logLevel;
import static org.ops4j.pax.exam.karaf.options.LogLevelOption.LogLevel;

import java.io.File;
import java.io.OutputStreamWriter;

import java.net.HttpURLConnection;
import java.net.URL;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.junit.PaxExam;

import org.ops4j.pax.exam.karaf.options.KarafDistributionOption;
import org.osgi.framework.BundleContext;
import org.osgi.service.cm.ConfigurationAdmin;

@RunWith(PaxExam.class)
public class ITNeutronE2E {

    private static final String KARAF_DEBUG_PORT = "5005";
    private static final String KARAF_DEBUG_PROP = "karaf.debug";

    @Inject
    private BundleContext bundleContext;

    @Inject
    private ConfigurationAdmin configurationAdmin;

    @Configuration
    public Option[] config() {
        return new Option[] {
            // Provision and launch a container based on a distribution of Karaf (Apache ServiceMix).
            // FIXME: need to *NOT* hardcode the version here - it breaks on
            // version bumps
            karafDistributionConfiguration()
                .frameworkUrl(
                    maven()
                        .groupId("org.opendaylight.neutron")
                        .artifactId("neutron-karaf")
                        .type("zip")
                        .versionAsInProject())
                .karafVersion("3.0.3")
                .name("Neutron")
                .unpackDirectory(new File("target/pax"))
                .useDeployFolder(false),
            vmOption("-javaagent:../jars/org.jacoco.agent.jar=destfile=jacoco-it.exec"),
       // It is really nice if the container sticks around after the test so you can check the contents
       // of the data directory when things go wrong.
            keepRuntimeFolder(),
       // Don't bother with local console output as it just ends up cluttering the logs
            configureConsole().ignoreLocalConsole(),
       // Force the log level to INFO so we have more details during the test.  It defaults to WARN.
            logLevel(LogLevel.INFO),
       // provision the needed features for this test
       //    features("mvn:org.opendaylight.neutron/features-test/0.5.0-SNAPSHOT/xml/features",
       //        "features-neutron-test"),
       // Remember that the test executes in another process.  If you want to debug it, you need
       // to tell Pax Exam to launch that process with debugging enabled.  Launching the test class itself with
       // debugging enabled (for example in Eclipse) will not get you the desired results.
            when(Boolean.getBoolean(KARAF_DEBUG_PROP))
                    .useOptions(KarafDistributionOption.debugConfiguration(KARAF_DEBUG_PORT, true)),
       };
    }

    final String base = "http://127.0.0.1:8080/controller/nb/v2/neutron";

    @Test
    public void test() {
        NeutronNetworkTests network_tester = new NeutronNetworkTests(base);
        network_tester.network_collection_get_test();
        network_tester.singleton_network_create_test();
	network_tester.external_network_create_test(); //needed for router test

        NeutronSubnetTests subnet_tester = new NeutronSubnetTests(base);
        subnet_tester.subnet_collection_get_test();
        subnet_tester.singleton_subnet_create_test();
        subnet_tester.external_subnet_create_test(); //needed for router test

        NeutronPortTests port_tester = new NeutronPortTests(base);
        port_tester.port_collection_get_test();
        port_tester.singleton_port_create_test();
        port_tester.router_interface_port_create_test(); //needed for router test

        NeutronRouterTests router_tester = new NeutronRouterTests(base);
        router_tester.router_collection_get_test();
        router_tester.singleton_router_create_test();
        router_tester.router_add_interface_test();

        NeutronFloatingIPTests floatingIP_tester = new NeutronFloatingIPTests(base);
        floatingIP_tester.floatingIP_collection_get_test();

        NeutronSecurityGroupTests securityGroup_tester = new NeutronSecurityGroupTests(base);
        securityGroup_tester.securityGroup_collection_get_test();

        NeutronSecurityRuleTests securityRule_tester = new NeutronSecurityRuleTests(base);
        securityRule_tester.securityRule_collection_get_test();

        NeutronLoadBalancerTests loadBalancer_tester = new NeutronLoadBalancerTests(base);
        loadBalancer_tester.loadBalancer_collection_get_test();
        loadBalancer_tester.singleton_loadbalancer_create_test();

        NeutronLBListenerTests listener_tester = new NeutronLBListenerTests(base);
        listener_tester.listener_collection_get_test();
        listener_tester.singleton_lb_listener_create_test();

        NeutronLBPoolTests pool_tester = new NeutronLBPoolTests(base);
        pool_tester.pool_collection_get_test();
        pool_tester.singleton_lb_pool_create_test();

        NeutronLBHealthMonitorTests healthMonitor_tester = new NeutronLBHealthMonitorTests(base);
        healthMonitor_tester.healthMonitor_collection_get_test();

//  TODO: add LoadBalancerPoolMembers testing

        Li_NeutronLoadBalancerTests li_loadBalancer_tester = new Li_NeutronLoadBalancerTests(base);
        li_loadBalancer_tester.li_loadBalancer_collection_get_test();
        li_loadBalancer_tester.singleton_loadbalancer_create_test();

        Li_NeutronLBListenerTests li_listener_tester = new Li_NeutronLBListenerTests(base);
        li_listener_tester.li_listener_collection_get_test();
        li_listener_tester.singleton_lb_listener_create_test();

        Li_NeutronLBPoolTests li_pool_tester = new Li_NeutronLBPoolTests(base);
        li_pool_tester.li_pool_collection_get_test();
        li_pool_tester.singleton_lb_pool_create_test();

        Li_NeutronLBHealthMonitorTests li_healthMonitor_tester = new Li_NeutronLBHealthMonitorTests(base);
        li_healthMonitor_tester.li_healthMonitor_collection_get_test();

//  TODO: add LoadBalancerPoolMembers testing

        NeutronMeteringLabelTests meteringLabel_tester = new NeutronMeteringLabelTests(base);
        meteringLabel_tester.meteringLabel_collection_get_test();

        NeutronMeteringRuleTests meteringRule_tester = new NeutronMeteringRuleTests(base);
        meteringRule_tester.meteringRule_collection_get_test();

        NeutronVPNServicesTests vpnService_tester = new NeutronVPNServicesTests(base);
        vpnService_tester.vpnService_collection_get_test();

        NeutronIKEPoliciesTests ike_policy_tester = new NeutronIKEPoliciesTests(base);
        ike_policy_tester.ikePolicy_collection_get_test();

        NeutronIPSECPoliciesTests ipsec_policy_tester = new NeutronIPSECPoliciesTests(base);
        ipsec_policy_tester.ipsecPolicy_collection_get_test();

        NeutronIPSECSiteConnectionTests ipsec_siteConnection_test = new NeutronIPSECSiteConnectionTests(base);
        ipsec_siteConnection_test.ipsecSiteConnection_collection_get_test();

    // tests related to bug 3812
       Neutron_Bug3812_Tests bugTest = new Neutron_Bug3812_Tests(base);
       bugTest.check_bug3812();

    // tempest PortsIpV6TestJSON.test_create_port_in_allowd_allocation_pools
       Tempest_PortsIpV6TestJSON tpv6runner = new Tempest_PortsIpV6TestJSON(base);
       tpv6runner.test_create_port_in_allowed_allocation_pools();
    }

    static HttpURLConnection HttpURLConnectionFactoryGet(URL url) throws Exception {
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        httpConn.setRequestMethod("GET");
        httpConn.setRequestProperty("Content-Type", "application/json");
        httpConn.setRequestProperty("Authorization", "Basic YWRtaW46YWRtaW4=");
        return(httpConn);
    }

    static HttpURLConnection HttpURLConnectionFactoryDelete(URL url) throws Exception {
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        httpConn.setRequestMethod("DELETE");
        httpConn.setRequestProperty("Content-Type", "application/json");
        httpConn.setRequestProperty("Authorization", "Basic YWRtaW46YWRtaW4=");
        return(httpConn);
    }

    static HttpURLConnection HttpURLConnectionFactoryPost(URL url, String content) throws Exception {
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        httpConn.setRequestMethod("POST");
        httpConn.setRequestProperty("Content-Type", "application/json");
        httpConn.setRequestProperty("Authorization", "Basic YWRtaW46YWRtaW4=");
        httpConn.setDoOutput(true);
        OutputStreamWriter out = new OutputStreamWriter(
            httpConn.getOutputStream());
        out.write(content);
        out.close();
        return(httpConn);
    }

    static HttpURLConnection HttpURLConnectionFactoryPut(URL url, String content) throws Exception {
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        httpConn.setRequestMethod("PUT");
        httpConn.setRequestProperty("Content-Type", "application/json");
        httpConn.setRequestProperty("Authorization", "Basic YWRtaW46YWRtaW4=");
        httpConn.setDoOutput(true);
        OutputStreamWriter out = new OutputStreamWriter(
            httpConn.getOutputStream());
        out.write(content);
        out.close();
        return(httpConn);
    }

    static void test_create(String url_s, String content, String context) {
        try {
            URL url = new URL(url_s);
            HttpURLConnection httpConn = HttpURLConnectionFactoryPost(url, content);
            Assert.assertEquals(context, 201, httpConn.getResponseCode());
        } catch (Exception e) {
            e.printStackTrace(); // temporary, remove me
            Assert.assertFalse("E2E Tests Failed", true);
        }
    }

    static void test_modify(String url_s, String content, String context) {
        try {
            URL url = new URL(url_s);
            HttpURLConnection httpConn = HttpURLConnectionFactoryPut(url, content);
            Assert.assertEquals(context, 200, httpConn.getResponseCode());
        } catch (Exception e) {
            e.printStackTrace(); // temporary, remove me
            Assert.assertFalse("E2E Tests Failed", true);
        }
    }

    static void test_fetch(String url_s, String context) {
        try {
            URL url = new URL(url_s);
            HttpURLConnection httpConn = HttpURLConnectionFactoryGet(url);
            Assert.assertEquals(context, 200, httpConn.getResponseCode());
        } catch (Exception e) {
            e.printStackTrace(); // temporary, remove me
            Assert.assertFalse("E2E Tests Failed", true);
        }
    }

    static void test_delete(String url_s, String context) {
        try {
            URL url = new URL(url_s);
            HttpURLConnection httpConn = HttpURLConnectionFactoryDelete(url);
            Assert.assertEquals(context, 204, httpConn.getResponseCode());
        } catch (Exception e) {
            e.printStackTrace(); // temporary, remove me
            Assert.assertFalse("E2E Tests Failed", true);
        }
    }
}
