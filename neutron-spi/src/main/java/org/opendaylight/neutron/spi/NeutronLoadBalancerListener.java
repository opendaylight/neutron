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
 * <p>
 * http://docs.openstack.org/api/openstack-network/2.0/openstack-network.pdf
 */

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public final class NeutronLoadBalancerListener extends NeutronBaseAttributes<NeutronLoadBalancerListener>
        implements Serializable {
    private static final Logger LOG = LoggerFactory.getLogger(NeutronLoadBalancerListener.class);
    private static final long serialVersionUID = 1L;

    @XmlElement(name = "default_pool_id")
    String loadBalancerListenerDefaultPoolID;

    @XmlElement(name = "connection_limit")
    Integer loadBalancerListenerConnectionLimit;

    @XmlElement(defaultValue = "true", name = "admin_state_up")
    Boolean loadBalancerListenerAdminStateIsUp;

    @XmlElement(name = "protocol")
    String loadBalancerListenerProtocol;

    @XmlElement(name = "protocol_port")
    String loadBalancerListenerProtocolPort;

    @XmlElement(name = "loadbalancers")
    List<NeutronID> loadBalancerListenerLoadBalancerIDs;

    public Boolean getLoadBalancerListenerAdminStateIsUp() {
        return loadBalancerListenerAdminStateIsUp;
    }

    public void setLoadBalancerListenerAdminStateIsUp(Boolean loadBalancerListenerAdminStateIsUp) {
        this.loadBalancerListenerAdminStateIsUp = loadBalancerListenerAdminStateIsUp;
    }

    public String getNeutronLoadBalancerListenerProtocol() {
        return loadBalancerListenerProtocol;
    }

    public void setNeutronLoadBalancerListenerProtocol(String loadBalancerListenerProtocol) {
        this.loadBalancerListenerProtocol = loadBalancerListenerProtocol;
    }

    public String getNeutronLoadBalancerListenerProtocolPort() {
        return loadBalancerListenerProtocolPort;
    }

    public void setNeutronLoadBalancerListenerProtocolPort(String loadBalancerListenerProtocolPort) {
        this.loadBalancerListenerProtocolPort = loadBalancerListenerProtocolPort;
    }

    public String getNeutronLoadBalancerListenerDefaultPoolID() {
        return loadBalancerListenerDefaultPoolID;
    }

    public void setNeutronLoadBalancerListenerDefaultPoolID(String loadBalancerListenerDefaultPoolID) {
        this.loadBalancerListenerDefaultPoolID = loadBalancerListenerDefaultPoolID;
    }

    public Integer getNeutronLoadBalancerListenerConnectionLimit() {
        return loadBalancerListenerConnectionLimit;
    }

    public void setNeutronLoadBalancerListenerConnectionLimit(Integer loadBalancerListenerConnectionLimit) {
        this.loadBalancerListenerConnectionLimit = loadBalancerListenerConnectionLimit;
    }

    public List<NeutronID> getNeutronLoadBalancerListenerLoadBalancerIDs() {
        return loadBalancerListenerLoadBalancerIDs;
    }

    public void setNeutronLoadBalancerListenerLoadBalancerIDs(
            List<NeutronID> loadBalancerListenerLoadBalancerIDs) {
        this.loadBalancerListenerLoadBalancerIDs = loadBalancerListenerLoadBalancerIDs;
    }

    public NeutronLoadBalancerListener extractFields(List<String> fields) {
        NeutronLoadBalancerListener ans = new NeutronLoadBalancerListener();
        for (String s : fields) {
            if (extractField(s, ans)) {
                continue;
            }
            switch (s) {
                case "default_pool_id":
                    ans.setNeutronLoadBalancerListenerDefaultPoolID(this.getNeutronLoadBalancerListenerDefaultPoolID());
                    break;
                case "protocol":
                    ans.setNeutronLoadBalancerListenerProtocol(this.getNeutronLoadBalancerListenerProtocol());
                    break;
                case "protocol_port":
                    ans.setNeutronLoadBalancerListenerProtocolPort(this.getNeutronLoadBalancerListenerProtocolPort());
                    break;
                case "admin_state_up":
                    ans.setLoadBalancerListenerAdminStateIsUp(loadBalancerListenerAdminStateIsUp);
                    break;
                default:
                    LOG.warn("{} is not a NeutronLoadBalancerListener suitable field.", s);
                    break;
            }
        }
        return ans;
    }

    @Override
    public String toString() {
        return "NeutronLoadBalancerListener{" + "loadBalancerListenerID='" + uuid + '\''
                + ", loadBalancerListenerDefaultPoolID='" + loadBalancerListenerDefaultPoolID + '\''
                + ", loadBalancerListenerConnectionLimit='" + loadBalancerListenerConnectionLimit + '\''
                + ", loadBalancerListenerTenantID='" + tenantID + '\''
                + ", loadBalancerListenerName='" + name + '\''
                + ", loadBalancerListenerAdminStateIsUp=" + loadBalancerListenerAdminStateIsUp + '\''
                + ", loadBalancerListenerProtocol='" + loadBalancerListenerProtocol + '\''
                + ", loadBalancerListenerProtocolPort='" + loadBalancerListenerProtocolPort + '\''
                + ", loadBalancerListenerLoadBalancerIDs='" + loadBalancerListenerLoadBalancerIDs + '\''
                + '}';
    }
}
