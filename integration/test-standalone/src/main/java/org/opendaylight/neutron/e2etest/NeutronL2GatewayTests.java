/*
 * Copyright (C) 2016 Intel Corp.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.e2etest;

public class NeutronL2GatewayTests {
    String base;

    public NeutronL2GatewayTests(String base) {
        this.base = base;
    }

    public void l2gateway_collection_get_test() {
        String url = base + "/l2-gateways?limit=5&name=gateway1";
        HttpUtils.test_fetch(url, "Firewall Collection GET failed");
    }

    public String singleton_l2gateway_create_test() {
        String url = base + "/l2-gateways";
        String content = "{ \"l2_gateway\": { \"name\": \"gateway1\","
                + "\"id\": \"3b0ef8f4-82c7-44d4-a4fb-6177f9a21977\","
                + "\"tenant_id\": \"45977fa2dbd7482098dd68d0d8970117\","
                + "\"devices\": [{ \"device_name\": \"device1\"," + "\"id\": \"0a24b09a-88a1-4f2c-94e9-92515972a704\","
                + "\"interfaces\": [{\"name\": \"interface1\", \"segmentation_id\": [100] }] }]" + "} }";
        HttpUtils.test_create(url, content, "L2 Gateway Singleton Post Failed");
        return content;
    }

    public void singleton_l2gateway_get_with_one_query_item_test(String createJsonString) {
        String url = base + "/l2-gateways";
        HttpUtils.test_fetch_with_one_query_item(url, createJsonString, "l2_gateways");
    }

    public void l2gateway_modify_test() {
        String url = base + "/l2-gateways/3b0ef8f4-82c7-44d4-a4fb-6177f9a21977";
        String content = "{ \"l2_gateway\": { \"name\": \"gateway1\","
                + "\"id\": \"3b0ef8f4-82c7-44d4-a4fb-6177f9a21977\","
                + "\"tenant_id\": \"45977fa2dbd7482098dd68d0d8970117\","
                + "\"devices\": [{ \"device_name\": \"device1\"," + "\"id\": \"0a24b09a-88a1-4f2c-94e9-92515972a704\","
                + "\"interfaces\": [{\"name\": \"interface1\", \"segmentation_id\": [100, 50] }] }]" + "} }";
        HttpUtils.test_modify(url, content, "L2 Gateway  Singleton Put Failed");
    }

    public void l2gateway_element_get_test() {
        String url = base + "/l2-gateways/3b0ef8f4-82c7-44d4-a4fb-6177f9a21977";
        HttpUtils.test_fetch(url, true, "L2 Gateway  Element Get Failed");
    }

    public void l2gateway_element_get_with_query_test() {
        String url = base + "/l2-gateways/3b0ef8f4-82c7-44d4-a4fb-6177f9a21977"
                + "?fields=tenant_id&fields=id&fields=name" + "&fields=devices";
        HttpUtils.test_fetch(url, true, "L2 Gateway Element Get With Query Failed");
    }

    public void l2gateway_delete_test() {
        String url = base + "/l2-gateways/3b0ef8f4-82c7-44d4-a4fb-6177f9a21977";
        HttpUtils.test_delete(url, "L2 Gateway Delete Failed");
    }

    public void l2gateway_element_negative_get_test() {
        String url = base + "/l2-gateways/3b0ef8f4-82c7-44d4-a4fb-6177f9a21977";
        HttpUtils.test_fetch(url, false, "L2 Gateway Element Negative Get Failed");
    }

    public static void runTests(String base) {
        NeutronL2GatewayTests l2gatewayTester = new NeutronL2GatewayTests(base);
        String createJsonString = l2gatewayTester.singleton_l2gateway_create_test();
        l2gatewayTester.singleton_l2gateway_get_with_one_query_item_test(createJsonString);
        l2gatewayTester.l2gateway_element_get_test();
        l2gatewayTester.l2gateway_element_get_with_query_test();
        l2gatewayTester.l2gateway_collection_get_test();
        l2gatewayTester.l2gateway_modify_test();
        l2gatewayTester.l2gateway_delete_test();
        l2gatewayTester.l2gateway_element_negative_get_test();
    }
}
