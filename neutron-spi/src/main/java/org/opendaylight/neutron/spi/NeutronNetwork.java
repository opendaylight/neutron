/*
 * Copyright (c) 2013, 2015 IBM Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.spi;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "network")
@XmlAccessorType(XmlAccessType.NONE)
// Non-transient non-serializable instance field 'segments' - we have to assume/hope users will set a Serializable List
// instance.
@SuppressFBWarnings("SE_BAD_FIELD")
public final class NeutronNetwork extends NeutronAdminAttributes<NeutronNetwork> {
    // See OpenStack Network API v2.0 Reference for description of
    // annotated attributes

    private static final long serialVersionUID = 1L;

    @XmlElement(defaultValue = "false", name = "shared")
    Boolean shared;

    //    @XmlElement (defaultValue = "false", name = "router:external")
    @XmlElement(defaultValue = "false", namespace = "router", name = "external")
    Boolean routerExternal;

    //    @XmlElement (defaultValue = "flat", name = "provider:network_type")
    @XmlElement(namespace = "provider", name = "network_type")
    String providerNetworkType;

    //    @XmlElement (name = "provider:physical_network")
    @XmlElement(namespace = "provider", name = "physical_network")
    String providerPhysicalNetwork;

    //    @XmlElement (name = "provider:segmentation_id")
    @XmlElement(namespace = "provider", name = "segmentation_id")
    String providerSegmentationID;

    @XmlElement(name = "segments")
    List<NeutronNetworkSegment> segments;

    @XmlElement(name = "vlan_transparent")
    Boolean vlanTransparent;

    @XmlElement(name = "mtu")
    Integer mtu;

    @XmlElement(name = "qos_policy_id")
    String qosPolicyId;

    /* This attribute lists the ports associated with an instance
     * which is needed for determining if that instance can be deleted
     */

    public NeutronNetwork() {
    }

    @Override
    public void initDefaults() {
        super.initDefaults();
        if (shared == null) {
            shared = false;
        }
        if (routerExternal == null) {
            routerExternal = false;
        }
        if (providerNetworkType == null) {
            providerNetworkType = "flat";
        }
    }

    public boolean isShared() {
        if (shared == null) {
            return false;
        }

        return shared;
    }

    public Boolean getShared() {
        return shared;
    }

    public void setShared(boolean newValue) {
        shared = newValue;
    }

    public boolean isRouterExternal() {
        if (routerExternal == null) {
            return false;
        }

        return routerExternal;
    }

    public Boolean getRouterExternal() {
        return routerExternal;
    }

    public void setRouterExternal(boolean newValue) {
        routerExternal = newValue;
    }

    public String getProviderNetworkType() {
        return providerNetworkType;
    }

    public void setProviderNetworkType(String providerNetworkType) {
        this.providerNetworkType = providerNetworkType;
    }

    public String getProviderPhysicalNetwork() {
        return providerPhysicalNetwork;
    }

    public void setProviderPhysicalNetwork(String providerPhysicalNetwork) {
        this.providerPhysicalNetwork = providerPhysicalNetwork;
    }

    public String getProviderSegmentationID() {
        return providerSegmentationID;
    }

    public void setProviderSegmentationID(String providerSegmentationID) {
        this.providerSegmentationID = providerSegmentationID;
    }

    public void setSegments(List<NeutronNetworkSegment> segments) {
        this.segments = segments;
    }

    public List<NeutronNetworkSegment> getSegments() {
        return segments;
    }

    public Boolean getVlanTransparent() {
        return vlanTransparent;
    }

    public void setVlanTransparent(Boolean input) {
        this.vlanTransparent = input;
    }

    public Integer getMtu() {
        return mtu;
    }

    public void setMtu(Integer input) {
        mtu = input;
    }

    public String getQosPolicyId() {
        return qosPolicyId;
    }

    public void setQosPolicyId(String qosPolicyId) {
        this.qosPolicyId = qosPolicyId;
    }

    @Override
    public boolean extractField(String field, NeutronNetwork ans) {
        switch (field) {
            case "shared":
                ans.setShared(isShared());
                break;
            case "vlan_transparent":
                ans.setVlanTransparent(this.getVlanTransparent());
                break;
            case "vlan_transparent":
                ans.setVlanTransparent(this.getVlanTransparent());
                break;
            case "external":
                ans.setRouterExternal(this.getRouterExternal());
                break;
            case "segmentation_id":
                ans.setProviderSegmentationID(this.getProviderSegmentationID());
                break;
            case "physical_network":
                ans.setProviderPhysicalNetwork(this.getProviderPhysicalNetwork());
                break;
            case "network_type":
                ans.setProviderNetworkType(this.getProviderNetworkType());
                break;
            case "qos_policy_id":
                ans.setQosPolicyId(this.getQosPolicyId());
                break;
            default:
                return super.extractField(field, ans);
        }
        return true;
    }

    @Override
    public String toString() {
        return "NeutronNetwork [networkUUID=" + uuid + ", networkName=" + name + ", adminStateUp=" + adminStateUp
                + ", shared=" + shared + ", vlanTransparent=" + vlanTransparent + ", tenantID=" + tenantID
                + ", routerExternal=" + routerExternal + ", providerNetworkType=" + providerNetworkType
                + ", providerPhysicalNetwork=" + providerPhysicalNetwork + ", providerSegmentationID="
                + providerSegmentationID + ", status=" + status + ", qosPolicyId =" + qosPolicyId
                + ", segments = " + segments + "]";
    }
}
