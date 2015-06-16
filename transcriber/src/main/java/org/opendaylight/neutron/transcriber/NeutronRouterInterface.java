/*
 * Copyright IBM Corporation, 2013.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.transcriber;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.opendaylight.controller.sal.binding.api.BindingAwareBroker.ProviderContext;
import org.opendaylight.neutron.spi.INeutronRouterCRUD;
import org.opendaylight.neutron.spi.NeutronRouter;
import org.opendaylight.neutron.spi.NeutronRouter_Interface;
import org.opendaylight.neutron.spi.NeutronRouter_NetworkReference;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.l3.rev141002.routers.attributes.Routers;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.l3.rev141002.routers.attributes.routers.Router;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.l3.rev141002.routers.attributes.routers.RouterBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.l3.rev141002.routers.attributes.routers.router.ExternalGatewayInfo;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.l3.rev141002.routers.attributes.routers.router.ExternalGatewayInfoBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.l3.rev141002.routers.attributes.routers.router.Interfaces;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.l3.rev141002.routers.attributes.routers.router.external_gateway_info.ExternalFixedIps;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.rev150325.Neutron;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NeutronRouterInterface extends  AbstractNeutronInterface<Router, NeutronRouter> implements INeutronRouterCRUD {
    private static final Logger LOGGER = LoggerFactory.getLogger(NeutronRouterInterface.class);
    private ConcurrentMap<String, NeutronRouter> routerDB  = new ConcurrentHashMap<String, NeutronRouter>();
    // methods needed for creating caches


    NeutronRouterInterface(ProviderContext providerContext) {
        super(providerContext);
    }

    // this method uses reflection to update an object from it's delta.

    private boolean overwrite(Object target, Object delta) {
        Method[] methods = target.getClass().getMethods();

        for(Method toMethod: methods){
            if(toMethod.getDeclaringClass().equals(target.getClass())
                    && toMethod.getName().startsWith("set")){

                String toName = toMethod.getName();
                String fromName = toName.replace("set", "get");

                try {
                    Method fromMethod = delta.getClass().getMethod(fromName);
                    Object value = fromMethod.invoke(delta, (Object[])null);
                    if(value != null){
                        toMethod.invoke(target, value);
                    }
                } catch (Exception e) {
                    LOGGER.error(e.getMessage());
                    return false;
                }
            }
        }
        return true;
    }


    // IfNBRouterCRUD Interface methods

    @Override
    public boolean routerExists(String uuid) {
        return routerDB.containsKey(uuid);
    }

    @Override
    public NeutronRouter getRouter(String uuid) {
        if (!routerExists(uuid)) {
            return null;
        }
        return routerDB.get(uuid);
    }

    @Override
    public List<NeutronRouter> getAllRouters() {
        Set<NeutronRouter> allRouters = new HashSet<NeutronRouter>();
        for (Entry<String, NeutronRouter> entry : routerDB.entrySet()) {
            NeutronRouter router = entry.getValue();
            allRouters.add(router);
        }
        LOGGER.debug("Exiting getAllRouters, Found {} Routers", allRouters.size());
        List<NeutronRouter> ans = new ArrayList<NeutronRouter>();
        ans.addAll(allRouters);
        return ans;
    }

    @Override
    public boolean addRouter(NeutronRouter input) {
        if (routerExists(input.getID())) {
            return false;
        }
        routerDB.putIfAbsent(input.getID(), input);
        addMd(input);
        return true;
    }

    @Override
    public boolean removeRouter(String uuid) {
        if (!routerExists(uuid)) {
            return false;
        }
        routerDB.remove(uuid);
        removeMd(toMd(uuid));
        return true;
    }

    @Override
    public boolean updateRouter(String uuid, NeutronRouter delta) {
        if (!routerExists(uuid)) {
            return false;
        }
        NeutronRouter target = routerDB.get(uuid);
        boolean rc = overwrite(target, delta);
        if (rc) {
            updateMd(routerDB.get(uuid));
        }
        return rc;
    }

    @Override
    public boolean routerInUse(String routerUUID) {
        if (!routerExists(routerUUID)) {
            return true;
        }
        NeutronRouter target = routerDB.get(routerUUID);
        return (target.getInterfaces().size() > 0);
    }

    @Override
    protected Router toMd(NeutronRouter router) {

        RouterBuilder routerBuilder = new RouterBuilder();

        if (router.getRouterUUID() != null) {
            routerBuilder.setUuid(toUuid(router.getRouterUUID()));
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
        routerBuilder.setDistribted(router.getDistributed());
        if (router.getRoutes() != null) {
            List<String> routes = new ArrayList<String>();
            for (String route : router.getRoutes()) {
                routes.add(route);
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
                List<ExternalFixedIps> externalFixedIps = new ArrayList<ExternalFixedIps>();
                for (int i = 0; i < externalFixedIps.size(); i++) {
                    externalFixedIps.add((ExternalFixedIps) externalGatewayInfos.getExternalFixedIPs().get(i));
                }
                builder.setExternalFixedIps(externalFixedIps);
                externalGatewayInfo = builder.build();
            }
            routerBuilder.setExternalGatewayInfo(externalGatewayInfo);
        }
        if (router.getInterfaces() != null) {
            HashMap<String, NeutronRouter_Interface> mapInterfaces = new HashMap<String, NeutronRouter_Interface>();
            List<Interfaces> interfaces = new ArrayList<Interfaces>();
            for (Entry<String, NeutronRouter_Interface> entry : mapInterfaces.entrySet()) {
                interfaces.add((Interfaces) entry.getValue());
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
        return InstanceIdentifier.create(Neutron.class).child(Routers.class).child(Router.class, router.getKey());
    }

    @Override
    protected Router toMd(String uuid) {
        RouterBuilder routerBuilder = new RouterBuilder();
        routerBuilder.setUuid(toUuid(uuid));
        return routerBuilder.build();
    }
}
