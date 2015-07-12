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

import org.opendaylight.controller.sal.binding.api.BindingAwareBroker.ProviderContext;
import org.opendaylight.neutron.spi.INeutronLoadBalancerListenerCRUD;
import org.opendaylight.neutron.spi.NeutronLoadBalancerListener;
import org.opendaylight.neutron.spi.Neutron_ID;
import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.yang.types.rev130715.Uuid;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.constants.rev150712.ProtocolBase;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.constants.rev150712.ProtocolHttp;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.constants.rev150712.ProtocolHttps;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.constants.rev150712.ProtocolTcp;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.lbaasv2.rev150712.lbaas.attributes.Listeners;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.lbaasv2.rev150712.lbaas.attributes.listeners.Listener;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.lbaasv2.rev150712.lbaas.attributes.listeners.ListenerBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.rev150712.Neutron;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableBiMap;

public class NeutronLoadBalancerListenerInterface extends AbstractNeutronInterface<Listener, NeutronLoadBalancerListener> implements INeutronLoadBalancerListenerCRUD {
    private static final Logger LOGGER = LoggerFactory.getLogger(NeutronLoadBalancerListenerInterface.class);

    private static final ImmutableBiMap<Class<? extends ProtocolBase>,String> PROTOCOL_MAP
            = new ImmutableBiMap.Builder<Class<? extends ProtocolBase>,String>()
            .put(ProtocolHttp.class,"HTTP")
            .put(ProtocolHttps.class,"HTTPS")
            .put(ProtocolTcp.class,"TCP")
            .build();

    NeutronLoadBalancerListenerInterface(ProviderContext providerContext) {
        super(providerContext);
    }

    @Override
    public boolean neutronLoadBalancerListenerExists(String uuid) {
        return exists(uuid);
    }

    @Override
    public NeutronLoadBalancerListener getNeutronLoadBalancerListener(String uuid) {
        return get(uuid);
    }

    @Override
    public List<NeutronLoadBalancerListener> getAll() {
        Set<NeutronLoadBalancerListener> allLoadBalancerListeners = new HashSet<NeutronLoadBalancerListener>();
        Listeners listeners = readMd(createInstanceIdentifier());
        if (listeners != null) {
            for (Listener listener: listeners.getListener()) {
                allLoadBalancerListeners.add(fromMd(listener));
            }
        }
        LOGGER.debug("Exiting getLoadBalancerListeners, Found {} OpenStackLoadBalancerListener", allLoadBalancerListeners.size());
        List<NeutronLoadBalancerListener> ans = new ArrayList<NeutronLoadBalancerListener>();
        ans.addAll(allLoadBalancerListeners);
        return ans;
    }

    @Override
    public List<NeutronLoadBalancerListener> getAllNeutronLoadBalancerListeners() {
        return getAll();
    }

    @Override
    public boolean addNeutronLoadBalancerListener(NeutronLoadBalancerListener input) {
        return add(input);
    }

    @Override
    public boolean removeNeutronLoadBalancerListener(String uuid) {
        return remove(uuid);
    }

    @Override
    public boolean updateNeutronLoadBalancerListener(String uuid, NeutronLoadBalancerListener delta) {
        return update(uuid, delta);
    }

    @Override
    public boolean neutronLoadBalancerListenerInUse(String loadBalancerListenerUUID) {
        return !exists(loadBalancerListenerUUID);
    }


    @Override
    protected Listener toMd(String uuid) {
        ListenerBuilder listenerBuilder = new ListenerBuilder();
        listenerBuilder.setUuid(toUuid(uuid));
        return listenerBuilder.build();
    }

    @Override
    protected InstanceIdentifier<Listener> createInstanceIdentifier(
            Listener listener) {
        return InstanceIdentifier.create(Neutron.class)
                .child(Listeners.class)
                .child(Listener.class, listener.getKey());
    }

    protected InstanceIdentifier<Listeners> createInstanceIdentifier() {
        return InstanceIdentifier.create(Neutron.class)
                .child(Listeners.class);
    }

