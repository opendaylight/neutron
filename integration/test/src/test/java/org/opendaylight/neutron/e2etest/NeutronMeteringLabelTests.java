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

public class NeutronMeteringLabelTests {
    String base;

    public NeutronMeteringLabelTests(String base) {
        this.base = base;
    }

    public void meteringLabel_collection_get_test() {
        String url = base + "/metering/metering-labels";
        ITNeutronE2E.test_fetch(url, "Metering Label Collection GET failed");
    }

    //TODO handle SB check
    public void singleton_meteringLabel_create_test() {
        String url = base + "/metering/metering-labels";
        String content = "{ \"metering_label\": { " +
            "\"tenant_id\": \"45345b0ee1ea477fac0f541b2cb79cd4\", " +
            "\"description\": \"description of label1\", " +
            "\"name\": \"label1\", " +
            "\"id\": \"bc91b832-8465-40a7-a5d8-ba87de442266\" } }";
        ITNeutronE2E.test_create(url, content, "Singleton Metering Label Post Failed NB");
    }
}
