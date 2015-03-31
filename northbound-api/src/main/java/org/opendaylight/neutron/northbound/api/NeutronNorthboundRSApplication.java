/*
 * Copyright IBM Corporation, 2013.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.northbound.api;

import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


/**
 * This class is an instance of javax.ws.rs.core.Application and is used to return the classes
 * that will be instantiated for JAXRS processing. This is necessary
 * because package scanning in jersey doesn't yet work in OSGi environment.
 *
 */
public class NeutronNorthboundRSApplication extends Application {
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<Class<?>>();
// northbound URIs
        classes.add(NeutronNetworksNorthbound.class);
        classes.add(NeutronSubnetsNorthbound.class);
        classes.add(NeutronPortsNorthbound.class);
        classes.add(NeutronRoutersNorthbound.class);
        classes.add(NeutronFloatingIPsNorthbound.class);
        classes.add(NeutronSecurityGroupsNorthbound.class);
        classes.add(NeutronSecurityRulesNorthbound.class);
        classes.add(NeutronFirewallNorthbound.class);
        classes.add(NeutronFirewallPolicyNorthbound.class);
        classes.add(NeutronFirewallRulesNorthbound.class);
        classes.add(NeutronLoadBalancerNorthbound.class);
        classes.add(NeutronLoadBalancerListenerNorthbound.class);
        classes.add(NeutronLoadBalancerPoolNorthbound.class);
        classes.add(NeutronLoadBalancerHealthMonitorNorthbound.class);
        classes.add(NeutronLoadBalancerPoolMembersNorthbound.class);
        return classes;
    }
}
