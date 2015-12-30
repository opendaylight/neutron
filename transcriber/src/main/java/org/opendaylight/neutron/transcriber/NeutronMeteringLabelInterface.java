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

import org.opendaylight.controller.sal.binding.api.BindingAwareBroker.ProviderContext;
import org.opendaylight.neutron.spi.INeutronMeteringLabelCRUD;
import org.opendaylight.neutron.spi.NeutronMeteringLabel;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.metering.rev150712.metering.labels.attributes.MeteringLabels;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.metering.rev150712.metering.labels.attributes.metering.labels.MeteringLabel;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.metering.rev150712.metering.labels.attributes.metering.labels.MeteringLabelBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.rev150712.Neutron;
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
        return exists(uuid);
    }

    @Override
    public NeutronMeteringLabel getNeutronMeteringLabel(String uuid) {
        return get(uuid);
    }

    @Override
    protected List<MeteringLabel> getDataObjectList(MeteringLabels labels) {
        return labels.getMeteringLabel();
    }

    @Override
    public List<NeutronMeteringLabel> getAllNeutronMeteringLabels() {
        return getAll();
    }

    @Override
    public boolean addNeutronMeteringLabel(NeutronMeteringLabel input) {
        return add(input);
    }

    @Override
    public boolean removeNeutronMeteringLabel(String uuid) {
        return remove(uuid);
    }

    @Override
    public boolean updateNeutronMeteringLabel(String uuid, NeutronMeteringLabel delta) {
        return update(uuid, delta);
    }

    @Override
    public boolean neutronMeteringLabelInUse(String netUUID) {
        return !exists(netUUID);
    }

    @Override
    protected InstanceIdentifier<MeteringLabel> createInstanceIdentifier(
            MeteringLabel item) {
        return InstanceIdentifier.create(Neutron.class)
            .child(MeteringLabels.class)
            .child(MeteringLabel.class,item.getKey());
    }

    @Override
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
        if (meteringLabel.getTenantID()!=null) {
            meteringLabelBuilder.setTenantId(toUuid(meteringLabel.getTenantID()));
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
        answer.setTenantID(label.getTenantId());
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
