/*
 * Copyright (c) 2016 Brocade Communications Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.neutron.transcriber;

import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.neutron.spi.INeutronSFCPortPairGroupCRUD;
import org.opendaylight.neutron.spi.NeutronSFCPortPairGroup;
import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.yang.types.rev130715.Uuid;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.rev150712.Neutron;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.sfc.rev160511.sfc.attributes.PortPairGroups;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.sfc.rev160511.sfc.attributes.port.pair.groups.PortPairGroup;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.sfc.rev160511.sfc.attributes.port.pair.groups.PortPairGroupBuilder;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anil Vishnoi (avishnoi@Brocade.com)
 */
public class NeutronSFCPortPairGroupInterface
        extends AbstractNeutronInterface<PortPairGroup, PortPairGroups, NeutronSFCPortPairGroup>
        implements INeutronSFCPortPairGroupCRUD {

    private static final Logger LOGGER = LoggerFactory.getLogger(NeutronSFCPortPairGroupInterface.class);

    NeutronSFCPortPairGroupInterface(DataBroker db) {
        super(db);
    }

    @Override
    protected InstanceIdentifier<PortPairGroup> createInstanceIdentifier(PortPairGroup portPairGroup) {
        return InstanceIdentifier.create(Neutron.class).child(PortPairGroups.class)
                .child(PortPairGroup.class, portPairGroup.getKey());
    }

    @Override
    protected InstanceIdentifier<PortPairGroups> createInstanceIdentifier() {
        return InstanceIdentifier.create(Neutron.class).child(PortPairGroups.class);
    }

    @Override
    protected PortPairGroup toMd(NeutronSFCPortPairGroup neutronPortPairGroup) {

        LOGGER.trace("toMd: REST SFC Port Pair Group data : {}", neutronPortPairGroup);

        PortPairGroupBuilder result = new PortPairGroupBuilder();
        result.setUuid(new Uuid(neutronPortPairGroup.getID()));
        result.setName(neutronPortPairGroup.getName());
        result.setTenantId(toUuid(neutronPortPairGroup.getTenantID()));
        if (neutronPortPairGroup.getPortPairs() != null ) {
            List<Uuid> portPairs = new ArrayList<>();
            for(String ppUuid : neutronPortPairGroup.getPortPairs()) {
                portPairs.add(new Uuid(ppUuid));
            }
            result.setPortPairs(portPairs);
        }
        LOGGER.trace("toMd: Yang SFC Port Pair Group data : {}", result);
        return result.build();
    }

    @Override
    protected PortPairGroup toMd(String uuid) {
        final PortPairGroupBuilder portPairGroupBuilder = new PortPairGroupBuilder();
        portPairGroupBuilder.setUuid(toUuid(uuid));
        return portPairGroupBuilder.build();
    }

    @Override
    protected NeutronSFCPortPairGroup fromMd(PortPairGroup mdPortPairGroup) {
        LOGGER.trace("fromMd: Yang SFC Port Pair Group data : {}", mdPortPairGroup);
        NeutronSFCPortPairGroup result = new NeutronSFCPortPairGroup();
        result.setID(mdPortPairGroup.getUuid().getValue());
        result.setName(mdPortPairGroup.getName());
        result.setTenantID(mdPortPairGroup.getTenantId());
        if (mdPortPairGroup.getPortPairs() != null) {
            List<String> portPairsUUID = new ArrayList<>();
            for(Uuid uuid : mdPortPairGroup.getPortPairs()) {
                portPairsUUID.add(uuid.getValue());
            }
            result.setPortPairs(portPairsUUID);
        }
        LOGGER.trace("fromMd: REST SFC Port Pair Group data : {}", result);
        return result;
    }

    @Override
    protected List<PortPairGroup> getDataObjectList(PortPairGroups dataObjects) {
        return dataObjects.getPortPairGroup();
    }
}
