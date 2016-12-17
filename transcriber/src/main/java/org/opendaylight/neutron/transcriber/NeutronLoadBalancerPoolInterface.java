/*
 * Copyright (c) 2014, 2015 Red Hat, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.transcriber;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.util.concurrent.CheckedFuture;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.controller.md.sal.binding.api.ReadOnlyTransaction;
import org.opendaylight.controller.md.sal.binding.api.WriteTransaction;
import org.opendaylight.controller.md.sal.common.api.data.LogicalDatastoreType;
import org.opendaylight.controller.md.sal.common.api.data.ReadFailedException;
import org.opendaylight.controller.md.sal.common.api.data.TransactionCommitFailedException;
import org.opendaylight.neutron.spi.INeutronLoadBalancerPoolCRUD;
import org.opendaylight.neutron.spi.NeutronID;
import org.opendaylight.neutron.spi.NeutronLoadBalancerPool;
import org.opendaylight.neutron.spi.NeutronLoadBalancerPoolMember;
import org.opendaylight.neutron.spi.NeutronLoadBalancerSessionPersistence;
import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.inet.types.rev130715.IpAddress;
import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.yang.types.rev130715.Uuid;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.constants.rev150712.ProtocolBase;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.constants.rev150712.ProtocolHttp;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.constants.rev150712.ProtocolHttps;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.constants.rev150712.ProtocolTcp;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.constants.rev150712.ProtocolTerminatedHttps;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.lbaasv2.rev150712.lbaas.attributes.Pools;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.lbaasv2.rev150712.lbaas.attributes.pools.Pool;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.lbaasv2.rev150712.lbaas.attributes.pools.PoolBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.lbaasv2.rev150712.lbaas.attributes.pools.PoolKey;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.lbaasv2.rev150712.lbaas.attributes.pools.pool.Members;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.lbaasv2.rev150712.lbaas.attributes.pools.pool.members.Member;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.lbaasv2.rev150712.lbaas.attributes.pools.pool.members.MemberBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.lbaasv2.rev150712.pool.attributes.SessionPersistenceBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.rev150712.Neutron;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class NeutronLoadBalancerPoolInterface
        extends AbstractNeutronInterface<Pool, Pools, PoolKey, NeutronLoadBalancerPool>
        implements INeutronLoadBalancerPoolCRUD {
    private static final Logger LOGGER = LoggerFactory.getLogger(NeutronLoadBalancerPoolInterface.class);

    private static final ImmutableBiMap<Class<? extends ProtocolBase>,
            String> PROTOCOL_MAP = new ImmutableBiMap.Builder<Class<? extends ProtocolBase>, String>()
                    .put(ProtocolHttp.class, "HTTP").put(ProtocolHttps.class, "HTTPS").put(ProtocolTcp.class, "TCP")
                    .put(ProtocolTerminatedHttps.class, "TERMINATED_HTTPS").build();

    NeutronLoadBalancerPoolInterface(DataBroker db) {
        super(PoolBuilder.class, db);
    }

    @Override
    protected List<Pool> getDataObjectList(Pools pools) {
        return pools.getPool();
    }

    @Override
    protected Pool toMd(NeutronLoadBalancerPool pool) {
        final PoolBuilder poolBuilder = new PoolBuilder();
        poolBuilder.setAdminStateUp(pool.getLoadBalancerPoolAdminIsStateIsUp());
        if (pool.getNeutronLoadBalancerPoolHealthMonitorID() != null) {
            poolBuilder.setHealthmonitorId(toUuid(pool.getNeutronLoadBalancerPoolHealthMonitorID()));
        }
        if (pool.getLoadBalancerPoolLbAlgorithm() != null) {
            poolBuilder.setLbAlgorithm(pool.getLoadBalancerPoolLbAlgorithm());
        }
        if (pool.getLoadBalancerPoolListeners() != null) {
            final List<Uuid> listListener = new ArrayList<Uuid>();
            for (final NeutronID neutronId : pool.getLoadBalancerPoolListeners()) {
                listListener.add(toUuid(neutronId.getID()));
            }
            poolBuilder.setListeners(listListener);
        }
        // because members are another container, we don't want to copy
        // it over, so just skip it here
        if (pool.getName() != null) {
            poolBuilder.setName(pool.getName());
        }
        if (pool.getLoadBalancerPoolProtocol() != null) {
            final ImmutableBiMap<String, Class<? extends ProtocolBase>> mapper = PROTOCOL_MAP.inverse();
            poolBuilder.setProtocol((Class<? extends ProtocolBase>) mapper.get(pool.getLoadBalancerPoolProtocol()));
        }
        if (pool.getLoadBalancerPoolSessionPersistence() != null) {
            final NeutronLoadBalancerSessionPersistence sessionPersistence = pool
                    .getLoadBalancerPoolSessionPersistence();
            final SessionPersistenceBuilder sessionPersistenceBuilder = new SessionPersistenceBuilder();
            sessionPersistenceBuilder.setCookieName(sessionPersistence.getCookieName());
            sessionPersistenceBuilder.setType(sessionPersistence.getType());
            poolBuilder.setSessionPersistence(sessionPersistenceBuilder.build());
        }
        if (pool.getTenantID() != null) {
            poolBuilder.setTenantId(toUuid(pool.getTenantID()));
        }
        if (pool.getID() != null) {
            poolBuilder.setUuid(toUuid(pool.getID()));
        } else {
            LOGGER.warn("Attempting to write neutron load balancer pool without UUID");
        }
        return poolBuilder.build();
    }

    protected NeutronLoadBalancerPool fromMd(Pool pool) {
        final NeutronLoadBalancerPool answer = new NeutronLoadBalancerPool();
        if (pool.isAdminStateUp() != null) {
            answer.setLoadBalancerPoolAdminStateIsUp(pool.isAdminStateUp());
        }
        if (pool.getHealthmonitorId() != null) {
            answer.setNeutronLoadBalancerPoolHealthMonitorID(pool.getHealthmonitorId().getValue());
        }
        if (pool.getLbAlgorithm() != null) {
            answer.setLoadBalancerPoolLbAlgorithm(pool.getLbAlgorithm());
        }
        if (pool.getListeners() != null) {
            final List<NeutronID> ids = new ArrayList<NeutronID>();
            for (final Uuid id : pool.getListeners()) {
                ids.add(new NeutronID(id.getValue()));
            }
            answer.setLoadBalancerPoolListeners(ids);
        }
        if (pool.getMembers() != null) {
            final List<NeutronLoadBalancerPoolMember> members = new ArrayList<NeutronLoadBalancerPoolMember>();
            for (final Member member : pool.getMembers().getMember()) {
                members.add(fromMemberMd(member));
            }
            answer.setLoadBalancerPoolMembers(members);
        }
        if (pool.getName() != null) {
            answer.setName(pool.getName());
        }
        if (pool.getProtocol() != null) {
            answer.setLoadBalancerPoolProtocol(PROTOCOL_MAP.get(pool.getProtocol()));
        }
        if (pool.getSessionPersistence() != null) {
            final NeutronLoadBalancerSessionPersistence sessionPersistence =
                    new NeutronLoadBalancerSessionPersistence();
            sessionPersistence.setCookieName(pool.getSessionPersistence().getCookieName());
            sessionPersistence.setType(pool.getSessionPersistence().getType());

            answer.setLoadBalancerSessionPersistence(sessionPersistence);
        }
        if (pool.getTenantId() != null) {
            answer.setTenantID(pool.getTenantId());
        }
        if (pool.getUuid() != null) {
            answer.setID(pool.getUuid().getValue());
        }
        return answer;
    }

    public boolean neutronLoadBalancerPoolMemberExists(String poolUuid, String uuid) {
        final Member member = readMemberMd(createMemberInstanceIdentifier(toMd(poolUuid), toMemberMd(uuid)));
        if (member == null) {
            return false;
        }
        return true;
    }

    public NeutronLoadBalancerPoolMember getNeutronLoadBalancerPoolMember(String poolUuid, String uuid) {
        final Member member = readMemberMd(createMemberInstanceIdentifier(toMd(poolUuid), toMemberMd(uuid)));
        if (member == null) {
            return null;
        }
        return fromMemberMd(member);
    }

    public List<NeutronLoadBalancerPoolMember> getAllNeutronLoadBalancerPoolMembers(String poolUuid) {
        final Set<NeutronLoadBalancerPoolMember> allLoadBalancerPoolMembers = new HashSet<
                NeutronLoadBalancerPoolMember>();
        final Members members = readMd(createMembersInstanceIdentifier(toMd(poolUuid)));
        if (members != null) {
            for (final Member member : members.getMember()) {
                allLoadBalancerPoolMembers.add(fromMemberMd(member));
            }
        }
        LOGGER.debug("Exiting getLoadBalancerPoolMembers, Found {} OpenStackLoadBalancerPoolMember",
                allLoadBalancerPoolMembers.size());
        final List<NeutronLoadBalancerPoolMember> ans = new ArrayList<NeutronLoadBalancerPoolMember>();
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

    public boolean updateNeutronLoadBalancerPoolMember(String poolUuid, String uuid,
            NeutronLoadBalancerPoolMember delta) {
        if (!neutronLoadBalancerPoolMemberExists(poolUuid, uuid)) {
            return false;
        }
        updateMemberMd(toMd(poolUuid), delta);
        return true;
    }

    public boolean neutronLoadBalancerPoolMemberInUse(String poolUuid, String loadBalancerPoolMemberID) {
        return !neutronLoadBalancerPoolMemberExists(poolUuid, loadBalancerPoolMemberID);
    }

    protected InstanceIdentifier<Member> createMemberInstanceIdentifier(Pool pool, Member item) {
        return InstanceIdentifier.create(Neutron.class).child(Pools.class).child(Pool.class, pool.getKey())
                .child(Members.class).child(Member.class, item.getKey());
    }

    protected InstanceIdentifier<Members> createMembersInstanceIdentifier(Pool pool) {
        return InstanceIdentifier.create(Neutron.class).child(Pools.class).child(Pool.class, pool.getKey())
                .child(Members.class);
    }

    static NeutronLoadBalancerPoolMember fromMemberMd(Member member) {
        final NeutronLoadBalancerPoolMember answer = new NeutronLoadBalancerPoolMember();
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
            answer.setTenantID(member.getTenantId());
        }
        if (member.getWeight() != null) {
            answer.setPoolMemberWeight(member.getWeight());
        }
        return answer;
    }

    protected Member toMemberMd(NeutronLoadBalancerPoolMember member) {
        final MemberBuilder memberBuilder = new MemberBuilder();
        memberBuilder.setAdminStateUp(member.getPoolMemberAdminStateIsUp());
        if (member.getPoolMemberAddress() != null) {
            final IpAddress ipAddress = new IpAddress(member.getPoolMemberAddress().toCharArray());
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
        if (member.getTenantID() != null) {
            memberBuilder.setTenantId(toUuid(member.getTenantID()));
        }
        if (member.getPoolMemberWeight() != null) {
            memberBuilder.setWeight(member.getPoolMemberWeight());
        }
        return memberBuilder.build();
    }

    protected Member toMemberMd(String uuid) {
        final MemberBuilder memberBuilder = new MemberBuilder();
        memberBuilder.setUuid(toUuid(uuid));
        return memberBuilder.build();
    }

    protected <
            T extends org.opendaylight.yangtools.yang.binding.DataObject> T readMemberMd(InstanceIdentifier<T> path) {
        T result = null;
        final ReadOnlyTransaction transaction = getDataBroker().newReadOnlyTransaction();
        final CheckedFuture<Optional<T>,
                ReadFailedException> future = transaction.read(LogicalDatastoreType.CONFIGURATION, path);
        if (future != null) {
            Optional<T> optional;
            try {
                optional = future.checkedGet();
                if (optional.isPresent()) {
                    result = optional.get();
                }
            } catch (final ReadFailedException e) {
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
        final WriteTransaction transaction = getDataBroker().newWriteOnlyTransaction();
        final Member item = toMemberMd(neutronObject);
        final InstanceIdentifier<Member> iid = createMemberInstanceIdentifier(pool, item);
        transaction.put(LogicalDatastoreType.CONFIGURATION, iid, item, true);
        final CheckedFuture<Void, TransactionCommitFailedException> future = transaction.submit();
        try {
            future.get();
        } catch (InterruptedException | ExecutionException e) {
            LOGGER.warn("Transation failed ", e);
            return false;
        }
        return true;
    }

    protected boolean removeMemberMd(Pool pool, Member item) {
        final WriteTransaction transaction = getDataBroker().newWriteOnlyTransaction();
        final InstanceIdentifier<Member> iid = createMemberInstanceIdentifier(pool, item);
        transaction.delete(LogicalDatastoreType.CONFIGURATION, iid);
        final CheckedFuture<Void, TransactionCommitFailedException> future = transaction.submit();
        try {
            future.get();
        } catch (InterruptedException | ExecutionException e) {
            LOGGER.warn("Transation failed ", e);
            return false;
        }
        return true;
    }
}
