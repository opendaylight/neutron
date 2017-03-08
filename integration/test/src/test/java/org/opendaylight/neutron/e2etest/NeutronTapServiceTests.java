/*
 * Copyright (C) 2017 Intel, Corp.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.e2etest;

public class NeutronTapServiceTests {
    String base;

    public NeutronTapServiceTests(String base) {
        this.base = base;
    }

    public void tap_service_collection_get_test() {
        String url = base + "/tap/services";
        ITNeutronE2E.test_fetch(url, "Tap Service collection GET failed");
    }

    public String singleton_tap_service_create_test() {
        String url = base + "/tap/services";
        String content = "{\"tap_service\": {\"id\": \"b6440bbb-35f3-48ab-8eae-69c60aef3546\","
                + "\"tenant_id\": \"aa902936679e4ea29bfe1158e3450a13\","
                + "\"name\": \"tap-service-test\","
                + "\"port_id\": \"311d75c3-4da8-4267-8aed-6dbb19a3dcfe\" }}";
        ITNeutronE2E.test_create(url, content, "Tap Service Singleton POST Failed");
        return content;
    }

    public void singleton_tap_service_get_with_query_item_test(String createJsonString) {
        String url = base + "/tap/services";
        ITNeutronE2E.test_fetch_with_one_query_item(url, createJsonString, "services");
    }

    public void tap_service_modify_test() {
        String url = base + "/tap/services/b6440bbb-35f3-48ab-8eae-69c60aef3546";
        String content = "{\"tap-service\": {\"id\": \"b6440bbb-35f3-48ab-8eae-69c60aef3546\","
                + "\"tenant_id\": \"aa902936679e4ea29bfe1158e3450a13\","
                + "\"name\": \"tap-service-test\","
                + "\"port_id\": \"311d75c3-4da8-4267-8aed-6dbb19a3dcfe\" }}";
        ITNeutronE2E.test_modify(url, content, "Tap Service Singleton PUT failed");
    }

    public void tap_service_element_get_test() {
        String url = base + "/tap/services/b6440bbb-35f3-48ab-8eae-69c60aef3546";
        ITNeutronE2E.test_fetch(url, true, "Tap Service Element Get failed");
    }

    public void tap_service_element_get_with_query_test() {
        String url = base + "/tap/services/b6440bbb-35f3-48ab-8eae-69c60aef3546"
                + "?fields=tenant_id&fields=id&fields=name&fields=port_id"
                + "&fields=limits&fields=marker&fields=page_reverse";
        ITNeutronE2E.test_fetch(url, true, "Tap Service Element Get with Query Failed");
    }

    public void tap_service_delete_test() {
        String url = base + "/tap/services/b6440bbb-35f3-48ab-8eae-69c60aef3546";
        ITNeutronE2E.test_delete(url, "Tap Service Delete Failed");
    }

    public void tap_service_element_negative_get_test() {
        String url = base + "/tap/services/b6440bbb-35f3-48ab-8eae-69c60aef3546";
        ITNeutronE2E.test_fetch(url, false, "Tap Service Element Negative Get Failed");
    }

    public static void runTests(String base) {
        NeutronTapServiceTests tapServiceTester = new NeutronTapServiceTests(base);
        String createJsonString = tapServiceTester.singleton_tap_service_create_test();
        tapServiceTester.singleton_tap_service_get_with_query_item_test(createJsonString);
        tapServiceTester.tap_service_element_get_test();
        tapServiceTester.tap_service_element_get_with_query_test();
        tapServiceTester.tap_service_collection_get_test();
        tapServiceTester.tap_service_modify_test();
        tapServiceTester.tap_service_delete_test();
        tapServiceTester.tap_service_element_negative_get_test();
    }
}
