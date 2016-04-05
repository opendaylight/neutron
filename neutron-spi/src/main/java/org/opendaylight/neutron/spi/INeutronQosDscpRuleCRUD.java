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

public interface INeutronQosDscpRuleCRUD extends INeutronCRUD<NeutronQosDscpRule> {
    /**
     * Applications call this interface method to determine if a particular
     *Qos DSCP Rule object exists
     *
     * @param uuid
     *            UUID of the Qos DSCP rule object
     * @return boolean
     */

    boolean neutronQosDscpRuleExists(String uuid);

    /**
     * Applications call this interface method to return if a particular
     * Qos DSCP Rule object exists
     *
     * @param uuid
     *            UUID of the Qos DSCP Rule object
     * @return {@link NeutronQosDscpRule}
     *          OpenStack Qos DSCP Rule class
     */

    NeutronQosDscpRule getNeutronQosDscpRule(String uuid);

    /**
     * Applications call this interface method to return all Qos DSCP Rule objects
     *
     * @return List of OpenStack Qos DSCP Rule objects
     */

    List<NeutronQosDscpRule> getAllNeutronQosDscpRules();

    /**
     * Applications call this interface method to add a Qos DSCP Rule object to the
     * concurrent map
     *
     * @param input
     *            OpenStack Network object
     * @return boolean on whether the object was added or not
     */

    boolean addNeutronQosDscpRule(NeutronQosDscpRule input);

    /**
     * Applications call this interface method to remove a Neutron DSCP Rule object to the
     * concurrent map
     *
     * @param uuid
     *            identifier for the Qos DSCP Rule object
     * @return boolean on whether the object was removed or not
     */

    boolean removeNeutronQosDscpRule(String uuid);
    /**
     * Applications call this interface method to edit a Qos DSCP Rule object
     *
     * @param uuid
     *            identifier of the Qos DSCP Rule object
     * @param delta
     *            OpenStack Qos DSCP Rule object containing changes to apply
     * @return boolean on whether the object was updated or not
     */

    boolean updateNeutronQosDscpRule(String uuid, NeutronQosDscpRule delta);

    /**
     * Applications call this interface method to see if a MAC address is in use
     *
     * @param uuid
     *            identifier of the Qos Policy object
     * @return boolean on whether the macAddress is already associated with a
     * port or not
     */

    boolean neutronQosDscpRuleInUse(String uuid);

}
