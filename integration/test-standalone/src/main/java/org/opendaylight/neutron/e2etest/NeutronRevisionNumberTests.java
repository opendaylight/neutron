/*
 * Copyright (C) 2017 Intel, Corp.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.e2etest;

public class NeutronRevisionNumberTests {

    String base;

    public NeutronRevisionNumberTests(String base) {
        this.base = base;
    }

    public void subnet_create_test() {
        String url = base + "/subnets";
        String content = " { \"subnets\": [ " + " { \"allocation_pools\": [ "
                + " { \"end\": \"192.168.199.254\", \"start\": \"192.168.199.2\" } ], "
                + " \"cidr\": \"192.168.199.0/24\", " + " \"dns_nameservers\": [\"8.8.8.8\"], "
                + " \"enable_dhcp\": true, " + " \"gateway_ip\": \"192.168.199.1\", "
                + " \"host_routes\":[ { \"destination\":\"0.0.0.0/0\", " + " \"nexthop\":\"192.168.199.3\" }, "
                + " { \"destination\":\"192.168.0.0/24\", " + " \"nexthop\":\"192.168.199.4\" } ], "
                + " \"id\": \"1468a7a7-290d-4127-aedd-6c9449775a24\", " + " \"ip_version\": 4, " + " \"name\": \"\", "
                + " \"network_id\": \"af374017-c9ae-4a1d-b799-ab73111476e2\", "
                + " \"tenant_id\": \"4fd44f30292945e481c7b8a0c8908869\" }, { "
                + " \"allocation_pools\": [ { \"end\": \"10.56.7.254\", \"start\": \"10.56.4.2\" } ], "
                + " \"cidr\": \"10.56.4.0/22\", " + " \"dns_nameservers\": [\"8.8.8.8\", \"8.8.8.4\"], "
                + " \"enable_dhcp\": true, " + " \"gateway_ip\": \"10.56.4.1\", "
                + " \"host_routes\":[ { \"destination\":\"0.0.0.0/0\", " + " \"nexthop\":\"10.56.4.3\" }, "
                + " { \"destination\":\"192.168.0.0/24\", " + " \"nexthop\":\"10.56.4.4\" } ], "
                + " \"id\": \"c0e7435c-1512-45fb-aa9e-9a7c5932fb30\", " + " \"ip_version\": 4, " + " \"name\": \"\", "
                + " \"network_id\": \"af374017-c9ae-4a1d-b799-ab73111476e2\", "
                + " \"tenant_id\": \"4fd44f30292945e481c7b8a0c8908869\" } ] }";
        HttpUtils.test_create(url, content, "Revision Number Post Failed");
    }

    public void subnet_update_test() {
        String url = base + "/subnets/c0e7435c-1512-45fb-aa9e-9a7c5932fb30";
        String content = " { \"subnet\": { " + " \"name\": \"my_subnet\", " + " \"enable_dhcp\": true, "
                + " \"network_id\": \"af374017-c9ae-4a1d-b799-ab73111476e2\", "
                + " \"tenant_id\": \"4fd44f30292945e481c7b8a0c8908869\", "
                + " \"dns_nameservers\": [\"8.8.8.8\", \"8.8.8.4\"], "
                + " \"allocation_pools\": [ { \"start\": \"10.0.0.2\", \"end\": \"10.0.0.254\" } ], "
                + " \"host_routes\": [{ \"destination\":\"192.168.0.0/24\", " + " \"nexthop\":\"10.0.0.11\" } ], "
                + " \"ip_version\": 4, " + " \"gateway_ip\": \"10.0.0.1\", " + " \"cidr\": \"10.0.0.0/24\", "
                + "\"revision_number\": 3, "
                + " \"id\": \"c0e7435c-1512-45fb-aa9e-9a7c5932fb30\" } }";
        HttpUtils.test_modify(url, content, "Revision Number Put Failed");
    }

    public void subnet_update_test_with_old_value() {
        String url = base + "/subnets/c0e7435c-1512-45fb-aa9e-9a7c5932fb30";
        String content = " { \"subnet\": { " + " \"name\": \"my_subnet\", " + " \"enable_dhcp\": true, "
                + " \"network_id\": \"af374017-c9ae-4a1d-b799-ab73111476e2\", "
                + " \"tenant_id\": \"4fd44f30292945e481c7b8a0c8908869\", "
                + " \"dns_nameservers\": [\"8.8.8.8\", \"8.8.8.4\"], "
                + " \"allocation_pools\": [ { \"start\": \"10.0.0.2\", \"end\": \"10.0.0.254\" } ], "
                + " \"host_routes\": [{ \"destination\":\"192.168.0.0/24\", " + " \"nexthop\":\"10.0.0.11\" } ], "
                + " \"ip_version\": 4, " + " \"gateway_ip\": \"10.0.0.1\", " + " \"cidr\": \"10.0.0.0/24\", "
                + "\"revision_number\": 2, "
                + " \"id\": \"c0e7435c-1512-45fb-aa9e-9a7c5932fb30\" } }";
        HttpUtils.test_modify(url, content, "Revision Number Put Failed");
    }

    public void subnet_update_test_with_no_value() {
        String url = base + "/subnets/c0e7435c-1512-45fb-aa9e-9a7c5932fb30";
        String content = " { \"subnet\": { " + " \"name\": \"my_subnet\", " + " \"enable_dhcp\": true, "
                + " \"network_id\": \"af374017-c9ae-4a1d-b799-ab73111476e2\", "
                + " \"tenant_id\": \"4fd44f30292945e481c7b8a0c8908869\", "
                + " \"dns_nameservers\": [\"8.8.8.8\", \"8.8.8.4\"], "
                + " \"allocation_pools\": [ { \"start\": \"10.0.0.2\", \"end\": \"10.0.0.254\" } ], "
                + " \"host_routes\": [{ \"destination\":\"192.168.0.0/24\", " + " \"nexthop\":\"10.0.0.11\" } ], "
                + " \"ip_version\": 4, " + " \"gateway_ip\": \"10.0.0.1\", " + " \"cidr\": \"10.0.0.0/24\", "
                + " \"id\": \"c0e7435c-1512-45fb-aa9e-9a7c5932fb30\" } }";
        HttpUtils.test_modify(url, content, "Revision Number Put Failed");
    }

    public void subnet_element_get_test() {
        String url = base + "/subnets/c0e7435c-1512-45fb-aa9e-9a7c5932fb30";
        HttpUtils.test_fetch(url, true, "Revision Number Element Get Failed");
    }

    public void subnet_delete_test() {
        String url = base + "/subnets/c0e7435c-1512-45fb-aa9e-9a7c5932fb30";
        HttpUtils.test_delete(url, "Revision Number Element Delete Failed");
    }

    public static void runTests(String base) {
        NeutronRevisionNumberTests tester = new NeutronRevisionNumberTests(base);
        tester.subnet_create_test();
        tester.subnet_element_get_test();
        tester.subnet_update_test();
        tester.subnet_update_test_with_old_value();
        tester.subnet_update_test_with_no_value();
        tester.subnet_delete_test();
    }
}
