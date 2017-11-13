/*
 * Copyright (c) 2015 IBM Corporation and others.  All rights reserved.
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
public final class NeutronVpnIpSecSiteConnection extends NeutronAdminAttributes<NeutronVpnIpSecSiteConnection>
        implements Serializable {
    private static final long serialVersionUID = 1L;

    // See OpenStack Network API v2.0 Reference for description of
    // annotated attributes

    @XmlElement(name = "peer_address")
    String peerAddress;

    @XmlElement(name = "peer_id")
    String peerID;

    @XmlElement(name = "peer_cidrs")
    List<String> peerCidrs;

    @XmlElement(name = "route_mode")
    String routeMode;

    @XmlElement(name = "mtu")
    Integer mtu;

    @XmlElement(name = "auth_mode")
    String authMode;

    @XmlElement(name = "psk")
    String preSharedKey;

    @XmlElement(name = "initiator")
    String initiator;

    @XmlElement(name = "ikepolicy_id")
    String ikePolicyID;

    @XmlElement(name = "ipsecpolicy_id")
    String ipsecPolicyID;

    @XmlElement(name = "vpnservice_id")
    String vpnServiceID;

    @XmlElement(name = "dpd")
    NeutronVpnDeadPeerDetection deadPeerDetection;

    public NeutronVpnIpSecSiteConnection() {
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

    public NeutronVpnDeadPeerDetection getDeadPeerDetection() {
        return deadPeerDetection;
    }

    public void setDeadPeerDetection(NeutronVpnDeadPeerDetection deadPeerDetection) {
        this.deadPeerDetection = deadPeerDetection;
    }

    @Override
    protected boolean extractField(String field, NeutronVpnIpSecSiteConnection ans) {
        switch (field) {
            case "peer_address":
                ans.setPeerAddress(this.getPeerAddress());
                break;
            case "peer_id":
                ans.setPeerID(this.getPeerID());
                break;
            case "route_mode":
                ans.setRouteMode(this.getRouteMode());
                break;
            case "mtu":
                ans.setMtu(this.getMtu());
                break;
            case "auth_mode":
                ans.setAuthMode(this.getAuthMode());
                break;
            case "psk":
                ans.setPreSharedKey(this.getPreSharedKey());
                break;
            case "initiator":
                ans.setInitiator(this.getInitiator());
                break;
            case "ikepolicy_id":
                ans.setIkePolicyID(this.getIkePolicyID());
                break;
            case "ipsecpolicy_id":
                ans.setIpsecPolicyID(this.getIpsecPolicyID());
                break;
            case "vpnservice_id":
                ans.setVpnServiceID(this.getVpnServiceID());
                break;
            default:
                return super.extractField(field, ans);
        }
        return true;
    }

    @Override
    public String toString() {
        return "NeutronVpnIpSecSiteConnection{" + "id='" + uuid + '\'' + ", tenantID='" + tenantID + '\'' + ", name='"
                + name + '\'' + ", peerAddress=" + peerAddress + ", peerID='" + peerID + '\'' + ", routeMode='"
                + routeMode + '\'' + ", mtu=" + mtu + ", authMode='" + authMode + '\'' + ", preSharedKey='"
                + preSharedKey + '\'' + ", initiator='" + initiator + '\'' + ", adminStateUp=" + adminStateUp
                + '\'' + ", ikePolicyID='" + ikePolicyID + '\'' + ", ipsecPolicyID='"
                + ipsecPolicyID + '\'' + ", vpnServiceID='" + vpnServiceID + '\'' + '}';
    }
}
