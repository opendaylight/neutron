/*
 * Copyright (c) 2018 Red Hat, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.neutron.northbound.impl;

import com.google.common.base.Optional;
import java.util.concurrent.ExecutionException;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.core.MultivaluedHashMap;

import org.opendaylight.netconf.sal.restconf.api.JSONRestconfService;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.northbound.api.config.rev181024.NeutronNorthboundApiConfig;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.ports.rev150712.ports.attributes.Ports;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.rev150712.Neutron;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.controller.md.sal.remote.rev140114.CreateDataChangeEventSubscriptionInputBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.controller.md.sal.remote.rev140114.SalRemoteService;
import org.opendaylight.yang.gen.v1.urn.sal.restconf.event.subscription.rev140708.CreateDataChangeEventSubscriptionInput1;
import org.opendaylight.yang.gen.v1.urn.sal.restconf.event.subscription.rev140708.CreateDataChangeEventSubscriptionInput1.Datastore;
import org.opendaylight.yang.gen.v1.urn.sal.restconf.event.subscription.rev140708.CreateDataChangeEventSubscriptionInput1.Scope;
import org.opendaylight.yang.gen.v1.urn.sal.restconf.event.subscription.rev140708.CreateDataChangeEventSubscriptionInput1Builder;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.opendaylight.yangtools.yang.common.OperationFailedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class PortStatusUpdateInitializer {
    private static final Logger LOG = LoggerFactory.getLogger(PortStatusUpdateInitializer.class);

    private final NeutronNorthboundApiConfig cfg;
    private final SalRemoteService salRemoteService;
    private final JSONRestconfService jsonRestconfService;

    @Inject
    public PortStatusUpdateInitializer(SalRemoteService salRemoteService, JSONRestconfService jsonRestconfService,
                                       NeutronNorthboundApiConfig neutronNorthboundApiConfig) {
        this.cfg = neutronNorthboundApiConfig;
        this.salRemoteService = salRemoteService;
        this.jsonRestconfService = jsonRestconfService;

        boolean preRegister = cfg.isPreRegisterPortStatusWebsocket() == null || cfg.isPreRegisterPortStatusWebsocket();

        if (preRegister) {
            subscribeWebsocket();
        } else {
            LOG.info("PortStatusUpdateInitializer: Skipping pre-register of websockets");
        }
    }

    private void subscribeWebsocket() {
        dataChangeEventSubscription();
        streamSubscribe();
    }

    private void dataChangeEventSubscription() {
        try {
            String streamName = salRemoteService.createDataChangeEventSubscription(
                new CreateDataChangeEventSubscriptionInputBuilder()
                    .setPath(InstanceIdentifier.builder(Neutron.class).child(Ports.class).build())
                    .addAugmentation(CreateDataChangeEventSubscriptionInput1.class,
                        new CreateDataChangeEventSubscriptionInput1Builder()
                            .setDatastore(Datastore.OPERATIONAL)
                            .setScope(Scope.SUBTREE).build())
                    .build()).get().getResult().getStreamName();
            LOG.info("create-data-change-event-subscription returned {}", streamName);
        } catch (InterruptedException | ExecutionException e) {
            LOG.error("exception while calling create-data-change-event-subscription", e);
        }
    }

    private void streamSubscribe() {
        String identifier = "data-change-event-subscription/neutron:neutron/neutron:ports"
                                                                        + "/datastore=OPERATIONAL/scope=SUBTREE";
        MultivaluedHashMap<String, String> map = new MultivaluedHashMap<>();
        map.add("odl-leaf-nodes-only", "true");
        try {
            Optional<String> res = jsonRestconfService.subscribeToStream(identifier, map);
            LOG.info("subscribeToStream returned {}", res);
        } catch (OperationFailedException e) {
            LOG.error("exception while calling subscribeToStream", e);
        }
    }
}
