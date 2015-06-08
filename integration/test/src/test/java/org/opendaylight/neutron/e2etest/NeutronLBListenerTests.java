/*
 * Copyright (C) 2015 IBM, Inc.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.e2etest;

import java.io.OutputStreamWriter;

import java.lang.Thread;

import java.net.HttpURLConnection;
import java.net.URL;

import org.junit.Assert;

public class NeutronLBListenerTests {
    String base;

    public NeutronLBListenerTests(String base) {
        this.base = base;
    }

    public void listener_collection_get_test() {
        String url_s = base + "/lbaas/listeners";
        try {
            URL url = new URL(url_s);
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            httpConn.setRequestMethod("GET");
            httpConn.setRequestProperty("Content-Type", "application/json");
            httpConn.setRequestProperty("Authorization", "Basic YWRtaW46YWRtaW4=");
            Assert.assertEquals("LB Listener Collection GET failed",
                        200, httpConn.getResponseCode());
        } catch (Exception e) {
            Assert.assertFalse("E2E Tests Failed", true);
        }
    }

    //TODO handle SB check
    public void singleton_lb_listener_create_test() {
        String url_s = base + "/lbaas/listeners";
        String content = "{ \"listener\": { " +
            "\"admin_state_up\": true, " +
            "\"connection_limit\": 100, " +
            "\"default_pool_id\": null, " +
            "\"description\": \"listener one\", " +
            "\"id\": \"39de4d56-d663-46e5-85a1-5b9d5fa17829\", " +
            "\"loadbalancers\": [ { " +
                "\"id\": \"a36c20d0-18e9-42ce-88fd-82a35977ee8c\" } ], " +
            "\"name\": \"listener1\", " +
            "\"protocol\": \"HTTP\", " +
            "\"protocol_port\": 80, " +
            "\"tenant_id\": \"b7c1a69e88bf4b21a8148f787aef2081\" } }";

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
            Assert.assertEquals("Singleton LB Listener Post Failed NB",
                201, httpConn.getResponseCode());
        } catch (Exception e) {
            Assert.assertFalse("E2E Tests Failed", true);
        }
    }
}
