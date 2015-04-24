/*
 * Copyright IBM Corporation, 2013.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.transcriber;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.opendaylight.controller.sal.binding.api.BindingAwareBroker.ProviderContext;
import org.opendaylight.neutron.spi.INeutronFloatingIPCRUD;
import org.opendaylight.neutron.spi.INeutronNetworkCRUD;
import org.opendaylight.neutron.spi.INeutronPortCRUD;
import org.opendaylight.neutron.spi.INeutronSubnetCRUD;
import org.opendaylight.neutron.spi.NeutronCRUDInterfaces;
import org.opendaylight.neutron.spi.NeutronFloatingIP;
import org.opendaylight.neutron.spi.NeutronPort;
import org.opendaylight.neutron.spi.NeutronSubnet;
import org.opendaylight.yangtools.yang.binding.DataObject;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NeutronFloatingIPInterface extends AbstractNeutronInterface implements INeutronFloatingIPCRUD {
    private static final Logger logger = LoggerFactory.getLogger(NeutronFloatingIPInterface.class);

    private ConcurrentMap<String, NeutronFloatingIP> floatingIPDB  = new ConcurrentHashMap<String, NeutronFloatingIP>();


    NeutronFloatingIPInterface(ProviderContext providerContext) {
        super(providerContext);
    }

    // this method uses reflection to update an object from it's delta.

    @SuppressWarnings("unused")
    private boolean overwrite(Object target, Object delta) {
        Method[] methods = target.getClass().getMethods();

        for(Method toMethod: methods){
            if(toMethod.getDeclaringClass().equals(target.getClass())
                    && toMethod.getName().startsWith("set")){

                String toName = toMethod.getName();
                String fromName = toName.replace("set", "get");

                try {
                    Method fromMethod = delta.getClass().getMethod(fromName);
                    Object value = fromMethod.invoke(delta, (Object[])null);
                    if(value != null){
                        toMethod.invoke(target, value);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }
        }
        return true;
    }

    // IfNBFloatingIPCRUD interface methods

    @Override
    public boolean floatingIPExists(String uuid) {
        return floatingIPDB.containsKey(uuid);
    }

    @Override
    public NeutronFloatingIP getFloatingIP(String uuid) {
        if (!floatingIPExists(uuid)) {
            return null;
        }
        return floatingIPDB.get(uuid);
    }

    @Override
    public List<NeutronFloatingIP> getAllFloatingIPs() {
        Set<NeutronFloatingIP> allIPs = new HashSet<NeutronFloatingIP>();
        for (Entry<String, NeutronFloatingIP> entry : floatingIPDB.entrySet()) {
            NeutronFloatingIP floatingip = entry.getValue();
            allIPs.add(floatingip);
        }
        logger.debug("Exiting getAllFloatingIPs, Found {} FloatingIPs", allIPs.size());
        List<NeutronFloatingIP> ans = new ArrayList<NeutronFloatingIP>();
        ans.addAll(allIPs);
        return ans;
    }

    @Override
    public boolean addFloatingIP(NeutronFloatingIP input) {
        INeutronNetworkCRUD networkCRUD = NeutronCRUDInterfaces.getINeutronNetworkCRUD(this);
        INeutronSubnetCRUD subnetCRUD = NeutronCRUDInterfaces.getINeutronSubnetCRUD(this);
        INeutronPortCRUD portCRUD = NeutronCRUDInterfaces.getINeutronPortCRUD(this);

        if (floatingIPExists(input.getID())) {
            return false;
        }
        //if floating_ip_address isn't there, allocate from the subnet pool
        NeutronSubnet subnet = subnetCRUD.getSubnet(networkCRUD.getNetwork(input.getFloatingNetworkUUID()).getSubnets().get(0));
        if (input.getFloatingIPAddress() == null) {
            input.setFloatingIPAddress(subnet.getLowAddr());
        }
        subnet.allocateIP(input.getFloatingIPAddress());

        //if port_id is there, bind port to this floating ip
        if (input.getPortUUID() != null) {
            NeutronPort port = portCRUD.getPort(input.getPortUUID());
            port.addFloatingIP(input.getFixedIPAddress(), input);
        }

        floatingIPDB.putIfAbsent(input.getID(), input);
        return true;
    }

    @Override
    public boolean removeFloatingIP(String uuid) {
        INeutronNetworkCRUD networkCRUD = NeutronCRUDInterfaces.getINeutronNetworkCRUD(this);
        INeutronSubnetCRUD subnetCRUD = NeutronCRUDInterfaces.getINeutronSubnetCRUD(this);
        INeutronPortCRUD portCRUD = NeutronCRUDInterfaces.getINeutronPortCRUD(this);

        if (!floatingIPExists(uuid)) {
            return false;
        }
        NeutronFloatingIP floatIP = getFloatingIP(uuid);
        //if floating_ip_address isn't there, allocate from the subnet pool
        NeutronSubnet subnet = subnetCRUD.getSubnet(networkCRUD.getNetwork(floatIP.getFloatingNetworkUUID()).getSubnets().get(0));
        subnet.releaseIP(floatIP.getFloatingIPAddress());
        if (floatIP.getPortUUID() != null) {
            NeutronPort port = portCRUD.getPort(floatIP.getPortUUID());
            port.removeFloatingIP(floatIP.getFixedIPAddress());
        }
        floatingIPDB.remove(uuid);
        return true;
    }

    @Override
    public boolean updateFloatingIP(String uuid, NeutronFloatingIP delta) {
        INeutronPortCRUD portCRUD = NeutronCRUDInterfaces.getINeutronPortCRUD(this);

        if (!floatingIPExists(uuid)) {
            return false;
        }
        NeutronFloatingIP target = floatingIPDB.get(uuid);
        if (target.getPortUUID() != null) {
            NeutronPort port = portCRUD.getPort(target.getPortUUID());
            port.removeFloatingIP(target.getFixedIPAddress());
        }

        //if port_id is there, bind port to this floating ip
        if (delta.getPortUUID() != null) {
            NeutronPort port = portCRUD.getPort(delta.getPortUUID());
            port.addFloatingIP(delta.getFixedIPAddress(), delta);
        }

        target.setPortUUID(delta.getPortUUID());
        target.setFixedIPAddress(delta.getFixedIPAddress());
        return true;
    }

    @Override
    protected InstanceIdentifier createInstanceIdentifier(DataObject item) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected DataObject toMd(Object neutronObject) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected DataObject toMd(String uuid) {
        // TODO Auto-generated method stub
        return null;
    }
}
