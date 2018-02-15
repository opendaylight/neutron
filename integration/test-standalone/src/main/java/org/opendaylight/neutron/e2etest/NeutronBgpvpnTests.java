/*
 * Copyright (c) 2015 Ericsson India Global Services Pvt Ltd. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.e2etest;

public class NeutronBgpvpnTests {
    String base;

    public NeutronBgpvpnTests(String base) {
        this.base = base;
    }

    public void bgpvpn_collection_get_test() {
        String url = base + "/bgpvpns";
        HttpUtils.test_fetch(url, "BGPVPN collection GET failed");
    }

    //TODO handle SB check
    public String singleton_bgpvpn_create_test() {
        String url = base + "/bgpvpns";
        String content = "{ \"bgpvpn\": {" + " \"status\": \"ACTIVE\", \"type\": \"l3\", "
                + " \"name\": \"vpn1\", \"admin_state_up\": true, "
                + " \"tenant_id\": \"9bacb3c5d39d41a79512987f338cf177\", " + " \"route_targets\": \"64512:1\", "
                + " \"networks\": \"3b80198d-4f7b-4f77-9ef5-774d54e17126\", "
                + " \"auto_aggregate\": true, \"id\": \"4e8e5957-649f-477b-9e5b-f1f75b21c03c\" " + " } }";
        HttpUtils.test_create(url, content, "Singleton Bgpvpn Post Failed NB");
        return content;
    }

    public void singleton_bgpvpn_get_with_one_query_item_test(String createJsonString) {
        String url = base + "/bgpvpns";
        HttpUtils.test_fetch_with_one_query_item(url, createJsonString, "bgpvpns");
    }

    //TODO handle SB check
    public void bulk_bgpvpn_create_test() {
        String url = base + "/bgpvpns";
        String content = "{ \"bgpvpns\": [ { " + "\"status\": \"ACTIVE\", " + "\"name\": \"sample_bgpvpn1\", "
                + "\"admin_state_up\": true, " + "\"tenant_id\": \"4fd44f30292945e481c7b8a0c8908869\", "
                + "\"id\": \"bc1a76cb-8767-4c3a-bb95-018b822f2130\", " + "\"route_targets\": \"64512:1\", "
                + "\"auto_aggregate\": true, " + "\"type\": \"l3\" }, { " + "\"status\": \"ACTIVE\", "
                + "\"name\": \"sample_bgpvpn2\", " + "\"admin_state_up\": true, "
                + "\"tenant_id\": \"4fd44f30292945e481c7b8a0c8908869\", "
                + "\"id\": \"af374017-c9ae-4a1d-b799-ab73111476e2\", " + "\"route_targets\": \"64512:2\", "
                + "\"auto_aggregate\": false, " + "\"type\": \"l3\" } ] } ";
        HttpUtils.test_create(url, content, "Bulk Bgpvpn Post Failed");
    }

    //TODO handle SB check
    public void bgpvpn_update_test() {
        String url = base + "/bgpvpns/bc1a76cb-8767-4c3a-bb95-018b822f2130";
        String content = " { \"bgpvpn\": { " + "\"status\": \"ACTIVE\", " + "\"name\": \"sample_bgpvpn_updated\", "
                + "\"admin_state_up\": true, " + "\"tenant_id\": \"4fd44f30292945e481c7b8a0c8908869\", "
                + "\"auto_aggregate\": true, " + "\"type\": \"l3\" } } ";
        HttpUtils.test_modify(url, content, "Bgpvpn Put Failed");
    }

    public void bgpvpn_element_get_test() {
        String url = base + "/bgpvpns/bc1a76cb-8767-4c3a-bb95-018b822f2130";
        HttpUtils.test_fetch(url, true, "Bgpvpn Element Get Failed");
    }

    public void bgpvpn_element_get_test_with_added_query() {
        String url = base + "/bgpvpns/bc1a76cb-8767-4c3a-bb95-018b822f2130"
                + "?fields=id&fields=tenant_id&fields=name&fields=type"
                + "fields=route_targets&fields=status&fields=admin_state_up"
                + "&fields=tenant_id&fields=auto_aggregate";
        HttpUtils.test_fetch(url, true, "Bgpvpn Element Get Failed");
    }

    public void bgpvpn_element_negative_get_test() {
        String url = base + "/bgpvpns/bc1a76cb-8767-4c3a-bb95-018b822f2130";
        HttpUtils.test_fetch(url, false, "Bgpvpn Element Negative Get Failed");
    }

    public void bgpvpn_delete_test() {
        String url = base + "/bgpvpns/bc1a76cb-8767-4c3a-bb95-018b822f2130";
        HttpUtils.test_delete(url, "bgpvpnElement Delete Failed");
    }

    public static void runTests(String base) {
        NeutronBgpvpnTests bgpvpnTester = new NeutronBgpvpnTests(base);
        bgpvpnTester.bgpvpn_collection_get_test();
        String createJsonString = bgpvpnTester.singleton_bgpvpn_create_test();
        bgpvpnTester.singleton_bgpvpn_get_with_one_query_item_test(createJsonString);
        bgpvpnTester.bulk_bgpvpn_create_test();
        bgpvpnTester.bgpvpn_update_test();
        bgpvpnTester.bgpvpn_element_get_test();
        bgpvpnTester.bgpvpn_element_get_test_with_added_query();
        bgpvpnTester.bgpvpn_delete_test();
        bgpvpnTester.bgpvpn_element_negative_get_test();
    }
}
