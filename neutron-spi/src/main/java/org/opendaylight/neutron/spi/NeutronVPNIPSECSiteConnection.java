/*
 * Copyright IBM Corporation, 2015.  All rights reserved.
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

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class NeutronVPNIPSECSiteConnection implements Serializable {
    private static final long serialVersionUID = 1L;

    // See OpenStack Network API v2.0 Reference for description of
    // annotated attributes

    @XmlElement (name = "id")
    String id;

    @XmlElement (name = "tenant_id")
    String tenantID;

    @XmlElement (name = "name")
    String name;

    @XmlElement (name = "description")
    String description;

    @XmlElement (name = "peer_address")
    String peerAddress;

    @XmlElement (name = "peer_id")
    String peerID;

    @XmlElement (name = "peer_cidrs")
    List<String> peerCidrs;

    @XmlElement (name = "route_mode")
    String routeMode;

    @XmlElement (name = "mtu")
    Integer mtu;

    @XmlElement (name = "auth_mode")
    String authMode;

    @XmlElement (name = "psk")
    String preSharedKey;

    @XmlElement (name = "initiator")
    String initiator;

    @XmlElement (defaultValue = "true", name = "admin_state_up")
    Boolean adminStateUp;             // admin state up (true/false)

    @XmlElement (name = "status")
    String status;                   // status (read-only)

    @XmlElement (name = "ikepolicy_id")
    String ikePolicyID;

    @XmlElement (name = "ipsecpolicy_id")
    String ipsecPolicyID;

    @XmlElement (name = "vpnservice_id")
    String vpnServiceID;

    @XmlElement (name = "dpd")
    NeutronVPNDeadPeerDetection deadPeerDetection;

    public NeutronVPNIPSECSiteConnection() {
    }

    public String getID() {
        return id;
    }

    public void setID(String id) {
        this.id = id;
    }

    public String getTenantID() {
        return tenantID;
    }

    public void setTenantID(String tenantID) {
        this.tenantID = tenantID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPeerAddress() {
        return peerAddress;
    }

    public void setPeerAddress(String peerAddress) {
        this.peerAddress = peerAddress;
    }

    public String getPeerID() {
        return peerID;
    }

    public void setPeerID(String peerID) {
        this.peerID = peerID;
    }

    public List<String> getPeerCidrs() {
        return peerCidrs;
    }

    public void setPeerCidrs(List<String> peerCidrs) {
        this.peerCidrs = peerCidrs;
    }

    public String getRouteMode() {
        return routeMode;
    }

    public void setRouteMode(String routeMode) {
        this.routeMode = routeMode;
    }

    public Integer getMtu() {
        return mtu;
    }

    public void setMtu(Integer mtu) {
        this.mtu = mtu;
    }

    public String getAuthMode() {
        return authMode;
    }

    public void setAuthMode(String authMode) {
        this.authMode = authMode;
    }

    public String getPreSharedKey() {
        return preSharedKey;
    }

    public void setPreSharedKey(String preSharedKey) {
        this.preSharedKey = preSharedKey;
    }

    public String getInitiator() {
        return initiator;
    }

    public void setInitiator(String initiator) {
        this.initiator = initiator;
    }

    public Boolean getAdminStateUp() { return adminStateUp; }

    public void setAdminStateUp(boolean newValue) {
        adminStateUp = newValue;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIkePolicyID() {
        return ikePolicyID;
    }

    public void setIkePolicyID(String ikePolicyID) {
        this.ikePolicyID = ikePolicyID;
    }

    public String getIpsecPolicyID() {
        return ipsecPolicyID;
    }

    public void setIpsecPolicyID(String ipsecPolicyID) {
        this.ipsecPolicyID = ipsecPolicyID;
    }

    public String getVpnServiceID() {
        return vpnServiceID;
    }

    public void setVpnServiceID(String vpnServiceID) {
        this.vpnServiceID = vpnServiceID;
    }

    public NeutronVPNDeadPeerDetection getDeadPeerDetection() {
        return deadPeerDetection;
    }

    public void setDeadPeerDetection(NeutronVPNDeadPeerDetection deadPeerDetection) {
        this.deadPeerDetection = deadPeerDetection;
    }

}
