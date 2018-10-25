/*
 * Copyright (c) 2018 Red Hat, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.neutron.northbound.impl;

import java.net.URI;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.UriInfo;

import org.opendaylight.mdsal.dom.api.DOMSchemaService;
import org.opendaylight.netconf.sal.rest.api.RestconfService;
import org.opendaylight.restconf.common.context.InstanceIdentifierContext;
import org.opendaylight.restconf.common.context.NormalizedNodeContext;
import org.opendaylight.restconf.common.util.SimpleUriInfo;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.northbound.api.config.rev181024.NeutronNorthboundApiConfig;
import org.opendaylight.yangtools.yang.common.QName;
import org.opendaylight.yangtools.yang.common.QNameModule;
import org.opendaylight.yangtools.yang.common.Revision;
import org.opendaylight.yangtools.yang.data.api.YangInstanceIdentifier;
import org.opendaylight.yangtools.yang.data.api.YangInstanceIdentifier.AugmentationIdentifier;
import org.opendaylight.yangtools.yang.data.api.YangInstanceIdentifier.NodeIdentifier;
import org.opendaylight.yangtools.yang.data.api.schema.AugmentationNode;
import org.opendaylight.yangtools.yang.data.api.schema.LeafNode;
import org.opendaylight.yangtools.yang.data.api.schema.NormalizedNode;
import org.opendaylight.yangtools.yang.data.impl.schema.Builders;
import org.opendaylight.yangtools.yang.model.api.RpcDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class PortStatusUpdateInitializer {
    private static final Logger LOG = LoggerFactory.getLogger(PortStatusUpdateInitializer.class);

    private final RestconfService restconfService;
    private final DOMSchemaService schemaService;
    private final NeutronNorthboundApiConfig cfg;

    @Inject
    public PortStatusUpdateInitializer(final RestconfService restconfService,
                                       final DOMSchemaService schemaService,
                                       final NeutronNorthboundApiConfig neutronNorthboundApiConfig) {
        this.restconfService = restconfService;
        this.schemaService = schemaService;
        this.cfg = neutronNorthboundApiConfig;

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
        RpcDefinition rpcDef = getSubscribeRpcDef();
        NormalizedNode salRemoteSubscribeParams = getSalRemoteSubscribeParams();
        UriInfo uriInfo = new SimpleUriInfo(
                "/restconf/operations/sal-remote:create-data-change-event-subscription");
        InstanceIdentifierContext iidCtxt = new InstanceIdentifierContext(
                null, rpcDef, null, null);
        NormalizedNodeContext nnCtxt = new NormalizedNodeContext(iidCtxt, salRemoteSubscribeParams);
        NormalizedNodeContext res = restconfService.invokeRpc(
                "sal-remote:create-data-change-event-subscription", nnCtxt, uriInfo);
        LOG.info("PortStatusUpdateInitializer call to create-data-change-event-subscription returned {}",
                res.getData());
    }

    private void streamSubscribe() {
        boolean isSsl = cfg.isWebsocketUsesSsl() == null || cfg.isWebsocketUsesSsl();
        //Note: we use 127.0.0.1 here because the IP address makes no difference but is required
        String uri = (isSsl ? "https" : "http") + "://127.0.0.1:8081/restconf/streams/stream/"
                + "data-change-event-subscription/neutron:neutron/neutron:ports/datastore=OPERATIONAL/scope=SUBTREE";
        MultivaluedHashMap map = new MultivaluedHashMap<String, String>();
        map.add("odl-leaf-nodes-only", "true");
        UriInfo uriInfo = new SimpleUriInfo(uri, map);

        NormalizedNodeContext res = restconfService.subscribeToStream(
                "data-change-event-subscription/neutron:neutron/neutron:ports/datastore=OPERATIONAL/scope=SUBTREE",
                uriInfo);

        LOG.info("PortStatusUpdateInitializer call to subscribeToStream returned {}", res.getData());
        if (res.getNewHeaders().get("Location") == null) {
            LOG.error(
                "PortStatusUpdateInitializer call to subscribeToStream did not return location, subsciption failed");
        }
    }

    private RpcDefinition getSubscribeRpcDef() {
        for (RpcDefinition rpc : schemaService.getGlobalContext().getOperations()) {
            if (rpc.getQName().equals(QName.create("(urn:opendaylight:params:xml:ns:yang:controller:md:sal:remote?"
                    + "revision=2014-01-14)create-data-change-event-subscription"))) {
                return rpc;
            }
        }

        return null;
    }

    private NormalizedNode getSalRemoteSubscribeParams() {
        QNameModule salRemoteMod = QNameModule.create(URI.create(
                "urn:opendaylight:params:xml:ns:yang:controller:md:sal:remote"), Revision.of("2014-01-14"));
        QNameModule eventSubMod = QNameModule.create(
                URI.create("urn:sal:restconf:event:subscription"), Revision.of("2014-07-08"));
        QNameModule neutronMod = QNameModule.create(URI.create("urn:opendaylight:neutron"), Revision.of("2015-07-12"));

        QName neutronQName = QName.create(neutronMod, "neutron");
        QName portsQName = QName.create(neutronMod, "ports");

        QName inputQName = QName.create(salRemoteMod, "input");
        QName pathQName = QName.create(salRemoteMod, "path");


        QName datastoreQName = QName.create(eventSubMod, "datastore");
        QName typeQName = QName.create(eventSubMod, "notification-output-type");
        QName scopeQName = QName.create(eventSubMod, "scope");

        LeafNode datastore = Builders.leafBuilder()
                .withNodeIdentifier(NodeIdentifier.create(datastoreQName))
                .withValue("OPERATIONAL")
                .build();
        LeafNode type = Builders.leafBuilder()
                .withNodeIdentifier(NodeIdentifier.create(typeQName))
                .withValue("JSON")
                .build();
        LeafNode scope = Builders.leafBuilder()
                .withNodeIdentifier(NodeIdentifier.create(scopeQName))
                .withValue("SUBTREE")
                .build();
        AugmentationNode aug = Builders.augmentationBuilder()
                .withNodeIdentifier(new AugmentationIdentifier(Stream.of(
                        datastoreQName, typeQName, scopeQName).collect(Collectors.toSet())))
                .withChild(datastore)
                .withChild(type)
                .withChild(scope)
                .build();

        LeafNode path = Builders.leafBuilder()
                .withNodeIdentifier(NodeIdentifier.create(pathQName))
                .withValue(YangInstanceIdentifier.of(neutronQName).node(portsQName))
                .build();

        return Builders.containerBuilder()
                .withNodeIdentifier(NodeIdentifier.create(inputQName))
                .withChild(path)
                .withChild(aug)
                .build();
    }
}
