/*
 * Copyright (c) 2015 Hewlett-Packard Development Company, L.P. and others.
 * All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse License v1.0 which accompanies this distribution,
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

import org.opendaylight.controller.sal.binding.api.BindingAwareBroker.ProviderContext;
import org.opendaylight.neutron.spi.INeutronFloatingIPCRUD;
import org.opendaylight.neutron.spi.INeutronL2GatewayCRUD;
import org.opendaylight.neutron.spi.NeutronDevice;
import org.opendaylight.neutron.spi.NeutronDeviceInterface;
import org.opendaylight.neutron.spi.NeutronL2Gateway;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.l2gateways.rev141002.l2gateway.attributes.Device;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.l2gateways.rev141002.l2gateway.attributes.DeviceBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.l2gateways.rev141002.l2gateway.attributes.device.Interface;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.l2gateways.rev141002.l2gateway.attributes.device.InterfaceBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.l2gateways.rev141002.l2gateways.attributes.L2gateways;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.l2gateways.rev141002.l2gateways.attributes.l2gateways.L2gateway;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.l2gateways.rev141002.l2gateways.attributes.l2gateways.L2gatewayBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.rev150325.Neutron;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NeutronL2GatewayInterface extends  
AbstractNeutronInterface<L2gateway, NeutronL2Gateway> 
implements INeutronL2GatewayCRUD{
    private static final Logger LOGGER =
            LoggerFactory.getLogger(NeutronL2GatewayInterface.class);
    private ConcurrentMap<String, NeutronL2Gateway> neutronL2gatewayDB =
            new ConcurrentHashMap<String, NeutronL2Gateway>();

    NeutronL2GatewayInterface(ProviderContext providerContext) {
        super(providerContext);
    }

    @Override
    public boolean neutronL2gatewayExists(String uuid) {
        return neutronL2gatewayDB.containsKey(uuid);
    }

    @Override
    public boolean addNeutronL2gateway(NeutronL2Gateway input) {
        if (neutronL2gatewayExists(input.getL2gatewayUUID())) {
            return false;
        }
        neutronL2gatewayDB.putIfAbsent(input.getL2gatewayUUID(), input);
        addMd(input);
        return true;
    }

    @Override
    protected InstanceIdentifier<L2gateway> 
    createInstanceIdentifier(L2gateway l2gateway) {
        return InstanceIdentifier.create(Neutron.class).child(L2gateways.class)
                .child(L2gateway.class,l2gateway.getKey());
    }

    @Override
    protected L2gateway toMd(NeutronL2Gateway neutronObject) {
        L2gatewayBuilder l2gatewayBuilder=new L2gatewayBuilder();
        if(neutronObject.getGatewayName()!=null){
            l2gatewayBuilder.setL2gatewayName(neutronObject.getGatewayName());
        }
        if(neutronObject.getL2gatewayUUID()!=null){
            l2gatewayBuilder.setUuid(toUuid(neutronObject.getL2gatewayUUID()));
        }
        if(neutronObject.getTenantUUID()!=null){
            l2gatewayBuilder.setTenantId(toUuid(neutronObject.getTenantUUID()));
        }

        if(neutronObject.getNeutronDevices()!=null){
            //for neutron devices
            List<Device> devices=new ArrayList<>();
            for(NeutronDevice neutronDevice: neutronObject.getNeutronDevices()){
                DeviceBuilder deviceBuilder=new DeviceBuilder();
                List<Interface> interfaces=new ArrayList<Interface>();
                for(NeutronDeviceInterface neutronDeviceInterface: neutronDevice
                        .getNeutrondeviceInterfaces()){
                    for(String interfaceName: neutronDeviceInterface
                            .getInterfaceNames()){
                        InterfaceBuilder interfacesBuilder=new InterfaceBuilder();
                        interfacesBuilder.setInterfaceName(interfaceName);
                        interfaces.add(interfacesBuilder.build());
                    }
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

    @Override
    public List<NeutronL2Gateway> getAllL2gateways() {
        Set<NeutronL2Gateway> allL2gateways = new HashSet<NeutronL2Gateway>();
        for (Entry<String, NeutronL2Gateway> entry : 
            neutronL2gatewayDB.entrySet()) {
            NeutronL2Gateway l2gateway = entry.getValue();
            allL2gateways.add(l2gateway);
        }
        LOGGER.debug("Exiting getAllL2gateway, Found {} L2gateways",
                     allL2gateways.size());
        List<NeutronL2Gateway> ans = new ArrayList<NeutronL2Gateway>();
        ans.addAll(allL2gateways);
        return ans;
    }

    @Override
    public NeutronL2Gateway getNeutronL2gateway(String uuid) {
        if (!neutronL2gatewayExists(uuid)) {
            LOGGER.debug("No L2gateway Have Been Defined");
            return null;
        }
        return neutronL2gatewayDB.get(uuid);
    }

    public static void registerNewInterface(BundleContext context,
                                            ProviderContext providerContext,
                                            List<ServiceRegistration<?>> registrations) {
        NeutronL2GatewayInterface neutronL2gatewayInterface = new NeutronL2GatewayInterface(providerContext);
        ServiceRegistration<INeutronL2GatewayCRUD> neutronL2gatewayInterfaceRegistration = context.registerService(INeutronL2GatewayCRUD.class, neutronL2gatewayInterface, null);
        if (neutronL2gatewayInterfaceRegistration != null) {
            registrations.add(neutronL2gatewayInterfaceRegistration);
        }
    }
}
