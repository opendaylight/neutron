/*
 * Copyright (C) 2014 Red Hat, Inc.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.spi;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public final class NeutronLoadBalancerPoolMember extends NeutronObject<NeutronLoadBalancerPoolMember>
        implements Serializable, INeutronObject<NeutronLoadBalancerPoolMember> {
    private static final long serialVersionUID = 1L;

    /**
     * TODO: Plumb into LBaaS Pool. Members are nested underneath Pool CRUD.
     */
    @XmlElement(name = "address")
    String poolMemberAddress;

    @XmlElement(name = "protocol_port")
    Integer poolMemberProtoPort;

    @XmlElement(name = "admin_state_up")
    Boolean poolMemberAdminStateIsUp;

    @XmlElement(name = "weight")
    Integer poolMemberWeight;

    @XmlElement(name = "subnet_id")
    String poolMemberSubnetID;

    String poolID;

    public NeutronLoadBalancerPoolMember() {
    }

    @XmlTransient
    public String getPoolID() {
        return poolID;
    }

    public void setPoolID(String poolID) {
        this.poolID = poolID;
    }

    public String getPoolMemberAddress() {
        return poolMemberAddress;
    }

    public void setPoolMemberAddress(String poolMemberAddress) {
        this.poolMemberAddress = poolMemberAddress;
    }

    public Integer getPoolMemberProtoPort() {
        return poolMemberProtoPort;
    }

    public void setPoolMemberProtoPort(Integer poolMemberProtoPort) {
        this.poolMemberProtoPort = poolMemberProtoPort;
    }

    public Boolean getPoolMemberAdminStateIsUp() {
        return poolMemberAdminStateIsUp;
    }

    public void setPoolMemberAdminStateIsUp(Boolean poolMemberAdminStateIsUp) {
        this.poolMemberAdminStateIsUp = poolMemberAdminStateIsUp;
    }

    public Integer getPoolMemberWeight() {
        return poolMemberWeight;
    }

    public void setPoolMemberWeight(Integer poolMemberWeight) {
        this.poolMemberWeight = poolMemberWeight;
    }

    public String getPoolMemberSubnetID() {
        return poolMemberSubnetID;
    }

    public void setPoolMemberSubnetID(String poolMemberSubnetID) {
        this.poolMemberSubnetID = poolMemberSubnetID;
    }

    @Override
    protected boolean extractField(String field, NeutronLoadBalancerPoolMember ans) {
        switch (field) {
            case "pool_id":
                ans.setPoolID(this.getPoolID());
                break;
            case "address":
                ans.setPoolMemberAddress(this.getPoolMemberAddress());
                break;
            case "protocol_port":
                ans.setPoolMemberProtoPort(this.getPoolMemberProtoPort());
                break;
            case "admin_state_up":
                ans.setPoolMemberAdminStateIsUp(poolMemberAdminStateIsUp);
                break;
            case "weight":
                ans.setPoolMemberWeight(this.getPoolMemberWeight());
                break;
            case "subnet_id":
                ans.setPoolMemberSubnetID(this.getPoolMemberSubnetID());
                break;
            default:
                return super.extractField(field, ans);
        }
        return true;
    }

    @Override
    public String toString() {
        return "NeutronLoadBalancerPoolMember{" + "poolMemberID='" + uuid + '\'' + ", poolID='" + poolID + '\''
                + ", poolMemberTenantID='" + getTenantID() + '\'' + ", poolMemberAddress='" + poolMemberAddress + '\''
                + ", poolMemberProtoPort=" + poolMemberProtoPort + ", poolMemberAdminStateIsUp="
                + poolMemberAdminStateIsUp + ", poolMemberWeight=" + poolMemberWeight + ", poolMemberSubnetID='"
                + poolMemberSubnetID + '\'' + '}';
    }
}
