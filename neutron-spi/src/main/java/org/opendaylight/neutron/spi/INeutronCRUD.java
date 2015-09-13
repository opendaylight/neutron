/*
 * Copyright (c) 2015 Intel Corporation.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.spi;

import java.util.List;

import org.opendaylight.controller.md.sal.binding.api.BindingTransactionChain;

/**
 * This interface defines the methods for CRUD of NB neutron objects
 *
 */

public interface INeutronCRUD<T extends INeutronObject> {
    /**
     * Applications call this interface method to determine if a particular
     * Port object exists
     *
     * @return {@link org.opendaylight.controller.md.sal.binding.api.BindingTransactionChain}
     *            binding transaction chain class for chained transaction
     */
    public BindingTransactionChain createTransactionChain();

    /**
     * Applications call this interface method to determine if a particular
     * Neutron object exists
     *
     * @param uuid
     *            UUID of the Neutron object
     * @param chain
     *            transaction chain for md-sal transaction.
     *            if null is passed, the chain will be created/closed internally
     * @return boolean
     */

    boolean exists(String uuid, BindingTransactionChain chain);

    /**
     * Applications call this interface method to return if a particular
     * Neutron object exists
     *
     * @param uuid
     *            UUID of the Neutron object
     * @param chain
     *            transaction chain for md-sal transaction.
     *            if null is passed, the chain will be created/closed internally
     * @return {@link org.opendaylight.neutron.spi.INeutronObject}
     *          OpenStack Neutron class
     */

    T get(String uuid, BindingTransactionChain chain);

    /**
     * Applications call this interface method to return all Neutron objects
     *
     * @param chain
     *            transaction chain for md-sal transaction.
     *            if null is passed, the chain will be created/closed internally
     * @return List of OpenStackNeutrons objects
     */

    List<T> getAll(BindingTransactionChain chain);

    /**
     * Applications call this interface method to add a Neutron object to the
     * concurrent map
     *
     * @param input
     *            OpenStackNeutron object
     * @param chain
     *            transaction chain for md-sal transaction.
     *            if null is passed, the chain will be created/closed internally
     * @return boolean on whether the object was added or not
     */

    boolean add(T input, BindingTransactionChain chain);

    /**
     * Applications call this interface method to remove a Neutron object to the
     * concurrent map
     *
     * @param uuid
     *            identifier for the neutron object
     * @param chain
     *            transaction chain for md-sal transaction.
     *            if null is passed, the chain will be created/closed internally
     * @return boolean on whether the object was removed or not
     */

    boolean remove(String uuid, BindingTransactionChain chain);

    /**
     * Applications call this interface method to edit a Neutron object
     *
     * @param uuid
     *            identifier of the neutron object
     * @param delta
     *            OpenStackNeutron object containing changes to apply
     * @param chain
     *            transaction chain for md-sal transaction.
     *            if null is passed, the chain will be created/closed internally
     * @return boolean on whether the object was updated or not
     */

    boolean update(String uuid, T delta, BindingTransactionChain chain);

    /**
     * Applications call this interface method to determine if a Neutron object
     * is use
     *
     * @param uuid
     *            identifier of the neutron object
     * @param chain
     *            transaction chain for md-sal transaction.
     *            if null is passed, the chain will be created/closed internally
     * @return boolean on whether the neutron is in use or not
     */

    boolean inUse(String uuid, BindingTransactionChain chain);
}
