/*
 * Copyright (C) 2014 Red Hat, Inc.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.spi;

import java.util.List;

/**
 * This interface defines the methods for CRUD of NB OpenStack Qos rule objects
 *
 */

public interface INeutronQosDscpRuleCRUD
        extends INeutronCRUD<NeutronQosDscpRule> {
    /**
     * Applications call this interface method to determine if a particular
     *Qosrule object exists
     *
     * @param uuid
     *            UUID of the Qos rule object
     * @return boolean
     */

    boolean NeutronQosDscpRuleExists(String uuid);

    /**
     * Applications call this interface method to return if a particular
     * Qosrule object exists
     *
     * @param uuid
     *            UUID of the Qos rule object
     * @return {@link NeutronQosDscpRule}
     *          OpenStackQosrule class
     */

    NeutronQosDscpRule getNeutronQosDscpRule(String uuid);

    /**
     * Applications call this interface method to return all Qos rule objects
     *
     * @return List of OpenStack Qos rule objects
     */

    List<NeutronQosDscpRule> getAllNeutronQosPolicies();

    /**
     * Applications call this interface method to add a Qos rule object to the
     * concurrent map
     *
     * @param input
     *            OpenStackNetwork object
     * @return boolean on whether the object was added or not
     */

    boolean addNeutronQosDscpRule(NeutronQosDscpRule input);

    /**
     * Applications call this interface method to remove a Neutron Qosrule object to the
     * concurrent map
     *
     * @param uuid
     *            identifier for the Qos rule object
     * @return boolean on whether the object was removed or not
     */

    boolean removeNeutronQosDscpRule(String uuid);

    /**
     * Applications call this interface method to edit a Qosrule object
     *
     * @param uuid
     *            identifier of the Qos rule object
     * @param delta
     *            OpenStackQosrule object containing changes to apply
     * @return boolean on whether the object was updated or not
     */

    boolean updateNeutronQosDscpRule(String uuid, NeutronQosDscpRule delta);

    /**
     * Applications call this interface method to see if a MAC address is in use
     *
     * @param uuid
     *            identifier of the Qos rule object
     * @return boolean on whether the macAddress is already associated with a
     * port or not
     */

    boolean NeutronQosDscpRuleInUse(String uuid);

}
