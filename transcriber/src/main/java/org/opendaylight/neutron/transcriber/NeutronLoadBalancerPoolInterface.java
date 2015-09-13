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

import java.util.concurrent.ExecutionException;

import org.opendaylight.controller.md.sal.binding.api.BindingTransactionChain;
import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.controller.md.sal.binding.api.ReadOnlyTransaction;
import org.opendaylight.controller.md.sal.binding.api.WriteTransaction;
import org.opendaylight.controller.md.sal.common.api.data.LogicalDatastoreType;
import org.opendaylight.controller.md.sal.common.api.data.ReadFailedException;
import org.opendaylight.controller.md.sal.common.api.data.TransactionCommitFailedException;

import org.opendaylight.controller.sal.binding.api.BindingAwareBroker.ProviderContext;
import org.opendaylight.neutron.spi.INeutronLoadBalancerPoolCRUD;
import org.opendaylight.neutron.spi.NeutronCRUDInterfaces;
import org.opendaylight.neutron.spi.NeutronLoadBalancerPool;
import org.opendaylight.neutron.spi.NeutronLoadBalancerPoolMember;
import org.opendaylight.neutron.spi.NeutronLoadBalancer_SessionPersistence;
import org.opendaylight.neutron.spi.Neutron_ID;

import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.inet.types.rev100924.IpAddress;
import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.yang.types.rev130715.Uuid;

import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.constants.rev160807.ProtocolBase;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.constants.rev160807.ProtocolHttp;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.constants.rev160807.ProtocolHttps;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.constants.rev160807.ProtocolTcp;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.lbaasv2.rev141002.lbaas.attributes.Pools;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.lbaasv2.rev141002.lbaas.attributes.pools.Pool;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.lbaasv2.rev141002.lbaas.attributes.pools.PoolBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.lbaasv2.rev141002.lbaas.attributes.pools.pool.Members;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.lbaasv2.rev141002.lbaas.attributes.pools.pool.members.Member;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.lbaasv2.rev141002.lbaas.attributes.pools.pool.members.MemberBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.lbaasv2.rev141002.pool.attributes.SessionPersistenceBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.rev150325.Neutron;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.util.concurrent.CheckedFuture;

public class NeutronLoadBalancerPoolInterface extends AbstractNeutronInterface<Pool, NeutronLoadBalancerPool> implements INeutronLoadBalancerPoolCRUD {
    private static final Logger LOGGER = LoggerFactory.getLogger(NeutronLoadBalancerPoolInterface.class);

    private static final ImmutableBiMap<Class<? extends ProtocolBase>,String> PROTOCOL_MAP
            = new ImmutableBiMap.Builder<Class<? extends ProtocolBase>,String>()
            .put(ProtocolHttp.class,"HTTP")
            .put(ProtocolHttps.class,"HTTPS")
            .put(ProtocolTcp.class,"TCP")
            .build();

    private static final int DEDASHED_UUID_LENGTH = 32;
    private static final int DEDASHED_UUID_START = 0;
    private static final int DEDASHED_UUID_DIV1 = 8;
    private static final int DEDASHED_UUID_DIV2 = 12;
    private static final int DEDASHED_UUID_DIV3 = 16;
    private static final int DEDASHED_UUID_DIV4 = 20;

    NeutronLoadBalancerPoolInterface(ProviderContext providerContext) {
        super(providerContext);
    }

    @Override
    public boolean neutronLoadBalancerPoolExists(String uuid) {
        return exists(uuid, null);
    }

    @Override
    public NeutronLoadBalancerPool getNeutronLoadBalancerPool(String uuid) {
        return get(uuid, null);
    }

