/*
 * Copyright (c) 2015 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.transcriber;

import java.util.ArrayList;
import java.util.List;

import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.neutron.spi.INeutronCRUD;
import org.opendaylight.neutron.spi.INeutronBgpvpnCRUD;
import org.opendaylight.neutron.spi.INeutronFirewallCRUD;
import org.opendaylight.neutron.spi.INeutronFirewallPolicyCRUD;
import org.opendaylight.neutron.spi.INeutronFirewallRuleCRUD;
import org.opendaylight.neutron.spi.INeutronFloatingIPCRUD;
import org.opendaylight.neutron.spi.INeutronL2gatewayCRUD;
import org.opendaylight.neutron.spi.INeutronL2gatewayConnectionCRUD;
import org.opendaylight.neutron.spi.INeutronLoadBalancerCRUD;
import org.opendaylight.neutron.spi.INeutronLoadBalancerHealthMonitorCRUD;
import org.opendaylight.neutron.spi.INeutronLoadBalancerListenerCRUD;
import org.opendaylight.neutron.spi.INeutronLoadBalancerPoolCRUD;
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
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class NeutronTranscriberProvider
    implements AutoCloseable, NeutronTranscriber {
    private static final Logger LOGGER = LoggerFactory.getLogger(NeutronTranscriberProvider.class);

    private DataBroker db;
    private final List<ServiceRegistration<? extends INeutronCRUD<?>>> registrations = new ArrayList<>();
    private final List<AutoCloseable> neutronInterfaces = new ArrayList<>();

    private NeutronBgpvpnInterface bgpvpnInterface;
    private NeutronFirewallInterface firewallInterface;
    private NeutronFirewallPolicyInterface firewallPolicyInterface;
    private NeutronFirewallRuleInterface firewallRuleInterface;
    private NeutronFloatingIPInterface floatingIPInterface;
    private NeutronL2gatewayConnectionInterface l2gatewayConnectionInterface;
    private NeutronL2gatewayInterface l2gatewayInterface;
    private NeutronLoadBalancerHealthMonitorInterface loadBalancerHealthMonitorInterface;
    private NeutronLoadBalancerInterface loadBalancerInterface;
    private NeutronLoadBalancerListenerInterface loadBalancerListenerInterface;
    private NeutronLoadBalancerPoolInterface loadBalancerPoolInterface;
    private NeutronMeteringLabelInterface meteringLabelInterface;
    private NeutronMeteringLabelRuleInterface meteringLabelRuleInterface;
    private NeutronNetworkInterface networkInterface;
    private NeutronPortInterface portInterface;
    private NeutronRouterInterface routerInterface;
    private NeutronSecurityGroupInterface securityGroupInterface;
    private NeutronSecurityRuleInterface securityRuleInterface;
    private NeutronSubnetInterface subnetInterface;
    private NeutronVPNIKEPolicyInterface vPNIKEPolicyInterface;
    private NeutronVPNIPSECPolicyInterface vPNIPSECPolicyInterface;
    private NeutronVPNIPSECSiteConnectionsInterface vPNIPSECSiteConnectionsInterface;
    private NeutronVPNServiceInterface vPNServiceInterface;

    public NeutronTranscriberProvider(DataBroker db) {
        LOGGER.debug("DataBroker set to: {}", db);
        this.db = db;
    }

    public void init() {
        BundleContext context = FrameworkUtil.getBundle(this.getClass()).getBundleContext();

        bgpvpnInterface = new NeutronBgpvpnInterface(db);
        neutronInterfaces.add(bgpvpnInterface);
        final ServiceRegistration<INeutronBgpvpnCRUD> bgpvpnInterfaceRegistration =
            context.registerService(INeutronBgpvpnCRUD.class, bgpvpnInterface, null);
        registrations.add(bgpvpnInterfaceRegistration);

        firewallInterface = new NeutronFirewallInterface(db);
        neutronInterfaces.add(firewallInterface);
        final ServiceRegistration<INeutronFirewallCRUD> firewallInterfaceRegistration =
            context.registerService(INeutronFirewallCRUD.class, firewallInterface, null);
        registrations.add(firewallInterfaceRegistration);

        firewallPolicyInterface = new NeutronFirewallPolicyInterface(db);
        neutronInterfaces.add(firewallPolicyInterface);
        final ServiceRegistration<INeutronFirewallPolicyCRUD> firewallPolicyInterfaceRegistration =
            context.registerService(INeutronFirewallPolicyCRUD.class, firewallPolicyInterface, null);
        registrations.add(firewallPolicyInterfaceRegistration);

        firewallRuleInterface = new NeutronFirewallRuleInterface(db);
        neutronInterfaces.add(firewallRuleInterface);
        final ServiceRegistration<INeutronFirewallRuleCRUD> firewallRuleInterfaceRegistration =
            context.registerService(INeutronFirewallRuleCRUD.class, firewallRuleInterface, null);
        registrations.add(firewallRuleInterfaceRegistration);

        floatingIPInterface = new NeutronFloatingIPInterface(db);
        neutronInterfaces.add(floatingIPInterface);
        final ServiceRegistration<INeutronFloatingIPCRUD> floatingIPInterfaceRegistration =
            context.registerService(INeutronFloatingIPCRUD.class, floatingIPInterface, null);
        registrations.add(floatingIPInterfaceRegistration);

        l2gatewayConnectionInterface = new NeutronL2gatewayConnectionInterface(db);
        neutronInterfaces.add(l2gatewayConnectionInterface);
        final ServiceRegistration<INeutronL2gatewayConnectionCRUD> l2gatewayConnectionInterfaceRegistration =
            context.registerService(INeutronL2gatewayConnectionCRUD.class, l2gatewayConnectionInterface, null);
        registrations.add(l2gatewayConnectionInterfaceRegistration);

        l2gatewayInterface = new NeutronL2gatewayInterface(db);
        neutronInterfaces.add(l2gatewayInterface);
        final ServiceRegistration<INeutronL2gatewayCRUD> l2gatewayInterfaceRegistration =
            context.registerService(INeutronL2gatewayCRUD.class, l2gatewayInterface, null);
        registrations.add(l2gatewayInterfaceRegistration);

        loadBalancerHealthMonitorInterface = new NeutronLoadBalancerHealthMonitorInterface(db);
        neutronInterfaces.add(loadBalancerHealthMonitorInterface);
        final ServiceRegistration<INeutronLoadBalancerHealthMonitorCRUD> loadBalancerHealthMonitorInterfaceRegistration =
            context.registerService(INeutronLoadBalancerHealthMonitorCRUD.class, loadBalancerHealthMonitorInterface, null);
        registrations.add(loadBalancerHealthMonitorInterfaceRegistration);

        loadBalancerInterface = new NeutronLoadBalancerInterface(db);
        neutronInterfaces.add(loadBalancerInterface);
        final ServiceRegistration<INeutronLoadBalancerCRUD> loadBalancerInterfaceRegistration =
            context.registerService(INeutronLoadBalancerCRUD.class, loadBalancerInterface, null);
        registrations.add(loadBalancerInterfaceRegistration);

        loadBalancerListenerInterface = new NeutronLoadBalancerListenerInterface(db);
        neutronInterfaces.add(loadBalancerListenerInterface);
        final ServiceRegistration<INeutronLoadBalancerListenerCRUD> loadBalancerListenerInterfaceRegistration =
            context.registerService(INeutronLoadBalancerListenerCRUD.class, loadBalancerListenerInterface, null);
        registrations.add(loadBalancerListenerInterfaceRegistration);

        loadBalancerPoolInterface = new NeutronLoadBalancerPoolInterface(db);
        neutronInterfaces.add(loadBalancerPoolInterface);
        final ServiceRegistration<INeutronLoadBalancerPoolCRUD> loadBalancerPoolInterfaceRegistration =
            context.registerService(INeutronLoadBalancerPoolCRUD.class, loadBalancerPoolInterface, null);
        registrations.add(loadBalancerPoolInterfaceRegistration);

        meteringLabelInterface = new NeutronMeteringLabelInterface(db);
        neutronInterfaces.add(meteringLabelInterface);
        final ServiceRegistration<INeutronMeteringLabelCRUD> meteringLabelInterfaceRegistration =
            context.registerService(INeutronMeteringLabelCRUD.class, meteringLabelInterface, null);
        registrations.add(meteringLabelInterfaceRegistration);

        meteringLabelRuleInterface = new NeutronMeteringLabelRuleInterface(db);
        neutronInterfaces.add(meteringLabelRuleInterface);
        final ServiceRegistration<INeutronMeteringLabelRuleCRUD> meteringLabelRuleInterfaceRegistration =
            context.registerService(INeutronMeteringLabelRuleCRUD.class, meteringLabelRuleInterface, null);
        registrations.add(meteringLabelRuleInterfaceRegistration);

        networkInterface = new NeutronNetworkInterface(db);
        neutronInterfaces.add(networkInterface);
        final ServiceRegistration<INeutronNetworkCRUD> networkInterfaceRegistration =
            context.registerService(INeutronNetworkCRUD.class, networkInterface, null);
        registrations.add(networkInterfaceRegistration);

        portInterface = new NeutronPortInterface(db);
        neutronInterfaces.add(portInterface);
        final ServiceRegistration<INeutronPortCRUD> portInterfaceRegistration =
            context.registerService(INeutronPortCRUD.class, portInterface, null);
        registrations.add(portInterfaceRegistration);

        routerInterface = new NeutronRouterInterface(db);
        neutronInterfaces.add(routerInterface);
        final ServiceRegistration<INeutronRouterCRUD> routerInterfaceRegistration =
            context.registerService(INeutronRouterCRUD.class, routerInterface, null);
        registrations.add(routerInterfaceRegistration);

        securityGroupInterface = new NeutronSecurityGroupInterface(db);
        neutronInterfaces.add(securityGroupInterface);
        final ServiceRegistration<INeutronSecurityGroupCRUD> securityGroupInterfaceRegistration =
            context.registerService(INeutronSecurityGroupCRUD.class, securityGroupInterface, null);
        registrations.add(securityGroupInterfaceRegistration);

        securityRuleInterface = new NeutronSecurityRuleInterface(db);
        neutronInterfaces.add(securityRuleInterface);
        final ServiceRegistration<INeutronSecurityRuleCRUD> securityRuleInterfaceRegistration =
            context.registerService(INeutronSecurityRuleCRUD.class, securityRuleInterface, null);
        registrations.add(securityRuleInterfaceRegistration);

        subnetInterface = new NeutronSubnetInterface(db);
        neutronInterfaces.add(subnetInterface);
        final ServiceRegistration<INeutronSubnetCRUD> subnetInterfaceRegistration =
            context.registerService(INeutronSubnetCRUD.class, subnetInterface, null);
        registrations.add(subnetInterfaceRegistration);

        vPNIKEPolicyInterface = new NeutronVPNIKEPolicyInterface(db);
        neutronInterfaces.add(vPNIKEPolicyInterface);
        final ServiceRegistration<INeutronVPNIKEPolicyCRUD> vPNIKEPolicyInterfaceRegistration =
            context.registerService(INeutronVPNIKEPolicyCRUD.class, vPNIKEPolicyInterface, null);
        registrations.add(vPNIKEPolicyInterfaceRegistration);

        vPNIPSECPolicyInterface = new NeutronVPNIPSECPolicyInterface(db);
        neutronInterfaces.add(vPNIPSECPolicyInterface);
        final ServiceRegistration<INeutronVPNIPSECPolicyCRUD> vPNIPSECPolicyInterfaceRegistration =
            context.registerService(INeutronVPNIPSECPolicyCRUD.class, vPNIPSECPolicyInterface, null);
        registrations.add(vPNIPSECPolicyInterfaceRegistration);

        vPNIPSECSiteConnectionsInterface = new NeutronVPNIPSECSiteConnectionsInterface(db);
        neutronInterfaces.add(vPNIPSECSiteConnectionsInterface);
        final ServiceRegistration<INeutronVPNIPSECSiteConnectionsCRUD> vPNIPSECSiteConnectionsInterfaceRegistration =
            context.registerService(INeutronVPNIPSECSiteConnectionsCRUD.class, vPNIPSECSiteConnectionsInterface, null);
        registrations.add(vPNIPSECSiteConnectionsInterfaceRegistration);

        vPNServiceInterface = new NeutronVPNServiceInterface(db);
        neutronInterfaces.add(vPNServiceInterface);
        final ServiceRegistration<INeutronVPNServiceCRUD> vPNServiceInterfaceRegistration =
            context.registerService(INeutronVPNServiceCRUD.class, vPNServiceInterface, null);
        registrations.add(vPNServiceInterfaceRegistration);
    }

    @Override
    public void close() throws Exception {
        for (final ServiceRegistration registration : registrations) {
            registration.unregister();
        }
        for (final AutoCloseable neutronCRUD : neutronInterfaces) {
            neutronCRUD.close();
        }
        neutronInterfaces.clear();
    }
}
