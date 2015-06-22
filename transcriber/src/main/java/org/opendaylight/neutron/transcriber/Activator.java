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
import org.opendaylight.neutron.spi.NeutronRouter;

import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.l3.rev141002.routers.attributes.routers.Router;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class Activator implements BundleActivator {
    private List<ServiceRegistration<?>> registrations = new ArrayList<ServiceRegistration<?>>();
    private ProviderContext providerContext;

    public Activator(ProviderContext providerContext) {
        this.providerContext = providerContext;
    }

    private void addInterface(Class crudClazz, Class<? extends AbstractNeutronInterface> interfaceClazz, BundleContext context) throws Exception {
        AbstractNeutronInterface myInterface = interfaceClazz.newInstance();
        myInterface.setProviderContext(providerContext);
        ServiceRegistration<?> registration = context.registerService(crudClazz, myInterface, null);
        if(registration != null) {
            registrations.add(registration);
        }
    }

    @Override
    public void start(BundleContext context) throws Exception {
        addInterface(INeutronRouterCRUD.class, NeutronRouterInterface.class, context);
        addInterface(INeutronPortCRUD.class, NeutronPortInterface.class, context);
        addInterface(INeutronSubnetCRUD.class, NeutronSubnetInterface.class, context);
        addInterface(INeutronNetworkCRUD.class, NeutronNetworkInterface.class, context);
        addInterface(INeutronSecurityGroupCRUD.class, NeutronSecurityGroupInterface.class, context);
        addInterface(INeutronSecurityRuleCRUD.class, NeutronSecurityRuleInterface.class, context);
        addInterface(INeutronFirewallCRUD.class, NeutronFirewallInterface.class, context);
        addInterface(INeutronFirewallPolicyCRUD.class, NeutronFirewallPolicyInterface.class, context);
        addInterface(INeutronFirewallRuleCRUD.class, NeutronFirewallRuleInterface.class, context);
        addInterface(INeutronLoadBalancerCRUD.class, NeutronLoadBalancerInterface.class, context);
        addInterface(INeutronLoadBalancerPoolCRUD.class, NeutronLoadBalancerPoolInterface.class, context);
        addInterface(INeutronLoadBalancerListenerCRUD.class, NeutronLoadBalancerListenerInterface.class, context);
        addInterface(INeutronLoadBalancerHealthMonitorCRUD.class, NeutronLoadBalancerHealthMonitorInterface.class, context);
        addInterface(INeutronLoadBalancerPoolMemberCRUD.class, NeutronLoadBalancerPoolMemberInterface.class, context);
        addInterface(INeutronMeteringLabelCRUD.class, NeutronMeteringLabelInterface.class, context);
        addInterface(INeutronMeteringLabelRuleCRUD.class, NeutronMeteringLabelRuleInterface.class, context);
        addInterface(INeutronVPNServiceCRUD.class, NeutronVPNServiceInterface.class, context);
        addInterface(INeutronVPNIKEPolicyCRUD.class, NeutronVPNIKEPolicyInterface.class, context);
        addInterface(INeutronVPNIPSECPolicyCRUD.class, NeutronVPNIPSECPolicyInterface.class, context);
        addInterface(INeutronVPNIPSECSiteConnectionsCRUD.class, NeutronVPNIPSECSiteConnectionsInterface.class, context);
        addInterface(INeutronFloatingIPCRUD.class, NeutronFloatingIPInterface.class, context);
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
