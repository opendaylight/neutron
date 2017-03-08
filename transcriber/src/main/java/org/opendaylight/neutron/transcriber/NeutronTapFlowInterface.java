/*
 * Copyright (c) 2014, 2015 Red Hat, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.transcriber;

import com.google.common.collect.ImmutableBiMap;
import java.util.List;
import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.neutron.spi.INeutronTapFlowCRUD;
import org.opendaylight.neutron.spi.NeutronTapFlow;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.tapaas.rev170308.DirectionBase;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.tapaas.rev170308.DirectionBoth;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.tapaas.rev170308.DirectionIn;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.tapaas.rev170308.DirectionOut;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.tapaas.rev170308.TapServiceAttributes;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.tapaas.rev170308.tap.service.attributes.TapFlows;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.tapaas.rev170308.tap.service.attributes.tap.flows.TapFlow;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.tapaas.rev170308.tap.service.attributes.tap.flows.TapFlowBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.tapaas.rev170308.tap.service.attributes.tap.flows.TapFlowKey;




public final class NeutronTapFlowInterface
        extends AbstractTranscriberInterface<TapFlow, TapFlows, TapFlowKey, NeutronTapFlow, TapServiceAttributes>
        implements INeutronTapFlowCRUD {

    private static final ImmutableBiMap<Class<? extends DirectionBase>,
            String> DIRECTION_MAP = new ImmutableBiMap.Builder<Class<? extends DirectionBase>, String>()
                    .put(DirectionOut.class, "OUT")
                    .put(DirectionIn.class, "IN")
                    .put(DirectionBoth.class, "BOTH").build();

    NeutronTapFlowInterface(DataBroker db) {
        super(TapFlowBuilder.class, db);
    }

    @Override
    protected List<TapFlow> getDataObjectList(TapFlows flows) {
        return flows.getTapFlow();
    }

    @Override
    protected NeutronTapFlow fromMd(TapFlow flow) {
        final NeutronTapFlow answer = new NeutronTapFlow();
        fromMdBaseAttributes(flow, answer);
        if (flow.getDirection() != null) {
            answer.setTapFlowDirection(DIRECTION_MAP.get(flow.getDirection()));
        }
        if (flow.getSourcePort() != null) {
            answer.setTapFlowSourcePort(flow.getSourcePort().getValue());
        }
        if (flow.getTapServiceId() != null) {
            answer.setTapFlowServiceID(flow.getTapServiceId().getValue());
        }
        return answer;
    }

    @Override
    protected TapFlow toMd(NeutronTapFlow flow) {
        final TapFlowBuilder flowBuilder = new TapFlowBuilder();
        toMdBaseAttributes(flow, flowBuilder);
        if (flow.getTapFlowDirection() != null) {
            final ImmutableBiMap<String, Class<? extends DirectionBase>> mapper = DIRECTION_MAP.inverse();
            flowBuilder.setDirection(mapper.get(flow.getTapFlowDirection()));
        }
        if (flow.getTapFlowSourcePort() != null) {
            flowBuilder.setSourcePort(toUuid(flow.getTapFlowSourcePort()));
        }
        if (flow.getTapFlowServiceID() != null) {
            flowBuilder.setTapServiceId(toUuid(flow.getTapFlowServiceID()));
        }

        return flowBuilder.build();
    }
}
