/*
 * Copyright (c) 2015 Hewlett-Packard Development Company and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.spi;

import java.util.List;
/**
 * This interface defines the methods for CRUD of NB OpenStack L2gateway
 * Connection objects.
 */
public interface INeutronL2GatewayConnectionCRUD {
    /**
     * Applications call this interface method to determine if a particular
     * L2gateway Connection object exists
     *
     * @param uuid UUID of the L2gateway Connection object
     * @return boolean
     */
    public boolean neutronL2gatewayConnectionExists(String uuid);

    /**
     * Applications call this interface method to add a L2gateway Connection
     * object to the concurrent map.
     *
     * @param input OpenStack L2Gateway Connection object
     * @return boolean on whether the object was added or not.
     */
    public boolean addNeutronL2gatewayConnection(NeutronL2GatewayConnection input);

    /**
     * Applications call this interface method to return all L2gateway.
     * Connection objects.
     *
     * @return List of OpenStack L2GatewayConnection objects
     */
    List<NeutronL2GatewayConnection> getAllL2gatewayConnections();

    /**
     * Applications call this interface method to return if a particular
     * L2Gateway Connection object exists
     *
     * @param uuid UUID of the L2GatewayConnection object
     * @return {@link org.opendaylight.neutron.spi.NeutronL2GatewayConnection}
     */
    NeutronL2GatewayConnection getNeutronL2gatewayConnection(String uuid);

    /**
     * Applications call this interface method to remove a L2gateway Connection
     *  object to the concurrent map
     *
     * @param uuid
     *            identifier for the L2gatewayConnection object
     * @return boolean on whether the object was removed or not
     */
    boolean removeL2gatewayConnection(String uuid);

}
