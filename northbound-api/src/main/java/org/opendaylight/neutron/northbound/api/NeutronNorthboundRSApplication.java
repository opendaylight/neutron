/*
 * Copyright (c) 2013, 2015 IBM Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.northbound.api;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.ws.rs.core.Application;
import org.eclipse.persistence.jaxb.rs.MOXyJsonProvider;

/**
 * This class is an instance of javax.ws.rs.core.Application and is used to return the classes
 * that will be instantiated for JAXRS processing. This is necessary
 * because package scanning in jersey doesn't yet work in OSGi environment.
 *
 */
public final class NeutronNorthboundRSApplication extends Application {
    private static final int HASHMAP_SIZE = 3;

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<>();
        // northbound URIs
        classes.add(NeutronNetworksNorthbound.class);
        classes.add(NeutronSubnetsNorthbound.class);
        classes.add(NeutronPortsNorthbound.class);
        classes.add(NeutronRoutersNorthbound.class);
        classes.add(NeutronFloatingIpsNorthbound.class);
        classes.add(NeutronSecurityGroupsNorthbound.class);
        classes.add(NeutronSecurityRulesNorthbound.class);
        classes.add(NeutronFirewallNorthbound.class);
        classes.add(NeutronFirewallPolicyNorthbound.class);
        classes.add(NeutronFirewallRulesNorthbound.class);
        classes.add(NeutronLoadBalancerNorthbound.class);
        classes.add(NeutronLoadBalancerListenerNorthbound.class);
        classes.add(NeutronLoadBalancerPoolNorthbound.class);
        classes.add(NeutronLoadBalancerHealthMonitorNorthbound.class);
        classes.add(NeutronMeteringLabelsNorthbound.class);
        classes.add(NeutronMeteringLabelRulesNorthbound.class);
        classes.add(NeutronVpnServicesNorthbound.class);
        classes.add(NeutronVpnIkePoliciesNorthbound.class);
        classes.add(NeutronVpnIpSecPoliciesNorthbound.class);
        classes.add(NeutronVpnIpSecSiteConnectionsNorthbound.class);
        classes.add(NeutronBgpvpnsNorthbound.class);
        classes.add(NeutronL2gatewayNorthbound.class);
        classes.add(NeutronL2gatewayConnectionNorthbound.class);
        classes.add(NeutronSFCFlowClassifiersNorthbound.class);
        classes.add(NeutronSFCPortPairsNorthbound.class);
        classes.add(NeutronSFCPortPairGroupsNorthbound.class);
        classes.add(NeutronSFCPortChainsNorthbound.class);
        classes.add(NeutronQosPolicyNorthbound.class);
        classes.add(NeutronTrunksNorthbound.class);
        classes.add(NeutronTapServiceNorthbound.class);
        classes.add(NeutronTapFlowNorthbound.class);

        classes.add(MOXyJsonProvider.class);
        return classes;
    }

    @Override
    public Set<Object> getSingletons() {
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

        Set<Object> set = new HashSet<>(1);
        set.add(moxyJsonProvider);
        return set;
    }
}
