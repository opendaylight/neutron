/*
 * Copyright (c) 2015 Hewlett-Packard Development Company, L.P. and others.
 * All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.neutron.spi;

import java.util.List;
/**
 * This interface defines the methods for CRUD of NB OpenStack L2gateway objects
 */
public interface INeutronL2GatewayCRUD {
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
    public boolean addNeutronL2gateway(NeutronL2Gateway input);

    /**
     * Applications call this interface method to return all L2gateway objects
     *
     * @return List of OpenStack L2Gateway objects
     */
    public List<NeutronL2Gateway> getAllL2gateways();

    /**
     * Applications call this interface method to return if a particular
     * L2Gateway object exists
     *
     * @param uuid UUID of the L2Gateway object
     * @return {@link org.opendaylight.neutron.spi.NeutronL2Gateway}
     */
    NeutronL2Gateway getNeutronL2gateway(String uuid);
}
