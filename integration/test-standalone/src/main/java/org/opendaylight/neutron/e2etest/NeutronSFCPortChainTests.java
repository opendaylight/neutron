/*
 * Copyright (C) 2016 Intel, Corp.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.e2etest;

public class NeutronSFCPortChainTests {
    String base;

    public NeutronSFCPortChainTests(String base) {
        this.base = base;
    }

    public void sfc_portchain_collection_get_test() {
        String url = base + "/sfc/portchains";
        HttpUtils.test_fetch(url, "Portchains group collection GET failed");
    }

    public String singleton_sfc_portchain_create_test() {
        String url = base + "/sfc/portchains";
        String content = "{ \"portchain\" : { \"name\": \"portchain1\", "
            + "\"port_pair_groups\": [ "
            + "\"4512d643-24fc-4fae-af4b-321c5e2eb3d1\", "
            + "\"4a634d49-76dc-4fae-af4b-321c5e23d651\" "
            + "], "
            + "\"flow_classifiers\": [ "
            + "\"4a334cd4-fe9c-4fae-af4b-321c5e2eb051\", "
            + "\"105a4b0a-73d6-11e5-b392-2c27d72acb4c\" "
            + "], "
            + "\"chain_parameters\": [ "
            + "{ "
            + "\"correlation\": \"value\" "
            + "} "
            + "], "
            + "\"tenant_id\": \"4969c491a3c74ee4af974e6d800c62de\", "
            + "\"id\": \"4e8e5957-649f-477b-9e5b-f1f75b21c03c\" } }";
        HttpUtils.test_create(url, content, "SFC Portchain group singleton POST failed");
        return content;
    }

    public void singleton_sfc_portchain_get_with_query_item(String createJsonString) {
        String url = base + "/sfc/portchains";
        HttpUtils.test_fetch_with_one_query_item(url, createJsonString, "portchains");
    }

    public void sfc_portchain_element_get_test() {
        String url = base + "/sfc/portchains";
        HttpUtils.test_fetch(url, "SFC portchains elements GET failed");
    }

    public void sfc_portchain_element_get_test_with_query_test() {
        String url = base + "/sfc/portchains/4e8e5957-649f-477b-9e5b-f1f75b21c03c"
            + "?fields=name&fields=port_pair_groups&fields=flow_classifiers"
            + "&fields=chain_parameters&fields=tenant_id&fields=id"
            + "&fields=limits&fields=marker&fields=page_reverse";
        HttpUtils.test_fetch(url, "SFC portchain group element GET with query failed");
    }

    public void sfc_portchain_modify_test() {
        String url = base + "/sfc/portchains/4e8e5957-649f-477b-9e5b-f1f75b21c03c";
        String content = "{ \"portchain\" : { \"name\": \"portchain1\", "
            + "\"port_pair_groups\": [ "
            + "\"4512d643-24fc-4fae-af4b-321c5e2eb3d1\", "
            + "\"4a634d49-76dc-4fae-af4b-321c5e23d651\","
            + "\"4a634d49-76dc-4fae-af4b-321c5e23d652\" "
            + "], "
            + "\"flow_classifiers\": [ "
            + "\"4a334cd4-fe9c-4fae-af4b-321c5e2eb051\", "
            + "\"105a4b0a-73d6-11e5-b392-2c27d72acb4c\" "
            + "], "
            + "\"chain_parameters\": [ "
            + "{ "
            + "\"correlation\": \"value\" "
            + "} "
            + "], "
            + "\"tenant_id\": \"4969c491a3c74ee4af974e6d800c62de\", "
            + "\"id\": \"4e8e5957-649f-477b-9e5b-f1f75b21c03c\" } }";
        HttpUtils.test_modify(url, content, "SFC portchain group singleton PUT failed");
    }

    public void sfc_portchain_delete_test() {
        String url = base + "/sfc/portchains/4e8e5957-649f-477b-9e5b-f1f75b21c03c";
        HttpUtils.test_delete(url, "SFC portchain DELETE failed");
    }

    public void sfc_portchain_element_negative_get_test() {
        String url = base + "/sfc/portchains/4e8e5957-649f-477b-9e5b-f1f75b21c03c";
        HttpUtils.test_fetch(url, false, "SFC portchain Element negative GET failed");
    }

    public static void runTests(String base) {
        NeutronSFCPortChainTests sfcPortchainTester = new NeutronSFCPortChainTests(base);
        String createJsonString = sfcPortchainTester.singleton_sfc_portchain_create_test();
        sfcPortchainTester.singleton_sfc_portchain_get_with_query_item(createJsonString);
        sfcPortchainTester.sfc_portchain_element_get_test();
        sfcPortchainTester.sfc_portchain_element_get_test_with_query_test();
        sfcPortchainTester.sfc_portchain_collection_get_test();
        sfcPortchainTester.sfc_portchain_modify_test();
        sfcPortchainTester.sfc_portchain_delete_test();
        sfcPortchainTester.sfc_portchain_element_negative_get_test();
    }
}
