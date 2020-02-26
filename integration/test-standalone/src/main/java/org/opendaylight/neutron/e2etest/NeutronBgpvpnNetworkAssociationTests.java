/*
 * Copyright (c) 2020 Ericsson India Global Services Pvt Ltd. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.e2etest;

public class NeutronBgpvpnNetworkAssociationTests {
    String base;

    public NeutronBgpvpnNetworkAssociationTests(String base) {
        this.base = base;
    }

    public void bgpvpn_netasso_collection_get_test() {
        String url = base + "/bgpvpn/networkassociations";
        HttpUtils.test_fetch(url, "BGPVPN network association collection GET failed");
    }

    public String singleton_bgpvpn_netasso_create_test() {
        String url = base + "/bgpvpn/networkassociations";
        String content = "{ \"bgpvpn_network_association\" : { \"id\": \"7326ef73-378d-4981-bfa2-51cb80de78e0\","
                + " \"bgpvpn-id\": \"b472f6eb-3ff1-4c4b-8f1b-e1032e10c372\", "
                + "\"network-id\": \"3ad0e6c3-80da-421e-8733-254e62adad16\" } }";
        HttpUtils.test_create(url, content, "Singleton Bgpvpn network association Post Failed NB");
        return content;
    }

    public void singleton_bgpvpn_netasso_get_with_one_query_item_test(String createJsonString) {
        String url = base + "/bgpvpn/networkassociations";
        HttpUtils.test_fetch_with_one_query_item(url, createJsonString, "bgpvpn_network_associations");
    }

    public void bgpvpn_netasso_element_get_test() {
        String url = base + "/bgpvpn/networkassociations/7326ef73-378d-4981-bfa2-51cb80de78e0";
        HttpUtils.test_fetch(url, true, "Bgpvpn network association Element Get Failed");
    }

    public void bgpvpn_netasso_element_get_test_with_added_query() {
        String url = base + "/bgpvpn/networkassociations/7326ef73-378d-4981-bfa2-51cb80de78e0"
                + "?fields=id&fields=bgpvpn-id&fields=network-id";
        HttpUtils.test_fetch(url, true, "Bgpvpn network association Element Get Failed");
    }

    public void bgpvpn_netasso_element_negative_get_test() {
        String url = base + "/bgpvpn/networkassociations/7326ef73-378d-4981-bfa2-51cb80de78e0";
        HttpUtils.test_fetch(url, false, "Bgpvpn network association Element Negative Get Failed");
    }

    public void bgpvpn_netasso_delete_test() {
        String url = base + "/bgpvpn/networkassociations/7326ef73-378d-4981-bfa2-51cb80de78e0";
        HttpUtils.test_delete(url, "Bgpvpn network association Element Delete Failed");
    }

    public static void runTests(String base) {
        NeutronBgpvpnNetworkAssociationTests bgpvpnNetAssoTester = new NeutronBgpvpnNetworkAssociationTests(base);
        bgpvpnNetAssoTester.bgpvpn_netasso_collection_get_test();
        String createJsonString = bgpvpnNetAssoTester.singleton_bgpvpn_netasso_create_test();
        bgpvpnNetAssoTester.singleton_bgpvpn_netasso_get_with_one_query_item_test(createJsonString);
        bgpvpnNetAssoTester.bgpvpn_netasso_element_get_test();
        bgpvpnNetAssoTester.bgpvpn_netasso_element_get_test_with_added_query();
        bgpvpnNetAssoTester.bgpvpn_netasso_delete_test();
        bgpvpnNetAssoTester.bgpvpn_netasso_element_negative_get_test();
    }
}
