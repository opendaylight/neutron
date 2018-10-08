/*
 * Copyright (c) 2013, 2015 IBM Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.neutron.northbound.api;

import static java.util.Collections.emptySet;

import com.google.common.collect.ImmutableSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import org.eclipse.persistence.jaxb.rs.MOXyJsonProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is an instance of javax.ws.rs.core.Application and is used to return the classes
 * that will be instantiated for JAXRS processing. This is necessary
 * because package scanning in jersey doesn't yet work in OSGi environment.
 */
@Singleton
public final class NeutronNorthboundRSApplication extends Application {
    private static final int HASHMAP_SIZE = 3;
    private static final Logger LOG = LoggerFactory.getLogger(NeutronNorthboundRSApplication.class);

    private final NeutronNetworksNorthbound neutronNetworksNorthbound;
    private final NeutronSubnetsNorthbound neutronSubnetsNorthbound;
    private final NeutronPortsNorthbound neutronPortsNorthbound;
    private final NeutronRoutersNorthbound neutronRoutersNorthbound;
    private final NeutronFloatingIpsNorthbound neutronFloatingIpsNorthbound;
    private final NeutronSecurityGroupsNorthbound neutronSecurityGroupsNorthbound;
    private final NeutronSecurityRulesNorthbound neutronSecurityRulesNorthbound;
    private final NeutronFirewallNorthbound neutronFirewallNorthbound;
    private final NeutronFirewallPolicyNorthbound neutronFirewallPolicyNorthbound;
    private final NeutronFirewallRulesNorthbound neutronFirewallRulesNorthbound;
    private final NeutronLoadBalancerNorthbound neutronLoadBalancerNorthbound;
    private final NeutronLoadBalancerListenerNorthbound neutronLoadBalancerListenerNorthbound;
    private final NeutronLoadBalancerPoolNorthbound neutronLoadBalancerPoolNorthbound;
    private final NeutronBgpvpnsNorthbound neutronBgpvpnsNorthbound;
    private final NeutronL2gatewayNorthbound neutronL2gatewayNorthbound;
    private final NeutronL2gatewayConnectionNorthbound neutronL2gatewayConnectionNorthbound;
    private final NeutronLoadBalancerHealthMonitorNorthbound neutronLoadBalancerHealthMonitorNorthbound;
    private final NeutronMeteringLabelsNorthbound neutronMeteringLabelsNorthbound;
    private final NeutronMeteringLabelRulesNorthbound neutronMeteringLabelRulesNorthbound;
    private final NeutronVpnServicesNorthbound neutronVpnServicesNorthbound;
    private final NeutronVpnIkePoliciesNorthbound neutronVpnIkePoliciesNorthbound;
    private final NeutronVpnIpSecPoliciesNorthbound neutronVpnIpSecPoliciesNorthbound;
    private final NeutronVpnIpSecSiteConnectionsNorthbound neutronVpnIpSecSiteConnectionsNorthbound;
    private final NeutronSFCFlowClassifiersNorthbound neutronSFCFlowClassifiersNorthbound;
    private final NeutronSFCPortChainsNorthbound neutronSFCPortChainsNorthbound;
    private final NeutronSFCPortPairGroupsNorthbound neutronSFCPortPairGroupsNorthbound;
    private final NeutronSFCPortPairsNorthbound neutronSFCPortPairsNorthbound;
    private final NeutronQosPolicyNorthbound neutronQosPolicyNorthbound;
    private final NeutronTrunksNorthbound neutronTrunksNorthbound;
    private final NeutronTapServiceNorthbound neutronTapServiceNorthbound;
    private final NeutronTapFlowNorthbound neutronTapFlowNorthbound;

