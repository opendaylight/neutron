/*
 * Copyright (C) 2015 IBM, Inc.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.e2etest;

public class NeutronVpnServicesTests {
    String base;

    public NeutronVpnServicesTests(String base) {
        this.base = base;
    }

    public void vpnService_collection_get_test() {
        String url = base + "/vpn/vpnservices";
        ITNeutronE2E.test_fetch(url, "VPN Services GET failed");
    }

    public String singleton_vpnservice_create_test() {
        String url = base + "/vpn/vpnservices";
        String content = " { \"vpnservice\": {" + "\"router_id\": \"ec8619be-0ba8-4955-8835-3b49ddb76f89\","
                + "\"name\": \"myservice\"," + "\"admin_state_up\": true,"
                + "\"subnet_id\": \"f4fb4528-ed93-467c-a57b-11c7ea9f963e\","
                + "\"tenant_id\": \"ccb81365fe36411a9011e90491fe1330\","
                + "\"id\": \"9faaf49f-dd89-4e39-a8c6-101839aa49bc\"," + "\"description\": \"\" } }";
        ITNeutronE2E.test_create(url, content, "VPN Services POST failed");
        return content;
    }

    public void singleton_vpnservice_get_with_one_query_item_test(String createJsonString) {
        String url = base + "/vpn/vpnservices";
        ITNeutronE2E.test_fetch_with_one_query_item(url, createJsonString, "vpnservices");
    }

    public void vpnservice_update_test() {
        String url = base + "/vpn/vpnservices/9faaf49f-dd89-4e39-a8c6-101839aa49bc";
        String content = " { \"vpnservice\": {" + "\"router_id\": \"881b7b30-4efb-407e-a162-5630a7af3595\","
                + "\"name\": \"myvpn\"," + "\"admin_state_up\": true,"
                + "\"subnet_id\": \"25f8a35c-82d5-4f55-a45b-6965936b33f6\","
                + "\"tenant_id\": \"26de9cd6cae94c8cb9f79d660d628e1f\","
                + "\"id\": \"41bfef97-af4e-4f6b-a5d3-4678859d2485\"," + "\"description\": \"Updated description\" } }";
        ITNeutronE2E.test_modify(url, content, "VPN Services PUT failed");
    }

    public void vpnservice_element_get_test() {
        String url = base + "/vpn/vpnservices/9faaf49f-dd89-4e39-a8c6-101839aa49bc";
        ITNeutronE2E.test_fetch(url, true, "VPN Services Element GET failed");
    }

    public void vpnservice_element_get_with_query_test() {
        String url = base + "/vpn/vpnservices/9faaf49f-dd89-4e39-a8c6-101839aa49bc"
                + "?fields=id&fields=tenant_id&fields=name&fields=admin_state_up"
                + "&fields=router_id&fields=subnet_id"
                + "&fields=limit&fields=marker&fields=page_reverse";
        ITNeutronE2E.test_fetch(url, true, "VPN Services Element GET failed");
    }

    public void vpnservice_delete_test() {
        String url = base + "/vpn/vpnservices/9faaf49f-dd89-4e39-a8c6-101839aa49bc";
        ITNeutronE2E.test_delete(url, "VPN Services DELETE failed");
    }

    public void vpnservice_element_negative_get_test() {
        String url = base + "/vpn/vpnservices/9faaf49f-dd89-4e39-a8c6-101839aa49bc";
        ITNeutronE2E.test_fetch(url, false, "VPN Services Element Negative GET failed");
    }

    public static void runTests(String base) {
        NeutronVpnServicesTests vpnServiceTester = new NeutronVpnServicesTests(base);
        String createJsonString = vpnServiceTester.singleton_vpnservice_create_test();
        vpnServiceTester.singleton_vpnservice_get_with_one_query_item_test(createJsonString);
        vpnServiceTester.vpnservice_update_test();
        vpnServiceTester.vpnservice_element_get_test();
        vpnServiceTester.vpnservice_element_get_with_query_test();
        vpnServiceTester.vpnService_collection_get_test();
        vpnServiceTester.vpnservice_delete_test();
        vpnServiceTester.vpnservice_element_negative_get_test();
    }
}
