/*
 * Copyright (c) 2018 Red Hat, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.neutron.northbound.impl;

import java.util.Optional;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.core.MultivaluedHashMap;
import org.opendaylight.netconf.sal.restconf.api.JSONRestconfService;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.northbound.api.config.rev181024.NeutronNorthboundApiConfig;
import org.opendaylight.yangtools.yang.common.OperationFailedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class PortStatusUpdateInitializer {
    private static final Logger LOG = LoggerFactory.getLogger(PortStatusUpdateInitializer.class);

    private static final String SUBSCRIBE_JSON = "{\n"
            + "  \"input\": {\n"
            + "    \"path\": \"/neutron:neutron/neutron:ports\", \n"
            + "    \"sal-remote-augment:notification-output-type\": \"JSON\", \n"
            + "    \"sal-remote-augment:datastore\": \"OPERATIONAL\", \n"
            + "    \"sal-remote-augment:scope\": \"SUBTREE\"\n"
            + "  }\n"
            + "}\n";

    private final NeutronNorthboundApiConfig cfg;
    private final JSONRestconfService jsonRestconfService;

    @Inject
    public PortStatusUpdateInitializer(final JSONRestconfService jsonRestconfService,
                                       final NeutronNorthboundApiConfig neutronNorthboundApiConfig) {
        this.cfg = neutronNorthboundApiConfig;
        this.jsonRestconfService = jsonRestconfService;

        if (!Boolean.FALSE.equals(cfg.getPreRegisterPortStatusWebsocket())) {
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
            Optional<String> res = jsonRestconfService.invokeRpc(
                    "sal-remote:create-data-change-event-subscription", Optional.of(SUBSCRIBE_JSON));
            LOG.info("create-data-change-event-subscription returned {}", res);
        } catch (OperationFailedException e) {
            LOG.error("exception while calling create-data-change-event-subscription", e);
        }
    }

    private void streamSubscribe() {
        String identifier = "data-change-event-subscription/neutron:neutron/neutron:ports"
                                                                        + "/datastore=OPERATIONAL/scope=SUBTREE";
        MultivaluedHashMap<String, String> map = new MultivaluedHashMap<>();
        map.add("odl-leaf-nodes-only", "true");
        Optional<String> res = null;
        try {
            res = jsonRestconfService.subscribeToStream(identifier, map);
            LOG.info("subscribeToStream returned {}", res);
        } catch (OperationFailedException e) {
            LOG.error("exception while calling subscribeToStream", e);
        }
    }
}
