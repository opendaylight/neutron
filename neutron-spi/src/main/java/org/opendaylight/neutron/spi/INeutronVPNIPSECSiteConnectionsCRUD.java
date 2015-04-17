/*
 * Copyright Tata Consultancy Services, 2015.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.spi;

import java.util.List;

public interface INeutronVPNIPSECSiteConnectionsCRUD {

    /**
     * yes Applications call this interface method to determine if a particular
     * NeutronVPNIPSECSiteConnection object exists
     * 
     * @param policyID
     *            policyID of the NeutronVPNIPSECSiteConnection object
     * @return boolean
     */

    public boolean neutronVPNIPSECSiteConnectionsExists(String policyID);

    /**
     * Applications call this interface method to return if a particular
     * NeutronVPNIPSECSiteConnection object exists
     * 
     * @param uuid
     *            UUID of the NeutronVPNIPSECSiteConnection object
     * @return {@link org.opendaylight.neutron.neutron.spi.NeutronVPNIPSECSiteConnection}
     *         NeutronVPNIPSECSiteConnection class
     */

    public NeutronVPNIPSECSiteConnection getNeutronVPNIPSECSiteConnections(String policyID);

    /**
     * Applications call this interface method to return all
     * NeutronVPNIPSECSiteConnection objects
     * 
     * @return List of NeutronVPNIPSECSiteConnection objects
     */

    public List<NeutronVPNIPSECSiteConnection> getAllNeutronVPNIPSECSiteConnections();

    /**
     * Applications call this interface method to add a
     * NeutronVPNIPSECSiteConnection object to the concurrent map
     * 
     * @param input
     *            NeutronVPNIPSECSiteConnection object
     * @return boolean on whether the object was added or not
     */

    public boolean addNeutronVPNIPSECSiteConnections(NeutronVPNIPSECSiteConnection input);

    /**
     * Applications call this interface method to remove a
     * NeutronVPNIPSECSiteConnection object to the concurrent map
     * 
     * @param policyID
     *            identifier for the NeutronVPNIPSECSiteConnection object
     * @return boolean on whether the object was removed or not
     */

    public boolean removeNeutronVPNIPSECSiteConnections(String policyID);

    /**
     * Applications call this interface method to edit a
     * NeutronVPNIPSECSiteConnection object
     * 
     * @param policyID
     *            identifier of the NeutronVPNIPSECSiteConnection object
     * @param delta
     *            NeutronVPNIPSECSiteConnection object containing changes to
     *            apply
     * @return boolean on whether the object was updated or not
     */

    public boolean updateNeutronVPNIPSECSiteConnections(String policyID, NeutronVPNIPSECSiteConnection delta);

    /**
     * Applications call this interface method to see if a connection is in use
     * 
     * @param policyID
     *            identifier of the NeutronVPNIPSECSiteConnection object
     * @return boolean on whether the connection is already is in use or not
     */

    public boolean neutronVPNIPSECSiteConnectionsInUse(String policyID);

}