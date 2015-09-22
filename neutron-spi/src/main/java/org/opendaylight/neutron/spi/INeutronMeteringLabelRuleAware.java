/*
 * Copyright (c) 2015 IBM Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.spi;

/**
 * This interface defines the methods a service that wishes to be aware of Neutron Metering Label Rules needs to implement
 *
 */
@Deprecated
public interface INeutronMeteringLabelRuleAware {

    /**
     * Services provide this interface method to indicate if the specified
     * metering lable rule can be created
     *
     * @param meteringLabelRule
     *            instance of proposed new Neutron Metering Label Rule object
     * @return integer
     *            the return value is understood to be a HTTP status code.  A return value outside of 200 through 299
     *            results in the create operation being interrupted and the returned status value reflected in the
     *            HTTP response.
     */
    int canCreateMeteringLabelRule(NeutronMeteringLabelRule meteringLabelRule);

    /**
     * Services provide this interface method for taking action after a meteringLabelRule has been created
     *
     * @param meteringLabelRule
     *            instance of new Neutron Metering Label Rule object
     */
    void neutronMeteringLabelRuleCreated(NeutronMeteringLabelRule meteringLabelRule);

    /**
     * Services provide this interface method to indicate if the specified meteringLabelRule can be deleted
     *
     * @param meteringLabelRule
     *            instance of the Neutron Metering Label Rule object to be deleted
     * @return integer
     *            the return value is understood to be a HTTP status code.  A return value outside of 200 through 299
     *            results in the delete operation being interrupted and the returned status value reflected in the
     *            HTTP response.
     */
    int canDeleteMeteringLabelRule(NeutronMeteringLabelRule meteringLabelRule);

    /**
     * Services provide this interface method for taking action after a meteringLabelRule has been deleted
     *
     * @param meteringLabelRule
     *            instance of deleted Neutron Metering Label Rule object
     */
    void neutronMeteringLabelRuleDeleted(NeutronMeteringLabelRule meteringLabelRule);
}
