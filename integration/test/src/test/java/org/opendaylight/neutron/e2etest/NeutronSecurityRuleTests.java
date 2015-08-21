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

    public void sr_element_get_test() {
        String url = base + "/security-group-rules/9b4be7fa-e56e-40fb-9516-1f0fa9185669";
        ITNeutronE2E.test_fetch(url, true, "Security Rule Element Get Failed");
    }

    public void sr_delete_test() {
        String url = base + "/security-group-rules/9b4be7fa-e56e-40fb-9516-1f0fa9185669";
        ITNeutronE2E.test_delete(url, "Security Rule Delete Failed");
    }

    public void sr_element_negative_get_test() {
        String url = base + "/security-group-rules/9b4be7fa-e56e-40fb-9516-1f0fa9185669";
        ITNeutronE2E.test_fetch(url, false, "Security Rule Element Negative Get Failed");
    }

    public static void runTests(String base) {
        NeutronSecurityRuleTests securityRule_tester = new NeutronSecurityRuleTests(base);
        securityRule_tester.securityRule_collection_get_test();
        securityRule_tester.singleton_sr_create_test();
        securityRule_tester.sr_element_get_test();
        securityRule_tester.sr_delete_test();
        securityRule_tester.sr_element_negative_get_test();
    }
}
