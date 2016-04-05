/*
* Copyright (C) 2016 Intel, Corp.
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

public class NeutronQosPolicyTests {
    String base;

    public NeutronQosPolicyTests(String base) {
        this.base = base;
    }

    public String singleton_qos_policy_create_test() {
        String url = base + "/qos/policies";
        String content = "{ \"policy\": { \"id\": \"d6220bbb-35f3-48ab-8eae-69c60aef3546\","
            + "\"tenant_id\": \"aa902936679e4ea29bfe1158e3450a13\"," + "\"name\" : \"test\", "
            + " \"shared\": false  }}";
        ITNeutronE2E.test_create(url, content, "Qos Policy Singleton Post Failed");
        return content;
    }

    public void qos_policy_modify_test() {
        String url = base + "/qos/policies/d6220bbb-35f3-48ab-8eae-69c60aef3546";
        String content = "{\"policy\":" + "{\"id\": \"d6220bbb-35f3-48ab-8eae-69c60aef3546\","
            + "\"tenant_id\": \"aa902936679e4ea29bfe1158e3450a13\"," + "\"name\" : \"jaxb-test\", "
            + " \"shared\": false,"
            + " \"bandwidth_limit_rules\": [ {\"id\": \"d6220bbb-35f3-48ab-8eae-69c60aef3547\","
            + " \"tenant_id\": \"aa902936679e4ea29bfe1158e3450a14\",\"max_kbps\": 25,"
            + " \"max_burst_kbps\": 100 } ] }}";
        ITNeutronE2E.test_modify(url, content, "Qos Policy Singleton Put failed");
    }

    public void qos_policy_delete_test() {
        String url = base + "/qos/policies/d6220bbb-35f3-48ab-8eae-69c60aef3546";
        ITNeutronE2E.test_delete(url, "Qos Policy Delete Failed");
    }

    public static void runTests(String base) {
        NeutronQosPolicyTests qos_policy_tester = new NeutronQosPolicyTests(base);
        String createJsonString = qos_policy_tester.singleton_qos_policy_create_test();
        qos_policy_tester.qos_policy_modify_test();
        qos_policy_tester.qos_policy_delete_test();
    }

}

