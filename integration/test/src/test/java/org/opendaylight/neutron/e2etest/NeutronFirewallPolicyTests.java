/*
 * Copyright (C) 2015 IBM, Inc.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.e2etest;

public class NeutronFirewallPolicyTests {
    String base;

    public NeutronFirewallPolicyTests(String base) {
        this.base = base;
    }

    public void fw_policy_collection_get_test() {
        String url = base + "/fw/firewall_policies";
        HttpUtils.test_fetch(url, "Firewall Policy Collection GET failed");
    }

    public String singleton_fw_policy_create_test() {
        String url = base + "/fw/firewall_policies";
        String content = " { \"firewall_policy\": { \"audited\": false,"
                + "\"description\": \"\", \"firewall_rules\": [" + "\"8722e0e0-9cc9-4490-9660-8c9a5732fbb0\" ],"
                + "\"id\": \"c69933c1-b472-44f9-8226-30dc4ffd454c\"," + "\"name\": \"test-policy\", \"shared\": false,"
                + "\"tenant_id\": \"45977fa2dbd7482098dd68d0d8970117\" } }";
        HttpUtils.test_create(url, content, "Firewall Policy Singleton Post Failed");
        return content;
    }

    public void singleton_fw_policy_get_with_one_query_item_test(String createJsonString) {
        String url = base + "/fw/firewall_policies";
        HttpUtils.test_fetch_with_one_query_item(url, createJsonString, "firewall_policies");
    }

    public void fw_policy_modify_test() {
        String url = base + "/fw/firewall_policies/c69933c1-b472-44f9-8226-30dc4ffd454c";
        String content = " { \"firewall_policy\": { \"audited\": false,"
                + "\"description\": \"\", \"firewall_rules\": [" + "\"a08ef905-0ff6-4784-8374-175fffe7dade\","
                + "\"8722e0e0-9cc9-4490-9660-8c9a5732fbb0\" ]," + "\"id\": \"c69933c1-b472-44f9-8226-30dc4ffd454c\","
                + "\"name\": \"test-policy\", \"shared\": false,"
                + "\"tenant_id\": \"45977fa2dbd7482098dd68d0d8970117\" } }";
        HttpUtils.test_modify(url, content, "Firewall Policy Singleton Post Failed");
    }

    public void fw_policy_element_get_test() {
        String url = base + "/fw/firewall_policies/c69933c1-b472-44f9-8226-30dc4ffd454c";
        HttpUtils.test_fetch(url, true, "Firewall Policy Element Get Failed");
    }

    public void fw_policy_element_get_with_query_test() {
        String url = base + "/fw/firewall_policies/c69933c1-b472-44f9-8226-30dc4ffd454c"
                + "?fields=tenant_id&field=id&fields=name&fields=description"
                + "&fields=shared&fields=firewall_rules&fields=audited"
                + "&fields=limit&fields=marker&fields=page_reverse";
        HttpUtils.test_fetch(url, true, "Firewall Policy Element Get with Query Failed");
    }

    public void fw_policy_delete_test() {
        String url = base + "/fw/firewall_policies/c69933c1-b472-44f9-8226-30dc4ffd454c";
        HttpUtils.test_delete(url, "Firewall Policy Delete Failed");
    }

    public void fw_policy_element_negative_get_test() {
        String url = base + "/fw/firewall_policies/c69933c1-b472-44f9-8226-30dc4ffd454c";
        HttpUtils.test_fetch(url, false, "Firewall Policy Element Negative Get Failed");
    }

    public static void runTests(String base) {
        NeutronFirewallPolicyTests fwPolicyTester = new NeutronFirewallPolicyTests(base);
        String createJsonString = fwPolicyTester.singleton_fw_policy_create_test();
        fwPolicyTester.singleton_fw_policy_get_with_one_query_item_test(createJsonString);
        fwPolicyTester.fw_policy_element_get_test();
        fwPolicyTester.fw_policy_element_get_with_query_test();
        fwPolicyTester.fw_policy_collection_get_test();
        fwPolicyTester.fw_policy_modify_test();
        fwPolicyTester.fw_policy_delete_test();
        fwPolicyTester.fw_policy_element_negative_get_test();
    }
}
