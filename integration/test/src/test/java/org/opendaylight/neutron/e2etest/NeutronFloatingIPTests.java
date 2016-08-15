/*
 * Copyright (C) 2015 IBM, Inc.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.e2etest;

public class NeutronFloatingIPTests {
    String base;

    public NeutronFloatingIPTests(String base) {
        this.base = base;
    }

    public void floatingIP_collection_get_test() {
        String url = base + "/floatingips";
        ITNeutronE2E.test_fetch(url, "FloatingIP Collection GET failed");
    }

    public String singleton_floatingIP_create_test() {
        String url = base + "/floatingips";
        String content = "{ \"floatingip\": { " + "\"fixed_ip_address\": \"10.0.0.3\","
                + "\"floating_ip_address\": \"172.24.4.228\","
                + "\"floating_network_id\": \"376da547-b977-4cfe-9cba-275c80debf57\","
                + "\"id\": \"2f245a7b-796b-4f26-9cf9-9e82d248fda7\","
                + "\"port_id\": \"ce705c24-c1ef-408a-bda3-7bbd946164ab\","
                + "\"router_id\": \"d23abc8d-2991-4a55-ba98-2aaea84cc72f\"," + "\"status\": \"ACTIVE\","
                + "\"tenant_id\": \"4969c491a3c74ee4af974e6d800c62de\" } }";
        ITNeutronE2E.test_create(url, content, "Singleton Floating IP Post Failed NB");
        return content;
    }

    public void singleton_floatingIP_get_with_one_query_item_test(String createJsonString) {
        String url = base + "/floatingips";
        ITNeutronE2E.test_fetch_with_one_query_item(url, createJsonString, "floatingips");
    }

    public void update_floatingIP_test() {
        String url = base + "/floatingips/2f245a7b-796b-4f26-9cf9-9e82d248fda7";
        String content1 = "{ \"floatingip\": {" + "\"floating_network_id\": \"376da547-b977-4cfe-9cba-275c80debf57\","
                + "\"router_id\": \"d23abc8d-2991-4a55-ba98-2aaea84cc72f\"," + "\"fixed_ip_address\": \"10.0.0.4\","
                + "\"floating_ip_address\": \"172.24.4.228\"," + "\"tenant_id\": \"4969c491a3c74ee4af974e6d800c62de\","
                + "\"status\": \"ACTIVE\"," + "\"port_id\": \"fc861431-0e6c-4842-a0ed-e2363f9bc3a8\","
                + "\"id\": \"2f245a7b-796b-4f26-9cf9-9e82d248fda7\" } }";
        ITNeutronE2E.test_modify(url, content1, "Singleton Floating IP Put 1 Failed NB");

        String content2 = " { \"floatingip\": {" + "\"floating_network_id\": \"376da547-b977-4cfe-9cba-275c80debf57\","
                + "\"router_id\": \"d23abc8d-2991-4a55-ba98-2aaea84cc72f\"," + "\"fixed_ip_address\": null,"
                + "\"floating_ip_address\": \"172.24.4.228\"," + "\"tenant_id\": \"4969c491a3c74ee4af974e6d800c62de\","
                + "\"status\": \"ACTIVE\"," + "\"port_id\": null,"
                + "\"id\": \"2f245a7b-796b-4f26-9cf9-9e82d248fda7\" } }";
        ITNeutronE2E.test_modify(url, content2, "Singleton Floating IP Put 2 Failed NB");
    }

    public void floatingIP_element_get_test() {
        String url = base + "/floatingips/2f245a7b-796b-4f26-9cf9-9e82d248fda7";
        ITNeutronE2E.test_fetch(url, true, "Floating IP Element Get Test");
    }

    public void floatingIP_element_get_with_query_test() {
        String url = base + "/floatingips/2f245a7b-796b-4f26-9cf9-9e82d248fda7"
                + "?fields=id&fields=floating_network_id&fields=port_id&fields=status"
                + "&fields=fixed_ip_address&fields=floating_ip_address&fields=tenant_id"
                + "&fields=router_id&fields=limit&fields=marker&fields=page_reverse";
        ITNeutronE2E.test_fetch(url, true, "Floating IP Element Get Query Test");
    }

    public void floatingIP_delete_test() {
        String url = base + "/floatingips/2f245a7b-796b-4f26-9cf9-9e82d248fda7";
        ITNeutronE2E.test_delete(url, "Floating IP Delete Test");
    }

    public void floatingIP_element_negative_get_test() {
        String url = base + "/floatingips/2f245a7b-796b-4f26-9cf9-9e82d248fda7";
        ITNeutronE2E.test_fetch(url, false, "Floating IP Element Negative Get Test");
    }

    public static void runTests(String base) {
        NeutronFloatingIPTests floatingIP_tester = new NeutronFloatingIPTests(base);
        String createJsonString = floatingIP_tester.singleton_floatingIP_create_test();
        floatingIP_tester.singleton_floatingIP_get_with_one_query_item_test(createJsonString);
        floatingIP_tester.update_floatingIP_test();
        floatingIP_tester.floatingIP_element_get_test();
        floatingIP_tester.floatingIP_element_get_with_query_test();
        floatingIP_tester.floatingIP_collection_get_test();
        floatingIP_tester.floatingIP_delete_test();
        floatingIP_tester.floatingIP_element_negative_get_test();
    }
}
