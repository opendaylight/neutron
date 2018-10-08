/*
 * Copyright (c) 2014, 2015 Red Hat, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.neutron.transcriber;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableBiMap;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.apache.aries.blueprint.annotation.service.Service;
import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.neutron.northbound.api.BadRequestException;
import org.opendaylight.neutron.spi.INeutronLoadBalancerListenerCRUD;
import org.opendaylight.neutron.spi.NeutronLoadBalancerListener;
import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.yang.types.rev130715.Uuid;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.constants.rev150712.ProtocolBase;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.constants.rev150712.ProtocolHttp;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.constants.rev150712.ProtocolHttps;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.constants.rev150712.ProtocolTcp;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.constants.rev150712.ProtocolTerminatedHttps;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.lbaasv2.rev150712.lbaas.attributes.Listeners;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.lbaasv2.rev150712.lbaas.attributes.listeners.Listener;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.lbaasv2.rev150712.lbaas.attributes.listeners.ListenerBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.lbaasv2.rev150712.lbaas.attributes.listeners.ListenerKey;

@Singleton
@Service(classes = INeutronLoadBalancerListenerCRUD.class)
public final class NeutronLoadBalancerListenerInterface
        extends AbstractNeutronInterface<Listener, Listeners, ListenerKey, NeutronLoadBalancerListener>
        implements INeutronLoadBalancerListenerCRUD {

    private static final ImmutableBiMap<Class<? extends ProtocolBase>,
            String> PROTOCOL_MAP = new ImmutableBiMap.Builder<Class<? extends ProtocolBase>, String>()
                    .put(ProtocolHttp.class, "HTTP").put(ProtocolHttps.class, "HTTPS").put(ProtocolTcp.class, "TCP")
                    .put(ProtocolTerminatedHttps.class, "TERMINATED_HTTPS").build();

    @Inject
    public NeutronLoadBalancerListenerInterface(DataBroker db) {
        super(ListenerBuilder.class, db);
    }

    @Override
    protected List<Listener> getDataObjectList(Listeners listeners) {
        return listeners.getListener();
    }

    @Override
    protected Listener toMd(NeutronLoadBalancerListener listener) {
        final ListenerBuilder listenerBuilder = new ListenerBuilder();
        toMdBaseAttributes(listener, listenerBuilder);
        listenerBuilder.setAdminStateUp(listener.getLoadBalancerListenerAdminStateIsUp());
        if (listener.getNeutronLoadBalancerListenerConnectionLimit() != null) {
            listenerBuilder.setConnectionLimit(listener.getNeutronLoadBalancerListenerConnectionLimit());
        }
        if (listener.getNeutronLoadBalancerListenerDefaultPoolID() != null) {
            listenerBuilder.setDefaultPoolId(toUuid(listener.getNeutronLoadBalancerListenerDefaultPoolID()));
        }
        if (listener.getNeutronLoadBalancerListenerLoadBalancerIDs() != null) {
            final List<Uuid> listLoadBalancers = new ArrayList<>();
            for (final String neutronId : listener.getNeutronLoadBalancerListenerLoadBalancerIDs()) {
                if (!Strings.isNullOrEmpty(neutronId)) {
                    listLoadBalancers.add(toUuid(neutronId));
                }
            }
            listenerBuilder.setLoadbalancers(listLoadBalancers);
        }
        if (listener.getNeutronLoadBalancerListenerProtocol() != null) {
            final ImmutableBiMap<String, Class<? extends ProtocolBase>> mapper = PROTOCOL_MAP.inverse();
            Class<? extends ProtocolBase> protocol = mapper.get(listener.getNeutronLoadBalancerListenerProtocol());
            if (protocol != null) {
                listenerBuilder.setProtocol(protocol);
            } else {
                throw new BadRequestException("Protocol {" + listener.getNeutronLoadBalancerListenerProtocol()
                        + "} is not supported");
            }
        }
        if (listener.getNeutronLoadBalancerListenerProtocolPort() != null) {
            listenerBuilder.setProtocolPort(Integer.valueOf(listener.getNeutronLoadBalancerListenerProtocolPort()));
        }
        return listenerBuilder.build();
    }

    @Override
    protected NeutronLoadBalancerListener fromMd(Listener listener) {
        final NeutronLoadBalancerListener answer = new NeutronLoadBalancerListener();
        fromMdBaseAttributes(listener, answer);
        if (listener.isAdminStateUp() != null) {
            answer.setLoadBalancerListenerAdminStateIsUp(listener.isAdminStateUp());
        }
        if (listener.getConnectionLimit() != null) {
            answer.setNeutronLoadBalancerListenerConnectionLimit(listener.getConnectionLimit());
        }
        if (listener.getDefaultPoolId() != null) {
            answer.setNeutronLoadBalancerListenerDefaultPoolID(listener.getDefaultPoolId().getValue());
        }
        if (listener.getLoadbalancers() != null) {
            final List<String> list = new ArrayList<>();
            for (final Uuid id : listener.getLoadbalancers()) {
                list.add(id.getValue());
            }
            answer.setNeutronLoadBalancerListenerLoadBalancerIDs(list);
        }
        if (listener.getProtocol() != null) {
            answer.setNeutronLoadBalancerListenerProtocol(PROTOCOL_MAP.get(listener.getProtocol()));
        }
        if (listener.getProtocolPort() != null) {
            answer.setNeutronLoadBalancerListenerProtocolPort(String.valueOf(listener.getProtocolPort()));
        }
        return answer;
    }
}