    @Override
    protected List<NeutronLoadBalancerPool> _getAll(BindingTransactionChain chain) {
        Preconditions.checkNotNull(chain);

        Set<NeutronLoadBalancerPool> allLoadBalancerPools = new HashSet<NeutronLoadBalancerPool>();
        Pools pools = readMd(createInstanceIdentifier(), chain);
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
    public List<NeutronLoadBalancerPool> getAllNeutronLoadBalancerPools() {
        return getAll(null);
    }

    @Override
    public boolean addNeutronLoadBalancerPool(NeutronLoadBalancerPool input) {
        //TODO: add code to find INeutronLoadBalancerPoolAware services and call newtorkCreated on them
        return add(input, null);
    }

    @Override
    public boolean removeNeutronLoadBalancerPool(String uuid) {
        return remove(uuid, null);
    }

    @Override
    public boolean updateNeutronLoadBalancerPool(String uuid, NeutronLoadBalancerPool delta) {
        return update(uuid, delta, null);
    }

    @Override
    public boolean neutronLoadBalancerPoolInUse(String loadBalancerPoolUUID) {
        return !exists(loadBalancerPoolUUID, null);
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
            List<NeutronLoadBalancerPoolMember> members = new ArrayList<NeutronLoadBalancerPoolMember>();
            for (Member member: pool.getMembers().getMember()) {
                members.add(fromMemberMd(member));
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

    public boolean neutronLoadBalancerPoolMemberExists(String poolUuid, String uuid) {
        Member member = readMemberMd(
                createMemberInstanceIdentifier(toMd(poolUuid),
                                               toMemberMd(uuid)));
        if (member == null) {
            return false;
        }
        return true;
    }

    public NeutronLoadBalancerPoolMember getNeutronLoadBalancerPoolMember(
            String poolUuid, String uuid) {
        Member member = readMemberMd(
                createMemberInstanceIdentifier(toMd(poolUuid),
                                               toMemberMd(uuid)));
        if (member == null) {
            return null;
        }
        return fromMemberMd(member);
    }

    public List<NeutronLoadBalancerPoolMember> getAllNeutronLoadBalancerPoolMembers(String poolUuid) {
        Set<NeutronLoadBalancerPoolMember> allLoadBalancerPoolMembers = new HashSet<NeutronLoadBalancerPoolMember>();
        Members members = readMd(createMembersInstanceIdentifier(toMd(poolUuid)));
        if (members != null) {
            for (Member member: members.getMember()) {
                allLoadBalancerPoolMembers.add(fromMemberMd(member));
            }
        }
        LOGGER.debug("Exiting getLoadBalancerPoolMembers, Found {} OpenStackLoadBalancerPoolMember",
                allLoadBalancerPoolMembers.size());
        List<NeutronLoadBalancerPoolMember> ans = new ArrayList<NeutronLoadBalancerPoolMember>();
        ans.addAll(allLoadBalancerPoolMembers);
        return ans;
    }

    public boolean addNeutronLoadBalancerPoolMember(String poolUuid, NeutronLoadBalancerPoolMember input) {
        if (neutronLoadBalancerPoolMemberExists(poolUuid, input.getID())) {
            return false;
        }
        addMemberMd(toMd(poolUuid), input);
        return true;
    }

    public boolean removeNeutronLoadBalancerPoolMember(String poolUuid, String uuid) {
        if (!neutronLoadBalancerPoolMemberExists(poolUuid, uuid)) {
            return false;
        }
        return removeMemberMd(toMd(poolUuid), toMemberMd(uuid));
    }

    public boolean updateNeutronLoadBalancerPoolMember(String poolUuid,
            String uuid, NeutronLoadBalancerPoolMember delta) {
        if (!neutronLoadBalancerPoolMemberExists(poolUuid, uuid)) {
            return false;
        }
        updateMemberMd(toMd(poolUuid), delta);
        return true;
    }

    public boolean neutronLoadBalancerPoolMemberInUse(String poolUuid,
            String loadBalancerPoolMemberID) {
        return !neutronLoadBalancerPoolMemberExists(poolUuid,
                                                    loadBalancerPoolMemberID);
    }

    protected InstanceIdentifier<Member> createMemberInstanceIdentifier(Pool pool,
            Member item) {
        return InstanceIdentifier.create(Neutron.class)
            .child(Pools.class)
            .child(Pool.class, pool.getKey())
            .child(Members.class)
            .child(Member.class, item.getKey());
    }

    protected InstanceIdentifier<Members> createMembersInstanceIdentifier(Pool pool) {
        return InstanceIdentifier.create(Neutron.class)
            .child(Pools.class)
            .child(Pool.class, pool.getKey())
            .child(Members.class);
    }

    static NeutronLoadBalancerPoolMember fromMemberMd(Member member) {
        NeutronLoadBalancerPoolMember answer = new NeutronLoadBalancerPoolMember();
        if (member.isAdminStateUp() != null) {
            answer.setPoolMemberAdminStateIsUp(member.isAdminStateUp());
        }
        if (member.getAddress() != null) {
            answer.setPoolMemberAddress(String.valueOf(member.getAddress().getValue()));
        }
        if (member.getProtocolPort() != null) {
            answer.setPoolMemberProtoPort(member.getProtocolPort());
        }
        if (member.getUuid() != null) {
            answer.setID(member.getUuid().getValue());
            answer.setPoolID(member.getUuid().getValue());
        }
        if (member.getSubnetId() != null) {
            answer.setPoolMemberSubnetID(member.getSubnetId().getValue());
        }
        if (member.getTenantId() != null) {
            answer.setPoolMemberTenantID(member.getTenantId().getValue().replace("-",""));
        }
        if (member.getWeight() != null) {
            answer.setPoolMemberWeight(member.getWeight());
        }
        return answer;
    }

    protected Member toMemberMd(NeutronLoadBalancerPoolMember member) {
        MemberBuilder memberBuilder = new MemberBuilder();
        memberBuilder.setAdminStateUp(member.getPoolMemberAdminStateIsUp());
        if (member.getPoolMemberAddress() != null) {
            IpAddress ipAddress = new IpAddress(member.getPoolMemberAddress().toCharArray());
            memberBuilder.setAddress(ipAddress);
        }
        if (member.getPoolMemberProtoPort() != null) {
            memberBuilder.setProtocolPort(member.getPoolMemberProtoPort());
        }
        if (member.getID() != null) {
            memberBuilder.setUuid(toUuid(member.getID()));
        }
        if (member.getPoolMemberSubnetID() != null) {
            memberBuilder.setSubnetId(toUuid(member.getPoolMemberSubnetID()));
        }
        if (member.getPoolMemberTenantID() != null) {
            memberBuilder.setTenantId(toUuid(member.getPoolMemberTenantID()));
        }
        if (member.getPoolMemberWeight() != null) {
            memberBuilder.setWeight(member.getPoolMemberWeight());
        }
        return memberBuilder.build();
    }

    protected Member toMemberMd(String uuid) {
        MemberBuilder memberBuilder = new MemberBuilder();
        memberBuilder.setUuid(toUuid(uuid));
        return memberBuilder.build();
    }

    protected <T extends org.opendaylight.yangtools.yang.binding.DataObject> T readMemberMd(InstanceIdentifier<T> path) {
        T result = null;
        final ReadOnlyTransaction transaction = getDataBroker().newReadOnlyTransaction();
        CheckedFuture<Optional<T>, ReadFailedException> future = transaction.read(LogicalDatastoreType.CONFIGURATION, path);
        if (future != null) {
            Optional<T> optional;
            try {
                optional = future.checkedGet();
                if (optional.isPresent()) {
                    result = optional.get();
                }
            } catch (ReadFailedException e) {
                LOGGER.warn("Failed to read {}", path, e);
            }
        }
        transaction.close();
        return result;
    }

    protected boolean addMemberMd(Pool pool, NeutronLoadBalancerPoolMember neutronObject) {
        // TODO think about adding existence logic
        return updateMemberMd(pool, neutronObject);
    }

    protected boolean updateMemberMd(Pool pool, NeutronLoadBalancerPoolMember neutronObject) {
        WriteTransaction transaction = getDataBroker().newWriteOnlyTransaction();
        Member item = toMemberMd(neutronObject);
        InstanceIdentifier<Member> iid = createMemberInstanceIdentifier(pool, item);
        transaction.put(LogicalDatastoreType.CONFIGURATION, iid, item,true);
        CheckedFuture<Void, TransactionCommitFailedException> future = transaction.submit();
        try {
            future.get();
        } catch (InterruptedException | ExecutionException e) {
            LOGGER.warn("Transation failed ",e);
            return false;
        }
        return true;
    }

    protected boolean removeMemberMd(Pool pool, Member item) {
        WriteTransaction transaction = getDataBroker().newWriteOnlyTransaction();
        InstanceIdentifier<Member> iid = createMemberInstanceIdentifier(pool, item);
        transaction.delete(LogicalDatastoreType.CONFIGURATION, iid);
        CheckedFuture<Void, TransactionCommitFailedException> future = transaction.submit();
        try {
            future.get();
        } catch (InterruptedException | ExecutionException e) {
            LOGGER.warn("Transation failed ",e);
            return false;
        }
        return true;
    }
}
