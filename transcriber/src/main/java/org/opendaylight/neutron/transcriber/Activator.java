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
import org.opendaylight.neutron.spi.INeutronLoadBalancerCRUD;
import org.opendaylight.neutron.spi.INeutronLoadBalancerHealthMonitorCRUD;
import org.opendaylight.neutron.spi.INeutronLoadBalancerListenerCRUD;
import org.opendaylight.neutron.spi.INeutronLoadBalancerPoolCRUD;
import org.opendaylight.neutron.spi.INeutronLoadBalancerPoolMemberCRUD;
import org.opendaylight.neutron.spi.INeutronMeteringLabelCRUD;
import org.opendaylight.neutron.spi.INeutronNetworkCRUD;
import org.opendaylight.neutron.spi.INeutronPortCRUD;
import org.opendaylight.neutron.spi.INeutronRouterCRUD;
import org.opendaylight.neutron.spi.INeutronSecurityGroupCRUD;
import org.opendaylight.neutron.spi.INeutronSecurityRuleCRUD;
import org.opendaylight.neutron.spi.INeutronSubnetCRUD;
import org.opendaylight.neutron.spi.INeutronVPNIPSECSiteConnectionsCRUD;
import org.opendaylight.neutron.spi.INeutronVPNServiceCRUD;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Activator implements BundleActivator {
    protected static final Logger logger = LoggerFactory
    .getLogger(Activator.class);
    private List<ServiceRegistration<?>> registrations = new ArrayList<ServiceRegistration<?>>();
    private ProviderContext providerContext;

    public Activator(ProviderContext providerContext) {
        this.providerContext = providerContext;
    }

    @Override
    public void start(BundleContext context) throws Exception {
        NeutronRouterInterface neutronRouterInterface = new NeutronRouterInterface(providerContext);
        ServiceRegistration<INeutronRouterCRUD> neutronRouterInterfaceRegistration = context.registerService(INeutronRouterCRUD.class, neutronRouterInterface, null);
        if(neutronRouterInterfaceRegistration != null) {
            registrations.add(neutronRouterInterfaceRegistration);
        }
        NeutronPortInterface neutronPortInterface = new NeutronPortInterface(providerContext);
        ServiceRegistration<INeutronPortCRUD> neutronPortInterfaceRegistration = context.registerService(INeutronPortCRUD.class, neutronPortInterface, null);
        if(neutronPortInterfaceRegistration != null) {
            registrations.add(neutronPortInterfaceRegistration);
        }

        NeutronSubnetInterface neutronSubnetInterface = new NeutronSubnetInterface(providerContext);
        ServiceRegistration<INeutronSubnetCRUD> neutronSubnetInterfaceRegistration = context.registerService(INeutronSubnetCRUD.class, neutronSubnetInterface, null);
        if(neutronSubnetInterfaceRegistration != null) {
            registrations.add(neutronSubnetInterfaceRegistration);
        }

        NeutronNetworkInterface neutronNetworkInterface = new NeutronNetworkInterface(providerContext);
        ServiceRegistration<INeutronNetworkCRUD> neutronNetworkInterfaceRegistration = context.registerService(INeutronNetworkCRUD.class, neutronNetworkInterface, null);
        if(neutronNetworkInterfaceRegistration != null) {
            registrations.add(neutronNetworkInterfaceRegistration);
        }

        NeutronSecurityGroupInterface neutronSecurityGroupInterface = new NeutronSecurityGroupInterface(providerContext);
        ServiceRegistration<INeutronSecurityGroupCRUD> neutronSecurityGroupInterfaceRegistration = context.registerService(INeutronSecurityGroupCRUD.class, neutronSecurityGroupInterface, null);
        if(neutronSecurityGroupInterfaceRegistration != null) {
            registrations.add(neutronSecurityGroupInterfaceRegistration);
        }

        NeutronSecurityRuleInterface neutronSecurityRuleInterface = new NeutronSecurityRuleInterface(providerContext);
        ServiceRegistration<INeutronSecurityRuleCRUD> neutronSecurityRuleInterfaceRegistration = context.registerService(INeutronSecurityRuleCRUD.class, neutronSecurityRuleInterface, null);
        if(neutronSecurityRuleInterfaceRegistration != null) {
            registrations.add(neutronSecurityRuleInterfaceRegistration);
        }

        NeutronFirewallInterface neutronFirewallInterface = new NeutronFirewallInterface(providerContext);
        ServiceRegistration<INeutronFirewallCRUD> neutronFirewallInterfaceRegistration = context.registerService(INeutronFirewallCRUD.class, neutronFirewallInterface, null);
        if(neutronFirewallInterfaceRegistration != null) {
            registrations.add(neutronFirewallInterfaceRegistration);
        }

        NeutronFirewallPolicyInterface neutronFirewallPolicyInterface = new NeutronFirewallPolicyInterface(providerContext);
        ServiceRegistration<INeutronFirewallPolicyCRUD> neutronFirewallPolicyInterfaceRegistration = context.registerService(INeutronFirewallPolicyCRUD.class, neutronFirewallPolicyInterface, null);
        if(neutronFirewallPolicyInterfaceRegistration != null) {
            registrations.add(neutronFirewallPolicyInterfaceRegistration);
        }

        NeutronFirewallRuleInterface neutronFirewallRuleInterface = new NeutronFirewallRuleInterface(providerContext);
        ServiceRegistration<INeutronFirewallRuleCRUD> neutronFirewallRuleInterfaceRegistration = context.registerService(INeutronFirewallRuleCRUD.class, neutronFirewallRuleInterface, null);
        if(neutronFirewallRuleInterfaceRegistration != null) {
            registrations.add(neutronFirewallRuleInterfaceRegistration);
        }

        NeutronLoadBalancerInterface neutronLoadBalancerInterface = new NeutronLoadBalancerInterface(providerContext);
        ServiceRegistration<INeutronLoadBalancerCRUD> neutronLoadBalancerInterfaceRegistration = context.registerService(INeutronLoadBalancerCRUD.class, neutronLoadBalancerInterface, null);
        if(neutronLoadBalancerInterfaceRegistration != null) {
            registrations.add(neutronLoadBalancerInterfaceRegistration);
        }

        NeutronLoadBalancerPoolInterface neutronLoadBalancerPoolInterface = new NeutronLoadBalancerPoolInterface(providerContext);
        ServiceRegistration<INeutronLoadBalancerPoolCRUD> neutronLoadBalancerPoolInterfaceRegistration = context.registerService(INeutronLoadBalancerPoolCRUD.class, neutronLoadBalancerPoolInterface, null);
        if(neutronLoadBalancerPoolInterfaceRegistration != null) {
            registrations.add(neutronLoadBalancerPoolInterfaceRegistration);
        }

        NeutronLoadBalancerListenerInterface neutronLoadBalancerListenerInterface = new NeutronLoadBalancerListenerInterface(providerContext);
        ServiceRegistration<INeutronLoadBalancerListenerCRUD> neutronLoadBalancerListenerInterfaceRegistration = context.registerService(INeutronLoadBalancerListenerCRUD.class, neutronLoadBalancerListenerInterface, null);
        if(neutronLoadBalancerListenerInterfaceRegistration != null) {
            registrations.add(neutronLoadBalancerListenerInterfaceRegistration);
        }

        NeutronLoadBalancerHealthMonitorInterface neutronLoadBalancerHealthMonitorInterface = new NeutronLoadBalancerHealthMonitorInterface(providerContext);
        ServiceRegistration<INeutronLoadBalancerHealthMonitorCRUD> neutronLoadBalancerHealthMonitorInterfaceRegistration = context.registerService(INeutronLoadBalancerHealthMonitorCRUD.class, neutronLoadBalancerHealthMonitorInterface, null);
        if(neutronLoadBalancerHealthMonitorInterfaceRegistration != null) {
            registrations.add(neutronLoadBalancerHealthMonitorInterfaceRegistration);
        }

        NeutronLoadBalancerPoolMemberInterface neutronLoadBalancerPoolMemberInterface = new NeutronLoadBalancerPoolMemberInterface(providerContext);
        ServiceRegistration<INeutronLoadBalancerPoolMemberCRUD> neutronLoadBalancerPoolMemberInterfaceRegistration = context.registerService(INeutronLoadBalancerPoolMemberCRUD.class, neutronLoadBalancerPoolMemberInterface, null);
        if(neutronLoadBalancerPoolMemberInterfaceRegistration != null) {
            registrations.add(neutronLoadBalancerPoolMemberInterfaceRegistration);
        }

        NeutronMeteringLabelInterface neutronMeteringLabelInterface = new NeutronMeteringLabelInterface(providerContext);
        ServiceRegistration<INeutronMeteringLabelCRUD> neutronMeteringLabelInterfaceRegistration = context.registerService(INeutronMeteringLabelCRUD.class, neutronMeteringLabelInterface, null);
        if(neutronMeteringLabelInterfaceRegistration != null) {
            registrations.add(neutronMeteringLabelInterfaceRegistration);
        }

        NeutronVPNServiceInterface neutronVPNServiceInterface = new NeutronVPNServiceInterface(providerContext);
        ServiceRegistration<INeutronVPNServiceCRUD> neutronVPNServiceInterfaceRegistration = context.registerService(INeutronVPNServiceCRUD.class, neutronVPNServiceInterface, null);
        if(neutronVPNServiceInterfaceRegistration != null) {
            registrations.add(neutronVPNServiceInterfaceRegistration);
        }

        NeutronVPNIPSECSiteConnectionsInterface neutronVPNIPSECSiteConnectionsInterface = new NeutronVPNIPSECSiteConnectionsInterface(providerContext);
        ServiceRegistration<INeutronVPNIPSECSiteConnectionsCRUD> neutronVPNIPSECSiteConnectionsInterfaceRegistration = context.registerService(INeutronVPNIPSECSiteConnectionsCRUD.class, neutronVPNIPSECSiteConnectionsInterface, null);
        if (neutronVPNIPSECSiteConnectionsInterfaceRegistration != null) {
            registrations.add(neutronVPNIPSECSiteConnectionsInterfaceRegistration);
        }
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
