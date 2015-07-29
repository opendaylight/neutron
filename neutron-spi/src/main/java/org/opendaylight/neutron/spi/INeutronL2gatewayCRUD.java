/*
 * Copyright (c) 2015 Hewlett-Packard Development Company, L.P. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.spi;

import java.util.List;
/**
 * This interface defines the methods for CRUD of NB OpenStack L2gateway objects
 */
public interface INeutronL2gatewayCRUD extends INeutronCRUD<NeutronL2gateway>{
    /**
     * Applications call this interface method to determine if a particular
     * L2gateway object exists
     * @param uuid UUID of the L2gateway object
     * @return boolean
     */
    public boolean neutronL2gatewayExists(String uuid);

    /**
     * Applications call this interface method to add a L2gateway object to the
     * concurrent map
     *
     * @param input OpenStack L2Gateway object
     * @return boolean on whether the object was added or not
     */
    public boolean addNeutronL2gateway(NeutronL2gateway input);

    /**
     * Applications call this interface method to return all L2gateway objects
     *
     * @return List of OpenStack L2Gateway objects
     */
    public List<NeutronL2gateway> getAllNeutronL2gateways();

    /**
     * Applications call this interface method to return if a particular
     * L2Gateway object exists
     *
     * @param uuid UUID of the L2Gateway object
     * @return {@link org.opendaylight.neutron.spi.NeutronL2gateway}
     */
    NeutronL2gateway getNeutronL2gateway(String uuid);

    /**
     * Applications call this interface method to remove a L2gateway
     *  object to the concurrent map
     *
     * @param uuid
     *            identifier for the L2gateway object
     * @return boolean on whether the object was removed or not
     */
    boolean removeNeutronL2gateway(String uuid);

    /**
     * Applications call this interface method to edit a L2gateway object
     *
     * @param uuid
     *            identifier of the L2gateway object
     * @param delta
     *            OpenStackL2gateway object containing changes to apply
     * @return boolean on whether the object was updated or not
     */

    boolean updateNeutronL2gateway(String uuid, NeutronL2gateway delta);

    /**
     * Applications call this interface method to determine if a l2gateway object
     * is use
     *
     * @param l2gatewayID
     *            identifier of the l2gateway object
     *
     * @return boolean on whether the l2gateway is in use or not
     */

    boolean neutronL2gatewayInUse(String l2gatewayID);
}
