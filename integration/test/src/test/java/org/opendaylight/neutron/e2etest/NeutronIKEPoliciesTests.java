/*
 * Copyright (C) 2015 IBM, Inc.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.e2etest;

public class NeutronIKEPoliciesTests {
    String base;

    public NeutronIKEPoliciesTests(String base) {
        this.base = base;
    }

    public void ikePolicy_collection_get_test() {
        String url = base + "/vpn/ikepolicies";
        HttpUtils.test_fetch(url, "IKE Policy GET failed");
    }

    public String singleton_ikePolicy_create_test() {
        String url = base + "/vpn/ikepolicies";
        String content = " { \"ikepolicy\": { \"name\": \"ikepolicy1\","
                + "\"tenant_id\": \"ccb81365fe36411a9011e90491fe1330\"," + "\"auth_algorithm\": \"sha1\","
                + "\"encryption_algorithm\": \"aes-128\"," + "\"pfs\": \"group5\","
                + "\"phase1_negotiation_mode\": \"main\"," + "\"lifetime\": { \"units\": \"seconds\","
                + "\"value\": 7200}," + "\"ike_version\": \"v1\"," + "\"id\": \"5522aff7-1b3c-48dd-9c3c-b50f016b73db\","
                + "\"description\": \"\" } }";
        HttpUtils.test_create(url, content, "IKE Policy POST failed");
        return content;
    }

    public void singleton_ikePolicy_get_with_one_query_item_test(String createJsonString) {
        String url = base + "/vpn/ikepolicies";
        HttpUtils.test_fetch_with_one_query_item(url, createJsonString, "ikepolicies");
    }

    public void ikePolicy_update_test() {
        String url = base + "/vpn/ikepolicies/5522aff7-1b3c-48dd-9c3c-b50f016b73db";
        String content = " { \"ikepolicy\": { \"name\": \"ikepolicy1\","
                + "\"tenant_id\": \"ccb81365fe36411a9011e90491fe1330\"," + "\"auth_algorithm\": \"sha1\","
                + "\"encryption_algorithm\": \"aes-256\"," + "\"pfs\": \"group5\","
                + "\"phase1_negotiation_mode\": \"main\","
                + "\"lifetime\": { \"units\": \"seconds\", \"value\": 3600 }," + "\"ike_version\": \"v1\","
                + "\"id\": \"5522aff7-1b3c-48dd-9c3c-b50f016b73db\"," + "\"description\": \"\" } }";
        HttpUtils.test_modify(url, content, "IKE Policy PUT failed");
    }

    public void ikePolicy_element_get_test() {
        String url = base + "/vpn/ikepolicies/5522aff7-1b3c-48dd-9c3c-b50f016b73db";
        HttpUtils.test_fetch(url, true, "IKE Policy Element GET failed");
    }

    public void ikePolicy_element_get_with_query_test() {
        String url = base + "/vpn/ikepolicies/5522aff7-1b3c-48dd-9c3c-b50f016b73db"
                + "?fields=id&fields=tenant_id&fields=name&fields=description&fields=pfs"
                + "&fields=auth_algorithm&fields=phase1_negotiation_mode"
                + "&fields=ike_version&fields=encryption_algorithm";
        HttpUtils.test_fetch(url, true, "IKE Policy Element GET With Query failed");
    }

    public void ikePolicy_delete_test() {
        String url = base + "/vpn/ikepolicies/5522aff7-1b3c-48dd-9c3c-b50f016b73db";
        HttpUtils.test_delete(url, "IKE Policy DELETE failed");
    }

    public void ikePolicy_element_negative_get_test() {
        String url = base + "/vpn/ikepolicies/5522aff7-1b3c-48dd-9c3c-b50f016b73db";
        HttpUtils.test_fetch(url, false, "IKE Policy Element Negative GET failed");
    }

    public static void runTests(String base) {
        NeutronIKEPoliciesTests ikePolicyTester = new NeutronIKEPoliciesTests(base);
        String createJsonString = ikePolicyTester.singleton_ikePolicy_create_test();
        ikePolicyTester.singleton_ikePolicy_get_with_one_query_item_test(createJsonString);
        ikePolicyTester.ikePolicy_update_test();
        ikePolicyTester.ikePolicy_element_get_test();
        ikePolicyTester.ikePolicy_element_get_with_query_test();
        ikePolicyTester.ikePolicy_collection_get_test();
        ikePolicyTester.ikePolicy_delete_test();
        ikePolicyTester.ikePolicy_element_negative_get_test();
    }
}
