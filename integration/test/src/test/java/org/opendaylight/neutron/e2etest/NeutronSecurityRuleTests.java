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

    public void singleton_sr_create_test() {
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

    public void bug4550_sg_sr_test() {
        String sg_uuid = "d55c7ec4-6189-4cbf-b136-3059dc2afd37";
        String sg_url = base + "/security-groups";
        String sg_content = "{\"security_group\": {\"tenant_id\": " +
            "\"4693014ff2dd4485b32d778c4942b18f\", \"description\": \"\", " +
            "\"id\": \"" + sg_uuid + "\", " +
            "\"security_group_rules\": [], " +
            "\"name\": \"tempest-secgroup-bug4550-sg-sr-test\"}}";
        ITNeutronE2E.test_create(sg_url, sg_content, "Security Group Singleton Post Failed");

        String rule_uuid = "19a72e11-783c-4baf-bf49-ed8053956d5e";
        String rule_url = base + "/security-group-rules";
        String rule_content = " {\"security_group_rules\": [" +
                "{" +
                "  \"id\": \"" + rule_uuid + "\"," +
                "  \"direction\": \"egress\"," +
                "  \"ethertype\": \"IPv4\"," +
                "  \"security_group_id\": \"" + sg_uuid + "\"," +
                "  \"tenant_id\": \"4693014ff2dd4485b32d778c4942b18f\"" +
                "}" +
                "]}";
        ITNeutronE2E.test_create(rule_url, rule_content, "Security Rule bug4550 Failed");

        String sg_one_url = sg_url + "/" + sg_uuid;
        JsonObject jsonObject = ITNeutronE2E.test_fetch_gson(sg_one_url, "Security group bug4550 fetch");
        JsonObject sg = jsonObject.getAsJsonObject("security_group");
        JsonArray sgRules = sg.getAsJsonArray("security_group_rules");
        Assert.assertEquals("security rule bug4550 array size", 1, sgRules.size());
        Assert.assertEquals("security rule bug4550 uuid",
                            rule_uuid, sgRules.get(0).getAsJsonObject().get("id").getAsString());
    }

    public static void runTests(String base) {
        NeutronSecurityRuleTests securityRule_tester = new NeutronSecurityRuleTests(base);
        securityRule_tester.singleton_sr_create_test();
        securityRule_tester.multiple_sr_create_test();
        securityRule_tester.singleton_sr_modify_test();
        securityRule_tester.sr_element_get_test();
        securityRule_tester.sr_element_get_with_query_test();
        securityRule_tester.securityRule_collection_get_test();
        securityRule_tester.sr_delete_test();
        securityRule_tester.sr_element_negative_get_test();
        securityRule_tester.bug4043_ipv4_test();
        securityRule_tester.bug4043_ipv6_test();
        securityRule_tester.bug4550_sg_sr_test();
    }
}
