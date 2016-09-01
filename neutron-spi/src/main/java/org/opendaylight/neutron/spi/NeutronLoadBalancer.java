/*
 * Copyright (C) 2014 Red Hat, Inc.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.spi;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
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
public class NeutronLoadBalancer extends NeutronObject<NeutronLoadBalancer>
        implements Serializable, INeutronObject<NeutronLoadBalancer> {
    private static final long serialVersionUID = 1L;

    @XmlElement(name = "name")
    String loadBalancerName;

    @XmlElement(name = "status")
    String loadBalancerStatus;

    @XmlElement(name = "admin_state_up")
    Boolean loadBalancerAdminStateUp;

    @XmlElement(name = "vip_address")
    String loadBalancerVipAddress;

    @XmlElement(name = "vip_subnet_id")
    String loadBalancerVipSubnetID;

    public String getLoadBalancerName() {
        return loadBalancerName;
    }

    public void setLoadBalancerName(String loadBalancerName) {
        this.loadBalancerName = loadBalancerName;
    }

    public String getLoadBalancerStatus() {
        return loadBalancerStatus;
    }

    public void setLoadBalancerStatus(String loadBalancerStatus) {
        this.loadBalancerStatus = loadBalancerStatus;
    }

    public Boolean getLoadBalancerAdminStateUp() {
        return loadBalancerAdminStateUp;
    }

    public void setLoadBalancerAdminStateUp(Boolean loadBalancerAdminStateUp) {
        this.loadBalancerAdminStateUp = loadBalancerAdminStateUp;
    }

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
        Iterator<String> i = fields.iterator();
        while (i.hasNext()) {
            String s = i.next();
            if (s.equals("id")) {
                ans.setID(this.getID());
            }
            if (s.equals("tenant_id")) {
                ans.setTenantID(this.getTenantID());
            }
            if (s.equals("name")) {
                ans.setLoadBalancerName(this.getLoadBalancerName());
            }
            if (s.equals("vip_address")) {
                ans.setLoadBalancerVipAddress(this.getLoadBalancerVipAddress());
            }
            if (s.equals("vip_subnet_id")) {
                ans.setLoadBalancerVipSubnetID(this.getLoadBalancerVipSubnetID());
            }
            if (s.equals("status")) {
                ans.setLoadBalancerStatus(this.getLoadBalancerStatus());
            }
            if (s.equals("admin_state_up")) {
                ans.setLoadBalancerAdminStateUp(this.getLoadBalancerAdminStateUp());
            }
        }
        return ans;
    }

    @Override
    public String toString() {
        return "NeutronLoadBalancer{" + "loadBalancerID='" + uuid + '\'' + ", loadBalancerTenantID='" + tenantID + '\''
                + ", loadBalancerName='" + loadBalancerName + '\'' + ", loadBalancerStatus='" + loadBalancerStatus
                + '\'' + ", loadBalancerAdminStateUp='" + loadBalancerAdminStateUp + '\'' + ", loadBalancerVipAddress='"
                + loadBalancerVipAddress + '\'' + ", loadBalancerVipSubnetID='" + loadBalancerVipSubnetID + '\'' + '}';
    }
}
