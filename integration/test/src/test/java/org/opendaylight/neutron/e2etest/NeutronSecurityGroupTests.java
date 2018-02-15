/*
 * Copyright (C) 2015 IBM, Inc.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.e2etest;

public class NeutronSecurityGroupTests {
    String base;

    public NeutronSecurityGroupTests(String base) {
        this.base = base;
    }

    public void securityGroup_collection_get_test() {
        String url = base + "/security-groups";
        HttpUtils.test_fetch(url, "Security Group Collection GET failed");
    }

    public String singleton_sg_create_test() {
        String url = base + "/security-groups";
        String content = "{\"security_group\": {\"tenant_id\": "
                + "\"1dfe7dffa0624ae882cdbda397d1d276\", \"description\": \"\", "
                + "\"id\": \"521e29d6-67b8-4b3c-8633-027d21195333\", "
                + "\"security_group_rules\": [{\"remote_group_id\": null, "
                + "\"direction\": \"egress\", \"remote_ip_prefix\": null, "
                + "\"protocol\": null, \"ethertype\": \"IPv4\", "
                + "\"tenant_id\": \"1dfe7dffa0624ae882cdbda397d1d276\", "
                + "\"port_range_max\": null, \"port_range_min\": null, "
                + "\"id\": \"823faaf7-175d-4f01-a271-0bf56fb1e7e6\", "
                + "\"security_group_id\": \"d3329053-bae5-4bf4-a2d1-7330f11ba5db\"}, "
                + "{\"remote_group_id\": null, \"direction\": \"egress\", "
                + "\"remote_ip_prefix\": null, \"protocol\": null, " + "\"ethertype\": \"IPv6\", \"tenant_id\": "
                + "\"1dfe7dffa0624ae882cdbda397d1d276\", \"port_range_max\": null, "
                + "\"port_range_min\": null, \"id\": " + "\"d3329053-bae5-4bf4-a2d1-7330f11ba5db\", "
                + "\"security_group_id\": \"d3329053-bae5-4bf4-a2d1-7330f11ba5db\"}], "
                + "\"name\": \"tempest-secgroup-1272206251\"}}";
        HttpUtils.test_create(url, content, "Security Group Singleton Post Failed");
        return content;
    }

    public void singleton_sg_get_with_one_query_item_test(String createJsonString) {
        String url = base + "/security-groups";
        HttpUtils.test_fetch_with_one_query_item(url, createJsonString, "security_groups");
    }

    public void sg_update_test() {
        String url = base + "/security-groups/521e29d6-67b8-4b3c-8633-027d21195333";
        String content = "{\"security_group\": {\"tenant_id\": "
                + "\"00f340c7c3b34ab7be1fc690c05a0275\", \"description\": "
                + "\"tempest-security-description-897433715\", \"id\": " + "\"521e29d6-67b8-4b3c-8633-027d21195333\", "
                + "\"security_group_rules\": [{\"remote_group_id\": null, "
                + "\"direction\": \"egress\", \"remote_ip_prefix\": null, "
                + "\"protocol\": null, \"ethertype\": \"IPv4\", \"tenant_id\": "
                + "\"00f340c7c3b34ab7be1fc690c05a0275\", \"port_range_max\": null, "
                + "\"port_range_min\": null, \"id\": "
                + "\"808bcefb-9917-4640-be68-14157bf33288\", \"security_group_id\": "
                + "\"521e29d6-67b8-4b3c-8633-027d21195333\"}, {\"remote_group_id\": "
                + "null, \"direction\": \"egress\", \"remote_ip_prefix\": null, "
                + "\"protocol\": null, \"ethertype\": \"IPv6\", \"tenant_id\": "
                + "\"00f340c7c3b34ab7be1fc690c05a0275\", \"port_range_max\": null, "
                + "\"port_range_min\": null, \"id\": " + "\"c376f7b5-a281-40e0-a703-5c832c03aeb3\", "
                + "\"security_group_id\": " + "\"521e29d6-67b8-4b3c-8633-027d21195333\"}], \"name\": "
                + "\"tempest-security--1135434738\"}}";
        HttpUtils.test_modify(url, content, "Security Group Put Failed");
    }

    public void sg_element_get_test() {
        String url = base + "/security-groups/521e29d6-67b8-4b3c-8633-027d21195333";
        HttpUtils.test_fetch(url, true, "Security Group Element Get Failed");
    }

    public void sg_element_get_with_query_test() {
        String url = base + "/security-groups/521e29d6-67b8-4b3c-8633-027d21195333"
                + "?fields=id&fields=name&fields=description&fields=tenant_id"
                + "&fields=limit&fields=marker&fields=page_reverse";
        HttpUtils.test_fetch(url, true, "Security Group Element Get Failed");
    }

    public void sg_delete_test() {
        String url = base + "/security-groups/521e29d6-67b8-4b3c-8633-027d21195333";
        HttpUtils.test_delete(url, "Security Group Delete Failed");
    }

    public void sg_element_negative_get_test() {
        String url = base + "/security-groups/521e29d6-67b8-4b3c-8633-027d21195333";
        HttpUtils.test_fetch(url, false, "Security Group Element Negative Get Failed");
    }

    public static void runTests(String base) {
        NeutronSecurityGroupTests securityGroupTester = new NeutronSecurityGroupTests(base);
        String createJsonString = securityGroupTester.singleton_sg_create_test();
        securityGroupTester.singleton_sg_get_with_one_query_item_test(createJsonString);
        securityGroupTester.sg_update_test();
        securityGroupTester.sg_element_get_test();
        securityGroupTester.sg_element_get_with_query_test();
        securityGroupTester.securityGroup_collection_get_test();
        securityGroupTester.sg_delete_test();
        securityGroupTester.sg_element_negative_get_test();
    }
}
