/*
 * Copyright (C) 2015 IBM, Inc.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.e2etest;

import java.io.OutputStreamWriter;

import java.lang.Thread;

import java.net.HttpURLConnection;
import java.net.URL;

import org.junit.Assert;

public class NeutronNetworkTests {
    String base;

    public NeutronNetworkTests(String base) {
        this.base = base;
    }

    public void network_collection_get_test() {
        String url_s = base + "/networks";
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
                    Assert.assertEquals("Network Collection GET failed",
                        200, httpConn.getResponseCode());
                    return;
                }
            }
            Assert.assertFalse("Network Collection GET failed", true);
        } catch (Exception e) {
            Assert.assertFalse("E2E Tests Failed", true);
        }
    }

    //TODO handle SB check
    public void singleton_network_create_test() {
        String url = base + "/networks";
        String content = "{ \"network\": {" +
            " \"status\": \"ACTIVE\", \"subnets\": [], " +
            " \"name\": \"net1\", \"admin_state_up\": true, " +
            " \"tenant_id\": \"9bacb3c5d39d41a79512987f338cf177\", " +
            " \"router:external\": false, \"segments\": [ " +
            " { \"provider:segmentation_id\": 2, " +
            " \"provider:physical_network\": \"8bab8453-1bc9-45af-8c70-f83aa9b50453\", " +
            " \"provider:network_type\": \"vlan\" }, { " +
            " \"provider:segmentation_id\": null, " +
            " \"provider:physical_network\": \"8bab8453-1bc9-45af-8c70-f83aa9b50453\", " +
            " \"provider:network_type\": \"stt\" } ], " +
            " \"shared\": false, \"id\": \"4e8e5957-649f-477b-9e5b-f1f75b21c03c\" " +
            " } } ";
        ITNeutronE2E.test_create(url, content,"Singleton Network Post Failed NB");
    }

    //TODO handle SB check
    public void external_network_create_test() {
        String url = base + "/networks";
        String content = "{ \"network\": {" +
            " \"status\": \"ACTIVE\", \"subnets\": [], " +
            " \"name\": \"external1\", \"admin_state_up\": true, " +
            " \"tenant_id\": \"9bacb3c5d39d41a79512987f338cf177\", " +
            " \"router:external\": true, \"shared\": false, " +
            " \"id\": \"8ca37218-28ff-41cb-9b10-039601ea7e6b\" } } ";
        ITNeutronE2E.test_create(url, content,"External Network Post Failed NB");
    }

    //TODO handle SB check
    public void bulk_network_create_test() {
        String url = base + "/networks";
        String content = "{ \"networks\": [ { "
            + "\"status\": \"ACTIVE\", \"subnets\": [], "
            + "\"name\": \"sample_network3\", "
            + "\"provider:physical_network\": null, "
            + "\"admin_state_up\": true, "
            + "\"tenant_id\": \"4fd44f30292945e481c7b8a0c8908869\", "
            + "\"provider:network_type\": \"local\", "
            + "\"shared\": false, "
            + "\"id\": \"bc1a76cb-8767-4c3a-bb95-018b822f2130\", "
            + "\"provider:segmentation_id\": null }, { "
            + "\"status\": \"ACTIVE\", "
            + "\"subnets\": [], "
            + "\"name\": \"sample_network4\", "
            + "\"provider:physical_network\": null, "
            + "\"admin_state_up\": true, "
            + "\"tenant_id\": \"4fd44f30292945e481c7b8a0c8908869\", "
            + "\"provider:network_type\": \"local\", "
            + "\"shared\": false, "
            + "\"id\": \"af374017-c9ae-4a1d-b799-ab73111476e2\", "
            + "\"provider:segmentation_id\": null } ] } ";
        ITNeutronE2E.test_create(url, content,"Bulk Network Post Failed");
    }

    //TODO handle SB check
    public void network_update_test() {
        String url = base + "/networks/bc1a76cb-8767-4c3a-bb95-018b822f2130";
        String content = " { \"network\": { "
            +"\"status\": \"ACTIVE\", "
            +"\"subnets\": [], "
            +"\"name\": \"sample_network_5_updated\", "
            +"\"provider:physical_network\": null, "
            +"\"admin_state_up\": true, "
            +"\"tenant_id\": \"4fd44f30292945e481c7b8a0c8908869\", "
            +"\"provider:network_type\": \"local\", "
            +"\"router:external\": false, "
            +"\"shared\": false, "
            +"\"provider:segmentation_id\": null } } ";
        ITNeutronE2E.test_modify(url, content,"Network Put Failed");
    }
   
    public void network_element_get_test() {
        String url = base + "/networks/bc1a76cb-8767-4c3a-bb95-018b822f2130";
        ITNeutronE2E.test_fetch(url, true ,"Network Element Get Failed");
    }

    public void network_element_negative_get_test() {
        String url = base + "/networks/bc1a76cb-8767-4c3a-bb95-018b822f2130";
        ITNeutronE2E.test_fetch(url, false ,"Network Element Negative Get Failed");
    }

    public void network_delete_test() {
        String url = base + "/networks/bc1a76cb-8767-4c3a-bb95-018b822f2130";
        ITNeutronE2E.test_delete(url, "Network Element Delete Failed");
    }

    public void network_bug4847_test() {
        // https://bugs.opendaylight.org/show_bug.cgi?id=4847
        // null tenant id in MD model causes null pointer exception
        String uuid = "8fff19c7-f008-42e2-a664-f409cf55e45b";

        String url = base + "/networks";
        String content = "{ \"networks\": [ { "
            + "\"name\": \"private1\", "
            + "\"id\": \"" + uuid + "\" } ] } ";
        ITNeutronE2E.test_create(url, content,"Bug4847 Network Post Failed");

        url = url + "/" + uuid;
        ITNeutronE2E.test_fetch(url, "Bug4847 Network GET failed");
        ITNeutronE2E.test_delete(url, "Bug4847 Network Element Delete Failed");
    }

    public static void runTests(String base) {
        NeutronNetworkTests network_tester = new NeutronNetworkTests(base);
        network_tester.network_collection_get_test();
        network_tester.singleton_network_create_test();
        network_tester.external_network_create_test(); //needed for router test
        network_tester.bulk_network_create_test();
        network_tester.network_update_test();
        network_tester.network_element_get_test();
        network_tester.network_delete_test();
        network_tester.network_element_negative_get_test();
        network_tester.network_bug4847_test();
    }
}
