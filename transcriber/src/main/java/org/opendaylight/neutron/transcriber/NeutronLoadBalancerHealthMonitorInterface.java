/*
 * Copyright (c) 2014, 2015 Red Hat, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.transcriber;

import com.google.common.collect.ImmutableBiMap;
import java.util.ArrayList;
import java.util.List;
import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.neutron.spi.INeutronLoadBalancerHealthMonitorCRUD;
import org.opendaylight.neutron.spi.NeutronLoadBalancerHealthMonitor;
import org.opendaylight.neutron.spi.Neutron_ID;
import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.yang.types.rev130715.Uuid;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.constants.rev150712.ProbeBase;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.constants.rev150712.ProbeHttp;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.constants.rev150712.ProbeHttps;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.constants.rev150712.ProbePing;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.constants.rev150712.ProbeTcp;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.lbaasv2.rev150712.lbaas.attributes.Healthmonitors;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.lbaasv2.rev150712.lbaas.attributes.healthmonitors.Healthmonitor;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.lbaasv2.rev150712.lbaas.attributes.healthmonitors.HealthmonitorBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.rev150712.Neutron;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NeutronLoadBalancerHealthMonitorInterface extends AbstractNeutronInterface<Healthmonitor, Healthmonitors, NeutronLoadBalancerHealthMonitor> implements INeutronLoadBalancerHealthMonitorCRUD {
    private static final Logger LOGGER = LoggerFactory.getLogger(NeutronLoadBalancerHealthMonitorInterface.class);

    private static final ImmutableBiMap<Class<? extends ProbeBase>,String> PROBE_MAP
            = new ImmutableBiMap.Builder<Class<? extends ProbeBase>,String>()
            .put(ProbeHttp.class,"HTTP")
            .put(ProbeHttps.class,"HTTPS")
            .put(ProbePing.class,"PING")
            .put(ProbeTcp.class,"TCP")
            .build();


    NeutronLoadBalancerHealthMonitorInterface(DataBroker db) {
        super(db);
    }

    @Override
    protected List<Healthmonitor> getDataObjectList(Healthmonitors healthMonitors) {
        return healthMonitors.getHealthmonitor();
    }

    @Override
    protected Healthmonitor toMd(String uuid) {
        final HealthmonitorBuilder healthmonitorBuilder = new HealthmonitorBuilder();
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

    @Override
    protected InstanceIdentifier<Healthmonitors> createInstanceIdentifier() {
        return InstanceIdentifier.create(Neutron.class)
                .child(Healthmonitors.class);
    }

    @Override
    protected Healthmonitor toMd(NeutronLoadBalancerHealthMonitor healthMonitor) {
        final HealthmonitorBuilder healthmonitorBuilder = new HealthmonitorBuilder();
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
            final List<Uuid> listUuid = new ArrayList<Uuid>();
            for (final Neutron_ID neutron_id : healthMonitor.getLoadBalancerHealthMonitorPools()) {
                listUuid.add(toUuid(neutron_id.getID()));
            }
            healthmonitorBuilder.setPools(listUuid);
        }
        if (healthMonitor.getTenantID() != null) {
            healthmonitorBuilder.setTenantId(toUuid(healthMonitor.getTenantID()));
        }
        if (healthMonitor.getLoadBalancerHealthMonitorTimeout() != null) {
            healthmonitorBuilder.setTimeout(Long.valueOf(healthMonitor.getLoadBalancerHealthMonitorTimeout()));
        }
        if (healthMonitor.getLoadBalancerHealthMonitorType() != null) {
            final ImmutableBiMap<String, Class<? extends ProbeBase>> mapper =
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
        final NeutronLoadBalancerHealthMonitor answer = new NeutronLoadBalancerHealthMonitor();
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
            final List<Neutron_ID> list = new ArrayList<Neutron_ID>();
            for (final Uuid id : healthMonitor.getPools()) {
                list.add(new Neutron_ID(id.getValue()));
            }
            answer.setLoadBalancerHealthMonitorPools(list);
        }
        if (healthMonitor.getTenantId() != null) {
            answer.setTenantID(healthMonitor.getTenantId());
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
}
