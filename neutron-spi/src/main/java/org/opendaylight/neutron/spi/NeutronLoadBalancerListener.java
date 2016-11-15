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

/**
 * OpenStack Neutron v2.0 Load Balancer as a service
 * (LBaaS) bindings. See OpenStack Network API
 * v2.0 Reference for description of  the fields:
 * Implemented fields are as follows:
 *
 * id                 uuid-str
 * default_pool_id    String
 * tenant_id          uuid-str
 * name               String
 * shared             Bool
 * protocol           String
 * protocol_port      String
 * loadbalancers       String
 * admin_state_up     Boolean
 * status             String
 *
 * http://docs.openstack.org/api/openstack-network/2.0/openstack-network.pdf
 */

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public final class NeutronLoadBalancerListener extends NeutronBaseAttributes<NeutronLoadBalancerListener>
        implements Serializable {
    private static final long serialVersionUID = 1L;

    @XmlElement(name = "default_pool_id")
    String neutronLoadBalancerListenerDefaultPoolID;

    @XmlElement(name = "connection_limit")
    Integer neutronLoadBalancerListenerConnectionLimit;

    @XmlElement(defaultValue = "true", name = "admin_state_up")
    Boolean loadBalancerListenerAdminStateIsUp;

    @XmlElement(name = "protocol")
    String neutronLoadBalancerListenerProtocol;

    @XmlElement(name = "protocol_port")
    String neutronLoadBalancerListenerProtocolPort;

    @XmlElement(name = "loadbalancers")
    List<Neutron_ID> neutronLoadBalancerListenerLoadBalancerIDs;

    public Boolean getLoadBalancerListenerAdminStateIsUp() {
        return loadBalancerListenerAdminStateIsUp;
    }

    public void setLoadBalancerListenerAdminStateIsUp(Boolean loadBalancerListenerAdminStateIsUp) {
        this.loadBalancerListenerAdminStateIsUp = loadBalancerListenerAdminStateIsUp;
    }

    public String getNeutronLoadBalancerListenerProtocol() {
        return neutronLoadBalancerListenerProtocol;
    }

    public void setNeutronLoadBalancerListenerProtocol(String neutronLoadBalancerListenerProtocol) {
        this.neutronLoadBalancerListenerProtocol = neutronLoadBalancerListenerProtocol;
    }

    public String getNeutronLoadBalancerListenerProtocolPort() {
        return neutronLoadBalancerListenerProtocolPort;
    }

    public void setNeutronLoadBalancerListenerProtocolPort(String neutronLoadBalancerListenerProtocolPort) {
        this.neutronLoadBalancerListenerProtocolPort = neutronLoadBalancerListenerProtocolPort;
    }

    public String getNeutronLoadBalancerListenerDefaultPoolID() {
        return neutronLoadBalancerListenerDefaultPoolID;
    }

    public void setNeutronLoadBalancerListenerDefaultPoolID(String neutronLoadBalancerListenerDefaultPoolID) {
        this.neutronLoadBalancerListenerDefaultPoolID = neutronLoadBalancerListenerDefaultPoolID;
    }

    public Integer getNeutronLoadBalancerListenerConnectionLimit() {
        return neutronLoadBalancerListenerConnectionLimit;
    }

    public void setNeutronLoadBalancerListenerConnectionLimit(Integer neutronLoadBalancerListenerConnectionLimit) {
        this.neutronLoadBalancerListenerConnectionLimit = neutronLoadBalancerListenerConnectionLimit;
    }

    public List<Neutron_ID> getNeutronLoadBalancerListenerLoadBalancerIDs() {
        return neutronLoadBalancerListenerLoadBalancerIDs;
    }

    public void setNeutronLoadBalancerListenerLoadBalancerIDs(
            List<Neutron_ID> neutronLoadBalancerListenerLoadBalancerIDs) {
        this.neutronLoadBalancerListenerLoadBalancerIDs = neutronLoadBalancerListenerLoadBalancerIDs;
    }

    public NeutronLoadBalancerListener extractFields(List<String> fields) {
        NeutronLoadBalancerListener ans = new NeutronLoadBalancerListener();
        for (String s : fields) {
            extractField(s, ans);
            if (s.equals("default_pool_id")) {
                ans.setNeutronLoadBalancerListenerDefaultPoolID(this.getNeutronLoadBalancerListenerDefaultPoolID());
            }
            if (s.equals("protocol")) {
                ans.setNeutronLoadBalancerListenerProtocol(this.getNeutronLoadBalancerListenerProtocol());
            }
            if (s.equals("protocol_port")) {
                ans.setNeutronLoadBalancerListenerProtocolPort(this.getNeutronLoadBalancerListenerProtocolPort());
            }
            if (s.equals("admin_state_up")) {
                ans.setLoadBalancerListenerAdminStateIsUp(loadBalancerListenerAdminStateIsUp);
            }
        }
        return ans;
    }

    @Override
    public String toString() {
        return "NeutronLoadBalancerListener{" + "loadBalancerListenerID='" + uuid + '\''
                + ", neutronLoadBalancerListenerDefaultPoolID='" + neutronLoadBalancerListenerDefaultPoolID + '\''
                + ", neutronLoadBalancerListenerConnectionLimit='" + neutronLoadBalancerListenerConnectionLimit + '\''
                + ", loadBalancerListenerTenantID='" + tenantID + '\''
                + ", loadBalancerListenerName='" + name + '\''
                + ", loadBalancerListenerAdminStateIsUp=" + loadBalancerListenerAdminStateIsUp + '\''
                + ", neutronLoadBalancerListenerProtocol='" + neutronLoadBalancerListenerProtocol + '\''
                + ", neutronLoadBalancerListenerProtocolPort='" + neutronLoadBalancerListenerProtocolPort + '\''
                + ", neutronLoadBalancerListenerLoadBalancerIDs='" + neutronLoadBalancerListenerLoadBalancerIDs + '\''
                + '}';
    }
}
