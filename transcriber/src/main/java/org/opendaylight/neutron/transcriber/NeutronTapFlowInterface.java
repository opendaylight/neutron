/*
 * Copyright (c) 2017 Intel Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.neutron.transcriber;

import com.google.common.collect.ImmutableBiMap;
import com.google.common.util.concurrent.CheckedFuture;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.controller.md.sal.binding.api.WriteTransaction;
import org.opendaylight.controller.md.sal.common.api.data.LogicalDatastoreType;
import org.opendaylight.controller.md.sal.common.api.data.ReadFailedException;
import org.opendaylight.controller.md.sal.common.api.data.TransactionCommitFailedException;
import org.opendaylight.neutron.spi.INeutronTapFlowCRUD;
import org.opendaylight.neutron.spi.NeutronTapFlow;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.rev150712.Neutron;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.tapaas.rev171024.DirectionBase;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.tapaas.rev171024.DirectionBoth;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.tapaas.rev171024.DirectionIn;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.tapaas.rev171024.DirectionOut;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.tapaas.rev171024.TapServiceAttributes;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.tapaas.rev171024.tap.service.attributes.TapFlows;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.tapaas.rev171024.tap.service.attributes.tap.flows.TapFlow;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.tapaas.rev171024.tap.service.attributes.tap.flows.TapFlowBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.tapaas.rev171024.tap.service.attributes.tap.flows.TapFlowKey;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.tapaas.rev171024.tap.services.attributes.TapServices;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.tapaas.rev171024.tap.services.attributes.tap.services.TapService;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.tapaas.rev171024.tap.services.attributes.tap.services.TapServiceKey;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.ops4j.pax.cdi.api.OsgiServiceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
@OsgiServiceProvider(classes = INeutronTapFlowCRUD.class)
public final class NeutronTapFlowInterface
        extends AbstractTranscriberInterface<TapFlow, TapFlows, TapFlowKey, NeutronTapFlow, TapServiceAttributes>
        implements INeutronTapFlowCRUD {

    private static final Logger LOG = LoggerFactory.getLogger(NeutronTapFlowInterface.class);

    private static final ImmutableBiMap<Class<? extends DirectionBase>,
            String> DIRECTION_MAP = new ImmutableBiMap.Builder<Class<? extends DirectionBase>, String>()
                    .put(DirectionOut.class, "OUT")
                    .put(DirectionIn.class, "IN")
                    .put(DirectionBoth.class, "BOTH").build();

    @Inject
    public NeutronTapFlowInterface(DataBroker db) {
        super(TapFlowBuilder.class, db);
    }

    protected InstanceIdentifier<TapFlow> createTapFlowInstanceIdentifier(String tapServiceUUID, TapFlow item) {
        return InstanceIdentifier.create(Neutron.class)
                .child(TapServices.class)
                .child(TapService.class, new TapServiceKey(toUuid(tapServiceUUID)))
                .child(TapFlows.class).child(TapFlow.class, item.key());
    }

    @Override
    protected List<TapFlow> getDataObjectList(TapFlows flows) {
        return flows.getTapFlow();
    }

    @Override
    protected NeutronTapFlow fromMd(TapFlow flow) {
        final NeutronTapFlow answer = new NeutronTapFlow();
        fromMdBaseAttributes(flow, answer);
        if (flow.getTapServiceId() != null) {
            answer.setTapFlowServiceID(flow.getTapServiceId().getValue());
        }
        if (flow.getSourcePort() != null) {
            answer.setTapFlowSourcePort(flow.getSourcePort().getValue());
        }
        if (flow.getDirection() != null) {
            answer.setTapFlowDirection(DIRECTION_MAP.get(flow.getDirection()));
        }

        return answer;
    }

    @Override
    protected TapFlow toMd(NeutronTapFlow flow) {
        final TapFlowBuilder flowBuilder = new TapFlowBuilder();
        toMdBaseAttributes(flow, flowBuilder);
        if (flow.getTapFlowServiceID() != null) {
            flowBuilder.setTapServiceId(toUuid(flow.getTapFlowServiceID()));
        }
        if (flow.getTapFlowSourcePort() != null) {
            flowBuilder.setSourcePort(toUuid(flow.getTapFlowSourcePort()));
        }
        if (flow.getTapFlowDirection() != null) {
            final ImmutableBiMap<String, Class<? extends DirectionBase>> mapper = DIRECTION_MAP.inverse();
            flowBuilder.setDirection(mapper.get(flow.getTapFlowDirection()));
        }

        return flowBuilder.build();
    }

    @Override
    public boolean tapFlowExists(String tapServiceUUID, String tapFlowUUID) throws ReadFailedException {
        final TapFlow dataObject = readMd(createTapFlowInstanceIdentifier(tapServiceUUID, toMd(tapFlowUUID)));
        return dataObject != null;
    }

    private boolean tapServiceExists(String tapServiceUUID) throws ReadFailedException {
        final TapService tapService = readMd(InstanceIdentifier.create(Neutron.class).child(TapServices.class)
                                        .child(TapService.class, new TapServiceKey(toUuid(tapServiceUUID))));
        return tapService != null;
    }


    private boolean updateTapFlowMd(NeutronTapFlow tapFlow) {
        final WriteTransaction transaction = getDataBroker().newWriteOnlyTransaction();
        final TapFlow item = toMd(tapFlow);
        final InstanceIdentifier<TapFlow> iid = createTapFlowInstanceIdentifier(tapFlow.getTapFlowServiceID(), item);
        transaction.put(LogicalDatastoreType.CONFIGURATION, iid, item, true);
        final CheckedFuture<Void, TransactionCommitFailedException> future = transaction.submit();
        try {
            future.get();
        } catch (InterruptedException | ExecutionException e) {
            LOG.warn("Transaction Failed ", e);
            return false;
        }
        return true;
    }

    private boolean removeTapFlowMd(String tapServiceUUID, String tapFlowUUID) {
        final WriteTransaction transaction = getDataBroker().newWriteOnlyTransaction();
        final InstanceIdentifier<TapFlow> iid = createTapFlowInstanceIdentifier(tapServiceUUID, toMd(tapFlowUUID));
        transaction.delete(LogicalDatastoreType.CONFIGURATION, iid);
        final CheckedFuture<Void, TransactionCommitFailedException> future = transaction.submit();
        try {
            future.get();
        } catch (InterruptedException | ExecutionException e) {
            LOG.warn("Transation failed ", e);
            return false;
        }
        return true;
    }

    private boolean addTapFlowMd(NeutronTapFlow tapFlow) {
        return updateTapFlowMd(tapFlow);
    }

    @Override
    public boolean updateTapFlow(NeutronTapFlow tapFlow) {
        return updateTapFlowMd(tapFlow);
    }

    @Override
    public boolean addTapFlow(NeutronTapFlow tapFlow) throws ReadFailedException {
        if (!tapServiceExists(tapFlow.getTapFlowServiceID())) {
            return false;
        }
        if (tapFlowExists(tapFlow.getTapFlowServiceID(), tapFlow.getID())) {
            return false;
        }
        addTapFlowMd(tapFlow);
        return true;
    }

    @Override
    public NeutronTapFlow getTapFlow(String tapServiceUUID, String tapFlowUUID) throws ReadFailedException {
        final TapFlow tapFlow = readMd(createTapFlowInstanceIdentifier(tapServiceUUID, toMd(tapFlowUUID)));
        if (tapFlow == null) {
            return null;
        }
        return fromMd(tapFlow);
    }

    @Override
    public boolean deleteTapFlow(String tapServiceUUID, String tapFlowUUID) {
        return removeTapFlowMd(tapServiceUUID, tapFlowUUID);
    }
}
