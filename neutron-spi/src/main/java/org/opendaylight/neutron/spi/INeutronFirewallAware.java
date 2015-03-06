/*
 * Copyright (C) 2014 Red Hat, Inc.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.spi;

/**
 * This interface defines the methods a service that wishes to be aware of Firewall Rules needs to implement
 *
 */

public interface INeutronFirewallAware {

    /**
     * Services provide this interface method to indicate if the specified firewall can be created
     * Deprecated as all Neutron FWaaS is experimental and so doesn't meet 
     * the scope of neutron northbound
     *
     * @deprecated
     * 
     * @param firewall
     *            instance of proposed new Firewall object
     * @return integer
     *            the return value is understood to be a HTTP status code.  A return value outside of 200 through 299
     *            results in the create operation being interrupted and the returned status value reflected in the
     *            HTTP response.
     */
    public int canCreateNeutronFirewall(NeutronFirewall firewall);

    /**
     * Services provide this interface method for taking action after a firewall has been created
     * Deprecated as all Neutron FWaaS is experimental and so doesn't meet 
     * the scope of neutron northbound
     *
     * @deprecated
     *
     * @param firewall
     *            instance of new Firewall object
     * @return void
     */
    public void neutronFirewallCreated(NeutronFirewall firewall);

    /**
     * Services provide this interface method to indicate if the specified firewall can be changed using the specified
     * delta
     * Deprecated as all Neutron FWaaS is experimental and so doesn't meet 
     * the scope of neutron northbound
     *
     * @deprecated
     *
     * @param delta
     *            updates to the firewall object using patch semantics
     * @param original
     *            instance of the Firewall object to be updated
     * @return integer
     *            the return value is understood to be a HTTP status code.  A return value outside of 200 through 299
     *            results in the update operation being interrupted and the returned status value reflected in the
     *            HTTP response.
     */
    public int canUpdateNeutronFirewall(NeutronFirewall delta, NeutronFirewall original);

    /**
     * Services provide this interface method for taking action after a firewall has been updated
     * Deprecated as all Neutron FWaaS is experimental and so doesn't meet 
     * the scope of neutron northbound
     *
     * @deprecated
     *
     * @param firewall
     *            instance of modified Firewall object
     * @return void
     */
    public void neutronFirewallUpdated(NeutronFirewall firewall);

    /**
     * Services provide this interface method to indicate if the specified firewall can be deleted
     * Deprecated as all Neutron FWaaS is experimental and so doesn't meet 
     * the scope of neutron northbound
     *
     * @deprecated
     *
     * @param firewall
     *            instance of the Firewall object to be deleted
     * @return integer
     *            the return value is understood to be a HTTP status code.  A return value outside of 200 through 299
     *            results in the delete operation being interrupted and the returned status value reflected in the
     *            HTTP response.
     */
    public int canDeleteNeutronFirewall(NeutronFirewall firewall);

    /**
     * Services provide this interface method for taking action after a firewall has been deleted
     * Deprecated as all Neutron FWaaS is experimental and so doesn't meet 
     * the scope of neutron northbound
     *
     * @deprecated
     *
     * @param firewall
     *            instance of deleted Firewall object
     * @return void
     */
    public void neutronFirewallDeleted(NeutronFirewall firewall);
}
