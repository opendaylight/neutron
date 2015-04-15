/*
 * Copyright (C) 2015 IBM, Inc.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.e2etest;

import java.io.OutputStreamWriter;

import java.net.HttpURLConnection;
import java.net.URL;

import org.junit.Assert;

public class NeutronNetworkTests {
    String base;

    public NeutronNetworkTests(String base) {
        this.base = base;
    }

    public void network_collection_get_test() {
        String url_s = base + "/networks";
        try {
            URL url = new URL(url_s);
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            httpConn.setRequestMethod("GET");
            httpConn.setRequestProperty("Content-Type", "application/json");
            httpConn.setRequestProperty("Authorization", "Basic YWRtaW46YWRtaW4=");
            Assert.assertEquals("Network Collection GET failed",
                200, httpConn.getResponseCode());
        } catch (Exception e) {
            Assert.assertFalse("E2E Tests Failed", true);
        }
    }

    //TODO handle SB check
    public void singleton_network_create_test() {
        String url_s = base + "/networks";
        String content = "{ \"network\": {" +
            " \"status\": \"ACTIVE\", \"subnets\": [], " +
            " \"name\": \"net1\", \"admin_state_up\": true, " +
            " \"tenant_id\": \"9bacb3c5d39d41a79512987f338cf177\", " +
            " \"router:external\": false, \"segments\": [ " +
            " { \"provider:segmentation_id\": 2, " +
            " \"provider:physical_network\": \"8bab8453-1bc9-45af-8c70-f83aa9b50453\", " +
            " \"provider:network_type\": \"vlan\" }, { " +
            " \"provider:segmentation_id\": null, " +
            " \"provider:physical_network\": \"8bab8453-1bc9-45af-8c70-f83aa9b50453\", " +
            " \"provider:network_type\": \"stt\" } ], " +
            " \"shared\": false, \"id\": \"4e8e5957-649f-477b-9e5b-f1f75b21c03c\" " +
            " } } ";

        try {
            URL url = new URL(url_s);
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            httpConn.setRequestMethod("POST");
            httpConn.setRequestProperty("Content-Type", "application/json");
            httpConn.setRequestProperty("Authorization", "Basic YWRtaW46YWRtaW4=");
            httpConn.setDoOutput(true);
            OutputStreamWriter out = new OutputStreamWriter(
                httpConn.getOutputStream());
            out.write(content);
            out.close();
            Assert.assertEquals("Singleton Network Post Failed NB",
                201, httpConn.getResponseCode());
        } catch (Exception e) {
            Assert.assertFalse("E2E Tests Failed", true);
        }
    }
}
