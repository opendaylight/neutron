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
        String url = base + "/lbaas/loadbalancers";
        ITNeutronE2E.test_fetch(url, "Load Balancer Collection GET failed");
    }

    //TODO handle SB check
    public void singleton_loadbalancer_create_test() {
        String url = base + "/lbaas/loadbalancers";
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
        ITNeutronE2E.test_create(url, content, "Singleton Load Balancer Post Failed NB");
    }

    public void loadBalancer_update_test() {
        String url = base + "/lbaas/loadbalancers/a36c20d0-18e9-42ce-88fd-82a35977ee8c";
        String content = " { \"loadbalancer\": { \"admin_state_up\": false," +
            "\"description\": \"simple lb2\"," +
            "\"id\": \"a36c20d0-18e9-42ce-88fd-82a35977ee8c\"," +
            "\"listeners\": []," +
            "\"name\": \"loadbalancer2\"," +
            "\"operating_status\": \"ONLINE\"," +
            "\"provisioning_status\": \"PENDING_UPDATE\"," +
            "\"tenant_id\": \"b7c1a69e88bf4b21a8148f787aef2081\"," +
            "\"vip_address\": \"10.0.0.4\"," +
            "\"vip_subnet_id\": \"013d3059-87a4-45a5-91e9-d721068ae0b2\" } }";
        ITNeutronE2E.test_modify(url, content,"Load Balancer Put Failed");
    }
   
    public void loadBalancer_element_get_test() {
        String url = base + "/lbaas/loadbalancers/a36c20d0-18e9-42ce-88fd-82a35977ee8c";
        ITNeutronE2E.test_fetch(url, true ,"Load Balancer Element Get Failed");
    }

    public void loadBalancer_element_negative_get_test() {
        String url = base + "/lbaas/loadbalancers/a36c20d0-18e9-42ce-88fd-82a35977ee8c";
        ITNeutronE2E.test_fetch(url, false ,"Load Balancer Element Negative Get Failed");
    }

    public void loadBalancer_delete_test() {
        String url = base + "/lbaas/loadbalancers/a36c20d0-18e9-42ce-88fd-82a35977ee8c";
        ITNeutronE2E.test_delete(url, "Load Balancer Element Delete Failed");
    }

    public static void runTests(String base) {
        NeutronLoadBalancerTests loadBalancer_tester = new NeutronLoadBalancerTests(base);
        loadBalancer_tester.loadBalancer_collection_get_test();
        loadBalancer_tester.singleton_loadbalancer_create_test();
        loadBalancer_tester.loadBalancer_update_test();
        loadBalancer_tester.loadBalancer_element_get_test();
        loadBalancer_tester.loadBalancer_delete_test();
        loadBalancer_tester.loadBalancer_element_negative_get_test();
    }
}
