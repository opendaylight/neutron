/*
 * Copyright (c) 2016 Intel Corporation.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.neutron.spi;

import java.util.List;

/**
 * This interface defines the methods for CRUD of NB OpenStack Qos BandWidth Rule objects
 *
 */

public interface INeutronQosBandWidthRuleCRUD
    extends INeutronCRUD<NeutronQosBandWidthRule> {
    /**
     * Applications call this interface method to determine if a particular
     *Qos BandWidth Rule object exists
     *
     * @param uuid
     *            UUID of the Qos BandWidth RuleDSCP Rule object
     * @return boolean
     */

    boolean neutronQosBandWidthRuleExists(String uuid);

    /**
     * Applications call this interface method to return if a particular
     * Qos BandWidth Rule object exists
     *
     * @param uuid
     *            UUID of the Qos BandWidth Rule object
     * @return {@link NeutronQosBandWidthRule}
     *          OpenStack Qos BandWidth Rule class
     */

    NeutronQosBandWidthRule getNeutronQosBandWidthRule(String uuid);

    /**
     * Applications call this interface method to return all Qos BandWidth Rule objects
     *
     * @return List of OpenStack Qos BandWidth Rule objects
     */

    List<NeutronQosBandWidthRule> getAllNeutronQosBandWidthRules();

    /**
     * Applications call this interface method to add a Qos BandWidth Rule object to the
     * concurrent map
     *
     * @param input
     *            OpenStack Network object
     * @return boolean on whether the object was added or not
     */

    boolean addNeutronQosBandWidthRule(NeutronQosBandWidthRule input);

    /**
     * Applications call this interface method to remove a Neutron BandWidth Rule object to the
     * concurrent map
     *
     * @param uuid
     *            identifier for the Qos BandWidth Rule object
     * @return boolean on whether the object was removed or not
     */

    boolean removeNeutronQosBandWidthRule(String uuid);

    /**
     * Applications call this interface method to edit a Qos BandWidth Rule object
     *
     * @param uuid
     *            identifier of the Qos BandWidth Rule object
     * @param delta
     *            OpenStack Qos BandWidth Rule object containing changes to apply
     * @return boolean on whether the object was updated or not
     */

    boolean updateNeutronQosBandWidthRule(String uuid, NeutronQosBandWidthRule delta);

    /**
     * Applications call this interface method to see if a MAC address is in use
     *
     * @param uuid
     *            identifier of the Qos Policy object
     * @return boolean on whether the macAddress is already associated with a
     * port or not
     */

    boolean neutronQosBandWidthRuleInUse(String uuid);

}
