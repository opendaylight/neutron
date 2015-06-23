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

public class Neutron_Bug3812_Tests {
    String base;

    public Neutron_Bug3812_Tests(String base) {
        this.base = base;
    }

    public void check_bug3812() {
        // create network
        String url_n = base + "/networks";
        String content_n = "{\"network\": {\"name\": \"test\", \"provider:physical_network\": \"\", \"router:external\": false, \"tenant_id\": \"04684ce029a6415ca8a9293a24f884b9\", \"admin_state_up\": true, \"shared\": false, \"provider:network_type\": \"vxlan\", \"id\": \"ec36ae5a-ff7f-4441-8229-179e5d5207a7\", \"provider:segmentation_id\": 2550}}";
        ITNeutronE2E.test_create(url_n, content_n, "Bug 3812 Network Post Failed");

        // create first subnet
        String url_s = base + "/subnets";
        String content_s1 = "{\"subnet\": {\"name\": \"s1\", \"enable_dhcp\": true, \"network_id\": \"ec36ae5a-ff7f-4441-8229-179e5d5207a7\", \"tenant_id\": \"04684ce029a6415ca8a9293a24f884b9\", \"dns_nameservers\": [], \"gateway_ip\": \"10.0.0.1\", \"ipv6_ra_mode\": \"\", \"allocation_pools\": [{\"start\": \"10.0.0.2\", \"end\": \"10.0.3.254\"}], \"host_routes\": [], \"shared\": false, \"ip_version\": 4, \"ipv6_address_mode\": \"\", \"cidr\": \"10.0.0.0/22\", \"id\": \"64605c41-688d-4548-97da-0f895943f840\", \"subnetpool_id\": \"\"}}";
        ITNeutronE2E.test_create(url_s, content_s1, "Bug 3812 Subnet 1 Post Failed");

        String url_p = base + "/ports";
        String content_s1p1 = "{\"port\": {\"binding:host_id\": \"libra\", \"allowed_address_pairs\": [], \"device_owner\": \"network:dhcp\", \"binding:profile\": {}, \"fixed_ips\": [{\"subnet_id\": \"64605c41-688d-4548-97da-0f895943f840\", \"ip_address\": \"10.0.0.2\"}], \"id\": \"fcd1d7ab-8486-42a0-8f60-9d1a682aa00e\", \"security_groups\": [], \"device_id\": \"dhcp1c8f692f-b8db-5449-80ea-c9243b652e59-ec36ae5a-ff7f-4441-8229-179e5d5207a7\", \"name\": \"\", \"admin_state_up\": true, \"network_id\": \"ec36ae5a-ff7f-4441-8229-179e5d5207a7\", \"tenant_id\": \"04684ce029a6415ca8a9293a24f884b9\", \"binding:vif_details\": {}, \"binding:vnic_type\": \"normal\", \"binding:vif_type\": \"unbound\", \"mac_address\": \"FA:16:3E:02:CB:DE\"}}";
        ITNeutronE2E.test_create(url_p, content_s1p1, "Bug 3812 Subnet 1 Dependent Port 1 Post Failed");

        String url_p1 = url_p + "/fcd1d7ab-8486-42a0-8f60-9d1a682aa00e";
        String content_s1p1m = "{\"port\": {\"binding:host_id\": \"libra\", \"allowed_address_pairs\": [], \"extra_dhcp_opts\": [], \"device_owner\": \"network:dhcp\", \"binding:profile\": {}, \"security_groups\": [], \"device_id\": \"dhcp1c8f692f-b8db-5449-80ea-c9243b652e59-ec36ae5a-ff7f-4441-8229-179e5d5207a7\", \"name\": \"\", \"admin_state_up\": true, \"binding:vif_details\": {\"port_filter\": true}, \"binding:vnic_type\": \"normal\", \"binding:vif_type\": \"ovs\"}}";
        ITNeutronE2E.test_modify(url_p1, content_s1p1m, "Bug 3812 Subnet 1 Dependent Port 1 Put Failed");
    }

}
