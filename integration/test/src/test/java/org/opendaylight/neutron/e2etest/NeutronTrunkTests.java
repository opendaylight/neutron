/*
 * Copyright (c) 2017 Ericsson India Global Services Pvt Ltd. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.e2etest;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.junit.Assert;

public class NeutronTrunkTests {
    String base;

    public NeutronTrunkTests(String base) {
        this.base = base;
    }

    public void trunk_collection_get_test() {
        String url = base + "/trunks";
        HttpUtils.test_fetch(url, "Trunk collection GET failed");
    }

    public String singleton_trunk_create_test() {
        String url = base + "/trunks";
        String content = "{\"trunk\":{\"status\":\"DOWN\",\"name\":\"trunk0\",\"admin_state_up\":true, "
                + "\"tenant_id\":\"cc3641789c8a4304abaa841c64f638d9\", "
                + "\"port_id\":\"60aac28d-1d3a-48d9-99bc-dd4bd62e50f2\", "
                + "\"sub_ports\":[{\"segmentation_type\":\"vlan\", "
                + "\"port_id\":\"dca33436-2a7c-415b-aa35-14769e7834e3\",\"segmentation_id\":101}, "
                + "{\"segmentation_type\":\"vlan\",\"port_id\":\"be28febe-bdff-45cc-8a2d-872d54e62527\", "
                + "\"segmentation_id\":102}],\"id\":\"c935240e-4aa6-496a-841c-d113c54499b9\", "
                + "\"description\":\"test trunk0\"} }";
        HttpUtils.test_create(url, content, "Singleton Trunk Post Failed NB");
        return content;
    }

    public void singleton_trunk_get_with_one_query_item_test(String createJsonString) {
        String url = base + "/trunks";
        HttpUtils.test_fetch_with_one_query_item(url, createJsonString, "trunks");
    }

    public String singleton_default_trunk_create_test() {
        String url = base + "/trunks";
        String content = "{\"trunk\":{\"name\":\"trunkdefault\", "
                + "\"tenant_id\":\"cc3641789c8a4304abaa841c64f638d9\", "
                + "\"port_id\":\"60aac28d-1d3a-48d9-99bc-dd4bd62e50f2\", "
                + "\"sub_ports\":[{\"segmentation_type\":\"vlan\", "
                + "\"port_id\":\"dca33436-2a7c-415b-aa35-14769e7834e3\",\"segmentation_id\":101}, "
                + "{\"segmentation_type\":\"vlan\",\"port_id\":\"be28febe-bdff-45cc-8a2d-872d54e62527\", "
                + "\"segmentation_id\":102}],\"id\":\"d935240e-4aa6-d96a-d41c-d113c54499b9\", "
                + "\"description\":\"test trunkdefault\"} }";
        HttpUtils.test_create(url, content, "Singleton Default Trunk Post Failed NB");
        return content;
    }

    public void default_trunk_content_validation_test() {
        //Validates NeutronTrunk default parmeters are set.
        //Default parameters: status,admin_state_up
        String element = "status";
        String url = base + "/trunks/d935240e-4aa6-d96a-d41c-d113c54499b9?fields=" + element;
        String expectedContent = "\"DOWN\"";
        String context = "Trunk details do not match.";
        JsonObject jsonObjectOutput = HttpUtils.test_fetch_gson(url, context);
        jsonObjectOutput = jsonObjectOutput.getAsJsonObject("trunk");
        JsonElement jsonElementValue = jsonObjectOutput.get(element);
        Assert.assertEquals(context, expectedContent, String.valueOf(jsonElementValue));
        element = "admin_state_up";
        url = base + "/trunks/d935240e-4aa6-d96a-d41c-d113c54499b9?fields=" + element;
        jsonObjectOutput = HttpUtils.test_fetch_gson(url, context);
        jsonObjectOutput = jsonObjectOutput.getAsJsonObject("trunk");
        jsonElementValue = jsonObjectOutput.get(element);
        Assert.assertEquals(context, true, jsonElementValue.getAsBoolean());
    }

    public void bulk_trunk_create_test() {
        String url = base + "/trunks";
        String content = "{\"trunks\":[{\"status\":\"DOWN\",\"name\":\"trunk1\",\"admin_state_up\":true, "
                + "\"tenant_id\":\"cc3641789c8a4304abaa841c64f638d9\", "
                + "\"port_id\":\"87927a7a-86ec-4062-946f-40222ec583ca\", "
                + "\"sub_ports\":[{\"segmentation_type\":\"vlan\", "
                + "\"port_id\":\"75e366aa-51b6-4ec8-9695-739c465377f7\",\"segmentation_id\":101}, "
                + "{\"segmentation_type\":\"vlan\",\"port_id\":\"e12f8356-ff66-4948-979f-9dedb63ee299\", "
                + "\"segmentation_id\":102}],\"id\":\"bc587c4c-de31-42b1-89c3-809add88c9b3\", "
                + "\"description\":\"test trunk1\"},"
                + "{\"status\":\"ACTIVE\",\"name\":\"trunk2\",\"admin_state_up\":true, "
                + "\"tenant_id\":\"cc3641789c8a4304abaa841c64f638d9\","
                + "\"port_id\":\"f5624c68-eda2-42c1-92a1-53094707dc36\", "
                + "\"sub_ports\":[{\"segmentation_type\":\"vlan\", "
                + "\"port_id\":\"2a4897de-d5ba-4bd5-8998-4f86e083e3fd\", \"segmentation_id\":101},"
                + "{\"segmentation_type\":\"vlan\",\"port_id\":\"9dedb63e-ff66-4948-979f-e12f8356e299\", "
                + "\"segmentation_id\":102}],"
                + "\"id\":\"5e97b0a4-b5a3-49fd-b0cb-821bec16acfe\",\"description\":\"test trunk2\"}]}";
        HttpUtils.test_create(url, content, "Bulk Trunk Post Failed");
    }

    public void trunk_update_test() {
        String url = base + "/trunks/c935240e-4aa6-496a-841c-d113c54499b9";
        String content = "{\"trunk\":{\"status\":\"DOWN\",\"name\":\"trunk0\",\"admin_state_up\":true, "
                + "\"port_id\":\"60aac28d-1d3a-48d9-99bc-dd4bd62e50f2\", "
                + "\"sub_ports\":[{\"segmentation_type\":\"vlan\", "
                + "\"port_id\":\"dca33436-2a7c-415b-aa35-14769e7834e3\",\"segmentation_id\":101}], "
                + "\"id\":\"c935240e-4aa6-496a-841c-d113c54499b9\", \"description\":\"test trunk0 updated\"} }";
        HttpUtils.test_modify(url, content, "Trunk Put Failed");
    }

    public void trunk_bulk_get_test() {
        String url = base + "/trunks"; // /c935240e-4aa6-496a-841c-d113c54499b9";
        HttpUtils.test_fetch(url, true, "Trunk Bulk Get Failed");
    }

    public void trunk_element_get_test() {
        String url = base + "/trunks/c935240e-4aa6-496a-841c-d113c54499b9";
        HttpUtils.test_fetch(url, true, "Trunk Element Get Failed");
    }

    public void trunk_element_get_test_with_added_query() {
        String url = base + "/trunks/c935240e-4aa6-496a-841c-d113c54499b9"
                + "?fields=id&fields=tenant_id&fields=name&fields=port_id"
                + "&fields=status&fields=admin_state_up&fields=sub_ports";
        HttpUtils.test_fetch(url, true, "Trunk Element Get with query Failed");
    }

    public void trunk_element_negative_get_test() {
        String url = base + "/trunks/bc1a76cb-8767-4c3a-bb95-018b822f2130";
        HttpUtils.test_fetch(url, false, "Trunk Element Negative Get Failed");
    }

    public void trunk_delete_test() {
        String url = base + "/trunks/c935240e-4aa6-496a-841c-d113c54499b9";
        HttpUtils.test_delete(url, "Trunk Element Delete Failed");
    }

    public static void runTests(String base) {
        NeutronTrunkTests trunkTester = new NeutronTrunkTests(base);
        trunkTester.trunk_collection_get_test();
        String createJsonString = trunkTester.singleton_trunk_create_test();
        trunkTester.singleton_trunk_get_with_one_query_item_test(createJsonString);
        trunkTester.singleton_default_trunk_create_test();
        trunkTester.default_trunk_content_validation_test();
        trunkTester.bulk_trunk_create_test();
        trunkTester.trunk_update_test();
        trunkTester.trunk_bulk_get_test();
        trunkTester.trunk_element_get_test();
        trunkTester.trunk_element_get_test_with_added_query();
        trunkTester.trunk_delete_test();
        trunkTester.trunk_element_negative_get_test();
    }
}
