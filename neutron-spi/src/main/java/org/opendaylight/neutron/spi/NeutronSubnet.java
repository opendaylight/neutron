/*
 * Copyright IBM Corporation and others, 2013.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.spi;

import java.io.Serializable;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.Inet6Address;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.net.util.SubnetUtils;
import org.apache.commons.net.util.SubnetUtils.SubnetInfo;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)

public class NeutronSubnet implements Serializable, INeutronObject {
    private static final long serialVersionUID = 1L;

    private static final int IPV4_VERSION = 4;
    private static final int IPV6_VERSION = 6;
    private static final int IPV6_LENGTH = 128;
    private static final int IPV6_LENGTH_BYTES = 8;
    private static final long IPV6_LSB_MASK = 0x000000FF;
    private static final int IPV6_BYTE_OFFSET = 7;

    // See OpenStack Network API v2.0 Reference for description of
    // annotated attributes

    @XmlElement (name = "id")
    String subnetUUID;

    @XmlElement (name = "network_id")
    String networkUUID;

    @XmlElement (name = "name")
    String name;

    @XmlElement (defaultValue = "4", name = "ip_version")
    Integer ipVersion;

    @XmlElement (name = "cidr")
    String cidr;

    @XmlElement (name = "gateway_ip")
    String gatewayIP;

    @XmlElement (name = "dns_nameservers")
    List<String> dnsNameservers;

    @XmlElement (name = "allocation_pools")
    List<NeutronSubnet_IPAllocationPool> allocationPools;

    @XmlElement (name = "host_routes")
    List<NeutronSubnet_HostRoute> hostRoutes;

    @XmlElement (defaultValue = "true", name = "enable_dhcp")
    Boolean enableDHCP;

    @XmlElement (name = "tenant_id")
    String tenantID;

    @XmlElement (name = "ipv6_address_mode", nillable = true)
    String ipV6AddressMode;

    @XmlElement (name = "ipv6_ra_mode", nillable = true)
    String ipV6RaMode;

    /* stores the OpenStackPorts associated with an instance
     * used to determine if that instance can be deleted.
     */
    List<NeutronPort> myPorts;

    Boolean gatewayIPAssigned;

    public NeutronSubnet() {
        myPorts = new ArrayList<NeutronPort>();
    }

    public String getID() { return subnetUUID; }

    public void setID(String id) { this.subnetUUID = id; }

    public String getSubnetUUID() {
        return subnetUUID;
    }

    public void setSubnetUUID(String subnetUUID) {
        this.subnetUUID = subnetUUID;
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

    public String getGatewayIP() {
        return gatewayIP;
    }

    public void setGatewayIP(String gatewayIP) {
        this.gatewayIP = gatewayIP;
    }

    public List<String> getDnsNameservers() {
        return dnsNameservers;
    }

    public void setDnsNameservers(List<String> dnsNameservers) {
        this.dnsNameservers = dnsNameservers;
    }

    public List<NeutronSubnet_IPAllocationPool> getAllocationPools() {
        return allocationPools;
    }

    public void setAllocationPools(List<NeutronSubnet_IPAllocationPool> allocationPools) {
        this.allocationPools = allocationPools;
    }

    public List<NeutronSubnet_HostRoute> getHostRoutes() {
        return hostRoutes;
    }

    public void setHostRoutes(List<NeutronSubnet_HostRoute> hostRoutes) {
        this.hostRoutes = hostRoutes;
    }

    public boolean isEnableDHCP() {
        if (enableDHCP == null) {
            return true;
        }
        return enableDHCP;
    }

    public Boolean getEnableDHCP() { return enableDHCP; }

    public void setEnableDHCP(Boolean newValue) {
            enableDHCP = newValue;
    }

    public String getTenantID() {
        return tenantID;
    }

    public void setTenantID(String tenantID) {
        this.tenantID = tenantID;
    }

    public String getIpV6AddressMode() { return ipV6AddressMode; }

    public void setIpV6AddressMode(String ipV6AddressMode) { this.ipV6AddressMode = ipV6AddressMode; }

    public String getIpV6RaMode() { return ipV6RaMode; }

    public void setIpV6RaMode(String ipV6RaMode) { this.ipV6RaMode = ipV6RaMode; }

    /**
     * This method copies selected fields from the object and returns them
     * as a new object, suitable for marshaling.
     *
     * @param fields
     *            List of attributes to be extracted
     * @return an OpenStackSubnets object with only the selected fields
     * populated
     */

    public NeutronSubnet extractFields(List<String> fields) {
        NeutronSubnet ans = new NeutronSubnet();
        Iterator<String> i = fields.iterator();
        while (i.hasNext()) {
            String s = i.next();
            if (s.equals("id")) {
                ans.setSubnetUUID(this.getSubnetUUID());
            }
            if (s.equals("network_id")) {
                ans.setNetworkUUID(this.getNetworkUUID());
            }
            if (s.equals("name")) {
                ans.setName(this.getName());
            }
            if (s.equals("ip_version")) {
                ans.setIpVersion(this.getIpVersion());
            }
            if (s.equals("cidr")) {
                ans.setCidr(this.getCidr());
            }
            if (s.equals("gateway_ip")) {
                ans.setGatewayIP(this.getGatewayIP());
            }
            if (s.equals("dns_nameservers")) {
                List<String> nsList = new ArrayList<String>();
                nsList.addAll(this.getDnsNameservers());
                ans.setDnsNameservers(nsList);
            }
            if (s.equals("allocation_pools")) {
                List<NeutronSubnet_IPAllocationPool> aPools = new ArrayList<NeutronSubnet_IPAllocationPool>();
                aPools.addAll(this.getAllocationPools());
                ans.setAllocationPools(aPools);
            }
            if (s.equals("host_routes")) {
                List<NeutronSubnet_HostRoute> hRoutes = new ArrayList<NeutronSubnet_HostRoute>();
                hRoutes.addAll(this.getHostRoutes());
                ans.setHostRoutes(hRoutes);
            }
            if (s.equals("enable_dhcp")) {
                ans.setEnableDHCP(this.getEnableDHCP());
            }
            if (s.equals("tenant_id")) {
                ans.setTenantID(this.getTenantID());
            }
            if (s.equals("ipv6_address_mode")) {
                ans.setIpV6AddressMode(this.getIpV6AddressMode());
            }
            if (s.equals("ipv6_ra_mode")) {
                ans.setIpV6RaMode(this.getIpV6RaMode());
            }
        }
        return ans;
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
            try {
                int length = Integer.parseInt(parts[1]);
                //TODO?: limit check on length
                // convert to byte array
                byte[] addrBytes = ((Inet6Address) InetAddress.getByName(parts[0])).getAddress();
                int i;
                for (i = length; i < IPV6_LENGTH; i++) { // offset is to ensure proper comparison
                    if (((((int) addrBytes[i/IPV6_LENGTH_BYTES]) & IPV6_LSB_MASK) & (1 << (IPV6_BYTE_OFFSET-(i%IPV6_LENGTH_BYTES)))) != 0) {
                        return(false);
                    }
                }
                return(true);
            } catch (UnknownHostException e) {
                return(false);
            }
        }
        return false;
    }

    /* test to see if the gateway IP specified overlaps with specified
     * allocation pools (an error condition when creating a new subnet
     * or assigning a gateway IP)
     */
    public boolean gatewayIP_Pool_overlap() {
        Iterator<NeutronSubnet_IPAllocationPool> i = allocationPools.iterator();
        while (i.hasNext()) {
            NeutronSubnet_IPAllocationPool pool = i.next();
            if (ipVersion == IPV4_VERSION) {
                if (pool.contains(gatewayIP)) {
                    return true;
                }
            }
            if (ipVersion == IPV6_VERSION) {
                if (pool.contains_V6(gatewayIP)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean initDefaults() {
        if (enableDHCP == null) {
            enableDHCP = true;
        }
        if (ipVersion == null) {
            ipVersion = IPV4_VERSION;
        }
        gatewayIPAssigned = false;
        dnsNameservers = new ArrayList<String>();
        if (hostRoutes == null) {
            hostRoutes = new ArrayList<NeutronSubnet_HostRoute>();
        }
        if (allocationPools == null) {
            allocationPools = new ArrayList<NeutronSubnet_IPAllocationPool>();
            if (ipVersion == IPV4_VERSION) {
                try {
                    SubnetUtils util = new SubnetUtils(cidr);
                    SubnetInfo info = util.getInfo();
                    if (gatewayIP == null || ("").equals(gatewayIP)) {
                        gatewayIP = info.getLowAddress();
                    }
                    if (allocationPools.size() < 1) {
                        NeutronSubnet_IPAllocationPool source =
                            new NeutronSubnet_IPAllocationPool(info.getLowAddress(),
                                    info.getHighAddress());
                        allocationPools = source.splitPool(gatewayIP);
                    }
                } catch (IllegalArgumentException e) {
                    return false;
                }
            }
            if (ipVersion == IPV6_VERSION) {
                String[] parts = cidr.split("/");
                if (parts.length != 2) {
                    return false;
                }
                try {
                    int length = Integer.parseInt(parts[1]);
                    BigInteger lowAddress_bi = NeutronSubnet_IPAllocationPool.convert_V6(parts[0]);
                    String lowAddress = NeutronSubnet_IPAllocationPool.bigIntegerToIP(lowAddress_bi.add(BigInteger.ONE));
                    BigInteger mask = BigInteger.ONE.shiftLeft(length).subtract(BigInteger.ONE);
                    String highAddress = NeutronSubnet_IPAllocationPool.bigIntegerToIP(lowAddress_bi.add(mask).subtract(BigInteger.ONE));
                    if (gatewayIP == null || ("").equals(gatewayIP)) {
                        gatewayIP = lowAddress;
                    }
                    if (allocationPools.size() < 1) {
                        NeutronSubnet_IPAllocationPool source =
                            new NeutronSubnet_IPAllocationPool(lowAddress,
                                    highAddress);
                        allocationPools = source.splitPool_V6(gatewayIP);
                    }
                } catch (Exception e) {
                    return false;
                }
            }
        }
        return true;
    }

    public List<NeutronPort> getPortsInSubnet() {
        return myPorts;
    }

    public List<NeutronPort> getPortsInSubnet(String ignore) {
        List<NeutronPort> ans = new ArrayList<NeutronPort>();
        for (NeutronPort port : myPorts) {
            if (!port.getDeviceOwner().equalsIgnoreCase(ignore)) {
                ans.add(port);
            }
        }
        return ans;
    }

    public void addPort(NeutronPort port) {
        myPorts.add(port);
    }

    public void removePort(NeutronPort port) {
        myPorts.remove(port);
    }

    public List<NeutronPort> getFloatingIpPortsInSubnet() {
        List<NeutronPort> result = new ArrayList<NeutronPort>();
        List<NeutronPort> ports = getPortsInSubnet();
        for(NeutronPort port: ports) {
            if(port.getDeviceOwner().equals("network:floatingip")) {
                result.add(port);
            }
        }
        return result;
    }

    public List<NeutronPort> getFloatingIpPortsInSubnet(String floatingIPaddress) {
        List<NeutronPort> result = new ArrayList<NeutronPort>();
        List<NeutronPort> floatingIpPorts = getFloatingIpPortsInSubnet();
        for(NeutronPort port: floatingIpPorts) {
            List<Neutron_IPs> fixedIps = port.getFixedIPs();
            for(Neutron_IPs fixedIp: fixedIps) {
                if(fixedIp.getIpAddress() != null && fixedIp.getIpAddress().equals(floatingIPaddress)) {
                    result.add(port);
                    break;
                }
            }
        }
        return result;
    }

    /* this method tests to see if the supplied IPv4 address
     * is valid for this subnet or not
     */
    public boolean isValidIP(String ipAddress) {
        if (ipVersion == IPV4_VERSION) {
            try {
                SubnetUtils util = new SubnetUtils(cidr);
                SubnetInfo info = util.getInfo();
                return info.isInRange(ipAddress);
            } catch (IllegalArgumentException e) {
                return false;
            }
        }

        if (ipVersion == IPV6_VERSION) {
            String[] parts = cidr.split("/");
            try {
                int length = Integer.parseInt(parts[1]);
                byte[] cidrBytes = ((Inet6Address) InetAddress.getByName(parts[0])).getAddress();
                byte[] ipBytes =  ((Inet6Address) InetAddress.getByName(ipAddress)).getAddress();
                int i;
                for (i = 0; i < length; i++) { // offset is to ensure proper comparison
                    if (((((int) cidrBytes[i/IPV6_LENGTH_BYTES]) & IPV6_LSB_MASK) & (1 << (IPV6_BYTE_OFFSET-(i%IPV6_LENGTH_BYTES)))) !=
                        ((((int) ipBytes[i/IPV6_LENGTH_BYTES]) & IPV6_LSB_MASK) & (1 << (IPV6_BYTE_OFFSET-(i%IPV6_LENGTH_BYTES))))) {
                        return(false);
                    }
                }
                return(true);
            } catch (UnknownHostException e) {
                return(false);
            }
        }
        return false;
    }

    /* test to see if the supplied IPv4 address is part of one of the
     * available allocation pools or not
     */
    public boolean isIPInUse(String ipAddress) {
        if (ipAddress.equals(gatewayIP) && !gatewayIPAssigned ) {
            return false;
        }
        Iterator<NeutronSubnet_IPAllocationPool> i = allocationPools.iterator();
        while (i.hasNext()) {
            NeutronSubnet_IPAllocationPool pool = i.next();
            if (ipVersion == IPV4_VERSION && pool.contains(ipAddress)) {
                return false;
            }
            if (ipVersion == IPV6_VERSION && pool.contains_V6(ipAddress)) {
                return false;
            }
        }
        return true;
    }

    /* method to get the lowest available address of the subnet.
     * go through all the allocation pools and keep the lowest of their
     * low addresses.
     */
    public String getLowAddr() {
        String ans = null;
        Iterator<NeutronSubnet_IPAllocationPool> i = allocationPools.iterator();
        while (i.hasNext()) {
            NeutronSubnet_IPAllocationPool pool = i.next();
            if (ans == null) {
                ans = pool.getPoolStart();
            }
            else {
                if (ipVersion == IPV4_VERSION) {
                    if (NeutronSubnet_IPAllocationPool.convert(pool.getPoolStart()) <
                            NeutronSubnet_IPAllocationPool.convert(ans)) {
                        ans = pool.getPoolStart();
                    }
                }
                if (ipVersion == IPV6_VERSION) {
                    if (NeutronSubnet_IPAllocationPool.convert_V6(pool.getPoolStart()).compareTo(NeutronSubnet_IPAllocationPool.convert_V6(ans)) < 0) {
                        ans = pool.getPoolStart();
                    }
                }
           }
        }
        return ans;
    }

    /*
     * allocate the parameter address.  Because this uses an iterator to
     * check the instance's list of allocation pools and we want to modify
     * pools while the iterator is being used, it is necessary to
     * build a new list of allocation pools and replace the list when
     * finished (otherwise a split will cause undefined iterator behavior.
     */
    public void allocateIP(String ipAddress) {
        Iterator<NeutronSubnet_IPAllocationPool> i = allocationPools.iterator();
        List<NeutronSubnet_IPAllocationPool> newList = new ArrayList<NeutronSubnet_IPAllocationPool>();    // we have to modify a separate list
        while (i.hasNext()) {
            NeutronSubnet_IPAllocationPool pool = i.next();
            /* if the pool contains a single address element and we are allocating it
             * then we don't need to copy the pool over.  Otherwise, we need to possibly
             * split the pool and add both pieces to the new list
             */
            if (!(pool.getPoolEnd().equalsIgnoreCase(ipAddress) &&
                    pool.getPoolStart().equalsIgnoreCase(ipAddress))) {
                if (ipVersion == IPV4_VERSION) {
                    if (pool.contains(ipAddress)) {
                        List<NeutronSubnet_IPAllocationPool> pools = pool.splitPool(ipAddress);
                        newList.addAll(pools);
                    } else {
                        newList.add(pool);
                    }
                }
                if (ipVersion == IPV6_VERSION) {
                    if (pool.contains_V6(ipAddress)) {
                        List<NeutronSubnet_IPAllocationPool> pools = pool.splitPool_V6(ipAddress);
                        newList.addAll(pools);
                    } else {
                        newList.add(pool);
                    }
                }
            }
        }
        allocationPools = newList;
    }

    /*
     * release an IP address back to the subnet.  Although an iterator
     * is used, the list is not modified until the iterator is complete, so
     * an extra list is not necessary.
     */
    public void releaseIP(String ipAddress) {
        NeutronSubnet_IPAllocationPool lPool = null;
        NeutronSubnet_IPAllocationPool hPool = null;
        Iterator<NeutronSubnet_IPAllocationPool> i = allocationPools.iterator();
        if (ipVersion == IPV4_VERSION) {
            long sIP = NeutronSubnet_IPAllocationPool.convert(ipAddress);
            //look for lPool where ipAddr - 1 is high address
            //look for hPool where ipAddr + 1 is low address
            while (i.hasNext()) {
                NeutronSubnet_IPAllocationPool pool = i.next();
                long lIP = NeutronSubnet_IPAllocationPool.convert(pool.getPoolStart());
                long hIP = NeutronSubnet_IPAllocationPool.convert(pool.getPoolEnd());
                if (sIP+1 == lIP) {
                    hPool = pool;
                }
                if (sIP-1 == hIP) {
                    lPool = pool;
                }
            }
        }
        if (ipVersion == IPV6_VERSION) {
            BigInteger sIP = NeutronSubnet_IPAllocationPool.convert_V6(ipAddress);
            //look for lPool where ipAddr - 1 is high address
            //look for hPool where ipAddr + 1 is low address
            while (i.hasNext()) {
                NeutronSubnet_IPAllocationPool pool = i.next();
                BigInteger lIP = NeutronSubnet_IPAllocationPool.convert_V6(pool.getPoolStart());
                BigInteger hIP = NeutronSubnet_IPAllocationPool.convert_V6(pool.getPoolEnd());
                if (lIP.compareTo(sIP.add(BigInteger.ONE)) == 0) {
                    hPool = pool;
                }
                if (hIP.compareTo(sIP.subtract(BigInteger.ONE)) == 0) {
                    lPool = pool;
                }
            }
        }
        //if (lPool == NULL and hPool == NULL) create new pool where low = ip = high
        if (lPool == null && hPool == null) {
            allocationPools.add(new NeutronSubnet_IPAllocationPool(ipAddress,ipAddress));
        }
        //if (lPool == NULL and hPool != NULL) change low address of hPool to ipAddr
        if (lPool == null && hPool != null) {
            hPool.setPoolStart(ipAddress);
        }
        //if (lPool != NULL and hPool == NULL) change high address of lPool to ipAddr
        if (lPool != null && hPool == null) {
            lPool.setPoolEnd(ipAddress);
        }
        //if (lPool != NULL and hPool != NULL) remove lPool and hPool and create new pool
        //        where low address = lPool.low address and high address = hPool.high Address
        if (lPool != null && hPool != null) {
            allocationPools.remove(lPool);
            allocationPools.remove(hPool);
            allocationPools.add(new NeutronSubnet_IPAllocationPool(
                    lPool.getPoolStart(), hPool.getPoolEnd()));
        }
    }

    public void setGatewayIPAllocated() {
        gatewayIPAssigned = true;
    }

    public void resetGatewayIPAllocated() {
        gatewayIPAssigned = false;
    }

    public Boolean getGatewayIPAllocated() {
        return gatewayIPAssigned;
    }

    @Override
    public String toString() {
        return "NeutronSubnet [subnetUUID=" + subnetUUID + ", networkUUID=" + networkUUID + ", name=" + name
                + ", ipVersion=" + ipVersion + ", cidr=" + cidr + ", gatewayIP=" + gatewayIP + ", dnsNameservers="
                + dnsNameservers + ", allocationPools=" + allocationPools + ", hostRoutes=" + hostRoutes
                + ", enableDHCP=" + enableDHCP + ", tenantID=" + tenantID + ", myPorts=" + myPorts
                + ", gatewayIPAssigned=" + gatewayIPAssigned + ", ipv6AddressMode=" + ipV6AddressMode
                + ", ipv6RaMode=" + ipV6RaMode + "]";
    }
}
