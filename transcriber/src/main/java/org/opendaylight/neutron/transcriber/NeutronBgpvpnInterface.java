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
import javax.inject.Inject;
import javax.inject.Singleton;
import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.neutron.spi.INeutronBgpvpnCRUD;
import org.opendaylight.neutron.spi.NeutronBgpvpn;
import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.yang.types.rev130715.Uuid;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.bgpvpns.rev150903.BgpvpnTypeBase;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.bgpvpns.rev150903.BgpvpnTypeL2;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.bgpvpns.rev150903.BgpvpnTypeL3;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.bgpvpns.rev150903.bgpvpns.attributes.Bgpvpns;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.bgpvpns.rev150903.bgpvpns.attributes.bgpvpns.Bgpvpn;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.bgpvpns.rev150903.bgpvpns.attributes.bgpvpns.BgpvpnBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.bgpvpns.rev150903.bgpvpns.attributes.bgpvpns.BgpvpnKey;
import org.ops4j.pax.cdi.api.OsgiServiceProvider;

@Singleton
@OsgiServiceProvider(classes = INeutronBgpvpnCRUD.class)
public final class NeutronBgpvpnInterface extends AbstractNeutronInterface<Bgpvpn, Bgpvpns, BgpvpnKey, NeutronBgpvpn>
        implements INeutronBgpvpnCRUD {

    private static final ImmutableBiMap<Class<? extends BgpvpnTypeBase>,
            String> BGPVPN_TYPE_MAP = new ImmutableBiMap.Builder<Class<? extends BgpvpnTypeBase>, String>()
                    .put(BgpvpnTypeL2.class, "l2").put(BgpvpnTypeL3.class, "l3").build();

    @Inject
    public NeutronBgpvpnInterface(DataBroker db) {
        super(BgpvpnBuilder.class, db);
    }

    @Override
    protected List<Bgpvpn> getDataObjectList(Bgpvpns bgpvpns) {
        return bgpvpns.getBgpvpn();
    }

    @Override
    protected NeutronBgpvpn fromMd(Bgpvpn bgpvpn) {
        final NeutronBgpvpn result = new NeutronBgpvpn();
        fromMdAdminAttributes(bgpvpn, result);
        result.setAutoAggregate(bgpvpn.isAutoAggregate());
        if (bgpvpn.getVni() != null) {
            result.setVni(bgpvpn.getVni());
        }
        if (bgpvpn.getType() != null) {
            result.setType(BGPVPN_TYPE_MAP.get(bgpvpn.getType()));
        }
        if (bgpvpn.getTechnique() != null) {
            result.setTechnique(bgpvpn.getTechnique());
        }
        if (bgpvpn.getRouteTargets() != null) {
            final List<String> routeTargets = new ArrayList<>();
            for (final String routeTarget : bgpvpn.getRouteTargets()) {
                routeTargets.add(routeTarget);
            }
            result.setRouteTargets(routeTargets);
        }
        if (bgpvpn.getImportTargets() != null) {
            final List<String> importTargets = new ArrayList<>();
            for (final String importTarget : bgpvpn.getImportTargets()) {
                importTargets.add(importTarget);
            }
            result.setImportTargets(importTargets);
        }
        if (bgpvpn.getExportTargets() != null) {
            final List<String> exportTargets = new ArrayList<>();
            for (final String exportTarget : bgpvpn.getExportTargets()) {
                exportTargets.add(exportTarget);
            }
            result.setExportTargets(exportTargets);
        }
        if (bgpvpn.getRouteDistinguishers() != null) {
            final List<String> routeDistinguishers = new ArrayList<>();
            for (final String routeDistinguisher : bgpvpn.getRouteDistinguishers()) {
                routeDistinguishers.add(routeDistinguisher);
            }
            result.setRouteDistinguishers(routeDistinguishers);
        }
        if (bgpvpn.getRouters() != null) {
            final List<String> routers = new ArrayList<>();
            for (final Uuid router : bgpvpn.getRouters()) {
                routers.add(router.getValue());
            }
            result.setRouters(routers);
        }
        if (bgpvpn.getNetworks() != null) {
            final List<String> networks = new ArrayList<>();
            for (final Uuid network : bgpvpn.getNetworks()) {
                networks.add(network.getValue());
            }
            result.setNetworks(networks);
        }
        return result;
    }

    @Override
    protected Bgpvpn toMd(NeutronBgpvpn bgpvpn) {
        final BgpvpnBuilder bgpvpnBuilder = new BgpvpnBuilder();

        toMdAdminAttributes(bgpvpn, bgpvpnBuilder);
        if (bgpvpn.getAutoAggregate() != null) {
            bgpvpnBuilder.setAutoAggregate(bgpvpn.getAutoAggregate());
        }
        if (bgpvpn.getVni() != null) {
            bgpvpnBuilder.setVni(bgpvpn.getVni());
        }
        if (bgpvpn.getType() != null) {
            final ImmutableBiMap<String, Class<? extends BgpvpnTypeBase>> mapper = BGPVPN_TYPE_MAP.inverse();
            bgpvpnBuilder.setType(mapper.get(bgpvpn.getType()));
        }
        if (bgpvpn.getTechnique() != null) {
            bgpvpnBuilder.setTechnique(bgpvpn.getTechnique());
        }
        if (bgpvpn.getRouteTargets() != null) {
            final List<String> routeTargets = new ArrayList<>();
            for (final String routeTarget : bgpvpn.getRouteTargets()) {
                routeTargets.add(routeTarget);
            }
            bgpvpnBuilder.setRouteTargets(routeTargets);
        }
        if (bgpvpn.getImportTargets() != null) {
            final List<String> importTargets = new ArrayList<>();
            for (final String importTarget : bgpvpn.getImportTargets()) {
                importTargets.add(importTarget);
            }
            bgpvpnBuilder.setImportTargets(importTargets);
        }
        if (bgpvpn.getExportTargets() != null) {
            final List<String> exportTargets = new ArrayList<>();
            for (final String exportTarget : bgpvpn.getExportTargets()) {
                exportTargets.add(exportTarget);
            }
            bgpvpnBuilder.setExportTargets(exportTargets);
        }
        if (bgpvpn.getRouteDistinguishers() != null) {
            final List<String> routeDistinguishers = new ArrayList<>();
            for (final String routeDistinguisher : bgpvpn.getRouteDistinguishers()) {
                routeDistinguishers.add(routeDistinguisher);
            }
            bgpvpnBuilder.setRouteDistinguishers(routeDistinguishers);
        }
        if (bgpvpn.getRouters() != null) {
            final List<Uuid> routers = new ArrayList<>();
            for (final String router : bgpvpn.getRouters()) {
                routers.add(toUuid(router));
            }
            bgpvpnBuilder.setRouters(routers);
        }
        if (bgpvpn.getNetworks() != null) {
            final List<Uuid> networks = new ArrayList<>();
            for (final String network : bgpvpn.getNetworks()) {
                networks.add(toUuid(network));
            }
            bgpvpnBuilder.setNetworks(networks);
        }
        return bgpvpnBuilder.build();
    }
}
