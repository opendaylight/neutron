/*
 * Copyright (c) 2015 IBM Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.spi;

import java.util.List;

public interface INeutronVPNIPSECPolicyCRUD
    extends INeutronCRUD<NeutronVPNIPSECPolicy> {

    /**yes
     * Applications call this interface method to determine if a particular
     *NeutronVPNIPSECPolicy object exists
     *
     * @param uuid
     *            UUID of the NeutronVPNIPSECPolicy object
     * @return boolean
     */

    boolean neutronVPNIPSECPolicyExists(String uuid);

    /**
     * Applications call this interface method to return if a particular
     * NeutronVPNIPSECPolicy object exists
     *
     * @param uuid
     *            UUID of the NeutronVPNIPSECPolicy object
     * @return {@link org.opendaylight.neutron.spi.NeutronVPNIPSECPolicy}
     *          OpenStackNeutronVPNIPSECPolicy class
     */

    NeutronVPNIPSECPolicy getNeutronVPNIPSECPolicy(String uuid);

    /**
     * Applications call this interface method to return all NeutronVPNIPSECPolicy objects
     *
     * @return List of OpenStackNetworks objects
     */

    List<NeutronVPNIPSECPolicy> getAllNeutronVPNIPSECPolicies();

    /**
     * Applications call this interface method to add a NeutronVPNIPSECPolicy object to the
     * concurrent map
     *
     * @param input
     *            OpenStackNetwork object
     * @return boolean on whether the object was added or not
     */

    boolean addNeutronVPNIPSECPolicy(NeutronVPNIPSECPolicy input);

    /**
     * Applications call this interface method to remove a Neutron NeutronVPNIPSECPolicy object to the
     * concurrent map
     *
     * @param uuid
     *            identifier for the NeutronVPNIPSECPolicy object
     * @return boolean on whether the object was removed or not
     */

    boolean removeNeutronVPNIPSECPolicy(String uuid);

    /**
     * Applications call this interface method to edit a NeutronVPNIPSECPolicy object
     *
     * @param uuid
     *            identifier of the NeutronVPNIPSECPolicy object
     * @param delta
     *            OpenStackNeutronVPNIPSECPolicy object containing changes to apply
     * @return boolean on whether the object was updated or not
     */

    boolean updateNeutronVPNIPSECPolicy(String uuid, NeutronVPNIPSECPolicy delta);

    /**
     * Applications call this interface method to see if a MAC address is in use
     *
     * @param uuid
     *            identifier of the NeutronVPNIPSECPolicy object
     * @return boolean on whether the macAddress is already associated with a
     * port or not
     */

    boolean neutronVPNIPSECPolicyInUse(String uuid);

}
