/*
 * Copyright (C) 2015 IBM, Inc.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.e2etest;

public class Neutron_Bug3812_Tests {
    String base;

    public Neutron_Bug3812_Tests(String base) {
        this.base = base;
    }

    public void check_bug3812() {
        // create network
        String url_n = base + "/networks";
        String content_n = "{\"network\": {\"name\": \"test\", \"provider:physical_network\": \"\", "
                + "\"router:external\": false, \"tenant_id\": \"04684ce029a6415ca8a9293a24f884b9\", "
                + "\"admin_state_up\": true, \"shared\": false, \"provider:network_type\": \"vxlan\", "
                + "\"id\": \"ec36ae5a-ff7f-4441-8229-179e5d5207a7\", \"provider:segmentation_id\": 2550}}";
        ITNeutronE2E.test_create(url_n, content_n, "Bug 3812 Network Post Failed");

        // create first subnet
        String url_s = base + "/subnets";
        String content_s1 = "{\"subnet\": {\"name\": \"s1\", \"enable_dhcp\": true, "
                + "\"network_id\": \"ec36ae5a-ff7f-4441-8229-179e5d5207a7\","
                + " \"tenant_id\": \"04684ce029a6415ca8a9293a24f884b9\", \"dns_nameservers\": [], "
                + "\"gateway_ip\": \"10.0.0.1\", \"ipv6_ra_mode\": \"\", \"allocation_pools\": "
                + "[{\"start\": \"10.0.0.2\", \"end\": \"10.0.3.254\"}], \"host_routes\": [], \"shared\": false,"
                + " \"ip_version\": 4, \"ipv6_address_mode\": \"\", \"cidr\": \"10.0.0.0/22\", "
                + "\"id\": \"64605c41-688d-4548-97da-0f895943f840\", \"subnetpool_id\": \"\"}}";
        ITNeutronE2E.test_create(url_s, content_s1, "Bug 3812 Subnet 1 Post Failed");

        String url_p = base + "/ports";
        String content_p1 = "{\"port\": {\"binding:host_id\": \"00000000-1111-2222-3333-444444444444\", "
                + "\"allowed_address_pairs\": [], \"device_owner\": \"network:dhcp\", "
                + "\"binding:profile\": {}, \"fixed_ips\": [{\"subnet_id\": \"64605c41-688d-4548-97da-0f895943f840\","
                + " \"ip_address\": \"10.0.0.2\"}], \"id\": \"fcd1d7ab-8486-42a0-8f60-9d1a682aa00e\", "
                + "\"security_groups\": [], "
                + "\"device_id\": \"dhcp1c8f692f-b8db-5449-80ea-c9243b652e59-ec36ae5a-ff7f-4441-8229-179e5d5207a7\","
                + " \"name\": \"\", \"admin_state_up\": true, "
                + "\"network_id\": \"ec36ae5a-ff7f-4441-8229-179e5d5207a7\","
                + " \"tenant_id\": \"04684ce029a6415ca8a9293a24f884b9\", \"binding:vif_details\": {}, "
                + "\"binding:vnic_type\": \"normal\", \"binding:vif_type\": \"unbound\","
                + " \"mac_address\": \"FA:16:3E:02:CB:DE\"}}";
        ITNeutronE2E.test_create(url_p, content_p1, "Bug 3812 DHCP Port 1 Post Failed");

        String url_p1 = url_p + "/fcd1d7ab-8486-42a0-8f60-9d1a682aa00e";
        String content_p1m1 = "{\"port\": {\"binding:host_id\": \"00000000-1111-2222-3333-444444444444\", "
                + "\"allowed_address_pairs\": [], \"extra_dhcp_opts\": [], \"device_owner\": \"network:dhcp\","
                + " \"network_id\": \"ec36ae5a-ff7f-4441-8229-179e5d5207a7\", \"binding:profile\": {},"
                + " \"security_groups\": [], "
                + "\"device_id\": \"dhcp1c8f692f-b8db-5449-80ea-c9243b652e59-ec36ae5a-ff7f-4441-8229-179e5d5207a7\","
                + " \"name\": \"\", \"admin_state_up\": true, \"binding:vif_details\": "
                + "{\"port_filter\": \"true\"}, \"binding:vnic_type\": \"normal\", \"binding:vif_type\": \"ovs\"}}";
        ITNeutronE2E.test_modify(url_p1, content_p1m1, "Bug 3812 DHCP Port 1 Put 1 Failed");

        // subnet 2 create
        String content_s2 = "{\"subnet\": {\"name\": \"s2\", \"enable_dhcp\": true, "
                + "\"network_id\": \"ec36ae5a-ff7f-4441-8229-179e5d5207a7\", "
                + "\"tenant_id\": \"04684ce029a6415ca8a9293a24f884b9\", "
                + "\"dns_nameservers\": [], \"gateway_ip\": \"20.0.0.1\", "
                + "\"ipv6_ra_mode\": \"\", \"allocation_pools\": "
                + "[{\"start\": \"20.0.0.2\", \"end\": \"20.0.3.254\"}], "
                + "\"host_routes\": [], \"shared\": false, \"ip_version\": 4, "
                + "\"ipv6_address_mode\": \"\", \"cidr\": \"20.0.0.0/22\", "
                + "\"id\": \"dd9b62eb-d9a3-42b5-b2f1-bffa43475614\", \"subnetpool_id\": \"\"}}";
        ITNeutronE2E.test_create(url_s, content_s2, "Bug 3812 Subnet 2 Post Failed");

        String content_p1m2 = "{\"port\": {\"binding:host_id\": \"00000000-1111-2222-3333-444444444444\", "
                + "\"allowed_address_pairs\": [], \"extra_dhcp_opts\": [], \"device_owner\": \"network:dhcp\", "
                + "\"network_id\": \"ec36ae5a-ff7f-4441-8229-179e5d5207a7\", \"binding:profile\": {}, "
                + "\"security_groups\": [], "
                + "\"device_id\": \"dhcp1c8f692f-b8db-5449-80ea-c9243b652e59-ec36ae5a-ff7f-4441-8229-179e5d5207a7\","
                + " \"name\": \"\", \"admin_state_up\": true, \"binding:vif_details\": "
                + "{\"port_filter\": \"true\"}, \"binding:vnic_type\": \"normal\", \"binding:vif_type\": \"ovs\"}}";
        ITNeutronE2E.test_modify(url_p1, content_p1m2, "Bug 3812 DHCP Port Put 2 Failed");

        // delete first subnet
        String content_p1m3 = "{\"port\": {\"binding:host_id\": \"00000000-1111-2222-3333-444444444444\","
                + " \"allowed_address_pairs\": [], \"extra_dhcp_opts\": [], \"device_owner\": \"network:dhcp\", "
                + "\"binding:profile\": {}, \"network_id\": \"ec36ae5a-ff7f-4441-8229-179e5d5207a7\", "
                + "\"security_groups\": [], "
                + "\"device_id\": \"dhcp1c8f692f-b8db-5449-80ea-c9243b652e59-ec36ae5a-ff7f-4441-8229-179e5d5207a7\", "
                + "\"name\": \"\", \"admin_state_up\": true, \"binding:vif_details\": "
                + "{\"port_filter\": \"true\"}, \"binding:vnic_type\": \"normal\", \"binding:vif_type\": \"ovs\"}}";
        ITNeutronE2E.test_modify(url_p1, content_p1m3, "Bug 3812 DHCP Port Put 3 Failed");

        String url_s1 = url_s + "/64605c41-688d-4548-97da-0f895943f840";
        ITNeutronE2E.test_delete(url_s1, "Bug 3812 Subnet 1 Delete Failed");

        String url_s2 = url_s + "/dd9b62eb-d9a3-42b5-b2f1-bffa43475614";
        ITNeutronE2E.test_delete(url_s2, "Bug 3812 Subnet 2 Delete Failed");
    }

    public static void runTests(String base) {
        Neutron_Bug3812_Tests bugTest = new Neutron_Bug3812_Tests(base);
        bugTest.check_bug3812();
    }
}
