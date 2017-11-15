/*
 * Copyright (C) 2015 IBM, Inc.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.e2etest;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.junit.Assert;

public class NeutronPortTests {
    String base;

    public NeutronPortTests(String base) {
        this.base = base;
    }

    public void port_collection_get_test() {
        String url = base + "/ports";
        ITNeutronE2E.test_fetch(url, "Ports Collection GET failed");
    }

    //TODO handle SB check
    public String singleton_port_create_test() {
        String url = base + "/ports";
        String content = "{ \"port\": { \"status\": \"DOWN\"," + "\"binding:host_id\": \"\","
                + "\"name\": \"private-port\"," + "\"allowed_address_pairs\": []," + "\"admin_state_up\": true,"
                + "\"port_security_enabled\": true,"
                + "\"network_id\": \"4e8e5957-649f-477b-9e5b-f1f75b21c03c\","
                + "\"tenant_id\": \"9bacb3c5d39d41a79512987f338cf177\"," + "\"binding:vif_details\": {},"
                + "\"binding:vnic_type\": \"normal\"," + "\"binding:vif_type\": \"unbound\","
                + "\"device_owner\": \"\"," + "\"mac_address\": \"fa:16:3e:c9:cb:f0\"," + "\"binding:profile\": {},"
                + "\"fixed_ips\": [ {" + "\"subnet_id\": \"3b80198d-4f7b-4f77-9ef5-774d54e17126\","
                + "\"ip_address\": \"10.0.0.2\" } ]," + "\"id\": \"65c0ee9f-d634-4522-8954-51021b570b0d\","
                + "\"security_groups\": [] ," + "\"device_id\": \"\" } }";
        ITNeutronE2E.test_create(url, content, "Singleton Port Post Failed NB");
        return content;
    }

    public void singleton_port_get_with_one_query_item_test(String createJsonString) {
        String url = base + "/ports";
        ITNeutronE2E.test_fetch_with_one_query_item(url, createJsonString, "ports");
    }

    public String singleton_default_port_create_test() {
        String url = base + "/ports";
        String content = "{ \"port\": { \"binding:host_id\": \"\","
                + "\"name\": \"default-port\"," + "\"allowed_address_pairs\": [],"
                + "\"network_id\": \"4e8e5957-649f-477b-9e5b-f1f75b21c03c\","
                + "\"tenant_id\": \"9bacb3c5d39d41a79512987f338cf177\"," + "\"binding:vif_details\": {},"
                + "\"binding:vnic_type\": \"normal\"," + "\"binding:vif_type\": \"unbound\","
                + "\"device_owner\": \"\"," + "\"mac_address\": \"fa:16:3e:c9:cb:f0\"," + "\"binding:profile\": {},"
                + "\"fixed_ips\": [ {" + "\"subnet_id\": \"3b80198d-4f7b-4f77-9ef5-774d54e17126\","
                + "\"ip_address\": \"10.0.0.2\" } ]," + "\"id\": \"d5c0ee9f-d634-d522-d954-d1021b570b0d\","
                + "\"security_groups\": [] ," + "\"device_id\": \"\" } }";
        ITNeutronE2E.test_create(url, content, "Singleton Default Port Post Failed NB");
        return content;
    }

    public void default_port_content_validation_test() {
        //Validates NeutronPort default parameters are set.
        //Default parameters: status,admin_state_up,port_security_enabled
        String element = "status";
        String url = base + "/ports/d5c0ee9f-d634-d522-d954-d1021b570b0d"
                + "?fields=" + element;
        String expectedContent = "\"ACTIVE\"";
        String context = "Port details do not match.";
        JsonObject jsonObjectOutput = ITNeutronE2E.test_fetch_gson(url, context);
        jsonObjectOutput = jsonObjectOutput.getAsJsonObject("port");
        JsonElement jsonElementValue = jsonObjectOutput.get(element);
        Assert.assertEquals(context, expectedContent, String.valueOf(jsonElementValue));
        element = "port_security_enabled";
        url = base + "/ports/d5c0ee9f-d634-d522-d954-d1021b570b0d?fields=" + element;
        jsonObjectOutput = ITNeutronE2E.test_fetch_gson(url, context);
        jsonObjectOutput = jsonObjectOutput.getAsJsonObject("port");
        jsonElementValue = jsonObjectOutput.get(element);
        Assert.assertEquals(context, true, jsonElementValue.getAsBoolean());
        element = "admin_state_up";
        url = base + "/ports/d5c0ee9f-d634-d522-d954-d1021b570b0d?fields=" + element;
        jsonObjectOutput = ITNeutronE2E.test_fetch_gson(url, context);
        jsonObjectOutput = jsonObjectOutput.getAsJsonObject("port");
        jsonElementValue = jsonObjectOutput.get(element);
        Assert.assertEquals(context, true, jsonElementValue.getAsBoolean());
    }

    //TODO handle SB check
    public void router_interface_port_create_test() {
        String url = base + "/ports";
        String content = "{\"ports\": [ {\"status\": \"DOWN\", \"binding:host_id\": \"\", "
                + "\"allowed_address_pairs\": [], \"device_owner\": \"network:router_gateway\", "
                + "\"binding:profile\": {}, \"fixed_ips\": "
                + "[{\"subnet_id\": \"3b80198d-4f7b-4f77-9ef5-774d54e17126\", "
                + "\"ip_address\": \"10.0.0.1\"}], \"id\": \"d8a4cc85-ad78-46ac-b5a1-8e04f16fa51e\", "
                + "\"security_groups\": [], \"device_id\": \"8604a0de-7f6b-409a-a47c-a1cc7bc77b2e\", "
                + "\"name\": \"\", \"admin_state_up\": true, "
                + "\"network_id\": \"4e8e5957-649f-477b-9e5b-f1f75b21c03c\", "
                + "\"tenant_id\": \"9bacb3c5d39d41a79512987f338cf177\", \"binding:vif_details\": {},"
                + " \"binding:vnic_type\": \"normal\", \"binding:vif_type\": \"unbound\", "
                + "\"mac_address\": \"fa:16:3e:dc:1d:8d\"}, "
                + " {\"status\": \"DOWN\", \"binding:host_id\": \"\", "
                + "\"allowed_address_pairs\": [], \"device_owner\": \"network:router_gateway\", "
                + "\"binding:profile\": {}, \"fixed_ips\": [{\"subnet_id\": \"3b80198d-4f7b-4f77-9ef5-774d54e17126\", "
                + "\"ip_address\": \"10.0.0.2\"}], \"id\": \"d8a4cc85-ad78-46ac-b5a1-8e04f16fa51f\", "
                + "\"security_groups\": [], \"device_id\": \"8604a0de-7f6b-409a-a47c-a1cc7bc77b2f\", "
                + "\"name\": \"\", \"admin_state_up\": true, "
                + "\"network_id\": \"4e8e5957-649f-477b-9e5b-f1f75b21c03c\", "
                + "\"tenant_id\": \"9bacb3c5d39d41a79512987f338cf177\", "
                + "\"binding:vif_details\": {}, \"binding:vnic_type\": \"normal\", \"binding:vif_type\": \"unbound\","
                + " \"mac_address\": \"fa:16:3e:dc:1d:8e\"} ] }";
        ITNeutronE2E.test_create(url, content, "Router Interface Port Post Failed NB");
    }

    public void binding_profile_port_create_test() {
        String url = base + "/ports";
        String content = "{ \"port\": { \"status\": \"DOWN\"," + "\"binding:host_id\": \"\","
                + "\"name\": \"private-port\"," + "\"allowed_address_pairs\": []," + "\"admin_state_up\": true,"
                + "\"port_security_enabled\": true,"
                + "\"network_id\": \"4e8e5957-649f-477b-9e5b-f1f75b21c03c\","
                + "\"tenant_id\": \"9bacb3c5d39d41a79512987f338cf177\"," + "\"binding:vif_details\": {},"
                + "\"binding:vnic_type\": \"normal\"," + "\"binding:vif_type\": \"unbound\","
                + "\"device_owner\": \"\"," + "\"mac_address\": \"fa:16:3e:c9:cb:f0\","
                + "\"binding:profile\": {\"capabilities\": [\"switchdev\", \"other_cap\"]},"
                + "\"fixed_ips\": [ {" + "\"subnet_id\": \"3b80198d-4f7b-4f77-9ef5-774d54e17126\","
                + "\"ip_address\": \"10.0.0.2\" } ]," + "\"id\": \"89125887-6821-4898-aff2-cde8a3b3b5de\","
                + "\"security_groups\": [] ," + "\"device_id\": \"\" } }";
        ITNeutronE2E.test_create(url, content, "Binding profile Port Post Failed NB");
    }

    public void bulk_port_create_test() {
        String url = base + "/ports";
        String content = "{ \"ports\": [ { " + " \"status\": \"DOWN\", " + " \"name\": \"sample_port_1\", "
                + " \"allowed_address_pairs\": [], " + " \"admin_state_up\": false, "
                + " \"network_id\": \"4e8e5957-649f-477b-9e5b-f1f75b21c03c\", "
                + " \"tenant_id\": \"d6700c0c9ffa4f1cb322cd4a1f3906fa\", " + " \"device_owner\": \"\", "
                + " \"mac_address\": \"fa:16:3e:48:b8:9f\", " + " \"fixed_ips\": [ { "
                + "\"subnet_id\": \"3b80198d-4f7b-4f77-9ef5-774d54e17126\"," + " \"ip_address\": \"10.0.0.5\" } ], "
                + " \"id\": \"94225baa-9d3f-4b93-bf12-b41e7ce49cdb\", " + " \"security_groups\": [], "
                + " \"device_id\": \"\" }, { " + " \"status\": \"DOWN\", " + " \"name\": \"sample_port_2\", "
                + " \"allowed_address_pairs\": [], " + " \"admin_state_up\": false, "
                + " \"port_security_enabled\": false,"
                + " \"network_id\": \"4e8e5957-649f-477b-9e5b-f1f75b21c03c\", "
                + " \"tenant_id\": \"d6700c0c9ffa4f1cb322cd4a1f3906fa\", " + " \"device_owner\": \"\", "
                + " \"mac_address\": \"fa:16:3e:f4:73:df\", " + " \"fixed_ips\": [ { "
                + "\"subnet_id\": \"3b80198d-4f7b-4f77-9ef5-774d54e17126\"," + " \"ip_address\": \"10.0.0.6\" } ], "
                + " \"id\": \"43c831e0-19ce-4a76-9a49-57b57e69428b\", " + " \"security_groups\": [], "
                + " \"device_id\": \"\" } ] } ";
        ITNeutronE2E.test_create(url, content, "Bulk Port Post Failed");
    }

    public void port_update_test() {
        String url = base + "/ports/43c831e0-19ce-4a76-9a49-57b57e69428b";
        String content = " { \"port\": { " + " \"status\": \"DOWN\", "
                + " \"binding:host_id\": \"00000000-1111-2222-3333-444444444444\", "
                + " \"allowed_address_pairs\": [ { " + " \"mac_address\": \"fa:16:3e:11:11:5e\", "
                + " \"ip_address\": \"192.168.1.200/32\" } ], " + " \"extra_dhcp_opts\": [], "
                + " \"device_owner\": \"compute:nova\", " + " \"binding:profile\": {}, " + " \"fixed_ips\": [ { "
                + "\"subnet_id\": \"3b80198d-4f7b-4f77-9ef5-774d54e17126\"," + " \"ip_address\": \"10.0.0.7\" } ], "
                + " \"id\": \"43c831e0-19ce-4a76-9a49-57b57e69428b\", " + " \"security_groups\": [], "
                + " \"device_id\": \"\", " + " \"name\": \"test-for-port-update\", " + " \"admin_state_up\": true, "
                + " \"port_security_enabled\": true,"
                + " \"network_id\": \"4e8e5957-649f-477b-9e5b-f1f75b21c03c\", "
                + " \"tenant_id\": \"522eda8d23124b25bf03fe44f1986b74\", " + " \"binding:vif_details\": {}, "
                + " \"binding:vnic_type\": \"normal\", " + " \"binding:vif_type\": \"binding_failed\", "
                + " \"mac_address\": \"fa:16:3e:11:11:5e\" } } ";
        ITNeutronE2E.test_modify(url, content, "Port Put Failed");
    }

    public void port_element_get_test() {
        String url = base + "/ports/43c831e0-19ce-4a76-9a49-57b57e69428b";
        ITNeutronE2E.test_fetch(url, true, "Port Element Get Failed");
    }

    public void port_element_get_with_query_test() {
        String url = base + "/ports/43c831e0-19ce-4a76-9a49-57b57e69428b"
                + "?fields=id&fields=network_id&fields=name&fields=admin_state_up&fields=port_seurity_enabled"
                + "&fields=status&fields=mac_address&fields=device_id&fields=tenant_id"
                + "&fields=device_owner&fields=limit&fields=marker&fields=page_reverse";
        ITNeutronE2E.test_fetch(url, true, "Port Element Get With Query Failed");
    }

    public void port_element_get_with_query_content_validation_test() {
        String element = "allowed_address_pairs";
        String url = base + "/ports/43c831e0-19ce-4a76-9a49-57b57e69428b"
                + "?fields=" + element;
        String expectedContent = "[{\"mac_address\":\"fa:16:3e:11:11:5e\",\"ip_address\":\"192.168.1.200/32\"}]";
        String context = "Port details do not match.";
        JsonObject jsonObjectOutput = ITNeutronE2E.test_fetch_gson(url, context);
        jsonObjectOutput = jsonObjectOutput.getAsJsonObject("port");
        JsonElement jsonElementValue = jsonObjectOutput.get(element);
        Assert.assertEquals(context, expectedContent, String.valueOf(jsonElementValue));
        element = "port_security_enabled";
        url = base + "/ports/43c831e0-19ce-4a76-9a49-57b57e69428b" + "?fields=" + element;
        jsonObjectOutput = ITNeutronE2E.test_fetch_gson(url, context);
        jsonObjectOutput = jsonObjectOutput.getAsJsonObject("port");
        jsonElementValue = jsonObjectOutput.get(element);
        Assert.assertEquals(context, true,  jsonElementValue.getAsBoolean());
    }

    public void port_delete_test() {
        String url = base + "/ports/43c831e0-19ce-4a76-9a49-57b57e69428b";
        ITNeutronE2E.test_delete(url, "Port Element Delete Failed");
    }

    public void port_element_negative_get_test() {
        String url = base + "/ports/43c831e0-19ce-4a76-9a49-57b57e69428b";
        ITNeutronE2E.test_fetch(url, false, "Port Element Get Failed");
    }

    public static void runTests(String base) {
        NeutronPortTests portTester = new NeutronPortTests(base);
        String createJsonString = portTester.singleton_port_create_test();
        portTester.singleton_port_get_with_one_query_item_test(createJsonString);
        portTester.singleton_default_port_create_test();
        portTester.default_port_content_validation_test();
        portTester.router_interface_port_create_test(); //needed for router test
        portTester.bulk_port_create_test();
        portTester.port_update_test();
        portTester.port_element_get_test();
        portTester.port_element_get_with_query_content_validation_test();
        portTester.port_element_get_with_query_test();
        portTester.port_collection_get_test();
        portTester.port_delete_test();
        portTester.port_element_negative_get_test();
        portTester.binding_profile_port_create_test();
    }
}
