/*
 * Copyright (C) 2016 Intel, Corp.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.e2etest;

public class NeutronSFCPortPairTests {
    String base;

    public NeutronSFCPortPairTests(String base) {
        this.base = base;
    }

    public void sfc_portpair_collection_get_test() {
        String url = base + "/sfc/portpairs";
        HttpUtils.test_fetch(url, "Portpair collection GET failed");
    }

    public String singleton_sfc_portpair_create_test() {
        String url = base + "/sfc/portpairs";
        String content = " { \"portpair\": { "
            + "\"name\": \"portpair1\", "
            + "\"ingress\": \"5e8e5957-649f-477b-9e5b-f1f75b21c03c\", "
            + "\"egress\": \"6e8e5957-649f-477b-9e5b-f1f75b21c03c\", "
            + "\"service_function_parameters\": [ "
            + "{ "
            + "\"correlation\": \"value\" "
            + "} "
            + "], "
            + "\"tenant_id\": \"4969c491a3c74ee4af974e6d800c62de\", "
            + "\"id\": \"4e8e5957-649f-477b-9e5b-f1f75b21c03c\" } }";
        HttpUtils.test_create(url, content, "SFC Portpair Singleton POST failed");
        return content;
    }

    public void singleton_sfc_portpair_get_with_query_item_test(String createJsonString) {
        String url = base + "/sfc/portpairs";
        HttpUtils.test_fetch_with_one_query_item(url, createJsonString, "portpairs");
    }

    public void sfc_portpair_element_get_test() {
        String url = base + "/sfc/portpairs/4e8e5957-649f-477b-9e5b-f1f75b21c03c";
        HttpUtils.test_fetch(url, "SFC Portpair Element GET failed");
    }

    public void sfc_portpair_element_get_with_query_test() {
        String url = base + "/sfc/portpairs/4e8e5957-649f-477b-9e5b-f1f75b21c03c"
            + "?fields=name&fields=ingress&fields=egress"
            + "&fields=limits&fields=marker&fields=page_reverse";
        HttpUtils.test_fetch(url, "SFC Portpair Element GET with query failed");
    }

    public void sfc_portpair_modify_test() {
        String url = base + "/sfc/portpairs/4e8e5957-649f-477b-9e5b-f1f75b21c03c";
        String content = " { \"portpair\": { "
            + "\"name\": \"portpair2\", "
            + "\"ingress\": \"5e8e5957-649f-477b-9e5b-f1f75b21c03c\", "
            + "\"egress\": \"6e8e5957-649f-477b-9e5b-f1f75b21c03c\", "
            + "\"service_function_parameters\": [ "
            + "{ "
            + "\"correlation\": \"value\" "
            + "} "
            + "], "
            + "\"tenant_id\": \"4969c491a3c74ee4af974e6d800c62de\", "
            + "\"id\": \"4e8e5957-649f-477b-9e5b-f1f75b21c03c\" }}";
        HttpUtils.test_modify(url, content, "SFC Portpair Singleton PUT failed");
    }

    public void sfc_portpair_delete_test() {
        String url = base + "/sfc/portpairs/4e8e5957-649f-477b-9e5b-f1f75b21c03c";
        HttpUtils.test_delete(url, "SFC Portpair DELETE failed");
    }

    public void sfc_portpair_element_negative_get_test() {
        String url = base + "/sfc/portpairs/4e8e5957-649f-477b-9e5b-f1f75b21c03c";
        HttpUtils.test_fetch(url, false, "SFC Portpair Element Negative GET Failed");
    }

    public static void runTests(String base) {
        NeutronSFCPortPairTests sfcPortpairTester = new NeutronSFCPortPairTests(base);
        String createJsonString = sfcPortpairTester.singleton_sfc_portpair_create_test();
        sfcPortpairTester.singleton_sfc_portpair_get_with_query_item_test(createJsonString);
        sfcPortpairTester.sfc_portpair_element_get_test();
        sfcPortpairTester.sfc_portpair_element_get_with_query_test();
        sfcPortpairTester.sfc_portpair_collection_get_test();
        sfcPortpairTester.sfc_portpair_modify_test();
        sfcPortpairTester.sfc_portpair_delete_test();
        sfcPortpairTester.sfc_portpair_element_negative_get_test();
    }
}
