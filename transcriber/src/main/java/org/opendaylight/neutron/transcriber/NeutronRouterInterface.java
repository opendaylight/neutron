/*
 * Copyright (c) 2013, 2015 IBM Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.transcriber;

import java.util.ArrayList;
import java.util.List;
import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.neutron.spi.INeutronRouterCRUD;
import org.opendaylight.neutron.spi.NeutronRoute;
import org.opendaylight.neutron.spi.NeutronRouter;
import org.opendaylight.neutron.spi.NeutronRouter_NetworkReference;
import org.opendaylight.neutron.spi.Neutron_IPs;
import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.inet.types.rev130715.IpAddress;
import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.inet.types.rev130715.IpPrefix;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.l3.rev150712.l3.attributes.Routes;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.l3.rev150712.l3.attributes.RoutesBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.l3.rev150712.routers.attributes.Routers;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.l3.rev150712.routers.attributes.routers.Router;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.l3.rev150712.routers.attributes.routers.RouterBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.l3.rev150712.routers.attributes.routers.router.ExternalGatewayInfo;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.l3.rev150712.routers.attributes.routers.router.ExternalGatewayInfoBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.l3.rev150712.routers.attributes.routers.router.external_gateway_info.ExternalFixedIps;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.l3.rev150712.routers.attributes.routers.router.external_gateway_info.ExternalFixedIpsBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.rev150712.Neutron;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class NeutronRouterInterface extends AbstractNeutronInterface<Router, Routers, NeutronRouter>
        implements INeutronRouterCRUD {
    private static final Logger LOGGER = LoggerFactory.getLogger(NeutronRouterInterface.class);
    // methods needed for creating caches

    NeutronRouterInterface(DataBroker db) {
        super(RouterBuilder.class, db);
    }

    // IfNBRouterCRUD Interface methods
    @Override
    protected List<Router> getDataObjectList(Routers routers) {
        return routers.getRouter();
    }

    @Override
    protected Router toMd(NeutronRouter router) {

        final RouterBuilder routerBuilder = new RouterBuilder();

        if (router.getID() != null) {
            routerBuilder.setUuid(toUuid(router.getID()));
        }
        if (router.getName() != null) {
            routerBuilder.setName(router.getName());
        }
        if (router.getTenantID() != null && !router.getTenantID().isEmpty()) {
            routerBuilder.setTenantId(toUuid(router.getTenantID()));
        }
        if (router.getStatus() != null) {
            routerBuilder.setStatus(router.getStatus());
        }
        if (router.getGatewayPortId() != null && !router.getGatewayPortId().isEmpty()) {
            routerBuilder.setGatewayPortId(toUuid(router.getGatewayPortId()));
        }
        routerBuilder.setAdminStateUp(router.getAdminStateUp());
        routerBuilder.setDistributed(router.getDistributed());
        if (router.getRoutes() != null) {
            final List<Routes> routes = new ArrayList<Routes>();
            for (final NeutronRoute route : router.getRoutes()) {
                final RoutesBuilder routeBuilder = new RoutesBuilder();
                routeBuilder.setDestination(new IpPrefix(route.getDestination().toCharArray()));
                routeBuilder.setNexthop(new IpAddress(route.getNextHop().toCharArray()));
                routes.add(routeBuilder.build());
            }
            routerBuilder.setRoutes(routes);
        }
        if (router.getExternalGatewayInfo() != null) {
            ExternalGatewayInfo externalGatewayInfo = null;
            final List<NeutronRouter_NetworkReference> neutronRouter_NetworkReferences = new ArrayList<
                    NeutronRouter_NetworkReference>();
            neutronRouter_NetworkReferences.add(router.getExternalGatewayInfo());
            for (final NeutronRouter_NetworkReference externalGatewayInfos : neutronRouter_NetworkReferences) {
                final ExternalGatewayInfoBuilder builder = new ExternalGatewayInfoBuilder();
                builder.setEnableSnat(externalGatewayInfos.getEnableSNAT());
                builder.setExternalNetworkId(toUuid(externalGatewayInfos.getNetworkID()));
                if (externalGatewayInfos.getExternalFixedIPs() != null) {
                    final List<ExternalFixedIps> externalFixedIps = new ArrayList<ExternalFixedIps>();
                    for (final Neutron_IPs eIP : externalGatewayInfos.getExternalFixedIPs()) {
                        final ExternalFixedIpsBuilder eFixedIpBuilder = new ExternalFixedIpsBuilder();
                        eFixedIpBuilder.setIpAddress(new IpAddress(eIP.getIpAddress().toCharArray()));
                        eFixedIpBuilder.setSubnetId(toUuid(eIP.getSubnetUUID()));
                        externalFixedIps.add(eFixedIpBuilder.build());
                    }
                    builder.setExternalFixedIps(externalFixedIps);
                }
                externalGatewayInfo = builder.build();
            }
            routerBuilder.setExternalGatewayInfo(externalGatewayInfo);
        }
        if (router.getID() != null) {
            routerBuilder.setUuid(toUuid(router.getID()));
        } else {
            LOGGER.warn("Attempting to write neutron router without UUID");
        }
        return routerBuilder.build();
    }

    @Override
    protected InstanceIdentifier<Router> createInstanceIdentifier(Router router) {
        return InstanceIdentifier.create(Neutron.class).child(Routers.class).child(Router.class, router.getKey());
    }

    @Override
    protected InstanceIdentifier<Routers> createInstanceIdentifier() {
        return InstanceIdentifier.create(Neutron.class).child(Routers.class);
    }

    public NeutronRouter fromMd(Router router) {
        final NeutronRouter result = new NeutronRouter();
        result.setID(router.getUuid().getValue());
        result.setName(router.getName());
        result.setTenantID(router.getTenantId());
        result.setAdminStateUp(router.isAdminStateUp());
        result.setStatus(router.getStatus());
        result.setDistributed(router.isDistributed());
        if (router.getGatewayPortId() != null) {
            result.setGatewayPortId(String.valueOf(router.getGatewayPortId().getValue()));
        }
        if (router.getRoutes() != null) {
            final List<NeutronRoute> routes = new ArrayList<NeutronRoute>();
            for (final Routes route : router.getRoutes()) {
                final NeutronRoute routerRoute = new NeutronRoute();
                routerRoute.setDestination(String.valueOf(route.getDestination().getValue()));
                routerRoute.setNextHop(String.valueOf(route.getNexthop().getValue()));
                routes.add(routerRoute);
            }
            result.setRoutes(routes);
        }

        if (router.getExternalGatewayInfo() != null) {
            final NeutronRouter_NetworkReference extGwInfo = new NeutronRouter_NetworkReference();
            extGwInfo.setNetworkID(String.valueOf(router.getExternalGatewayInfo().getExternalNetworkId().getValue()));
            extGwInfo.setEnableSNAT(router.getExternalGatewayInfo().isEnableSnat());
            if (router.getExternalGatewayInfo().getExternalFixedIps() != null) {
                final List<Neutron_IPs> fixedIPs = new ArrayList<Neutron_IPs>();
                for (final ExternalFixedIps mdFixedIP : router.getExternalGatewayInfo().getExternalFixedIps()) {
                    final Neutron_IPs fixedIP = new Neutron_IPs();
                    fixedIP.setSubnetUUID(String.valueOf(mdFixedIP.getSubnetId().getValue()));
                    fixedIP.setIpAddress(String.valueOf(mdFixedIP.getIpAddress().getValue()));
                    fixedIPs.add(fixedIP);
                }
                extGwInfo.setExternalFixedIPs(fixedIPs);
            }
            result.setExternalGatewayInfo(extGwInfo);
        }

        return result;
    }
}
