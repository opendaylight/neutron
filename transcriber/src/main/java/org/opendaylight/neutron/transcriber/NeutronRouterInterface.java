/*
 * Copyright (c) 2013, 2015 IBM Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.transcriber;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.opendaylight.controller.sal.binding.api.BindingAwareBroker.ProviderContext;
import org.opendaylight.neutron.spi.INeutronRouterCRUD;
import org.opendaylight.neutron.spi.NeutronRoute;
import org.opendaylight.neutron.spi.Neutron_IPs;
import org.opendaylight.neutron.spi.NeutronRouter;
import org.opendaylight.neutron.spi.NeutronRouter_Interface;
import org.opendaylight.neutron.spi.NeutronRouter_NetworkReference;
import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.inet.types.rev100924.IpAddress;
import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.inet.types.rev100924.IpPrefix;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.l3.rev150712.l3.attributes.Routes;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.l3.rev150712.l3.attributes.RoutesBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.l3.rev150712.routers.attributes.Routers;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.l3.rev150712.routers.attributes.routers.Router;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.l3.rev150712.routers.attributes.routers.RouterBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.l3.rev150712.routers.attributes.routers.router.ExternalGatewayInfo;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.l3.rev150712.routers.attributes.routers.router.ExternalGatewayInfoBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.l3.rev150712.routers.attributes.routers.router.InterfacesBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.l3.rev150712.routers.attributes.routers.router.external_gateway_info.ExternalFixedIpsBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.l3.rev150712.routers.attributes.routers.router.Interfaces;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.l3.rev150712.routers.attributes.routers.router.external_gateway_info.ExternalFixedIps;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.rev150712.Neutron;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NeutronRouterInterface extends AbstractNeutronInterface<Router, Routers, NeutronRouter> implements INeutronRouterCRUD {
    private static final Logger LOGGER = LoggerFactory.getLogger(NeutronRouterInterface.class);
    // methods needed for creating caches


    NeutronRouterInterface(ProviderContext providerContext) {
        super(providerContext);
    }


    // IfNBRouterCRUD Interface methods

    @Override
    public boolean routerExists(String uuid) {
        return exists(uuid);
    }

    @Override
    public NeutronRouter getRouter(String uuid) {
        return get(uuid);
    }

    @Override
    protected List<Router> getDataObjectList(Routers routers) {
        return routers.getRouter();
    }

    @Override
    public List<NeutronRouter> getAllRouters() {
        return getAll();
    }

    @Override
    public boolean addRouter(NeutronRouter input) {
        return add(input);
    }

    @Override
    public boolean removeRouter(String uuid) {
        return remove(uuid);
    }

    @Override
    public boolean updateRouter(String uuid, NeutronRouter delta) {
        return update(uuid, delta);
    }

    @Override
    public boolean routerInUse(String routerUUID) {
        if (!exists(routerUUID)) {
            return true;
        }
        NeutronRouter target = getRouter(routerUUID);
        return (target.getInterfaces().size() > 0);
    }

    @Override
    protected Router toMd(NeutronRouter router) {

        RouterBuilder routerBuilder = new RouterBuilder();

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
            List<Routes> routes = new ArrayList<Routes>();
            for (NeutronRoute route : router.getRoutes()) {
                RoutesBuilder routeBuilder = new RoutesBuilder();
                routeBuilder.setDestination(new IpPrefix(route.getDestination().toCharArray()));
                routeBuilder.setNexthop(new IpAddress(route.getNextHop().toCharArray()));
                routes.add(routeBuilder.build());
            }
            routerBuilder.setRoutes(routes);
        }
        if (router.getExternalGatewayInfo() != null) {
            ExternalGatewayInfo externalGatewayInfo = null;
            List<NeutronRouter_NetworkReference> neutronRouter_NetworkReferences = new ArrayList<NeutronRouter_NetworkReference>();
            neutronRouter_NetworkReferences.add(router.getExternalGatewayInfo());
            for (NeutronRouter_NetworkReference externalGatewayInfos : neutronRouter_NetworkReferences) {
                ExternalGatewayInfoBuilder builder = new ExternalGatewayInfoBuilder();
                builder.setEnableSnat(externalGatewayInfos.getEnableSNAT());
                builder.setExternalNetworkId(toUuid(externalGatewayInfos.getNetworkID()));
                if (externalGatewayInfos.getExternalFixedIPs() != null) {
                    List<ExternalFixedIps> externalFixedIps = new ArrayList<ExternalFixedIps>();
                    for (Neutron_IPs eIP : externalGatewayInfos.getExternalFixedIPs()) {
                        ExternalFixedIpsBuilder eFixedIpBuilder = new ExternalFixedIpsBuilder();
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
        if (router.getInterfaces() != null) {
            Map<String, NeutronRouter_Interface> mapInterfaces = new HashMap<>(router.getInterfaces());
            List<Interfaces> interfaces = new ArrayList<Interfaces>();
            for (Entry<String, NeutronRouter_Interface> entry : mapInterfaces.entrySet()) {
                InterfacesBuilder ifBuilder = new InterfacesBuilder();
                ifBuilder.setPortId(toUuid(entry.getValue().getPortUUID()));
                ifBuilder.setSubnetId(toUuid(entry.getValue().getSubnetUUID()));
                ifBuilder.setUuid(toUuid(entry.getValue().getID()));
                ifBuilder.setTenantId(toUuid(entry.getValue().getTenantID()));
                interfaces.add(ifBuilder.build());
            }
            routerBuilder.setInterfaces(interfaces);
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
        return InstanceIdentifier.create(Neutron.class)
                 .child(Routers.class)
                 .child(Router.class, router.getKey());
    }

    @Override
    protected InstanceIdentifier<Routers> createInstanceIdentifier() {
        return InstanceIdentifier.create(Neutron.class).child(Routers.class);
    }

    @Override
    protected Router toMd(String uuid) {
        RouterBuilder routerBuilder = new RouterBuilder();
        routerBuilder.setUuid(toUuid(uuid));
        return routerBuilder.build();
    }

    public static void registerNewInterface(BundleContext context,
                                            ProviderContext providerContext,
                                            List<ServiceRegistration<?>> registrations) {
        NeutronRouterInterface neutronRouterInterface = new NeutronRouterInterface(providerContext);
        ServiceRegistration<INeutronRouterCRUD> neutronRouterInterfaceRegistration = context.registerService(INeutronRouterCRUD.class, neutronRouterInterface, null);
        if(neutronRouterInterfaceRegistration != null) {
            registrations.add(neutronRouterInterfaceRegistration);
        }
    }

    public NeutronRouter fromMd(Router router) {
        NeutronRouter result = new NeutronRouter();
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
            List<NeutronRoute> routes = new ArrayList<NeutronRoute>();
            for (Routes route : router.getRoutes()) {
                NeutronRoute routerRoute = new NeutronRoute();
                routerRoute.setDestination(String.valueOf(route.getDestination().getValue()));
                routerRoute.setNextHop(String.valueOf(route.getNexthop().getValue()));
                routes.add(routerRoute);
            }
            result.setRoutes(routes);
        }

        if (router.getExternalGatewayInfo() != null) {
            NeutronRouter_NetworkReference extGwInfo = new NeutronRouter_NetworkReference();
            extGwInfo.setNetworkID(String.valueOf(router.getExternalGatewayInfo().getExternalNetworkId().getValue()));
            extGwInfo.setEnableSNAT(router.getExternalGatewayInfo().isEnableSnat());
            if (router.getExternalGatewayInfo().getExternalFixedIps() != null) {
                List<Neutron_IPs> fixedIPs = new ArrayList<Neutron_IPs>();
                for (ExternalFixedIps mdFixedIP : router.getExternalGatewayInfo().getExternalFixedIps()) {
                     Neutron_IPs fixedIP = new Neutron_IPs();
                     fixedIP.setSubnetUUID(String.valueOf(mdFixedIP.getSubnetId().getValue()));
                     fixedIP.setIpAddress(String.valueOf(mdFixedIP.getIpAddress().getValue()));
                     fixedIPs.add(fixedIP);
                }
                extGwInfo.setExternalFixedIPs(fixedIPs);
            }
            result.setExternalGatewayInfo(extGwInfo);
        }

        if (router.getInterfaces() != null) {
            Map<String, NeutronRouter_Interface> interfaces = new HashMap<String, NeutronRouter_Interface>();
            for (Interfaces mdInterface : router.getInterfaces()) {
                NeutronRouter_Interface pojoInterface = new NeutronRouter_Interface();
                String id = mdInterface.getUuid().getValue();
                pojoInterface.setID(id);
                pojoInterface.setTenantID(mdInterface.getTenantId());
                pojoInterface.setSubnetUUID(String.valueOf(mdInterface.getSubnetId().getValue()));
                pojoInterface.setPortUUID(String.valueOf(mdInterface.getPortId().getValue()));
                interfaces.put(mdInterface.getPortId().getValue(), pojoInterface);
            }
            result.setInterfaces(interfaces);
        }
        return result;
    }
}