    @Inject
    public NeutronNorthboundRSApplication(
            NeutronNetworksNorthbound neutronNetworksNorthbound,
            NeutronSubnetsNorthbound neutronSubnetsNorthbound,
            NeutronPortsNorthbound neutronPortsNorthbound,
            NeutronRoutersNorthbound neutronRoutersNorthbound,
            NeutronFloatingIpsNorthbound neutronFloatingIpsNorthbound,
            NeutronSecurityGroupsNorthbound neutronSecurityGroupsNorthbound,
            NeutronSecurityRulesNorthbound neutronSecurityRulesNorthbound,
            NeutronFirewallNorthbound neutronFirewallNorthbound,
            NeutronFirewallPolicyNorthbound neutronFirewallPolicyNorthbound,
            NeutronFirewallRulesNorthbound neutronFirewallRulesNorthbound,
            NeutronLoadBalancerListenerNorthbound neutronLoadBalancerListenerNorthbound,
            NeutronLoadBalancerNorthbound neutronLoadBalancerNorthbound,
            NeutronLoadBalancerPoolNorthbound neutronLoadBalancerPoolNorthbound,
            NeutronBgpvpnsNorthbound neutronBgpvpnsNorthbound,
            NeutronL2gatewayNorthbound neutronL2gatewayNorthbound,
            NeutronL2gatewayConnectionNorthbound neutronL2gatewayConnectionNorthbound,
            NeutronLoadBalancerHealthMonitorNorthbound neutronLoadBalancerHealthMonitorNorthbound,
            NeutronMeteringLabelsNorthbound neutronMeteringLabelsNorthbound,
            NeutronMeteringLabelRulesNorthbound neutronMeteringLabelRulesNorthbound,
            NeutronVpnServicesNorthbound neutronVpnServicesNorthbound,
            NeutronVpnIkePoliciesNorthbound neutronVpnIkePoliciesNorthbound,
            NeutronVpnIpSecSiteConnectionsNorthbound neutronVpnIpSecSiteConnectionsNorthbound,
            NeutronVpnIpSecPoliciesNorthbound neutronVpnIpSecPoliciesNorthbound,
            NeutronSFCFlowClassifiersNorthbound neutronSFCFlowClassifiersNorthbound,
            NeutronSFCPortChainsNorthbound neutronSFCPortChainsNorthbound,
            NeutronSFCPortPairGroupsNorthbound neutronSFCPortPairGroupsNorthbound,
            NeutronSFCPortPairsNorthbound neutronSFCPortPairsNorthbound,
            NeutronQosPolicyNorthbound neutronQosPolicyNorthbound,
            NeutronTrunksNorthbound neutronTrunksNorthbound,
            NeutronTapServiceNorthbound neutronTapServiceNorthbound,
            NeutronTapFlowNorthbound neutronTapFlowNorthbound) {

        this.neutronNetworksNorthbound = neutronNetworksNorthbound;
        this.neutronSubnetsNorthbound = neutronSubnetsNorthbound;
        this.neutronPortsNorthbound = neutronPortsNorthbound;
        this.neutronRoutersNorthbound = neutronRoutersNorthbound;
        this.neutronFloatingIpsNorthbound = neutronFloatingIpsNorthbound;
        this.neutronSecurityGroupsNorthbound = neutronSecurityGroupsNorthbound;
        this.neutronSecurityRulesNorthbound = neutronSecurityRulesNorthbound;
        this.neutronFirewallNorthbound = neutronFirewallNorthbound;
        this.neutronFirewallRulesNorthbound = neutronFirewallRulesNorthbound;
        this.neutronLoadBalancerListenerNorthbound = neutronLoadBalancerListenerNorthbound;
        this.neutronLoadBalancerNorthbound = neutronLoadBalancerNorthbound;
        this.neutronFirewallPolicyNorthbound = neutronFirewallPolicyNorthbound;
        this.neutronLoadBalancerPoolNorthbound = neutronLoadBalancerPoolNorthbound;
        this.neutronBgpvpnsNorthbound = neutronBgpvpnsNorthbound;
        this.neutronL2gatewayNorthbound = neutronL2gatewayNorthbound;
        this.neutronL2gatewayConnectionNorthbound = neutronL2gatewayConnectionNorthbound;
        this.neutronLoadBalancerHealthMonitorNorthbound = neutronLoadBalancerHealthMonitorNorthbound;
        this.neutronMeteringLabelsNorthbound = neutronMeteringLabelsNorthbound;
        this.neutronMeteringLabelRulesNorthbound = neutronMeteringLabelRulesNorthbound;
        this.neutronVpnServicesNorthbound = neutronVpnServicesNorthbound;
        this.neutronVpnIkePoliciesNorthbound = neutronVpnIkePoliciesNorthbound;
        this.neutronVpnIpSecSiteConnectionsNorthbound = neutronVpnIpSecSiteConnectionsNorthbound;
        this.neutronVpnIpSecPoliciesNorthbound = neutronVpnIpSecPoliciesNorthbound;
        this.neutronSFCFlowClassifiersNorthbound = neutronSFCFlowClassifiersNorthbound;
        this.neutronSFCPortChainsNorthbound = neutronSFCPortChainsNorthbound;
        this.neutronSFCPortPairGroupsNorthbound = neutronSFCPortPairGroupsNorthbound;
        this.neutronSFCPortPairsNorthbound = neutronSFCPortPairsNorthbound;
        this.neutronQosPolicyNorthbound = neutronQosPolicyNorthbound;
        this.neutronTrunksNorthbound = neutronTrunksNorthbound;
        this.neutronTapServiceNorthbound = neutronTapServiceNorthbound;
        this.neutronTapFlowNorthbound = neutronTapFlowNorthbound;
    }

