/*
 * Copyright (C) 2015 IBM, Inc.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.e2etest;

public class NeutronLBPoolMembersTests {
    String base;

    public NeutronLBPoolMembersTests(String base) {
        this.base = base;
    }

    public void pool_member_collection_get_test() {
        String url = base + "/lbaas/pools/12ff63af-4127-4074-a251-bcb2ecc53ebe/members";
        HttpUtils.test_fetch(url, "LB Pool Member Collection GET failed");
    }

    //TODO handle SB check
    public String singleton_lb_pool_member_create_test() {
        String url = base + "/lbaas/pools/12ff63af-4127-4074-a251-bcb2ecc53ebe/members";
        String content = " { \"member\": { \"address\": \"10.0.0.8\"," + "\"admin_state_up\": true,"
                + "\"id\": \"9a7aff27-fd41-4ec1-ba4c-3eb92c629313\"," + "\"protocol_port\": 80,"
                + "\"subnet_id\": \"013d3059-87a4-45a5-91e9-d721068ae0b2\","
                + "\"tenant_id\": \"1a3e005cf9ce40308c900bcb08e5320c\"," + "\"weight\": 1 } }";
        HttpUtils.test_create(url, content, "Singleton LB Pool Member Post Failed NB");
        return content;
    }

    public void singleton_lb_pool_member_get_with_one_query_item_test(String createJsonString) {
        String url = base + "/lbaas/pools/12ff63af-4127-4074-a251-bcb2ecc53ebe/members";
        HttpUtils.test_fetch_with_one_query_item(url, createJsonString, "members");
    }

    public void pool_member_update_test() {
        String url = base
                + "/lbaas/pools/12ff63af-4127-4074-a251-bcb2ecc53ebe/members/9a7aff27-fd41-4ec1-ba4c-3eb92c629313";
        String content = " { \"member\": { \"address\": \"10.0.0.8\"," + "\"admin_state_up\": false,"
                + "\"id\": \"9a7aff27-fd41-4ec1-ba4c-3eb92c629313\"," + "\"protocol_port\": 80,"
                + "\"subnet_id\": \"013d3059-87a4-45a5-91e9-d721068ae0b2\","
                + "\"tenant_id\": \"1a3e005cf9ce40308c900bcb08e5320c\"," + "\"weight\": 5 } }";
        HttpUtils.test_modify(url, content, "LB Pool Member Put Failed");
    }

    public void pool_member_element_get_test() {
        String url = base
                + "/lbaas/pools/12ff63af-4127-4074-a251-bcb2ecc53ebe/members/9a7aff27-fd41-4ec1-ba4c-3eb92c629313";
        HttpUtils.test_fetch(url, true, "LB Pool Member Element Get Failed");
    }

    public void pool_member_element_get_with_query_test() {
        String url = base
                + "/lbaas/pools/12ff63af-4127-4074-a251-bcb2ecc53ebe/members/9a7aff27-fd41-4ec1-ba4c-3eb92c629313"
                + "?fields=id&fields=tenant_id&fields=address&fields=protocol_port"
                + "&fields=weight&fields=subnet_id&fields=admin_state_up"
                + "&fields=limit&fields=marker&fields=page_reverse";
        HttpUtils.test_fetch(url, true, "LB Pool Member Element Get With Query Failed");
    }

    public void pool_member_element_negative_get_test() {
        String url = base
                + "/lbaas/pools/12ff63af-4127-4074-a251-bcb2ecc53ebe/members/9a7aff27-fd41-4ec1-ba4c-3eb92c629313";
        HttpUtils.test_fetch(url, false, "LB Pool Member Element Negative Get Failed");
    }

    public void pool_member_delete_test() {
        String url = base
                + "/lbaas/pools/12ff63af-4127-4074-a251-bcb2ecc53ebe/members/9a7aff27-fd41-4ec1-ba4c-3eb92c629313";
        HttpUtils.test_delete(url, "LB Pool Member Element Delete Failed");
    }

    public static void runTests(String base) {
        NeutronLBPoolMembersTests poolMemberTester = new NeutronLBPoolMembersTests(base);
        String createJsonString = poolMemberTester.singleton_lb_pool_member_create_test();
        poolMemberTester.singleton_lb_pool_member_get_with_one_query_item_test(createJsonString);
        poolMemberTester.pool_member_update_test();
        poolMemberTester.pool_member_element_get_test();
        poolMemberTester.pool_member_element_get_with_query_test();
        poolMemberTester.pool_member_collection_get_test();
        poolMemberTester.pool_member_delete_test();
        poolMemberTester.pool_member_element_negative_get_test();
    }
}
