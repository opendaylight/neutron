/*
 * Copyright (c) 2017 Ericsson India Global Services Pvt Ltd. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.neutron.transcriber;

import com.google.common.collect.ImmutableBiMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.apache.aries.blueprint.annotation.service.Service;
import org.opendaylight.mdsal.binding.api.DataBroker;
import org.opendaylight.neutron.spi.INeutronTrunkCRUD;
import org.opendaylight.neutron.spi.NeutronTrunk;
import org.opendaylight.neutron.spi.NeutronTrunkSubPort;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.networks.rev150712.NetworkTypeBase;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.networks.rev150712.NetworkTypeFlat;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.networks.rev150712.NetworkTypeGre;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.networks.rev150712.NetworkTypeVlan;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.networks.rev150712.NetworkTypeVxlan;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.trunks.rev170118.trunk.attributes.SubPorts;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.trunks.rev170118.trunk.attributes.SubPortsBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.trunks.rev170118.trunks.attributes.Trunks;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.trunks.rev170118.trunks.attributes.trunks.Trunk;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.trunks.rev170118.trunks.attributes.trunks.TrunkBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.trunks.rev170118.trunks.attributes.trunks.TrunkKey;
import org.opendaylight.yangtools.yang.binding.util.BindingMap;
import org.opendaylight.yangtools.yang.common.Uint32;

@Singleton
@Service(classes = INeutronTrunkCRUD.class)
public final class NeutronTrunkInterface extends AbstractNeutronInterface<Trunk, Trunks, TrunkKey, NeutronTrunk>
        implements INeutronTrunkCRUD {

    private static final ImmutableBiMap<Class<? extends NetworkTypeBase>, String> NETWORK_TYPE_MAP =
            new ImmutableBiMap.Builder<Class<? extends NetworkTypeBase>, String>()
            .put(NetworkTypeFlat.class, "flat").put(NetworkTypeGre.class, "gre")
            .put(NetworkTypeVlan.class, "vlan").put(NetworkTypeVxlan.class, "vxlan").build();

    @Inject
    public NeutronTrunkInterface(DataBroker db) {
        super(TrunkBuilder.class, db);
    }

    @Override
    protected Collection<Trunk> getDataObjectList(Trunks trunks) {
        return trunks.nonnullTrunk().values();
    }

    @Override
    protected NeutronTrunk fromMd(Trunk trunk) {
        final NeutronTrunk result = new NeutronTrunk();
        fromMdBaseAttributes(trunk, result);
        fromMdAdminAttributes(trunk, result);
        if (trunk.getPortId() != null) {
            result.setPortId(trunk.getPortId().getValue());
        }
        if (trunk.getSubPorts() != null) {
            final List<NeutronTrunkSubPort> subPortList = new ArrayList<>();
            for (final SubPorts subPort : trunk.getSubPorts().values()) {
                NeutronTrunkSubPort trunkSubPort = new NeutronTrunkSubPort();
                trunkSubPort.setPortId(subPort.getPortId().getValue());
                trunkSubPort.setSegmentationType(NETWORK_TYPE_MAP.get(subPort.getSegmentationType()));
                trunkSubPort.setSegmentationId(subPort.getSegmentationId().toString());
                subPortList.add(trunkSubPort);
            }
            result.setSubPorts(subPortList);
        }
        return result;
    }

    @Override
    protected Trunk toMd(NeutronTrunk trunk) {
        final TrunkBuilder trunkBuilder = new TrunkBuilder();
        toMdBaseAttributes(trunk, trunkBuilder);
        toMdAdminAttributes(trunk, trunkBuilder);
        if (trunk.getPortId() != null) {
            trunkBuilder.setPortId(toUuid(trunk.getPortId()));
        }
        if (trunk.getSubPorts() != null) {
            final ImmutableBiMap<String, Class<? extends NetworkTypeBase>> mapper = NETWORK_TYPE_MAP.inverse();
            trunkBuilder.setSubPorts(trunk.getSubPorts().stream()
                .map(subPort -> new SubPortsBuilder()
                    .setPortId(toUuid(subPort.getPortId()))
                    .setSegmentationType(mapper.get(subPort.getSegmentationType()))
                    .setSegmentationId(Uint32.valueOf(subPort.getSegmentationId()))
                    .build())
                .collect(BindingMap.toOrderedMap()));
        }
        return trunkBuilder.build();
    }
}
