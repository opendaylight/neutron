/*
 * Copyright (c) 2015 Ericsson India Global Services Pvt Ltd. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.spi;

import java.util.List;

/**
 * This interface defines the methods for CRUD of NB bgpvpn objects
 *
 */

public interface INeutronBgpvpnCRUD {
    /**
     * Applications call this interface method to determine if a particular
     * Bgpvpn object exists
     *
     * @param uuid
     *            UUID of the Bgpvpn object
     * @return boolean
     */

    boolean bgpvpnExists(String uuid);

    /**
     * Applications call this interface method to return if a particular
     * Bgpvpn object exists
     *
     * @param uuid
     *            UUID of the Bgpvpn object
     * @return {@link org.opendaylight.neutron.spi.NeutronBgpvpn}
     *          OpenStack Bgpvpn class
     */

    NeutronBgpvpn getBgpvpn(String uuid);

    /**
     * Applications call this interface method to return all Bgpvpn objects
     *
     * @return List of OpenStackBgpvpn objects
     */

    List<NeutronBgpvpn> getAllBgpvpns();

    /**
     * Applications call this interface method to add a Bgpvpn object to the
     * concurrent map
     *
     * @param input
     *            OpenStackBgpvpn object
     * @return boolean on whether the object was added or not
     */

    boolean addBgpvpn(NeutronBgpvpn input);

    /**
     * Applications call this interface method to remove a Bgpvpn object to the
     * concurrent map
     *
     * @param uuid
     *            identifier for the bgpvpn object
     * @return boolean on whether the object was removed or not
     */

    boolean removeBgpvpn(String uuid);

    /**
     * Applications call this interface method to edit a Bgpvpn object
     *
     * @param uuid
     *            identifier of the bgpvpn object
     * @param delta
     *            OpenStackBgpvpn object containing changes to apply
     * @return boolean on whether the object was updated or not
     */

    boolean updateBgpvpn(String uuid, NeutronBgpvpn delta);

    /**
     * Applications call this interface method to determine if a Bgpvpn object
     * is use
     *
     * @param bgpvpnUUID
     *            identifier of the bgpvpn object
     *
     * @return boolean on whether the bgpvpn is in use or not
     */

    boolean bgpvpnInUse(String bgpvpnUUID);
}
