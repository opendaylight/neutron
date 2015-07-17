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

public class NeutronLBPoolTests {
    String base;

    public NeutronLBPoolTests(String base) {
        this.base = base;
    }

    public void pool_collection_get_test() {
        String url = base + "/lbaas/pools";
        ITNeutronE2E.test_fetch(url, "LB Pool Collection GET failed");
    }

    //TODO handle SB check
    public void singleton_lb_pool_create_test() {
        String url = base + "/lbaas/pools";
        String content = "{ \"pool\": { " +
            "\"admin_state_up\": true, " +
            "\"description\": \"simple pool\", " +
            "\"healthmonitor_id\": null, " +
            "\"id\": \"12ff63af-4127-4074-a251-bcb2ecc53ebe\", " +
            "\"lb_algorithm\": \"ROUND_ROBIN\", " +
            "\"listeners\": [ { " +
                    "\"id\": \"39de4d56-d663-46e5-85a1-5b9d5fa17829\" } ], " +
            "\"members\": [], " +
            "\"name\": \"pool1\", " +
            "\"protocol\": \"HTTP\", " +
            "\"session_persistence\": { " +
                "\"cookie_name\": \"my_cookie\", " +
                "\"type\": \"APP_COOKIE\" }, " +
            "\"tenant_id\": \"b7c1a69e88bf4b21a8148f787aef2081\" } }";
        ITNeutronE2E.test_create(url, content, "Singleton LB Pool Post Failed NB");
    }

    public static void runTests(String base) {
        NeutronLBPoolTests pool_tester = new NeutronLBPoolTests(base);
        pool_tester.pool_collection_get_test();
        pool_tester.singleton_lb_pool_create_test();
    }
}
