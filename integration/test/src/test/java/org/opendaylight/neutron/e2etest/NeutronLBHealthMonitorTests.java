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

public class NeutronLBHealthMonitorTests {
    String base;

    public NeutronLBHealthMonitorTests(String base) {
        this.base = base;
    }

    public void healthMonitor_collection_get_test() {
        String url = base + "/lbaas/healthmonitors";
        ITNeutronE2E.test_fetch(url, "LB Health Monitor Collection GET failed");
    }

    public static void runTests(String base) {
        NeutronLBHealthMonitorTests healthMonitor_tester = new NeutronLBHealthMonitorTests(base);
        healthMonitor_tester.healthMonitor_collection_get_test();
    }
}
