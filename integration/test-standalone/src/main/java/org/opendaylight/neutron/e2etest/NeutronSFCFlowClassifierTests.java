/*
 * Copyright (C) 2016 Intel, Corp.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.e2etest;

public class NeutronSFCFlowClassifierTests {
    String base;

    public NeutronSFCFlowClassifierTests(String base) {
        this.base = base;
    }

    public void sfc_flowclassifier_collection_get_test() {
        String url = base + "/sfc/flowclassifiers";
        HttpUtils.test_fetch(url, "Flowclassifier group collection GET failed");
    }

    public String singleton_sfc_flowclassifier_create_test() {
        String url = base + "/sfc/flowclassifiers";
        String content = "{ \"flowclassifier\" : { \"name\": \"flowclassifier1\", "
            + "\"ethertype\": \"IPv4\", "
            + "\"protocol\": \"udp\", "
            + "\"source_port_range_min\": 100, "
            + "\"source_port_range_max\": 200, "
            + "\"destination_port_range_min\": 100, "
            + "\"destination_port_range_max\": 200, "
            + "\"source_ip_prefix\": \"10.0.0.0/24\", "
            + "\"destination_ip_prefix\": \"11.0.0.0/24\", "
            + "\"logical_source_port\": \"5e8e5957-649f-477b-9e5b-f1f75b21c03c\", "
            + "\"logical_destination_port\": \"6e8e5957-649f-477b-9e5b-f1f75b21c03c\", "
            + "\"l7_parameters\": [ "
            + "{ "
            + "\"Key\": \"value\" "
            + "} "
            + "], "
            + "\"tenant_id\": \"4969c491a3c74ee4af974e6d800c62de\", "
            + "\"id\": \"4e8e5957-649f-477b-9e5b-f1f75b21c03c\" } }";
        HttpUtils.test_create(url, content, " SFC Flowclassifier group singleton POST failed");
        return content;
    }

    public void singleton_sfc_flowclassifier_get_with_query_item(String createJsonString) {
        String url = base + "/sfc/flowclassifiers";
        HttpUtils.test_fetch_with_one_query_item(url, createJsonString, "flowclassifiers");
    }

    public void sfc_flowclassifier_element_get_test() {
        String url = base + "/sfc/flowclassifiers";
        HttpUtils.test_fetch(url, "SFC flowclassifier elements GET failed");
    }

    public void sfc_flowclassifier_get_test_with_query_test() {
        String url = base + "/sfc/flowclassifiers/4e8e5957-649f-477b-9e5b-f1f75b21c03c"
            + "?fields=name&fields=ethertype&fields=protocol&fields=source_port_range_min"
            + "&fields=source_port_range_max&fields=destination_port_range_min&fields=destination_port_range_max"
            + "&fields=source_ip_prefix&fields=destination_ip_prefix&fields=logical_source_port"
            + "&fields=logical_destination_port&fields=l7_parameters&fields=tenant_id&fields=id";
        HttpUtils.test_fetch(url, "SFC flowclassifier group element GET with query failed");
    }

    public void sfc_flowclassifier_modify_test() {
        String url = base + "/sfc/flowclassifiers/4e8e5957-649f-477b-9e5b-f1f75b21c03c";
        String content = "{ \"flowclassifier\" : { \"name\": \"flowclassifier1\", "
            + "\"ethertype\": \"IPv4\", "
            + "\"protocol\": \"udp\", "
            + "\"source_port_range_min\": 100, "
            + "\"source_port_range_max\": 200, "
            + "\"destination_port_range_min\": 100, "
            + "\"destination_port_range_max\": 200, "
            + "\"source_ip_prefix\": \"10.0.0.0/24\", "
            + "\"destination_ip_prefix\": \"11.0.0.0/24\", "
            + "\"logical_source_port\": \"5e8e5957-649f-477b-9e5b-f1f75b21c03c\", "
            + "\"logical_destination_port\": \"6e8e5957-649f-477b-9e5b-f1f75b21c03c\", "
            + "\"l7_parameters\": [ "
            + "{ "
            + "\"Key\": \"value\" "
            + "} "
            + "], "
            + "\"tenant_id\": \"4969c491a3c74ee4af974e6d800c62de\", "
            + "\"id\": \"4e8e5957-649f-477b-9e5b-f1f75b21c03c\" } }";
        HttpUtils.test_modify(url, content, "SFC flowclassifier group singleton PUT failed");
    }

    public void sfc_flowclassifier_delete_test() {
        String url = base + "/sfc/flowclassifiers/4e8e5957-649f-477b-9e5b-f1f75b21c03c";
        HttpUtils.test_delete(url, "SFC flowclassifier DELETE failed");
    }

    public void sfc_flowclassifier_element_negative_get_test() {
        String url = base + "/sfc/flowclassifiers/4e8e5957-649f-477b-9e5b-f1f75b21c03c";
        HttpUtils.test_fetch(url, false, "SFC flowclassifier Element negative GET failed");
    }

    public void test_bug_6865() {
        String url = base + "/sfc/flowclassifiers";
        String content = "{ \"flowclassifier\" : { \"name\": \"flowclassifier-bug-6865\", "
                + "\"ethertype\": \"IPv4\", "
                + "\"protocol\": \"TCP\", "
                + "\"source_port_range_min\": 100, "
                + "\"source_port_range_max\": 200, "
                + "\"destination_port_range_min\": 100, "
                + "\"destination_port_range_max\": 200, "
                + "\"source_ip_prefix\": \"10.0.0.0/24\", "
                + "\"destination_ip_prefix\": \"11.0.0.0/24\", "
                + "\"logical_source_port\": \"5e8e5957-649f-477b-9e5b-f1f75b21c03c\", "
                + "\"logical_destination_port\": \"6e8e5957-649f-477b-9e5b-f1f75b21c03c\", "
                + "\"l7_parameters\": [ "
                + "{ "
                + "\"Key\": \"value\" "
                + "} "
                + "], "
                + "\"tenant_id\": \"4969c491a3c74ee4af974e6d800c62de\", "
                + "\"id\": \"5e8e5957-649f-477b-9e5b-f1f75b21c03c\" } }";
        HttpUtils.test_create(url, 400, content, "SFC Flowclassifier Bug 6865 regressed");
    }

    public static void runTests(String base) {
        NeutronSFCFlowClassifierTests sfcFlowclassifierTester = new NeutronSFCFlowClassifierTests(base);
        String createJsonString = sfcFlowclassifierTester.singleton_sfc_flowclassifier_create_test();
        sfcFlowclassifierTester.singleton_sfc_flowclassifier_get_with_query_item(createJsonString);
        sfcFlowclassifierTester.sfc_flowclassifier_element_get_test();
        sfcFlowclassifierTester.sfc_flowclassifier_get_test_with_query_test();
        sfcFlowclassifierTester.sfc_flowclassifier_collection_get_test();
        sfcFlowclassifierTester.sfc_flowclassifier_modify_test();
        sfcFlowclassifierTester.sfc_flowclassifier_delete_test();
        sfcFlowclassifierTester.sfc_flowclassifier_element_negative_get_test();
        sfcFlowclassifierTester.test_bug_6865();
    }
}
