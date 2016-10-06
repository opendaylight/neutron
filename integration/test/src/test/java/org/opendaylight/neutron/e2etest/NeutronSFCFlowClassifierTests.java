/*
 * Copyright (c) 2016 Brocade Communications Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.neutron.e2etest;

/**
 * Created by Anil Vishnoi (vishnoianil@gmail.com)
 */
public class NeutronSFCFlowClassifierTests {
    String base;

    public NeutronSFCFlowClassifierTests(String base) {
        this.base = base;
    }

    private String singleton_sfc_flowclassifier_create_test() {
        String url = base + "/sfc/flowclassifiers";
        String content = "{ \"flowclassifier\": {" +
                " \"id\": \"444e5957-649f-477b-9e5b-f1f75b21c03c\", \"name\": \"Flow-Classifier\", " +
                "\"tenant_id\": \"9bacb3c5d39d41a79512987f338cf177\", "+
                " \"ethertype\": \"IPv4\", \"protocol\": \"tcp\", " +
                " \"source_port_range_min\": 100, " +
                " \"source_port_range_max\": 200, " +
                " \"destination_port_range_min\": 100, " +
                " \"destination_port_range_max\": 200, " +
                " \"source_ip_prefix\": \"10.0.0.0/24\", " +
                " \"destination_ip_prefix\": \"11.0.0.0/24\", " +
                " \"logical_source_port\": \"5e8e5957-649f-477b-9e5b-f1f75b21c03c\", " +
                " \"logical_destination_port\": \"6e8e5957-649f-477b-9e5b-f1f75b21c03c\", " +
                " \"l7_parameters\": [ {\"isSymmetric\":\"false\"}] " +
                " } } ";
        ITNeutronE2E.test_create(url, content,"Singleton SFC Flow Classifier Post Failed NB");
        return content;
    }

    private void singleton_sfc_flowclassifier_get_with_one_query_item_test(String createJsonString) {
        String url = base + "/sfc/flowclassifiers";
        ITNeutronE2E.test_fetch_with_one_query_item(url, createJsonString, "flowclassifiers");
    }

    private void sfc_flowclassifier_update_test() {
        String url = base + "/sfc/flowclassifiers/444e5957-649f-477b-9e5b-f1f75b21c03c";
        String content = "{ \"flowclassifier\": {" +
                " \"id\": \"444e5957-649f-477b-9e5b-f1f75b21c03c\", \"name\": \"Flow-Classifier\", " +
                "\"tenant_id\": \"9bacb3c5d39d41a79512987f338cf177\", "+
                " \"ethertype\": \"IPv4\", \"protocol\": \"tcp\", " +
                " \"source_port_range_min\": 100, " +
                " \"source_port_range_max\": 200, " +
                " \"destination_port_range_min\": 100, " +
                " \"destination_port_range_max\": 200, " +
                " \"source_ip_prefix\": \"12.0.0.0/24\", " +
                " \"destination_ip_prefix\": \"13.0.0.0/24\", " +
                " \"logical_source_port\": \"5e8e5957-649f-477b-9e5b-f1f75b21c03c\", " +
                " \"logical_destination_port\": \"6e8e5957-649f-477b-9e5b-f1f75b21c03c\", " +
                " \"l7_parameters\": [ {\"key\":\"value\"}] " +
                " } } ";
        ITNeutronE2E.test_modify(url, content,"SFC Flow Classifier Put Failed");
    }

    private void sfc_flowclassifier_delete_test() {
        String url = base + "/sfc/flowclassifiers/444e5957-649f-477b-9e5b-f1f75b21c03c";
        ITNeutronE2E.test_delete(url, "SFC Flow Classifier Element Delete Failed");
    }

    private void sfc_flowclassifier_element_negative_get_test() {
        String url = base + "/sfc/flowclassifiers/444e5957-649f-477b-9e5b-f1f75b21c03c";
        ITNeutronE2E.test_fetch(url, false, "SFC Flow Classifier Element Get Failed");
    }

    public static void runTests(String base) {
        NeutronSFCFlowClassifierTests sfc_fc_tester = new NeutronSFCFlowClassifierTests(base);
        String createJsonString = sfc_fc_tester.singleton_sfc_flowclassifier_create_test();
        sfc_fc_tester.singleton_sfc_flowclassifier_get_with_one_query_item_test(createJsonString);
        sfc_fc_tester.sfc_flowclassifier_update_test();
        sfc_fc_tester.sfc_flowclassifier_delete_test();
        sfc_fc_tester.sfc_flowclassifier_element_negative_get_test();
    }
}
