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
import org.opendaylight.neutron.spi.INeutronLoadBalancerPoolCRUD;
import org.opendaylight.neutron.spi.INeutronLoadBalancerPoolMemberCRUD;
import org.opendaylight.neutron.spi.NeutronCRUDInterfaces;
import org.opendaylight.neutron.spi.NeutronLoadBalancerPool;
import org.opendaylight.neutron.spi.NeutronLoadBalancerPoolMember;
import org.opendaylight.neutron.spi.NeutronLoadBalancer_SessionPersistence;
import org.opendaylight.neutron.spi.Neutron_ID;
import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.yang.types.rev130715.Uuid;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.constants.rev160807.ProtocolBase;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.constants.rev160807.ProtocolHttp;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.constants.rev160807.ProtocolHttps;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.constants.rev160807.ProtocolTcp;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.lbaasv2.rev141002.lbaas.attributes.Pools;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.lbaasv2.rev141002.lbaas.attributes.pools.Pool;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.lbaasv2.rev141002.lbaas.attributes.pools.PoolBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.lbaasv2.rev141002.lbaas.attributes.pools.pool.members.Member;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.lbaasv2.rev141002.pool.attributes.SessionPersistenceBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.rev150325.Neutron;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableBiMap;

public class NeutronLoadBalancerPoolInterface extends AbstractNeutronInterface<Pool, NeutronLoadBalancerPool> implements INeutronLoadBalancerPoolCRUD {
    private static final Logger LOGGER = LoggerFactory.getLogger(NeutronLoadBalancerPoolInterface.class);

    private static final ImmutableBiMap<Class<? extends ProtocolBase>,String> PROTOCOL_MAP
            = new ImmutableBiMap.Builder<Class<? extends ProtocolBase>,String>()
            .put(ProtocolHttp.class,"HTTP")
            .put(ProtocolHttps.class,"HTTPS")
            .put(ProtocolTcp.class,"TCP")
            .build();

    NeutronLoadBalancerPoolInterface(ProviderContext providerContext) {
        super(providerContext);
    }

    @Override
    public boolean neutronLoadBalancerPoolExists(String uuid) {
        Pool pool = readMd(createInstanceIdentifier(toMd(uuid)));
        if (pool == null) {
            return false;
        }
        return true;
    }

    @Override
    public NeutronLoadBalancerPool getNeutronLoadBalancerPool(String uuid) {
        Pool pool = readMd(createInstanceIdentifier(toMd(uuid)));
        if (pool == null) {
            return null;
        }
        return fromMd(pool);
    }

    @Override
    public List<NeutronLoadBalancerPool> getAllNeutronLoadBalancerPools() {
        Set<NeutronLoadBalancerPool> allLoadBalancerPools = new HashSet<NeutronLoadBalancerPool>();
        Pools pools = readMd(createInstanceIdentifier());
        if (pools != null) {
            for (Pool pool: pools.getPool()) {
                allLoadBalancerPools.add(fromMd(pool));
            }
        }
        LOGGER.debug("Exiting getLoadBalancerPools, Found {} OpenStackLoadBalancerPool", allLoadBalancerPools.size());
        List<NeutronLoadBalancerPool> ans = new ArrayList<NeutronLoadBalancerPool>();
        ans.addAll(allLoadBalancerPools);
        return ans;
    }

    @Override
    public boolean addNeutronLoadBalancerPool(NeutronLoadBalancerPool input) {
        if (neutronLoadBalancerPoolExists(input.getID())) {
            return false;
        }
        addMd(input);
        //TODO: add code to find INeutronLoadBalancerPoolAware services and call newtorkCreated on them
        return true;
    }

    @Override
    public boolean removeNeutronLoadBalancerPool(String uuid) {
        if (!neutronLoadBalancerPoolExists(uuid)) {
            return false;
        }
        return removeMd(toMd(uuid));
    }

    @Override
    public boolean updateNeutronLoadBalancerPool(String uuid, NeutronLoadBalancerPool delta) {
        if (!neutronLoadBalancerPoolExists(uuid)) {
            return false;
        }
        updateMd(delta);
        return true;
    }

    @Override
    public boolean neutronLoadBalancerPoolInUse(String loadBalancerPoolUUID) {
        return !neutronLoadBalancerPoolExists(loadBalancerPoolUUID);
    }

    @Override
    protected Pool toMd(String uuid) {
        PoolBuilder poolBuilder = new PoolBuilder();
        poolBuilder.setUuid(toUuid(uuid));
        return poolBuilder.build();
    }

    @Override
    protected InstanceIdentifier<Pool> createInstanceIdentifier(Pool pool) {
        return InstanceIdentifier.create(Neutron.class)
                .child(Pools.class)
                .child(Pool.class, pool.getKey());
    }

    protected InstanceIdentifier<Pools> createInstanceIdentifier() {
        return InstanceIdentifier.create(Neutron.class)
                .child(Pools.class);
    }

