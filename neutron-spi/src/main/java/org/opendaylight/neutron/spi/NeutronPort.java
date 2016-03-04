/*
 * Copyright (c) 2013, 2015 IBM Corporation and others.  All rights reserved.
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


@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)

public class NeutronPort extends NeutronObject implements Serializable, INeutronObject {
    private static final long serialVersionUID = 1L;

    // See OpenStack Network API v2.0 Reference for description of
    // annotated attributes

    @XmlElement (name = "network_id")
    String networkUUID;

    @XmlElement (name = "name")
    String name;

    @XmlElement (defaultValue = "true", name = "admin_state_up")
    Boolean adminStateUp;

    @XmlElement (name = "status")
    String status;

    @XmlElement (name = "mac_address")
    String macAddress;

    @XmlElement (name = "fixed_ips")
    List<Neutron_IPs> fixedIPs;

    @XmlElement (name = "device_id")
    String deviceID;

    @XmlElement (name = "device_owner")
    String deviceOwner;

    @XmlElement (name = "security_groups")
    List<NeutronSecurityGroup> securityGroups;

    @XmlElement (name = "allowed_address_pairs")
    List<NeutronPort_AllowedAddressPairs> allowedAddressPairs;

    //@XmlElement (name = "binding:host_id")
    @XmlElement (namespace = "binding", name = "host_id")
    String bindinghostID;

    //@XmlElement (name = "binding:vnic_type")
    @XmlElement (namespace = "binding", name = "vnic_type")
    String bindingvnicType;

    //@XmlElement (name = "binding:vif_type")
    @XmlElement (namespace = "binding", name = "vif_type")
    String bindingvifType;

    //@XmlElement (name = "binding:vif_details")
    @XmlElement (namespace = "binding", name = "vif_details")
    List<NeutronPort_VIFDetail> vifDetails;

    //@XmlElement (name = "binding:profile")
    @XmlElement (namespace = "binding", name = "profile")
    String bindingProfile;

    @XmlElement (name = "extra_dhcp_opts")
    List<NeutronPort_ExtraDHCPOption> extraDHCPOptions;

    //Port security is enabled by default for backward compatibility.
    @XmlElement (defaultValue = "true", name = "port_security_enabled")
    Boolean portSecurityEnabled;

    public NeutronPort() {
    }

    public String getNetworkUUID() {
        return networkUUID;
    }

    public void setNetworkUUID(String networkUUID) {
        this.networkUUID = networkUUID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isAdminStateUp() {
        if (adminStateUp == null) {
            return true;
        }
        return adminStateUp;
    }

    public Boolean getAdminStateUp() { return adminStateUp; }

    public void setAdminStateUp(Boolean newValue) {
        adminStateUp = newValue;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public List<Neutron_IPs> getFixedIPs() {
        return fixedIPs;
    }

    public void setFixedIPs(List<Neutron_IPs> fixedIPs) {
        this.fixedIPs = fixedIPs;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public String getDeviceOwner() {
        return deviceOwner;
    }

    public void setDeviceOwner(String deviceOwner) {
        this.deviceOwner = deviceOwner;
    }

    public List<NeutronSecurityGroup> getSecurityGroups() {
        return securityGroups;
    }

    public void setSecurityGroups(List<NeutronSecurityGroup> securityGroups) {
        this.securityGroups = securityGroups;
    }

    public List<NeutronPort_AllowedAddressPairs> getAllowedAddressPairs() {
        return allowedAddressPairs;
    }

    public void setAllowedAddressPairs(List<NeutronPort_AllowedAddressPairs> allowedAddressPairs) {
        this.allowedAddressPairs = allowedAddressPairs;
    }

    public List<NeutronPort_ExtraDHCPOption> getExtraDHCPOptions() {
        return extraDHCPOptions;
    }

    public void setExtraDHCPOptions(List<NeutronPort_ExtraDHCPOption> extraDHCPOptions) {
        this.extraDHCPOptions = extraDHCPOptions;
    }

    public List<NeutronPort_VIFDetail> getVIFDetail() {
        return vifDetails;
    }

    public void setVIFDetail(List<NeutronPort_VIFDetail> vifDetails) {
        this.vifDetails = vifDetails;
    }

    public String getBindinghostID() {
        return bindinghostID;
    }

    public void setBindinghostID(String bindinghostID) {
        this.bindinghostID = bindinghostID;
    }

    public String getBindingvnicType() {
        return bindingvnicType;
    }

    public void setBindingvnicType(String bindingvnicType) {
        this.bindingvnicType = bindingvnicType;
    }

    public String getBindingvifType() {
        return bindingvifType;
    }

    public void setBindingvifType(String bindingvifType) {
        this.bindingvifType = bindingvifType;
    }

    public String getBindingProfile() {
        return bindingProfile;
    }

    public void setBindingProfile(String bindingProfile) {
        this.bindingProfile = bindingProfile;
    }

    public Boolean getPortSecurityEnabled() {
        if (portSecurityEnabled == null) {
            return true;
        }
        return portSecurityEnabled;
    }

    public void setPortSecurityEnabled(Boolean newValue) {
        portSecurityEnabled = newValue;
    }

    /**
     * This method copies selected fields from the object and returns them
     * as a new object, suitable for marshaling.
     *
     * @param fields
     *            List of attributes to be extracted
     * @return an OpenStackPorts object with only the selected fields
     * populated
     */

    public NeutronPort extractFields(List<String> fields) {
        NeutronPort ans = new NeutronPort();
        for (String field: fields) {
            if ("id".equals(field)) {
                ans.setID(this.getID());
            }
            if ("tenant_id".equals(field)) {
                ans.setTenantID(this.getTenantID());
            }
            if ("network_id".equals(field)) {
                ans.setNetworkUUID(this.getNetworkUUID());
            }
            if ("name".equals(field)) {
                ans.setName(this.getName());
            }
            if ("admin_state_up".equals(field)) {
                ans.setAdminStateUp(this.getAdminStateUp());
            }
            if ("status".equals(field)) {
                ans.setStatus(this.getStatus());
            }
            if ("mac_address".equals(field)) {
                ans.setMacAddress(this.getMacAddress());
            }
            if ("fixed_ips".equals(field)) {
                ans.setFixedIPs(new ArrayList<Neutron_IPs>(this.getFixedIPs()));
            }
            if ("device_id".equals(field)) {
                ans.setDeviceID(this.getDeviceID());
            }
            if ("device_owner".equals(field)) {
                ans.setDeviceOwner(this.getDeviceOwner());
            }
            if ("security_groups".equals(field)) {
                ans.setSecurityGroups(new ArrayList<NeutronSecurityGroup>(this.getSecurityGroups()));
            }
            if ("allowed_address_pairs".equals(field)) {
                ans.setAllowedAddressPairs(new ArrayList<NeutronPort_AllowedAddressPairs>(this.getAllowedAddressPairs()));
            }
            if ("binding:host_id".equals(field)) {
                ans.setBindinghostID(this.getBindinghostID());
            }
            if ("binding:vnic_type".equals(field)) {
                ans.setBindingvnicType(this.getBindingvnicType());
            }
            if ("binding:vif_type".equals(field)) {
                ans.setBindingvifType(this.getBindingvifType());
            }
            if ("binding:vif_details".equals(field)) {
                ans.setVIFDetail(new ArrayList<NeutronPort_VIFDetail>(this.getVIFDetail()));
            }
            if ("binding:profile".equals(field)) {
                ans.setBindingProfile(this.getBindingProfile());
            }
            if ("extra_dhcp_opts".equals(field)) {
                ans.setExtraDHCPOptions(new ArrayList<NeutronPort_ExtraDHCPOption>(this.getExtraDHCPOptions()));
            }
            if ("port_security_enabled".equals(field)) {
                ans.setPortSecurityEnabled(this.getPortSecurityEnabled());
            }
        }
        return ans;
    }

    @Override
    public void initDefaults() {
        adminStateUp = true;
        portSecurityEnabled = true;
        if (status == null) {
            status = "ACTIVE";
        }
        if (fixedIPs == null) {
            fixedIPs = new ArrayList<Neutron_IPs>();
        }
    }

    @Override
    public String toString() {
        return "NeutronPort [portUUID=" + uuid + ", networkUUID=" + networkUUID + ", name=" + name
                + ", adminStateUp=" + adminStateUp + ", status=" + status + ", macAddress=" + macAddress
                + ", fixedIPs=" + fixedIPs + ", deviceID=" + deviceID + ", deviceOwner=" + deviceOwner + ", tenantID="
                + tenantID + ", securityGroups=" + securityGroups
                + ", allowedAddressPairs" + allowedAddressPairs
                + ", bindinghostID=" + bindinghostID + ", bindingvnicType=" + bindingvnicType
                + ", bindingvifType=" + bindingvifType
                + ", vifDetails=" + vifDetails
                + ", bindingProfile=" + bindingProfile
                + ", extraDHCPOptions=" + extraDHCPOptions
                + ", portSecurityEnabled=" + portSecurityEnabled +"]";
    }
}
