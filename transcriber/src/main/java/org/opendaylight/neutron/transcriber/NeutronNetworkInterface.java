/*
 * Copyright (c) 2013, 2015 IBM Corporation and others.  All rights reserved.
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
import org.opendaylight.mdsal.binding.api.ReadOperations;
import org.opendaylight.mdsal.common.api.ReadFailedException;
import org.opendaylight.neutron.spi.INeutronNetworkCRUD;
import org.opendaylight.neutron.spi.NeutronNetwork;
import org.opendaylight.neutron.spi.NeutronNetworkSegment;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.l3.ext.rev150712.NetworkL3Extension;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.l3.ext.rev150712.NetworkL3ExtensionBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.mtu.ext.rev181114.NetworkMtuExtension;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.mtu.ext.rev181114.NetworkMtuExtensionBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.networks.rev150712.NetworkTypeBase;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.networks.rev150712.NetworkTypeFlat;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.networks.rev150712.NetworkTypeGre;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.networks.rev150712.NetworkTypeVlan;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.networks.rev150712.NetworkTypeVxlan;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.networks.rev150712.networks.attributes.Networks;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.networks.rev150712.networks.attributes.networks.Network;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.networks.rev150712.networks.attributes.networks.NetworkBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.networks.rev150712.networks.attributes.networks.NetworkKey;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.provider.ext.rev150712.NetworkProviderExtension;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.provider.ext.rev150712.NetworkProviderExtensionBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.provider.ext.rev150712.network.provider.extension.Segments;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.provider.ext.rev150712.network.provider.extension.SegmentsBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.qos.ext.rev160613.QosNetworkExtension;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.qos.ext.rev160613.QosNetworkExtensionBuilder;
import org.opendaylight.yangtools.yang.common.Uint16;
import org.opendaylight.yangtools.yang.common.Uint32;

@Singleton
@Service(classes = INeutronNetworkCRUD.class)
public final class NeutronNetworkInterface
        extends AbstractNeutronInterface<Network, Networks, NetworkKey, NeutronNetwork>
        implements INeutronNetworkCRUD {

    private static final ImmutableBiMap<Class<? extends NetworkTypeBase>,
            String> NETWORK_MAP = new ImmutableBiMap.Builder<Class<? extends NetworkTypeBase>, String>()
                    .put(NetworkTypeFlat.class, "flat").put(NetworkTypeGre.class, "gre")
                    .put(NetworkTypeVlan.class, "vlan").put(NetworkTypeVxlan.class, "vxlan").build();

    private final NeutronQosPolicyInterface qosPolicyInterface;

    @Inject
    public NeutronNetworkInterface(DataBroker db, NeutronQosPolicyInterface qosPolicyInterface) {
        super(NetworkBuilder.class, db);
        this.qosPolicyInterface = qosPolicyInterface;
    }

    // IfNBNetworkCRUD methods
    @Override
    protected Collection<Network> getDataObjectList(Networks networks) {
        return networks.nonnullNetwork().values();
    }

    @Override
    protected NeutronNetwork fromMd(Network network) {
        final NeutronNetwork result = new NeutronNetwork();
        result.initDefaults();
        fromMdBaseAttributes(network, result);
        fromMdAdminAttributes(network, result);
        result.setShared(network.getShared());
        result.setVlanTransparent(network.getVlanTransparent());

        final NetworkL3Extension l3Extension = network.augmentation(NetworkL3Extension.class);
        result.setRouterExternal(l3Extension.getExternal());

        final NetworkProviderExtension providerExtension = network.augmentation(NetworkProviderExtension.class);
        result.setProviderPhysicalNetwork(providerExtension.getPhysicalNetwork());
        result.setProviderSegmentationID(providerExtension.getSegmentationId());
        result.setProviderNetworkType(NETWORK_MAP.get(providerExtension.getNetworkType()));
        final List<NeutronNetworkSegment> segments = new ArrayList<>();
        for (final Segments segment : providerExtension.nonnullSegments().values()) {
            final NeutronNetworkSegment neutronSegment = new NeutronNetworkSegment();
            neutronSegment.setProviderPhysicalNetwork(segment.getPhysicalNetwork());
            neutronSegment.setProviderSegmentationID(segment.getSegmentationId());
            neutronSegment.setProviderNetworkType(NETWORK_MAP.get(segment.getNetworkType()));
            segments.add(neutronSegment);
        }
        final QosNetworkExtension qos = network.augmentation(QosNetworkExtension.class);
        if (qos != null && qos.getQosPolicyId() != null) {
            result.setQosPolicyId(qos.getQosPolicyId().getValue());
        }
        final NetworkMtuExtension mtu = network.augmentation(NetworkMtuExtension.class);
        if (mtu != null && mtu.getMtu() != null) {
            result.setMtu(mtu.getMtu().toJava());
        }
        result.setSegments(segments);
        return result;
    }

    private void fillExtensions(NetworkBuilder networkBuilder, NeutronNetwork network) {
        final NetworkL3ExtensionBuilder l3ExtensionBuilder = new NetworkL3ExtensionBuilder();
        if (network.getRouterExternal() != null) {
            l3ExtensionBuilder.setExternal(network.getRouterExternal());
        }

        final NetworkProviderExtensionBuilder providerExtensionBuilder = new NetworkProviderExtensionBuilder();
        if (network.getProviderPhysicalNetwork() != null) {
            providerExtensionBuilder.setPhysicalNetwork(network.getProviderPhysicalNetwork());
        }
        if (network.getProviderSegmentationID() != null) {
            providerExtensionBuilder.setSegmentationId(network.getProviderSegmentationID());
        }
        if (network.getProviderNetworkType() != null) {
            final ImmutableBiMap<String, Class<? extends NetworkTypeBase>> mapper = NETWORK_MAP.inverse();
            providerExtensionBuilder
                    .setNetworkType(mapper.get(network.getProviderNetworkType()));
        }
        if (network.getSegments() != null) {
            final List<Segments> segments = new ArrayList<>();
            long count = 0;
            for (final NeutronNetworkSegment segment : network.getSegments()) {
                count++;
                final SegmentsBuilder segmentsBuilder = new SegmentsBuilder();
                if (segment.getProviderPhysicalNetwork() != null) {
                    segmentsBuilder.setPhysicalNetwork(segment.getProviderPhysicalNetwork());
                }
                if (segment.getProviderSegmentationID() != null) {
                    segmentsBuilder.setSegmentationId(segment.getProviderSegmentationID());
                }
                if (segment.getProviderNetworkType() != null) {
                    final ImmutableBiMap<String, Class<? extends NetworkTypeBase>> mapper = NETWORK_MAP.inverse();
                    segmentsBuilder.setNetworkType(
                            mapper.get(segment.getProviderNetworkType()));
                }
                segmentsBuilder.setSegmentationIndex(Uint32.valueOf(count));
                segments.add(segmentsBuilder.build());
            }
            providerExtensionBuilder.setSegments(segments);
        }
        if (network.getProviderSegmentationID() != null) {
            providerExtensionBuilder.setSegmentationId(network.getProviderSegmentationID());
        }
        if (network.getQosPolicyId() != null) {
            networkBuilder.addAugmentation(new QosNetworkExtensionBuilder()
                .setQosPolicyId(toUuid(network.getQosPolicyId()))
                .build());
        }
        networkBuilder.addAugmentation(l3ExtensionBuilder.build());
        networkBuilder.addAugmentation(providerExtensionBuilder.build());
        if (network.getMtu() != null) {
            networkBuilder.addAugmentation(new NetworkMtuExtensionBuilder()
                .setMtu(Uint16.valueOf(network.getMtu()))
                .build());
        }
    }

    @Override
    protected Network toMd(NeutronNetwork network) {
        final NetworkBuilder networkBuilder = new NetworkBuilder();
        toMdBaseAttributes(network, networkBuilder);
        toMdAdminAttributes(network, networkBuilder);
        fillExtensions(networkBuilder, network);

        if (network.getShared() != null) {
            networkBuilder.setShared(network.getShared());
        }

        if (network.getVlanTransparent() != null) {
            networkBuilder.setVlanTransparent(network.getVlanTransparent());
        }

        return networkBuilder.build();
    }

    @Override
    protected boolean areAllDependenciesAvailable(ReadOperations tx, NeutronNetwork network)
            throws ReadFailedException {
        return ifNonNull(network.getQosPolicyId(), qosPolicyId -> qosPolicyInterface.exists(qosPolicyId, tx));
    }
}
