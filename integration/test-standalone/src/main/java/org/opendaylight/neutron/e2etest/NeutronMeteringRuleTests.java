/*
 * Copyright (C) 2015 IBM, Inc.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.e2etest;

public class NeutronMeteringRuleTests {
    String base;

    public NeutronMeteringRuleTests(String base) {
        this.base = base;
    }

    public void meteringRule_collection_get_test() {
        String url = base + "/metering/metering-label-rules";
        HttpUtils.test_fetch(url, "Metering Rule Collection GET failed");
    }

    //TODO handle SB check
    public void singleton_meteringRule_create_test() {
        String url = base + "/metering/metering-label-rules";
        String content = "{ \"metering_label_rule\": { " + "\"remote_ip_prefix\": \"10.0.1.0/24\", "
                + "\"direction\": \"ingress\", " + "\"metering_label_id\": \"bc91b832-8465-40a7-a5d8-ba87de442266\", "
                + "\"id\": \"00e13b58-b4f2-4579-9c9c-7ac94615f9ae\", " + "\"excluded\": false } }";
        HttpUtils.test_create(url, content, "Singleton Metering Rule Post Failed NB");
    }

    public void meteringRule_element_get_test() {
        String url = base + "/metering/metering-label-rules/00e13b58-b4f2-4579-9c9c-7ac94615f9ae";
        HttpUtils.test_fetch(url, true, "Metering Rule Element Get Failed");
    }

    public void meteringRule_element_get_with_query_test() {
        String url = base + "/metering/metering-label-rules/00e13b58-b4f2-4579-9c9c-7ac94615f9ae"
                + "?fields=id&fields=direction&fields=remote_ip_prefix&fields=metering_label_id";
        HttpUtils.test_fetch(url, true, "Metering Rule Element Get Failed");
    }

    public void meteringRule_delete_test() {
        String url = base + "/metering/metering-label-rules/00e13b58-b4f2-4579-9c9c-7ac94615f9ae";
        HttpUtils.test_delete(url, "Metering Rule Element Delete Failed");
    }

    public void meteringRule_element_negative_get_test() {
        String url = base + "/metering/metering-label-rules/00e13b58-b4f2-4579-9c9c-7ac94615f9ae";
        HttpUtils.test_fetch(url, false, "Metering Rule Element Negative Get Failed");
    }

    public void bug4224_ipv6_test() {
        String url = base + "/metering/metering-label-rules";
        String content = "{ \"metering_label_rule\": { " + "\"remote_ip_prefix\": \"fe80::1/10\", "
                + "\"direction\": \"ingress\", " + "\"metering_label_id\": \"bc91b832-8465-40a7-a5d8-ba87de442266\", "
                + "\"id\": \"00e13b58-b4f2-4579-9c9c-7ac94615f9ae\", " + "\"excluded\": false } }";
        HttpUtils.test_create(url, content, "Metering Rule Bug4224 IPv6 Failed NB");
        url = url + "/00e13b58-b4f2-4579-9c9c-7ac94615f9ae";
        HttpUtils.test_delete(url, "Metering Rule Element Delete Failed");
    }

    public static void runTests(String base) {
        NeutronMeteringRuleTests meteringRuleTester = new NeutronMeteringRuleTests(base);
        meteringRuleTester.singleton_meteringRule_create_test();
        meteringRuleTester.meteringRule_element_get_test();
        meteringRuleTester.meteringRule_element_get_with_query_test();
        meteringRuleTester.meteringRule_collection_get_test();
        meteringRuleTester.meteringRule_delete_test();
        meteringRuleTester.meteringRule_element_negative_get_test();
    }
}
