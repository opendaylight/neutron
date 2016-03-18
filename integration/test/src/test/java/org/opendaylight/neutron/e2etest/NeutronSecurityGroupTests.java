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

public class NeutronSecurityGroupTests {
    String base;

    public NeutronSecurityGroupTests(String base) {
        this.base = base;
    }

    public void securityGroup_collection_get_test() {
        String url = base + "/security-groups";
        ITNeutronE2E.test_fetch(url, "Security Group Collection GET failed");
    }

    public void singleton_sg_create_test() {
        String url = base + "/security-groups";
        String content = "{\"security_group\": {\"tenant_id\": " +
            "\"1dfe7dffa0624ae882cdbda397d1d276\", \"description\": \"\", " +
            "\"id\": \"521e29d6-67b8-4b3c-8633-027d21195333\", " +
            "\"security_group_rules\": [{\"remote_group_id\": null, " +
            "\"direction\": \"egress\", \"remote_ip_prefix\": null, " +
            "\"protocol\": null, \"ethertype\": \"IPv4\", " +
            "\"tenant_id\": \"1dfe7dffa0624ae882cdbda397d1d276\", " +
            "\"port_range_max\": null, \"port_range_min\": null, " +
            "\"id\": \"823faaf7-175d-4f01-a271-0bf56fb1e7e6\", " +
            "\"security_group_id\": \"d3329053-bae5-4bf4-a2d1-7330f11ba5db\"}, " +
            "{\"remote_group_id\": null, \"direction\": \"egress\", " +
            "\"remote_ip_prefix\": null, \"protocol\": null, " +
            "\"ethertype\": \"IPv6\", \"tenant_id\": " +
            "\"1dfe7dffa0624ae882cdbda397d1d276\", \"port_range_max\": null, " +
            "\"port_range_min\": null, \"id\": " +
            "\"d3329053-bae5-4bf4-a2d1-7330f11ba5db\", " +
            "\"security_group_id\": \"d3329053-bae5-4bf4-a2d1-7330f11ba5db\"}], " +
            "\"name\": \"tempest-secgroup-1272206251\"}}";
        ITNeutronE2E.test_create(url, content, "Security Group Singleton Post Failed");
    }

    public void sg_update_test() {
        String url = base + "/security-groups/521e29d6-67b8-4b3c-8633-027d21195333";
        String content = "{\"security_group\": {\"tenant_id\": " +
            "\"00f340c7c3b34ab7be1fc690c05a0275\", \"description\": " +
            "\"tempest-security-description-897433715\", \"id\": " +
            "\"521e29d6-67b8-4b3c-8633-027d21195333\", " +
            "\"security_group_rules\": [{\"remote_group_id\": null, " +
            "\"direction\": \"egress\", \"remote_ip_prefix\": null, " +
            "\"protocol\": null, \"ethertype\": \"IPv4\", \"tenant_id\": " +
            "\"00f340c7c3b34ab7be1fc690c05a0275\", \"port_range_max\": null, " +
            "\"port_range_min\": null, \"id\": " +
            "\"808bcefb-9917-4640-be68-14157bf33288\", \"security_group_id\": " +
            "\"521e29d6-67b8-4b3c-8633-027d21195333\"}, {\"remote_group_id\": " +
            "null, \"direction\": \"egress\", \"remote_ip_prefix\": null, " +
            "\"protocol\": null, \"ethertype\": \"IPv6\", \"tenant_id\": " +
            "\"00f340c7c3b34ab7be1fc690c05a0275\", \"port_range_max\": null, " +
            "\"port_range_min\": null, \"id\": " +
            "\"c376f7b5-a281-40e0-a703-5c832c03aeb3\", " +
            "\"security_group_id\": " +
            "\"521e29d6-67b8-4b3c-8633-027d21195333\"}], \"name\": " +
            "\"tempest-security--1135434738\"}}";
        ITNeutronE2E.test_modify(url, content, "Security Group Put Failed");
    }

    public void sg_element_get_test() {
        String url = base + "/security-groups/521e29d6-67b8-4b3c-8633-027d21195333";
        ITNeutronE2E.test_fetch(url, true, "Security Group Element Get Failed");
    }

    public void sg_element_get_with_query_test() {
        String url = base + "/security-groups/521e29d6-67b8-4b3c-8633-027d21195333" +
            "?fields=id&fields=name&fields=description&fields=tenant_id" +
            "&fields=limit&fields=marker&fields=page_reverse";
        ITNeutronE2E.test_fetch(url, true, "Security Group Element Get Failed");
    }

