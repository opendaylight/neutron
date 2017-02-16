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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public final class NeutronPort extends NeutronAdminAttributes<NeutronPort> implements Serializable {
    private static final long serialVersionUID = 1L;

    // See OpenStack Network API v2.0 Reference for description of
    // annotated attributes

    @XmlElement(name = "network_id")
    String networkUUID;

    @XmlElement(name = "mac_address")
    String macAddress;

    @XmlElement(name = "fixed_ips")
    List<NeutronIps> fixedIps;

    @XmlElement(name = "device_id")
    String deviceID;

    @XmlElement(name = "device_owner")
    String deviceOwner;

    @XmlElement(name = "security_groups")
    List<NeutronSecurityGroup> securityGroups;

    @XmlElement(name = "allowed_address_pairs")
    List<NeutronPortAllowedAddressPairs> allowedAddressPairs;

    //@XmlElement (name = "binding:host_id")
    @XmlElement(namespace = "binding", name = "host_id")
    String bindinghostID;

    //@XmlElement (name = "binding:vnic_type")
    @XmlElement(namespace = "binding", name = "vnic_type")
    String bindingvnicType;

    //@XmlElement (name = "binding:vif_type")
    @XmlElement(namespace = "binding", name = "vif_type")
    String bindingvifType;

    //@XmlElement (name = "binding:vif_details")
    @XmlElement(namespace = "binding", name = "vif_details")
    @XmlJavaTypeAdapter(NeutronResourceMapPropertyAdapter.class)
    Map<String, String> vifDetails;

    @XmlElement(name = "extra_dhcp_opts")
    List<NeutronPortExtraDHCPOption> extraDHCPOptions;

    //Port security is enabled by default for backward compatibility.
    @XmlElement(defaultValue = "true", name = "port_security_enabled")
    Boolean portSecurityEnabled;

    @XmlElement(name = "qos_policy_id")
    String qosPolicyId;

    public NeutronPort() {
    }

    public String getNetworkUUID() {
        return networkUUID;
    }

    public void setNetworkUUID(String networkUUID) {
        this.networkUUID = networkUUID;
    }

    public boolean isAdminStateUp() {
        if (adminStateUp == null) {
            return true;
        }
        return adminStateUp;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public List<NeutronIps> getFixedIps() {
        return fixedIps;
    }

    public void setFixedIps(List<NeutronIps> fixedIps) {
        this.fixedIps = fixedIps;
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

    public List<NeutronPortAllowedAddressPairs> getAllowedAddressPairs() {
        return allowedAddressPairs;
    }

    public void setAllowedAddressPairs(List<NeutronPortAllowedAddressPairs> allowedAddressPairs) {
        this.allowedAddressPairs = allowedAddressPairs;
    }

    public List<NeutronPortExtraDHCPOption> getExtraDHCPOptions() {
        return extraDHCPOptions;
    }

    public void setExtraDHCPOptions(List<NeutronPortExtraDHCPOption> extraDHCPOptions) {
        this.extraDHCPOptions = extraDHCPOptions;
    }

    public Map<String, String> getVIFDetails() {
        return vifDetails;
    }

    public void setVIFDetails(Map<String, String> vifDetails) {
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

    public Boolean getPortSecurityEnabled() {
        if (portSecurityEnabled == null) {
            return true;
        }
        return portSecurityEnabled;
    }

    public String getQosPolicyId() {
        return qosPolicyId;
    }

    public void setQosPolicyId(String qosPolicyId) {
        this.qosPolicyId = qosPolicyId;
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
     *             populated
     */

    public NeutronPort extractFields(List<String> fields) {
        NeutronPort ans = new NeutronPort();
        for (String field : fields) {
            extractField(field, ans);
            if ("network_id".equals(field)) {
                ans.setNetworkUUID(this.getNetworkUUID());
            }
            if ("mac_address".equals(field)) {
                ans.setMacAddress(this.getMacAddress());
            }
            if ("fixed_ips".equals(field)) {
                ans.setFixedIps(new ArrayList<NeutronIps>(this.getFixedIps()));
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
                ans.setAllowedAddressPairs(
                        new ArrayList<NeutronPortAllowedAddressPairs>(this.getAllowedAddressPairs()));
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
                ans.setVIFDetails(new HashMap<String, String>(this.getVIFDetails()));
            }
            if ("extra_dhcp_opts".equals(field)) {
                ans.setExtraDHCPOptions(new ArrayList<NeutronPortExtraDHCPOption>(this.getExtraDHCPOptions()));
            }
            if ("port_security_enabled".equals(field)) {
                ans.setPortSecurityEnabled(this.getPortSecurityEnabled());
            }
            if ("qos_policy_id".equals(field)) {
                ans.setQosPolicyId(this.getQosPolicyId());
            }
        }
        return ans;
    }

    @Override
    public void initDefaults() {
        adminStateUp = true;
        portSecurityEnabled = true;
        if (fixedIps == null) {
            fixedIps = new ArrayList<NeutronIps>();
        }
    }

    @Override
    public String toString() {
        return "NeutronPort [portUUID=" + uuid + ", networkUUID=" + networkUUID + ", name=" + name + ", adminStateUp="
                + adminStateUp + ", status=" + status + ", macAddress=" + macAddress + ", fixedIps=" + fixedIps
                + ", deviceID=" + deviceID + ", deviceOwner=" + deviceOwner + ", tenantID=" + tenantID
                + ", securityGroups=" + securityGroups + ", allowedAddressPairs" + allowedAddressPairs
                + ", bindinghostID=" + bindinghostID + ", bindingvnicType=" + bindingvnicType + ", bindingvifType="
                + bindingvifType + ", vifDetails=" + vifDetails + ", extraDHCPOptions=" + extraDHCPOptions
                + ", portSecurityEnabled=" + portSecurityEnabled + ", qosPolicyId=" + qosPolicyId + "]";
    }
}
