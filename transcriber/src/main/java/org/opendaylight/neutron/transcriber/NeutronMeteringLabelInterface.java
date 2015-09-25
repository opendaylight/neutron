/*
 * Copyright (c) 2015 IBM Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.transcriber;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import com.google.common.base.Preconditions;
import org.opendaylight.controller.md.sal.binding.api.BindingTransactionChain;
import org.opendaylight.controller.sal.binding.api.BindingAwareBroker.ProviderContext;
import org.opendaylight.neutron.spi.INeutronMeteringLabelCRUD;
import org.opendaylight.neutron.spi.NeutronMeteringLabel;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.metering.rev141002.metering.labels.attributes.MeteringLabels;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.metering.rev141002.metering.labels.attributes.metering.labels.MeteringLabel;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.metering.rev141002.metering.labels.attributes.metering.labels.MeteringLabelBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.rev150325.Neutron;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NeutronMeteringLabelInterface extends AbstractNeutronInterface<MeteringLabel, MeteringLabels, NeutronMeteringLabel> implements INeutronMeteringLabelCRUD {
    private static final Logger LOGGER = LoggerFactory.getLogger(NeutronMeteringLabelInterface.class);

    NeutronMeteringLabelInterface(ProviderContext providerContext) {
        super(providerContext);
    }

    // IfNBMeteringLabelCRUD methods

    @Override
    public boolean neutronMeteringLabelExists(String uuid) {
        return exists(uuid, null);
    }

    @Override
    public NeutronMeteringLabel getNeutronMeteringLabel(String uuid) {
        return get(uuid, null);
    }

    @Override
    protected List<MeteringLabel> getDataObjectList(MeteringLabels labels) {
        return labels.getMeteringLabel();
    }

    @Override
    public List<NeutronMeteringLabel> getAllNeutronMeteringLabels() {
        return getAll(null);
    }

    @Override
    public boolean addNeutronMeteringLabel(NeutronMeteringLabel input) {
        return add(input, null);
    }

    @Override
    public boolean removeNeutronMeteringLabel(String uuid) {
        return remove(uuid, null);
    }

    @Override
    public boolean updateNeutronMeteringLabel(String uuid, NeutronMeteringLabel delta) {
        return update(uuid, delta, null);
    }

    @Override
    public boolean neutronMeteringLabelInUse(String netUUID) {
        return !exists(netUUID, null);
    }

    @Override
    protected InstanceIdentifier<MeteringLabel> createInstanceIdentifier(
            MeteringLabel item) {
        return InstanceIdentifier.create(Neutron.class)
            .child(MeteringLabels.class)
            .child(MeteringLabel.class,item.getKey());
    }

    protected InstanceIdentifier<MeteringLabels> createInstanceIdentifier() {
        return InstanceIdentifier.create(Neutron.class)
            .child(MeteringLabels.class);
    }

    @Override
    protected MeteringLabel toMd(NeutronMeteringLabel meteringLabel) {
        MeteringLabelBuilder meteringLabelBuilder = new MeteringLabelBuilder();
        if (meteringLabel.getMeteringLabelName()!=null) {
            meteringLabelBuilder.setName(meteringLabel.getMeteringLabelName());
        }
        if (meteringLabel.getMeteringLabelDescription()!=null) {
            meteringLabelBuilder.setDescription(meteringLabel.getMeteringLabelDescription());
        }
        if (meteringLabel.getMeteringLabelTenantID()!=null) {
            meteringLabelBuilder.setTenantId(toUuid(meteringLabel.getMeteringLabelTenantID()));
        }
        if (meteringLabel.getID()!=null) {
            meteringLabelBuilder.setUuid(toUuid(meteringLabel.getID()));
        }
        return meteringLabelBuilder.build();
    }

    protected NeutronMeteringLabel fromMd(MeteringLabel label) {
        NeutronMeteringLabel answer = new NeutronMeteringLabel();
        if (label.getName() != null) {
            answer.setMeteringLabelName(label.getName());
        }
        if (label.getDescription() != null) {
            answer.setMeteringLabelDescription(label.getName());
        }
//todo: remove '-' chars as tenant id doesn't use them
        if (label.getTenantId() != null) {
            answer.setMeteringLabelTenantID(label.getTenantId().getValue());
        }
        if (label.getUuid() != null) {
            answer.setID(label.getUuid().getValue());
        }
        return answer;
    }

    @Override
    protected MeteringLabel toMd(String uuid) {
        MeteringLabelBuilder meteringLabelBuilder = new MeteringLabelBuilder();
        meteringLabelBuilder.setUuid(toUuid(uuid));
        return meteringLabelBuilder.build();
    }

    public static void registerNewInterface(BundleContext context,
                                            ProviderContext providerContext,
                                            List<ServiceRegistration<?>> registrations) {
        NeutronMeteringLabelInterface neutronMeteringLabelInterface = new NeutronMeteringLabelInterface(providerContext);
        ServiceRegistration<INeutronMeteringLabelCRUD> neutronMeteringLabelInterfaceRegistration = context.registerService(INeutronMeteringLabelCRUD.class, neutronMeteringLabelInterface, null);
        if(neutronMeteringLabelInterfaceRegistration != null) {
            registrations.add(neutronMeteringLabelInterfaceRegistration);
        }
    }
}
