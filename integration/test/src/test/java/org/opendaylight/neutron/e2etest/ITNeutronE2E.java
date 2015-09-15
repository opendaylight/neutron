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
            when(Boolean.getBoolean("karaf.debug"))
                    .useOptions(KarafDistributionOption.debugConfiguration("5005", true)),
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
       // It is really nice if the container sticks around after the test so you can check the contents
       // of the data directory when things go wrong.
            vmOption("-javaagent:../jars/org.jacoco.agent.jar=destfile=jacoco-it.exec"),
            when(Boolean.getBoolean("karaf.keep.unpack")).useOptions(keepRuntimeFolder()),
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
       //   debugConfiguration("5000", true),
       };
    }

    final String base = "http://127.0.0.1:8080/controller/nb/v2/neutron";

    @Test
    public void test() {
        NeutronNetworkTests.runTests(base);
        NeutronSubnetTests.runTests(base);
        NeutronPortTests.runTests(base);
        NeutronRouterTests.runTests(base);
        NeutronFloatingIPTests.runTests(base);
        NeutronSecurityGroupTests.runTests(base);
        NeutronSecurityRuleTests.runTests(base);
        NeutronFirewallTests.runTests(base);
        NeutronFirewallPolicyTests.runTests(base);
        NeutronFirewallRuleTests.runTests(base);
        NeutronLoadBalancerTests.runTests(base);
        NeutronLBListenerTests.runTests(base);
        NeutronLBPoolTests.runTests(base);
        NeutronLBPoolMembersTests.runTests(base);
        NeutronLBHealthMonitorTests.runTests(base);
        NeutronMeteringLabelTests.runTests(base);
        NeutronMeteringRuleTests.runTests(base);
        NeutronVPNServicesTests.runTests(base);
        NeutronIPSECPoliciesTests.runTests(base);
        NeutronIPSECSiteConnectionTests.runTests(base);
        NeutronIKEPoliciesTests.runTests(base);

    // tests related to bugs
        Neutron_Bug3812_Tests.runTests(base);
        Tempest_PortsIpV6TestJSON.runTests(base);
        Neutron_Bug4027_Tests.runTests(base);
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

    static void test_fetch(String url_s, boolean positiveTest, String context) {
        try {
            URL url = new URL(url_s);
            HttpURLConnection httpConn = HttpURLConnectionFactoryGet(url);
            if (positiveTest) {
                Assert.assertEquals(context, 200, httpConn.getResponseCode());
            } else {
                Assert.assertEquals(context, 404, httpConn.getResponseCode());
            }
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
