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
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.karafDistributionConfiguration;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.keepRuntimeFolder;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.logLevel;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UncheckedIOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Set;
import javax.inject.Inject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.karaf.options.KarafDistributionOption;
import org.ops4j.pax.exam.karaf.options.LogLevelOption.LogLevel;
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
                        .frameworkUrl(maven().groupId("org.opendaylight.neutron").artifactId("neutron-karaf")
                                .type("zip").versionAsInProject())
                        .karafVersion("3.0.3").name("Neutron").unpackDirectory(new File("target/pax"))
                        .useDeployFolder(false),
                // It is really nice if the container sticks around after the test so you can check the contents
                // of the data directory when things go wrong.
                vmOption("-javaagent:../jars/org.jacoco.agent.jar=destfile=jacoco-it.exec"), keepRuntimeFolder(),
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
                        .useOptions(KarafDistributionOption.debugConfiguration(KARAF_DEBUG_PORT, true)), };
    }

    final String base = "http://127.0.0.1:8080/controller/nb/v2/neutron";

    @Test
    public void test() throws IOException, InterruptedException {
        NeutronNetworkTests.runTests(base);
        NeutronSubnetTests.runTests(base);
        NeutronPortTests.runTests(base);
        NeutronRouterTests.runTests(base);
        NeutronFloatingIpTests.runTests(base);
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
        NeutronVpnServicesTests.runTests(base);
        NeutronIpSecPoliciesTests.runTests(base);
        NeutronIpSecSiteConnectionTests.runTests(base);
        NeutronIKEPoliciesTests.runTests(base);
        NeutronBgpvpnTests.runTests(base);
        NeutronL2GatewayTests.runTests(base);
        NeutronL2GatewayConnectionTests.runTests(base);
        NeutronQosPolicyTests.runTests(base);
        NeutronSFCPortPairTests.runTests(base);
        NeutronSFCPortPairGroupTests.runTests(base);
        NeutronSFCPortChainTests.runTests(base);
        NeutronSFCFlowClassifierTests.runTests(base);
        NeutronTrunkTests.runTests(base);

        // tests related to bugs
        NeutronBug3812Tests.runTests(base);
        TempestPortsIpV6TestJSON.runTests(base);
        NeutronBug4027Tests.runTests(base);
    }

    static HttpURLConnection httpURLConnectionFactoryGet(URL url) throws IOException {
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        httpConn.setRequestMethod("GET");
        httpConn.setRequestProperty("Content-Type", "application/json");
        httpConn.setRequestProperty("Authorization", "Basic YWRtaW46YWRtaW4=");
        return httpConn;
    }

    static HttpURLConnection httpURLConnectionFactoryDelete(URL url) throws IOException {
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        httpConn.setRequestMethod("DELETE");
        httpConn.setRequestProperty("Content-Type", "application/json");
        httpConn.setRequestProperty("Authorization", "Basic YWRtaW46YWRtaW4=");
        return httpConn;
    }

    static HttpURLConnection httpURLConnectionFactoryPost(URL url, String content) throws IOException {
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        httpConn.setRequestMethod("POST");
        httpConn.setRequestProperty("Content-Type", "application/json");
        httpConn.setRequestProperty("Authorization", "Basic YWRtaW46YWRtaW4=");
        httpConn.setDoOutput(true);
        OutputStreamWriter out = new OutputStreamWriter(httpConn.getOutputStream());
        out.write(content);
        out.close();
        return httpConn;
    }

    static HttpURLConnection httpURLConnectionFactoryPut(URL url, String content) throws IOException {
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        httpConn.setRequestMethod("PUT");
        httpConn.setRequestProperty("Content-Type", "application/json");
        httpConn.setRequestProperty("Authorization", "Basic YWRtaW46YWRtaW4=");
        httpConn.setDoOutput(true);
        OutputStreamWriter out = new OutputStreamWriter(httpConn.getOutputStream());
        out.write(content);
        out.close();
        return httpConn;
    }

    static void test_create(String urlStr, String content, String context) {
        try {
            URL url = new URL(urlStr);
            HttpURLConnection httpConn = httpURLConnectionFactoryPost(url, content);
            Assert.assertEquals(context, 201, httpConn.getResponseCode());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    static void test_create(String urlStr, int responseCode, String content, String context) {
        try {
            URL url = new URL(urlStr);
            HttpURLConnection httpConn = httpURLConnectionFactoryPost(url, content);
            Assert.assertEquals(context, responseCode, httpConn.getResponseCode());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    static void test_modify(String urlStr, String content, String context) {
        try {
            URL url = new URL(urlStr);
            HttpURLConnection httpConn = httpURLConnectionFactoryPut(url, content);
            Assert.assertEquals(context, 200, httpConn.getResponseCode());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    static void test_fetch(String urlStr, int responseCode, String context) {
        try {
            URL url = new URL(urlStr);
            HttpURLConnection httpConn = httpURLConnectionFactoryGet(url);
            Assert.assertEquals(context, responseCode, httpConn.getResponseCode());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    static void test_fetch(String urlStr, String context) {
        test_fetch(urlStr, 200, context);
    }

    static void test_fetch(String urlStr, boolean positiveTest, String context) {
        int responseCode = positiveTest ? 200 : 404;
        test_fetch(urlStr, responseCode, context);
    }

    static void test_delete(String urlStr, int responseCode, String context) {
        try {
            URL url = new URL(urlStr);
            HttpURLConnection httpConn = httpURLConnectionFactoryDelete(url);
            Assert.assertEquals(context, responseCode, httpConn.getResponseCode());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    static void test_delete(String urlStr, String context) {
        test_delete(urlStr, 204, context);
    }

    static void test_delete_404(String urlStr, String context) {
        test_delete(urlStr, 404, context);
    }

    private static String fetchResponse(String urlStr, String context) {
        StringBuffer response = new StringBuffer();

        try {
            URL url = new URL(urlStr);
            HttpURLConnection httpConn = httpURLConnectionFactoryGet(url);
            Assert.assertEquals(context, 200, httpConn.getResponseCode());
            BufferedReader in = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return response.toString();
    }

    static JsonObject test_fetch_gson(String urlStr, String context) {
        String response = fetchResponse(urlStr, context);
        Gson gson = new Gson();
        return gson.fromJson(response, JsonObject.class);
    }

    static void test_fetch_collection_response(String urlStr, String collectionName, String context) {
        String response = fetchResponse(urlStr, context);

        //Collection is returned in an array. Format - {"collectionName": [{...}, {....}]}
        Gson gson = new Gson();
        JsonObject jsonObjectOutput = gson.fromJson(response, JsonObject.class);
        Set<Map.Entry<String, JsonElement>> entrySet = jsonObjectOutput.entrySet();
        Assert.assertTrue("E2E Tests Failed - Json Error", entrySet.size() > 0);
        JsonElement jsonElementValue = entrySet.iterator().next().getValue();
        String key = entrySet.iterator().next().getKey();
        Assert.assertEquals(context, collectionName, key);
        Assert.assertTrue("E2E Tests Failed - Collection not Array", jsonElementValue.isJsonArray());
        JsonArray jsonArray = jsonElementValue.getAsJsonArray();
        Assert.assertNotEquals(context, jsonArray.size(), 0);
    }

    // Helper function - content is json used during create. Format - {"Name": {...}}
    static void test_fetch_with_one_query_item(String urlStr, String content, String collectionName) {
        Gson gson = new Gson();
        JsonObject jsonObjectInput = gson.fromJson(content, JsonObject.class);
        Set<Map.Entry<String, JsonElement>> entrySet = jsonObjectInput.entrySet();
        JsonObject jsonObjectOutput = entrySet.iterator().next().getValue().getAsJsonObject();
        for (Map.Entry<String, JsonElement> element : jsonObjectOutput.entrySet()) {
            String key = element.getKey();
            JsonElement jsonElementValue = element.getValue();
            // Query only values that are non null Primitives - Integer,Strings,character and boolean
            if (jsonElementValue.isJsonPrimitive() && !jsonElementValue.isJsonNull()) {
                String valueStr = jsonElementValue.getAsString();
                valueStr = valueStr.replaceAll("\\s+", "+");
                String queryUrl = urlStr + "?" + key + "=" + valueStr;
                String context = collectionName + " " + key + "=" + jsonElementValue.toString() + " Get Failed";
                test_fetch_collection_response(queryUrl, collectionName, context);
            }
        }
    }
}
