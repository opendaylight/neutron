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
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.neutron.spi.INeutronBgpvpnCRUD;
import org.opendaylight.neutron.spi.INeutronCRUD;
import org.opendaylight.neutron.spi.INeutronFirewallCRUD;
import org.opendaylight.neutron.spi.INeutronFirewallPolicyCRUD;
import org.opendaylight.neutron.spi.INeutronFirewallRuleCRUD;
import org.opendaylight.neutron.spi.INeutronFloatingIpCRUD;
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
import org.opendaylight.neutron.spi.INeutronTapFlowCRUD;
import org.opendaylight.neutron.spi.INeutronTapServiceCRUD;
import org.opendaylight.neutron.spi.INeutronTrunkCRUD;
import org.opendaylight.neutron.spi.INeutronVpnIkePolicyCRUD;
import org.opendaylight.neutron.spi.INeutronVpnIpSecPolicyCRUD;
import org.opendaylight.neutron.spi.INeutronVpnIpSecSiteConnectionsCRUD;
import org.opendaylight.neutron.spi.INeutronVpnServiceCRUD;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public final class NeutronTranscriberProvider implements NeutronTranscriber {
    private static final Logger LOG = LoggerFactory.getLogger(NeutronTranscriberProvider.class);

    private BundleContext context;
    private final DataBroker db;
    private final List<ServiceRegistration<? extends INeutronCRUD<?>>> registrations = new ArrayList<>();
    private final List<AutoCloseable> neutronInterfaces = new ArrayList<>();

    @Inject
    public NeutronTranscriberProvider(BundleContext context, DataBroker db) {
        LOG.debug("DataBroker set to: {}", db);
        this.context = Preconditions.checkNotNull(context);
        this.db = Preconditions.checkNotNull(db);
    }

    private <S extends INeutronCRUD<?>,
            T extends AutoCloseable /* & S */> void registerCRUDInterface(java.lang.Class<S> clazz, T crudInterface) {
        neutronInterfaces.add(crudInterface);
        @SuppressWarnings("unchecked")
        S castCrudInterface = (S) crudInterface;
        final ServiceRegistration<S> crudInterfaceRegistration =
                context.registerService(clazz, castCrudInterface, null);
        registrations.add(crudInterfaceRegistration);
    }

    @PostConstruct
    public void init() {
        registerCRUDInterface(INeutronBgpvpnCRUD.class, new NeutronBgpvpnInterface(db));
        registerCRUDInterface(INeutronFirewallCRUD.class, new NeutronFirewallInterface(db));
        registerCRUDInterface(INeutronFirewallPolicyCRUD.class, new NeutronFirewallPolicyInterface(db));
        registerCRUDInterface(INeutronFirewallRuleCRUD.class, new NeutronFirewallRuleInterface(db));
        registerCRUDInterface(INeutronFloatingIpCRUD.class, new NeutronFloatingIpInterface(db));
        registerCRUDInterface(INeutronL2gatewayConnectionCRUD.class, new NeutronL2gatewayConnectionInterface(db));
        registerCRUDInterface(INeutronL2gatewayCRUD.class, new NeutronL2gatewayInterface(db));
        registerCRUDInterface(INeutronLoadBalancerHealthMonitorCRUD.class,
                              new NeutronLoadBalancerHealthMonitorInterface(db));
        registerCRUDInterface(INeutronLoadBalancerCRUD.class, new NeutronLoadBalancerInterface(db));
        registerCRUDInterface(INeutronLoadBalancerListenerCRUD.class, new NeutronLoadBalancerListenerInterface(db));
        registerCRUDInterface(INeutronLoadBalancerPoolCRUD.class, new NeutronLoadBalancerPoolInterface(db));
        registerCRUDInterface(INeutronMeteringLabelCRUD.class, new NeutronMeteringLabelInterface(db));
        registerCRUDInterface(INeutronMeteringLabelRuleCRUD.class, new NeutronMeteringLabelRuleInterface(db));
        registerCRUDInterface(INeutronNetworkCRUD.class, new NeutronNetworkInterface(db));
        registerCRUDInterface(INeutronPortCRUD.class, new NeutronPortInterface(db));
        registerCRUDInterface(INeutronQosPolicyCRUD.class, new NeutronQosPolicyInterface(db));
        registerCRUDInterface(INeutronRouterCRUD.class, new NeutronRouterInterface(db));
        registerCRUDInterface(INeutronSFCFlowClassifierCRUD.class, new NeutronSFCFlowClassifierInterface(db));
        registerCRUDInterface(INeutronSFCPortPairCRUD.class, new NeutronSFCPortPairInterface(db));
        registerCRUDInterface(INeutronSFCPortPairGroupCRUD.class, new NeutronSFCPortPairGroupInterface(db));
        registerCRUDInterface(INeutronSFCPortChainCRUD.class, new NeutronSFCPortChainInterface(db));
        registerCRUDInterface(INeutronSecurityGroupCRUD.class, new NeutronSecurityGroupInterface(db));
        registerCRUDInterface(INeutronSecurityRuleCRUD.class, new NeutronSecurityRuleInterface(db));
        registerCRUDInterface(INeutronSubnetCRUD.class, new NeutronSubnetInterface(db));
        registerCRUDInterface(INeutronTapServiceCRUD.class, new NeutronTapServiceInterface(db));
        registerCRUDInterface(INeutronTapFlowCRUD.class, new NeutronTapFlowInterface(db));
        registerCRUDInterface(INeutronTrunkCRUD.class, new NeutronTrunkInterface(db));
        registerCRUDInterface(INeutronVpnIkePolicyCRUD.class, new NeutronVpnIkePolicyInterface(db));
        registerCRUDInterface(INeutronVpnIpSecPolicyCRUD.class, new NeutronVpnIpSecPolicyInterface(db));
        registerCRUDInterface(INeutronVpnIpSecSiteConnectionsCRUD.class,
                              new NeutronVpnIpSecSiteConnectionsInterface(db));
        registerCRUDInterface(INeutronVpnServiceCRUD.class, new NeutronVpnServiceInterface(db));

        // We don't need context any more
        this.context = null;
    }

    @PreDestroy
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
