/*
 * Copyright (C) 2015 IBM, Inc.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.e2etest;

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
        String content = "{ \"router\": {" + "\"external_gateway_info\": { "
                + "\"network_id\": \"8ca37218-28ff-41cb-9b10-039601ea7e6b\" }, " + "\"name\": \"another_router\", "
                + "\"admin_state_up\": true, " + "\"tenant_id\": \"9bacb3c5d39d41a79512987f338cf177\", "
                + "\"id\": \"8604a0de-7f6b-409a-a47c-a1cc7bc77b2e\" } } ";
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
        String content = "{ \"router\": {" + "\"external_gateway_info\": { "
                + "\"network_id\": \"8ca37218-28ff-41cb-9b10-039601ea7e6b\" }, " + "\"name\": \"another_router\", "
                + "\"admin_state_up\": true, " + "\"tenant_id\": \"9bacb3c5d39d41a79512987f338cf177\", "
                + "\"id\": \"8604a0de-7f6b-409a-a47c-a1cc7bc77b2f\" } } ";
        ITNeutronE2E.test_create(url, content, "Singleton Router Post Failed NB");
    }

    //TODO handle SB check
    public void update_router_test() {
        String url = base + "/routers/8604a0de-7f6b-409a-a47c-a1cc7bc77b2e";
        String content = "{ \"router\": { " + "\"external_gateway_info\": { "
                + "\"network_id\": \"8ca37218-28ff-41cb-9b10-039601ea7e6b\" }, " + "\"name\": \"new_name\", "
                + "\"admin_state_up\": true, " + "\"tenant_id\": \"9bacb3c5d39d41a79512987f338cf177\", "
                + "\"id\": \"8604a0de-7f6b-409a-a47c-a1cc7bc77b2e\" } } ";
        ITNeutronE2E.test_modify(url, content, "Singleton Router Put Failed NB");
    }

    //TODO handle SB check
    public void router_add_interface_test() {
        String url = base + "/routers/8604a0de-7f6b-409a-a47c-a1cc7bc77b2e/add_router_interface";
        String content = "{ " + "\"subnet_id\": \"3b80198d-4f7b-4f77-9ef5-774d54e17126\", "
                + "\"port_id\": \"d8a4cc85-ad78-46ac-b5a1-8e04f16fa51e\", "
                + "\"tenant_id\": \"9bacb3c5d39d41a79512987f338cf177\", "
                + "\"id\": \"8604a0de-7f6b-409a-a47c-a1cc7bc77b2e\"}";
        ITNeutronE2E.test_modify(url, content, "Add Interface to Router Put Failed NB");
    }

    //TODO handle SB check
    public void router_add_interface() {
        String url = base + "/routers/8604a0de-7f6b-409a-a47c-a1cc7bc77b2f/add_router_interface";
        String content = "{ " + "\"subnet_id\": \"3b80198d-4f7b-4f77-9ef5-774d54e17126\", "
                + "\"port_id\": \"d8a4cc85-ad78-46ac-b5a1-8e04f16fa51f\", "
                + "\"tenant_id\": \"9bacb3c5d39d41a79512987f338cf177\", "
                + "\"id\": \"8604a0de-7f6b-409a-a47c-a1cc7bc77b2f\"}";
        ITNeutronE2E.test_modify(url, content, "Router Prep Interface Add Failed");
    }

    //TODO handle SB check
    public void router_remove_interface_test() {
        String url = base + "/routers/8604a0de-7f6b-409a-a47c-a1cc7bc77b2f/remove_router_interface";
        String content = "{ " + "\"subnet_id\": \"3b80198d-4f7b-4f77-9ef5-774d54e17126\", "
                + "\"port_id\": \"d8a4cc85-ad78-46ac-b5a1-8e04f16fa51f\", "
                + "\"tenant_id\": \"9bacb3c5d39d41a79512987f338cf177\", "
                + "\"id\": \"8604a0de-7f6b-409a-a47c-a1cc7bc77b2f\"}";
        ITNeutronE2E.test_modify(url, content, "Remove Interface to Router Put Failed NB");
    }

    public void router_element_get_test() {
        String url = base + "/routers/8604a0de-7f6b-409a-a47c-a1cc7bc77b2f";
        ITNeutronE2E.test_fetch(url, true, "Router Element Get Test");
    }

    public void router_element_get_with_query_test() {
        String url = base + "/routers/8604a0de-7f6b-409a-a47c-a1cc7bc77b2f"
                + "?fields=id&fields=admin_state_up&fields=name"
                + "&fields=tenant_id&fields=external_gateway_info" + "&fields=limit&fields=marker&fields=page_reverse";
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
        NeutronRouterTests routerTester = new NeutronRouterTests(base);
        String createJsonString = routerTester.singleton_router_create_test();
        routerTester.singleton_router_get_with_one_query_item_test(createJsonString);
        routerTester.router_add_interface_test();
        routerTester.update_router_test();
        routerTester.create_router(); // needed for following tests
        routerTester.router_add_interface();
        routerTester.router_element_get_test();
        routerTester.router_element_get_with_query_test();
        routerTester.router_collection_get_test();
        routerTester.router_remove_interface_test();
        routerTester.router_delete_test();
        routerTester.router_element_negative_get_test();
    }
}