    @Override
    protected Listener toMd(NeutronLoadBalancerListener listener) {
        ListenerBuilder listenerBuilder = new ListenerBuilder();
        listenerBuilder.setAdminStateUp(listener.getLoadBalancerListenerAdminStateIsUp());
        if (listener.getNeutronLoadBalancerListenerConnectionLimit() != null) {
            listenerBuilder.setConnectionLimit(listener.getNeutronLoadBalancerListenerConnectionLimit());
        }
        if (listener.getNeutronLoadBalancerListenerDefaultPoolID() != null) {
            listenerBuilder.setDefaultPoolId(toUuid(listener.getNeutronLoadBalancerListenerDefaultPoolID()));
        }
        if (listener.getLoadBalancerListenerDescription() != null) {
            listenerBuilder.setDescr(listener.getLoadBalancerListenerDescription());
        }
        if (listener.getNeutronLoadBalancerListenerLoadBalancerIDs() != null) {
            List<Uuid> listLoadBalancers = new ArrayList<Uuid>();
            for (Neutron_ID neutron_id : listener.getNeutronLoadBalancerListenerLoadBalancerIDs()) {
                listLoadBalancers.add(toUuid(neutron_id.getID()));
            }
            listenerBuilder.setLoadbalancers(listLoadBalancers);
        }
        if (listener.getLoadBalancerListenerName() != null) {
            listenerBuilder.setName(listener.getLoadBalancerListenerName());
        }
        if (listener.getNeutronLoadBalancerListenerProtocol() != null) {
            ImmutableBiMap<String, Class<? extends ProtocolBase>> mapper =
                PROTOCOL_MAP.inverse();
            listenerBuilder.setProtocol((Class<? extends ProtocolBase>) mapper.get(listener.getNeutronLoadBalancerListenerProtocol()));
        }
        if (listener.getNeutronLoadBalancerListenerProtocolPort() != null) {
            listenerBuilder.setProtocolPort(Integer.valueOf(listener.getNeutronLoadBalancerListenerProtocolPort()));
        }
        if (listener.getTenantID() != null) {
            listenerBuilder.setTenantId(toUuid(listener.getTenantID()));
        }
        if (listener.getID() != null) {
            listenerBuilder.setUuid(toUuid(listener.getID()));
        } else {
            LOGGER.warn("Attempting to write neutron load balancer listener without UUID");
        }
        return listenerBuilder.build();
    }

    protected NeutronLoadBalancerListener fromMd(Listener listener) {
        NeutronLoadBalancerListener answer = new NeutronLoadBalancerListener();
        if (listener.isAdminStateUp() != null) {
            answer.setLoadBalancerListenerAdminStateIsUp(listener.isAdminStateUp());
        }
        if (listener.getConnectionLimit() != null) {
            answer.setNeutronLoadBalancerListenerConnectionLimit(listener.getConnectionLimit());
        }
        if (listener.getDefaultPoolId() != null) {
            answer.setNeutronLoadBalancerListenerDefaultPoolID(listener.getDefaultPoolId().getValue());
        }
        if (listener.getDescr() != null) {
            answer.setLoadBalancerListenerDescription(listener.getDescr());
        }
        if (listener.getLoadbalancers() != null) {
            List<Neutron_ID> list = new ArrayList<Neutron_ID>();
            for (Uuid id : listener.getLoadbalancers()) {
                list.add(new Neutron_ID(id.getValue()));
            }
            answer.setNeutronLoadBalancerListenerLoadBalancerIDs(list);
        }
        if (listener.getName() != null) {
            answer.setLoadBalancerListenerName(listener.getName());
        }
        if (listener.getProtocol() != null) {
            answer.setNeutronLoadBalancerListenerProtocol(PROTOCOL_MAP.get(listener.getProtocol()));
        }
        if (listener.getProtocolPort() != null) {
            answer.setNeutronLoadBalancerListenerProtocolPort(String.valueOf(listener.getProtocolPort()));
        }
        if (listener.getTenantId() != null) {
            answer.setTenantID(listener.getTenantId().getValue().replace("-",""));
        }
        if (listener.getUuid() != null) {
            answer.setID(listener.getUuid().getValue());
        }
        return answer;
    }

    public static void registerNewInterface(BundleContext context,
                                            ProviderContext providerContext,
                                            List<ServiceRegistration<?>> registrations) {
        NeutronLoadBalancerListenerInterface neutronLoadBalancerListenerInterface = new NeutronLoadBalancerListenerInterface(providerContext);
        ServiceRegistration<INeutronLoadBalancerListenerCRUD> neutronLoadBalancerListenerInterfaceRegistration = context.registerService(INeutronLoadBalancerListenerCRUD.class, neutronLoadBalancerListenerInterface, null);
        if(neutronLoadBalancerListenerInterfaceRegistration != null) {
            registrations.add(neutronLoadBalancerListenerInterfaceRegistration);
        }
    }
}
