/*
 * Copyright (C) 2016 Intel, Corp.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.e2etest;

public class NeutronQosPolicyTests {
    String base;

    public NeutronQosPolicyTests(String base) {
        this.base = base;
    }

    public void qos_policy_collection_get_test() {
        String url = base + "/qos/policies";
        ITNeutronE2E.test_fetch(url, "Qos Policy collection GET failed");
    }

    public String singleton_qos_policy_create_test() {
        String url = base + "/qos/policies";
        String content = "{\"policy\": {\"id\": \"d6220bbb-35f3-48ab-8eae-69c60aef3546\","
                + "\"tenant_id\": \"aa902936679e4ea29bfe1158e3450a13\"," + "\"name\": \"jaxb-test\", "
                + "\"shared\": false }}";
        ITNeutronE2E.test_create(url, content, "Qos Policy Singleton POST Failed");
        return content;
    }

    public void singleton_qos_policy_get_with_query_item_test(String createJsonString) {
        String url = base + "/qos/policies";
        ITNeutronE2E.test_fetch_with_one_query_item(url, createJsonString, "policies");
    }

    public void qos_policy_modify_test() {
        String url = base + "/qos/policies/d6220bbb-35f3-48ab-8eae-69c60aef3546";
        String content = "{\"policy\": {\"id\": \"d6220bbb-35f3-48ab-8eae-69c60aef3546\","
            + "\"tenant_id\": \"aa902936679e4ea29bfe1158e3450a13\"," + "\"name\": \"jaxb-test\", "
            + "\"shared\": false,"
            + "\"bandwidth_limit_rules\": [ {\"id\": \"d6220bbb-35f3-48ab-8eae-69c60aef3546\","
            + "\"tenant_id\": \"aa902936679e4ea29bfe1158e3450a13\",\"max_kbps\": 25,"
            + "\"max_burst_kbps\": 100, \"direction\": \"egress\" } ] ,"
            + "\"dscp_marking_rules\": [ {\"id\": \"d6220bbb-35f3-48ab-8eae-69c60aef3546\","
            + "\"tenant_id\": \"aa902936679e4ea29bfe1158e3450a13\", " + "\"dscp_mark\": 8 } ] ,"
            + "\"minimum_bandwidth_rules\": [ {\"id\": \"d6220bbb-35f3-48ab-8eae-69c60aef3546\","
            + "\"tenant_id\": \"aa902936679e4ea29bfe1158e3450a13\", " + "\"min_kbps\": 20,"
            + "\"direction\": \"egress\" } ] }}";
        ITNeutronE2E.test_modify(url, content, "Qos Policy Singleton Put failed");
    }

    public void qos_policy_element_get_test() {
        String url = base + "/qos/policies/d6220bbb-35f3-48ab-8eae-69c60aef3546";
        ITNeutronE2E.test_fetch(url, true, "Qos Policy Element Get failed");
    }

    public void qos_policy_element_get_with_query_test() {
        String url = base + "/qos/policies/d6220bbb-35f3-48ab-8eae-69c60aef3546"
                + "?fields=tenant_id&fields=id&fields=name&fields=description" + "&fields=shared&fields=limits"
                + "&fields=marker&fields=page_reverse";
        ITNeutronE2E.test_fetch(url, true, "Qos Firewall Element Get with Query Failed");
    }

    public void qos_policy_delete_test() {
        String url = base + "/qos/policies/d6220bbb-35f3-48ab-8eae-69c60aef3546";
        ITNeutronE2E.test_delete(url, "Qos Policy Delete Failed");
    }

    public void qos_policy_element_negative_get_test() {
        String url = base + "/qos/policies/d6220bbb-35f3-48ab-8eae-69c60aef3546";
        ITNeutronE2E.test_fetch(url, false, "Qos Policy Element Negative Get Failed");
    }

    public static void runTests(String base) {
        NeutronQosPolicyTests qosPolicyTester = new NeutronQosPolicyTests(base);
        String createJsonString = qosPolicyTester.singleton_qos_policy_create_test();
        qosPolicyTester.singleton_qos_policy_get_with_query_item_test(createJsonString);
        qosPolicyTester.qos_policy_element_get_test();
        qosPolicyTester.qos_policy_element_get_with_query_test();
        qosPolicyTester.qos_policy_collection_get_test();
        qosPolicyTester.qos_policy_modify_test();
        qosPolicyTester.qos_policy_delete_test();
        qosPolicyTester.qos_policy_element_negative_get_test();
    }
}
