/*
 * Copyright (c) 2015 IBM Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.spi;

import java.util.List;

public interface INeutronMeteringLabelRuleCRUD
    extends INeutronCRUD<NeutronMeteringLabelRule> {

    /**
     * Applications call this interface method to determine if a particular
     *NeutronMeteringLabelRule object exists
     *
     * @param uuid
     *            UUID of the NeutronMeteringLabelRule object
     * @return boolean
     */

    boolean neutronMeteringLabelRuleExists(String uuid);

    /**
     * Applications call this interface method to return if a particular
     * NeutronMeteringLabelRule object exists
     *
     * @param uuid
     *            UUID of the NeutronMeteringLabelRule object
     * @return {@link org.opendaylight.neutron.spi.NeutronMeteringLabelRule}
     *          OpenStackNeutronMeteringLabelRule class
     */

    NeutronMeteringLabelRule getNeutronMeteringLabelRule(String uuid);

    /**
     * Applications call this interface method to return all NeutronMeteringLabelRule objects
     *
     * @return List of OpenStackNetworks objects
     */

    List<NeutronMeteringLabelRule> getAllNeutronMeteringLabelRules();

    /**
     * Applications call this interface method to add a NeutronMeteringLabelRule object to the
     * concurrent map
     *
     * @param input
     *            OpenStackNetwork object
     * @return boolean on whether the object was added or not
     */

    boolean addNeutronMeteringLabelRule(NeutronMeteringLabelRule input);

    /**
     * Applications call this interface method to remove a Neutron NeutronMeteringLabelRule object to the
     * concurrent map
     *
     * @param uuid
     *            identifier for the NeutronMeteringLabelRule object
     * @return boolean on whether the object was removed or not
     */

    boolean removeNeutronMeteringLabelRule(String uuid);

    /**
     * Applications call this interface method to edit a NeutronMeteringLabelRule object
     *
     * @param uuid
     *            identifier of the NeutronMeteringLabelRule object
     * @param delta
     *            OpenStackNeutronMeteringLabelRule object containing changes to apply
     * @return boolean on whether the object was updated or not
     */

    boolean updateNeutronMeteringLabelRule(String uuid, NeutronMeteringLabelRule delta);

    /**
     * Applications call this interface method to see if a MAC address is in use
     *
     * @param uuid
     *            identifier of the NeutronMeteringLabelRule object
     * @return boolean on whether the macAddress is already associated with a
     * port or not
     */

    boolean neutronMeteringLabelRuleInUse(String uuid);

}
