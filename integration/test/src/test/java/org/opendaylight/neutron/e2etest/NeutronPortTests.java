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

public class NeutronPortTests {
    String base;

    public NeutronPortTests(String base) {
        this.base = base;
    }

    public void port_collection_get_test() {
        String url_s = base + "/ports";
        try {
            URL url = new URL(url_s);
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            httpConn.setRequestMethod("GET");
            httpConn.setRequestProperty("Content-Type", "application/json");
            httpConn.setRequestProperty("Authorization", "Basic YWRtaW46YWRtaW4=");
            Assert.assertEquals("Ports Collection GET failed",
                        200, httpConn.getResponseCode());
        } catch (Exception e) {
            Assert.assertFalse("E2E Tests Failed", true);
        }
    }

    //TODO handle SB check
    public void singleton_port_create_test() {
        String url_s = base + "/ports";
        String content = "{ \"port\": { \"status\": \"DOWN\","+
            "\"binding:host_id\": \"\","+
            "\"name\": \"private-port\","+
            "\"allowed_address_pairs\": [],"+
            "\"admin_state_up\": true,"+
            "\"network_id\": \"a87cc70a-3e15-4acf-8205-9b711a3531b7\","+
            "\"tenant_id\": \"d6700c0c9ffa4f1cb322cd4a1f3906fa\","+
            "\"binding:vif_details\": {},"+
            "\"binding:vnic_type\": \"normal\","+
            "\"binding:vif_type\": \"unbound\","+
            "\"device_owner\": \"\","+
            "\"mac_address\": \"fa:16:3e:c9:cb:f0\","+
            "\"binding:profile\": {},"+
            "\"fixed_ips\": [ {"+
                "\"subnet_id\": \"a0304c3a-4f08-4c43-88af-d796509c97d2\","+
                "\"ip_address\": \"10.0.0.2\" } ],"+
            "\"id\": \"65c0ee9f-d634-4522-8954-51021b570b0d\","+
            "\"security_groups\": [ \"f0ac4394-7e4a-4409-9701-ba8be283dbc3\" ],"+
            "\"device_id\": \"\" } }";

        try {
            URL url = new URL(url_s);
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            httpConn.setRequestMethod("POST");
            httpConn.setRequestProperty("Content-Type", "application/json");
            httpConn.setRequestProperty("Authorization", "Basic YWRtaW46YWRtaW4=");
            httpConn.setDoOutput(true);
            OutputStreamWriter out = new OutputStreamWriter(
                httpConn.getOutputStream());
            out.write(content);
            out.close();
            Assert.assertEquals("Singleton Port Post Failed NB",
                201, httpConn.getResponseCode());
        } catch (Exception e) {
            Assert.assertFalse("E2E Tests Failed", true);
        }
    }
}
