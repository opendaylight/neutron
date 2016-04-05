/*
 * Copyright (c) 2016 Intel Corporation.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */


package org.opendaylight.neutron.spi;

import java.util.List;

/**
 * This interface defines the methods for CRUD of NB OpenStack Qos Policy objects
 *
 */

public interface INeutronQosPolicyCRUD
    extends INeutronCRUD<NeutronQosPolicy> {
    /**
     * Applications call this interface method to determine if a particular
     *Qos Policy object exists
     *
     * @param uuid
     *            UUID of the Qos Policy object
     * @return boolean
     */

    boolean neutronQosPolicyExists(String uuid);

    /**
     * Applications call this interface method to return if a particular
     * QosPolicy object exists
     *
     * @param uuid
     *            UUID of the Qos Policy object
     * @return {@link NeutronQosPolicy}
     *          OpenStackQosPolicy class
     */

    NeutronQosPolicy getNeutronQosPolicy(String uuid);

    /**
     * Applications call this interface method to return all Qos Policy objects
     *
     * @return List of OpenStack Qos Policy objects
     */

    List<NeutronQosPolicy> getAllNeutronQosPolicies();

    /**
     * Applications call this interface method to add a Qos Policy object to the
     * concurrent map
     *
     * @param input
     *            OpenStackNetwork object
     * @return boolean on whether the object was added or not
     */

    boolean addNeutronQosPolicy(NeutronQosPolicy input);

    /**
     * Applications call this interface method to remove a Neutron QosPolicy object to the
     * concurrent map
     *
     * @param uuid
     *            identifier for the Qos Policy object
     * @return boolean on whether the object was removed or not
     */

    boolean removeNeutronQosPolicy(String uuid);

    /**
     * Applications call this interface method to edit a QosPolicy object
     *
     * @param uuid
     *            identifier of the Qos Policy object
     * @param delta
     *            OpenStackQosPolicy object containing changes to apply
     * @return boolean on whether the object was updated or not
     */

    boolean updateNeutronQosPolicy(String uuid, NeutronQosPolicy delta);

    /**
     * Applications call this interface method to see if a MAC address is in use
     *
     * @param uuid
     *            identifier of the Qos Policy object
     * @return boolean on whether the macAddress is already associated with a
     * port or not
     */

    boolean neutronQosPolicyInUse(String uuid);

}
