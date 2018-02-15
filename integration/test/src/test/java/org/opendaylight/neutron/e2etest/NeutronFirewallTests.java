/*
 * Copyright (C) 2015 IBM, Inc.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.e2etest;

public class NeutronFirewallTests {
    String base;

    public NeutronFirewallTests(String base) {
        this.base = base;
    }

    public void fw_collection_get_test() {
        String url = base + "/fw/firewalls";
        HttpUtils.test_fetch(url, "Firewall Collection GET failed");
    }

    public String singleton_fw_create_test() {
        String url = base + "/fw/firewalls";
        String content = " { \"firewall\": { \"admin_state_up\": true," + "\"description\": \"\","
                + "\"firewall_policy_id\": \"c69933c1-b472-44f9-8226-30dc4ffd454c\","
                + "\"id\": \"3b0ef8f4-82c7-44d4-a4fb-6177f9a21977\","
                + "\"name\": \"\", \"status\": \"PENDING_CREATE\","
                + "\"tenant_id\": \"45977fa2dbd7482098dd68d0d8970117\" } }";
        HttpUtils.test_create(url, content, "Firewall Singleton Post Failed");
        return content;
    }

    public void singleton_fw_get_with_one_query_item_test(String createJsonString) {
        String url = base + "/fw/firewalls";
        HttpUtils.test_fetch_with_one_query_item(url, createJsonString, "firewalls");
    }

    public void fw_modify_test() {
        String url = base + "/fw/firewalls/3b0ef8f4-82c7-44d4-a4fb-6177f9a21977";
        String content = " { \"firewall\": { \"admin_state_up\": false," + "\"description\": \"\","
                + "\"firewall_policy_id\": \"c69933c1-b472-44f9-8226-30dc4ffd454c\","
                + "\"id\": \"3b0ef8f4-82c7-44d4-a4fb-6177f9a21977\","
                + "\"name\": \"\", \"status\": \"PENDING_CREATE\","
                + "\"tenant_id\": \"45977fa2dbd7482098dd68d0d8970117\" } }";
        HttpUtils.test_modify(url, content, "Firewall Singleton Post Failed");
    }

    public void fw_element_get_test() {
        String url = base + "/fw/firewalls/3b0ef8f4-82c7-44d4-a4fb-6177f9a21977";
        HttpUtils.test_fetch(url, true, "Firewall Element Get Failed");
    }

    public void fw_element_get_with_query_test() {
        String url = base + "/fw/firewalls/3b0ef8f4-82c7-44d4-a4fb-6177f9a21977"
                + "?fields=tenant_id&fields=id&fields=name&fields=description&fields=shared"
                + "&fields=admin_state_up&fields=status&fields=firewall_policy_id"
                + "&fields=limit&fields=marker&fields=page_reverse";
        HttpUtils.test_fetch(url, true, "Firewall Element Get With Query Failed");
    }

    public void fw_delete_test() {
        String url = base + "/fw/firewalls/3b0ef8f4-82c7-44d4-a4fb-6177f9a21977";
        HttpUtils.test_delete(url, "Firewall Delete Failed");
    }

    public void fw_element_negative_get_test() {
        String url = base + "/fw/firewalls/3b0ef8f4-82c7-44d4-a4fb-6177f9a21977";
        HttpUtils.test_fetch(url, false, "Firewall Element Negative Get Failed");
    }

    public void fw_collection_bug4944_test() {
        String url = base + "/fw/firewalls";
        String content = " { \"firewall\": { \"admin_state_up\": true,\"shared\": false,"
                + "\"id\": \"3b0ef8f4-82c7-44d4-a4fb-6177f9a21977\" } }";
        HttpUtils.test_create(url, content, "Firewall Singleton Post Failed");
        url = base + "/fw/firewalls?shared=false";
        HttpUtils.test_fetch_collection_response(url, "firewalls", "Firewall Collection Get Response Failed");
        url = base + "/fw/firewalls/3b0ef8f4-82c7-44d4-a4fb-6177f9a21977";
        HttpUtils.test_delete(url, "Firewall Delete Failed");
    }

    public static void runTests(String base) {
        NeutronFirewallTests fwTester = new NeutronFirewallTests(base);
        String createJsonString = fwTester.singleton_fw_create_test();
        fwTester.singleton_fw_get_with_one_query_item_test(createJsonString);
        fwTester.fw_element_get_test();
        fwTester.fw_element_get_with_query_test();
        fwTester.fw_collection_get_test();
        fwTester.fw_modify_test();
        fwTester.fw_delete_test();
        fwTester.fw_element_negative_get_test();
        fwTester.fw_collection_bug4944_test();
    }
}
