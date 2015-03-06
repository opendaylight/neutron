/*
 * Copyright IBM Corporation, 2015.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.dummyprovider;

import java.util.Hashtable;
import java.util.Dictionary;

import org.apache.felix.dm.DependencyActivatorBase;
import org.apache.felix.dm.DependencyManager;

import org.opendaylight.neutron.spi.INeutronFirewallAware;
import org.opendaylight.neutron.spi.INeutronFirewallPolicyAware;
import org.opendaylight.neutron.spi.INeutronFirewallRuleAware;
import org.opendaylight.neutron.spi.INeutronFloatingIPAware;
import org.opendaylight.neutron.spi.INeutronLoadBalancerAware;
import org.opendaylight.neutron.spi.INeutronLoadBalancerHealthMonitorAware;
import org.opendaylight.neutron.spi.INeutronLoadBalancerListenerAware;
import org.opendaylight.neutron.spi.INeutronLoadBalancerPoolAware;
import org.opendaylight.neutron.spi.INeutronLoadBalancerPoolMemberAware;
import org.opendaylight.neutron.spi.INeutronNetworkAware;
import org.opendaylight.neutron.spi.INeutronPortAware;
import org.opendaylight.neutron.spi.INeutronRouterAware;
import org.opendaylight.neutron.spi.INeutronSecurityGroupAware;
import org.opendaylight.neutron.spi.INeutronSecurityRuleAware;
import org.opendaylight.neutron.spi.INeutronSubnetAware;

import org.osgi.framework.BundleContext;

public class Activator extends DependencyActivatorBase {

    @Override
    public void init(BundleContext context, DependencyManager manager) throws Exception {
       manager.add(createComponent().setInterface(new String[] {
           INeutronFirewallAware.class.getName(),
           INeutronFirewallPolicyAware.class.getName(),
           INeutronFirewallRuleAware.class.getName(),
           INeutronFloatingIPAware.class.getName(),
           INeutronLoadBalancerAware.class.getName(),
           INeutronLoadBalancerHealthMonitorAware.class.getName(),
           INeutronLoadBalancerListenerAware.class.getName(),
           INeutronLoadBalancerPoolAware.class.getName(),
           INeutronLoadBalancerPoolMemberAware.class.getName(),
           INeutronNetworkAware.class.getName(),
           INeutronPortAware.class.getName(),
           INeutronRouterAware.class.getName(),
           INeutronSecurityGroupAware.class.getName(),
           INeutronSecurityRuleAware.class.getName(),
           INeutronSubnetAware.class.getName() }, null));
    }

    /**
     * Function called when the activator stops just before the
     * cleanup done by ComponentActivatorAbstractBase
     *
     */
    @Override
    public void destroy(BundleContext context, DependencyManager manager) throws Exception {
    }

}
