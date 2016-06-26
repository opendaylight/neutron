/*
 * Copyright (c) 2015 IBM Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.transcriber;

import java.util.ArrayList;
import java.util.List;

import org.opendaylight.controller.sal.binding.api.BindingAwareBroker.ProviderContext;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class Activator implements BundleActivator {
    private final List<ServiceRegistration<?>> registrations = new ArrayList<ServiceRegistration<?>>();
    private final ProviderContext providerContext;

    public Activator(ProviderContext providerContext) {
        this.providerContext = providerContext;
    }

    @Override
    public void start(BundleContext context) throws Exception {
        NeutronRouterInterface.registerNewInterface(context, providerContext, registrations);
        NeutronPortInterface.registerNewInterface(context, providerContext, registrations);
        NeutronSubnetInterface.registerNewInterface(context, providerContext, registrations);
        NeutronNetworkInterface.registerNewInterface(context, providerContext, registrations);
        NeutronSecurityGroupInterface.registerNewInterface(context, providerContext, registrations);
        NeutronSecurityRuleInterface.registerNewInterface(context, providerContext, registrations);
        NeutronFirewallInterface.registerNewInterface(context, providerContext, registrations);
        NeutronFirewallPolicyInterface.registerNewInterface(context, providerContext, registrations);
        NeutronFirewallRuleInterface.registerNewInterface(context, providerContext, registrations);
        NeutronLoadBalancerInterface.registerNewInterface(context, providerContext, registrations);
        NeutronLoadBalancerPoolInterface.registerNewInterface(context, providerContext, registrations);
        NeutronLoadBalancerListenerInterface.registerNewInterface(context, providerContext, registrations);
        NeutronLoadBalancerHealthMonitorInterface.registerNewInterface(context, providerContext, registrations);
        NeutronMeteringLabelInterface.registerNewInterface(context, providerContext, registrations);
        NeutronMeteringLabelRuleInterface.registerNewInterface(context, providerContext, registrations);
        NeutronVPNServiceInterface.registerNewInterface(context, providerContext, registrations);
        NeutronVPNIKEPolicyInterface.registerNewInterface(context, providerContext, registrations);
        NeutronVPNIPSECPolicyInterface.registerNewInterface(context, providerContext, registrations);
        NeutronVPNIPSECSiteConnectionsInterface.registerNewInterface(context, providerContext, registrations);
        NeutronFloatingIPInterface.registerNewInterface(context, providerContext, registrations);
        NeutronBgpvpnInterface.registerNewInterface(context, providerContext, registrations);
        NeutronL2gatewayInterface.registerNewInterface(context, providerContext, registrations);
        NeutronL2gatewayConnectionInterface.registerNewInterface(context, providerContext, registrations);
        NeutronSFCFlowClassifierInterface.registerNewInterface(context, providerContext, registrations);
        NeutronSFCPortPairInterface.registerNewInterface(context, providerContext, registrations);
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        for (final ServiceRegistration registration : registrations) {
            if (registration != null) {
                registration.unregister();
            }
        }
    }
}
