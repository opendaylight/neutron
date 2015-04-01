/*
 * Copyright (C) 2015 IBM, Inc.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.spi;

/**
 * This interface defines the methods a service that wishes to be aware of NeutronVPNService needs to implement
 *
 */

public interface INeutronVPNServiceAware {

    /**
     * Services provide this interface method to indicate if the specified NeutronVPNService can be created
     *
     * @param vpnService
     *            instance of proposed new NeutronVPNService object
     * @return integer
     *            the return value is understood to be a HTTP status code.  A return value outside of 200 through 299
     *            results in the create operation being interrupted and the returned status value reflected in the
     *            HTTP response.
     */
    public int canCreateNeutronVPNService(NeutronVPNService vpnService);

    /**
     * Services provide this interface method for taking action after a NeutronVPNService has been created
     *
     * @param vpnService
     *            instance of new NeutronVPNService object
     */
    public void neutronVPNServiceCreated(NeutronVPNService vpnService);

    /**
     * Services provide this interface method to indicate if the
     * specified NeutronVPNService can be changed using the specified
     * delta
     *
     * @param delta
     *            updates to the NeutronVPNService object using patch semantics
     * @param original
     *            instance of the NeutronVPNService object to be updated
     * @return integer
     *            the return value is understood to be a HTTP status code.  A return value outside of 200 through 299
     *            results in the update operation being interrupted and the returned status value reflected in the
     *            HTTP response.
     */
    public int canUpdateNeutronVPNService(NeutronVPNService delta, NeutronVPNService original);

    /**
     * Services provide this interface method for taking action after a NeutronVPNService has been updated
     *
     * @param vpnService
     *            instance of modified NeutronVPNService object
     */
    public void neutronVPNServiceUpdated(NeutronVPNService vpnService);

    /**
     * Services provide this interface method to indicate if the specified NeutronVPNService can be deleted
     *
     * @param vpnService
     *            instance of the NeutronVPNService object to be deleted
     * @return integer
     *            the return value is understood to be a HTTP status code.  A return value outside of 200 through 299
     *            results in the delete operation being interrupted and the returned status value reflected in the
     *            HTTP response.
     */
    public int canDeleteNeutronVPNService(NeutronVPNService vpnService);

    /**
     * Services provide this interface method for taking action after a NeutronVPNService has been deleted
     *
     * @param vpnService
     *            instance of deleted NeutronVPNService object
     */
    public void neutronVPNServiceDeleted(NeutronVPNService vpnService);
}
