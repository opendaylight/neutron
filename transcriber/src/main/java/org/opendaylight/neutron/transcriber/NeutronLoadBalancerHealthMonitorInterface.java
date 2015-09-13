/*
 * Copyright (c) 2014, 2015 Red Hat, Inc. and others.  All rights reserved.
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

import org.opendaylight.controller.md.sal.binding.api.BindingTransactionChain;
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
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.lbaasv2.rev141002.lbaas.attributes.Healthmonitors;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.lbaasv2.rev141002.lbaas.attributes.healthmonitors.Healthmonitor;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.lbaasv2.rev141002.lbaas.attributes.healthmonitors.HealthmonitorBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.rev150325.Neutron;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableBiMap;

public class NeutronLoadBalancerHealthMonitorInterface extends AbstractNeutronInterface<Healthmonitor, NeutronLoadBalancerHealthMonitor> implements INeutronLoadBalancerHealthMonitorCRUD {
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
        return exists(uuid, null);
    }

    @Override
    public NeutronLoadBalancerHealthMonitor getNeutronLoadBalancerHealthMonitor(String uuid) {
        return get(uuid, null);
    }

    @Override
    protected List<NeutronLoadBalancerHealthMonitor> _getAll(BindingTransactionChain chain) {
        Preconditions.checkNotNull(chain);

        Set<NeutronLoadBalancerHealthMonitor> allLoadBalancerHealthMonitors = new HashSet<NeutronLoadBalancerHealthMonitor>();
        Healthmonitors healthMonitors = readMd(createInstanceIdentifier(), chain);
        if (healthMonitors != null) {
            for (Healthmonitor healthMonitor : healthMonitors.getHealthmonitor()) {
                allLoadBalancerHealthMonitors.add(fromMd(healthMonitor));
            }
        }
        LOGGER.debug("Exiting getLoadBalancerHealthMonitors, Found {} OpenStackLoadBalancerHealthMonitor", allLoadBalancerHealthMonitors.size());
        List<NeutronLoadBalancerHealthMonitor> ans = new ArrayList<NeutronLoadBalancerHealthMonitor>();
        ans.addAll(allLoadBalancerHealthMonitors);
        return ans;
    }

    @Override
    public List<NeutronLoadBalancerHealthMonitor> getAllNeutronLoadBalancerHealthMonitors() {
        return getAll(null);
    }

    @Override
    public boolean addNeutronLoadBalancerHealthMonitor(NeutronLoadBalancerHealthMonitor input) {
        return add(input, null);
    }

    @Override
    public boolean removeNeutronLoadBalancerHealthMonitor(String uuid) {
        return remove(uuid, null);
    }

    @Override
    public boolean updateNeutronLoadBalancerHealthMonitor(String uuid, NeutronLoadBalancerHealthMonitor delta) {
        return update(uuid, delta, null);
    }

    @Override
    public boolean neutronLoadBalancerHealthMonitorInUse(String loadBalancerHealthMonitorUUID) {
        return !exists(loadBalancerHealthMonitorUUID, null);
    }

    @Override
    protected Healthmonitor toMd(String uuid) {
        HealthmonitorBuilder healthmonitorBuilder = new HealthmonitorBuilder();
        healthmonitorBuilder.setUuid(toUuid(uuid));
        return healthmonitorBuilder.build();
    }

    @Override
    protected InstanceIdentifier<Healthmonitor> createInstanceIdentifier(
            Healthmonitor healthMonitor) {
        return InstanceIdentifier.create(Neutron.class)
                .child(Healthmonitors.class )
                .child(Healthmonitor.class, healthMonitor.getKey());
    }

    protected InstanceIdentifier<Healthmonitors> createInstanceIdentifier() {
        return InstanceIdentifier.create(Neutron.class)
                .child(Healthmonitors.class);
    }

    @Override
    protected Healthmonitor toMd(NeutronLoadBalancerHealthMonitor healthMonitor) {
        HealthmonitorBuilder healthmonitorBuilder = new HealthmonitorBuilder();
        healthmonitorBuilder.setAdminStateUp(healthMonitor.getLoadBalancerHealthMonitorAdminStateIsUp());
        if (healthMonitor.getLoadBalancerHealthMonitorDelay() != null) {
            healthmonitorBuilder.setDelay(Long.valueOf(healthMonitor.getLoadBalancerHealthMonitorDelay()));
        }
        if (healthMonitor.getLoadBalancerHealthMonitorExpectedCodes() != null) {
            healthmonitorBuilder.setExpectedCodes(healthMonitor.getLoadBalancerHealthMonitorExpectedCodes());
        }
        if (healthMonitor.getLoadBalancerHealthMonitorHttpMethod() != null) {
            healthmonitorBuilder.setHttpMethod(healthMonitor.getLoadBalancerHealthMonitorHttpMethod());
        }
        if (healthMonitor.getLoadBalancerHealthMonitorMaxRetries() != null) {
            healthmonitorBuilder.setMaxRetries(Integer.valueOf(healthMonitor.getLoadBalancerHealthMonitorMaxRetries()));
        }
        if (healthMonitor.getLoadBalancerHealthMonitorPools() != null) {
            List<Uuid> listUuid = new ArrayList<Uuid>();
            for (Neutron_ID neutron_id : healthMonitor.getLoadBalancerHealthMonitorPools()) {
                listUuid.add(toUuid(neutron_id.getID()));
            }
            healthmonitorBuilder.setPools(listUuid);
        }
        if (healthMonitor.getLoadBalancerHealthMonitorTenantID() != null) {
            healthmonitorBuilder.setTenantId(toUuid(healthMonitor.getLoadBalancerHealthMonitorTenantID()));
        }
        if (healthMonitor.getLoadBalancerHealthMonitorTimeout() != null) {
            healthmonitorBuilder.setTimeout(Long.valueOf(healthMonitor.getLoadBalancerHealthMonitorTimeout()));
        }
        if (healthMonitor.getLoadBalancerHealthMonitorType() != null) {
            ImmutableBiMap<String, Class<? extends ProbeBase>> mapper =
                    PROBE_MAP.inverse();
            healthmonitorBuilder.setType((Class<? extends ProbeBase>) mapper.get(healthMonitor.getLoadBalancerHealthMonitorType()));
        }
        if (healthMonitor.getLoadBalancerHealthMonitorUrlPath() != null) {
            healthmonitorBuilder.setUrlPath(healthMonitor.getLoadBalancerHealthMonitorUrlPath());
        }
        if (healthMonitor.getID() != null) {
            healthmonitorBuilder.setUuid(toUuid(healthMonitor.getID()));
        } else {
            LOGGER.warn("Attempting to write neutron laod balancer health monitor without UUID");
        }
        return healthmonitorBuilder.build();
    }

    protected NeutronLoadBalancerHealthMonitor fromMd(Healthmonitor healthMonitor) {
        NeutronLoadBalancerHealthMonitor answer = new NeutronLoadBalancerHealthMonitor();
        if (healthMonitor.isAdminStateUp() != null) {
             answer.setLoadBalancerHealthMonitorAdminStateIsUp(healthMonitor.isAdminStateUp());
        }
        if (healthMonitor.getDelay() != null) {
            answer.setLoadBalancerHealthMonitorDelay(healthMonitor.getDelay().intValue());
        }
        if (healthMonitor.getExpectedCodes() != null) {
            answer.setLoadBalancerHealthMonitorExpectedCodes(healthMonitor.getExpectedCodes());
        }
        if (healthMonitor.getHttpMethod() != null) {
            answer.setLoadBalancerHealthMonitorHttpMethod(healthMonitor.getHttpMethod());
        }
        if (healthMonitor.getMaxRetries() != null) {
            answer.setLoadBalancerHealthMonitorMaxRetries(Integer.valueOf(healthMonitor.getMaxRetries()));
        }
        if (healthMonitor.getPools() != null) {
            List<Neutron_ID> list = new ArrayList<Neutron_ID>();
            for (Uuid id : healthMonitor.getPools()) {
                list.add(new Neutron_ID(id.getValue()));
            }
            answer.setLoadBalancerHealthMonitorPools(list);
        }
        if (healthMonitor.getTenantId() != null) {
            answer.setLoadBalancerHealthMonitorTenantID(healthMonitor.getTenantId().getValue().replace("-",""));
        }
        if (healthMonitor.getTimeout() != null) {
            answer.setLoadBalancerHealthMonitorTimeout(healthMonitor.getTimeout().intValue());
        }
        if (healthMonitor.getType() != null) {
            answer.setLoadBalancerHealthMonitorType(PROBE_MAP.get(healthMonitor.getType()));
        }
        if (healthMonitor.getUrlPath() != null) {
            answer.setLoadBalancerHealthMonitorUrlPath(healthMonitor.getUrlPath());
        }
        if (healthMonitor.getUuid() != null) {
            answer.setID(healthMonitor.getUuid().getValue());
        } else {
            LOGGER.warn("Attempting to write neutron laod balancer health monitor without UUID");
        }
        return answer;
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
