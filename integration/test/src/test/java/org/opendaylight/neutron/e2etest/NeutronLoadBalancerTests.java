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

public class NeutronLoadBalancerTests {
    String base;

    public NeutronLoadBalancerTests(String base) {
        this.base = base;
    }

    public void loadBalancer_collection_get_test() {
        String url_s = base + "/lbaas/loadbalancers";
        try {
            URL url = new URL(url_s);
            HttpURLConnection httpConn = ITNeutronE2E.HttpURLConnectionFactoryGet(url);
            Assert.assertEquals("Load Balancer Collection GET failed",
                        200, httpConn.getResponseCode());
        } catch (Exception e) {
            Assert.assertFalse("E2E Tests Failed", true);
        }
    }

    //TODO handle SB check
    public void singleton_loadbalancer_create_test() {
        String url_s = base + "/lbaas/loadbalancers";
        String content = "{ \"loadbalancer\": { " +
            "\"admin_state_up\": true, " +
            "\"description\": \"simple lb\", " +
            "\"id\": \"a36c20d0-18e9-42ce-88fd-82a35977ee8c\", " +
            "\"listeners\": [], " +
            "\"name\": \"loadbalancer1\", " +
            "\"operating_status\": \"ONLINE\", " +
            "\"provisioning_status\": \"ACTIVE\", " +
            "\"tenant_id\": \"b7c1a69e88bf4b21a8148f787aef2081\", " +
            "\"vip_address\": \"10.0.0.4\", " +
            "\"vip_subnet_id\": \"013d3059-87a4-45a5-91e9-d721068ae0b2\" } }";

        try {
            URL url = new URL(url_s);
            HttpURLConnection httpConn = ITNeutronE2E.HttpURLConnectionFactoryPost(url, content);
            Assert.assertEquals("Singleton Load Balancer Post Failed NB",
                201, httpConn.getResponseCode());
        } catch (Exception e) {
            Assert.assertFalse("E2E Tests Failed", true);
        }
    }
}
