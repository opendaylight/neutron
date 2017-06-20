/*
 * Copyright (c) 2017 Inocybe, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.hostconfig.vpp;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SocketInfoTest {

    private static final String SOCKET_PATH = "/tmp";
    private static final String SOCKET_PREFIX = "socket_";
    private static final String VHOSTUSER_MODE = "server";

    // For equal test
    private static final String SOCKET_PATH_BIS = "/opt";
    private static final String SOCKET_PREFIX_BIS = "socket_bis_";
    private static final String VHOSTUSER_MODE_BIS = "server_bis";
    private SocketInfo socketInfo;

    @Before
    public void init() {
        socketInfo = new SocketInfo(SOCKET_PATH, SOCKET_PREFIX, VHOSTUSER_MODE);
    }

    @Test
    public void getSocketPathTest() {
        Assert.assertEquals(socketInfo.getSocketPath(), SOCKET_PATH);
    }

    @Test
    public void getSocketPrefixTest() {
        Assert.assertEquals(socketInfo.getSocketPrefix(), SOCKET_PREFIX);
    }

    @Test
    public void getVhostuserModeTest() {
        Assert.assertEquals(socketInfo.getVhostuserMode(), VHOSTUSER_MODE);
    }

    @Test
    public void getVhostUserSocketTest() {
        final String expectedResult = "/tmpsocket_$PORT_ID";
        Assert.assertEquals(socketInfo.getVhostUserSocket(), expectedResult);
    }

    @Test
    public void hashCodeTest() {
        final int expectedResult = 1774869086;
        Assert.assertEquals(socketInfo.hashCode(), expectedResult);
    }

    @Test
    public void equalTest() {
        Assert.assertTrue(socketInfo.equals(socketInfo));
        Assert.assertFalse(socketInfo.equals(null));
        Assert.assertFalse(socketInfo.equals(0));
        Assert.assertFalse(socketInfo.equals(new SocketInfo(SOCKET_PATH_BIS, SOCKET_PREFIX, VHOSTUSER_MODE)));
        Assert.assertFalse(socketInfo.equals(new SocketInfo(SOCKET_PATH, SOCKET_PREFIX_BIS, VHOSTUSER_MODE)));
        Assert.assertFalse(socketInfo.equals(new SocketInfo(SOCKET_PATH, SOCKET_PATH, VHOSTUSER_MODE_BIS)));
    }



}
