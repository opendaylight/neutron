/*
 * Copyright (C) 2014 Red Hat, Inc.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.spi;

import java.io.Serializable;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * OpenStack Neutron v2.0 Load Balancer as a service
 * (LBaaS) bindings. See OpenStack Network API
 * v2.0 Reference for description of  the fields:
 * Implemented fields are as follows:
 *
 * <p>
 * id                 uuid-str
 * tenant_id          uuid-str
 * name               String
 * status             String
 * vip_address        IP address
 * vip_subnet         uuid-str
 * http://docs.openstack.org/api/openstack-network/2.0/openstack-network.pdf
 */

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public final class NeutronLoadBalancer extends NeutronAdminAttributes<NeutronLoadBalancer> implements Serializable {
    private static final Logger LOGGER = LoggerFactory.getLogger(NeutronLoadBalancer.class);
    private static final long serialVersionUID = 1L;

    @XmlElement(name = "vip_address")
    String loadBalancerVipAddress;

    @XmlElement(name = "vip_subnet_id")
    String loadBalancerVipSubnetID;

    public String getLoadBalancerVipAddress() {
        return loadBalancerVipAddress;
    }

    public void setLoadBalancerVipAddress(String loadBalancerVipAddress) {
        this.loadBalancerVipAddress = loadBalancerVipAddress;
    }

    public String getLoadBalancerVipSubnetID() {
        return loadBalancerVipSubnetID;
    }

    public void setLoadBalancerVipSubnetID(String loadBalancerVipSubnetID) {
        this.loadBalancerVipSubnetID = loadBalancerVipSubnetID;
    }

    public NeutronLoadBalancer extractFields(List<String> fields) {
        NeutronLoadBalancer ans = new NeutronLoadBalancer();
        for (String s : fields) {
            if (extractField(s, ans)) {
                continue;
            }
            switch (s) {
                case "vip_address":
                    ans.setLoadBalancerVipAddress(this.getLoadBalancerVipAddress());
                    break;
                case "vip_subnet_id":
                    ans.setLoadBalancerVipSubnetID(this.getLoadBalancerVipSubnetID());
                    break;
                default:
                    LOGGER.warn("{} is not a NeutronLoadBalancer suitable field.", s);
                    break;
            }
        }
        return ans;
    }

    @Override
    public String toString() {
        return "NeutronLoadBalancer{" + "loadBalancerID='" + uuid + '\'' + ", loadBalancerTenantID='" + tenantID + '\''
                + ", loadBalancerName='" + name + '\'' + ", loadBalancerStatus='" + status
                + '\'' + ", loadBalancerAdminStateUp='" + adminStateUp + '\'' + ", loadBalancerVipAddress='"
                + loadBalancerVipAddress + '\'' + ", loadBalancerVipSubnetID='" + loadBalancerVipSubnetID + '\'' + '}';
    }
}
