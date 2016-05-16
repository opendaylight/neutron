/*
 * Copyright (C) 2015 IBM, Inc.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.e2etest;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import java.lang.Thread;

import java.net.HttpURLConnection;
import java.net.URL;

import org.junit.Assert;

public class NeutronRouterTests {
    String base;

    public NeutronRouterTests(String base) {
        this.base = base;
    }

    public void router_collection_get_test() {
        String url = base + "/routers";
        ITNeutronE2E.test_fetch(url, "Router Collection GET failed");
    }

    //TODO handle SB check
    public String singleton_router_create_test() {
        String url = base + "/routers";
        String content = "{ \"router\": { " +
            "\"status\": \"ACTIVE\", " +
            "\"external_gateway_info\": { " +
                "\"network_id\": \"8ca37218-28ff-41cb-9b10-039601ea7e6b\" }, " +
            "\"name\": \"another_router\", " +
            "\"admin_state_up\": true, " +
            "\"tenant_id\": \"9bacb3c5d39d41a79512987f338cf177\", " +
            "\"id\": \"8604a0de-7f6b-409a-a47c-a1cc7bc77b2e\" } } ";
        ITNeutronE2E.test_create(url, content, "Singleton Router Post Failed NB");
        return content;
    }

    public void singleton_router_get_with_one_query_item_test(String createJsonString) {
        String url = base + "/routers";
        ITNeutronE2E.test_fetch_with_one_query_item(url, createJsonString, "routers");
    }

    //TODO handle SB check
    public void create_router() {
        String url = base + "/routers";
        String content = "{ \"router\": { " +
            "\"status\": \"ACTIVE\", " +
            "\"external_gateway_info\": { " +
                "\"network_id\": \"8ca37218-28ff-41cb-9b10-039601ea7e6b\" }, " +
            "\"name\": \"another_router\", " +
            "\"admin_state_up\": true, " +
            "\"tenant_id\": \"9bacb3c5d39d41a79512987f338cf177\", " +
            "\"id\": \"8604a0de-7f6b-409a-a47c-a1cc7bc77b2f\" } } ";
        ITNeutronE2E.test_create(url, content, "Singleton Router Post Failed NB");
    }

    //TODO handle SB check
    public void update_router_test() {
        String url = base + "/routers/8604a0de-7f6b-409a-a47c-a1cc7bc77b2e";
        String content = "{ \"router\": { " +
            "\"status\": \"ACTIVE\", " +
            "\"external_gateway_info\": { " +
                "\"network_id\": \"8ca37218-28ff-41cb-9b10-039601ea7e6b\" }, " +
            "\"name\": \"new_name\", " +
            "\"admin_state_up\": true, " +
            "\"tenant_id\": \"9bacb3c5d39d41a79512987f338cf177\", " +
            "\"id\": \"8604a0de-7f6b-409a-a47c-a1cc7bc77b2e\" } } ";
        ITNeutronE2E.test_modify(url, content, "Singleton Router Put Failed NB");
    }

    public void router_element_get_test() {
        String url = base + "/routers/8604a0de-7f6b-409a-a47c-a1cc7bc77b2f";
        ITNeutronE2E.test_fetch(url, true, "Router Element Get Test");
    }

    public void router_element_get_with_query_test() {
        String url = base + "/routers/8604a0de-7f6b-409a-a47c-a1cc7bc77b2f"+
            "?fields=id&fields=admin_state_up&fields=name&fields=status" +
            "&fields=tenant_id&fields=external_gateway_info" +
            "&fields=limit&fields=marker&fields=page_reverse";;
        ITNeutronE2E.test_fetch(url, true, "Router Element Get With Query Test");
    }

    public void router_delete_test() {
        String url = base + "/routers/8604a0de-7f6b-409a-a47c-a1cc7bc77b2f";
        ITNeutronE2E.test_delete(url, "Router Delete Test");
    }

    public void router_element_negative_get_test() {
        String url = base + "/routers/8604a0de-7f6b-409a-a47c-a1cc7bc77b2f";
        ITNeutronE2E.test_fetch(url, false, "Router Element Negative Get Test");
    }

    public static void runTests(String base) {
        NeutronRouterTests router_tester = new NeutronRouterTests(base);
        String createJsonString = router_tester.singleton_router_create_test();
        router_tester.singleton_router_get_with_one_query_item_test(createJsonString);
        router_tester.update_router_test();
        router_tester.create_router(); // needed for following tests
        router_tester.router_element_get_test();
        router_tester.router_element_get_with_query_test();
        router_tester.router_collection_get_test();
        router_tester.router_delete_test();
        router_tester.router_element_negative_get_test();
    }
}

