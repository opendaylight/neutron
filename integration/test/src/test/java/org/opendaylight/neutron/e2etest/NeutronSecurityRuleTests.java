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

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class NeutronSecurityRuleTests {
    String base;

    public NeutronSecurityRuleTests(String base) {
        this.base = base;
    }

    public void securityRule_collection_get_test() {
        String url = base + "/security-group-rules";
        ITNeutronE2E.test_fetch(url, "Security Rule Collection GET failed");
    }

    public String singleton_sr_create_test() {
        String url = base + "/security-group-rules";
        String content = " {\"security_group_rule\": " +
            "{\"remote_group_id\": null, \"direction\": \"ingress\", " +
            "\"remote_ip_prefix\": null, \"protocol\": \"tcp\", " +
            "\"ethertype\": \"IPv6\", \"tenant_id\": " +
            "\"00f340c7c3b34ab7be1fc690c05a0275\", \"port_range_max\": 77, " +
            "\"port_range_min\": 77, " +
            "\"id\": \"9b4be7fa-e56e-40fb-9516-1f0fa9185669\", " +
            "\"security_group_id\": " +
            "\"b60490fe-60a5-40be-af63-1d641381b784\"}}";
        ITNeutronE2E.test_create(url, content, "Security Rule Singleton Post Failed");
        return content;
    }

    public void singleton_sr_get_with_one_query_item_test(String createJsonString) {
        String url = base + "/security-group-rules";
        ITNeutronE2E.test_fetch_with_one_query_item(url, createJsonString, "security_group_rules");
    }

    public void multiple_sr_create_test() {
        String url = base + "/security-group-rules";
        String content = " {\"security_group_rules\": [" +
                "{" +
                "  \"id\": \"35fb0f34-c8d3-416d-a205-a2c75f7b8e22\"," +
                "  \"direction\": \"egress\"," +
                "  \"ethertype\": \"IPv6\"," +
                "  \"security_group_id\": \"70f1b157-e79b-44dc-85a8-7de0fc9f2aab\"," +
                "  \"tenant_id\": \"2640ee2ac2474bf3906e482047204fcb\"" +
                "}," +
                "{" +
                "  \"id\": \"63814eed-bc12-4fe4-8b17-2af178224c71\"," +
                "  \"direction\": \"egress\"," +
                "  \"ethertype\": \"IPv4\"," +
                "  \"security_group_id\": \"70f1b157-e79b-44dc-85a8-7de0fc9f2aab\"," +
                "  \"tenant_id\": \"2640ee2ac2474bf3906e482047204fcb\"" +
                "}," +
                "{" +
                "  \"id\": \"ccb9823e-559b-4d84-b656-2739f8e56d89\"," +
                "  \"direction\": \"ingress\"," +
                "  \"ethertype\": \"IPv6\"," +
                "  \"remote_group_id\": \"70f1b157-e79b-44dc-85a8-7de0fc9f2aab\"," +
                "  \"security_group_id\": \"70f1b157-e79b-44dc-85a8-7de0fc9f2aab\"," +
                "  \"tenant_id\": \"2640ee2ac2474bf3906e482047204fcb\"" +
                "}," +
                "{" +
                "  \"id\": \"fbc3f809-7378-40a4-822f-7a70f6ccba98\"," +
                "  \"direction\": \"ingress\"," +
                "  \"ethertype\": \"IPv4\"," +
                "  \"remote_group_id\": \"70f1b157-e79b-44dc-85a8-7de0fc9f2aab\"," +
                "  \"security_group_id\": \"70f1b157-e79b-44dc-85a8-7de0fc9f2aab\"," +
                "  \"tenant_id\": \"2640ee2ac2474bf3906e482047204fcb\"" +
                "}" +
                "]}";
        ITNeutronE2E.test_create(url, content, "Security Rule Multiple Post Failed");
    }

    public void singleton_sr_modify_test() {
        String url = base + "/security-group-rules/9b4be7fa-e56e-40fb-9516-1f0fa9185669";
        String content = " {\"security_group_rule\": " +
            "{\"remote_group_id\": null, \"direction\": \"egress\", " +
            "\"remote_ip_prefix\": null, \"protocol\": \"tcp\", " +
            "\"ethertype\": \"IPv6\", \"tenant_id\": " +
            "\"00f340c7c3b34ab7be1fc690c05a0275\", \"port_range_max\": 77, " +
            "\"port_range_min\": 77, " +
            "\"id\": \"9b4be7fa-e56e-40fb-9516-1f0fa9185669\", " +
            "\"security_group_id\": " +
            "\"b60490fe-60a5-40be-af63-1d641381b784\"}}";
        ITNeutronE2E.test_modify(url, content, "Security Rule Singleton Put Failed");
    }

    public void sr_element_get_test() {
        String url = base + "/security-group-rules/9b4be7fa-e56e-40fb-9516-1f0fa9185669";
        ITNeutronE2E.test_fetch(url, true, "Security Rule Element Get Failed");
    }

    public void sr_element_get_with_query_test() {
        String url = base + "/security-group-rules/9b4be7fa-e56e-40fb-9516-1f0fa9185669" +
            "?fields=id&fields=direction&fields=protocol&fields=port_range_min" +
            "&fields=port_range_max&fields=ethertype&fields=remote_ip_prefix" +
            "&fields=remote_group_id&fields=security_group_id&fields=tenant_id" +
            "&fields=limit&fields=marker&fields=page_reverse";
        ITNeutronE2E.test_fetch(url, true, "Security Rule Element Get With Query Failed");
    }

    public void sr_delete_test() {
        String url = base + "/security-group-rules/9b4be7fa-e56e-40fb-9516-1f0fa9185669";
        ITNeutronE2E.test_delete(url, "Security Rule Delete Failed");
    }

    public void sr_element_negative_get_test() {
        String url = base + "/security-group-rules/9b4be7fa-e56e-40fb-9516-1f0fa9185669";
        ITNeutronE2E.test_fetch(url, false, "Security Rule Element Negative Get Failed");
    }

    public void bug5478_rule_delete_negative_test() {
        String url = base + "/security-group-rules/9b4be7fa-e56e-40fb-9516-1f0fa9185669";
        ITNeutronE2E.test_delete_404(url, "Security Rule Delete 404 Failed");
    }

    public void bug4043_ipv4_test() {
        String url = base + "/security-group-rules";
        String content = " {\"security_group_rule\": " +
            "{\"remote_group_id\": null, \"direction\": \"ingress\", " +
            "\"remote_ip_prefix\": \"10.10.10.10/16\", \"protocol\": \"tcp\", " +
            "\"ethertype\": \"IPv4\", \"tenant_id\": " +
            "\"00f340c7c3b34ab7be1fc690c05a0275\", \"port_range_max\": 77, " +
            "\"port_range_min\": 77, " +
            "\"id\": \"01234567-0123-0123-0123-01234567890a\", " +
            "\"security_group_id\": " +
            "\"b60490fe-60a5-40be-af63-1d641381b784\"}}";
        ITNeutronE2E.test_create(url, content, "Security Rule bug4043 IPv4 Failed");

        url = url + "/01234567-0123-0123-0123-01234567890a";
        ITNeutronE2E.test_delete(url, "Security Rule Delete Failed");
    }

    public void bug4043_ipv6_test() {
        String url = base + "/security-group-rules";
        String content = " {\"security_group_rule\": " +
            "{\"remote_group_id\": null, \"direction\": \"ingress\", " +
            "\"remote_ip_prefix\": \"fe80::1/10\", \"protocol\": \"tcp\", " +
            "\"ethertype\": \"IPv6\", \"tenant_id\": " +
            "\"00f340c7c3b34ab7be1fc690c05a0275\", \"port_range_max\": 77, " +
            "\"port_range_min\": 77, " +
            "\"id\": \"01234567-0123-0123-0123-01234567890a\", " +
            "\"security_group_id\": " +
            "\"b60490fe-60a5-40be-af63-1d641381b784\"}}";
        ITNeutronE2E.test_create(url, content, "Security Rule Bug4043 IPv6 Failed");

        url = url + "/01234567-0123-0123-0123-01234567890a";
        ITNeutronE2E.test_delete(url, "Security Rule Delete Failed");
    }

    public static void runTests(String base) {
        NeutronSecurityRuleTests securityRule_tester = new NeutronSecurityRuleTests(base);
        String createJsonString = securityRule_tester.singleton_sr_create_test();
        securityRule_tester.singleton_sr_get_with_one_query_item_test(createJsonString);
        securityRule_tester.multiple_sr_create_test();
        securityRule_tester.singleton_sr_modify_test();
        securityRule_tester.sr_element_get_test();
        securityRule_tester.sr_element_get_with_query_test();
        securityRule_tester.securityRule_collection_get_test();
        securityRule_tester.sr_delete_test();
        securityRule_tester.sr_element_negative_get_test();
        securityRule_tester.bug5478_rule_delete_negative_test();
        securityRule_tester.bug4043_ipv4_test();
        securityRule_tester.bug4043_ipv6_test();
    }
}
