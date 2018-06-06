/*
 * Copyright (c) 2015 Intel Corporation.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.neutron.spi;

import java.util.List;

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
     */
    boolean exists(String uuid); // TODO remove and replace usages by variant below with Tx

    // TODO boolean exists(String uuid, ReadTransaction tx);

    /**
     * Applications call this interface method to return if a particular
     * Neutron object exists.
     *
     * @param uuid
     *            UUID of the Neutron object
     * @return {@link org.opendaylight.neutron.spi.INeutronObject}
     *          OpenStack Neutron class
     */
    T get(String uuid);

    /**
     * Applications call this interface method to return all Neutron objects.
     *
     * @return List of OpenStackNeutrons objects
     */
    List<T> getAll();

    /**
     * Applications call this interface method to add a Neutron object to the
     * concurrent map.
     *
     * @param input
     *            OpenStackNeutron object
     * @return boolean on whether the object was added or not
     */
    boolean add(T input);

    /**
     * Applications call this interface method to remove a Neutron object to the
     * concurrent map.
     *
     * @param uuid
     *            identifier for the neutron object
     * @return boolean on whether the object was removed or not
     */
    boolean remove(String uuid);

    /**
     * Applications call this interface method to edit a Neutron object.
     *
     * @param uuid
     *            identifier of the neutron object
     * @param delta
     *            OpenStackNeutron object containing changes to apply
     * @return boolean on whether the object was updated or not
     */
    boolean update(String uuid, T delta);
}
