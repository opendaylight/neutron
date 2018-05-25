/*
 * Copyright (c) 2018 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.neutron.e2etest;

import com.google.inject.AbstractModule;
import org.opendaylight.aaa.web.WebContextSecurer;
import org.opendaylight.aaa.web.WebServer;
import org.opendaylight.aaa.web.jetty.JettyWebServer;
import org.opendaylight.aaa.web.servlet.ServletSupport;
import org.opendaylight.aaa.web.servlet.jersey2.JerseyServletSupport;
import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.controller.md.sal.binding.test.DataBrokerTestModule;
import org.opendaylight.neutron.northbound.api.WebInitializer;
import org.opendaylight.neutron.spi.INeutronBgpvpnCRUD;
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
import org.opendaylight.neutron.transcriber.NeutronBgpvpnInterface;
import org.opendaylight.neutron.transcriber.NeutronFirewallInterface;
import org.opendaylight.neutron.transcriber.NeutronFirewallPolicyInterface;
import org.opendaylight.neutron.transcriber.NeutronFirewallRuleInterface;
import org.opendaylight.neutron.transcriber.NeutronFloatingIpInterface;
import org.opendaylight.neutron.transcriber.NeutronL2gatewayConnectionInterface;
import org.opendaylight.neutron.transcriber.NeutronL2gatewayInterface;
import org.opendaylight.neutron.transcriber.NeutronLoadBalancerHealthMonitorInterface;
import org.opendaylight.neutron.transcriber.NeutronLoadBalancerInterface;
import org.opendaylight.neutron.transcriber.NeutronLoadBalancerListenerInterface;
import org.opendaylight.neutron.transcriber.NeutronLoadBalancerPoolInterface;
import org.opendaylight.neutron.transcriber.NeutronMeteringLabelInterface;
import org.opendaylight.neutron.transcriber.NeutronMeteringLabelRuleInterface;
import org.opendaylight.neutron.transcriber.NeutronNetworkInterface;
import org.opendaylight.neutron.transcriber.NeutronPortInterface;
import org.opendaylight.neutron.transcriber.NeutronQosPolicyInterface;
import org.opendaylight.neutron.transcriber.NeutronRouterInterface;
import org.opendaylight.neutron.transcriber.NeutronSFCFlowClassifierInterface;
import org.opendaylight.neutron.transcriber.NeutronSFCPortChainInterface;
import org.opendaylight.neutron.transcriber.NeutronSFCPortPairGroupInterface;
import org.opendaylight.neutron.transcriber.NeutronSFCPortPairInterface;
import org.opendaylight.neutron.transcriber.NeutronSecurityGroupInterface;
import org.opendaylight.neutron.transcriber.NeutronSecurityRuleInterface;
import org.opendaylight.neutron.transcriber.NeutronSubnetInterface;
import org.opendaylight.neutron.transcriber.NeutronTapFlowInterface;
import org.opendaylight.neutron.transcriber.NeutronTapServiceInterface;
import org.opendaylight.neutron.transcriber.NeutronTrunkInterface;
import org.opendaylight.neutron.transcriber.NeutronVpnIkePolicyInterface;
import org.opendaylight.neutron.transcriber.NeutronVpnIpSecPolicyInterface;
import org.opendaylight.neutron.transcriber.NeutronVpnIpSecSiteConnectionsInterface;
import org.opendaylight.neutron.transcriber.NeutronVpnServiceInterface;

/**
 * Dependency Injection wiring for Neutron.
 *
 * @author Michael Vorburger.ch
 */
public class NeutronTestWiring extends AbstractModule {

    @Override
    protected void configure() {
        bind(WebServer.class).toInstance(new JettyWebServer(9090));
        bind(WebContextSecurer.class).toInstance((webContextBuilder, urlPatterns) -> { }); // NOOP
        bind(ServletSupport.class).toInstance(new JerseyServletSupport());
        bind(WebInitializer.class);

        DataBrokerTestModule dataBrokerTestModule = new DataBrokerTestModule(true);
        DataBroker dataBroker = dataBrokerTestModule.getDataBroker();
        bind(DataBroker.class).toInstance(dataBroker);

        bind(INeutronNetworkCRUD.class).to(NeutronNetworkInterface.class);
        bind(INeutronSubnetCRUD.class).to(NeutronSubnetInterface.class);
        bind(INeutronPortCRUD.class).to(NeutronPortInterface.class);
        bind(INeutronRouterCRUD.class).to(NeutronRouterInterface.class);
        bind(INeutronFloatingIpCRUD.class).to(NeutronFloatingIpInterface.class);
        bind(INeutronSecurityGroupCRUD.class).to(NeutronSecurityGroupInterface.class);
        bind(INeutronSecurityRuleCRUD.class).to(NeutronSecurityRuleInterface.class);
        bind(INeutronFirewallCRUD.class).to(NeutronFirewallInterface.class);
        bind(INeutronFirewallPolicyCRUD.class).to(NeutronFirewallPolicyInterface.class);
        bind(INeutronFirewallRuleCRUD.class).to(NeutronFirewallRuleInterface.class);
        bind(INeutronLoadBalancerCRUD.class).to(NeutronLoadBalancerInterface.class);
        bind(INeutronLoadBalancerListenerCRUD.class).to(NeutronLoadBalancerListenerInterface.class);
        bind(INeutronLoadBalancerPoolCRUD.class).to(NeutronLoadBalancerPoolInterface.class);
        bind(INeutronBgpvpnCRUD.class).to(NeutronBgpvpnInterface.class);
        bind(INeutronL2gatewayCRUD.class).to(NeutronL2gatewayInterface.class);
        bind(INeutronL2gatewayConnectionCRUD.class).to(NeutronL2gatewayConnectionInterface.class);
        bind(INeutronLoadBalancerHealthMonitorCRUD.class).to(NeutronLoadBalancerHealthMonitorInterface.class);
        bind(INeutronMeteringLabelCRUD.class).to(NeutronMeteringLabelInterface.class);
        bind(INeutronMeteringLabelRuleCRUD.class).to(NeutronMeteringLabelRuleInterface.class);
        bind(INeutronVpnServiceCRUD.class).to(NeutronVpnServiceInterface.class);
        bind(INeutronVpnIkePolicyCRUD.class).to(NeutronVpnIkePolicyInterface.class);
        bind(INeutronVpnIpSecPolicyCRUD.class).to(NeutronVpnIpSecPolicyInterface.class);
        bind(INeutronSFCFlowClassifierCRUD.class).to(NeutronSFCFlowClassifierInterface.class);
        bind(INeutronSFCPortChainCRUD.class).to(NeutronSFCPortChainInterface.class);
        bind(INeutronSFCPortPairGroupCRUD.class).to(NeutronSFCPortPairGroupInterface.class);
        bind(INeutronSFCPortPairCRUD.class).to(NeutronSFCPortPairInterface.class);
        bind(INeutronQosPolicyCRUD.class).to(NeutronQosPolicyInterface.class);
        bind(INeutronTrunkCRUD.class).to(NeutronTrunkInterface.class);
        bind(INeutronTapServiceCRUD.class).to(NeutronTapServiceInterface.class);
        bind(INeutronTapFlowCRUD.class).to(NeutronTapFlowInterface.class);
        bind(INeutronVpnIpSecSiteConnectionsCRUD.class).to(NeutronVpnIpSecSiteConnectionsInterface.class);
    }

}
