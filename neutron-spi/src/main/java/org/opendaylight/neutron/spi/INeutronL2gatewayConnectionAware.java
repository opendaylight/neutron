/*
 * Copyright (c) 2015 Hewlett-Packard Development Company, L.P. and others.
 * All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.neutron.spi;
/**
 * This interface defines the methods a service that wishes to be aware of 
 * L2gateway Connection needs to implement
 */
public interface INeutronL2gatewayConnectionAware {
    /**
     * Services provide this interface method to indicate if the specified 
     * l2gateway Connection can be created.
     *
     * @param l2gatewayConnection
     *            instance of proposed new NeutronL2GatewayConnection object
     * @return integer
     *            the return value is understood to be a HTTP status code.  
     *            A return value outside of 200 through 299 results in the 
     *            create operation being interrupted and the returned status 
     *            value reflected in the HTTP response.
     */
    int canCreateNeutronL2gatewayConnection(NeutronL2GatewayConnection
                                            l2gatewayConnection);
    /**
     * Services provide this interface method for taking action after a L2Gateway
     * Connection has been created.
     *
     * @param l2gatewayConnection
     *            instance of new NeutronL2GatewayConnection object.
     */
    void neutronL2gatewayConnectionCreated(NeutronL2GatewayConnection
                                           l2gatewayConnection);
}
