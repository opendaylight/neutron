/*
 * Copyright IBM Corporation, 2013.  All rights reserved.
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

@XmlRootElement(name = "network")
@XmlAccessorType(XmlAccessType.NONE)

public class NeutronNetwork implements Serializable, INeutronObject {
    // See OpenStack Network API v2.0 Reference for description of
    // annotated attributes

    private static final long serialVersionUID = 1L;

    @XmlElement (name = "id")
    String networkUUID;

    @XmlElement (name = "name")
    String networkName;

    @XmlElement (defaultValue = "true", name = "admin_state_up")
    Boolean adminStateUp;

    @XmlElement (defaultValue = "false", name = "shared")
    Boolean shared;

    @XmlElement (name = "tenant_id")
    String tenantID;

    //    @XmlElement (defaultValue = "false", name = "router:external")
    @XmlElement (defaultValue="false", namespace="router", name="external")
    Boolean routerExternal;

    //    @XmlElement (defaultValue = "flat", name = "provider:network_type")
    @XmlElement (namespace="provider", name="network_type")
    String providerNetworkType;

    //    @XmlElement (name = "provider:physical_network")
    @XmlElement (namespace="provider", name="physical_network")
    String providerPhysicalNetwork;

    //    @XmlElement (name = "provider:segmentation_id")
    @XmlElement (namespace="provider", name="segmentation_id")

    String providerSegmentationID;

    @XmlElement (name = "status")
    String status;

    @XmlElement (name = "subnets")
    List<String> subnets;

    @XmlElement (name="segments")
    List<NeutronNetwork_Segment> segments;

    /* This attribute lists the ports associated with an instance
     * which is needed for determining if that instance can be deleted
     */

    List<NeutronPort> myPorts;

    public NeutronNetwork() {
        myPorts = new ArrayList<NeutronPort>();
    }

    public void initDefaults() {
        subnets = new ArrayList<String>();
        if (status == null) {
            status = "ACTIVE";
        }
        if (adminStateUp == null) {
            adminStateUp = true;
        }
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

    public String getID() {
        return networkUUID;
    }

    public void setID(String id) {
        this.networkUUID = id;
    }

    public String getNetworkName() {
        return networkName;
    }

    public void setNetworkName(String networkName) {
        this.networkName = networkName;
    }

    public Boolean getAdminStateUp() {
        return adminStateUp;
    }

    public void setAdminStateUp(boolean newValue) {
        adminStateUp = newValue;
    }

    public Boolean getShared() {
        return shared;
    }

    public void setShared(boolean newValue) {
        shared = newValue;
    }

    public String getTenantID() {
        return tenantID;
    }

    public void setTenantID(String tenantID) {
        this.tenantID = tenantID;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getSubnets() {
        return subnets;
    }

    public void setSubnets(List<String> subnets) {
        this.subnets = subnets;
    }

    public void addSubnet(String uuid) {
        subnets.add(uuid);
    }

    public void removeSubnet(String uuid) {
        subnets.remove(uuid);
    }

    public void setSegments(List<NeutronNetwork_Segment> segments) {
        this.segments = segments;
    }

    public List<NeutronNetwork_Segment> getSegments() {
        return segments;
    }

    public List<NeutronPort> getPortsOnNetwork() {
        return myPorts;
    }

    public void addPort(NeutronPort port) {
        myPorts.add(port);
    }

    public void removePort(NeutronPort port) {
        myPorts.remove(port);
    }

    /**
     * This method copies selected fields from the object and returns them
     * as a new object, suitable for marshaling.
     *
     * @param fields
     *            List of attributes to be extracted
     * @return an OpenStackNetworks object with only the selected fields
     * populated
     */

    public NeutronNetwork extractFields(List<String> fields) {
        NeutronNetwork ans = new NeutronNetwork();
        for (String field: fields) {
            if ("id".equals(field)) {
                ans.setID(this.getID());
            }
            if ("name".equals(field)) {
                ans.setNetworkName(this.getNetworkName());
            }
            if ("admin_state_up".equals(field)) {
                ans.setAdminStateUp(adminStateUp);
            }
            if ("status".equals(field)) {
                ans.setStatus(this.getStatus());
            }
            if ("subnets".equals(field)) {
                List<String> subnetList = new ArrayList<String>();
                subnetList.addAll(this.getSubnets());
                ans.setSubnets(subnetList);
            }
            if ("shared".equals(field)) {
                ans.setShared(shared);
            }
            if ("tenant_id".equals(field)) {
                ans.setTenantID(this.getTenantID());
            }
            if ("external".equals(field)) {
                ans.setRouterExternal(this.getRouterExternal());
            }
            if ("segmentation_id".equals(field)) {
                ans.setProviderSegmentationID(this.getProviderSegmentationID());
            }
            if ("physical_network".equals(field)) {
                ans.setProviderPhysicalNetwork(this.getProviderPhysicalNetwork());
            }
            if ("network_type".equals(field)) {
                ans.setProviderNetworkType(this.getProviderNetworkType());
            }
        }
        return ans;
    }

    @Override
    public String toString() {
        return "NeutronNetwork [networkUUID=" + networkUUID + ", networkName=" + networkName + ", adminStateUp="
                + adminStateUp + ", shared=" + shared + ", tenantID=" + tenantID + ", routerExternal=" + routerExternal
                + ", providerNetworkType=" + providerNetworkType + ", providerPhysicalNetwork="
                + providerPhysicalNetwork + ", providerSegmentationID=" + providerSegmentationID + ", status=" + status
                + ", subnets=" + subnets + ", myPorts=" + myPorts
            + ", segments = " + segments + "]";
    }
}

