/*
 * Copyright (c) 2015 Ericsson India Global Services Pvt Ltd. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.e2etest;

import java.lang.Thread;

import java.net.HttpURLConnection;
import java.net.URL;

import org.junit.Assert;

public class NeutronBgpvpnTests {
    String base;

    public NeutronBgpvpnTests(String base) {
        this.base = base;
    }

    public void bgpvpn_collection_get_test() {
        String url_s = base + "/bgpvpns";
        try {
            int i = 0;
            while (i < 60) {
                URL url = new URL(url_s);
                HttpURLConnection httpConn = ITNeutronE2E.HttpURLConnectionFactoryGet(url);
                if (httpConn.getResponseCode() != 200) {
                    System.out.println("trial "+Integer.toString(i)+": failed with: " +
                                       Integer.toString(httpConn.getResponseCode()));
                    Thread.sleep(1000);
                    i+=1;
                } else {
                    Assert.assertEquals("BGPVPN Collection GET failed",
                        200, httpConn.getResponseCode());
                    return;
                }
            }
            Assert.assertFalse("BGPVPN Collection GET failed", true);
        } catch (Exception e) {
            Assert.assertFalse("E2E Tests Failed", true);
        }
    }

    //TODO handle SB check
    public void singleton_bgpvpn_create_test() {
        String url = base + "/bgpvpns";
        String content = "{ \"bgpvpn\": {" +
            " \"status\": \"ACTIVE\", \"type\": \"l3\", " +
            " \"name\": \"vpn1\", \"admin_state_up\": true, " +
            " \"tenant_id\": \"9bacb3c5d39d41a79512987f338cf177\", " +
            " \"route_targets\": \"64512:1\", " +
            " \"networks\": \"3b80198d-4f7b-4f77-9ef5-774d54e17126\", " +
            " \"auto_aggregate\": true, \"id\": \"4e8e5957-649f-477b-9e5b-f1f75b21c03c\" " +
            " } } ";
        ITNeutronE2E.test_create(url, content,"Singleton Bgpvpn Post Failed NB");
    }

    //TODO handle SB check
    public void bulk_bgpvpn_create_test() {
        String url = base + "/bgpvpns";
        String content = "{ \"bgpvpns\": [ { "
            + "\"status\": \"ACTIVE\", "
            + "\"name\": \"sample_bgpvpn1\", "
            + "\"admin_state_up\": true, "
            + "\"tenant_id\": \"4fd44f30292945e481c7b8a0c8908869\", "
            + "\"id\": \"bc1a76cb-8767-4c3a-bb95-018b822f2130\", "
            + "\"route_targets\": \"64512:1\", "
            + "\"auto_aggregate\": true, "
            + "\"type\": \"l3\" }, { "
            + "\"status\": \"ACTIVE\", "
            + "\"name\": \"sample_bgpvpn2\", "
            + "\"admin_state_up\": true, "
            + "\"tenant_id\": \"4fd44f30292945e481c7b8a0c8908869\", "
            + "\"id\": \"af374017-c9ae-4a1d-b799-ab73111476e2\", "
            + "\"route_targets\": \"64512:2\", "
            + "\"auto_aggregate\": false, "
            + "\"type\": \"l3\" } ] } ";
        ITNeutronE2E.test_create(url, content,"Bulk Bgpvpn Post Failed");
    }

    //TODO handle SB check
    public void bgpvpn_update_test() {
        String url = base + "/bgpvpns/bc1a76cb-8767-4c3a-bb95-018b822f2130";
        String content = " { \"bgpvpn\": { "
            +"\"status\": \"ACTIVE\", "
            +"\"name\": \"sample_bgpvpn_updated\", "
            +"\"admin_state_up\": true, "
            +"\"tenant_id\": \"4fd44f30292945e481c7b8a0c8908869\", "
            + "\"auto_aggregate\": true, "
            + "\"type\": \"l3\" } } ";
        ITNeutronE2E.test_modify(url, content,"Bgpvpn Put Failed");
    }

    public void bgpvpn_element_get_test() {
        String url = base + "/bgpvpns/bc1a76cb-8767-4c3a-bb95-018b822f2130";
        ITNeutronE2E.test_fetch(url, true ,"Bgpvpn Element Get Failed");
    }

    public void bgpvpn_element_negative_get_test() {
        String url = base + "/bgpvpns/bc1a76cb-8767-4c3a-bb95-018b822f2130";
        ITNeutronE2E.test_fetch(url, false ,"Bgpvpn Element Negative Get Failed");
    }

    public void bgpvpn_delete_test() {
        String url = base + "/bgpvpns/bc1a76cb-8767-4c3a-bb95-018b822f2130";
        ITNeutronE2E.test_delete(url, "bgpvpnElement Delete Failed");
    }

    public static void runTests(String base) {
        NeutronBgpvpnTests bgpvpn_tester = new NeutronBgpvpnTests(base);
        bgpvpn_tester.bgpvpn_collection_get_test();
        bgpvpn_tester.singleton_bgpvpn_create_test();
        bgpvpn_tester.bulk_bgpvpn_create_test();
        bgpvpn_tester.bgpvpn_update_test();
        bgpvpn_tester.bgpvpn_element_get_test();
        bgpvpn_tester.bgpvpn_delete_test();
        bgpvpn_tester.bgpvpn_element_negative_get_test();
    }
}