    @Override
    protected Pool toMd(NeutronLoadBalancerPool pool) {
        PoolBuilder poolBuilder = new PoolBuilder();
        poolBuilder.setAdminStateUp(pool.getLoadBalancerPoolAdminIsStateIsUp());
        if (pool.getLoadBalancerPoolDescription() != null) {
            poolBuilder.setDescr(pool.getLoadBalancerPoolDescription());
        }
        if (pool.getNeutronLoadBalancerPoolHealthMonitorID() != null) {
            poolBuilder.setHealthmonitorId(toUuid(pool.getNeutronLoadBalancerPoolHealthMonitorID()));
        }
        if (pool.getLoadBalancerPoolLbAlgorithm() != null) {
            poolBuilder.setLbAlgorithm(pool.getLoadBalancerPoolLbAlgorithm());
        }
        if (pool.getLoadBalancerPoolListeners() != null) {
            List<Uuid> listListener = new ArrayList<Uuid>();
            for (Neutron_ID neutron_id : pool.getLoadBalancerPoolListeners()) {
                listListener.add(toUuid(neutron_id.getID()));
            }
            poolBuilder.setListeners(listListener);
        }
        // because members are another container, we don't want to copy
        // it over, so just skip it here
        if (pool.getLoadBalancerPoolName() != null) {
            poolBuilder.setName(pool.getLoadBalancerPoolName());
        }
        if (pool.getLoadBalancerPoolProtocol() != null) {
            ImmutableBiMap<String, Class<? extends ProtocolBase>> mapper =
                PROTOCOL_MAP.inverse();
            poolBuilder.setProtocol((Class<? extends ProtocolBase>) mapper.get(pool.getLoadBalancerPoolProtocol()));
        }
        if (pool.getLoadBalancerPoolSessionPersistence() != null) {
            NeutronLoadBalancer_SessionPersistence sessionPersistence = pool.getLoadBalancerPoolSessionPersistence();
            SessionPersistenceBuilder sessionPersistenceBuilder = new SessionPersistenceBuilder();
            sessionPersistenceBuilder.setCookieName(sessionPersistence.getCookieName());
            sessionPersistenceBuilder.setType(sessionPersistence.getType());
            poolBuilder.setSessionPersistence(sessionPersistenceBuilder.build());
        }
        if (pool.getLoadBalancerPoolTenantID() != null) {
            poolBuilder.setTenantId(toUuid(pool.getLoadBalancerPoolTenantID()));
        }
        if (pool.getID() != null) {
            poolBuilder.setUuid(toUuid(pool.getID()));
        } else {
            LOGGER.warn("Attempting to write neutron load balancer pool without UUID");
        }
        return poolBuilder.build();
    }

    protected NeutronLoadBalancerPool fromMd(Pool pool) {
        NeutronLoadBalancerPool answer = new NeutronLoadBalancerPool();
        if (pool.isAdminStateUp() != null) {
            answer.setLoadBalancerPoolAdminStateIsUp(pool.isAdminStateUp());
        }
        if (pool.getDescr() != null) {
            answer.setLoadBalancerPoolDescription(pool.getDescr());
        }
        if (pool.getHealthmonitorId() != null) {
            answer.setNeutronLoadBalancerPoolHealthMonitorID(pool.getHealthmonitorId().getValue());
        }
        if (pool.getLbAlgorithm() != null) {
            answer.setLoadBalancerPoolLbAlgorithm(pool.getLbAlgorithm());
        }
        if (pool.getListeners() != null) {
            List<Neutron_ID> ids = new ArrayList<Neutron_ID>();
            for (Uuid id : pool.getListeners()) {
                ids.add(new Neutron_ID(id.getValue()));
            }
            answer.setLoadBalancerPoolListeners(ids);
        }
        if (pool.getMembers() != null) {
            NeutronCRUDInterfaces interfaces = new NeutronCRUDInterfaces()
                .fetchINeutronLoadBalancerPoolMemberCRUD(this);
            INeutronLoadBalancerPoolMemberCRUD mI = interfaces.getLoadBalancerPoolMemberInterface();

            List<NeutronLoadBalancerPoolMember> members = new ArrayList<NeutronLoadBalancerPoolMember>();
            for (Member member: pool.getMembers().getMember()) {
                members.add(NeutronLoadBalancerPoolMemberInterface.fromMd(member));
            }
            answer.setLoadBalancerPoolMembers(members);
        }
        if (pool.getName() != null) {
            answer.setLoadBalancerPoolName(pool.getName());
        }
        if (pool.getProtocol() != null) {
            answer.setLoadBalancerPoolProtocol(PROTOCOL_MAP.get(pool.getProtocol()));
        }
        if (pool.getSessionPersistence() != null) {
            NeutronLoadBalancer_SessionPersistence sessionPersistence = new NeutronLoadBalancer_SessionPersistence();
            sessionPersistence.setCookieName(pool.getSessionPersistence().getCookieName());
            sessionPersistence.setType(pool.getSessionPersistence().getType());
            
            answer.setLoadBalancerSessionPersistence(sessionPersistence);
        }
        if (pool.getTenantId() != null) {
            answer.setLoadBalancerPoolTenantID(pool.getTenantId().getValue().replace("-",""));
        }
        if (pool.getUuid() != null) {
            answer.setID(pool.getUuid().getValue());
        }
        return answer;
    }

    public static void registerNewInterface(BundleContext context,
                                            ProviderContext providerContext,
                                            List<ServiceRegistration<?>> registrations) {
        NeutronLoadBalancerPoolInterface neutronLoadBalancerPoolInterface = new NeutronLoadBalancerPoolInterface(providerContext);
        ServiceRegistration<INeutronLoadBalancerPoolCRUD> neutronLoadBalancerPoolInterfaceRegistration = context.registerService(INeutronLoadBalancerPoolCRUD.class, neutronLoadBalancerPoolInterface, null);
        if(neutronLoadBalancerPoolInterfaceRegistration != null) {
            registrations.add(neutronLoadBalancerPoolInterfaceRegistration);
        }
    }
}
