/*
 * Copyright (c) 2015 IBM Corporation and others.  All rights reserved.
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

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class NeutronVPNIPSECSiteConnection extends NeutronObject implements Serializable, INeutronObject {
    private static final long serialVersionUID = 1L;

    // See OpenStack Network API v2.0 Reference for description of
    // annotated attributes

    @XmlElement (name = "name")
    String name;

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
    Boolean adminStateUp;

    @XmlElement (name = "status")
    String status;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    /**
     * This method copies selected fields from the object and returns them as a
     * new object, suitable for marshaling.
     *
     * @param fields
     *            List of attributes to be extracted
     * @return a NeutronVPNIPSECSiteConnection object with only the selected
     *         fields populated
     */

    public NeutronVPNIPSECSiteConnection extractFields(List<String> fields) {
        NeutronVPNIPSECSiteConnection ans = new NeutronVPNIPSECSiteConnection();
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
                ans.setName(this.getName());
            }
            if (s.equals("peer_address")) {
                ans.setPeerAddress(this.getPeerAddress());
            }
            if (s.equals("peer_id")) {
                ans.setPeerID(this.getPeerID());
            }
            if (s.equals("route_mode")) {
                ans.setRouteMode(this.getRouteMode());
            }
            if (s.equals("mtu")) {
                ans.setMtu(this.getMtu());
            }
            if (s.equals("auth_mode")) {
                ans.setAuthMode(this.getAuthMode());
            }
            if (s.equals("psk")) {
                ans.setPreSharedKey(this.getPreSharedKey());
            }
            if (s.equals("initiator")) {
                ans.setInitiator(this.getInitiator());
            }
            if (s.equals("admin_state_up")) {
                ans.setAdminStateUp(this.getAdminStateUp());
            }
            if (s.equals("status")) {
                ans.setStatus(this.getStatus());
            }
            if (s.equals("ikepolicy_id")) {
                ans.setIkePolicyID(this.getIkePolicyID());
            }
            if (s.equals("ipsecpolicy_id")) {
                ans.setIpsecPolicyID(this.getIpsecPolicyID());
            }
            if (s.equals("vpnservice_id")) {
                ans.setVpnServiceID(this.getVpnServiceID());
            }
        }
        return ans;
    }

    @Override
    public String toString() {
        return "NeutronVPNIPSECSiteConnection{" + "id='" + uuid + '\'' + ", tenantID='" + tenantID + '\'' + ", name='"
                + name + '\'' + ", peerAddress=" + peerAddress + ", peerID='" + peerID
                + '\'' + ", routeMode='" + routeMode + '\'' + ", mtu=" + mtu + ", authMode='" + authMode + '\''
                + ", preSharedKey='" + preSharedKey + '\'' + ", initiator='" + initiator + '\'' + ", adminStateUp="
                + adminStateUp + ", status='" + status + '\'' + ", ikePolicyID='" + ikePolicyID + '\''
                + ", ipsecPolicyID='" + ipsecPolicyID + '\'' + ", vpnServiceID='" + vpnServiceID + '\'' + '}';
    }
}
