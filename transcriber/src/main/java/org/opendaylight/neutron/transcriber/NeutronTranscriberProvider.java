/*
 * Copyright (c) 2015 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.transcriber;

import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.List;
import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.neutron.spi.INeutronBgpvpnCRUD;
import org.opendaylight.neutron.spi.INeutronCRUD;
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
import org.opendaylight.neutron.spi.INeutronQosPolicyCRUD;
import org.opendaylight.neutron.spi.INeutronRouterCRUD;
import org.opendaylight.neutron.spi.INeutronSFCFlowClassifierCRUD;
import org.opendaylight.neutron.spi.INeutronSFCPortChainCRUD;
import org.opendaylight.neutron.spi.INeutronSFCPortPairCRUD;
import org.opendaylight.neutron.spi.INeutronSFCPortPairGroupCRUD;
import org.opendaylight.neutron.spi.INeutronSecurityGroupCRUD;
import org.opendaylight.neutron.spi.INeutronSecurityRuleCRUD;
import org.opendaylight.neutron.spi.INeutronSubnetCRUD;
import org.opendaylight.neutron.spi.INeutronVPNIKEPolicyCRUD;
import org.opendaylight.neutron.spi.INeutronVPNIPSECPolicyCRUD;
import org.opendaylight.neutron.spi.INeutronVPNIPSECSiteConnectionsCRUD;
import org.opendaylight.neutron.spi.INeutronVPNServiceCRUD;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NeutronTranscriberProvider implements AutoCloseable, NeutronTranscriber {
    private static final Logger LOGGER = LoggerFactory.getLogger(NeutronTranscriberProvider.class);

    private BundleContext context;
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
    private NeutronVPNIKEPolicyInterface vpnIkePolicyInterface;
    private NeutronVPNIPSECPolicyInterface vpnIpsecPolicyInterface;
    private NeutronVPNIPSECSiteConnectionsInterface vpnIpsecSiteConnectionsInterface;
    private NeutronVPNServiceInterface vpnServiceInterface;
    private NeutronSFCFlowClassifierInterface sfcFlowClassifierInterface;
    private NeutronSFCPortPairInterface sfcPortPairInterface;
    private NeutronSFCPortPairGroupInterface sfcPortPairGroupInterface;
    private NeutronSFCPortChainInterface sfcPortChainInterface;
    private NeutronQosPolicyInterface qosPolicyInterface;

    public NeutronTranscriberProvider(BundleContext context, DataBroker db) {
        LOGGER.debug("DataBroker set to: {}", db);
        this.context = Preconditions.checkNotNull(context);
        this.db = Preconditions.checkNotNull(db);
    }

    private <S extends INeutronCRUD<?>,
            T extends AutoCloseable /* & S */> void registerCRUDInterface(java.lang.Class<S> clazz, T crudInterface) {
        neutronInterfaces.add(crudInterface);
        @SuppressWarnings("unchecked")
        S sCrudInterface = (S) crudInterface;
        final ServiceRegistration<S> crudInterfaceRegistration = context.registerService(clazz, sCrudInterface, null);
        registrations.add(crudInterfaceRegistration);
    }

    public void init() {
        bgpvpnInterface = new NeutronBgpvpnInterface(db);
        registerCRUDInterface(INeutronBgpvpnCRUD.class, bgpvpnInterface);

        firewallInterface = new NeutronFirewallInterface(db);
        registerCRUDInterface(INeutronFirewallCRUD.class, firewallInterface);

        firewallPolicyInterface = new NeutronFirewallPolicyInterface(db);
        registerCRUDInterface(INeutronFirewallPolicyCRUD.class, firewallPolicyInterface);

        firewallRuleInterface = new NeutronFirewallRuleInterface(db);
        registerCRUDInterface(INeutronFirewallRuleCRUD.class, firewallRuleInterface);

        floatingIPInterface = new NeutronFloatingIPInterface(db);
        registerCRUDInterface(INeutronFloatingIPCRUD.class, floatingIPInterface);

        l2gatewayConnectionInterface = new NeutronL2gatewayConnectionInterface(db);
        registerCRUDInterface(INeutronL2gatewayConnectionCRUD.class, l2gatewayConnectionInterface);

        l2gatewayInterface = new NeutronL2gatewayInterface(db);
        registerCRUDInterface(INeutronL2gatewayCRUD.class, l2gatewayInterface);

        loadBalancerHealthMonitorInterface = new NeutronLoadBalancerHealthMonitorInterface(db);
        registerCRUDInterface(INeutronLoadBalancerHealthMonitorCRUD.class, loadBalancerHealthMonitorInterface);

        loadBalancerInterface = new NeutronLoadBalancerInterface(db);
        registerCRUDInterface(INeutronLoadBalancerCRUD.class, loadBalancerInterface);

        loadBalancerListenerInterface = new NeutronLoadBalancerListenerInterface(db);
        registerCRUDInterface(INeutronLoadBalancerListenerCRUD.class, loadBalancerListenerInterface);

        loadBalancerPoolInterface = new NeutronLoadBalancerPoolInterface(db);
        registerCRUDInterface(INeutronLoadBalancerPoolCRUD.class, loadBalancerPoolInterface);

        meteringLabelInterface = new NeutronMeteringLabelInterface(db);
        registerCRUDInterface(INeutronMeteringLabelCRUD.class, meteringLabelInterface);

        meteringLabelRuleInterface = new NeutronMeteringLabelRuleInterface(db);
        registerCRUDInterface(INeutronMeteringLabelRuleCRUD.class, meteringLabelRuleInterface);

        networkInterface = new NeutronNetworkInterface(db);
        registerCRUDInterface(INeutronNetworkCRUD.class, networkInterface);

        portInterface = new NeutronPortInterface(db);
        registerCRUDInterface(INeutronPortCRUD.class, portInterface);

        routerInterface = new NeutronRouterInterface(db);
        registerCRUDInterface(INeutronRouterCRUD.class, routerInterface);

        securityGroupInterface = new NeutronSecurityGroupInterface(db);
        registerCRUDInterface(INeutronSecurityGroupCRUD.class, securityGroupInterface);

        securityRuleInterface = new NeutronSecurityRuleInterface(db);
        registerCRUDInterface(INeutronSecurityRuleCRUD.class, securityRuleInterface);

        subnetInterface = new NeutronSubnetInterface(db);
        registerCRUDInterface(INeutronSubnetCRUD.class, subnetInterface);

        vpnIkePolicyInterface = new NeutronVPNIKEPolicyInterface(db);
        registerCRUDInterface(INeutronVPNIKEPolicyCRUD.class, vpnIkePolicyInterface);

        vpnIpsecPolicyInterface = new NeutronVPNIPSECPolicyInterface(db);
        registerCRUDInterface(INeutronVPNIPSECPolicyCRUD.class, vpnIpsecPolicyInterface);

        vpnIpsecSiteConnectionsInterface = new NeutronVPNIPSECSiteConnectionsInterface(db);
        registerCRUDInterface(INeutronVPNIPSECSiteConnectionsCRUD.class, vpnIpsecSiteConnectionsInterface);

        vpnServiceInterface = new NeutronVPNServiceInterface(db);
        registerCRUDInterface(INeutronVPNServiceCRUD.class, vpnServiceInterface);

        sfcFlowClassifierInterface = new NeutronSFCFlowClassifierInterface(db);
        registerCRUDInterface(INeutronSFCFlowClassifierCRUD.class, sfcFlowClassifierInterface);

        sfcPortPairInterface = new NeutronSFCPortPairInterface(db);
        registerCRUDInterface(INeutronSFCPortPairCRUD.class, sfcPortPairInterface);

        sfcPortPairGroupInterface = new NeutronSFCPortPairGroupInterface(db);
        registerCRUDInterface(INeutronSFCPortPairGroupCRUD.class, sfcPortPairGroupInterface);

        sfcPortChainInterface = new NeutronSFCPortChainInterface(db);
        registerCRUDInterface(INeutronSFCPortChainCRUD.class, sfcPortChainInterface);

        qosPolicyInterface = new NeutronQosPolicyInterface(db);
        registerCRUDInterface(INeutronQosPolicyCRUD.class, qosPolicyInterface);

        // We don't need context any more
        this.context = null;
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
