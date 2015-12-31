/*
 * Copyright (C) 2015 IBM, Inc.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.e2etest;

import java.io.OutputStreamWriter;

import java.lang.Thread;

import java.net.HttpURLConnection;
import java.net.URL;

import org.junit.Assert;

public class NeutronFirewallPolicyTests {
    String base;

    public NeutronFirewallPolicyTests(String base) {
        this.base = base;
    }

    public void fw_policy_collection_get_test() {
        String url = base + "/fw/firewall_policies";
        ITNeutronE2E.test_fetch(url, "Firewall Policy Collection GET failed");
    }

    public void singleton_fw_policy_create_test() {
        String url = base + "/fw/firewall_policies";
        String content = " { \"firewall_policy\": { \"audited\": false," +
            "\"description\": \"\", \"firewall_rules\": [" +
                "\"8722e0e0-9cc9-4490-9660-8c9a5732fbb0\" ]," +
            "\"id\": \"c69933c1-b472-44f9-8226-30dc4ffd454c\"," +
            "\"name\": \"test-policy\", \"shared\": false," +
            "\"tenant_id\": \"45977fa2dbd7482098dd68d0d8970117\" } }";
        ITNeutronE2E.test_create(url, content, "Firewall Policy Singleton Post Failed");
    }

    public void fw_policy_modify_test() {
        String url = base + "/fw/firewall_policies/c69933c1-b472-44f9-8226-30dc4ffd454c";
        String content = " { \"firewall_policy\": { \"audited\": false," +
            "\"description\": \"\", \"firewall_rules\": [" +
                "\"a08ef905-0ff6-4784-8374-175fffe7dade\"," +
                "\"8722e0e0-9cc9-4490-9660-8c9a5732fbb0\" ]," +
            "\"id\": \"c69933c1-b472-44f9-8226-30dc4ffd454c\"," +
            "\"name\": \"test-policy\", \"shared\": false," +
            "\"tenant_id\": \"45977fa2dbd7482098dd68d0d8970117\" } }";
        ITNeutronE2E.test_modify(url, content, "Firewall Policy Singleton Post Failed");
    }

    public void fw_policy_element_get_test() {
        String url = base + "/fw/firewall_policies/c69933c1-b472-44f9-8226-30dc4ffd454c" +
            "?fields=tenant_id&fields=name&fields=description" +
            "&fields=shared&fields=firewall_rules&fields=audited";
        ITNeutronE2E.test_fetch(url, true, "Firewall Policy Element Get Failed");
    }

    public void fw_policy_delete_test() {
        String url = base + "/fw/firewall_policies/c69933c1-b472-44f9-8226-30dc4ffd454c";
        ITNeutronE2E.test_delete(url, "Firewall Policy Delete Failed");
    }

    public void fw_policy_element_negative_get_test() {
        String url = base + "/fw/firewall_policies/c69933c1-b472-44f9-8226-30dc4ffd454c";
        ITNeutronE2E.test_fetch(url, false, "Firewall Policy Element Negative Get Failed");
    }

    public static void runTests(String base) {
        NeutronFirewallPolicyTests fw_policy_tester = new NeutronFirewallPolicyTests(base);
        fw_policy_tester.fw_policy_collection_get_test();
        fw_policy_tester.singleton_fw_policy_create_test();
        fw_policy_tester.fw_policy_element_get_test();
        fw_policy_tester.fw_policy_modify_test();
        fw_policy_tester.fw_policy_delete_test();
        fw_policy_tester.fw_policy_element_negative_get_test();
    }
}
