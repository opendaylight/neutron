/*
 * Copyright (c) 2015 Hewlett-Packard Development Company, L.P. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.transcriber;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.opendaylight.controller.sal.binding.api.BindingAwareBroker.ProviderContext;
import org.opendaylight.neutron.spi.INeutronFloatingIPCRUD;
import org.opendaylight.neutron.spi.INeutronL2gatewayCRUD;
import org.opendaylight.neutron.spi.INeutronSecurityGroupCRUD;
import org.opendaylight.neutron.spi.INeutronSubnetCRUD;
import org.opendaylight.neutron.spi.NeutronCRUDInterfaces;
import org.opendaylight.neutron.spi.NeutronL2gwDevice;
import org.opendaylight.neutron.spi.NeutronL2gwDeviceInterface;
import org.opendaylight.neutron.spi.NeutronL2gateway;
import org.opendaylight.neutron.spi.NeutronL2gatewayConnection;
import org.opendaylight.neutron.spi.NeutronPort;
import org.opendaylight.neutron.spi.NeutronSecurityGroup;
import org.opendaylight.neutron.spi.NeutronSecurityRule;
import org.opendaylight.neutron.spi.NeutronSubnet;
import org.opendaylight.neutron.spi.Neutron_IPs;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.l2gateways.rev150712.l2gateway.attributes.Device;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.l2gateways.rev150712.l2gateway.attributes.DeviceBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.l2gateways.rev150712.l2gateway.attributes.device.Interface;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.l2gateways.rev150712.l2gateway.attributes.device.InterfaceBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.l2gateways.rev150712.l2gateways.attributes.L2gateways;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.l2gateways.rev150712.l2gateways.attributes.l2gateways.L2gateway;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.l2gateways.rev150712.l2gateways.attributes.l2gateways.L2gatewayBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.rev150712.Neutron;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;

public class NeutronL2gatewayInterface extends
    AbstractNeutronInterface<L2gateway, L2gateways, NeutronL2gateway>
    implements INeutronL2gatewayCRUD{
    private static final Logger LOGGER = LoggerFactory.getLogger(NeutronL2gatewayInterface.class);

    NeutronL2gatewayInterface(ProviderContext providerContext) {
        super(providerContext);
    }

    @Override
    protected List<L2gateway> getDataObjectList(L2gateways l2gateways) {
        return l2gateways.getL2gateway();
    }

    @Override
    public boolean neutronL2gatewayExists(String uuid) {
        return exists(uuid);
  }

    @Override
    public NeutronL2gateway getNeutronL2gateway(String uuid) {
        return get(uuid);
    }

    @Override
    public List<NeutronL2gateway> getAllNeutronL2gateways() {
        return getAll();
    }

    @Override
    public boolean addNeutronL2gateway(NeutronL2gateway input) {
        return add(input);
    }

    @Override
    public boolean removeNeutronL2gateway(String uuid) {
        return remove(uuid);
    }

    @Override
    public boolean updateNeutronL2gateway(String uuid, NeutronL2gateway delta) {
        return update(uuid, delta);
    }

    @Override
    public boolean neutronL2gatewayInUse(String l2gatewayID) {
        return !exists(l2gatewayID);
    }

    @Override
    protected InstanceIdentifier<L2gateway>
    createInstanceIdentifier(L2gateway l2gateway) {
        return InstanceIdentifier.create(Neutron.class).child(L2gateways.class)
                .child(L2gateway.class,l2gateway.getKey());
    }

    protected InstanceIdentifier<L2gateways> createInstanceIdentifier() {
        return InstanceIdentifier.create(Neutron.class).child(L2gateways.class);
    }

    protected NeutronL2gateway fromMd(L2gateway l2gateway){
        NeutronL2gateway result = new NeutronL2gateway();
        List<NeutronL2gwDevice> neutronL2gwDevices = new ArrayList<NeutronL2gwDevice>();

        if (l2gateway.getUuid().getValue() != null){
            result.setID(String.valueOf(l2gateway.getUuid().getValue()));
        }
        if (l2gateway.getUuid().getValue() != null){
        result.setID(String.valueOf(l2gateway.getUuid().getValue()));
        }
        if (l2gateway.getL2gatewayName() != null){
            result.setL2gatewayName(String.valueOf(l2gateway.getL2gatewayName()));
        }
        if (l2gateway.getTenantId().getValue() != null){
            result.setTenantID(String.valueOf(l2gateway.getTenantId().getValue()));
        }
        if (l2gateway.getDevice() != null){
            for (Device device: l2gateway.getDevice()){
                NeutronL2gwDevice neutronL2gwDevice = new NeutronL2gwDevice();
                List<NeutronL2gwDeviceInterface> neutronL2gwDeviceInterfaces =
                        new ArrayList<NeutronL2gwDeviceInterface>();
                if (device.getDeviceName() != null){
                    neutronL2gwDevice.setDeviceName(device.getDeviceName().toString());
                }
                if(device.getInterface() != null){
                    for(Interface deviceInterface:device.getInterface()){
                        NeutronL2gwDeviceInterface neutronL2gwDeviceInterface =
                                new NeutronL2gwDeviceInterface();
                        List<String> interfaceNames = new ArrayList<String>();
                        List<Integer> segmentationIds = new ArrayList<Integer>();
                        if (deviceInterface.getInterfaceName() != null){
                            String interfaceName = deviceInterface
                                    .getInterfaceName().toString();
                        interfaceNames.add(interfaceName);
                        }
                        if (deviceInterface.getSegmentationId() != null){
                            for(Integer segmentId:deviceInterface.getSegmentationId()){
                                segmentationIds.add(segmentId);
                            }
                            neutronL2gwDeviceInterface.setSegmentationId(segmentationIds);
                        }
                        neutronL2gwDeviceInterface.setInterfaceNames(interfaceNames);
                        neutronL2gwDeviceInterfaces.add(neutronL2gwDeviceInterface);
                    }
                }
                neutronL2gwDevice.setNeutronL2gwDeviceInterfaces(neutronL2gwDeviceInterfaces);
                neutronL2gwDevices.add(neutronL2gwDevice);
            }
        }
        result.setNeutronL2gwDevices(neutronL2gwDevices);
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

        if (neutronObject.getNeutronL2gwDevices() != null){
            List<Device> devices = new ArrayList<>();
            for(NeutronL2gwDevice neutronL2gwDevice: neutronObject.getNeutronL2gwDevices()){
                DeviceBuilder deviceBuilder = new DeviceBuilder();
                List<Interface> interfaces = new ArrayList<Interface>();
                for(NeutronL2gwDeviceInterface neutronL2gwDeviceInterface: neutronL2gwDevice
                        .getNeutronL2gwDeviceInterfaces()){
                    InterfaceBuilder interfacesBuilder = new InterfaceBuilder();
                    List<Integer> segmentIds = new ArrayList<Integer>();
                    for(String interfaceName: neutronL2gwDeviceInterface
                            .getInterfaceNames()){
                        interfacesBuilder.setInterfaceName(interfaceName);
                    }
                    if (neutronL2gwDeviceInterface.getSegmentationId() != null){
                        for(Integer segmentationId:neutronL2gwDeviceInterface
                                .getSegmentationId()){
                            segmentIds.add(segmentationId);
                        }
                        interfacesBuilder.setSegmentationId(segmentIds);
                    }
                    interfaces.add(interfacesBuilder.build());
                }
                deviceBuilder.setDeviceName(neutronL2gwDevice.getDeviceName());
                deviceBuilder.setInterface(interfaces);
                devices.add(deviceBuilder.build());
            }
            l2gatewayBuilder.setDevice(devices);
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
