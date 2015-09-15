/*
 * Copyright (c) 2015 Tata Consultancy Services and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.spi;

import java.util.List;

/**
 * This interface defines the methods for CRUD of NB VPNService objects
 *
 */

public interface INeutronVPNServiceCRUD
    extends INeutronCRUD<NeutronVPNService> {

    /**
     * Applications call this interface method to determine if a particular
     * VPNService object exists
     *
     * @param uuid
     *            UUID of the VPNService object
     * @return boolean
     */

    boolean neutronVPNServiceExists(String uuid);

    /**
     * Applications call this interface method to return if a particular
     * VPNService object exists
     *
     * @param uuid
     *            UUID of the VPNService object
     * @return {@link org.opendaylight.neutron.spi.NeutronVPNService}
     *         OpenStack VPNService class
     */

    NeutronVPNService getVPNService(String uuid);

    /**
     * Applications call this interface method to return all VPNService objects
     *
     * @return a Set of OpenStackVPNService objects
     */

    List<NeutronVPNService> getAllVPNService();

    /**
     * Applications call this interface method to add a VPNService object to the
     * concurrent map
     *
     * @param input
     *            OpenStackVPNService object
     * @return boolean on whether the object was added or not
     */

    boolean addVPNService(NeutronVPNService input);

    /**
     * Applications call this interface method to remove a VPNService object to
     * the concurrent map
     *
     * @param uuid
     *            identifier for the VPNService object
     * @return boolean on whether the object was removed or not
     */

    boolean removeVPNService(String uuid);

    /**
     * Applications call this interface method to edit a VPNService object
     *
     * @param uuid
     *            identifier of the VPNService object
     * @param delta
     *            OpenStackVPNService object containing changes to apply
     * @return boolean on whether the object was updated or not
     */

    boolean updateVPNService(String uuid, NeutronVPNService delta);

    /**
     * Applications call this interface method to see if a MAC address is in use
     *
     * @param uuid
     *            identifier of the VPNService object
     * @return boolean on whether the macAddress is already associated with a
     *         port or not
     */

    boolean neutronVPNServiceInUse(String uuid);

}
