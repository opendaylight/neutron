/*
 * Copyright (c) 2013, 2015 IBM Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.spi;

import com.google.common.net.InetAddresses;
import java.io.Serializable;
import java.math.BigInteger;
import java.net.Inet6Address;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.apache.commons.net.util.SubnetUtils;
import org.apache.commons.net.util.SubnetUtils.SubnetInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public final class NeutronSubnet extends NeutronBaseAttributes<NeutronSubnet> implements Serializable {
    private static final Logger LOG = LoggerFactory.getLogger(NeutronCRUDInterfaces.class);

    private static final long serialVersionUID = 1L;
    private static final int IPV4_VERSION = 4;
    private static final int IPV6_VERSION = 6;
    private static final int IPV6_LENGTH = 128;
    private static final int IPV6_LENGTH_BYTES = 8;
    private static final long IPV6_LSB_MASK = 0x000000FF;
    private static final int IPV6_BYTE_OFFSET = 7;

    // See OpenStack Network API v2.0 Reference for description of
    // annotated attributes

    @XmlElement(name = "network_id")
    String networkUUID;

    @XmlElement(defaultValue = "4", name = "ip_version")
    Integer ipVersion;

    @XmlElement(name = "cidr")
    String cidr;

    @XmlElement(name = "gateway_ip")
    String gatewayIp;

    @XmlElement(name = "dns_nameservers")
    List<String> dnsNameservers;

    @XmlElement(name = "allocation_pools")
    List<NeutronSubnetIpAllocationPool> allocationPools;

    @XmlElement(name = "host_routes")
    List<NeutronRoute> hostRoutes;

    @XmlElement(defaultValue = "true", name = "enable_dhcp")
    Boolean enableDHCP;

    @XmlElement(name = "ipv6_address_mode", nillable = true)
    String ipV6AddressMode;

    @XmlElement(name = "ipv6_ra_mode", nillable = true)
    String ipV6RaMode;

    public String getNetworkUUID() {
        return networkUUID;
    }

    public void setNetworkUUID(String networkUUID) {
        this.networkUUID = networkUUID;
    }

    public Integer getIpVersion() {
        return ipVersion;
    }

    public void setIpVersion(Integer ipVersion) {
        this.ipVersion = ipVersion;
    }

    public String getCidr() {
        return cidr;
    }

    public void setCidr(String cidr) {
        this.cidr = cidr;
    }

    public String getGatewayIp() {
        return gatewayIp;
    }

    public void setGatewayIp(String gatewayIp) {
        this.gatewayIp = gatewayIp;
    }

    public List<String> getDnsNameservers() {
        return dnsNameservers;
    }

    public void setDnsNameservers(List<String> dnsNameservers) {
        this.dnsNameservers = dnsNameservers;
    }

    public List<NeutronSubnetIpAllocationPool> getAllocationPools() {
        return allocationPools;
    }

    public void setAllocationPools(List<NeutronSubnetIpAllocationPool> allocationPools) {
        this.allocationPools = allocationPools;
    }

    public List<NeutronRoute> getHostRoutes() {
        return hostRoutes;
    }

    public void setHostRoutes(List<NeutronRoute> hostRoutes) {
        this.hostRoutes = hostRoutes;
    }

    public boolean isEnableDHCP() {
        if (enableDHCP == null) {
            return true;
        }
        return enableDHCP;
    }

    public Boolean getEnableDHCP() {
        return enableDHCP;
    }

    public void setEnableDHCP(Boolean newValue) {
        enableDHCP = newValue;
    }

    public String getIpV6AddressMode() {
        return ipV6AddressMode;
    }

    public void setIpV6AddressMode(String ipV6AddressMode) {
        this.ipV6AddressMode = ipV6AddressMode;
    }

    public String getIpV6RaMode() {
        return ipV6RaMode;
    }

    public void setIpV6RaMode(String ipV6RaMode) {
        this.ipV6RaMode = ipV6RaMode;
    }

    @Override
    protected boolean extractField(String field, NeutronSubnet ans) {
        switch (field) {
            case "network_id":
                ans.setNetworkUUID(this.getNetworkUUID());
                break;
            case "ip_version":
                ans.setIpVersion(this.getIpVersion());
                break;
            case "cidr":
                ans.setCidr(this.getCidr());
                break;
            case "gateway_ip":
                ans.setGatewayIp(this.getGatewayIp());
                break;
            case "dns_nameservers":
                List<String> nsList = new ArrayList<>();
                nsList.addAll(this.getDnsNameservers());
                ans.setDnsNameservers(nsList);
                break;
            case "allocation_pools":
                List<NeutronSubnetIpAllocationPool> pools = new ArrayList<>();
                pools.addAll(this.getAllocationPools());
                ans.setAllocationPools(pools);
                break;
            case "host_routes":
                List<NeutronRoute> routes = new ArrayList<>();
                routes.addAll(this.getHostRoutes());
                ans.setHostRoutes(routes);
                break;
            case "enable_dhcp":
                ans.setEnableDHCP(this.getEnableDHCP());
                break;
            case "ipv6_address_mode":
                ans.setIpV6AddressMode(this.getIpV6AddressMode());
                break;
            case "ipv6_ra_mode":
                ans.setIpV6RaMode(this.getIpV6RaMode());
                break;
            default:
                return super.extractField(field, ans);
        }
        return true;
    }

    /* test to see if the cidr address used to define this subnet
     * is a valid network address (an necessary condition when creating
     * a new subnet)
     */
    public boolean isValidCIDR() {
        // fix for Bug 2290 - need to wrap the existing test as
        // IPv4 because SubnetUtils doesn't support IPv6
        if (ipVersion == IPV4_VERSION) {
            try {
                SubnetUtils util = new SubnetUtils(cidr);
                SubnetInfo info = util.getInfo();
                if (!info.getNetworkAddress().equals(info.getAddress())) {
                    return false;
                }
            } catch (IllegalArgumentException e) {
                LOG.warn("Failure in isValidCIDR()", e);
                return false;
            }
            return true;
        }
        if (ipVersion == IPV6_VERSION) {
            // fix for Bug2290 - this is custom code because no classes
            // with ODL-friendly licenses have been found
            // extract address (in front of /) and length (after /)
            String[] parts = cidr.split("/");
            if (parts.length != 2) {
                return false;
            }
            int length = Integer.parseInt(parts[1]);
            //TODO?: limit check on length
            // convert to byte array
            byte[] addrBytes = ((Inet6Address) InetAddresses.forString(parts[0])).getAddress();
            for (int index = length; index < IPV6_LENGTH; index++) {
                if (((((int) addrBytes[index / IPV6_LENGTH_BYTES]) & IPV6_LSB_MASK)
                        & (1 << (IPV6_BYTE_OFFSET - (index % IPV6_LENGTH_BYTES)))) != 0) {
                    return (false);
                }
            }
            return (true);
        }
        return false;
    }

    /* test to see if the gateway IP specified overlaps with specified
     * allocation pools (an error condition when creating a new subnet
     * or assigning a gateway IP)
     */
    public boolean gatewayIp_Pool_overlap() {
        for (NeutronSubnetIpAllocationPool pool : allocationPools) {
            if (ipVersion == IPV4_VERSION && pool.contains(gatewayIp)) {
                return true;
            }
            if (ipVersion == IPV6_VERSION && pool.containsV6(gatewayIp)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void initDefaults() {
        if (enableDHCP == null) {
            enableDHCP = true;
        }
        if (ipVersion == null) {
            ipVersion = IPV4_VERSION;
        }
        if (dnsNameservers == null) {
            dnsNameservers = new ArrayList<>();
        }
        if (hostRoutes == null) {
            hostRoutes = new ArrayList<>();
        }
        if (allocationPools == null) {
            allocationPools = new ArrayList<>();
            if (ipVersion == IPV4_VERSION) {
                try {
                    SubnetUtils util = new SubnetUtils(cidr);
                    SubnetInfo info = util.getInfo();
                    if (gatewayIp == null || ("").equals(gatewayIp)) {
                        gatewayIp = info.getLowAddress();
                    }
                    if (allocationPools.size() < 1) {
                        NeutronSubnetIpAllocationPool source = new NeutronSubnetIpAllocationPool(info.getLowAddress(),
                                info.getHighAddress());
                        allocationPools = source.splitPool(gatewayIp);
                    }
                } catch (IllegalArgumentException e) {
                    LOG.warn("Failure in initDefault()", e);
                    return;
                }
            }
            if (ipVersion == IPV6_VERSION) {
                String[] parts = cidr.split("/");
                if (parts.length != 2) {
                    return;
                }

                int length = Integer.parseInt(parts[1]);
                BigInteger lowAddressBi = NeutronSubnetIpAllocationPool.convertV6(parts[0]);
                String lowAddress = NeutronSubnetIpAllocationPool.bigIntegerToIp(lowAddressBi.add(BigInteger.ONE));
                BigInteger mask = BigInteger.ONE.shiftLeft(length).subtract(BigInteger.ONE);
                String highAddress = NeutronSubnetIpAllocationPool
                        .bigIntegerToIp(lowAddressBi.add(mask).subtract(BigInteger.ONE));
                if (gatewayIp == null || ("").equals(gatewayIp)) {
                    gatewayIp = lowAddress;
                }
                if (allocationPools.size() < 1) {
                    NeutronSubnetIpAllocationPool source = new NeutronSubnetIpAllocationPool(lowAddress,
                            highAddress);
                    allocationPools = source.splitPoolV6(gatewayIp);
                }
            }
        }
    }

    /* this method tests to see if the supplied IPv4 address
     * is valid for this subnet or not
     */
    public boolean isValidIp(String ipAddress) {
        if (ipVersion == IPV4_VERSION) {
            try {
                SubnetUtils util = new SubnetUtils(cidr);
                SubnetInfo info = util.getInfo();
                return info.isInRange(ipAddress);
            } catch (IllegalArgumentException e) {
                LOG.warn("Failure in isValidIp()", e);
                return false;
            }
        }

        if (ipVersion == IPV6_VERSION) {
            String[] parts = cidr.split("/");
            int length = Integer.parseInt(parts[1]);
            byte[] cidrBytes = ((Inet6Address) InetAddresses.forString(parts[0])).getAddress();
            byte[] ipBytes = ((Inet6Address) InetAddresses.forString(ipAddress)).getAddress();
            for (int index = 0; index < length; index++) {
                if (((((int) cidrBytes[index / IPV6_LENGTH_BYTES]) & IPV6_LSB_MASK) & (1 << (IPV6_BYTE_OFFSET
                        - (index % IPV6_LENGTH_BYTES)))) != (
                            (((int) ipBytes[index / IPV6_LENGTH_BYTES]) & IPV6_LSB_MASK)
                            & (1 << (IPV6_BYTE_OFFSET - (index % IPV6_LENGTH_BYTES))))) {
                    return (false);
                }
            }
            return (true);
        }
        return false;
    }

    /* method to get the lowest available address of the subnet.
     * go through all the allocation pools and keep the lowest of their
     * low addresses.
     */
    public String getLowAddr() {
        String ans = null;
        for (NeutronSubnetIpAllocationPool pool : allocationPools) {
            if (ans == null) {
                ans = pool.getPoolStart();
            } else {
                if (ipVersion == IPV4_VERSION && NeutronSubnetIpAllocationPool
                        .convert(pool.getPoolStart()) < NeutronSubnetIpAllocationPool.convert(ans)) {
                    ans = pool.getPoolStart();
                }
                if (ipVersion == IPV6_VERSION && NeutronSubnetIpAllocationPool.convertV6(pool.getPoolStart())
                        .compareTo(NeutronSubnetIpAllocationPool.convertV6(ans)) < 0) {
                    ans = pool.getPoolStart();
                }
            }
        }
        return ans;
    }

    @Override
    public String toString() {
        return "NeutronSubnet [subnetUUID=" + uuid + ", networkUUID=" + networkUUID + ", name=" + name + ", ipVersion="
                + ipVersion + ", cidr=" + cidr + ", gatewayIp=" + gatewayIp + ", dnsNameservers=" + dnsNameservers
                + ", allocationPools=" + allocationPools + ", hostRoutes=" + hostRoutes + ", enableDHCP=" + enableDHCP
                + ", tenantID=" + tenantID + ", ipv6AddressMode=" + ipV6AddressMode + ", ipv6RaMode=" + ipV6RaMode
                + "]";
    }
}
