/*
 * Copyright (C) 2015 IBM, Inc.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.e2etest;

public class NeutronFirewallRuleTests {
    String base;

    public NeutronFirewallRuleTests(String base) {
        this.base = base;
    }

    public void fw_rule_collection_get_test() {
        String url = base + "/fw/firewall_rules";
        HttpUtils.test_fetch(url, "Firewall Rule Collection GET failed");
    }

    public String singleton_fw_rule_create_test() {
        String url = base + "/fw/firewall_rules";
        String content = "{ \"firewall_rule\": { \"action\": \"allow\"," + "\"destination_ip_address\": null,"
                + "\"destination_port_min\": \"80\", \"destination_port_max\": \"80\"," + "\"enabled\": true,"
                + "\"firewall_policy_id\": null," + "\"id\": \"8722e0e0-9cc9-4490-9660-8c9a5732fbb0\","
                + "\"ip_version\": 4, \"name\": \"ALLOW_HTTP\"," + "\"position\": null, \"protocol\": \"tcp\","
                + "\"shared\": false, \"source_ip_address\": null,"
                + "\"source_port_min\": null, \"source_port_max\": null,"
                + "\"tenant_id\": \"45977fa2dbd7482098dd68d0d8970117\" } }";
        HttpUtils.test_create(url, content, "Firewall Rule Singleton Post Failed");
        return content;
    }

    public void singleton_fw_rule_get_with_one_query_item_test(String createJsonString) {
        String url = base + "/fw/firewall_rules";
        HttpUtils.test_fetch_with_one_query_item(url, createJsonString, "firewall_rules");
    }

    public void fw_rule_modify_test() {
        String url = base + "/fw/firewall_rules/8722e0e0-9cc9-4490-9660-8c9a5732fbb0";
        String content = "{ \"firewall_rule\": { \"action\": \"allow\"," + "\"destination_ip_address\": null,"
                + "\"destination_port_min\": \"80\", \"destination_port_max\": \"80\"," + "\"enabled\": true,"
                + "\"firewall_policy_id\": null," + "\"id\": \"8722e0e0-9cc9-4490-9660-8c9a5732fbb0\","
                + "\"ip_version\": 4, \"name\": \"ALLOW_HTTP\"," + "\"position\": null, \"protocol\": \"tcp\","
                + "\"shared\": true, \"source_ip_address\": null,"
                + "\"source_port_min\": null, \"source_port_max\": null,"
                + "\"tenant_id\": \"45977fa2dbd7482098dd68d0d8970117\" } }";
        HttpUtils.test_modify(url, content, "Firewall Rule Singleton Put Failed");
    }

    public void fw_rule_element_get_test() {
        String url = base + "/fw/firewall_rules/8722e0e0-9cc9-4490-9660-8c9a5732fbb0";
        HttpUtils.test_fetch(url, true, "Firewall Rule Element Get Failed");
    }

    public void fw_rule_element_get_with_query_test() {
        String url = base + "/fw/firewall_rules/8722e0e0-9cc9-4490-9660-8c9a5732fbb0"
                + "?fields=id&fields=tenant_id&fields=name"
                + "&fields=shared&fields=firewall_policy_id&fields=protocol&fields=enabled"
                + "&fields=source_ip_address&fields=destination_ip_address"
                + "&fields=destination_port_min&fields=destination_port_max&fields=position"
                + "&fields=action&fields=source_port_min&fields=source_port_max"
                + "&fields=limit&fields=marker&fields=page_reverse";
        HttpUtils.test_fetch(url, true, "Firewall Rule Element Get With Query Failed");
    }

    public void fw_rule_delete_test() {
        String url = base + "/fw/firewall_rules/8722e0e0-9cc9-4490-9660-8c9a5732fbb0";
        HttpUtils.test_delete(url, "Firewall Rule Delete Failed");
    }

    public void fw_rule_element_negative_get_test() {
        String url = base + "/fw/firewall_rules/8722e0e0-9cc9-4490-9660-8c9a5732fbb0";
        HttpUtils.test_fetch(url, false, "Firewall Rule Element Negative Get Failed");
    }

    public String test_bug6398_fw_rule_create_test() {
        String url = base + "/fw/firewall_rules";
        String content = "{ \"firewall_rule\": { \"action\": \"allow\"," + "\"destination_ip_address\": null,"
                + "\"destination_port_min\": \"80\", \"destination_port_max\": \"80\"," + "\"enabled\": true,"
                + "\"firewall_policy_id\": null," + "\"id\": \"8722e0e0-9cc9-4490-9660-8c9a5732fbb0\","
                + "\"ip_version\": 4, \"name\": \"ALLOW_HTTP\"," + "\"position\": null, \"protocol\": \"TCP\","
                + "\"shared\": false, \"source_ip_address\": null,"
                + "\"source_port_min\": null, \"source_port_max\": null,"
                + "\"tenant_id\": \"45977fa2dbd7482098dd68d0d8970117\" } }";
        HttpUtils.test_create(url, 400, content, "Firewall Rule Singleton Post Bug 6398 regressed");
        return content;
    }

    public static void runTests(String base) {
        NeutronFirewallRuleTests fwRuleTester = new NeutronFirewallRuleTests(base);
        String createJsonString = fwRuleTester.singleton_fw_rule_create_test();
        fwRuleTester.singleton_fw_rule_get_with_one_query_item_test(createJsonString);
        fwRuleTester.fw_rule_element_get_test();
        fwRuleTester.fw_rule_element_get_with_query_test();
        fwRuleTester.fw_rule_collection_get_test();
        fwRuleTester.fw_rule_modify_test();
        fwRuleTester.fw_rule_delete_test();
        fwRuleTester.fw_rule_element_negative_get_test();
        fwRuleTester.test_bug6398_fw_rule_create_test();
    }
}
