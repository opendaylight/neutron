/*
 * Copyright (c) 2015 Ericsson India Global Services Pvt Ltd. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.transcriber;

import com.google.common.collect.ImmutableBiMap;

import java.util.ArrayList;
import java.util.List;

import org.opendaylight.controller.sal.binding.api.BindingAwareBroker.ProviderContext;
import org.opendaylight.neutron.spi.INeutronBgpvpnCRUD;
import org.opendaylight.neutron.spi.NeutronBgpvpn;
import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.yang.types.rev130715.Uuid;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.bgpvpns.rev150903.BgpvpnTypeBase;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.bgpvpns.rev150903.BgpvpnTypeL2;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.bgpvpns.rev150903.BgpvpnTypeL3;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.bgpvpns.rev150903.bgpvpns.attributes.Bgpvpns;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.bgpvpns.rev150903.bgpvpns.attributes.bgpvpns.Bgpvpn;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.bgpvpns.rev150903.bgpvpns.attributes.bgpvpns.BgpvpnBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.rev150712.Neutron;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NeutronBgpvpnInterface extends AbstractNeutronInterface<Bgpvpn, Bgpvpns, NeutronBgpvpn> implements INeutronBgpvpnCRUD {
    private static final Logger LOGGER = LoggerFactory.getLogger(NeutronBgpvpnInterface.class);

    private static final ImmutableBiMap<Class<? extends BgpvpnTypeBase>,String> BGPVPN_TYPE_MAP
            = new ImmutableBiMap.Builder<Class<? extends BgpvpnTypeBase>,String>()
            .put(BgpvpnTypeL2.class,"l2")
            .put(BgpvpnTypeL3.class,"l3")
            .build();

    NeutronBgpvpnInterface(ProviderContext providerContext) {
        super(providerContext);
    }

    @Override
    protected List<Bgpvpn> getDataObjectList(Bgpvpns bgpvpns) {
        return bgpvpns.getBgpvpn();
    }

    @Override
    public boolean inUse(String uuid) {
        return !exists(uuid);
    }

    @Override
    protected NeutronBgpvpn fromMd(Bgpvpn bgpvpn) {
        final NeutronBgpvpn result = new NeutronBgpvpn();
        result.setAdminStateUp(bgpvpn.isAdminStateUp());
        result.setBgpvpnName(bgpvpn.getName());
        result.setAutoAggregate(bgpvpn.isAutoAggregate());
        result.setStatus(bgpvpn.getStatus());
        result.setTenantID(bgpvpn.getTenantId());
        result.setID(bgpvpn.getUuid().getValue());
        if(bgpvpn.getVnid() != null) {
            result.setVnid(bgpvpn.getVnid());
        }
        if(bgpvpn.getType() != null) {
            result.setType(BGPVPN_TYPE_MAP.get(bgpvpn.getType()));
        }
        if(bgpvpn.getTechnique() != null) {
            result.setTechnique(bgpvpn.getTechnique());
        }
        if (bgpvpn.getRouteTargets() != null) {
            final List<String> routeTargets = new ArrayList<String>();
            for( final String routeTarget : bgpvpn.getRouteTargets()) {
                routeTargets.add(routeTarget);
            }
            result.setRouteTargets(routeTargets);
        }
        if (bgpvpn.getImportTargets() != null) {
            final List<String> importTargets = new ArrayList<String>();
            for( final String importTarget : bgpvpn.getImportTargets()) {
                importTargets.add(importTarget);
            }
            result.setImportTargets(importTargets);
        }
        if (bgpvpn.getExportTargets() != null) {
            final List<String> exportTargets = new ArrayList<String>();
            for( final String exportTarget : bgpvpn.getExportTargets()) {
                exportTargets.add(exportTarget);
            }
            result.setExportTargets(exportTargets);
        }
        if (bgpvpn.getRouteDistinguishers() != null) {
            final List<String> routeDistinguishers = new ArrayList<String>();
            for( final String routeDistinguisher : bgpvpn.getRouteDistinguishers()) {
                routeDistinguishers.add(routeDistinguisher);
            }
            result.setRouteDistinguishers(routeDistinguishers);
        }
        if (bgpvpn.getRouters() != null) {
            final List<String> routers = new ArrayList<String>();
            for( final Uuid router : bgpvpn.getRouters()) {
               routers.add(router.getValue());
            }
            result.setRouters(routers);
        }
        if (bgpvpn.getNetworks() != null) {
            final List<String> networks = new ArrayList<String>();
            for( final Uuid network : bgpvpn.getNetworks()) {
               networks.add(network.getValue());
            }
            result.setNetworks(networks);
        }
        return result;
    }

    @Override
    protected Bgpvpn toMd(NeutronBgpvpn bgpvpn) {
        final BgpvpnBuilder bgpvpnBuilder = new BgpvpnBuilder();

        bgpvpnBuilder.setAdminStateUp(bgpvpn.getAdminStateUp());
        if (bgpvpn.getBgpvpnName() != null) {
            bgpvpnBuilder.setName(bgpvpn.getBgpvpnName());
        }
        if (bgpvpn.getAutoAggregate() != null) {
            bgpvpnBuilder.setAutoAggregate(bgpvpn.getAutoAggregate());
        }
        if (bgpvpn.getStatus() != null) {
            bgpvpnBuilder.setStatus(bgpvpn.getStatus());
        }
        if (bgpvpn.getVnid() != null) {
            bgpvpnBuilder.setVnid(bgpvpn.getVnid());
        }
        if (bgpvpn.getType() != null) {
            final ImmutableBiMap<String, Class<? extends BgpvpnTypeBase>> mapper = BGPVPN_TYPE_MAP.inverse();
            bgpvpnBuilder.setType(mapper.get(bgpvpn.getType()));
        }
        if (bgpvpn.getTechnique() != null) {
            bgpvpnBuilder.setTechnique(bgpvpn.getTechnique());
        }
        if (bgpvpn.getRouteTargets() != null) {
            final List<String> routeTargets = new ArrayList<String>();
            for( final String routeTarget : bgpvpn.getRouteTargets()) {
                routeTargets.add(routeTarget);
            }
            bgpvpnBuilder.setRouteTargets(routeTargets);
        }
        if (bgpvpn.getImportTargets() != null) {
            final List<String> importTargets = new ArrayList<String>();
            for( final String importTarget : bgpvpn.getImportTargets()) {
                importTargets.add(importTarget);
            }
            bgpvpnBuilder.setImportTargets(importTargets);
        }
        if (bgpvpn.getExportTargets() != null) {
            final List<String> exportTargets = new ArrayList<String>();
            for( final String exportTarget : bgpvpn.getExportTargets()) {
                exportTargets.add(exportTarget);
            }
            bgpvpnBuilder.setExportTargets(exportTargets);
        }
        if (bgpvpn.getRouteDistinguishers() != null) {
            final List<String> routeDistinguishers = new ArrayList<String>();
            for( final String routeDistinguisher : bgpvpn.getRouteDistinguishers()) {
                routeDistinguishers.add(routeDistinguisher);
            }
            bgpvpnBuilder.setRouteDistinguishers(routeDistinguishers);
        }
        if (bgpvpn.getRouters() != null) {
            final List<Uuid> routers = new ArrayList<Uuid>();
            for( final String router : bgpvpn.getRouters()) {
                routers.add(toUuid(router));
            }
            bgpvpnBuilder.setRouters(routers);
        }
        if (bgpvpn.getNetworks() != null) {
            final List<Uuid> networks = new ArrayList<Uuid>();
            for( final String network : bgpvpn.getNetworks()) {
                networks.add(toUuid(network));
            }
            bgpvpnBuilder.setNetworks(networks);
        }
        if (bgpvpn.getTenantID() != null) {
            bgpvpnBuilder.setTenantId(toUuid(bgpvpn.getTenantID()));
        }
        if (bgpvpn.getID() != null) {
            bgpvpnBuilder.setUuid(toUuid(bgpvpn.getID()));
        } else {
            LOGGER.warn("Attempting to write neutron bgpvpn without UUID");
        }
        return bgpvpnBuilder.build();
    }

    @Override
    protected Bgpvpn toMd(String uuid) {
        final BgpvpnBuilder bgpvpnBuilder = new BgpvpnBuilder();
        bgpvpnBuilder.setUuid(toUuid(uuid));
        return bgpvpnBuilder.build();
    }

    @Override
    protected InstanceIdentifier<Bgpvpn> createInstanceIdentifier(Bgpvpn bgpvpn) {
        return InstanceIdentifier.create(Neutron.class)
                .child(Bgpvpns.class)
                .child(Bgpvpn.class, bgpvpn.getKey());
    }

    @Override
    protected InstanceIdentifier<Bgpvpns> createInstanceIdentifier() {
        return InstanceIdentifier.create(Neutron.class)
                .child(Bgpvpns.class);
    }

    public static void registerNewInterface(BundleContext context,
                                            ProviderContext providerContext,
                                            List<ServiceRegistration<?>> registrations) {
        final NeutronBgpvpnInterface neutronNetworkInterface = new NeutronBgpvpnInterface(providerContext);
        final ServiceRegistration<INeutronBgpvpnCRUD> neutronNetworkInterfaceRegistration = context.registerService(INeutronBgpvpnCRUD.class, neutronNetworkInterface, null);
        if(neutronNetworkInterfaceRegistration != null) {
            registrations.add(neutronNetworkInterfaceRegistration);
        }
    }
}
