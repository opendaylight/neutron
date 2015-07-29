/*
 * Copyright (c) 2015 Hewlett-Packard Development Company and others.  All rights reserved.
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
import org.opendaylight.neutron.spi.INeutronL2GatewayCRUD;
import org.opendaylight.neutron.spi.INeutronSecurityGroupCRUD;
import org.opendaylight.neutron.spi.INeutronSubnetCRUD;
import org.opendaylight.neutron.spi.NeutronCRUDInterfaces;
import org.opendaylight.neutron.spi.NeutronDevice;
import org.opendaylight.neutron.spi.NeutronDeviceInterface;
import org.opendaylight.neutron.spi.NeutronL2Gateway;
import org.opendaylight.neutron.spi.NeutronL2GatewayConnection;
import org.opendaylight.neutron.spi.NeutronPort;
import org.opendaylight.neutron.spi.NeutronSecurityGroup;
import org.opendaylight.neutron.spi.NeutronSecurityRule;
import org.opendaylight.neutron.spi.NeutronSubnet;
import org.opendaylight.neutron.spi.Neutron_IPs;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.l2gateways.rev141002.l2gateway.attributes.Device;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.l2gateways.rev141002.l2gateway.attributes.DeviceBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.l2gateways.rev141002.l2gateway.attributes.device.Interface;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.l2gateways.rev141002.l2gateway.attributes.device.InterfaceBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.l2gateways.rev141002.l2gateways.attributes.L2gateways;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.l2gateways.rev141002.l2gateways.attributes.l2gateways.L2gateway;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.l2gateways.rev141002.l2gateways.attributes.l2gateways.L2gatewayBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.rev150325.Neutron;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;


public class NeutronL2GatewayInterface extends
    AbstractNeutronInterface<L2gateway, NeutronL2Gateway>
    implements INeutronL2GatewayCRUD{
    private static final Logger LOGGER =
            LoggerFactory.getLogger(NeutronL2GatewayInterface.class);

    NeutronL2GatewayInterface(ProviderContext providerContext) {
        super(providerContext);
    }

    @Override
    public boolean neutronL2gatewayExists(String uuid) {
        L2gateway l2gateway = readMd(createInstanceIdentifier(toMd(uuid)));
        if (l2gateway == null) {
            return false;
        }
        return true;
  }

    @Override
    public NeutronL2Gateway getNeutronL2gateway(String uuid) {
        L2gateway l2gateway = readMd(createInstanceIdentifier(toMd(uuid)));
        if (l2gateway == null) {
            return null;
        }
        return fromMd(l2gateway);
    }

    @Override
    public List<NeutronL2Gateway> getAllL2gateways() {
        Set<NeutronL2Gateway> allL2gateways = new HashSet<NeutronL2Gateway>();
        L2gateways l2gateways=readMd(createInstanceIdentifier());
        if (l2gateways!=null){
            for(L2gateway l2gateway:l2gateways.getL2gateway()){
                allL2gateways.add(fromMd(l2gateway));
            }
        }
        LOGGER.debug("Exiting getAllL2gateway, Found {} L2gateways",
                     allL2gateways.size());
        List<NeutronL2Gateway> ans = new ArrayList<NeutronL2Gateway>();
        ans.addAll(allL2gateways);
        return ans;
    }

    @Override
    public boolean addNeutronL2gateway(NeutronL2Gateway input) {
        if (neutronL2gatewayExists(input.getL2gatewayUUID())) {
            return false;
        }
        addMd(input);
        return true;
    }

    @Override
    public boolean removeL2gateway(String uuid) {
        if (!neutronL2gatewayExists(uuid)) {
            return false;
        }
        return removeMd(toMd(uuid));
    }

    @Override
    public boolean updateL2gateway(String uuid, NeutronL2Gateway delta) {
        if (!neutronL2gatewayExists(uuid)) {
            return false;
        }
        updateMd(delta);
        return true;
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

    @Override
    protected L2gateway toMd(NeutronL2Gateway neutronObject) {
        L2gatewayBuilder l2gatewayBuilder=new L2gatewayBuilder();
        if (neutronObject.getGatewayName()!=null){
            l2gatewayBuilder.setL2gatewayName(neutronObject.getGatewayName());
        }
        if (neutronObject.getL2gatewayUUID()!=null){
            l2gatewayBuilder.setUuid(toUuid(neutronObject.getL2gatewayUUID()));
        }
        if (neutronObject.getTenantUUID()!=null){
            l2gatewayBuilder.setTenantId(toUuid(neutronObject.getTenantUUID()));
        }

        if (neutronObject.getNeutronDevices()!=null){
            List<Device> devices=new ArrayList<>();
            for(NeutronDevice neutronDevice: neutronObject.getNeutronDevices()){
                DeviceBuilder deviceBuilder=new DeviceBuilder();
                List<Interface> interfaces=new ArrayList<Interface>();
                for(NeutronDeviceInterface neutronDeviceInterface: neutronDevice
                        .getNeutrondeviceInterfaces()){
                    InterfaceBuilder interfacesBuilder=new InterfaceBuilder();
                    List<Integer> segmentIds=new ArrayList<Integer>();
                    for(String interfaceName: neutronDeviceInterface
                            .getInterfaceNames()){
                        interfacesBuilder.setInterfaceName(interfaceName);
                    }
                    if (neutronDeviceInterface.getSegmentationId()!=null){
                        for(Integer segmentationId:neutronDeviceInterface
                                .getSegmentationId()){
                            segmentIds.add(segmentationId);
                        }
                        interfacesBuilder.setSegmentationId(segmentIds);
                    }
                    interfaces.add(interfacesBuilder.build());
                }
                deviceBuilder.setDeviceName(neutronDevice.getDeviceName());
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

    protected NeutronL2Gateway fromMd(L2gateway l2gateway){
        NeutronL2Gateway result= new NeutronL2Gateway();
        List<NeutronDevice> neutronDevices=new ArrayList<NeutronDevice>();

        if (l2gateway.getUuid().getValue()!=null){
            result.setID(String.valueOf(l2gateway.getUuid().getValue()));
        }
        if (l2gateway.getUuid().getValue()!=null){
        result.setL2gatewayUUID(String.valueOf(l2gateway.getUuid().getValue()));
        }
        if (l2gateway.getL2gatewayName()!=null){
            result.setGatewayName(String.valueOf(l2gateway.getL2gatewayName()));
        }
        if (l2gateway.getTenantId().getValue()!=null){
            result.setTenantUUID(String.valueOf(l2gateway.getTenantId().getValue()));
        }
        if (l2gateway.getDevice()!=null){
            for (Device device: l2gateway.getDevice()){
                NeutronDevice neutronDevice= new NeutronDevice();
                List<NeutronDeviceInterface> neutronDeviceInterfaces=
                        new ArrayList<NeutronDeviceInterface>();
                if (device.getDeviceName()!=null){
                neutronDevice.setDeviceName(device.getDeviceName().toString());
                }
                if(device.getInterface()!=null){
                    for(Interface deviceInterface:device.getInterface()){
                        NeutronDeviceInterface neutronDeviceInterface=
                                new NeutronDeviceInterface();
                        List<String> interfaceNames=new ArrayList<String>();
                        List<Integer> segmentationIds=new ArrayList<Integer>();
                        if (deviceInterface.getInterfaceName()!=null){
                            String interfaceName=deviceInterface
                                    .getInterfaceName().toString();
                        interfaceNames.add(interfaceName);
                        }
                        if (deviceInterface.getSegmentationId()!=null){
                            for(Integer segmentId:deviceInterface.getSegmentationId()){
                                segmentationIds.add(segmentId);
                            }
                            neutronDeviceInterface.setSegmentationId(segmentationIds);
                        }
                        neutronDeviceInterface.setInterfaceNames(interfaceNames);
                        neutronDeviceInterfaces.add(neutronDeviceInterface);
                    }
                }
                neutronDevice.setNeutrondeviceInterfaces(neutronDeviceInterfaces);
                neutronDevices.add(neutronDevice);
            }
        }
        result.setNeutronDevices(neutronDevices);
        return result;
    }
    public static void registerNewInterface(BundleContext context,
                                            ProviderContext providerContext,
                                            List<ServiceRegistration<?>>
    registrations) {
        NeutronL2GatewayInterface neutronL2gatewayInterface =
                new NeutronL2GatewayInterface(providerContext);
        ServiceRegistration<INeutronL2GatewayCRUD>
        neutronL2gatewayInterfaceRegistration = context
        .registerService(INeutronL2GatewayCRUD.class,
                         neutronL2gatewayInterface, null);
        if (neutronL2gatewayInterfaceRegistration != null) {
            registrations.add(neutronL2gatewayInterfaceRegistration);
        }
    }

}
