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

public class NeutronIPSECSiteConnectionTests {
    String base;

    public NeutronIPSECSiteConnectionTests(String base) {
        this.base = base;
    }

    public void ipsecSiteConnection_collection_get_test() {
        String url = base + "/vpn/ipsecsiteconnections";
        ITNeutronE2E.test_fetch(url, true, "IPSEC Site Connection Collection GET failed");
    }
}
