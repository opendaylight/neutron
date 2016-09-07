/*
 * Copyright (c) 2015 IBM Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.transcriber;

import java.util.List;
import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.neutron.spi.INeutronMeteringLabelCRUD;
import org.opendaylight.neutron.spi.NeutronMeteringLabel;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.metering.rev150712.metering.labels.attributes.MeteringLabels;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.metering.rev150712.metering.labels.attributes.metering.labels.MeteringLabel;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.metering.rev150712.metering.labels.attributes.metering.labels.MeteringLabelBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.rev150712.Neutron;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class NeutronMeteringLabelInterface
        extends AbstractNeutronInterface<MeteringLabel, MeteringLabels, NeutronMeteringLabel>
        implements INeutronMeteringLabelCRUD {
    private static final Logger LOGGER = LoggerFactory.getLogger(NeutronMeteringLabelInterface.class);

    NeutronMeteringLabelInterface(DataBroker db) {
        super(db);
    }

    // IfNBMeteringLabelCRUD methods
    @Override
    protected List<MeteringLabel> getDataObjectList(MeteringLabels labels) {
        return labels.getMeteringLabel();
    }

    @Override
    protected InstanceIdentifier<MeteringLabel> createInstanceIdentifier(MeteringLabel item) {
        return InstanceIdentifier.create(Neutron.class).child(MeteringLabels.class).child(MeteringLabel.class,
                item.getKey());
    }

    @Override
    protected InstanceIdentifier<MeteringLabels> createInstanceIdentifier() {
        return InstanceIdentifier.create(Neutron.class).child(MeteringLabels.class);
    }

    @Override
    protected MeteringLabel toMd(NeutronMeteringLabel meteringLabel) {
        final MeteringLabelBuilder meteringLabelBuilder = new MeteringLabelBuilder();
        if (meteringLabel.getMeteringLabelName() != null) {
            meteringLabelBuilder.setName(meteringLabel.getMeteringLabelName());
        }
        if (meteringLabel.getMeteringLabelShared() != null) {
            meteringLabelBuilder.setShared(meteringLabel.getMeteringLabelShared());
        }
        if (meteringLabel.getTenantID() != null) {
            meteringLabelBuilder.setTenantId(toUuid(meteringLabel.getTenantID()));
        }
        if (meteringLabel.getID() != null) {
            meteringLabelBuilder.setUuid(toUuid(meteringLabel.getID()));
        }
        return meteringLabelBuilder.build();
    }

    @Override
    protected MeteringLabel toMd(String uuid) {
        final MeteringLabelBuilder meteringLabelBuilder = new MeteringLabelBuilder();
        meteringLabelBuilder.setUuid(toUuid(uuid));
        return meteringLabelBuilder.build();
    }

    protected NeutronMeteringLabel fromMd(MeteringLabel label) {
        final NeutronMeteringLabel answer = new NeutronMeteringLabel();
        if (label.getName() != null) {
            answer.setMeteringLabelName(label.getName());
        }
        if (label.isShared() != null) {
            answer.setMeteringLabelShared(label.isShared());
        }
        if (label.getTenantId() != null) {
            answer.setTenantID(label.getTenantId());
        }
        if (label.getUuid() != null) {
            answer.setID(label.getUuid().getValue());
        }
        return answer;
    }
}
