/*
 * Copyright (C) 2016 Intel, Corp.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.e2etest;

public class NeutronSFCPortPairGroupTests {
    String base;

    public NeutronSFCPortPairGroupTests(String base) {
        this.base = base;
    }

    public void sfc_portpairgroup_collection_get_test() {
        String url = base + "/sfc/portpairgroups";
        HttpUtils.test_fetch(url, "Portpair group collection GET failed");
    }

    public String singleton_sfc_portpairgroup_create_test() {
        String url = base + "/sfc/portpairgroups";
        String content = "{ \"portpairgroup\": { \"name\": \"portpair1\","
            + "\"port_pairs\":[],"
            + "\"tenant_id\": \"4969c491a3c74ee4af974e6d800c62de\","
            + "\"id\": \"4e8e5957-649f-477b-9e5b-f1f75b21c03c\" } }";
        HttpUtils.test_create(url, content, "SFC Portpair group singleton POST failed");
        return content;
    }

    public void singleton_sfc_portpairgroup_get_with_query_item(String createJsonString) {
        String url = base + "/sfc/portpairgroups";
        HttpUtils.test_fetch_with_one_query_item(url, createJsonString, "portpairgroups");
    }

    public void sfc_portpairgroup_element_get_test() {
        String url = base + "/sfc/portpairgroups";
        HttpUtils.test_fetch(url, "SFC portpairgroup Element GET failed");
    }

    public void sfc_portpairgroup_element_get_with_query_test() {
        String url = base + "/sfc/portpairgroups/4e8e5957-649f-477b-9e5b-f1f75b21c03c"
            + "?fields=name&fields=port_pairs&fields=tenant_id&fields=id"
            + "&fields=limits&fields=marker&fields=page_reverse";
        HttpUtils.test_fetch(url, "SFC portpair group Element GET with query failed");
    }

    public void sfc_portpairgroup_modify_test() {
        String url = base + "/sfc/portpairgroups/4e8e5957-649f-477b-9e5b-f1f75b21c03c";
        String content = "{ \"portpairgroup\": { \"name\": \"portpair1\", "
            + "\"port_pairs\": [\"d11e9190-73d4-11e5-b392-2c27d72acb4c\", \"d11e9190-73d4-11e5-b392-2c27d72acb4d\"], "
            + "\"tenant_id\": \"4969c491a3c74ee4af974e6d800c62de\", "
            + "\"id\": \"4e8e5957-649f-477b-9e5b-f1f75b21c03c\" } }";
        HttpUtils.test_modify(url, content, "SFC portpair group singleton PUT failed");
    }

    public void sfc_portpairgroup_delete_test() {
        String url = base + "/sfc/portpairgroups/4e8e5957-649f-477b-9e5b-f1f75b21c03c";
        HttpUtils.test_delete(url, "SFC portpairgroup DELETE failed");
    }

    public void sfc_portpairgroup_element_negative_get_test() {
        String url = base + "/sfc/portpairgroups/4e8e5957-649f-477b-9e5b-f1f75b21c03c";
        HttpUtils.test_fetch(url, false, "SFC Portpair Element negative GET failed");
    }

    public static void runTests(String base) {
        NeutronSFCPortPairGroupTests sfcPortpairgroupTester = new NeutronSFCPortPairGroupTests(base);
        String createJsonString = sfcPortpairgroupTester.singleton_sfc_portpairgroup_create_test();
        sfcPortpairgroupTester.singleton_sfc_portpairgroup_get_with_query_item(createJsonString);
        sfcPortpairgroupTester.sfc_portpairgroup_element_get_test();
        sfcPortpairgroupTester.sfc_portpairgroup_collection_get_test();
        sfcPortpairgroupTester.sfc_portpairgroup_element_get_with_query_test();
        sfcPortpairgroupTester.sfc_portpairgroup_modify_test();
        sfcPortpairgroupTester.sfc_portpairgroup_delete_test();
        sfcPortpairgroupTester.sfc_portpairgroup_element_negative_get_test();
    }
}
