/*
 * Copyright (c) 2015 IBM Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.spi;

/**
 * This interface defines the methods a service that wishes to be aware of NeutronVPNIKEPolicies needs to implement
 *
 */
@Deprecated
public interface INeutronVPNIKEPolicyAware {

    /**
     * Services provide this interface method to indicate if the specified NeutronVPNIKEPolicy can be created
     *
     * @param ikePolicy
     *            instance of proposed new NeutronVPNIKEPolicy object
     * @return integer
     *            the return value is understood to be a HTTP status code.  A return value outside of 200 through 299
     *            results in the create operation being interrupted and the returned status value reflected in the
     *            HTTP response.
     */
    int canCreateNeutronVPNIKEPolicy(NeutronVPNIKEPolicy ikePolicy);

    /**
     * Services provide this interface method for taking action after a NeutronVPNIKEPolicy has been created
     *
     * @param ikePolicy
     *            instance of new NeutronVPNIKEPolicy object
     */
    void neutronVPNIKEPolicyCreated(NeutronVPNIKEPolicy ikePolicy);

    /**
     * Services provide this interface method to indicate if the
     * specified NeutronVPNIKEPolicy can be changed using the specified
     * delta
     *
     * @param delta
     *            updates to the NeutronVPNIKEPolicy object using patch semantics
     * @param original
     *            instance of the NeutronVPNIKEPolicy object to be updated
     * @return integer
     *            the return value is understood to be a HTTP status code.  A return value outside of 200 through 299
     *            results in the update operation being interrupted and the returned status value reflected in the
     *            HTTP response.
     */
    int canUpdateNeutronVPNIKEPolicy(NeutronVPNIKEPolicy delta, NeutronVPNIKEPolicy original);

    /**
     * Services provide this interface method for taking action after a NeutronVPNIKEPolicy has been updated
     *
     * @param ikePolicy
     *            instance of modified NeutronVPNIKEPolicy object
     */
    void neutronVPNIKEPolicyUpdated(NeutronVPNIKEPolicy ikePolicy);

    /**
     * Services provide this interface method to indicate if the specified NeutronVPNIKEPolicy can be deleted
     *
     * @param ikePolicy
     *            instance of the NeutronVPNIKEPolicy object to be deleted
     * @return integer
     *            the return value is understood to be a HTTP status code.  A return value outside of 200 through 299
     *            results in the delete operation being interrupted and the returned status value reflected in the
     *            HTTP response.
     */
    int canDeleteNeutronVPNIKEPolicy(NeutronVPNIKEPolicy ikePolicy);

    /**
     * Services provide this interface method for taking action after a NeutronVPNIKEPolicy has been deleted
     *
     * @param ikePolicy
     *            instance of deleted NeutronVPNIKEPolicy object
     */
    void neutronVPNIKEPolicyDeleted(NeutronVPNIKEPolicy ikePolicy);
}
