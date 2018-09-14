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
import javax.inject.Inject;
import javax.inject.Singleton;
import org.apache.aries.blueprint.annotation.service.Service;
import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.neutron.spi.INeutronRouterCRUD;
import org.opendaylight.neutron.spi.NeutronIps;
import org.opendaylight.neutron.spi.NeutronRoute;
import org.opendaylight.neutron.spi.NeutronRouter;
import org.opendaylight.neutron.spi.NeutronRouterNetworkReference;
import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.inet.types.rev130715.IpAddressBuilder;
import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.inet.types.rev130715.IpPrefixBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.l3.rev150712.l3.attributes.Routes;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.l3.rev150712.l3.attributes.RoutesBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.l3.rev150712.routers.attributes.Routers;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.l3.rev150712.routers.attributes.routers.Router;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.l3.rev150712.routers.attributes.routers.RouterBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.l3.rev150712.routers.attributes.routers.RouterKey;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.l3.rev150712.routers.attributes.routers.router.ExternalGatewayInfo;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.l3.rev150712.routers.attributes.routers.router.ExternalGatewayInfoBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.l3.rev150712.routers.attributes.routers.router.external_gateway_info.ExternalFixedIps;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.l3.rev150712.routers.attributes.routers.router.external_gateway_info.ExternalFixedIpsBuilder;

@Singleton
@Service(classes = INeutronRouterCRUD.class)
public final class NeutronRouterInterface extends AbstractNeutronInterface<Router, Routers, RouterKey, NeutronRouter>
        implements INeutronRouterCRUD {
    // methods needed for creating caches

    @Inject
    public NeutronRouterInterface(DataBroker db) {
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
        toMdAdminAttributes(router, routerBuilder);
        if (router.getGatewayPortId() != null && !router.getGatewayPortId().isEmpty()) {
            routerBuilder.setGatewayPortId(toUuid(router.getGatewayPortId()));
        }
        routerBuilder.setDistributed(router.getDistributed());
        if (router.getRoutes() != null) {
            final List<Routes> routes = new ArrayList<>();
            for (final NeutronRoute route : router.getRoutes()) {
                final RoutesBuilder routeBuilder = new RoutesBuilder();
                routeBuilder.setDestination(IpPrefixBuilder.getDefaultInstance(route.getDestination()));
                routeBuilder.setNexthop(IpAddressBuilder.getDefaultInstance(route.getNextHop()));
                routes.add(routeBuilder.build());
            }
            routerBuilder.setRoutes(routes);
        }
        if (router.getExternalGatewayInfo() != null) {
            ExternalGatewayInfo externalGatewayInfo = null;
            final List<NeutronRouterNetworkReference> neutronRouterNetworkReferences = new ArrayList<>();
            neutronRouterNetworkReferences.add(router.getExternalGatewayInfo());
            for (final NeutronRouterNetworkReference externalGatewayInfos : neutronRouterNetworkReferences) {
                final ExternalGatewayInfoBuilder builder = new ExternalGatewayInfoBuilder();
                builder.setEnableSnat(externalGatewayInfos.getEnableSNAT());
                builder.setExternalNetworkId(toUuid(externalGatewayInfos.getNetworkID()));
                if (externalGatewayInfos.getExternalFixedIps() != null) {
                    final List<ExternalFixedIps> externalFixedIps = new ArrayList<>();
                    for (final NeutronIps externalIp : externalGatewayInfos.getExternalFixedIps()) {
                        final ExternalFixedIpsBuilder eFixedIpBuilder = new ExternalFixedIpsBuilder();
                        eFixedIpBuilder.setIpAddress(IpAddressBuilder.getDefaultInstance(externalIp.getIpAddress()));
                        eFixedIpBuilder.setSubnetId(toUuid(externalIp.getSubnetUUID()));
                        externalFixedIps.add(eFixedIpBuilder.build());
                    }
                    builder.setExternalFixedIps(externalFixedIps);
                }
                externalGatewayInfo = builder.build();
            }
            routerBuilder.setExternalGatewayInfo(externalGatewayInfo);
        }
        return routerBuilder.build();
    }

    @Override
    public NeutronRouter fromMd(Router router) {
        final NeutronRouter result = new NeutronRouter();
        fromMdAdminAttributes(router, result);
        result.setDistributed(router.isDistributed());
        if (router.getGatewayPortId() != null) {
            result.setGatewayPortId(String.valueOf(router.getGatewayPortId().getValue()));
        }
        if (router.getRoutes() != null) {
            final List<NeutronRoute> routes = new ArrayList<>();
            for (final Routes route : router.getRoutes()) {
                final NeutronRoute routerRoute = new NeutronRoute();
                routerRoute.setDestination(route.getDestination().stringValue());
                routerRoute.setNextHop(route.getNexthop().stringValue());
                routes.add(routerRoute);
            }
            result.setRoutes(routes);
        }

        if (router.getExternalGatewayInfo() != null) {
            final NeutronRouterNetworkReference extGwInfo = new NeutronRouterNetworkReference();
            extGwInfo.setNetworkID(String.valueOf(router.getExternalGatewayInfo().getExternalNetworkId().getValue()));
            extGwInfo.setEnableSNAT(router.getExternalGatewayInfo().isEnableSnat());
            if (router.getExternalGatewayInfo().getExternalFixedIps() != null) {
                final List<NeutronIps> fixedIps = new ArrayList<>();
                for (final ExternalFixedIps mdFixedIp : router.getExternalGatewayInfo().getExternalFixedIps()) {
                    final NeutronIps fixedIp = new NeutronIps();
                    fixedIp.setSubnetUUID(String.valueOf(mdFixedIp.getSubnetId().getValue()));
                    fixedIp.setIpAddress(mdFixedIp.getIpAddress().stringValue());
                    fixedIps.add(fixedIp);
                }
                extGwInfo.setExternalFixedIps(fixedIps);
            }
            result.setExternalGatewayInfo(extGwInfo);
        }

        return result;
    }
}
