/*
 * Copyright (C) 2014 Red Hat, Inc.  All rights reserved.
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
 * This interface defines the methods for CRUD of NB OpenStack LoadBalancerPool objects.
 */
public interface INeutronLoadBalancerPoolCRUD extends INeutronCRUD<NeutronLoadBalancerPool> {

    /**
     * Applications call this interface method to determine if a particular
     * NeutronLoadBalancerPoolMember object exists.
     *
     * @param poolUuid
     *            UUID of the NeutronLoadBalancerPool object
     * @param uuid
     *            UUID of the NeutronLoadBalancerPoolMember object
     * @return boolean
     * @throws ReadFailedException if the read failed
     */
    boolean neutronLoadBalancerPoolMemberExists(String poolUuid, String uuid) throws ReadFailedException;

    /**
     * Applications call this interface method to return if a particular
     * NeutronLoadBalancerPoolMember object exists.
     *
     * @param poolUuid
     *            UUID of the NeutronLoadBalancerPool object
     * @param uuid
     *            UUID of the NeutronLoadBalancerPoolMember object
     * @return {@link org.opendaylight.neutron.spi.NeutronLoadBalancerPoolMember}
     *          OpenStackNeutronLoadBalancerPoolMember class
     * @throws ReadFailedException if the read failed
     */
    NeutronLoadBalancerPoolMember getNeutronLoadBalancerPoolMember(String poolUuid, String uuid)
            throws ReadFailedException;

    /**
     * Applications call this interface method to return all NeutronLoadBalancerPoolMember objects.
     *
     * @param poolUuid
     *            UUID of the NeutronLoadBalancerPool object
     * @return List of OpenStackNetworks objects
     * @throws ReadFailedException if the read failed
     */
    List<NeutronLoadBalancerPoolMember> getAllNeutronLoadBalancerPoolMembers(String poolUuid)
            throws ReadFailedException;

    /**
     * Applications call this interface method to add a NeutronLoadBalancerPoolMember object to the
     * concurrent map.
     *
     * @param poolUuid
     *            UUID of the NeutronLoadBalancerPool object
     * @param input
     *            OpenStackNetwork object
     * @return boolean on whether the object was added or not
     * @throws OperationFailedException if the read or write failed
     */
    boolean addNeutronLoadBalancerPoolMember(String poolUuid, NeutronLoadBalancerPoolMember input)
            throws OperationFailedException;

    /**
     * Applications call this interface method to remove a Neutron NeutronLoadBalancerPoolMember object to the
     * concurrent map.
     *
     * @param poolUuid
     *            UUID of the NeutronLoadBalancerPool object
     * @param uuid
     *            identifier for the NeutronLoadBalancerPoolMember object
     * @return boolean on whether the object was removed or not
     * @throws OperationFailedException if the read or write failed
     */
    boolean removeNeutronLoadBalancerPoolMember(String poolUuid, String uuid) throws OperationFailedException;

    /**
     * Applications call this interface method to edit a NeutronLoadBalancerPoolMember object.
     *
     * @param poolUuid
     *            identifier of the NeutronLoadBalancerPool object
     * @param uuid
     *            identifier of the NeutronLoadBalancerPoolMember object
     * @param delta
     *            OpenStackNeutronLoadBalancerPoolMember object containing changes to apply
     * @return boolean on whether the object was updated or not
     * @throws OperationFailedException if the read or write operation failed
     */
    boolean updateNeutronLoadBalancerPoolMember(String poolUuid, String uuid, NeutronLoadBalancerPoolMember delta)
            throws OperationFailedException;

    /**
     * Applications call this interface method to see if a MAC address is in use.
     *
     * @param poolUuid
     *            identifier of the NeutronLoadBalancerPool object
     * @param uuid
     *            identifier of the NeutronLoadBalancerPoolMember object
     * @return boolean on whether the macAddress is already associated with a
     *             port or not
     * @throws ReadFailedException if the read operation failed
     */
    boolean neutronLoadBalancerPoolMemberInUse(String poolUuid, String uuid) throws ReadFailedException;
}
