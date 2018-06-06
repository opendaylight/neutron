/*
 * Copyright (c) 2017 Intel Corporation.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.neutron.spi;

import org.opendaylight.controller.md.sal.common.api.data.ReadFailedException;
import org.opendaylight.controller.md.sal.common.api.data.TransactionCommitFailedException;
import org.opendaylight.yangtools.yang.common.OperationFailedException;

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
     * @throws ReadFailedException if the read failed
     */

    boolean tapFlowExists(String tapServiceUUID, String tapFlowUUID) throws ReadFailedException;

    /**
     * Applications call this interface method to get a NeutronTapFlow object.
     *
     * @param tapServiceUUID
     *            UUID of Tap Service
     * @param tapFlowUUID
     *            UUID of Tap Flow
     * @return NeutronTapFlow object
     * @throws ReadFailedException if the read failed
     */
    NeutronTapFlow getTapFlow(String tapServiceUUID, String tapFlowUUID) throws ReadFailedException;

    /**
     * Applications call this interface method to add a NeutronTapFlow object to the
     * concurrent map.
     *
     * @param input
     *            OpenStackNetwork object
     * @return boolean on whether the object was added or not
     * @throws OperationFailedException if the read or write failed
     */
    boolean addTapFlow(NeutronTapFlow input) throws ReadFailedException, OperationFailedException;

    /**
     * Applications call this interface method to update a NeutronTapFlow object to the
     * concurrent map.
     *
     * @param input
     *            OpenStackNetwork object
     * @throws TransactionCommitFailedException if the write failed
     */
    void updateTapFlow(NeutronTapFlow input) throws TransactionCommitFailedException;

    /**
     * Applications call this interface method to delete a NeutronTapFlow object.
     *
     * @param tapServiceUUID
     *            UUID of Tap Service
     * @param tapFlowUUID
     *            UUID of Tap Flow
     * @throws TransactionCommitFailedException if the write failed
     */
    void deleteTapFlow(String tapServiceUUID, String tapFlowUUID) throws TransactionCommitFailedException;
}
