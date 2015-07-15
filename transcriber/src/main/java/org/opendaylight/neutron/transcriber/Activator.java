/*
 * Copyright IBM Corporation and others, 2015.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.transcriber;

import java.util.ArrayList;
import java.util.List;

import org.opendaylight.controller.sal.binding.api.BindingAwareBroker.ProviderContext;
import org.opendaylight.neutron.spi.INeutronFirewallCRUD;
import org.opendaylight.neutron.spi.INeutronFirewallPolicyCRUD;
import org.opendaylight.neutron.spi.INeutronFirewallRuleCRUD;
import org.opendaylight.neutron.spi.INeutronFloatingIPCRUD;
import org.opendaylight.neutron.spi.INeutronLoadBalancerCRUD;
import org.opendaylight.neutron.spi.INeutronLoadBalancerHealthMonitorCRUD;
import org.opendaylight.neutron.spi.INeutronLoadBalancerListenerCRUD;
import org.opendaylight.neutron.spi.INeutronLoadBalancerPoolCRUD;
import org.opendaylight.neutron.spi.INeutronLoadBalancerPoolMemberCRUD;
import org.opendaylight.neutron.spi.INeutronMeteringLabelCRUD;
import org.opendaylight.neutron.spi.INeutronMeteringLabelRuleCRUD;
import org.opendaylight.neutron.spi.INeutronNetworkCRUD;
import org.opendaylight.neutron.spi.INeutronPortCRUD;
import org.opendaylight.neutron.spi.INeutronRouterCRUD;
import org.opendaylight.neutron.spi.INeutronSecurityGroupCRUD;
import org.opendaylight.neutron.spi.INeutronSecurityRuleCRUD;
import org.opendaylight.neutron.spi.INeutronSubnetCRUD;
import org.opendaylight.neutron.spi.INeutronVPNIKEPolicyCRUD;
import org.opendaylight.neutron.spi.INeutronVPNIPSECPolicyCRUD;
import org.opendaylight.neutron.spi.INeutronVPNIPSECSiteConnectionsCRUD;
import org.opendaylight.neutron.spi.INeutronVPNServiceCRUD;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class Activator implements BundleActivator {
    private List<ServiceRegistration<?>> registrations = new ArrayList<ServiceRegistration<?>>();
    private ProviderContext providerContext;

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
        NeutronLoadBalancerPoolMemberInterface.registerNewInterface(context, providerContext, registrations);
        NeutronMeteringLabelInterface.registerNewInterface(context, providerContext, registrations);
        NeutronMeteringLabelRuleInterface.registerNewInterface(context, providerContext, registrations);
        NeutronVPNServiceInterface.registerNewInterface(context, providerContext, registrations);
        NeutronVPNIKEPolicyInterface.registerNewInterface(context, providerContext, registrations);
        NeutronVPNIPSECPolicyInterface.registerNewInterface(context, providerContext, registrations);
        NeutronVPNIPSECSiteConnectionsInterface.registerNewInterface(context, providerContext, registrations);
        NeutronFloatingIPInterface.registerNewInterface(context, providerContext, registrations);
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        for (ServiceRegistration registration : registrations) {
            if (registration != null) {
                registration.unregister();
            }
        }
    }
}
