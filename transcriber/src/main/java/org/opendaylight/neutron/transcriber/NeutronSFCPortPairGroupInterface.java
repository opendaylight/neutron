/*
 * Copyright (c) 2016 Brocade Communications Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.neutron.transcriber;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.neutron.spi.INeutronSFCPortPairGroupCRUD;
import org.opendaylight.neutron.spi.NeutronSFCPortPairGroup;
import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.yang.types.rev130715.Uuid;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.sfc.rev160511.sfc.attributes.PortPairGroups;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.sfc.rev160511.sfc.attributes.port.pair.groups.PortPairGroup;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.sfc.rev160511.sfc.attributes.port.pair.groups.PortPairGroupBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.sfc.rev160511.sfc.attributes.port.pair.groups.PortPairGroupKey;
import org.ops4j.pax.cdi.api.OsgiServiceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Anil Vishnoi (avishnoi@Brocade.com).
 */
@Singleton
@OsgiServiceProvider(classes = INeutronSFCPortPairGroupCRUD.class)
public final class NeutronSFCPortPairGroupInterface
        extends AbstractNeutronInterface<PortPairGroup, PortPairGroups, PortPairGroupKey, NeutronSFCPortPairGroup>
        implements INeutronSFCPortPairGroupCRUD {

    private static final Logger LOG = LoggerFactory.getLogger(NeutronSFCPortPairGroupInterface.class);

    @Inject
    public NeutronSFCPortPairGroupInterface(DataBroker db) {
        super(PortPairGroupBuilder.class, db);
    }

    @Override
    protected PortPairGroup toMd(NeutronSFCPortPairGroup neutronPortPairGroup) {

        LOG.trace("toMd: REST SFC Port Pair Group data : {}", neutronPortPairGroup);

        PortPairGroupBuilder result = new PortPairGroupBuilder();
        toMdBaseAttributes(neutronPortPairGroup, result);
        if (neutronPortPairGroup.getPortPairs() != null) {
            List<Uuid> portPairs = new ArrayList<>();
            for (String ppUuid : neutronPortPairGroup.getPortPairs()) {
                portPairs.add(new Uuid(ppUuid));
            }
            result.setPortPairs(portPairs);
        }
        LOG.trace("toMd: Yang SFC Port Pair Group data : {}", result);
        return result.build();
    }

    @Override
    protected NeutronSFCPortPairGroup fromMd(PortPairGroup mdPortPairGroup) {
        LOG.trace("fromMd: Yang SFC Port Pair Group data : {}", mdPortPairGroup);
        NeutronSFCPortPairGroup result = new NeutronSFCPortPairGroup();
        fromMdBaseAttributes(mdPortPairGroup, result);
        if (mdPortPairGroup.getPortPairs() != null) {
            List<String> portPairsUUID = new ArrayList<>();
            for (Uuid uuid : mdPortPairGroup.getPortPairs()) {
                portPairsUUID.add(uuid.getValue());
            }
            result.setPortPairs(portPairsUUID);
        }
        LOG.trace("fromMd: REST SFC Port Pair Group data : {}", result);
        return result;
    }

    @Override
    protected List<PortPairGroup> getDataObjectList(PortPairGroups dataObjects) {
        return dataObjects.getPortPairGroup();
    }
}
