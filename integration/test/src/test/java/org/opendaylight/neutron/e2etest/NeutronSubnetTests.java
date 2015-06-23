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

public class NeutronSubnetTests {
    String base;

    public NeutronSubnetTests(String base) {
        this.base = base;
    }

    public void subnet_collection_get_test() {
        String url_s = base + "/subnets";
        try {
            URL url = new URL(url_s);
            HttpURLConnection httpConn = ITNeutronE2E.HttpURLConnectionFactoryGet(url);
            Assert.assertEquals("Subnet Collection GET failed",
                        200, httpConn.getResponseCode());
        } catch (Exception e) {
            Assert.assertFalse("E2E Tests Failed", true);
        }
    }

    //TODO handle SB check
    public void singleton_subnet_create_test() {
        String url_s = base + "/subnets";
        String content = " { \"subnet\": { "+
            "\"name\": \"\", "+
            "\"enable_dhcp\": true, "+
            "\"network_id\": \"4e8e5957-649f-477b-9e5b-f1f75b21c03c\", "+
            "\"tenant_id\": \"9bacb3c5d39d41a79512987f338cf177\", "+
            "\"dns_nameservers\": [], "+
            "\"allocation_pools\": [ { "+
                "\"start\": \"10.0.0.2\", "+
                "\"end\": \"10.0.0.254\" } ], "+
            "\"host_routes\": [], "+
            "\"ip_version\": 4, "+
            "\"gateway_ip\": \"10.0.0.1\", "+
            "\"cidr\": \"10.0.0.0/24\", "+
            "\"id\": \"3b80198d-4f7b-4f77-9ef5-774d54e17126\" } } ";

        try {
            URL url = new URL(url_s);
            HttpURLConnection httpConn = ITNeutronE2E.HttpURLConnectionFactoryPost(url, content);
            Assert.assertEquals("Singleton Subnet Post Failed NB",
                201, httpConn.getResponseCode());
        } catch (Exception e) {
            Assert.assertFalse("E2E Tests Failed", true);
        }
    }

    //TODO handle SB check
    public void external_subnet_create_test() {
        String url_s = base + "/subnets";
        String content = " { \"subnet\": { "+
            "\"name\": \"\", "+
            "\"enable_dhcp\": true, "+
            "\"network_id\": \"8ca37218-28ff-41cb-9b10-039601ea7e6b\", "+
            "\"tenant_id\": \"9bacb3c5d39d41a79512987f338cf177\", "+
            "\"dns_nameservers\": [], "+
            "\"allocation_pools\": [ { "+
                "\"start\": \"10.1.0.2\", "+
                "\"end\": \"10.1.0.254\" } ], "+
            "\"host_routes\": [], "+
            "\"ip_version\": 4, "+
            "\"gateway_ip\": \"10.1.0.1\", "+
            "\"cidr\": \"10.1.0.0/24\", "+
            "\"id\": \"f13b537f-1268-455f-b5fa-1e6817a9c204\" } } ";

        try {
            URL url = new URL(url_s);
            HttpURLConnection httpConn = ITNeutronE2E.HttpURLConnectionFactoryPost(url, content);
            Assert.assertEquals("External Subnet Post Failed NB",
                201, httpConn.getResponseCode());
        } catch (Exception e) {
            Assert.assertFalse("E2E Tests Failed", true);
        }
    }
}
