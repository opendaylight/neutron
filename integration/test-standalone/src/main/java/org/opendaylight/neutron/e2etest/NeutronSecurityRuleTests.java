/*
 * Copyright (C) 2015 IBM, Inc.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.neutron.e2etest;

public class NeutronSecurityRuleTests {

    private static final String TEST_SECURITY_GROUP_ID = "b60490fe-60a5-40be-af63-1d641381b784";

    private final String base;

    public NeutronSecurityRuleTests(String base) {
        this.base = base;
    }

    private void securityRule_collection_get_test() {
        String url = base + "/security-group-rules";
        HttpUtils.test_fetch(url, "Security Rule Collection GET failed");
    }

    private void singleton_sr_without_groupid_create_test(int responseCode) {
        String url = base + "/security-group-rules";
        String content = " {\"security_group_rule\": " + "{\"remote_group_id\": null, \"direction\": \"ingress\", "
                + "\"remote_ip_prefix\": null, \"protocol\": \"tcp\", " + "\"ethertype\": \"IPv6\", \"tenant_id\": "
                + "\"00f340c7c3b34ab7be1fc690c05a0275\", \"port_range_max\": 77, " + "\"port_range_min\": 77, "
                + "\"id\": \"9b4be7fa-e56e-40fb-9516-1f0fa9185669\"}}"; // no security_group_id
        HttpUtils.test_create(url, responseCode, content, "Security Rule Singleton Post Failed");
    }

    private String singleton_sr_create_test(int responseCode) {
        String url = base + "/security-group-rules";
        String content = " {\"security_group_rule\": " + "{\"remote_group_id\": null, \"direction\": \"ingress\", "
                + "\"remote_ip_prefix\": null, \"protocol\": \"tcp\", " + "\"ethertype\": \"IPv6\", \"tenant_id\": "
                + "\"00f340c7c3b34ab7be1fc690c05a0275\", \"port_range_max\": 77, " + "\"port_range_min\": 77, "
                + "\"id\": \"9b4be7fa-e56e-40fb-9516-1f0fa9185669\", " + "\"security_group_id\": "
                + "\"" + TEST_SECURITY_GROUP_ID + "\"}}";
        HttpUtils.test_create(url, responseCode, content, "Security Rule Singleton Post Failed");
        return content;
    }

    private void singleton_sr_get_with_one_query_item_test(String createJsonString) {
        String url = base + "/security-group-rules";
        HttpUtils.test_fetch_with_one_query_item(url, createJsonString, "security_group_rules");
    }

    private void multiple_sr_create_test() {
        String url = base + "/security-group-rules";
        String content = " {\"security_group_rules\": [" + "{" + "  \"id\": \"35fb0f34-c8d3-416d-a205-a2c75f7b8e22\","
                + "  \"direction\": \"egress\"," + "  \"ethertype\": \"IPv6\"," + "  \"protocol\": \"tcp\","
                + "  \"security_group_id\": \"b60490fe-60a5-40be-af63-1d641381b784\","
                + "  \"tenant_id\": \"2640ee2ac2474bf3906e482047204fcb\"" + "}," + "{"
                + "  \"id\": \"63814eed-bc12-4fe4-8b17-2af178224c71\"," + "  \"direction\": \"egress\","
                + "  \"ethertype\": \"IPv4\"," + "  \"protocol\": \"6\","
                + "  \"security_group_id\": \"b60490fe-60a5-40be-af63-1d641381b784\","
                + "  \"tenant_id\": \"2640ee2ac2474bf3906e482047204fcb\"" + "}," + "{"
                + "  \"id\": \"ccb9823e-559b-4d84-b656-2739f8e56d89\"," + "  \"direction\": \"ingress\","
                + "  \"ethertype\": \"IPv6\"," + "  \"protocol\": 6,"
                + "  \"remote_group_id\": \"b60490fe-60a5-40be-af63-1d641381b784\","
                + "  \"security_group_id\": \"b60490fe-60a5-40be-af63-1d641381b784\","
                + "  \"tenant_id\": \"2640ee2ac2474bf3906e482047204fcb\"" + "}," + "{"
                + "  \"id\": \"fbc3f809-7378-40a4-822f-7a70f6ccba98\"," + "  \"direction\": \"ingress\","
                + "  \"ethertype\": \"IPv4\"," + "  \"protocol\": \"udp\","
                + "  \"remote_group_id\": \"b60490fe-60a5-40be-af63-1d641381b784\","
                + "  \"security_group_id\": \"b60490fe-60a5-40be-af63-1d641381b784\","
                + "  \"tenant_id\": \"2640ee2ac2474bf3906e482047204fcb\"" + "}" + "]}";
        HttpUtils.test_create(url, content, "Security Rule Multiple Post Failed");
    }

    private void singleton_sr_modify_test(int responseCode) {
        String url = base + "/security-group-rules/9b4be7fa-e56e-40fb-9516-1f0fa9185669";
        String content = " {\"security_group_rule\": " + "{\"remote_group_id\": null, \"direction\": \"egress\", "
                + "\"remote_ip_prefix\": null, \"protocol\": \"tcp\", " + "\"ethertype\": \"IPv6\", \"tenant_id\": "
                + "\"00f340c7c3b34ab7be1fc690c05a0275\", \"port_range_max\": 77, " + "\"port_range_min\": 77, "
                + "\"id\": \"9b4be7fa-e56e-40fb-9516-1f0fa9185669\", " + "\"security_group_id\": "
                + "\"" + TEST_SECURITY_GROUP_ID + "\"}}";
        HttpUtils.test_modify(url, responseCode, content, "Security Rule Singleton Put Failed");
    }

    private void sr_element_get_test() {
        String url = base + "/security-group-rules/9b4be7fa-e56e-40fb-9516-1f0fa9185669";
        HttpUtils.test_fetch(url, true, "Security Rule Element Get Failed");
    }

    private void sr_element_get_with_query_test() {
        String url = base + "/security-group-rules/9b4be7fa-e56e-40fb-9516-1f0fa9185669"
                + "?fields=id&fields=direction&fields=protocol&fields=port_range_min"
                + "&fields=port_range_max&fields=ethertype&fields=remote_ip_prefix"
                + "&fields=remote_group_id&fields=security_group_id&fields=tenant_id"
                + "&fields=limit&fields=marker&fields=page_reverse";
        HttpUtils.test_fetch(url, true, "Security Rule Element Get With Query Failed");
    }

    private void sr_delete_test() {
        String url = base + "/security-group-rules/9b4be7fa-e56e-40fb-9516-1f0fa9185669";
        HttpUtils.test_delete(url, "Security Rule Delete Failed");
    }

    private void sr_element_negative_get_test() {
        String url = base + "/security-group-rules/9b4be7fa-e56e-40fb-9516-1f0fa9185669";
        HttpUtils.test_fetch(url, false, "Security Rule Element Negative Get Failed");
    }

    private void bug5478_rule_delete_negative_test() {
        String url = base + "/security-group-rules/9b4be7fa-e56e-40fb-9516-1f0fa9185669";
        HttpUtils.test_delete_404(url, "Security Rule Delete 404 Failed");
    }

    private void bug4043_ipv4_test() {
        String url = base + "/security-group-rules";
        String content = " {\"security_group_rule\": " + "{\"remote_group_id\": null, \"direction\": \"ingress\", "
                + "\"remote_ip_prefix\": \"10.10.10.10/16\", \"protocol\": \"tcp\", "
                + "\"ethertype\": \"IPv4\", \"tenant_id\": "
                + "\"00f340c7c3b34ab7be1fc690c05a0275\", \"port_range_max\": 77, " + "\"port_range_min\": 77, "
                + "\"id\": \"01234567-0123-0123-0123-01234567890a\", " + "\"security_group_id\": "
                + "\"" + TEST_SECURITY_GROUP_ID + "\"}}";
        HttpUtils.test_create(url, content, "Security Rule bug4043 IPv4 Failed");

        url = url + "/01234567-0123-0123-0123-01234567890a";
        HttpUtils.test_delete(url, "Security Rule Delete Failed");
    }

    private void bug4043_ipv6_test() {
        String url = base + "/security-group-rules";
        String content = " {\"security_group_rule\": " + "{\"remote_group_id\": null, \"direction\": \"ingress\", "
                + "\"remote_ip_prefix\": \"fe80::1/10\", \"protocol\": \"tcp\", "
                + "\"ethertype\": \"IPv6\", \"tenant_id\": "
                + "\"00f340c7c3b34ab7be1fc690c05a0275\", \"port_range_max\": 77, " + "\"port_range_min\": 77, "
                + "\"id\": \"01234567-0123-0123-0123-01234567890a\", " + "\"security_group_id\": "
                + "\"b60490fe-60a5-40be-af63-1d641381b784\"}}";
        HttpUtils.test_create(url, content, "Security Rule Bug4043 IPv6 Failed");

        url = url + "/01234567-0123-0123-0123-01234567890a";
        HttpUtils.test_delete(url, "Security Rule Delete Failed");
    }

    private String bug6398_sr_create_test() {
        String url = base + "/security-group-rules";
        String content = " {\"security_group_rule\": " + "{\"remote_group_id\": null, \"direction\": \"ingress\", "
                + "\"remote_ip_prefix\": null, \"protocol\": \"TCP\", " + "\"ethertype\": \"IPv6\", \"tenant_id\": "
                + "\"00f340c7c3b34ab7be1fc690c05a0275\", \"port_range_max\": 77, " + "\"port_range_min\": 77, "
                + "\"id\": \"9b4be7fa-e56e-40fb-9516-1f0fa9185669\", " + "\"security_group_id\": "
                + "\"b60490fe-60a5-40be-af63-1d641381b784\"}}";
        HttpUtils.test_create(url, 400, content, "Security Rule Singleton Post Bug 6398 regressed");
        return content;
    }

    public static void runTests(String base) {
        NeutronSecurityRuleTests securityRuleTester = new NeutronSecurityRuleTests(base);
        securityRuleTester.singleton_sr_without_groupid_create_test(500);
        securityRuleTester.singleton_sr_create_test(HttpUtils.HTTP_MISSING_DEPENDENCY); // NEUTRON-158
        securityRuleTester.singleton_sr_modify_test(404); // cannot modify a SR that has not been created
        new NeutronSecurityGroupTests(base).singleton_sg_create(TEST_SECURITY_GROUP_ID);
        String createJsonString = securityRuleTester.singleton_sr_create_test(201);
        securityRuleTester.singleton_sr_get_with_one_query_item_test(createJsonString);
        securityRuleTester.multiple_sr_create_test();
        securityRuleTester.singleton_sr_modify_test(200);
        securityRuleTester.sr_element_get_test();
        securityRuleTester.sr_element_get_with_query_test();
        securityRuleTester.securityRule_collection_get_test();
        securityRuleTester.sr_delete_test();
        securityRuleTester.sr_element_negative_get_test();
        securityRuleTester.bug5478_rule_delete_negative_test();
        securityRuleTester.bug4043_ipv4_test();
        securityRuleTester.bug4043_ipv6_test();
        securityRuleTester.bug6398_sr_create_test();

        // NEUTRON-158: Cannot modify a SR who's SG is already deleted
        securityRuleTester.singleton_sr_create_test(201);
        new NeutronSecurityGroupTests(base).sg_delete(TEST_SECURITY_GROUP_ID);
        securityRuleTester.singleton_sr_modify_test(HttpUtils.HTTP_MISSING_DEPENDENCY);
    }
}
