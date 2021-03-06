/*
 * Copyright (c) 2015 IBM Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.neutron.transcriber;

import java.util.Collection;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.apache.aries.blueprint.annotation.service.Service;
import org.opendaylight.mdsal.binding.api.DataBroker;
import org.opendaylight.neutron.spi.INeutronMeteringLabelCRUD;
import org.opendaylight.neutron.spi.NeutronMeteringLabel;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.metering.rev150712.metering.labels.attributes.MeteringLabels;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.metering.rev150712.metering.labels.attributes.metering.labels.MeteringLabel;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.metering.rev150712.metering.labels.attributes.metering.labels.MeteringLabelBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.metering.rev150712.metering.labels.attributes.metering.labels.MeteringLabelKey;

@Singleton
@Service(classes = INeutronMeteringLabelCRUD.class)
public final class NeutronMeteringLabelInterface
        extends AbstractNeutronInterface<MeteringLabel, MeteringLabels, MeteringLabelKey, NeutronMeteringLabel>
        implements INeutronMeteringLabelCRUD {

    @Inject
    public NeutronMeteringLabelInterface(DataBroker db) {
        super(MeteringLabelBuilder.class, db);
    }

    // IfNBMeteringLabelCRUD methods
    @Override
    protected Collection<MeteringLabel> getDataObjectList(MeteringLabels labels) {
        return labels.nonnullMeteringLabel().values();
    }

    @Override
    protected MeteringLabel toMd(NeutronMeteringLabel meteringLabel) {
        final MeteringLabelBuilder meteringLabelBuilder = new MeteringLabelBuilder();
        toMdBaseAttributes(meteringLabel, meteringLabelBuilder);
        if (meteringLabel.getMeteringLabelShared() != null) {
            meteringLabelBuilder.setShared(meteringLabel.getMeteringLabelShared());
        }
        return meteringLabelBuilder.build();
    }

    @Override
    protected NeutronMeteringLabel fromMd(MeteringLabel label) {
        final NeutronMeteringLabel answer = new NeutronMeteringLabel();
        fromMdBaseAttributes(label, answer);
        if (label.getShared() != null) {
            answer.setMeteringLabelShared(label.getShared());
        }
        return answer;
    }
}
