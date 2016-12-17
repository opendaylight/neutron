/*
 * Copyright (c) 2013, 2015 IBM Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.spi;

import java.io.Serializable;
import java.math.BigInteger;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public final class NeutronSubnetIPAllocationPool implements Serializable {
    private static final Logger LOGGER = LoggerFactory.getLogger(NeutronSubnetIPAllocationPool.class);

    private static final long serialVersionUID = 1L;

    private static final int BYTE_LENGTH = 8;
    private static final int IPV4_DOTTED_QUADS = 4;
    private static final int IPV4_DOTTED_QUAD_OFFSET = 3;
    private static final int IPV4_DOTTED_QUAD_MASK = 255;

    // See OpenStack Network API v2.0 Reference for description of
    // annotated attributes

    @XmlElement(name = "start")
    String poolStart;

    @XmlElement(name = "end")
    String poolEnd;

    public NeutronSubnetIPAllocationPool() {
    }

    public NeutronSubnetIPAllocationPool(String lowAddress, String highAddress) {
        poolStart = lowAddress;
        poolEnd = highAddress;
    }

    public String getPoolStart() {
        return poolStart;
    }

    public void setPoolStart(String poolStart) {
        this.poolStart = poolStart;
    }

    public String getPoolEnd() {
        return poolEnd;
    }

    public void setPoolEnd(String poolEnd) {
        this.poolEnd = poolEnd;
    }

    /**
     * This method determines if this allocation pool contains the
     * input IPv4 address.
     *
     * @param inputString
     *            IPv4 address in dotted decimal format
     * @return a boolean on whether the pool contains the address or not
     */

    public boolean contains(String inputString) {
        long inputIP = convert(inputString);
        long startIP = convert(poolStart);
        long endIP = convert(poolEnd);
        return inputIP >= startIP && inputIP <= endIP;
    }

    /**
     * This static method converts the supplied IPv4 address to a long
     * integer for comparison
     *
     * @param inputString
     *            IPv4 address in dotted decimal format
     * @return high-endian representation of the IPv4 address as a long.
     *          This method will return 0 if the input is null.
     */

    static long convert(String inputString) {
        long ans = 0;
        if (inputString != null) {
            String[] parts = inputString.split("\\.");
            for (String part : parts) {
                ans <<= BYTE_LENGTH;
                ans |= Integer.parseInt(part);
            }
        }
        return ans;
    }

    /**
     * This method determines if this allocation pool contains the
     * input IPv4 address.
     *
     * @param inputString
     *            IPv4 address in dotted decimal format
     * @return a boolean on whether the pool contains the address or not
     */

    public boolean containsV6(String inputString) {
        BigInteger inputIP = convertV6(inputString);
        BigInteger startIP = convertV6(poolStart);
        BigInteger endIP = convertV6(poolEnd);
        return inputIP.compareTo(startIP) >= 0 && inputIP.compareTo(endIP) <= 0;
    }

    /**
     * This static method converts the supplied IPv4 address to a long
     * integer for comparison
     *
     * @param inputString
     *            IPv6 address in dotted decimal format
     * @return high-endian representation of the IPv4 address as a BigInteger.
     *          This method will return 0 if the input is null.
     */

    static BigInteger convertV6(String inputString) {
        if (inputString == null) {
            return BigInteger.ZERO;
        }
        try {
            return new BigInteger(((Inet6Address) InetAddress.getByName(inputString)).getAddress());
        } catch (UnknownHostException e) {
            LOGGER.error("convertV6 error", e);
            return BigInteger.ZERO;
        }
    }

    /**
     * This static method converts the supplied high-ending long back
     * into a dotted decimal representation of an IPv4 address.
     *
     * @param l
     *            high-endian representation of the IPv4 address as a long
     * @return IPv4 address in dotted decimal format
     */
    static String longToIP(long input) {
        int part;
        long ipLong = input;
        String[] parts = new String[IPV4_DOTTED_QUADS];
        for (part = 0; part < IPV4_DOTTED_QUADS; part++) {
            parts[IPV4_DOTTED_QUAD_OFFSET - part] = String.valueOf(ipLong & IPV4_DOTTED_QUAD_MASK);
            ipLong >>= BYTE_LENGTH;
        }
        return join(parts, ".");
    }

    /**
     * This static method converts the supplied high-ending long back
     * into a dotted decimal representation of an IPv4 address.
     *
     * @param l
     *            high-endian representation of the IPv4 address as a long
     * @return IPv4 address in dotted decimal format
     */
    static String bigIntegerToIP(BigInteger ipv4BigInteger) {
        try {
            return Inet6Address.getByAddress(ipv4BigInteger.toByteArray()).getHostAddress();
        } catch (UnknownHostException e) {
            LOGGER.error("bigIntegerToIP", e);
            return "ERROR";
        }
    }

    /*
     * helper routine used by longToIP
     */
    public static String join(String[] strings, String separator) {
        if (strings.length == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        int index;
        for (index = 0; index < strings.length - 1; index++) {
            sb.append(strings[index]);
            sb.append(separator);
        }
        return sb.toString() + strings[index];
    }

    /*
     * This method splits the current instance by removing the supplied
     * parameter.
     *
     * If the parameter is either the low or high address,
     * then that member is adjusted and a list containing just this instance
     * is returned.
     *
     * If the parameter is in the middle of the pool, then
     * create two new instances, one ranging from low to parameter-1
     * the other ranging from parameter+1 to high
     */
    public List<NeutronSubnetIPAllocationPool> splitPool(String ipAddress) {
        List<NeutronSubnetIPAllocationPool> ans = new ArrayList<>();
        long givenIp = NeutronSubnetIPAllocationPool.convert(ipAddress);
        long startIp = NeutronSubnetIPAllocationPool.convert(poolStart);
        long endIp = NeutronSubnetIPAllocationPool.convert(poolEnd);
        long ipaddr;
        NeutronSubnetIPAllocationPool pool = new NeutronSubnetIPAllocationPool();
        boolean poolStarted = false;
        for (ipaddr = startIp; ipaddr <= endIp; ipaddr++) {
            if (ipaddr == startIp) {
                if (ipaddr != givenIp) {
                    pool.setPoolStart(poolStart);
                    poolStarted = true;
                } else {
                    //FIX for bug 533
                    pool.setPoolStart(NeutronSubnetIPAllocationPool.longToIP(ipaddr + 1));
                }
            }
            if (ipaddr == endIp) {
                if (ipaddr != givenIp) {
                    pool.setPoolEnd(poolEnd);
                } else {
                    pool.setPoolEnd(NeutronSubnetIPAllocationPool.longToIP(ipaddr - 1));
                }
                ans.add(pool);
            }
            if (ipaddr != startIp && ipaddr != endIp) {
                if (ipaddr != givenIp) {
                    if (!poolStarted) {
                        pool.setPoolStart(NeutronSubnetIPAllocationPool.longToIP(ipaddr));
                        poolStarted = true;
                    }
                } else {
                    pool.setPoolEnd(NeutronSubnetIPAllocationPool.longToIP(ipaddr - 1));
                    poolStarted = false;
                    ans.add(pool);
                    pool = new NeutronSubnetIPAllocationPool();
                    // Fix for 2120
                    pool.setPoolStart(NeutronSubnetIPAllocationPool.longToIP(ipaddr + 1));
                    poolStarted = true;
                }
            }
        }
        return ans;
    }

    @Override
    public String toString() {
        return "NeutronSubnetIPAllocationPool [" + "start=" + poolStart + ", end=" + poolEnd + "]";
    }

    /*
     * This method splits the current instance by removing the supplied
     * parameter.
     *
     * If the parameter is either the low or high address,
     * then that member is adjusted and a list containing just this instance
     * is returned.
     new *
     * If the parameter is in the middle of the pool, then
     * create two new instances, one ranging from low to parameter-1
     * the other ranging from parameter+1 to high
     * If the pool is a single address, return null
     */
    public List<NeutronSubnetIPAllocationPool> splitPoolV6(String ipAddress) {
        List<NeutronSubnetIPAllocationPool> ans = new ArrayList<>();
        BigInteger givenIp = NeutronSubnetIPAllocationPool.convertV6(ipAddress);
        BigInteger startIp = NeutronSubnetIPAllocationPool.convertV6(poolStart);
        BigInteger endIp = NeutronSubnetIPAllocationPool.convertV6(poolEnd);
        if (givenIp.compareTo(startIp) == 0 && givenIp.compareTo(endIp) < 0) {
            NeutronSubnetIPAllocationPool pool = new NeutronSubnetIPAllocationPool();
            pool.setPoolStart(NeutronSubnetIPAllocationPool.bigIntegerToIP(startIp.add(BigInteger.ONE)));
            pool.setPoolEnd(poolEnd);
            ans.add(pool);
            return ans;
        }
        if (givenIp.compareTo(endIp) == 0 && givenIp.compareTo(startIp) > 0) {
            NeutronSubnetIPAllocationPool pool = new NeutronSubnetIPAllocationPool();
            pool.setPoolStart(poolStart);
            pool.setPoolEnd(NeutronSubnetIPAllocationPool.bigIntegerToIP(endIp.subtract(BigInteger.ONE)));
            ans.add(pool);
            return ans;
        }
        if (givenIp.compareTo(endIp) < 0 && givenIp.compareTo(startIp) > 0) {
            NeutronSubnetIPAllocationPool pool = new NeutronSubnetIPAllocationPool();
            pool.setPoolStart(poolStart);
            pool.setPoolEnd(NeutronSubnetIPAllocationPool.bigIntegerToIP(givenIp.subtract(BigInteger.ONE)));
            ans.add(pool);
            NeutronSubnetIPAllocationPool p2 = new NeutronSubnetIPAllocationPool();
            p2.setPoolStart(NeutronSubnetIPAllocationPool.bigIntegerToIP(givenIp.add(BigInteger.ONE)));
            p2.setPoolEnd(poolEnd);
            ans.add(p2);
            return ans;
        }
        return null;
    }
}
