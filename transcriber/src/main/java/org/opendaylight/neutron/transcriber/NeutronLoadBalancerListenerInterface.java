/*
 * Copyright (C) 2014 Red Hat, Inc.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.transcriber;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.opendaylight.controller.sal.binding.api.BindingAwareBroker.ProviderContext;
import org.opendaylight.neutron.spi.INeutronLoadBalancerListenerCRUD;
import org.opendaylight.neutron.spi.NeutronLoadBalancerListener;
import org.opendaylight.neutron.spi.Neutron_ID;
import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.yang.types.rev130715.Uuid;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.lbaasv2.rev141002.ListenerAttrs.Protocol;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.lbaasv2.rev141002.lbaas.attributes.Listener;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.lbaasv2.rev141002.lbaas.attributes.listener.Listeners;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.lbaasv2.rev141002.lbaas.attributes.listener.ListenersBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.rev150325.Neutron;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NeutronLoadBalancerListenerInterface extends AbstractNeutronInterface<Listeners, NeutronLoadBalancerListener> implements INeutronLoadBalancerListenerCRUD {
    private static final Logger LOGGER = LoggerFactory.getLogger(NeutronLoadBalancerListenerInterface.class);
    private ConcurrentMap<String, NeutronLoadBalancerListener> loadBalancerListenerDB  = new ConcurrentHashMap<String, NeutronLoadBalancerListener>();


    NeutronLoadBalancerListenerInterface(ProviderContext providerContext) {
        super(providerContext);
    }

    // this method uses reflection to update an object from it's delta.

    private boolean overwrite(Object target, Object delta) {
        Method[] methods = target.getClass().getMethods();

        for (Method toMethod : methods) {
            if (toMethod.getDeclaringClass().equals(target.getClass())
                    && toMethod.getName().startsWith("set")) {

                String toName = toMethod.getName();
                String fromName = toName.replace("set", "get");

                try {
                    Method fromMethod = delta.getClass().getMethod(fromName);
                    Object value = fromMethod.invoke(delta, (Object[]) null);
                    if (value != null) {
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

    @Override
    public boolean neutronLoadBalancerListenerExists(String uuid) {
        return loadBalancerListenerDB.containsKey(uuid);
    }

    @Override
    public NeutronLoadBalancerListener getNeutronLoadBalancerListener(String uuid) {
        if (!neutronLoadBalancerListenerExists(uuid)) {
            LOGGER.debug("No LoadBalancerListener Have Been Defined");
            return null;
        }
        return loadBalancerListenerDB.get(uuid);
    }

    @Override
    public List<NeutronLoadBalancerListener> getAllNeutronLoadBalancerListeners() {
        Set<NeutronLoadBalancerListener> allLoadBalancerListeners = new HashSet<NeutronLoadBalancerListener>();
        for (Entry<String, NeutronLoadBalancerListener> entry : loadBalancerListenerDB.entrySet()) {
            NeutronLoadBalancerListener loadBalancerListener = entry.getValue();
            allLoadBalancerListeners.add(loadBalancerListener);
        }
        LOGGER.debug("Exiting getLoadBalancerListeners, Found {} OpenStackLoadBalancerListener", allLoadBalancerListeners.size());
        List<NeutronLoadBalancerListener> ans = new ArrayList<NeutronLoadBalancerListener>();
        ans.addAll(allLoadBalancerListeners);
        return ans;
    }

    @Override
    public boolean addNeutronLoadBalancerListener(NeutronLoadBalancerListener input) {
        if (neutronLoadBalancerListenerExists(input.getLoadBalancerListenerID())) {
            return false;
        }
        loadBalancerListenerDB.putIfAbsent(input.getLoadBalancerListenerID(), input);
        //TODO: add code to find INeutronLoadBalancerListenerAware services and call newtorkCreated on them
        return true;
    }

    @Override
    public boolean removeNeutronLoadBalancerListener(String uuid) {
        if (!neutronLoadBalancerListenerExists(uuid)) {
            return false;
        }
        loadBalancerListenerDB.remove(uuid);
        //TODO: add code to find INeutronLoadBalancerListenerAware services and call newtorkDeleted on them
        return true;
    }

    @Override
    public boolean updateNeutronLoadBalancerListener(String uuid, NeutronLoadBalancerListener delta) {
        if (!neutronLoadBalancerListenerExists(uuid)) {
            return false;
        }
        NeutronLoadBalancerListener target = loadBalancerListenerDB.get(uuid);
        return overwrite(target, delta);
    }

    @Override
    public boolean neutronLoadBalancerListenerInUse(String loadBalancerListenerUUID) {
        return !neutronLoadBalancerListenerExists(loadBalancerListenerUUID);
    }


    @Override
    protected Listeners toMd(String uuid) {
        ListenersBuilder listenersBuilder = new ListenersBuilder();
        listenersBuilder.setUuid(toUuid(uuid));
        return listenersBuilder.build();
    }

    @Override
    protected InstanceIdentifier<Listeners> createInstanceIdentifier(
            Listeners listeners) {
        return InstanceIdentifier.create(Neutron.class)
                .child(Listener.class)
                .child(Listeners.class, listeners.getKey());
    }

    @Override
    protected Listeners toMd(NeutronLoadBalancerListener listeners) {
        ListenersBuilder listenersBuilder = new ListenersBuilder();
        listenersBuilder.setAdminStateUp(listeners.getLoadBalancerListenerAdminStateIsUp());
        if (listeners.getNeutronLoadBalancerListenerConnectionLimit() != null) {
            listenersBuilder.setConnectionLimit(listeners.getNeutronLoadBalancerListenerConnectionLimit());
        }
        if (listeners.getNeutronLoadBalancerListenerDefaultPoolID() != null) {
            listenersBuilder.setDefaultPoolD(toUuid(listeners.getNeutronLoadBalancerListenerDefaultPoolID()));
        }
        if (listeners.getLoadBalancerListenerDescription() != null) {
            listenersBuilder.setDescr(listeners.getLoadBalancerListenerDescription());
        }
        if (listeners.getNeutronLoadBalancerListenerLoadBalancerIDs() != null) {
            List<Uuid> listLoadBalancers = new ArrayList<Uuid>();
            for (Neutron_ID neutron_id : listeners.getNeutronLoadBalancerListenerLoadBalancerIDs()) {
                listLoadBalancers.add(toUuid(neutron_id.getID()));
            }
            listenersBuilder.setLoadbalancers(listLoadBalancers);
        }
        if (listeners.getLoadBalancerListenerName() != null) {
            listenersBuilder.setName(listeners.getLoadBalancerListenerName());
        }
        if (listeners.getNeutronLoadBalancerListenerProtocol() != null) {
            listenersBuilder.setProtocol(Protocol.valueOf(listeners.getNeutronLoadBalancerListenerProtocol()));
        }
        if (listeners.getNeutronLoadBalancerListenerProtocolPort() != null) {
            listenersBuilder.setProtocolPort(Integer.valueOf(listeners.getNeutronLoadBalancerListenerProtocolPort()));
        }
        if (listeners.getLoadBalancerListenerTenantID() != null && !listeners.getLoadBalancerListenerTenantID().isEmpty()) {
            listenersBuilder.setTenantId(toUuid(listeners.getLoadBalancerListenerTenantID()));
        }
        if (listeners.getLoadBalancerListenerID() != null) {
            listenersBuilder.setUuid(toUuid(listeners.getLoadBalancerListenerID()));
        } else {
            LOGGER.warn("Attempting to write neutron load balancer listener without UUID");
        }
        return listenersBuilder.build();
    }
}