    public void sg_delete_test() {
        String url = base + "/security-groups/521e29d6-67b8-4b3c-8633-027d21195333";
        ITNeutronE2E.test_delete(url, "Security Group Delete Failed");
    }

    public void sg_element_negative_get_test() {
        String url = base + "/security-groups/521e29d6-67b8-4b3c-8633-027d21195333";
        ITNeutronE2E.test_fetch(url, false, "Security Group Element Negative Get Failed");
    }

    public void security_group_default_rule_test() {
        String url = base + "/security-groups";
        String sgId = "dddd29d6-67b8-4b3c-8633-027d21195333";
        String sRuleId1 = "ddddaaf7-175d-4f01-a271-0bf56fb1e7e6";
        String sRuleId2 = "dddd29d6-67b8-4b3c-8633-027d21195333";

        String content = "{\"security_group\": {\"tenant_id\": " +
                "\"1dfe7dffa0624ae882cdbda397d1d276\", \"description\": \"\", " +
                "\"id\": \"" + sgId + "\", " +
                "\"security_group_rules\": [{\"remote_group_id\": null, " +
                "\"direction\": \"egress\", \"remote_ip_prefix\": null, " +
                "\"protocol\": null, \"ethertype\": \"IPv4\", " +
                "\"tenant_id\": \"1dfe7dffa0624ae882cdbda397d1d276\", " +
                "\"port_range_max\": null, \"port_range_min\": null, " +
                "\"id\": \"" + sRuleId1 + "\", " +
                "\"security_group_id\": \"" + sgId + "\"}, " +
                "{\"remote_group_id\": null, \"direction\": \"egress\", " +
                "\"remote_ip_prefix\": null, \"protocol\": null, " +
                "\"ethertype\": \"IPv6\", \"tenant_id\": " +
                "\"1dfe7dffa0624ae882cdbda397d1d276\", \"port_range_max\": null, " +
                "\"port_range_min\": null, \"id\": \"" + sRuleId2 + "\", " +
                "\"security_group_id\": \"" + sgId + "\"}], " +
                "\"name\": \"tempest-secgroup-1272206251\"}}";

        // Create Security Group and verify the default Egress rules are added by fetching it.
        ITNeutronE2E.test_create(url, content, "Default rule Test1 - Security Group Singleton Post Failed");
        fetchSecurityRule(sRuleId1, true);
        fetchSecurityRule(sRuleId2, true);
        // Delete Security Group. All associated rules should be deleted.
        String deleteUrl = url + "/" + sgId;
        ITNeutronE2E.test_delete(deleteUrl, "Security Group Delete Failed");
        fetchSecurityRule(sRuleId1, false);
        fetchSecurityRule(sRuleId2, false);

        // Create Security Group, Delete the default Egress rules one by one.
        // The corresponding rule should be removed.
        ITNeutronE2E.test_create(url, content, "Default rule Test2 - Security Group Singleton Post Failed");
        deleteSecurityRule(sRuleId1);
        fetchSecurityRule(sRuleId1, false);
        fetchSecurityRule(sRuleId2, true);
        deleteSecurityRule(sRuleId2);
        fetchSecurityRule(sRuleId1, false);
        fetchSecurityRule(sRuleId2, false);
    }

    private void deleteSecurityRule(String sRuleId) {
        String url = base + "/security-group-rules/" + sRuleId;
        ITNeutronE2E.test_delete(url, "Security Rule Delete Failed");
    }

    private void fetchSecurityRule(String sRuleId, boolean isPositiveTest) {
        String sRuleFetchUrl = base + "/security-group-rules/" + sRuleId;
        String errorMsg = "";
        if(isPositiveTest) {
            errorMsg = "Security Rule Element Does Not Exists";
        } else {
            errorMsg = "Dangling Security Rule Element Exists";
        }
        ITNeutronE2E.test_fetch(sRuleFetchUrl, isPositiveTest, errorMsg);
    }

    public static void runTests(String base) {
        NeutronSecurityGroupTests securityGroup_tester = new NeutronSecurityGroupTests(base);
        securityGroup_tester.singleton_sg_create_test();
        securityGroup_tester.sg_update_test();
        securityGroup_tester.sg_element_get_test();
        securityGroup_tester.sg_element_get_with_query_test();
        securityGroup_tester.securityGroup_collection_get_test();
        securityGroup_tester.sg_delete_test();
        securityGroup_tester.sg_element_negative_get_test();
        securityGroup_tester.security_group_default_rule_test();
    }
}
