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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.networks.rev141002.NetworkTypeBase;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.rev150325.Neutron;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NeutronBgpvpnInterface extends AbstractNeutronInterface<Bgpvpn,NeutronBgpvpn> implements INeutronBgpvpnCRUD {
    private static final Logger LOGGER = LoggerFactory.getLogger(NeutronBgpvpnInterface.class);

    private static final ImmutableBiMap<Class<? extends BgpvpnTypeBase>,String> BGPVPN_TYPE_MAP
            = new ImmutableBiMap.Builder<Class<? extends BgpvpnTypeBase>,String>()
            .put(BgpvpnTypeL2.class,"l2")
            .put(BgpvpnTypeL3.class,"l3")
            .build();

    NeutronBgpvpnInterface(ProviderContext providerContext) {
        super(providerContext);
    }

    // IfNBNetworkCRUD methods

    @Override
    public boolean bgpvpnExists(String uuid) {
        Bgpvpn bgpvpn = readMd(createInstanceIdentifier(toMd(uuid)));
        if (bgpvpn == null) {
            return false;
        }
        return true;
    }

    @Override
    public NeutronBgpvpn getBgpvpn(String uuid) {
        Bgpvpn bgpvpn = readMd(createInstanceIdentifier(toMd(uuid)));
        if (bgpvpn == null) {
            return null;
        }
        return fromMd(bgpvpn);
    }

    @Override
    public List<NeutronBgpvpn> getAllBgpvpns() {
        Set<NeutronBgpvpn> allBgpvpns = new HashSet<NeutronBgpvpn>();
        Bgpvpns bgpvpns = readMd(createInstanceIdentifier());
        if (bgpvpns != null) {
            for (Bgpvpn bgpvpn: bgpvpns.getBgpvpn()) {
                allBgpvpns.add(fromMd(bgpvpn));
            }
        }
        LOGGER.debug("Exiting getAllBgpvpns, Found {} OpenStackBgpvpns", allBgpvpns.size());
        List<NeutronBgpvpn> ans = new ArrayList<NeutronBgpvpn>();
        ans.addAll(allBgpvpns);
        return ans;
    }

    @Override
    public boolean addBgpvpn(NeutronBgpvpn input) {
        if (bgpvpnExists(input.getID())) {
            return false;
        }
        addMd(input);
        return true;
    }

    @Override
    public boolean removeBgpvpn(String uuid) {
        if (!bgpvpnExists(uuid)) {
            return false;
        }
        return removeMd(toMd(uuid));
    }

    @Override
    public boolean updateBgpvpn(String uuid, NeutronBgpvpn delta) {
        if (!bgpvpnExists(uuid)) {
            return false;
        }
/* note: because what we get is *not* a delta but (at this point) the updated
 * object, this is much simpler - just replace the value and update the mdsal
 * with it */
        updateMd(delta);
        return true;
    }

    @Override
    public boolean bgpvpnInUse(String netUUID) {
        if (!bgpvpnExists(netUUID)) {
            return true;
        }
        return false;
    }

    protected NeutronBgpvpn fromMd(Bgpvpn bgpvpn) {
        NeutronBgpvpn result = new NeutronBgpvpn();
        result.setAdminStateUp(bgpvpn.isAdminStateUp());
        result.setBgpvpnName(bgpvpn.getName());
        result.setAutoAggregate(bgpvpn.isAutoAggregate());
        result.setStatus(bgpvpn.getStatus());
        result.setTenantID(bgpvpn.getTenantId().getValue().replace("-", ""));
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
            List<String> routeTargets = new ArrayList<String>();
            for( String routeTarget : bgpvpn.getRouteTargets()) {
                routeTargets.add(routeTarget);
            }
            result.setRouteTargets(routeTargets);
        }
        if (bgpvpn.getImportTargets() != null) {
            List<String> importTargets = new ArrayList<String>();
            for( String importTarget : bgpvpn.getImportTargets()) {
                importTargets.add(importTarget);
            }
            result.setImportTargets(importTargets);
        }
        if (bgpvpn.getExportTargets() != null) {
            List<String> exportTargets = new ArrayList<String>();
            for( String exportTarget : bgpvpn.getExportTargets()) {
                exportTargets.add(exportTarget);
            }
            result.setExportTargets(exportTargets);
        }
        if (bgpvpn.getRouteDistinguishers() != null) {
            List<String> routeDistinguishers = new ArrayList<String>();
            for( String routeDistinguisher : bgpvpn.getRouteDistinguishers()) {
                routeDistinguishers.add(routeDistinguisher);
            }
            result.setRouteDistinguishers(routeDistinguishers);
        }
        if (bgpvpn.getRouters() != null) {
            List<String> routers = new ArrayList<String>();
            for( Uuid router : bgpvpn.getRouters()) {
               routers.add(router.getValue());
            }
            result.setRouters(routers);
        }
        if (bgpvpn.getNetworks() != null) {
            List<String> networks = new ArrayList<String>();
            for( Uuid network : bgpvpn.getNetworks()) {
               networks.add(network.getValue());
            }
            result.setNetworks(networks);
        }
        return result;
    }

    protected Bgpvpn toMd(NeutronBgpvpn bgpvpn) {
        BgpvpnBuilder bgpvpnBuilder = new BgpvpnBuilder();

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
            ImmutableBiMap<String, Class<? extends BgpvpnTypeBase>> mapper = BGPVPN_TYPE_MAP.inverse();
            bgpvpnBuilder.setType(mapper.get(bgpvpn.getType()));
        }
        if (bgpvpn.getTechnique() != null) {
            bgpvpnBuilder.setTechnique(bgpvpn.getTechnique());
        }
        if (bgpvpn.getRouteTargets() != null) {
            List<String> routeTargets = new ArrayList<String>();
            for( String routeTarget : bgpvpn.getRouteTargets()) {
                routeTargets.add(routeTarget);
            }
            bgpvpnBuilder.setRouteTargets(routeTargets);
        }
        if (bgpvpn.getImportTargets() != null) {
            List<String> importTargets = new ArrayList<String>();
            for( String importTarget : bgpvpn.getImportTargets()) {
                importTargets.add(importTarget);
            }
            bgpvpnBuilder.setImportTargets(importTargets);
        }
        if (bgpvpn.getExportTargets() != null) {
            List<String> exportTargets = new ArrayList<String>();
            for( String exportTarget : bgpvpn.getExportTargets()) {
                exportTargets.add(exportTarget);
            }
            bgpvpnBuilder.setExportTargets(exportTargets);
        }
        if (bgpvpn.getRouteDistinguishers() != null) {
            List<String> routeDistinguishers = new ArrayList<String>();
            for( String routeDistinguisher : bgpvpn.getRouteDistinguishers()) {
                routeDistinguishers.add(routeDistinguisher);
            }
            bgpvpnBuilder.setRouteDistinguishers(routeDistinguishers);
        }
        if (bgpvpn.getRouters() != null) {
            List<Uuid> routers = new ArrayList<Uuid>();
            for( String router : bgpvpn.getRouters()) {
                routers.add(toUuid(router));
            }
            bgpvpnBuilder.setRouters(routers);
        }
        if (bgpvpn.getNetworks() != null) {
            List<Uuid> networks = new ArrayList<Uuid>();
            for( String network : bgpvpn.getNetworks()) {
                networks.add(toUuid(network));
            }
            bgpvpnBuilder.setNetworks(networks);
        }
        if (bgpvpn.getTenantID() != null) {
            bgpvpnBuilder.setTenantId(toUuid(bgpvpn.getTenantID()));
        }
        if (bgpvpn.getBgpvpnUUID() != null) {
            bgpvpnBuilder.setUuid(toUuid(bgpvpn.getBgpvpnUUID()));
        } else {
            LOGGER.warn("Attempting to write neutron bgpvpn without UUID");
        }
        return bgpvpnBuilder.build();
    }

    protected Bgpvpn toMd(String uuid) {
        BgpvpnBuilder bgpvpnBuilder = new BgpvpnBuilder();
        bgpvpnBuilder.setUuid(toUuid(uuid));
        return bgpvpnBuilder.build();
    }

    @Override
    protected InstanceIdentifier<Bgpvpn> createInstanceIdentifier(Bgpvpn bgpvpn) {
        return InstanceIdentifier.create(Neutron.class)
                .child(Bgpvpns.class)
                .child(Bgpvpn.class, bgpvpn.getKey());
    }

    protected InstanceIdentifier<Bgpvpns> createInstanceIdentifier() {
        return InstanceIdentifier.create(Neutron.class)
                .child(Bgpvpns.class);
    }

    public static void registerNewInterface(BundleContext context,
                                            ProviderContext providerContext,
                                            List<ServiceRegistration<?>> registrations) {
        NeutronBgpvpnInterface neutronNetworkInterface = new NeutronBgpvpnInterface(providerContext);
        ServiceRegistration<INeutronBgpvpnCRUD> neutronNetworkInterfaceRegistration = context.registerService(INeutronBgpvpnCRUD.class, neutronNetworkInterface, null);
        if(neutronNetworkInterfaceRegistration != null) {
            registrations.add(neutronNetworkInterfaceRegistration);
        }
    }
}
