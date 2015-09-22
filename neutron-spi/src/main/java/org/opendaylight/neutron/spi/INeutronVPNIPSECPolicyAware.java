/*
 * Copyright (c) 2015 IBM Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.spi;

/**
 * This interface defines the methods a service that wishes to be aware of NeutronVPNIPSECPolicy needs to implement
 *
 */
@Deprecated
public interface INeutronVPNIPSECPolicyAware {

    /**
     * Services provide this interface method to indicate if the specified NeutronVPNIPSECPolicy can be created
     *
     * @param ipsecPolicy
     *            instance of proposed new NeutronVPNIPSECPolicy object
     * @return integer
     *            the return value is understood to be a HTTP status code.  A return value outside of 200 through 299
     *            results in the create operation being interrupted and the returned status value reflected in the
     *            HTTP response.
     */
    int canCreateNeutronVPNIPSECPolicy(NeutronVPNIPSECPolicy ipsecPolicy);

    /**
     * Services provide this interface method for taking action after a NeutronVPNIPSECPolicy has been created
     *
     * @param ipsecPolicy
     *            instance of new NeutronVPNIPSECPolicy object
     */
    void neutronVPNIPSECPolicyCreated(NeutronVPNIPSECPolicy ipsecPolicy);

    /**
     * Services provide this interface method to indicate if the
     * specified NeutronVPNIPSECPolicy can be changed using the specified
     * delta
     *
     * @param delta
     *            updates to the NeutronVPNIPSECPolicy object using patch semantics
     * @param original
     *            instance of the NeutronVPNIPSECPolicy object to be updated
     * @return integer
     *            the return value is understood to be a HTTP status code.  A return value outside of 200 through 299
     *            results in the update operation being interrupted and the returned status value reflected in the
     *            HTTP response.
     */
    int canUpdateNeutronVPNIPSECPolicy(NeutronVPNIPSECPolicy delta, NeutronVPNIPSECPolicy original);

    /**
     * Services provide this interface method for taking action after a NeutronVPNIPSECPolicy has been updated
     *
     * @param ipsecPolicy
     *            instance of modified NeutronVPNIPSECPolicy object
     */
    void neutronVPNIPSECPolicyUpdated(NeutronVPNIPSECPolicy ipsecPolicy);

    /**
     * Services provide this interface method to indicate if the specified NeutronVPNIPSECPolicy can be deleted
     *
     * @param ipsecPolicy
     *            instance of the NeutronVPNIPSECPolicy object to be deleted
     * @return integer
     *            the return value is understood to be a HTTP status code.  A return value outside of 200 through 299
     *            results in the delete operation being interrupted and the returned status value reflected in the
     *            HTTP response.
     */
    int canDeleteNeutronVPNIPSECPolicy(NeutronVPNIPSECPolicy ipsecPolicy);

    /**
     * Services provide this interface method for taking action after a NeutronVPNIPSECPolicy has been deleted
     *
     * @param ipsecPolicy
     *            instance of deleted NeutronVPNIPSECPolicy object
     */
    void neutronVPNIPSECPolicyDeleted(NeutronVPNIPSECPolicy ipsecPolicy);
}
