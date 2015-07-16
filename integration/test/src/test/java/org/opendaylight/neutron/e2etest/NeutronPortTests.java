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
        String url = base + "/ports";
        ITNeutronE2E.test_fetch(url, "Ports Collection GET failed");
    }

    //TODO handle SB check
    public void singleton_port_create_test() {
        String url = base + "/ports";
        String content = "{ \"port\": { \"status\": \"DOWN\","+
            "\"binding:host_id\": \"\","+
            "\"name\": \"private-port\","+
            "\"allowed_address_pairs\": [],"+
            "\"admin_state_up\": true,"+
            "\"network_id\": \"4e8e5957-649f-477b-9e5b-f1f75b21c03c\","+
            "\"tenant_id\": \"9bacb3c5d39d41a79512987f338cf177\","+
            "\"binding:vif_details\": {},"+
            "\"binding:vnic_type\": \"normal\","+
            "\"binding:vif_type\": \"unbound\","+
            "\"device_owner\": \"\","+
            "\"mac_address\": \"fa:16:3e:c9:cb:f0\","+
            "\"binding:profile\": {},"+
            "\"fixed_ips\": [ {"+
                "\"subnet_id\": \"3b80198d-4f7b-4f77-9ef5-774d54e17126\","+
                "\"ip_address\": \"10.0.0.2\" } ],"+
            "\"id\": \"65c0ee9f-d634-4522-8954-51021b570b0d\","+
            "\"security_groups\": [] ,"+
            "\"device_id\": \"\" } }";
        ITNeutronE2E.test_create(url, content,"Singleton Port Post Failed NB");
    }

    //TODO handle SB check
    public void router_interface_port_create_test() {
        String url = base + "/ports";
        String content = "{\"ports\": [ {\"status\": \"DOWN\", \"binding:host_id\": \"\", \"allowed_address_pairs\": [], \"device_owner\": \"network:router_gateway\", \"binding:profile\": {}, \"fixed_ips\": [{\"subnet_id\": \"3b80198d-4f7b-4f77-9ef5-774d54e17126\", \"ip_address\": \"10.0.0.1\"}], \"id\": \"d8a4cc85-ad78-46ac-b5a1-8e04f16fa51e\", \"security_groups\": [], \"device_id\": \"8604a0de-7f6b-409a-a47c-a1cc7bc77b2e\", \"name\": \"\", \"admin_state_up\": true, \"network_id\": \"4e8e5957-649f-477b-9e5b-f1f75b21c03c\", \"tenant_id\": \"9bacb3c5d39d41a79512987f338cf177\", \"binding:vif_details\": {}, \"binding:vnic_type\": \"normal\", \"binding:vif_type\": \"unbound\", \"mac_address\": \"fa:16:3e:dc:1d:8d\"}, "
            + " {\"status\": \"DOWN\", \"binding:host_id\": \"\", \"allowed_address_pairs\": [], \"device_owner\": \"network:router_gateway\", \"binding:profile\": {}, \"fixed_ips\": [{\"subnet_id\": \"3b80198d-4f7b-4f77-9ef5-774d54e17126\", \"ip_address\": \"10.0.0.2\"}], \"id\": \"d8a4cc85-ad78-46ac-b5a1-8e04f16fa51f\", \"security_groups\": [], \"device_id\": \"8604a0de-7f6b-409a-a47c-a1cc7bc77b2f\", \"name\": \"\", \"admin_state_up\": true, \"network_id\": \"4e8e5957-649f-477b-9e5b-f1f75b21c03c\", \"tenant_id\": \"9bacb3c5d39d41a79512987f338cf177\", \"binding:vif_details\": {}, \"binding:vnic_type\": \"normal\", \"binding:vif_type\": \"unbound\", \"mac_address\": \"fa:16:3e:dc:1d:8e\"} ] }";
        ITNeutronE2E.test_create(url, content, "Router Interface Port Post Failed NB");
    }

    public void bulk_port_create_test() {
        String url = base + "/ports";
        String content = "{ \"ports\": [ { "
            + " \"status\": \"DOWN\", "
            + " \"name\": \"sample_port_1\", "
            + " \"allowed_address_pairs\": [], "
            + " \"admin_state_up\": false, "
            + " \"network_id\": \"4e8e5957-649f-477b-9e5b-f1f75b21c03c\", "
            + " \"tenant_id\": \"d6700c0c9ffa4f1cb322cd4a1f3906fa\", "
            + " \"device_owner\": \"\", "
            + " \"mac_address\": \"fa:16:3e:48:b8:9f\", "
            + " \"fixed_ips\": [ { "
            + "\"subnet_id\": \"3b80198d-4f7b-4f77-9ef5-774d54e17126\","
            + " \"ip_address\": \"10.0.0.5\" } ], "
            + " \"id\": \"94225baa-9d3f-4b93-bf12-b41e7ce49cdb\", "
            + " \"security_groups\": [], "
            + " \"device_id\": \"\" }, { "
            + " \"status\": \"DOWN\", "
            + " \"name\": \"sample_port_2\", "
            + " \"allowed_address_pairs\": [], "
            + " \"admin_state_up\": false, "
            + " \"network_id\": \"4e8e5957-649f-477b-9e5b-f1f75b21c03c\", "
            + " \"tenant_id\": \"d6700c0c9ffa4f1cb322cd4a1f3906fa\", "
            + " \"device_owner\": \"\", "
            + " \"mac_address\": \"fa:16:3e:f4:73:df\", "
            + " \"fixed_ips\": [ { "
            + "\"subnet_id\": \"3b80198d-4f7b-4f77-9ef5-774d54e17126\","
            + " \"ip_address\": \"10.0.0.6\" } ], "
            + " \"id\": \"43c831e0-19ce-4a76-9a49-57b57e69428b\", "
            + " \"security_groups\": [], "
            + " \"device_id\": \"\" } ] } ";
        ITNeutronE2E.test_create(url, content, "Bulk Subnet Post Failed");
    }

    public void port_update_test() {
        String url = base + "/ports/43c831e0-19ce-4a76-9a49-57b57e69428b";
        String content = " { \"port\": { "
        + " \"status\": \"DOWN\", "
        + " \"binding:host_id\": \"test_for_port_update_host\", "
        + " \"allowed_address_pairs\": [], "
        + " \"extra_dhcp_opts\": [], "
        + " \"device_owner\": \"compute:nova\", "
        + " \"binding:profile\": {}, "
        + " \"fixed_ips\": [ { "
        + "\"subnet_id\": \"3b80198d-4f7b-4f77-9ef5-774d54e17126\","
        + " \"ip_address\": \"10.0.0.7\" } ], "
        + " \"id\": \"43c831e0-19ce-4a76-9a49-57b57e69428b\", "
        + " \"security_groups\": [], "
        + " \"device_id\": \"\", "
        + " \"name\": \"test-for-port-update\", "
        + " \"admin_state_up\": true, "
        + " \"network_id\": \"4e8e5957-649f-477b-9e5b-f1f75b21c03c\", "
        + " \"tenant_id\": \"522eda8d23124b25bf03fe44f1986b74\", "
        + " \"binding:vif_details\": {}, "
        + " \"binding:vnic_type\": \"normal\", "
        + " \"binding:vif_type\": \"binding_failed\", "
        + " \"mac_address\": \"fa:16:3e:11:11:5e\" } } ";
        ITNeutronE2E.test_modify(url, content,"Subnet Put Failed");
    }

    public void port_element_get_test() {
        String url = base + "/ports/43c831e0-19ce-4a76-9a49-57b57e69428b";
        ITNeutronE2E.test_fetch(url, true, "Subnet Element Get Failed");
    }

    public void port_delete_test() {
        String url = base + "/ports/43c831e0-19ce-4a76-9a49-57b57e69428b";
        ITNeutronE2E.test_delete(url, "Subnet Element Delete Failed");
    }

    public void port_element_negative_get_test() {
        String url = base + "/ports/43c831e0-19ce-4a76-9a49-57b57e69428b";
        ITNeutronE2E.test_fetch(url, false, "Subnet Element Get Failed");
    }
}
