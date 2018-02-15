/*
 * Copyright (C) 2017 Intel Corp.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.e2etest;

public class NeutronProjectIdTests {
    String base;

    public NeutronProjectIdTests(String base) {
        this.base = base;
    }

    public void collection_get_test() {
        String url = base + "/fw/firewalls";
        HttpUtils.test_fetch(url, "RevisionID Collection GET failed");
    }

    public String singleton_create_test() {
        String url = base + "/fw/firewalls";
        String content = " { \"firewall\": { \"admin_state_up\": true," + "\"description\": \"\","
                + "\"firewall_policy_id\": \"c69933c1-b472-44f9-8226-30dc4ffd454c\","
                + "\"id\": \"4b0ef8f4-82c7-44d4-a4fb-6177f9a21977\","
                + "\"name\": \"\","
                + "\"project_id\": \"45977fa2dbd7482098dd68d0d8970117\" } }";
        HttpUtils.test_create(url, content, "RevisionID Singleton Post Failed");
        return content;
    }

    public void singleton_get_with_one_query_item_test(String createJsonString) {
        String url = base + "/fw/firewalls";
        HttpUtils.test_fetch_with_one_query_item(url, createJsonString, "firewalls");
    }

    public void modify_test() {
        String url = base + "/fw/firewalls/4b0ef8f4-82c7-44d4-a4fb-6177f9a21977";
        String content = " { \"firewall\": { \"admin_state_up\": false," + "\"description\": \"\","
                + "\"firewall_policy_id\": \"c69933c1-b472-44f9-8226-30dc4ffd454c\","
                + "\"id\": \"4b0ef8f4-82c7-44d4-a4fb-6177f9a21977\","
                + "\"name\": \"\","
                + "\"project_id\": \"45977fa2dbd7482098dd68d0d8970117\" } }";
        HttpUtils.test_modify(url, content, "RevisionID Singleton Post Failed");
    }

    public void element_get_test() {
        String url = base + "/fw/firewalls/4b0ef8f4-82c7-44d4-a4fb-6177f9a21977";
        HttpUtils.test_fetch(url, true, "RevisionID Element Get Failed");
    }

    public void delete_test() {
        String url = base + "/fw/firewalls/4b0ef8f4-82c7-44d4-a4fb-6177f9a21977";
        HttpUtils.test_delete(url, "RevisionID Delete Failed");
    }

    public String singleton_create_test_with_tenant_id() {
        String url = base + "/fw/firewalls";
        String content = " { \"firewall\": { \"admin_state_up\": true," + "\"description\": \"\","
                + "\"firewall_policy_id\": \"c69933c1-b472-44f9-8226-30dc4ffd454c\","
                + "\"id\": \"5b0ef8f4-82c7-44d4-a4fb-6177f9a21977\","
                + "\"name\": \"\","
                + "\"tenant_id\": \"55988fb3dbd7482098dd68d0d8970228\","
                + "\"project_id\": \"45977fa2dbd7482098dd68d0d8970117\" } }";
        HttpUtils.test_create(url, content, "RevisionID Singleton Post Failed");
        return content;
    }

    public void delete_test_with_tenant_id() {
        String url = base + "/fw/firewalls/5b0ef8f4-82c7-44d4-a4fb-6177f9a21977";
        HttpUtils.test_delete(url, "RevisionID Delete Failed");
    }

    public static void runTests(String base) {
        NeutronProjectIdTests tester = new NeutronProjectIdTests(base);
        String createJsonString = tester.singleton_create_test();
        tester.singleton_get_with_one_query_item_test(createJsonString);
        tester.element_get_test();
        tester.collection_get_test();
        tester.modify_test();
        tester.delete_test();
        tester.singleton_create_test_with_tenant_id();
        tester.delete_test_with_tenant_id();
    }
}
