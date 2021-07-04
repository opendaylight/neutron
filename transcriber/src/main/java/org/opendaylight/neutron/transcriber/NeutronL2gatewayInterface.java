/*
 * Copyright (c) 2015 Hewlett-Packard Development Company, L.P. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.neutron.transcriber;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.apache.aries.blueprint.annotation.service.Service;
import org.opendaylight.mdsal.binding.api.DataBroker;
import org.opendaylight.neutron.spi.INeutronL2gatewayCRUD;
import org.opendaylight.neutron.spi.NeutronL2gateway;
import org.opendaylight.neutron.spi.NeutronL2gatewayDevice;
import org.opendaylight.neutron.spi.NeutronL2gatewayDeviceInterface;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.l2gateways.rev150712.l2gateway.attributes.Devices;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.l2gateways.rev150712.l2gateway.attributes.DevicesBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.l2gateways.rev150712.l2gateway.attributes.devices.Interfaces;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.l2gateways.rev150712.l2gateway.attributes.devices.InterfacesBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.l2gateways.rev150712.l2gateways.attributes.L2gateways;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.l2gateways.rev150712.l2gateways.attributes.l2gateways.L2gateway;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.l2gateways.rev150712.l2gateways.attributes.l2gateways.L2gatewayBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.l2gateways.rev150712.l2gateways.attributes.l2gateways.L2gatewayKey;
import org.opendaylight.yangtools.yang.binding.util.BindingMap;

@Singleton
@Service(classes = INeutronL2gatewayCRUD.class)
public final class NeutronL2gatewayInterface
        extends AbstractNeutronInterface<L2gateway, L2gateways, L2gatewayKey, NeutronL2gateway>
        implements INeutronL2gatewayCRUD {

    @Inject
    public NeutronL2gatewayInterface(DataBroker db) {
        super(L2gatewayBuilder.class, db);
    }

    @Override
    protected Collection<L2gateway> getDataObjectList(L2gateways l2gateways) {
        return l2gateways.nonnullL2gateway().values();
    }

    @Override
    protected NeutronL2gateway fromMd(L2gateway l2gateway) {
        final NeutronL2gateway result = new NeutronL2gateway();
        fromMdBaseAttributes(l2gateway, result);
        final List<NeutronL2gatewayDevice> neutronL2gatewayDevices = new ArrayList<>();

        for (final Devices device : l2gateway.nonnullDevices().values()) {
            final NeutronL2gatewayDevice neutronL2gatewayDevice = new NeutronL2gatewayDevice();
            final List<NeutronL2gatewayDeviceInterface> neutronL2gatewayDeviceInterfaces = new ArrayList<>();
            if (device.getDeviceName() != null) {
                neutronL2gatewayDevice.setDeviceName(device.getDeviceName());
            }
            if (device.getUuid() != null) {
                neutronL2gatewayDevice.setID(device.getUuid().getValue());
            }
            for (final Interfaces deviceInterface : device.nonnullInterfaces().values()) {
                final NeutronL2gatewayDeviceInterface neutronL2gatewayDeviceInterface =
                        new NeutronL2gatewayDeviceInterface();
                String interfaceName = null;
                final List<Integer> segmentationIds = new ArrayList<>();
                if (deviceInterface.getInterfaceName() != null) {
                    interfaceName = deviceInterface.getInterfaceName();
                }
                if (deviceInterface.getSegmentationIds() != null) {
                    for (final Integer segmentId : deviceInterface.getSegmentationIds()) {
                        segmentationIds.add(segmentId);
                    }
                    neutronL2gatewayDeviceInterface.setSegmentationId(segmentationIds);
                }
                neutronL2gatewayDeviceInterface.setInterfaceName(interfaceName);
                neutronL2gatewayDeviceInterfaces.add(neutronL2gatewayDeviceInterface);
            }
            neutronL2gatewayDevice.setNeutronL2gatewayDeviceInterfaces(neutronL2gatewayDeviceInterfaces);
            neutronL2gatewayDevices.add(neutronL2gatewayDevice);
        }
        result.setNeutronL2gatewayDevices(neutronL2gatewayDevices);
        return result;
    }

    @Override
    protected L2gateway toMd(NeutronL2gateway neutronObject) {
        final L2gatewayBuilder l2gatewayBuilder = new L2gatewayBuilder();
        toMdBaseAttributes(neutronObject, l2gatewayBuilder);

        if (neutronObject.getNeutronL2gatewayDevices() != null) {
            l2gatewayBuilder.setDevices(neutronObject.getNeutronL2gatewayDevices().stream()
                .map(neutronL2gatewayDevice -> new DevicesBuilder()
                    .setDeviceName(neutronL2gatewayDevice.getDeviceName())
                    .setUuid(toUuid(neutronL2gatewayDevice.getID()))
                    .setInterfaces(neutronL2gatewayDevice.getNeutronL2gatewayDeviceInterfaces().stream()
                        .map(neutronL2gatewayDeviceInterface -> {
                            final InterfacesBuilder interfacesBuilder = new InterfacesBuilder();
                            final List<Integer> segmentIds = new ArrayList<>();
                            interfacesBuilder.setInterfaceName(neutronL2gatewayDeviceInterface.getInterfaceName());
                            if (neutronL2gatewayDeviceInterface.getSegmentationId() != null) {
                                for (Integer segmentationId : neutronL2gatewayDeviceInterface.getSegmentationId()) {
                                    segmentIds.add(segmentationId);
                                }
                                interfacesBuilder.setSegmentationIds(segmentIds);
                            }

                            return interfacesBuilder.build();
                        })
                        .collect(BindingMap.toOrderedMap()))
                    .build())
                .collect(BindingMap.toOrderedMap()));
        }
        return l2gatewayBuilder.build();
    }
}
