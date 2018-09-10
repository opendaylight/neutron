/*
 * Copyright (C) 2014 Red Hat, Inc.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.neutron.spi;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

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
public final class NeutronLoadBalancer extends NeutronAdminAttributes<NeutronLoadBalancer> {
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

    @Override
    protected boolean extractField(String field, NeutronLoadBalancer ans) {
        switch (field) {
            case "vip_address":
                ans.setLoadBalancerVipAddress(this.getLoadBalancerVipAddress());
                break;
            case "vip_subnet_id":
                ans.setLoadBalancerVipSubnetID(this.getLoadBalancerVipSubnetID());
                break;
            default:
                return super.extractField(field, ans);
        }
        return true;
    }

    @Override
    public String toString() {
        return "NeutronLoadBalancer{" + "loadBalancerID='" + uuid + '\'' + ", loadBalancerTenantID='" + getTenantID()
                + '\'' + ", loadBalancerName='" + name + '\'' + ", loadBalancerStatus='" + status + '\''
                + ", loadBalancerAdminStateUp='" + adminStateUp + '\'' + ", loadBalancerVipAddress='"
                + loadBalancerVipAddress + '\'' + ", loadBalancerVipSubnetID='" + loadBalancerVipSubnetID + '\'' + '}';
    }
}
