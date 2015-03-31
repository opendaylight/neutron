/*
 * Copyright (C) 2015 IBM, Inc.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.spi;

/**
 * This interface defines the methods a service that wishes to be aware of NeutronVPNIPSECSiteConnection needs to implement
 *
 */

public interface INeutronVPNIPSECSiteConnectionAware {

    /**
     * Services provide this interface method to indicate if the specified NeutronVPNIPSECSiteConnection can be created
     *
     * @param ipsecSiteConnection
     *            instance of proposed new NeutronVPNIPSECSiteConnection object
     * @return integer
     *            the return value is understood to be a HTTP status code.  A return value outside of 200 through 299
     *            results in the create operation being interrupted and the returned status value reflected in the
     *            HTTP response.
     */
    public int canCreateNeutronVPNIPSECSiteConnection(NeutronVPNIPSECSiteConnection ipsecSiteConnection);

    /**
     * Services provide this interface method for taking action after a NeutronVPNIPSECSiteConnection has been created
     *
     * @param ipsecSiteConnection
     *            instance of new NeutronVPNIPSECSiteConnection object
     */
    public void neutronVPNIPSECSiteConnectionCreated(NeutronVPNIPSECSiteConnection ipsecSiteConnection);

    /**
     * Services provide this interface method to indicate if the
     * specified NeutronVPNIPSECSiteConnection can be changed using the specified
     * delta
     *
     * @param delta
     *            updates to the NeutronVPNIPSECSiteConnection object using patch semantics
     * @param original
     *            instance of the NeutronVPNIPSECSiteConnection object to be updated
     * @return integer
     *            the return value is understood to be a HTTP status code.  A return value outside of 200 through 299
     *            results in the update operation being interrupted and the returned status value reflected in the
     *            HTTP response.
     */
    public int canUpdateNeutronVPNIPSECSiteConnection(NeutronVPNIPSECSiteConnection delta, NeutronVPNIPSECSiteConnection original);

    /**
     * Services provide this interface method for taking action after a NeutronVPNIPSECSiteConnection has been updated
     *
     * @param ipsecSiteConnection
     *            instance of modified NeutronVPNIPSECSiteConnection object
     */
    public void neutronVPNIPSECSiteConnectionUpdated(NeutronVPNIPSECSiteConnection ipsecSiteConnection);

    /**
     * Services provide this interface method to indicate if the specified NeutronVPNIPSECSiteConnection can be deleted
     *
     * @param ipsecSiteConnection
     *            instance of the NeutronVPNIPSECSiteConnection object to be deleted
     * @return integer
     *            the return value is understood to be a HTTP status code.  A return value outside of 200 through 299
     *            results in the delete operation being interrupted and the returned status value reflected in the
     *            HTTP response.
     */
    public int canDeleteNeutronVPNIPSECSiteConnection(NeutronVPNIPSECSiteConnection ipsecSiteConnection);

    /**
     * Services provide this interface method for taking action after a NeutronVPNIPSECSiteConnection has been deleted
     *
     * @param ipsecSiteConnection
     *            instance of deleted NeutronVPNIPSECSiteConnection object
     */
    public void neutronVPNIPSECSiteConnectionDeleted(NeutronVPNIPSECSiteConnection ipsecSiteConnection);
}
