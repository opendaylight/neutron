/*
 * Copyright (c) 2017 Intel Corporation.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.spi;

/**
 * This interface defines the methods for CRUD of NB OpenStack Tap Flow objects.
 */

public interface INeutronTapFlowCRUD extends INeutronCRUD<NeutronTapFlow> {
    /**
     * Applications call this interface method to check if a NeutronTapFlow object exists.
     *
     * @param tapServiceUUID
     *            UUID of Tap Service
     * @param tapFlowUUID
     *            UUID of Tap Flow
     * @return boolean on whether the object was added or not
     */

    boolean tapFlowExists(String tapServiceUUID, String tapFlowUUID);

    /**
     * Applications call this interface method to get a NeutronTapFlow object.
     *
     * @param tapServiceUUID
     *            UUID of Tap Service
     * @param tapFlowUUID
     *            UUID of Tap Flow
     * @return NeutronTapFlow object
     */

    NeutronTapFlow getTapFlow(String tapServiceUUID, String tapFlowUUID);

    /**
     * Applications call this interface method to add a NeutronTapFlow object to the
     * concurrent map.
     *
     * @param input
     *            OpenStackNetwork object
     * @return boolean on whether the object was added or not
     */

    boolean addTapFlow(NeutronTapFlow input);

    /**
     * Applications call this interface method to update a NeutronTapFlow object to the
     * concurrent map.
     *
     * @param input
     *            OpenStackNetwork object
     * @return boolean on whether the object was added or not
     */

    boolean updateTapFlow(NeutronTapFlow input);

    /**
     * Applications call this interface method to delete a NeutronTapFlow object.
     *
     * @param tapServiceUUID
     *            UUID of Tap Service
     * @param tapFlowUUID
     *            UUID of Tap Flow
     * @return boolean on whether the object was added or not
     */

    boolean deleteTapFlow(String tapServiceUUID, String tapFlowUUID);
}
