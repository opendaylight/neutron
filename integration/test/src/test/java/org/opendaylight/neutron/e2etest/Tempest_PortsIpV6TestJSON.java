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

public class Tempest_PortsIpV6TestJSON {
    String base;

    public Tempest_PortsIpV6TestJSON(String base) {
        this.base = base;
    }

    public void test_create_port_in_allowed_allocation_pools() {
        // create network
        String url_n = base + "/networks";
        String content_n = " {\"network\": {\"status\": \"ACTIVE\", \"subnets\": [], \"name\": \"test-network--1850822235\", \"router:external\": false, \"tenant_id\": \"4c1be4874f0048fc8205acffe2821cd3\", \"admin_state_up\": true, \"mtu\": 0, \"shared\": false, \"port_security_enabled\": true, \"id\": \"114ddf69-8ccd-46bb-92fb-bc3e921318d4\"}}";
        ITNeutronE2E.test_create(url_n, content_n, "test_create_port_in_allowed_allocation_pools Network Post Failed");

        // create subnet
        String url_s = base + "/subnets";
        String content_s1 = "{\"subnet\": {\"name\": \"\", \"enable_dhcp\": true, \"network_id\": \"114ddf69-8ccd-46bb-92fb-bc3e921318d4\", \"tenant_id\": \"4c1be4874f0048fc8205acffe2821cd3\", \"dns_nameservers\": [], \"gateway_ip\": \"2003::1\", \"ipv6_ra_mode\": null, \"allocation_pools\": [{\"start\": \"2003::4\", \"end\": \"2003::6\"}], \"host_routes\": [], \"ip_version\": 6, \"ipv6_address_mode\": null, \"cidr\": \"2003::/64\", \"id\": \"77c68c25-72a6-415b-a6f0-886fe26f1b02\", \"subnetpool_id\": null}}";
        ITNeutronE2E.test_create(url_s, content_s1, "test_create_port_in_allowed_allocation_pools Subnet Post Failed");

        String url_p = base + "/ports";
        String content_p1 = "{ \"port\": {\"status\": \"ACTIVE\", \"binding:host_id\": \"odl-devstack\", \"allowed_address_pairs\": [], \"extra_dhcp_opts\": [], \"device_owner\": \"network:dhcp\", \"port_security_enabled\": false, \"binding:profile\": {}, \"fixed_ips\": [{\"subnet_id\": \"77c68c25-72a6-415b-a6f0-886fe26f1b02\", \"ip_address\": \"2003::5\"}], \"id\": \"7d8f5c18-fcde-471e-8a64-3dfd249cae92\", \"security_groups\": [], \"device_id\": \"dhcpff2867ff-b137-5086-a214-70bb12c3ea19-114ddf69-8ccd-46bb-92fb-bc3e921318d4\", \"name\": \"\", \"admin_state_up\": true, \"network_id\": \"114ddf69-8ccd-46bb-92fb-bc3e921318d4\", \"tenant_id\": \"4c1be4874f0048fc8205acffe2821cd3\", \"binding:vif_details\": {\"port_filter\": \"true\"}, \"binding:vnic_type\": \"normal\", \"binding:vif_type\": \"ovs\", \"mac_address\": \"fa:16:3e:09:db:75\"}}";
        ITNeutronE2E.test_create(url_p, content_p1, "test_create_port_in_allowed_allocation_pools Port Post Failed");

        String content_p2 = "{\"port\": {\"binding:host_id\": \"\", \"allowed_address_pairs\": [], \"device_owner\": \"\", \"port_security_enabled\": true, \"binding:profile\": {}, \"fixed_ips\": [{\"subnet_id\": \"77c68c25-72a6-415b-a6f0-886fe26f1b02\", \"ip_address\": \"2003::4\"}], \"id\": \"856c48ea-fd3a-4ee2-b0c6-bf86f8813888\", \"security_groups\": [{\"tenant_id\": \"4c1be4874f0048fc8205acffe2821cd3\", \"description\": \"Default security group\", \"id\": \"111f2d2d-4fe3-4679-b43a-17857ce91cab\", \"security_group_rules\": [{\"remote_group_id\": \"\", \"direction\": \"egress\", \"remote_ip_prefix\": \"\", \"protocol\": \"\", \"ethertype\": \"IPv6\", \"tenant_id\": \"4c1be4874f0048fc8205acffe2821cd3\", \"port_range_max\": \"\", \"port_range_min\": \"\", \"id\": \"7b506947-9f16-444e-b027-33a11aaed6bb\", \"security_group_id\": \"111f2d2d-4fe3-4679-b43a-17857ce91cab\"}, {\"remote_group_id\": \"111f2d2d-4fe3-4679-b43a-17857ce91cab\", \"direction\": \"ingress\", \"remote_ip_prefix\": \"\", \"protocol\": \"\", \"ethertype\": \"IPv6\", \"tenant_id\": \"4c1be4874f0048fc8205acffe2821cd3\", \"port_range_max\": \"\", \"port_range_min\": \"\", \"id\": \"7ba1a85b-3e05-44b5-90dd-0c50fe01ac46\", \"security_group_id\": \"111f2d2d-4fe3-4679-b43a-17857ce91cab\"}, {\"remote_group_id\": \"111f2d2d-4fe3-4679-b43a-17857ce91cab\", \"direction\": \"ingress\", \"remote_ip_prefix\": \"\", \"protocol\": \"\", \"ethertype\": \"IPv4\", \"tenant_id\": \"4c1be4874f0048fc8205acffe2821cd3\", \"port_range_max\": \"\", \"port_range_min\": \"\", \"id\": \"7f1ad334-7588-4ab5-ab02-d1080d401f66\", \"security_group_id\": \"111f2d2d-4fe3-4679-b43a-17857ce91cab\"}, {\"remote_group_id\": \"\", \"direction\": \"egress\", \"remote_ip_prefix\": \"\", \"protocol\": \"\", \"ethertype\": \"IPv4\", \"tenant_id\": \"4c1be4874f0048fc8205acffe2821cd3\", \"port_range_max\": \"\", \"port_range_min\": \"\", \"id\": \"862f79a2-f96c-4b64-8fbe-50a77b38bd77\", \"security_group_id\": \"111f2d2d-4fe3-4679-b43a-17857ce91cab\"}], \"name\": \"default\"}], \"device_id\": \"\", \"name\": \"\", \"admin_state_up\": true, \"network_id\": \"114ddf69-8ccd-46bb-92fb-bc3e921318d4\", \"tenant_id\": \"4c1be4874f0048fc8205acffe2821cd3\", \"binding:vif_details\": {}, \"binding:vnic_type\": \"normal\", \"binding:vif_type\": \"unbound\", \"mac_address\": \"FA:16:3E:66:B1:38\"}}";
        ITNeutronE2E.test_create(url_p, content_p2, "test_create_port_in_allowed_allocation_pools Port Post 2 Failed");

    }

    public static void runTests(String base) {
       Tempest_PortsIpV6TestJSON tpv6runner = new Tempest_PortsIpV6TestJSON(base);
       tpv6runner.test_create_port_in_allowed_allocation_pools();
    }
}
