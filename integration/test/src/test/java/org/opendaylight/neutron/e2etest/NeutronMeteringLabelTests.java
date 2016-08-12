/*
 * Copyright (C) 2015 IBM, Inc.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.e2etest;

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
        String content = "{ \"metering_label\": { " + "\"tenant_id\": \"45345b0ee1ea477fac0f541b2cb79cd4\", "
                + "\"description\": \"description of label1\", " + "\"name\": \"label1\", "
                + "\"id\": \"bc91b832-8465-40a7-a5d8-ba87de442266\" } }";
        ITNeutronE2E.test_create(url, content, "Singleton Metering Label Post Failed NB");
    }

    public void meteringLabel_element_get_test() {
        String url = base + "/metering/metering-labels/bc91b832-8465-40a7-a5d8-ba87de442266";
        ITNeutronE2E.test_fetch(url, true, "Metering Label Element Get Failed");
    }

    public void meteringLabel_element_get_with_query_test() {
        String url = base + "/metering/metering-labels/bc91b832-8465-40a7-a5d8-ba87de442266"
                + "?fields=id&fields=tenant_id&fields=name&fields=description";
        ITNeutronE2E.test_fetch(url, true, "Metering Label Element Get Failed");
    }

    public void meteringLabel_delete_test() {
        String url = base + "/metering/metering-labels/bc91b832-8465-40a7-a5d8-ba87de442266";
        ITNeutronE2E.test_delete(url, "Metering Label Element Delete Failed");
    }

    public void meteringLabel_element_negative_get_test() {
        String url = base + "/metering/metering-labels/bc91b832-8465-40a7-a5d8-ba87de442266";
        ITNeutronE2E.test_fetch(url, false, "Metering Label Element Negative Get Failed");
    }

    public static void runTests(String base) {
        NeutronMeteringLabelTests meteringLabel_tester = new NeutronMeteringLabelTests(base);
        meteringLabel_tester.singleton_meteringLabel_create_test();
        meteringLabel_tester.meteringLabel_element_get_test();
        meteringLabel_tester.meteringLabel_element_get_with_query_test();
        meteringLabel_tester.meteringLabel_collection_get_test();
        meteringLabel_tester.meteringLabel_delete_test();
        meteringLabel_tester.meteringLabel_element_negative_get_test();
    }
}