    @Override
    public Set<Class<?>> getClasses() {
        return emptySet();
    }

    @Override
    public Set<Object> getSingletons() {
        return ImmutableSet.builderWithExpectedSize(32)
                .add(getMOXyJsonProvider())
                // Northbound URIs JAX RS Resources:
                .add(neutronNetworksNorthbound)
                .add(neutronSubnetsNorthbound)
                .add(neutronPortsNorthbound)
                .add(neutronRoutersNorthbound)
                .add(neutronFloatingIpsNorthbound)
                .add(neutronSecurityGroupsNorthbound)
                .add(neutronSecurityRulesNorthbound)
                .add(neutronFirewallNorthbound)
                .add(neutronFirewallPolicyNorthbound)
                .add(neutronFirewallRulesNorthbound)
                .add(neutronLoadBalancerNorthbound)
                .add(neutronLoadBalancerListenerNorthbound)
                .add(neutronLoadBalancerPoolNorthbound)
                .add(neutronLoadBalancerHealthMonitorNorthbound)
                .add(neutronMeteringLabelsNorthbound)
                .add(neutronMeteringLabelRulesNorthbound)
                .add(neutronVpnServicesNorthbound)
                .add(neutronVpnIkePoliciesNorthbound)
                .add(neutronVpnIpSecPoliciesNorthbound)
                .add(neutronVpnIpSecSiteConnectionsNorthbound)
                .add(neutronBgpvpnsNorthbound)
                .add(neutronL2gatewayNorthbound)
                .add(neutronL2gatewayConnectionNorthbound)
                .add(neutronSFCFlowClassifiersNorthbound)
                .add(neutronSFCPortPairsNorthbound)
                .add(neutronSFCPortPairGroupsNorthbound)
                .add(neutronSFCPortChainsNorthbound)
                .add(neutronQosPolicyNorthbound)
                .add(neutronTrunksNorthbound)
                .add(neutronTapServiceNorthbound)
                .add(neutronTapFlowNorthbound)
            .add((ExceptionMapper<Exception>) exception -> {
                LOG.error("Error processing response", exception);
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(exception).type(
                    MediaType.TEXT_PLAIN_TYPE).build();
            })
            .add(new MOXyJsonProvider())
                .build();
    }

    private MOXyJsonProvider getMOXyJsonProvider() {
        MOXyJsonProvider moxyJsonProvider = new MOXyJsonProvider();

        moxyJsonProvider.setAttributePrefix("@");
        moxyJsonProvider.setFormattedOutput(true);
        moxyJsonProvider.setIncludeRoot(false);
        moxyJsonProvider.setMarshalEmptyCollections(true);
        moxyJsonProvider.setValueWrapper("$");

        Map<String, String> namespacePrefixMapper = new HashMap<>(HASHMAP_SIZE);
        // FIXME: fill in next two with XSD
        namespacePrefixMapper.put("router", "router");
        namespacePrefixMapper.put("provider", "provider");
        namespacePrefixMapper.put("binding", "binding");
        moxyJsonProvider.setNamespacePrefixMapper(namespacePrefixMapper);
        moxyJsonProvider.setNamespaceSeparator(':');

        return moxyJsonProvider;
    }
}
