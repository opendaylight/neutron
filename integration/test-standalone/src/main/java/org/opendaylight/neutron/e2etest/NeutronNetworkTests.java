/*
 * Copyright (C) 2015 IBM, Inc.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.e2etest;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NeutronNetworkTests {

    private static final int TIMEOUT = 180;

    String base;

    private static final Logger LOG = LoggerFactory.getLogger(NeutronNetworkTests.class);

    public NeutronNetworkTests(String base) {
        this.base = base;
    }

    public void network_collection_get_test_with_wait() throws IOException, InterruptedException {
        URL url = new URL(base + "/networks");
        for (int retry = 0; retry < TIMEOUT; retry++) {
            HttpURLConnection httpConn = HttpUtils.httpURLConnectionFactoryGet(url);
            int responseCode;
            try {
                responseCode = httpConn.getResponseCode();
            } catch (ConnectException e) {
                LOG.info("connection trial {} failed to URL {}", retry, url, e);
                Thread.sleep(1000);
                continue;
            }

            if (responseCode != 200) {
                LOG.info("trial {} to URL {} failed with {}", retry, url, httpConn.getResponseCode());
                Thread.sleep(1000);
            } else {
                Assert.assertEquals("Network Collection GET failed", 200, httpConn.getResponseCode());
                return;
            }
        }
        Assert.assertFalse("Network Collection GET to URL " + url + " with wait failed", true);
    }

    //TODO handle SB check
    public String singleton_network_create_test() {
        String url = base + "/networks";
        String content = "{ \"network\": {" + " \"status\": \"ACTIVE\", \"subnets\": [], "
                + " \"name\": \"net1\", \"admin_state_up\": true, "
                + " \"tenant_id\": \"9bacb3c5d39d41a79512987f338cf177\", "
                + " \"router:external\": false, \"segments\": [ " + " { \"provider:segmentation_id\": 2, "
                + " \"provider:physical_network\": \"8bab8453-1bc9-45af-8c70-f83aa9b50453\", "
                + " \"provider:network_type\": \"vlan\" }, { " + " \"provider:segmentation_id\": null, "
                + " \"provider:physical_network\": \"8bab8453-1bc9-45af-8c70-f83aa9b50453\", "
                + " \"provider:network_type\": \"stt\" } ], "
                + " \"shared\": false, \"vlan_transparent\": false, "
                + " \"id\": \"4e8e5957-649f-477b-9e5b-f1f75b21c03c\" " + " } } ";
        HttpUtils.test_create(url, content, "Singleton Network Post Failed NB");
        return content;
    }

    public void singleton_network_get_with_one_query_item_test(String createJsonString) {
        String url = base + "/networks";
        HttpUtils.test_fetch_with_one_query_item(url, createJsonString, "networks");
    }

    public String singleton_default_network_create_test() {
        String url = base + "/networks";
        String content = "{ \"network\": {" + "\"name\": \"netdefault\", \"subnets\": [], "
                + " \"tenant_id\": \"9bacb3c5d39d41a79512987f338cf177\", "
                + " \"router:external\": false, \"segments\": [ " + " { \"provider:segmentation_id\": 2, "
                + " \"provider:physical_network\": \"8bab8453-1bc9-45af-8c70-f83aa9b50453\", "
                + " \"provider:network_type\": \"vlan\" }, { " + " \"provider:segmentation_id\": null, "
                + " \"provider:physical_network\": \"8bab8453-1bc9-45af-8c70-f83aa9b50453\", "
                + " \"provider:network_type\": \"stt\" } ], "
                + " \"id\": \"de8e5957-d49f-d77b-de5b-d1f75b21c03c\" " + " } } ";
        HttpUtils.test_create(url, content, "Singleton Default Network Post Failed NB");
        return content;
    }

    public void default_network_content_validation_test() {
        //Validates Network default parameters are set.
        //Default parameters: status,shared
        String element = "status";
        String url = base + "/networks/de8e5957-d49f-d77b-de5b-d1f75b21c03c?fields=" + element;
        String expectedContent = "\"ACTIVE\"";
        String context = "Network details do not match.";
        JsonObject jsonObjectOutput = HttpUtils.test_fetch_gson(url, context);
        jsonObjectOutput = jsonObjectOutput.getAsJsonObject("network");
        JsonElement jsonElementValue = jsonObjectOutput.get(element);
        Assert.assertEquals(context, expectedContent, String.valueOf(jsonElementValue));
        element = "shared";
        url = base + "/networks/de8e5957-d49f-d77b-de5b-d1f75b21c03c?fields=" + element;
        jsonObjectOutput = HttpUtils.test_fetch_gson(url, context);
        jsonObjectOutput = jsonObjectOutput.getAsJsonObject("network");
        jsonElementValue = jsonObjectOutput.get(element);
        Assert.assertEquals(context, false, jsonElementValue.getAsBoolean());
        element = "vlan_transparent";
        url = base + "/networks/de8e5957-d49f-d77b-de5b-d1f75b21c03c?fields=" + element;
        jsonObjectOutput = HttpUtils.test_fetch_gson(url, context);
        jsonObjectOutput = jsonObjectOutput.getAsJsonObject("network");
        jsonElementValue = jsonObjectOutput.get(element);
        Assert.assertEquals(context, false, jsonElementValue.getAsBoolean());
    }

    //TODO handle SB check
    public void external_network_create_test() {
        String url = base + "/networks";
        String content = "{ \"network\": {" + " \"status\": \"ACTIVE\", \"subnets\": [], "
                + " \"name\": \"external1\", \"admin_state_up\": true, "
                + " \"tenant_id\": \"9bacb3c5d39d41a79512987f338cf177\", "
                + " \"router:external\": true, \"shared\": false, \"vlan_transparent\": false,"
                + " \"id\": \"8ca37218-28ff-41cb-9b10-039601ea7e6b\" } } ";
        HttpUtils.test_create(url, content, "External Network Post Failed NB");
    }

    //TODO handle SB check
    public void bulk_network_create_test() {
        String url = base + "/networks";
        String content = "{ \"networks\": [ { " + "\"status\": \"ACTIVE\", \"subnets\": [], "
                + "\"name\": \"sample_network3\", " + "\"provider:physical_network\": null, "
                + "\"admin_state_up\": true, " + "\"tenant_id\": \"4fd44f30292945e481c7b8a0c8908869\", "
                + "\"provider:network_type\": \"local\", " + "\"shared\": false, \"vlan_transparent\": false, "
                + "\"id\": \"bc1a76cb-8767-4c3a-bb95-018b822f2130\", " + "\"provider:segmentation_id\": null }, { "
                + "\"status\": \"ACTIVE\", " + "\"subnets\": [], " + "\"name\": \"sample_network4\", "
                + "\"provider:physical_network\": null, " + "\"admin_state_up\": true, "
                + "\"tenant_id\": \"4fd44f30292945e481c7b8a0c8908869\", " + "\"provider:network_type\": \"local\", "
                + "\"shared\": false, " + "\"vlan_transparent\": false, "
                + "\"id\": \"af374017-c9ae-4a1d-b799-ab73111476e2\", " + "\"provider:segmentation_id\": null } ] } ";
        HttpUtils.test_create(url, content, "Bulk Network Post Failed");
    }

    //TODO handle SB check
    public void network_update_test() {
        String url = base + "/networks/bc1a76cb-8767-4c3a-bb95-018b822f2130";
        String content = " { \"network\": { " + "\"status\": \"ACTIVE\", " + "\"subnets\": [], "
                + "\"name\": \"sample_network_5_updated\", " + "\"provider:physical_network\": null, "
                + "\"admin_state_up\": true, " + "\"tenant_id\": \"4fd44f30292945e481c7b8a0c8908869\", "
                + "\"provider:network_type\": \"local\", " + "\"router:external\": false, " + "\"shared\": false, "
                + "\"vlan_transparent\": false, " + "\"provider:segmentation_id\": null } } ";
        HttpUtils.test_modify(url, content, "Network Put Failed");
    }

    public void network_collection_get_test() {
        String urlS = base + "/networks";
        HttpUtils.test_fetch(urlS, true, "Network collection Get Failed");
    }

    public void network_element_get_test() {
        String url = base + "/networks/bc1a76cb-8767-4c3a-bb95-018b822f2130";
        HttpUtils.test_fetch(url, true, "Network Element Get Failed");
    }

    public void network_element_get_test_with_query() {
        String url = base + "/networks/bc1a76cb-8767-4c3a-bb95-018b822f2130"
                + "?fields=status&fields=subnets&fields=name"
                + "&fields=provider:physical_network&fields=admin_state_up"
                + "&fields=tenant_id&fields=provides:network_type"
                + "&fields=router:external&fields=shared&fields=vlan_transparent&fields=provider:segmentation_id";
        HttpUtils.test_fetch(url, true, "Network Element with query Get Failed");
    }

    public void network_element_negative_get_test() {
        String url = base + "/networks/bc1a76cb-8767-4c3a-bb95-018b822f2130";
        HttpUtils.test_fetch(url, false, "Network Element Negative Get Failed");
    }

    public void network_delete_test() {
        String url = base + "/networks/bc1a76cb-8767-4c3a-bb95-018b822f2130";
        HttpUtils.test_delete(url, "Network Element Delete Failed");
    }

    public static void runTests(String base) throws IOException, InterruptedException {
        NeutronNetworkTests networkTester = new NeutronNetworkTests(base);
        networkTester.network_collection_get_test_with_wait();
        String createJsonString = networkTester.singleton_network_create_test();
        networkTester.singleton_network_get_with_one_query_item_test(createJsonString);
        networkTester.singleton_default_network_create_test();
        networkTester.default_network_content_validation_test();
        networkTester.external_network_create_test(); //needed for router test
        networkTester.bulk_network_create_test();
        networkTester.network_update_test();
        networkTester.network_collection_get_test();
        networkTester.network_element_get_test();
        networkTester.network_element_get_test_with_query();
        networkTester.network_delete_test();
        networkTester.network_element_negative_get_test();
    }
}
