/*
 * Copyright (C) 2015 IBM Corporation.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.spi;

import java.util.List;

public interface INeutronVPNIKEPolicyCRUD {

    /**yes
     * Applications call this interface method to determine if a particular
     *NeutronVPNIKEPolicy object exists
     *
     * @param uuid
     *            UUID of the NeutronVPNIKEPolicy object
     * @return boolean
     */

    public boolean neutronVPNIKEPolicyExists(String uuid);

    /**
     * Applications call this interface method to return if a particular
     * NeutronVPNIKEPolicy object exists
     *
     * @param uuid
     *            UUID of the NeutronVPNIKEPolicy object
     * @return {@link org.opendaylight.neutron.spi.NeutronVPNIKEPolicy}
     *          OpenStackNeutronVPNIKEPolicy class
     */

    public NeutronVPNIKEPolicy getNeutronVPNIKEPolicy(String uuid);

    /**
     * Applications call this interface method to return all NeutronVPNIKEPolicy objects
     *
     * @return List of OpenStackNetworks objects
     */

    public List<NeutronVPNIKEPolicy> getAllNeutronVPNIKEPolicies();

    /**
     * Applications call this interface method to add a NeutronVPNIKEPolicy object to the
     * concurrent map
     *
     * @param input
     *            OpenStackNetwork object
     * @return boolean on whether the object was added or not
     */

    public boolean addNeutronVPNIKEPolicy(NeutronVPNIKEPolicy input);

    /**
     * Applications call this interface method to remove a Neutron NeutronVPNIKEPolicy object to the
     * concurrent map
     *
     * @param uuid
     *            identifier for the NeutronVPNIKEPolicy object
     * @return boolean on whether the object was removed or not
     */

    public boolean removeNeutronVPNIKEPolicy(String uuid);

    /**
     * Applications call this interface method to edit a NeutronVPNIKEPolicy object
     *
     * @param uuid
     *            identifier of the NeutronVPNIKEPolicy object
     * @param delta
     *            OpenStackNeutronVPNIKEPolicy object containing changes to apply
     * @return boolean on whether the object was updated or not
     */

    public boolean updateNeutronVPNIKEPolicy(String uuid, NeutronVPNIKEPolicy delta);

    /**
     * Applications call this interface method to see if a MAC address is in use
     *
     * @param uuid
     *            identifier of the NeutronVPNIKEPolicy object
     * @return boolean on whether the macAddress is already associated with a
     * port or not
     */

    public boolean neutronVPNIKEPolicyInUse(String uuid);

}
