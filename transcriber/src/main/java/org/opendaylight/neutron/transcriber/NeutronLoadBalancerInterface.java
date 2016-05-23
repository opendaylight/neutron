/*
 * Copyright (c) 2014, 2015 Red Hat, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.transcriber;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.opendaylight.controller.sal.binding.api.BindingAwareBroker.ProviderContext;
import org.opendaylight.neutron.spi.INeutronLoadBalancerCRUD;
import org.opendaylight.neutron.spi.NeutronLoadBalancer;
import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.inet.types.rev100924.IpAddress;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.lbaasv2.rev150712.lbaas.attributes.Loadbalancers;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.lbaasv2.rev150712.lbaas.attributes.loadbalancers.Loadbalancer;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.lbaasv2.rev150712.lbaas.attributes.loadbalancers.LoadbalancerBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.rev150712.Neutron;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NeutronLoadBalancerInterface extends AbstractNeutronInterface<Loadbalancer, Loadbalancers, NeutronLoadBalancer> implements INeutronLoadBalancerCRUD {
    private static final Logger LOGGER = LoggerFactory.getLogger(NeutronLoadBalancerInterface.class);


    NeutronLoadBalancerInterface(ProviderContext providerContext) {
        super(providerContext);
    }

    @Override
    public boolean neutronLoadBalancerExists(String uuid) {
        return exists(uuid);
    }

    @Override
    public NeutronLoadBalancer getNeutronLoadBalancer(String uuid) {
        return get(uuid);
    }

    @Override
    protected List<Loadbalancer> getDataObjectList(Loadbalancers lbs) {
        return lbs.getLoadbalancer();
    }

    @Override
    public List<NeutronLoadBalancer> getAllNeutronLoadBalancers() {
        return getAll();
    }

    @Override
    public boolean addNeutronLoadBalancer(NeutronLoadBalancer input) {
        return add(input);
    }

    @Override
    public boolean removeNeutronLoadBalancer(String uuid) {
        return remove(uuid);
    }

    @Override
    public boolean updateNeutronLoadBalancer(String uuid, NeutronLoadBalancer delta) {
        return update(uuid, delta);
    }

    @Override
    public boolean neutronLoadBalancerInUse(String loadBalancerUUID) {
        return !exists(loadBalancerUUID);
    }

    @Override
    protected Loadbalancer toMd(String uuid) {
        LoadbalancerBuilder loadBalancerBuilder = new LoadbalancerBuilder();
        loadBalancerBuilder.setUuid(toUuid(uuid));
        return loadBalancerBuilder.build();
    }

    @Override
    protected InstanceIdentifier<Loadbalancer> createInstanceIdentifier(
            Loadbalancer loadBalancer) {
        return InstanceIdentifier.create(Neutron.class)
                .child(Loadbalancers.class)
                .child(Loadbalancer.class, loadBalancer.getKey());
    }

    @Override
    protected InstanceIdentifier<Loadbalancers> createInstanceIdentifier() {
        return InstanceIdentifier.create(Neutron.class)
                .child(Loadbalancers.class);
    }

    protected NeutronLoadBalancer fromMd(Loadbalancer loadBalancer) {
        NeutronLoadBalancer answer = new NeutronLoadBalancer();
        if (loadBalancer.isAdminStateUp() != null) {
            answer.setLoadBalancerAdminStateUp(loadBalancer.isAdminStateUp());
        }
        if (loadBalancer.getName() != null) {
            answer.setLoadBalancerName(loadBalancer.getName());
        }
        if (loadBalancer.getStatus() != null) {
            answer.setLoadBalancerStatus(loadBalancer.getStatus());
        }
        if (loadBalancer.getTenantId() != null) {
            answer.setTenantID(loadBalancer.getTenantId());
        }
        if (loadBalancer.getVipAddress() != null) {
            answer.setLoadBalancerVipAddress(String.valueOf(loadBalancer.getVipAddress().getValue()));
        }
        if (loadBalancer.getVipSubnetId() != null) {
            answer.setLoadBalancerVipSubnetID(loadBalancer.getVipSubnetId().getValue());
        }
        if (loadBalancer.getUuid() != null) {
            answer.setID(loadBalancer.getUuid().getValue());
        }
        return answer;
    }

    @Override
    protected Loadbalancer toMd(NeutronLoadBalancer loadBalancer) {
        LoadbalancerBuilder loadBalancerBuilder = new LoadbalancerBuilder();
        loadBalancerBuilder.setAdminStateUp(loadBalancer.getLoadBalancerAdminStateUp());
        if (loadBalancer.getLoadBalancerName() != null) {
            loadBalancerBuilder.setName(loadBalancer.getLoadBalancerName());
        }
        if (loadBalancer.getLoadBalancerStatus() != null) {
            loadBalancerBuilder.setStatus(loadBalancer.getLoadBalancerStatus());
        }
        if (loadBalancer.getTenantID() != null) {
            loadBalancerBuilder.setTenantId(toUuid(loadBalancer.getTenantID()));
        }
        if (loadBalancer.getLoadBalancerVipAddress() != null) {
            loadBalancerBuilder.setVipAddress(new IpAddress(loadBalancer.getLoadBalancerVipAddress().toCharArray()));
        }
        if (loadBalancer.getLoadBalancerVipSubnetID() != null) {
            loadBalancerBuilder.setVipSubnetId(toUuid(loadBalancer.getLoadBalancerVipSubnetID()));
        }
        if (loadBalancer.getID() != null) {
            loadBalancerBuilder.setUuid(toUuid(loadBalancer.getID()));
        } else {
            LOGGER.warn("Attempting to write neutron load balancer without UUID");
        }
        return loadBalancerBuilder.build();
    }

    public static void registerNewInterface(BundleContext context,
                                            ProviderContext providerContext,
                                            List<ServiceRegistration<?>> registrations) {
        NeutronLoadBalancerInterface neutronLoadBalancerInterface = new NeutronLoadBalancerInterface(providerContext);
        ServiceRegistration<INeutronLoadBalancerCRUD> neutronLoadBalancerInterfaceRegistration = context.registerService(INeutronLoadBalancerCRUD.class, neutronLoadBalancerInterface, null);
        if(neutronLoadBalancerInterfaceRegistration != null) {
            registrations.add(neutronLoadBalancerInterfaceRegistration);
        }
    }
}
