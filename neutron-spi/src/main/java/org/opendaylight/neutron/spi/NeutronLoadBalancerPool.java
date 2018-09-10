/*
 * Copyright (C) 2014 Red Hat, Inc.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.spi;

import java.io.Serializable;
import java.util.ArrayList;
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
 * <p>
 * id                 uuid-str
 * tenant_id          uuid-str
 * name               String
 * protocol           String
 * lb_algorithm       String
 * healthmonitor_id   String
 * admin_state_up     Bool
 * status             String
 * members            List &lt;NeutronLoadBalancerPoolMember&gt;
 * http://docs.openstack.org/api/openstack-network/2.0/openstack-network.pdf
 */

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public final class NeutronLoadBalancerPool extends NeutronBaseAttributes<NeutronLoadBalancerPool>
        implements Serializable {
    private static final long serialVersionUID = 1L;

    @XmlElement(name = "protocol")
    String loadBalancerPoolProtocol;

    @XmlElement(name = "lb_algorithm")
    String loadBalancerPoolLbAlgorithm;

    @XmlElement(name = "healthmonitor_id")
    String loadBalancerPoolHealthMonitorID;

    @XmlElement(defaultValue = "true", name = "admin_state_up")
    Boolean loadBalancerPoolAdminStateIsUp;

    @XmlElement(name = "listeners")
    List<NeutronID> loadBalancerPoolListeners;

    @XmlElement(name = "session_persistence")
    NeutronLoadBalancerSessionPersistence loadBalancerPoolSessionPersistence;

    @XmlElement(name = "members")
    List<NeutronLoadBalancerPoolMember> loadBalancerPoolMembers;

    public NeutronLoadBalancerPool() {
        loadBalancerPoolMembers = new ArrayList<>();
    }

    public String getLoadBalancerPoolProtocol() {
        return loadBalancerPoolProtocol;
    }

    public void setLoadBalancerPoolProtocol(String loadBalancerPoolProtocol) {
        this.loadBalancerPoolProtocol = loadBalancerPoolProtocol;
    }

    public String getLoadBalancerPoolLbAlgorithm() {
        return loadBalancerPoolLbAlgorithm;
    }

    public void setLoadBalancerPoolLbAlgorithm(String loadBalancerPoolLbAlgorithm) {
        this.loadBalancerPoolLbAlgorithm = loadBalancerPoolLbAlgorithm;
    }

    public String getLoadBalancerPoolHealthMonitorID() {
        return loadBalancerPoolHealthMonitorID;
    }

    public void setLoadBalancerPoolHealthMonitorID(String loadBalancerPoolHealthMonitorID) {
        this.loadBalancerPoolHealthMonitorID = loadBalancerPoolHealthMonitorID;
    }

    public Boolean getLoadBalancerPoolAdminIsStateIsUp() {
        return loadBalancerPoolAdminStateIsUp;
    }

    public void setLoadBalancerPoolAdminStateIsUp(Boolean loadBalancerPoolAdminStateIsUp) {
        this.loadBalancerPoolAdminStateIsUp = loadBalancerPoolAdminStateIsUp;
    }

    public NeutronLoadBalancerSessionPersistence getLoadBalancerPoolSessionPersistence() {
        return loadBalancerPoolSessionPersistence;
    }

    public void setLoadBalancerSessionPersistence(NeutronLoadBalancerSessionPersistence persistence) {
        this.loadBalancerPoolSessionPersistence = persistence;
    }

    public List<NeutronID> getLoadBalancerPoolListeners() {
        return loadBalancerPoolListeners;
    }

    public void setLoadBalancerPoolListeners(List<NeutronID> loadBalancerPoolListeners) {
        this.loadBalancerPoolListeners = loadBalancerPoolListeners;
    }

    public List<NeutronLoadBalancerPoolMember> getLoadBalancerPoolMembers() {
        /*
         * Update the pool_id of the member to that this.id
         */
        List<NeutronLoadBalancerPoolMember> answer = new ArrayList<>();
        if (loadBalancerPoolMembers != null) {
            for (NeutronLoadBalancerPoolMember member : loadBalancerPoolMembers) {
                member.setPoolID(uuid);
                answer.add(member);
            }
        }
        return answer;
    }

    public NeutronLoadBalancerPoolMember getNeutronLoadBalancerPoolMember(String uuid) {
        for (NeutronLoadBalancerPoolMember member : loadBalancerPoolMembers) {
            if (uuid.equals(member.getID())) {
                return member;
            }
        }
        return null;
    }

    public void setLoadBalancerPoolMembers(List<NeutronLoadBalancerPoolMember> loadBalancerPoolMembers) {
        this.loadBalancerPoolMembers = loadBalancerPoolMembers;
    }

    public void addLoadBalancerPoolMember(NeutronLoadBalancerPoolMember loadBalancerPoolMember) {
        this.loadBalancerPoolMembers.add(loadBalancerPoolMember);
    }

    public void removeLoadBalancerPoolMember(NeutronLoadBalancerPoolMember loadBalancerPoolMember) {
        this.loadBalancerPoolMembers.remove(loadBalancerPoolMember);
    }

    @Override
    protected boolean extractField(String field, NeutronLoadBalancerPool ans) {
        switch (field) {
            case "protocol":
                ans.setLoadBalancerPoolProtocol(this.getLoadBalancerPoolProtocol());
                break;
            case "lb_algorithm":
                ans.setLoadBalancerPoolLbAlgorithm(this.getLoadBalancerPoolLbAlgorithm());
                break;
            case "healthmonitor_id":
                ans.setLoadBalancerPoolHealthMonitorID(this.getLoadBalancerPoolHealthMonitorID());
                break;
            case "admin_state_up":
                ans.setLoadBalancerPoolAdminStateIsUp(loadBalancerPoolAdminStateIsUp);
                break;
            case "members":
                ans.setLoadBalancerPoolMembers(getLoadBalancerPoolMembers());
                break;
            default:
                return super.extractField(field, ans);
        }
        return true;
    }

    @Override
    public String toString() {
        return "NeutronLoadBalancerPool{" + "id='" + uuid + '\'' + ", tenantID='" + getTenantID() + '\'' + ", name='"
                + name + '\'' + ", protocol=" + loadBalancerPoolProtocol + '\'' + ", lbAlgorithm='"
                + loadBalancerPoolLbAlgorithm + '\'' + ", healthmonitorID=" + loadBalancerPoolHealthMonitorID
                + ", adminStateUp=" + loadBalancerPoolAdminStateIsUp + '}';
     // todo: add loadBalancerPoolMembers as joined string
    }

}
