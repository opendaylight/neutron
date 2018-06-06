/*
 * Copyright (c) 2015 Intel Corporation.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.neutron.spi;

import java.util.List;
import org.opendaylight.controller.md.sal.common.api.data.ReadFailedException;
import org.opendaylight.yangtools.yang.common.OperationFailedException;

/**
 * This interface defines the methods for CRUD of NB neutron objects.
 */
public interface INeutronCRUD<T extends INeutronObject<T>> {

    /**
     * Applications call this interface method to determine if a particular
     * Neutron object exists.
     *
     * @param uuid
     *            UUID of the Neutron object
     * @return boolean
     * @throws ReadFailedException if the read failed
     */
    boolean exists(String uuid) throws ReadFailedException;

    /**
     * Applications call this interface method to return if a particular
     * Neutron object exists.
     *
     * @param uuid
     *            UUID of the Neutron object
     * @return {@link org.opendaylight.neutron.spi.INeutronObject}
     *          OpenStack Neutron class
     * @throws ReadFailedException if the read failed
     */
    T get(String uuid) throws ReadFailedException;

    /**
     * Applications call this interface method to return all Neutron objects.
     *
     * @return List of OpenStackNeutrons objects
     * @throws ReadFailedException if the read failed
     */
    List<T> getAll() throws ReadFailedException;

    /**
     * Applications call this interface method to add a Neutron object to the
     * concurrent map.
     *
     * @param input
     *            OpenStackNeutron object
     * @return result with indication on whether the object was added or not and if so why
     * @throws OperationFailedException if the write (or a required implicit read) failed
     */
    Result add(T input) throws OperationFailedException;

    /**
     * Applications call this interface method to remove a Neutron object to the
     * concurrent map.
     *
     * @param uuid
     *            identifier for the neutron object
     * @return boolean on whether the object was removed or not
     * @throws OperationFailedException if the remove (or a required implicit read) failed
     */
    boolean remove(String uuid) throws OperationFailedException;

    /**
     * Applications call this interface method to edit a Neutron object.
     *
     * @param uuid
     *            identifier of the neutron object
     * @param delta
     *            OpenStackNeutron object containing changes to apply
     * @return boolean on whether the object was updated or not
     * @throws OperationFailedException if the update (or a required implicit read) failed
     */
    boolean update(String uuid, T delta) throws OperationFailedException;

    // TODO The Exception Result should eventually be replaced by propagating exceptions, and removed
    enum Result { Success, AlreadyExists, Exception }

}
