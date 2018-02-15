/*
 * Copyright (C) 2015 IBM, Inc.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.e2etest;

public class NeutronIpSecPoliciesTests {
    String base;

    public NeutronIpSecPoliciesTests(String base) {
        this.base = base;
    }

    public void ipsecPolicy_collection_get_test() {
        String url = base + "/vpn/ipsecpolicies";
        HttpUtils.test_fetch(url, "IPSEC Policy Collection GET failed");
    }

    public String singleton_ipsecPolicy_create_test() {
        String url = base + "/vpn/ipsecpolicies";
        String content = " { \"ipsecpolicy\": { \"name\": \"ipsecpolicy1\"," + "\"transform_protocol\": \"esp\","
                + "\"auth_algorithm\": \"sha1\"," + "\"encapsulation_mode\": \"tunnel\","
                + "\"encryption_algorithm\": \"aes-128\"," + "\"pfs\": \"group5\","
                + "\"tenant_id\": \"ccb81365fe36411a9011e90491fe1330\","
                + "\"lifetime\": { \"units\": \"seconds\", \"value\": 7200 },"
                + "\"id\": \"5291b189-fd84-46e5-84bd-78f40c05d69c\"," + "\"description\": \"\" } }";
        HttpUtils.test_create(url, content, "IPSEC Policy POST failed");
        return content;
    }

    public void singleton_ipsecPolicy_get_with_one_query_item_test(String createJsonString) {
        String url = base + "/vpn/ipsecpolicies";
        HttpUtils.test_fetch_with_one_query_item(url, createJsonString, "ipsecpolicies");
    }

    public void ipsecPolicy_update_test() {
        String url = base + "/vpn/ipsecpolicies/5291b189-fd84-46e5-84bd-78f40c05d69c";
        String content = " { \"ipsecpolicy\": { \"name\": \"ipsecpolicy1\"," + "\"transform_protocol\": \"esp\","
                + "\"auth_algorithm\": \"sha1\"," + "\"encapsulation_mode\": \"tunnel\","
                + "\"encryption_algorithm\": \"aes-128\"," + "\"pfs\": \"group14\","
                + "\"tenant_id\": \"ccb81365fe36411a9011e90491fe1330\","
                + "\"lifetime\": { \"units\": \"seconds\", \"value\": 3600 },"
                + "\"id\": \"5291b189-fd84-46e5-84bd-78f40c05d69c\"," + "\"description\": \"\" } }";
        HttpUtils.test_modify(url, content, "IPSEC Policy PUT failed");
    }

    public void ipsecPolicy_element_get_test() {
        String url = base + "/vpn/ipsecpolicies/5291b189-fd84-46e5-84bd-78f40c05d69c";
        HttpUtils.test_fetch(url, true, "IPSEC Policy Element GET failed");
    }

    public void ipsecPolicy_element_get_with_query_test() {
        String url = base + "/vpn/ipsecpolicies/5291b189-fd84-46e5-84bd-78f40c05d69c"
                + "?fields=id&fields=tenant_id&fields=name&fields=description&fields=pfs"
                + "&fields=encapsulation_mode&fields=transform_protocol"
                + "&fields=auth_algorithm&fields=encryption_algorithm";
        HttpUtils.test_fetch(url, true, "IPSEC Policy Element GET With Query failed");
    }

    public void ipsecPolicy_delete_test() {
        String url = base + "/vpn/ipsecpolicies/5291b189-fd84-46e5-84bd-78f40c05d69c";
        HttpUtils.test_delete(url, "IPSEC Policy DELETE failed");
    }

    public void ipsecPolicy_element_negative_get_test() {
        String url = base + "/vpn/ipsecpolicies/5291b189-fd84-46e5-84bd-78f40c05d69c";
        HttpUtils.test_fetch(url, false, "IPSEC Policy Element Negative GET failed");
    }

    public static void runTests(String base) {
        NeutronIpSecPoliciesTests ipsecPolicyTester = new NeutronIpSecPoliciesTests(base);
        String createJsonString = ipsecPolicyTester.singleton_ipsecPolicy_create_test();
        ipsecPolicyTester.singleton_ipsecPolicy_get_with_one_query_item_test(createJsonString);
        ipsecPolicyTester.ipsecPolicy_update_test();
        ipsecPolicyTester.ipsecPolicy_element_get_test();
        ipsecPolicyTester.ipsecPolicy_element_get_with_query_test();
        ipsecPolicyTester.ipsecPolicy_collection_get_test();
        ipsecPolicyTester.ipsecPolicy_delete_test();
        ipsecPolicyTester.ipsecPolicy_element_negative_get_test();
    }
}
