/*
 * Copyright Tata Consultancy Services, 2015.  All rights reserved.
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
import org.opendaylight.neutron.spi.INeutronVPNServiceCRUD;
import org.opendaylight.neutron.spi.NeutronVPNService;
import org.opendaylight.yangtools.yang.binding.DataObject;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NeutronVPNServiceInterface extends AbstractNeutronInterface implements INeutronVPNServiceCRUD {
    private static final Logger logger = LoggerFactory.getLogger(NeutronVPNServiceInterface.class);
    private ConcurrentMap<String, NeutronVPNService> VPNServiceDB = new ConcurrentHashMap<String, NeutronVPNService>();


    NeutronVPNServiceInterface(ProviderContext providerContext) {
        super(providerContext);
    }

    // this method uses reflection to update an object from it's delta.

    private boolean overwrite(Object target, Object delta) {
        Method[] methods = target.getClass().getMethods();

        for (Method toMethod : methods) {
            if (toMethod.getDeclaringClass().equals(target.getClass()) && toMethod.getName().startsWith("set")) {

                String toName = toMethod.getName();
                String fromName = toName.replace("set", "get");

                try {
                    Method fromMethod = delta.getClass().getMethod(fromName);
                    Object value = fromMethod.invoke(delta, (Object[]) null);
                    if (value != null) {
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

    @Override
    public boolean neutronVPNServiceExists(String uuid) {
        return VPNServiceDB.containsKey(uuid);
    }

    @Override
    public NeutronVPNService getVPNService(String uuid) {
        if (!neutronVPNServiceExists(uuid)) {
            logger.debug("No VPNService Have Been Defined");
            return null;
        }
        return VPNServiceDB.get(uuid);
    }

    @Override
    public List<NeutronVPNService> getAllVPNService() {
        Set<NeutronVPNService> allVPNService = new HashSet<NeutronVPNService>();
        for (Entry<String, NeutronVPNService> entry : VPNServiceDB.entrySet()) {
            NeutronVPNService VPNService = entry.getValue();
            allVPNService.add(VPNService);
        }
        logger.debug("Exiting getVPNService, Found {} OpenStackVPNService", allVPNService.size());
        List<NeutronVPNService> ans = new ArrayList<NeutronVPNService>();
        ans.addAll(allVPNService);
        return ans;
    }

    @Override
    public boolean addVPNService(NeutronVPNService input) {
        if (neutronVPNServiceExists(input.getID())) {
            return false;
        }
        VPNServiceDB.putIfAbsent(input.getID(), input);
        return true;
    }

    @Override
    public boolean removeVPNService(String uuid) {
        if (!neutronVPNServiceExists(uuid)) {
            return false;
        }
        VPNServiceDB.remove(uuid);
        return true;
    }

    @Override
    public boolean updateVPNService(String uuid, NeutronVPNService delta) {
        if (!neutronVPNServiceExists(uuid)) {
            return false;
        }
        NeutronVPNService target = VPNServiceDB.get(uuid);
        return overwrite(target, delta);
    }

    @Override
    public boolean neutronVPNServiceInUse(String uuid) {
        return !neutronVPNServiceExists(uuid);
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
