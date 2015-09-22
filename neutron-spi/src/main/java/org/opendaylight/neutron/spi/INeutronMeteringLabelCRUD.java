/*
 * Copyright (c) 2015 IBM Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.spi;

import java.util.List;

public interface INeutronMeteringLabelCRUD
    extends INeutronCRUD<NeutronMeteringLabel> {

    /**
     * Applications call this interface method to determine if a particular
     *NeutronMeteringLabel object exists
     *
     * @param uuid
     *            UUID of the NeutronMeteringLabel object
     * @return boolean
     */

    boolean neutronMeteringLabelExists(String uuid);

    /**
     * Applications call this interface method to return if a particular
     * NeutronMeteringLabel object exists
     *
     * @param uuid
     *            UUID of the NeutronMeteringLabel object
     * @return {@link org.opendaylight.neutron.spi.NeutronMeteringLabel}
     *          OpenStackNeutronMeteringLabel class
     */

    NeutronMeteringLabel getNeutronMeteringLabel(String uuid);

    /**
     * Applications call this interface method to return all NeutronMeteringLabel objects
     *
     * @return List of OpenStackNetworks objects
     */

    List<NeutronMeteringLabel> getAllNeutronMeteringLabels();

    /**
     * Applications call this interface method to add a NeutronMeteringLabel object to the
     * concurrent map
     *
     * @param input
     *            OpenStackNetwork object
     * @return boolean on whether the object was added or not
     */

    boolean addNeutronMeteringLabel(NeutronMeteringLabel input);

    /**
     * Applications call this interface method to remove a Neutron NeutronMeteringLabel object to the
     * concurrent map
     *
     * @param uuid
     *            identifier for the NeutronMeteringLabel object
     * @return boolean on whether the object was removed or not
     */

    boolean removeNeutronMeteringLabel(String uuid);

    /**
     * Applications call this interface method to edit a NeutronMeteringLabel object
     *
     * @param uuid
     *            identifier of the NeutronMeteringLabel object
     * @param delta
     *            OpenStackNeutronMeteringLabel object containing changes to apply
     * @return boolean on whether the object was updated or not
     */

    boolean updateNeutronMeteringLabel(String uuid, NeutronMeteringLabel delta);

    /**
     * Applications call this interface method to see if a MAC address is in use
     *
     * @param uuid
     *            identifier of the NeutronMeteringLabel object
     * @return boolean on whether the macAddress is already associated with a
     * port or not
     */

    boolean neutronMeteringLabelInUse(String uuid);

}
