/*
 * Copyright (C) 2016 Intel Corp.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.e2etest;

public class NeutronL2GatewayConnectionTests {
    String base;

    public NeutronL2GatewayConnectionTests(String base) {
        this.base = base;
    }

    public void l2gateway_connection_collection_get_test() {
        String url = base + "/l2gateway-connections";
        HttpUtils.test_fetch(url, "Firewall Collection GET failed");
    }

    public String singleton_l2gateway_connection_create_test() {
        String url = base + "/l2gateway-connections";
        String content = " { \"l2gateway_connection\": { \"port_id\": \"9ea656c7-c9b8-4474-94f3-3b0bc741d9a9\","
                + "\"gateway_id\": \"d3590f37-b072-4358-9719-71964d84a31c\", \"segmentation_id\": 100,"
                + "\"network_id\": \"c69933c1-b472-44f9-8226-30dc4ffd454c\","
                + "\"id\": \"3b0ef8f4-82c7-44d4-a4fb-6177f9a21977\","
                + "\"tenant_id\": \"45977fa2dbd7482098dd68d0d8970117\" } }";
        HttpUtils.test_create(url, content, "L2 Gateway Connection Singleton Post Failed");
        return content;
    }

    public void singleton_l2gateway_connection_get_with_one_query_test(String createJsonString) {
        String url = base + "/l2gateway-connections";
        HttpUtils.test_fetch_with_one_query_item(url, createJsonString, "l2gateway_connections");
    }

    public void l2gateway_connection_element_get_test() {
        String url = base + "/l2gateway-connections/3b0ef8f4-82c7-44d4-a4fb-6177f9a21977";
        HttpUtils.test_fetch(url, true, "L2 Gateway Connection Element Get Failed");
    }

    public void l2gateway_connection_element_get_with_query_test() {
        String url = base + "/l2gateway-connections/3b0ef8f4-82c7-44d4-a4fb-6177f9a21977"
                + "?fields=tenant_id&fields=connection_id&fields=gateway_id"
                + "&fields=network_id&fields=port_id&fields=segmentation_id";
        HttpUtils.test_fetch(url, true, "L2 Gateway Connection Element Get With Query Failed");
    }

    public void l2gateway_connection_delete_test() {
        String url = base + "/l2gateway-connections/3b0ef8f4-82c7-44d4-a4fb-6177f9a21977";
        HttpUtils.test_delete(url, "L2 Gateway Connection Delete Failed");
    }

    public void l2gateway_connection_element_negative_get_test() {
        String url = base + "/l2gateway-connections/3b0ef8f4-82c7-44d4-a4fb-6177f9a21977";
        HttpUtils.test_fetch(url, false, "L2 Gateway Connection Element Negative Get Failed");
    }

    public void l2gateway_connection_create_without_port_test() {
        String url = base + "/l2gateway-connections";
        String content = " { \"l2gateway_connection\": { "
                + "\"gateway_id\": \"5227c228-6bba-4bbe-bdb8-6942768ff0f1\", " + "\"segmentation_id\": 100,"
                + "\"network_id\": \"9227c228-6bba-4bbe-bdb8-6942768ff0f1\","
                + "\"id\": \"5227c228-6bba-4bbe-bdb8-6942768ff0e1\","
                + "\"tenant_id\": \"de0a7495-05c4-4be0-b796-1412835c6820\" } }";
        HttpUtils.test_create(url, content, "L2 Gateway Connection with No PortId - Singleton Post Failed");
    }

    public void l2gateway_connection_element_get_without_port_test() {
        String url = base + "/l2gateway-connections/5227c228-6bba-4bbe-bdb8-6942768ff0e1";
        HttpUtils.test_fetch(url, true, "L2 Gateway Connection with No PortId - Element Get Failed");
    }

    public void l2gateway_connection_delete_without_port_test() {
        String url = base + "/l2gateway-connections/5227c228-6bba-4bbe-bdb8-6942768ff0e1";
        HttpUtils.test_delete(url, "L2 Gateway Connection with No PortId - Delete Failed");
    }

    public static void runTests(String base) {
        NeutronL2GatewayConnectionTests l2gatewayConnectionTester = new NeutronL2GatewayConnectionTests(base);
        String createJsonString = l2gatewayConnectionTester.singleton_l2gateway_connection_create_test();
        l2gatewayConnectionTester.singleton_l2gateway_connection_get_with_one_query_test(createJsonString);
        l2gatewayConnectionTester.l2gateway_connection_element_get_test();
        l2gatewayConnectionTester.l2gateway_connection_element_get_with_query_test();
        l2gatewayConnectionTester.l2gateway_connection_collection_get_test();
        l2gatewayConnectionTester.l2gateway_connection_delete_test();
        l2gatewayConnectionTester.l2gateway_connection_element_negative_get_test();
        l2gatewayConnectionTester.l2gateway_connection_create_without_port_test();
        l2gatewayConnectionTester.l2gateway_connection_element_get_without_port_test();
        l2gatewayConnectionTester.l2gateway_connection_delete_without_port_test();
    }
}
