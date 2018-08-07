/*
 * Copyright (C) 2015 IBM, Inc.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.e2etest;

public class NeutronBug4027Tests {
    String base;

    public NeutronBug4027Tests(String base) {
        this.base = base;
    }

    public void check_bug4027() {
        // create network
        String urlN = base + "/ports";
        String contentN = "{ \"port\" : {" + "\"id\" : \"ea2ac142-8454-4990-8bfb-7a218479864b\","
                + "\"network_id\" : \"e44cdc39-2d43-4775-82e8-b18da318da61\"," + "\"name\" : \"\","
                + "\"admin_state_up\" : true," + "\"mac_address\" : \"FA:16:3E:A2:44:3F\"," + "\"fixed_ips\" : [ {"
                + "\"ip_address\" : \"10.1.0.2\"," + "\"subnet_id\" : \"6aba69a3-a541-4b93-b724-7bf822e4986b\" } ],"
                + "\"device_id\" : \"dhcp58155ae3-f2e7-51ca-9978-71c513ab02ee-e44cdc39-2d43-4775-82e8-b18da318da61\","
                + "\"device_owner\" : \"network:dhcp\"," + "\"tenant_id\" : \"52e413cd075d47e4808f1845c7d8a2c8\","
                + "\"security_groups\" : [ ]," + "\"allowed_address_pairs\" : [ ],"
                + "\"binding:host_id\" : \"devstack-control\"," + "\"binding:vnic_type\" : \"normal\","
                + "\"binding:vif_type\" : \"unbound\"," + "\"binding:vif_details\" : { } } }";
        HttpUtils.test_create(urlN, 442, contentN, "Bug 4027 Port Post Failed");
        String url2 = base + "/ports/ea2ac142-8454-4990-8bfb-7a218479864b";
        String content2 = " { \"port\" : {" + "\"id\" : \"ea2ac142-8454-4990-8bfb-7a218479864b\"," + "\"name\" : \"\","
                + "\"admin_state_up\" : true,"
                + "\"device_id\" : \"dhcp58155ae3-f2e7-51ca-9978-71c513ab02ee-e44cdc39-2d43-4775-82e8-b18da318da61\","
                + "\"device_owner\" : \"network:dhcp\"," + "\"security_groups\" : [ ],"
                + "\"allowed_address_pairs\" : [ ]," + "\"binding:host_id\" : \"devstack-control\","
                + "\"binding:vnic_type\" : \"normal\"," + "\"binding:vif_type\" : \"ovs\","
                + "\"binding:vif_details\" : { \"port_filter\" : \"true\" }," + "\"extra_dhcp_opts\" : [ ] } } }";
        HttpUtils.test_modify(url2, 404, content2, "Bug 4027 Port Modify Failed");
    }

    public static void runTests(String base) {
        NeutronBug4027Tests bugTest = new NeutronBug4027Tests(base);
        bugTest.check_bug4027();
    }
}
