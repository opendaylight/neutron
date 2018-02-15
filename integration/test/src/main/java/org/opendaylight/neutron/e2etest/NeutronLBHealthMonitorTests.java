/*
 * Copyright (C) 2015 IBM, Inc.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.e2etest;

public class NeutronLBHealthMonitorTests {
    String base;

    public NeutronLBHealthMonitorTests(String base) {
        this.base = base;
    }

    public void healthMonitor_collection_get_test() {
        String url = base + "/lbaas/healthmonitors";
        HttpUtils.test_fetch(url, "LB Health Monitor Collection GET failed");
    }

    public String singleton_lb_healthMonitor_create_test() {
        String url = base + "/lbaas/healthmonitors";
        String content = " { \"healthmonitor\": { \"admin_state_up\": true,"
                + "\"delay\": 1, \"expected_codes\": \"200,201,202\"," + "\"http_method\": \"GET\","
                + "\"id\": \"0a9ac99d-0a09-4b18-8499-a0796850279a\"," + "\"max_retries\": 5,"
                + "\"pools\": [ { \"id\": \"74aa2010-a59f-4d35-a436-60a6da882819\" } ],"
                + "\"tenant_id\": \"6f3584d5754048a18e30685362b88411\"," + "\"timeout\": 1, \"type\": \"HTTP\","
                + "\"url_path\": \"/index.html\" } }";
        HttpUtils.test_create(url, content, "Singleton LB Health Monitor Post Failed NB");
        return content;
    }

    public void singleton_lb_healthMonitor_with_one_query_item_test(String createJsonString) {
        String url = base + "/lbaas/healthmonitors";
        HttpUtils.test_fetch_with_one_query_item(url, createJsonString, "healthmonitors");
    }

    public void healthMonitor_update_test() {
        String url = base + "/lbaas/healthmonitors/0a9ac99d-0a09-4b18-8499-a0796850279a";
        String content = " { \"healthmonitor\": { \"admin_state_up\": false,"
                + "\"delay\": 2, \"expected_codes\": \"200\"," + "\"http_method\": \"POST\","
                + "\"id\": \"0a9ac99d-0a09-4b18-8499-a0796850279a\"," + "\"max_retries\": 2,"
                + "\"pools\": [ { \"id\": \"74aa2010-a59f-4d35-a436-60a6da882819\" } ],"
                + "\"tenant_id\": \"6f3584d5754048a18e30685362b88411\"," + "\"timeout\": 2, \"type\": \"HTTP\","
                + "\"url_path\": \"/page.html\" } }";
        HttpUtils.test_modify(url, content, "LB Health Monitor Put Failed");
    }

    public void healthMonitor_element_get_test() {
        String url = base + "/lbaas/healthmonitors/0a9ac99d-0a09-4b18-8499-a0796850279a";
        HttpUtils.test_fetch(url, true, "LB Health Monitor Element Get Failed");
    }

    public void healthMonitor_element_get_with_query_test() {
        String url = base + "/lbaas/healthmonitors/0a9ac99d-0a09-4b18-8499-a0796850279a"
                + "?fields=id&fields=tenant_id&fields=type&fields=delay&fields=timeout"
                + "&fields=max_retries&fields=http_method&fields=url_path&fields=admin_state_up"
                + "&fields=expected_codes&fields=limit&fields=marker&fields=page_reverse";
        HttpUtils.test_fetch(url, true, "LB Health Monitor Element Get Failed");
    }

    public void healthMonitor_element_negative_get_test() {
        String url = base + "/lbaas/healthmonitors/0a9ac99d-0a09-4b18-8499-a0796850279a";
        HttpUtils.test_fetch(url, false, "LB Health Monitor Element Negative Get Failed");
    }

    public void healthMonitor_delete_test() {
        String url = base + "/lbaas/healthmonitors/0a9ac99d-0a09-4b18-8499-a0796850279a";
        HttpUtils.test_delete(url, "LB Health Monitor Element Delete Failed");
    }

    public static void runTests(String base) {
        NeutronLBHealthMonitorTests healthMonitorTester = new NeutronLBHealthMonitorTests(base);
        String createJsonString = healthMonitorTester.singleton_lb_healthMonitor_create_test();
        healthMonitorTester.singleton_lb_healthMonitor_with_one_query_item_test(createJsonString);
        healthMonitorTester.healthMonitor_update_test();
        healthMonitorTester.healthMonitor_element_get_test();
        healthMonitorTester.healthMonitor_element_get_with_query_test();
        healthMonitorTester.healthMonitor_collection_get_test();
        healthMonitorTester.healthMonitor_delete_test();
        healthMonitorTester.healthMonitor_element_negative_get_test();
    }
}
