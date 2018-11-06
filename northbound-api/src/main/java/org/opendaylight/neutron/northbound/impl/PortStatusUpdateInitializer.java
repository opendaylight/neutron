/*
 * Copyright (c) 2018 Red Hat, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.neutron.northbound.impl;

import com.google.common.base.Optional;
import java.time.Duration;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.opendaylight.netconf.sal.restconf.api.JSONRestconfService;
import org.opendaylight.neutron.northbound.impl.Retrier.RetryExhaustedException;
import org.opendaylight.restconf.common.util.MultivaluedHashMap;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.northbound.api.config.rev181024.NeutronNorthboundApiConfig;
import org.opendaylight.yangtools.yang.common.OperationFailedException;
import org.ops4j.pax.cdi.api.OsgiServiceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
@OsgiServiceProvider(classes = PortStatusUpdateInitializer.class)
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
    private final Retrier<OperationFailedException> retrier
        = new Retrier<>(OperationFailedException.class, 20, Duration.ofSeconds(1));

    @Inject
    public PortStatusUpdateInitializer(JSONRestconfService jsonRestconfService,
            NeutronNorthboundApiConfig neutronNorthboundApiConfig)
            throws RetryExhaustedException, InterruptedException {
        this.cfg = neutronNorthboundApiConfig;
        this.jsonRestconfService = jsonRestconfService;

        boolean preRegister = cfg.isPreRegisterPortStatusWebsocket() == null || cfg.isPreRegisterPortStatusWebsocket();

        if (!preRegister) {
            LOG.info("PortStatusUpdateInitializer: Skipping pre-register of websockets");
            return;
        }

        // In stable/oxygen the restconf bundle comes alive before it is fully wired. This results
        // in failed registration attempts. Retrying works.
        retrier.runWithRetries(() -> subscribeWebsocket());
    }

    private void subscribeWebsocket() throws OperationFailedException {
        dataChangeEventSubscription();
        streamSubscribe();
    }

    private void dataChangeEventSubscription() throws OperationFailedException {
        Optional<String> res = jsonRestconfService.invokeRpc(
                "sal-remote:create-data-change-event-subscription", Optional.of(SUBSCRIBE_JSON));
        LOG.info("create-data-change-event-subscription returned {}", res.toString());
    }

    private void streamSubscribe() throws OperationFailedException {
        String identifier = "data-change-event-subscription/neutron:neutron/neutron:ports"
                                                                        + "/datastore=OPERATIONAL/scope=SUBTREE";
        MultivaluedHashMap<String, String> map = new MultivaluedHashMap<>();
        map.add("odl-leaf-nodes-only", "true");

        Optional<String> res = jsonRestconfService.subscribeToStream(identifier, map);
        LOG.info("subscribeToStream returned {}", res.toString());
    }
}
