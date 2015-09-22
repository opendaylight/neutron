/*
 * Copyright (c) 2015 IBM Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.spi;

/**
 * This interface defines the methods a service that wishes to be aware of Neutron Metering Labels needs to implement
 *
 */
@Deprecated
public interface INeutronMeteringLabelAware {

    /**
     * Services provide this interface method to indicate if the specified network can be created
     *
     * @param meteringLabel
     *            instance of proposed new Neutron Metering Label object
     * @return integer
     *            the return value is understood to be a HTTP status code.  A return value outside of 200 through 299
     *            results in the create operation being interrupted and the returned status value reflected in the
     *            HTTP response.
     */
    int canCreateMeteringLabel(NeutronMeteringLabel meteringLabel);

    /**
     * Services provide this interface method for taking action after a network has been created
     *
     * @param meteringLabel
     *            instance of new Neutron Metering Label object
     */
    void neutronMeteringLabelCreated(NeutronMeteringLabel meteringLabel);

    /**
     * Services provide this interface method to indicate if the specified
     * metering label can be deleted
     *
     * @param meteringLabel
     *            instance of the Neutron Metering Label object to be deleted
     * @return integer
     *            the return value is understood to be a HTTP status code.  A return value outside of 200 through 299
     *            results in the delete operation being interrupted and the returned status value reflected in the
     *            HTTP response.
     */
    int canDeleteMeteringLabel(NeutronMeteringLabel meteringLabel);

    /**
     * Services provide this interface method for taking action after a network has been deleted
     *
     * @param meteringLabel
     *            instance of deleted Neutron Metering Label object
     */
    void neutronMeteringLabelDeleted(NeutronMeteringLabel meteringLabel);
}
