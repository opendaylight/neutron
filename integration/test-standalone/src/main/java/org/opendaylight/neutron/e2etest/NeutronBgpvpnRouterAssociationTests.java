/*
 * Copyright (c) 2020 Ericsson India Global Services Pvt Ltd. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.e2etest;

public class NeutronBgpvpnRouterAssociationTests {
    String base;

    public NeutronBgpvpnRouterAssociationTests(String base) {
        this.base = base;
    }

    public void bgpvpn_routerasso_collection_get_test() {
        String url = base + "/bgpvpn/routerassociations";
        HttpUtils.test_fetch(url, "BGPVPN router association collection GET failed");
    }

    public String singleton_bgpvpn_routerasso_create_test() {
        String url = base + "/bgpvpn/routerassociations";
        String content = "{ \"bgpvpn_router_association\" : { \"id\": \"8d819a7e-2f1b-4bb5-bbce-af476b237bc1\","
                + " \"bgpvpn-id\": \"b472f6eb-3ff1-4c4b-8f1b-e1032e10c372\","
                + " \"router-id\": \"3bbe9f3a-9c54-429f-8489-0611dbc99901\" } }";
        HttpUtils.test_create(url, content, "Singleton Bgpvpn router association Post Failed NB");
        return content;
    }

    public void singleton_bgpvpn_routerasso_get_with_one_query_item_test(String createJsonString) {
        String url = base + "/bgpvpn/routerassociations";
        HttpUtils.test_fetch_with_one_query_item(url, createJsonString, "bgpvpn_router_associations");
    }

    public void bgpvpn_routerasso_element_get_test() {
        String url = base + "/bgpvpn/routerassociations/8d819a7e-2f1b-4bb5-bbce-af476b237bc1";
        HttpUtils.test_fetch(url, true, "Bgpvpn router association Element Get Failed");
    }

    public void bgpvpn_routerasso_element_get_test_with_added_query() {
        String url = base + "/bgpvpn/routerassociations/8d819a7e-2f1b-4bb5-bbce-af476b237bc1"
                + "?fields=id&fields=bgpvpn-id&fields=router-id";
        HttpUtils.test_fetch(url, true, "Bgpvpn router association Element Get Failed");
    }

    public void bgpvpn_routerasso_element_negative_get_test() {
        String url = base + "/bgpvpn/routerassociations/8d819a7e-2f1b-4bb5-bbce-af476b237bc1";
        HttpUtils.test_fetch(url, false, "Bgpvpn router association Element Negative Get Failed");
    }

    public void bgpvpn_routerasso_delete_test() {
        String url = base + "/bgpvpn/routerassociations/8d819a7e-2f1b-4bb5-bbce-af476b237bc1";
        HttpUtils.test_delete(url, "Bgpvpn router association Element Delete Failed");
    }

    public static void runTests(String base) {
        NeutronBgpvpnRouterAssociationTests bgpvpnRouterAssoTester = new NeutronBgpvpnRouterAssociationTests(base);
        bgpvpnRouterAssoTester.bgpvpn_routerasso_collection_get_test();
        String createJsonString = bgpvpnRouterAssoTester.singleton_bgpvpn_routerasso_create_test();
        bgpvpnRouterAssoTester.singleton_bgpvpn_routerasso_get_with_one_query_item_test(createJsonString);
        bgpvpnRouterAssoTester.bgpvpn_routerasso_element_get_test();
        bgpvpnRouterAssoTester.bgpvpn_routerasso_element_get_test_with_added_query();
        bgpvpnRouterAssoTester.bgpvpn_routerasso_delete_test();
        bgpvpnRouterAssoTester.bgpvpn_routerasso_element_negative_get_test();
    }

}
