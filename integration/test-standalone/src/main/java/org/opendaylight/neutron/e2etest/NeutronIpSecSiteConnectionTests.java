/*
 * Copyright (C) 2015 IBM, Inc.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.e2etest;

public class NeutronIpSecSiteConnectionTests {
    String base;

    public NeutronIpSecSiteConnectionTests(String base) {
        this.base = base;
    }

    public void ipsecSiteConnection_collection_get_test() {
        String url = base + "/vpn/ipsecsiteconnections";
        HttpUtils.test_fetch(url, "IPSEC Site Connection Collection GET failed");
    }

    public String singleton_ipsecSiteConnection_create_test() {
        String url = base + "/vpn/ipsecsiteconnections";
        String content = " { \"ipsec_site_connection\": {" + "\"status\": \"PENDING_CREATE\"," + "\"psk\": \"secret\","
                + "\"initiator\": \"bi-directional\"," + "\"name\": \"vpnconnection1\"," + "\"admin_state_up\": true,"
                + "\"tenant_id\": \"b6887d0b45b54a249b2ce3dee01caa47\"," + "\"description\": \"\","
                + "\"auth_mode\": \"psk\"," + "\"peer_cidrs\": [ \"10.2.0.0/24\" ]," + "\"mtu\": 1500,"
                + "\"ikepolicy_id\": \"d3f373dc-0708-4224-b6f8-676adf27dab8\","
                + "\"dpd\": { \"action\": \"disabled\", \"interval\": 60," + "\"timeout\": 240 },"
                + "\"route_mode\": \"static\"," + "\"vpnservice_id\": \"7b347d20-6fa3-4e22-b744-c49ee235ae4f\","
                + "\"peer_address\": \"172.24.4.233\"," + "\"peer_id\": \"172.24.4.233\","
                + "\"id\": \"af44dfd7-cf91-4451-be57-cd4fdd96b5dc\","
                + "\"ipsecpolicy_id\": \"22b8abdc-e822-45b3-90dd-f2c8512acfa5\" } }";
        HttpUtils.test_create(url, content, "IPSEC Site Connection POST failed");
        return content;
    }

    public void singleton_ipsecSiteConnection_get_with_one_query_item_test(String createJsonString) {
        String url = base + "/vpn/ipsecsiteconnections";
        HttpUtils.test_fetch_with_one_query_item(url, createJsonString, "ipsec_site_connections");
    }

    public void ipsecSiteConnection_update_test() {
        String url = base + "/vpn/ipsecsiteconnections/af44dfd7-cf91-4451-be57-cd4fdd96b5dc";
        String content = " { \"ipsec_site_connection\": {" + "\"status\": \"DOWN\"," + "\"psk\": \"secret\","
                + "\"initiator\": \"bi-directional\"," + "\"name\": \"vpnconnection1\"," + "\"admin_state_up\": true,"
                + "\"tenant_id\": \"26de9cd6cae94c8cb9f79d660d628e1f\"," + "\"description\": \"\","
                + "\"auth_mode\": \"psk\"," + "\"peer_cidrs\": [ \"10.2.0.0/24\" ]," + "\"mtu\": 2000,"
                + "\"ikepolicy_id\": \"771f081c-5ec8-4f9a-b041-015dfb7fbbe2\","
                + "\"dpd\": { \"action\": \"hold\", \"interval\": 30, \"timeout\": 120 },"
                + "\"route_mode\": \"static\"," + "\"vpnservice_id\": \"41bfef97-af4e-4f6b-a5d3-4678859d2485\","
                + "\"peer_address\": \"172.24.4.233\"," + "\"peer_id\": \"172.24.4.233\","
                + "\"id\": \"af44dfd7-cf91-4451-be57-cd4fdd96b5dc\","
                + "\"ipsecpolicy_id\": \"9958d4fe-3719-4e8c-84e7-9893895b76b4\" } }";
        HttpUtils.test_modify(url, content, "IPSEC Site Connection PUT failed");
    }

    public void ipsecSiteConnection_element_get_test() {
        String url = base + "/vpn/ipsecsiteconnections/af44dfd7-cf91-4451-be57-cd4fdd96b5dc";
        HttpUtils.test_fetch(url, true, "IPSEC Site Connection Element GET failed");
    }

    public void ipsecSiteConnection_element_get_with_query_test() {
        String url = base + "/vpn/ipsecsiteconnections/af44dfd7-cf91-4451-be57-cd4fdd96b5dc"
                + "?fields=id&fields=tenant_id&fields=name&fields=description&fields=peer_address"
                + "&fields=peer_id&fields=route_mode&fields=auth_mode&fields=initiator"
                + "&fields=mtu&fields=psk&fields=admin_state_up&fields=status&fields=ikepolicy_id"
                + "&fields=ipsecpolicy_id&fields=vpnservice_id";
        HttpUtils.test_fetch(url, true, "IPSEC Site Connection Element GET With Query failed");
    }

    public void ipsecSiteConnection_delete_test() {
        String url = base + "/vpn/ipsecsiteconnections/af44dfd7-cf91-4451-be57-cd4fdd96b5dc";
        HttpUtils.test_delete(url, "IPSEC Site Connection DELETE failed");
    }

    public void ipsecSiteConnection_element_negative_get_test() {
        String url = base + "/vpn/ipsecsiteconnections/af44dfd7-cf91-4451-be57-cd4fdd96b5dc";
        HttpUtils.test_fetch(url, false, "IPSEC Site Connection Element Negative GET failed");
    }

    public static void runTests(String base) {
        NeutronIpSecSiteConnectionTests ipsecSiteConnectionTester = new NeutronIpSecSiteConnectionTests(base);
        String createJsonString = ipsecSiteConnectionTester.singleton_ipsecSiteConnection_create_test();
        ipsecSiteConnectionTester.singleton_ipsecSiteConnection_get_with_one_query_item_test(createJsonString);
        ipsecSiteConnectionTester.ipsecSiteConnection_update_test();
        ipsecSiteConnectionTester.ipsecSiteConnection_element_get_test();
        ipsecSiteConnectionTester.ipsecSiteConnection_element_get_with_query_test();
        ipsecSiteConnectionTester.ipsecSiteConnection_collection_get_test();
        ipsecSiteConnectionTester.ipsecSiteConnection_delete_test();
        ipsecSiteConnectionTester.ipsecSiteConnection_element_negative_get_test();
    }
}
