/*
 * Copyright (C) 2014 Red Hat, Inc.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.transcriber;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.opendaylight.controller.sal.binding.api.BindingAwareBroker.ProviderContext;
import org.opendaylight.neutron.spi.INeutronLoadBalancerHealthMonitorCRUD;
import org.opendaylight.neutron.spi.NeutronLoadBalancerHealthMonitor;
import org.opendaylight.neutron.spi.Neutron_ID;
import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.yang.types.rev130715.Uuid;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.constants.rev160807.ProbeBase;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.constants.rev160807.ProbeHttp;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.constants.rev160807.ProbeHttps;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.constants.rev160807.ProbeIcmp;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.constants.rev160807.ProbeTcp;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.lbaasv2.rev141002.lbaas.attributes.Healthmonitor;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.lbaasv2.rev141002.lbaas.attributes.healthmonitor.Healthmonitors;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.lbaasv2.rev141002.lbaas.attributes.healthmonitor.HealthmonitorsBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.rev150325.Neutron;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableBiMap;

public class NeutronLoadBalancerHealthMonitorInterface extends AbstractNeutronInterface<Healthmonitors, NeutronLoadBalancerHealthMonitor> implements INeutronLoadBalancerHealthMonitorCRUD {
    private static final Logger LOGGER = LoggerFactory.getLogger(NeutronLoadBalancerHealthMonitorInterface.class);
    private ConcurrentMap<String, NeutronLoadBalancerHealthMonitor> loadBalancerHealthMonitorDB = new ConcurrentHashMap<String, NeutronLoadBalancerHealthMonitor>();

    private static final ImmutableBiMap<Class<? extends ProbeBase>,String> PROBE_MAP
            = new ImmutableBiMap.Builder<Class<? extends ProbeBase>,String>()
            .put(ProbeHttp.class,"HTTP")
            .put(ProbeHttps.class,"HTTPS")
            .put(ProbeIcmp.class,"ICMP")
            .put(ProbeTcp.class,"TCP")
            .build();


    NeutronLoadBalancerHealthMonitorInterface(ProviderContext providerContext) {
        super(providerContext);
    }

    @Override
    public boolean neutronLoadBalancerHealthMonitorExists(String uuid) {
        return loadBalancerHealthMonitorDB.containsKey(uuid);
    }

    @Override
    public NeutronLoadBalancerHealthMonitor getNeutronLoadBalancerHealthMonitor(String uuid) {
        if (!neutronLoadBalancerHealthMonitorExists(uuid)) {
            LOGGER.debug("No LoadBalancerHealthMonitor has Been Defined");
            return null;
        }
        return loadBalancerHealthMonitorDB.get(uuid);
    }

    @Override
    public List<NeutronLoadBalancerHealthMonitor> getAllNeutronLoadBalancerHealthMonitors() {
        Set<NeutronLoadBalancerHealthMonitor> allLoadBalancerHealthMonitors = new HashSet<NeutronLoadBalancerHealthMonitor>();
        for (Entry<String, NeutronLoadBalancerHealthMonitor> entry : loadBalancerHealthMonitorDB.entrySet()) {
            NeutronLoadBalancerHealthMonitor loadBalancerHealthMonitor = entry.getValue();
            allLoadBalancerHealthMonitors.add(loadBalancerHealthMonitor);
        }
        LOGGER.debug("Exiting getLoadBalancerHealthMonitors, Found {} OpenStackLoadBalancerHealthMonitor", allLoadBalancerHealthMonitors.size());
        List<NeutronLoadBalancerHealthMonitor> ans = new ArrayList<NeutronLoadBalancerHealthMonitor>();
        ans.addAll(allLoadBalancerHealthMonitors);
        return ans;
    }

    @Override
    public boolean addNeutronLoadBalancerHealthMonitor(NeutronLoadBalancerHealthMonitor input) {
        if (neutronLoadBalancerHealthMonitorExists(input.getLoadBalancerHealthMonitorID())) {
            return false;
        }
        loadBalancerHealthMonitorDB.putIfAbsent(input.getLoadBalancerHealthMonitorID(), input);
        //TODO: add code to find INeutronLoadBalancerHealthMonitorAware services and call newtorkCreated on them
        return true;
    }

    @Override
    public boolean removeNeutronLoadBalancerHealthMonitor(String uuid) {
        if (!neutronLoadBalancerHealthMonitorExists(uuid)) {
            return false;
        }
        loadBalancerHealthMonitorDB.remove(uuid);
        //TODO: add code to find INeutronLoadBalancerHealthMonitorAware services and call newtorkDeleted on them
        return true;
    }

    @Override
    public boolean updateNeutronLoadBalancerHealthMonitor(String uuid, NeutronLoadBalancerHealthMonitor delta) {
        if (!neutronLoadBalancerHealthMonitorExists(uuid)) {
            return false;
        }
        NeutronLoadBalancerHealthMonitor target = loadBalancerHealthMonitorDB.get(uuid);
        return overwrite(target, delta);
    }

    @Override
    public boolean neutronLoadBalancerHealthMonitorInUse(String loadBalancerHealthMonitorUUID) {
        return !neutronLoadBalancerHealthMonitorExists(loadBalancerHealthMonitorUUID);
    }

    @Override
    protected Healthmonitors toMd(String uuid) {
        HealthmonitorsBuilder healthmonitorsBuilder = new HealthmonitorsBuilder();
        healthmonitorsBuilder.setUuid(toUuid(uuid));
        return healthmonitorsBuilder.build();
    }

    @Override
    protected InstanceIdentifier<Healthmonitors> createInstanceIdentifier(
            Healthmonitors healthMonitors) {
        return InstanceIdentifier.create(Neutron.class)
                .child(Healthmonitor.class )
                .child(Healthmonitors.class, healthMonitors.getKey());
    }

    @Override
    protected Healthmonitors toMd(NeutronLoadBalancerHealthMonitor healthMonitor) {
        HealthmonitorsBuilder healthmonitorsBuilder = new HealthmonitorsBuilder();
        healthmonitorsBuilder.setAdminStateUp(healthMonitor.getLoadBalancerHealthMonitorAdminStateIsUp());
        if (healthMonitor.getLoadBalancerHealthMonitorDelay() != null) {
            healthmonitorsBuilder.setDelay(Long.valueOf(healthMonitor.getLoadBalancerHealthMonitorDelay()));
        }
        if (healthMonitor.getLoadBalancerHealthMonitorExpectedCodes() != null) {
            healthmonitorsBuilder.setExpectedCodes(healthMonitor.getLoadBalancerHealthMonitorExpectedCodes());
        }
        if (healthMonitor.getLoadBalancerHealthMonitorHttpMethod() != null) {
            healthmonitorsBuilder.setHttpMethod(healthMonitor.getLoadBalancerHealthMonitorHttpMethod());
        }
        if (healthMonitor.getLoadBalancerHealthMonitorMaxRetries() != null) {
            healthmonitorsBuilder.setMaxRetries(Integer.valueOf(healthMonitor.getLoadBalancerHealthMonitorMaxRetries()));
        }
        if (healthMonitor.getLoadBalancerHealthMonitorPools() != null) {
            List<Uuid> listUuid = new ArrayList<Uuid>();
            for (Neutron_ID neutron_id : healthMonitor.getLoadBalancerHealthMonitorPools()) {
                listUuid.add(toUuid(neutron_id.getID()));
            }
            healthmonitorsBuilder.setPools(listUuid);
        }
        if (healthMonitor.getLoadBalancerHealthMonitorTenantID() != null) {
            healthmonitorsBuilder.setTenantId(toUuid(healthMonitor.getLoadBalancerHealthMonitorTenantID()));
        }
        if (healthMonitor.getLoadBalancerHealthMonitorTimeout() != null) {
            healthmonitorsBuilder.setTimeout(Long.valueOf(healthMonitor.getLoadBalancerHealthMonitorTimeout()));
        }
        if (healthMonitor.getLoadBalancerHealthMonitorType() != null) {
            ImmutableBiMap<String, Class<? extends ProbeBase>> mapper =
                    PROBE_MAP.inverse();
            healthmonitorsBuilder.setType((Class<? extends ProbeBase>) mapper.get(healthMonitor.getLoadBalancerHealthMonitorType()));
        }
        if (healthMonitor.getLoadBalancerHealthMonitorUrlPath() != null) {
            healthmonitorsBuilder.setUrlPath(healthMonitor.getLoadBalancerHealthMonitorUrlPath());
        }
        if (healthMonitor.getLoadBalancerHealthMonitorID() != null) {
            healthmonitorsBuilder.setUuid(toUuid(healthMonitor.getLoadBalancerHealthMonitorID()));
        } else {
            LOGGER.warn("Attempting to write neutron laod balancer health monitor without UUID");
        }
        return healthmonitorsBuilder.build();
    }

    public static void registerNewInterface(BundleContext context,
                                            ProviderContext providerContext,
                                            List<ServiceRegistration<?>> registrations) {
        NeutronLoadBalancerHealthMonitorInterface neutronLoadBalancerHealthMonitorInterface = new NeutronLoadBalancerHealthMonitorInterface(providerContext);
        ServiceRegistration<INeutronLoadBalancerHealthMonitorCRUD> neutronLoadBalancerHealthMonitorInterfaceRegistration = context.registerService(INeutronLoadBalancerHealthMonitorCRUD.class, neutronLoadBalancerHealthMonitorInterface, null);
        if(neutronLoadBalancerHealthMonitorInterfaceRegistration != null) {
            registrations.add(neutronLoadBalancerHealthMonitorInterfaceRegistration);
        }
    }
}
