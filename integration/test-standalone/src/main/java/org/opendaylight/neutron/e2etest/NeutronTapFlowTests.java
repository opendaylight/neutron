/*
 * Copyright (C) 2017 Intel, Corp.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.e2etest;

public class NeutronTapFlowTests {
    String base;

    public NeutronTapFlowTests(String base) {
        this.base = base;
    }

    public void tap_flow_collection_get_test() {
        String url = base + "/tap/flows";
        HttpUtils.test_fetch(url, "Tap Flow collection GET failed");
    }

    public String singleton_tap_flow_create_test() {
        String url = base + "/tap/flows/b6440bbb-35f3-48ab-8eae-69c60aef3546/flows";
        String content = "{\"tap_flow\": {\"id\": \"f6220bbb-35f3-48ab-8eae-69c60aef3546\","
                + "\"tenant_id\": \"aa902936679e4ea29bfe1158e3450a13\","
                + "\"name\": \"tap-flow-test\","
                + "\"tap_service_id\": \"b6440bbb-35f3-48ab-8eae-69c60aef3546\","
                + "\"source_port\": \"411d75c3-4da8-4267-8aed-6dbb19a3dcfe\","
                + "\"direction\": \"BOTH\" }}";
        HttpUtils.test_create(url, content, "Tap Flow Singleton POST Failed");
        return content;
    }

    public void singleton_tap_flow_get_with_query_item_test(String createJsonString) {
        String url = base + "/tap/flows/b6440bbb-35f3-48ab-8eae-69c60aef3546";
        HttpUtils.test_fetch_with_one_query_item(url, createJsonString, "flows");
    }

    public void tap_flow_modify_test() {
        String url = base + "/tap/flows/b6440bbb-35f3-48ab-8eae-69c60aef3546"
                          + "/flows/f6220bbb-35f3-48ab-8eae-69c60aef3546";
        String content = "{\"tap-flow\": {\"id\": \"f6220bbb-35f3-48ab-8eae-69c60aef3546\","
                + "\"tenant_id\": \"aa902936679e4ea29bfe1158e3450a13\","
                + "\"name\": \"tap-flow-test2\","
                + "\"tap_service_id\": \"b6440bbb-35f3-48ab-8eae-69c60aef3546\","
                + "\"source_port\": \"611d75c3-4da8-4267-8aed-6dbb19a3dcfe\","
                + "\"direction\": \"IN\" }}";
        HttpUtils.test_modify(url, content, "Tap Flow Singleton PUT failed");
    }

    public void tap_flow_element_get_test() {
        String url = base + "/tap/flows/b6440bbb-35f3-48ab-8eae-69c60aef3546"
                          + "/flows/f6220bbb-35f3-48ab-8eae-69c60aef3546";
        HttpUtils.test_fetch(url, true, "Tap Flow Element Get failed");
    }

    public void tap_flow_element_get_with_query_test() {
        String url = base + "/tap/flows/b6440bbb-35f3-48ab-8eae-69c60aef3546"
                + "/flows/f6220bbb-35f3-48ab-8eae-69c60aef3546"
                + "?fields=tenant_id&fields=id&fields=name&fields=tap_service_id"
                + "?fields=source_port&fields=direction"
                + "&fields=limits&fields=marker&fields=page_reverse";
        HttpUtils.test_fetch(url, true, "Tap Flow Element Get with Query Failed");
    }

    public void tap_flow_delete_test() {
        String url = base + "/tap/flows/b6440bbb-35f3-48ab-8eae-69c60aef3546"
                          + "/flows/f6220bbb-35f3-48ab-8eae-69c60aef3546";
        HttpUtils.test_delete(url, "Tap Flow Delete Failed");
    }

    public void tap_flow_element_negative_get_test() {
        String url = base + "/tap/flows/b6440bbb-35f3-48ab-8eae-69c60aef3546"
                          + "/flows/f6220bbb-35f3-48ab-8eae-69c60aef3546";
        HttpUtils.test_fetch(url, false, "Tap Flow Element Negative Get Failed");
    }

    public static void runTests(String base) {
        NeutronTapFlowTests tapFlowTester = new NeutronTapFlowTests(base);
        String createJsonString = tapFlowTester.singleton_tap_flow_create_test();
        tapFlowTester.singleton_tap_flow_get_with_query_item_test(createJsonString);
        tapFlowTester.tap_flow_element_get_test();
        tapFlowTester.tap_flow_element_get_with_query_test();
        tapFlowTester.tap_flow_collection_get_test();
        tapFlowTester.tap_flow_modify_test();
        tapFlowTester.tap_flow_delete_test();
        tapFlowTester.tap_flow_element_negative_get_test();
    }
}
