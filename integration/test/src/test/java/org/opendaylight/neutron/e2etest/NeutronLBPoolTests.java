/*
 * Copyright (C) 2015 IBM, Inc.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.e2etest;

public class NeutronLBPoolTests {
    String base;

    public NeutronLBPoolTests(String base) {
        this.base = base;
    }

    public void pool_collection_get_test() {
        String url = base + "/lbaas/pools";
        HttpUtils.test_fetch(url, "LB Pool Collection GET failed");
    }

    //TODO handle SB check
    public String singleton_lb_pool_create_test() {
        String url = base + "/lbaas/pools";
        String content = "{ \"pool\": { " + "\"admin_state_up\": true, " + "\"description\": \"simple pool\", "
                + "\"healthmonitor_id\": null, " + "\"id\": \"12ff63af-4127-4074-a251-bcb2ecc53ebe\", "
                + "\"lb_algorithm\": \"ROUND_ROBIN\", " + "\"listeners\": [ { "
                + "\"id\": \"39de4d56-d663-46e5-85a1-5b9d5fa17829\" } ], " + "\"members\": [], "
                + "\"name\": \"pool1\", " + "\"protocol\": \"HTTP\", " + "\"session_persistence\": { "
                + "\"cookie_name\": \"my_cookie\", " + "\"type\": \"APP_COOKIE\" }, "
                + "\"tenant_id\": \"b7c1a69e88bf4b21a8148f787aef2081\" } }";
        HttpUtils.test_create(url, content, "Singleton LB Pool Post Failed NB");
        return content;
    }

    public void singleton_lb_pool_get_with_one_query_item_test(String createJsonString) {
        String url = base + "/lbaas/pools";
        HttpUtils.test_fetch_with_one_query_item(url, createJsonString, "pools");
    }

    public void pool_update_test() {
        String url = base + "/lbaas/pools/12ff63af-4127-4074-a251-bcb2ecc53ebe";
        String content = " { \"pool\": { \"admin_state_up\": false," + "\"description\": \"pool two\","
                + "\"healthmonitor_id\": null," + "\"id\": \"12ff63af-4127-4074-a251-bcb2ecc53ebe\","
                + "\"lb_algorithm\": \"LEAST_CONNECTIONS\"," + "\"listeners\": [ {"
                + "\"id\": \"39de4d56-d663-46e5-85a1-5b9d5fa17829\" } ]," + "\"members\": []," + "\"name\": \"pool2\","
                + "\"protocol\": \"HTTP\"," + "\"session_persistence\": { \"cookie_name\": null,"
                + "\"type\": \"HTTP_COOKIE\" }," + "\"tenant_id\": \"1a3e005cf9ce40308c900bcb08e5320c\" } }";
        HttpUtils.test_modify(url, content, "LB Pool Put Failed");
    }

    public void pool_element_get_test() {
        String url = base + "/lbaas/pools/12ff63af-4127-4074-a251-bcb2ecc53ebe";
        HttpUtils.test_fetch(url, true, "LB Pool Element Get Failed");
    }

    public void pool_element_get_with_query_test() {
        String url = base + "/lbaas/pools/12ff63af-4127-4074-a251-bcb2ecc53ebe"
                + "?fields=id&fields=tenant_id&fields=name&fields=description"
                + "&fields=lb_algorithm&fields=protocol&fields=healthmonitor_id"
                + "&fields=members&fields=admin_state_up&fields=limit&fields=marker" + "&fields=page_reverse";
        HttpUtils.test_fetch(url, true, "LB Pool Element Get With Query Failed");
    }

    public void pool_element_negative_get_test() {
        String url = base + "/lbaas/pools/12ff63af-4127-4074-a251-bcb2ecc53ebe";
        HttpUtils.test_fetch(url, false, "LB Pool Element Negative Get Failed");
    }

    public void pool_delete_test() {
        String url = base + "/lbaas/pools/12ff63af-4127-4074-a251-bcb2ecc53ebe";
        HttpUtils.test_delete(url, "LB Pool Element Delete Failed");
    }

    public static void runTests(String base) {
        NeutronLBPoolTests poolTester = new NeutronLBPoolTests(base);
        String createJsonString = poolTester.singleton_lb_pool_create_test();
        poolTester.singleton_lb_pool_get_with_one_query_item_test(createJsonString);
        poolTester.pool_update_test();
        poolTester.pool_element_get_test();
        poolTester.pool_element_get_with_query_test();
        poolTester.pool_collection_get_test();
        poolTester.pool_delete_test();
        poolTester.pool_element_negative_get_test();
        // needed for pool member testing
        poolTester.singleton_lb_pool_create_test();
    }
}
