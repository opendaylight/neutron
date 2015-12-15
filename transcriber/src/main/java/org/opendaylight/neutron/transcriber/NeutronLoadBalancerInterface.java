/*
 * Copyright (C) 2014 Red Hat, Inc.
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
import org.opendaylight.neutron.spi.INeutronLoadBalancerCRUD;
import org.opendaylight.neutron.spi.NeutronLoadBalancer;
import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.inet.types.rev100924.IpAddress;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.lbaasv2.rev141002.lbaas.attributes.Loadbalancer;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.lbaasv2.rev141002.lbaas.attributes.loadbalancer.Loadbalancers;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.lbaasv2.rev141002.lbaas.attributes.loadbalancer.LoadbalancersBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.rev150325.Neutron;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NeutronLoadBalancerInterface extends AbstractNeutronInterface<Loadbalancers, NeutronLoadBalancer> implements INeutronLoadBalancerCRUD {
    private static final Logger LOGGER = LoggerFactory.getLogger(NeutronLoadBalancerInterface.class);
    private ConcurrentMap<String, NeutronLoadBalancer> loadBalancerDB  = new ConcurrentHashMap<String, NeutronLoadBalancer>();


    NeutronLoadBalancerInterface(ProviderContext providerContext) {
        super(providerContext);
    }

    // this method uses reflection to update an object from it's delta.

    private boolean overwrite(Object target, Object delta) {
        Method[] methods = target.getClass().getMethods();

        for (Method toMethod : methods) {
            if (toMethod.getDeclaringClass().equals(target.getClass())
                    && toMethod.getName().startsWith("set")) {

                String toName = toMethod.getName();
                String fromName = toName.replace("set", "get");

                try {
                    Method fromMethod = delta.getClass().getMethod(fromName);
                    Object value = fromMethod.invoke(delta, (Object[]) null);
                    if (value != null) {
                        toMethod.invoke(target, value);
                    }
                } catch (Exception e) {
                    LOGGER.error(e.getMessage());
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean neutronLoadBalancerExists(String uuid) {
        return loadBalancerDB.containsKey(uuid);
    }

    @Override
    public NeutronLoadBalancer getNeutronLoadBalancer(String uuid) {
        if (!neutronLoadBalancerExists(uuid)) {
            LOGGER.debug("No LoadBalancer Have Been Defined");
            return null;
        }
        return loadBalancerDB.get(uuid);
    }

    @Override
    public List<NeutronLoadBalancer> getAllNeutronLoadBalancers() {
        Set<NeutronLoadBalancer> allLoadBalancers = new HashSet<NeutronLoadBalancer>();
        for (Entry<String, NeutronLoadBalancer> entry : loadBalancerDB.entrySet()) {
            NeutronLoadBalancer loadBalancer = entry.getValue();
            allLoadBalancers.add(loadBalancer);
        }
        LOGGER.debug("Exiting getLoadBalancers, Found {} OpenStackLoadBalancer", allLoadBalancers.size());
        List<NeutronLoadBalancer> ans = new ArrayList<NeutronLoadBalancer>();
        ans.addAll(allLoadBalancers);
        return ans;
    }

    @Override
    public boolean addNeutronLoadBalancer(NeutronLoadBalancer input) {
        if (neutronLoadBalancerExists(input.getLoadBalancerID())) {
            return false;
        }
        loadBalancerDB.putIfAbsent(input.getLoadBalancerID(), input);
        //TODO: add code to find INeutronLoadBalancerAware services and call newtorkCreated on them
        return true;
    }

    @Override
    public boolean removeNeutronLoadBalancer(String uuid) {
        if (!neutronLoadBalancerExists(uuid)) {
            return false;
        }
        loadBalancerDB.remove(uuid);
        //TODO: add code to find INeutronLoadBalancerAware services and call newtorkDeleted on them
        return true;
    }

    @Override
    public boolean updateNeutronLoadBalancer(String uuid, NeutronLoadBalancer delta) {
        if (!neutronLoadBalancerExists(uuid)) {
            return false;
        }
        NeutronLoadBalancer target = loadBalancerDB.get(uuid);
        return overwrite(target, delta);
    }

    @Override
    public boolean neutronLoadBalancerInUse(String loadBalancerUUID) {
        return !neutronLoadBalancerExists(loadBalancerUUID);
    }

    @Override
    protected Loadbalancers toMd(String uuid) {
        LoadbalancersBuilder loadBalancersBuilder = new LoadbalancersBuilder();
        loadBalancersBuilder.setUuid(toUuid(uuid));
        return loadBalancersBuilder.build();
    }

    @Override
    protected InstanceIdentifier<Loadbalancers> createInstanceIdentifier(
            Loadbalancers loadBalancers) {
        return InstanceIdentifier.create(Neutron.class)
                .child(Loadbalancer.class)
                .child(Loadbalancers.class, loadBalancers.getKey());
    }

    @Override
    protected Loadbalancers toMd(NeutronLoadBalancer loadBalancer) {
        LoadbalancersBuilder loadBalancersBuilder = new LoadbalancersBuilder();
        loadBalancersBuilder.setAdminStateUp(loadBalancer.getLoadBalancerAdminStateUp());
        if (loadBalancer.getLoadBalancerDescription() != null) {
            loadBalancersBuilder.setDescr(loadBalancer.getLoadBalancerDescription());
        }
        if (loadBalancer.getLoadBalancerName() != null) {
            loadBalancersBuilder.setName(loadBalancer.getLoadBalancerName());
        }
        if (loadBalancer.getLoadBalancerStatus() != null) {
            loadBalancersBuilder.setStatus(loadBalancer.getLoadBalancerStatus());
        }
        if (loadBalancer.getLoadBalancerTenantID() != null && !loadBalancer.getLoadBalancerTenantID().isEmpty()) {
            loadBalancersBuilder.setTenantId(toUuid(loadBalancer.getLoadBalancerTenantID()));
        }
        if (loadBalancer.getLoadBalancerVipAddress() != null) {
            loadBalancersBuilder.setVipAddress(new IpAddress(loadBalancer.getLoadBalancerVipAddress().toCharArray()));
        }
        if (loadBalancer.getLoadBalancerVipSubnetID() != null) {
            loadBalancersBuilder.setVipSubnetId(toUuid(loadBalancer.getLoadBalancerVipSubnetID()));
        }
        if (loadBalancer.getLoadBalancerID() != null) {
            loadBalancersBuilder.setUuid(toUuid(loadBalancer.getLoadBalancerID()));
        } else {
            LOGGER.warn("Attempting to write neutron load balancer without UUID");
        }
        return loadBalancersBuilder.build();
    }
}
