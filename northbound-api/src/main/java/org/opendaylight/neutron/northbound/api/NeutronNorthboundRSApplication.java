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
import org.eclipse.persistence.jaxb.rs.MOXyJsonProvider;

/**
 * This class is an instance of javax.ws.rs.core.Application and is used to return the classes
 * that will be instantiated for JAXRS processing. This is necessary
 * because package scanning in jersey doesn't yet work in OSGi environment.
 */
@Singleton
public final class NeutronNorthboundRSApplication extends Application {
    private static final int HASHMAP_SIZE = 3;

    private final NeutronNetworksNorthbound neutronNetworksNorthbound;

    @Inject
    public NeutronNorthboundRSApplication(NeutronNetworksNorthbound neutronNetworksNorthbound) {
        this.neutronNetworksNorthbound = neutronNetworksNorthbound;
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
                .add(new NeutronSubnetsNorthbound())
                .add(new NeutronPortsNorthbound())
                .add(new NeutronRoutersNorthbound())
                .add(new NeutronFloatingIpsNorthbound())
                .add(new NeutronSecurityGroupsNorthbound())
                .add(new NeutronSecurityRulesNorthbound())
                .add(new NeutronFirewallNorthbound())
                .add(new NeutronFirewallPolicyNorthbound())
                .add(new NeutronFirewallRulesNorthbound())
                .add(new NeutronLoadBalancerNorthbound())
                .add(new NeutronLoadBalancerListenerNorthbound())
                .add(new NeutronLoadBalancerPoolNorthbound())
                .add(new NeutronLoadBalancerHealthMonitorNorthbound())
                .add(new NeutronMeteringLabelsNorthbound())
                .add(new NeutronMeteringLabelRulesNorthbound())
                .add(new NeutronVpnServicesNorthbound())
                .add(new NeutronVpnIkePoliciesNorthbound())
                .add(new NeutronVpnIpSecPoliciesNorthbound())
                .add(new NeutronVpnIpSecSiteConnectionsNorthbound())
                .add(new NeutronBgpvpnsNorthbound())
                .add(new NeutronL2gatewayNorthbound())
                .add(new NeutronL2gatewayConnectionNorthbound())
                .add(new NeutronSFCFlowClassifiersNorthbound())
                .add(new NeutronSFCPortPairsNorthbound())
                .add(new NeutronSFCPortPairGroupsNorthbound())
                .add(new NeutronSFCPortChainsNorthbound())
                .add(new NeutronQosPolicyNorthbound())
                .add(new NeutronTrunksNorthbound())
                .add(new NeutronTapServiceNorthbound())
                .add(new NeutronTapFlowNorthbound())
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
