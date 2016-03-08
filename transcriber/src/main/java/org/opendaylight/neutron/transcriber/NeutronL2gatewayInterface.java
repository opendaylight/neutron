/*
 * Copyright (c) 2015 Hewlett-Packard Development Company, L.P. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.transcriber;

import java.util.ArrayList;
import java.util.List;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.opendaylight.controller.sal.binding.api.BindingAwareBroker.ProviderContext;
import org.opendaylight.neutron.spi.INeutronL2gatewayCRUD;
import org.opendaylight.neutron.spi.NeutronL2gatewayDevice;
import org.opendaylight.neutron.spi.NeutronL2gatewayDeviceInterface;
import org.opendaylight.neutron.spi.NeutronL2gateway;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.l2gateways.rev150712.l2gateway.attributes.Devices;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.l2gateways.rev150712.l2gateway.attributes.DevicesBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.l2gateways.rev150712.l2gateway.attributes.devices.Interfaces;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.l2gateways.rev150712.l2gateway.attributes.devices.InterfacesBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.l2gateways.rev150712.l2gateways.attributes.L2gateways;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.l2gateways.rev150712.l2gateways.attributes.l2gateways.L2gateway;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.l2gateways.rev150712.l2gateways.attributes.l2gateways.L2gatewayBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.rev150712.Neutron;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;

public class NeutronL2gatewayInterface extends
    AbstractNeutronInterface<L2gateway, L2gateways, NeutronL2gateway>
    implements INeutronL2gatewayCRUD {
    private static final Logger LOGGER = LoggerFactory.getLogger(NeutronL2gatewayInterface.class);

    NeutronL2gatewayInterface(ProviderContext providerContext) {
        super(providerContext);
    }

    @Override
    protected List<L2gateway> getDataObjectList(L2gateways l2gateways) {
        return l2gateways.getL2gateway();
    }

    @Override
    public boolean inUse(String l2gatewayID) {
        return !exists(l2gatewayID);
    }

    @Override
    protected InstanceIdentifier<L2gateway>
    createInstanceIdentifier(L2gateway l2gateway) {
        return InstanceIdentifier.create(Neutron.class).child(L2gateways.class)
                .child(L2gateway.class,l2gateway.getKey());
    }

    @Override
    protected InstanceIdentifier<L2gateways> createInstanceIdentifier() {
        return InstanceIdentifier.create(Neutron.class).child(L2gateways.class);
    }

    @Override
    protected NeutronL2gateway fromMd(L2gateway l2gateway){
        NeutronL2gateway result = new NeutronL2gateway();
        List<NeutronL2gatewayDevice> neutronL2gatewayDevices = new ArrayList<NeutronL2gatewayDevice>();

        if (l2gateway.getUuid() != null){
            result.setID(l2gateway.getUuid().getValue());
        }
        if (l2gateway.getUuid() != null){
        result.setID(l2gateway.getUuid().getValue());
        }
        if (l2gateway.getL2gatewayName() != null){
            result.setL2gatewayName(String.valueOf(l2gateway.getL2gatewayName()));
        }
        if (l2gateway.getTenantId() != null){
            result.setTenantID(l2gateway.getTenantId());
        }
        if (l2gateway.getDevices() != null){
            for (Devices device:l2gateway.getDevices()){
                NeutronL2gatewayDevice neutronL2gatewayDevice = new NeutronL2gatewayDevice();
                List<NeutronL2gatewayDeviceInterface> neutronL2gatewayDeviceInterfaces =
                        new ArrayList<NeutronL2gatewayDeviceInterface>();
                if (device.getDeviceName() != null){
                    neutronL2gatewayDevice.setDeviceName(device.getDeviceName().toString());
                }
                if(device.getInterfaces() != null){
                    for(Interfaces deviceInterface:device.getInterfaces()){
                        NeutronL2gatewayDeviceInterface neutronL2gatewayDeviceInterface =
                                new NeutronL2gatewayDeviceInterface();
                        String interfaceName = null;
                        List<Integer> segmentationIds = new ArrayList<Integer>();
                        if (deviceInterface.getInterfaceName() != null){
                            interfaceName = deviceInterface
                                    .getInterfaceName().toString();
                        }
                        if (deviceInterface.getSegmentationIds() != null){
                            for(Integer segmentId:deviceInterface.getSegmentationIds()){
                                segmentationIds.add(segmentId);
                            }
                            neutronL2gatewayDeviceInterface.setSegmentationId(segmentationIds);
                        }
                        neutronL2gatewayDeviceInterface.setInterfaceName(interfaceName);
                        neutronL2gatewayDeviceInterfaces.add(neutronL2gatewayDeviceInterface);
                    }
                }
                neutronL2gatewayDevice.setNeutronL2gatewayDeviceInterfaces(neutronL2gatewayDeviceInterfaces);
                neutronL2gatewayDevices.add(neutronL2gatewayDevice);
            }
        }
        result.setNeutronL2gatewayDevices(neutronL2gatewayDevices);
        return result;
    }

    @Override
    protected L2gateway toMd(NeutronL2gateway neutronObject) {
        L2gatewayBuilder l2gatewayBuilder = new L2gatewayBuilder();
        if (neutronObject.getL2gatewayName() != null){
            l2gatewayBuilder.setL2gatewayName(neutronObject.getL2gatewayName());
        }
        if (neutronObject.getID() != null){
            l2gatewayBuilder.setUuid(toUuid(neutronObject.getID()));
        }
        if (neutronObject.getTenantID() != null){
            l2gatewayBuilder.setTenantId(toUuid(neutronObject.getTenantID()));
        }

        if (neutronObject.getNeutronL2gatewayDevices() != null){
            List<Devices> devices = new ArrayList<>();
            for(NeutronL2gatewayDevice neutronL2gatewayDevice:neutronObject.getNeutronL2gatewayDevices()){
                DevicesBuilder deviceBuilder = new DevicesBuilder();
                List<Interfaces> interfaces = new ArrayList<Interfaces>();
                for(NeutronL2gatewayDeviceInterface neutronL2gatewayDeviceInterface:neutronL2gatewayDevice
                        .getNeutronL2gatewayDeviceInterfaces()){
                    InterfacesBuilder interfacesBuilder = new InterfacesBuilder();
                    List<Integer> segmentIds = new ArrayList<Integer>();
                    interfacesBuilder.setInterfaceName(neutronL2gatewayDeviceInterface.getInterfaceName());
                    if (neutronL2gatewayDeviceInterface.getSegmentationId() != null){
                        for(Integer segmentationId:neutronL2gatewayDeviceInterface
                                .getSegmentationId()){
                            segmentIds.add(segmentationId);
                        }
                        interfacesBuilder.setSegmentationIds(segmentIds);
                    }
                    interfaces.add(interfacesBuilder.build());
                }
                deviceBuilder.setDeviceName(neutronL2gatewayDevice.getDeviceName());
                deviceBuilder.setInterfaces(interfaces);
                devices.add(deviceBuilder.build());
            }
            l2gatewayBuilder.setDevices(devices);
        }
        return l2gatewayBuilder.build();
    }

    @Override
    protected L2gateway toMd(String uuid) {
        L2gatewayBuilder l2gatewayBuilder = new L2gatewayBuilder();
        l2gatewayBuilder.setUuid(toUuid(uuid));
        return l2gatewayBuilder.build();
    }

    public static void registerNewInterface(BundleContext context,
                                            ProviderContext providerContext,
                                            List<ServiceRegistration<?>> registrations) {
        NeutronL2gatewayInterface neutronL2gatewayInterface = new NeutronL2gatewayInterface(providerContext);
        ServiceRegistration<INeutronL2gatewayCRUD> neutronL2gatewayInterfaceRegistration = context
                        .registerService(INeutronL2gatewayCRUD.class, neutronL2gatewayInterface, null);
        if (neutronL2gatewayInterfaceRegistration != null) {
            registrations.add(neutronL2gatewayInterfaceRegistration);
        }
    }

}
