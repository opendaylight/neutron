/*
 * Copyright (c) 2014, 2015 Red Hat, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.neutron.transcriber;

import com.google.common.collect.ImmutableBiMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.apache.aries.blueprint.annotation.service.Service;
import org.opendaylight.mdsal.binding.api.DataBroker;
import org.opendaylight.mdsal.binding.api.WriteTransaction;
import org.opendaylight.mdsal.common.api.LogicalDatastoreType;
import org.opendaylight.mdsal.common.api.ReadFailedException;
import org.opendaylight.mdsal.common.api.TransactionCommitFailedException;
import org.opendaylight.neutron.spi.INeutronLoadBalancerPoolCRUD;
import org.opendaylight.neutron.spi.NeutronID;
import org.opendaylight.neutron.spi.NeutronLoadBalancerPool;
import org.opendaylight.neutron.spi.NeutronLoadBalancerPoolMember;
import org.opendaylight.neutron.spi.NeutronLoadBalancerSessionPersistence;
import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.inet.types.rev130715.IpAddress;
import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.inet.types.rev130715.IpAddressBuilder;
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
import org.opendaylight.yangtools.yang.common.OperationFailedException;
import org.opendaylight.yangtools.yang.common.Uint16;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
@Service(classes = INeutronLoadBalancerPoolCRUD.class)
public final class NeutronLoadBalancerPoolInterface
        extends AbstractNeutronInterface<Pool, Pools, PoolKey, NeutronLoadBalancerPool>
        implements INeutronLoadBalancerPoolCRUD {

    private static final Logger LOG = LoggerFactory.getLogger(NeutronLoadBalancerPoolInterface.class);

    private static final ImmutableBiMap<Class<? extends ProtocolBase>,
            String> PROTOCOL_MAP = new ImmutableBiMap.Builder<Class<? extends ProtocolBase>, String>()
                    .put(ProtocolHttp.class, "HTTP").put(ProtocolHttps.class, "HTTPS").put(ProtocolTcp.class, "TCP")
                    .put(ProtocolTerminatedHttps.class, "TERMINATED_HTTPS").build();

    @Inject
    public NeutronLoadBalancerPoolInterface(DataBroker db) {
        super(PoolBuilder.class, db);
    }

    @Override
    protected Collection<Pool> getDataObjectList(Pools pools) {
        return pools.nonnullPool().values();
    }

    @Override
    protected Pool toMd(NeutronLoadBalancerPool pool) {
        final PoolBuilder poolBuilder = new PoolBuilder();
        toMdBaseAttributes(pool, poolBuilder);
        poolBuilder.setAdminStateUp(pool.getLoadBalancerPoolAdminIsStateIsUp());
        if (pool.getLoadBalancerPoolHealthMonitorID() != null) {
            poolBuilder.setHealthmonitorId(toUuid(pool.getLoadBalancerPoolHealthMonitorID()));
        }
        if (pool.getLoadBalancerPoolLbAlgorithm() != null) {
            poolBuilder.setLbAlgorithm(pool.getLoadBalancerPoolLbAlgorithm());
        }
        if (pool.getLoadBalancerPoolListeners() != null) {
            final List<Uuid> listListener = new ArrayList<>();
            for (final NeutronID neutronId : pool.getLoadBalancerPoolListeners()) {
                listListener.add(toUuid(neutronId.getID()));
            }
            poolBuilder.setListeners(listListener);
        }
        // because members are another container, we don't want to copy
        // it over, so just skip it here
        if (pool.getLoadBalancerPoolProtocol() != null) {
            final ImmutableBiMap<String, Class<? extends ProtocolBase>> mapper = PROTOCOL_MAP.inverse();
            poolBuilder.setProtocol(mapper.get(pool.getLoadBalancerPoolProtocol()));
        }
        if (pool.getLoadBalancerPoolSessionPersistence() != null) {
            final NeutronLoadBalancerSessionPersistence sessionPersistence = pool
                    .getLoadBalancerPoolSessionPersistence();
            final SessionPersistenceBuilder sessionPersistenceBuilder = new SessionPersistenceBuilder();
            sessionPersistenceBuilder.setCookieName(sessionPersistence.getCookieName());
            sessionPersistenceBuilder.setType(sessionPersistence.getType());
            poolBuilder.setSessionPersistence(sessionPersistenceBuilder.build());
        }
        return poolBuilder.build();
    }

    @Override
    protected NeutronLoadBalancerPool fromMd(Pool pool) {
        final NeutronLoadBalancerPool answer = new NeutronLoadBalancerPool();
        fromMdBaseAttributes(pool, answer);
        if (pool.getAdminStateUp() != null) {
            answer.setLoadBalancerPoolAdminStateIsUp(pool.getAdminStateUp());
        }
        if (pool.getHealthmonitorId() != null) {
            answer.setLoadBalancerPoolHealthMonitorID(pool.getHealthmonitorId().getValue());
        }
        if (pool.getLbAlgorithm() != null) {
            answer.setLoadBalancerPoolLbAlgorithm(pool.getLbAlgorithm());
        }
        if (pool.getListeners() != null) {
            final List<NeutronID> ids = new ArrayList<>();
            for (final Uuid id : pool.getListeners()) {
                ids.add(new NeutronID(id.getValue()));
            }
            answer.setLoadBalancerPoolListeners(ids);
        }
        if (pool.getMembers() != null) {
            final List<NeutronLoadBalancerPoolMember> members = new ArrayList<>();
            for (final Member member : pool.getMembers().nonnullMember().values()) {
                members.add(fromMemberMd(member));
            }
            answer.setLoadBalancerPoolMembers(members);
        }
        if (pool.getProtocol() != null) {
            answer.setLoadBalancerPoolProtocol(PROTOCOL_MAP.get(pool.getProtocol()));
        }
        if (pool.getSessionPersistence() != null) {
            NeutronLoadBalancerSessionPersistence sessionPersistence = new NeutronLoadBalancerSessionPersistence();
            sessionPersistence.setCookieName(pool.getSessionPersistence().getCookieName());
            sessionPersistence.setType(pool.getSessionPersistence().getType());

            answer.setLoadBalancerSessionPersistence(sessionPersistence);
        }
        return answer;
    }

    @Override
    public boolean neutronLoadBalancerPoolMemberExists(String poolUuid, String uuid) throws ReadFailedException {
        final Member member = readMd(createMemberInstanceIdentifier(toMd(poolUuid), toMemberMd(uuid)));
        if (member == null) {
            return false;
        }
        return true;
    }

    @Override
    public NeutronLoadBalancerPoolMember getNeutronLoadBalancerPoolMember(String poolUuid, String uuid)
            throws ReadFailedException {
        final Member member = readMd(createMemberInstanceIdentifier(toMd(poolUuid), toMemberMd(uuid)));
        if (member == null) {
            return null;
        }
        return fromMemberMd(member);
    }

    @Override
    public List<NeutronLoadBalancerPoolMember> getAllNeutronLoadBalancerPoolMembers(String poolUuid)
            throws ReadFailedException {
        final Set<NeutronLoadBalancerPoolMember> allLoadBalancerPoolMembers = new HashSet<>();
        final Members members = readMd(createMembersInstanceIdentifier(toMd(poolUuid)));
        if (members != null) {
            for (final Member member : members.nonnullMember().values()) {
                allLoadBalancerPoolMembers.add(fromMemberMd(member));
            }
        }
        LOG.debug("Exiting getLoadBalancerPoolMembers, Found {} OpenStackLoadBalancerPoolMember",
                allLoadBalancerPoolMembers.size());
        final List<NeutronLoadBalancerPoolMember> ans = new ArrayList<>();
        ans.addAll(allLoadBalancerPoolMembers);
        return ans;
    }

    @Override
    public boolean addNeutronLoadBalancerPoolMember(String poolUuid, NeutronLoadBalancerPoolMember input)
            throws OperationFailedException {
        if (neutronLoadBalancerPoolMemberExists(poolUuid, input.getID())) {
            return false;
        }
        addMemberMd(toMd(poolUuid), input);
        return true;
    }

    @Override
    public boolean removeNeutronLoadBalancerPoolMember(String poolUuid, String uuid) throws OperationFailedException {
        if (!neutronLoadBalancerPoolMemberExists(poolUuid, uuid)) {
            return false;
        }
        removeMemberMd(toMd(poolUuid), toMemberMd(uuid));
        return true;
    }

    @Override
    public boolean updateNeutronLoadBalancerPoolMember(String poolUuid, String uuid,
            NeutronLoadBalancerPoolMember delta) throws OperationFailedException {
        if (!neutronLoadBalancerPoolMemberExists(poolUuid, uuid)) {
            return false;
        }
        updateMemberMd(toMd(poolUuid), delta);
        return true;
    }

    @Override
    public boolean neutronLoadBalancerPoolMemberInUse(String poolUuid, String loadBalancerPoolMemberID)
            throws ReadFailedException {
        return !neutronLoadBalancerPoolMemberExists(poolUuid, loadBalancerPoolMemberID);
    }

    static InstanceIdentifier<Member> createMemberInstanceIdentifier(Pool pool, Member item) {
        return InstanceIdentifier.create(Neutron.class).child(Pools.class).child(Pool.class, pool.key())
                .child(Members.class).child(Member.class, item.key());
    }

    static InstanceIdentifier<Members> createMembersInstanceIdentifier(Pool pool) {
        return InstanceIdentifier.create(Neutron.class).child(Pools.class).child(Pool.class, pool.key())
                .child(Members.class);
    }

    protected NeutronLoadBalancerPoolMember fromMemberMd(Member member) {
        final NeutronLoadBalancerPoolMember answer = new NeutronLoadBalancerPoolMember();
        fromMdIds(member, answer);
        if (member.getAdminStateUp() != null) {
            answer.setPoolMemberAdminStateIsUp(member.getAdminStateUp());
        }
        if (member.getAddress() != null) {
            answer.setPoolMemberAddress(member.getAddress().stringValue());
        }
        if (member.getProtocolPort() != null) {
            answer.setPoolMemberProtoPort(member.getProtocolPort().toJava());
        }
        if (member.getSubnetId() != null) {
            answer.setPoolMemberSubnetID(member.getSubnetId().getValue());
        }
        if (member.getWeight() != null) {
            answer.setPoolMemberWeight(member.getWeight().toJava());
        }
        return answer;
    }

    static Member toMemberMd(NeutronLoadBalancerPoolMember member) {
        final MemberBuilder memberBuilder = toMdIds(member, MemberBuilder.class);
        memberBuilder.setAdminStateUp(member.getPoolMemberAdminStateIsUp());
        if (member.getPoolMemberAddress() != null) {
            final IpAddress ipAddress = IpAddressBuilder.getDefaultInstance(member.getPoolMemberAddress());
            memberBuilder.setAddress(ipAddress);
        }
        if (member.getPoolMemberProtoPort() != null) {
            memberBuilder.setProtocolPort(Uint16.valueOf(member.getPoolMemberProtoPort()));
        }
        if (member.getPoolMemberSubnetID() != null) {
            memberBuilder.setSubnetId(toUuid(member.getPoolMemberSubnetID()));
        }
        if (member.getPoolMemberWeight() != null) {
            memberBuilder.setWeight(Uint16.valueOf(member.getPoolMemberWeight()));
        }
        return memberBuilder.build();
    }

    private static Member toMemberMd(String uuid) {
        final MemberBuilder memberBuilder = new MemberBuilder();
        memberBuilder.setUuid(toUuid(uuid));
        return memberBuilder.build();
    }

    private void addMemberMd(Pool pool, NeutronLoadBalancerPoolMember neutronObject)
            throws TransactionCommitFailedException {
        // TODO think about adding existence logic
        updateMemberMd(pool, neutronObject);
    }

    private void updateMemberMd(Pool pool, NeutronLoadBalancerPoolMember neutronObject)
            throws TransactionCommitFailedException {
        final WriteTransaction transaction = getDataBroker().newWriteOnlyTransaction();
        final Member item = toMemberMd(neutronObject);
        final InstanceIdentifier<Member> iid = createMemberInstanceIdentifier(pool, item);
        transaction.mergeParentStructurePut(LogicalDatastoreType.CONFIGURATION, iid, item);
        checkedCommit(transaction);
    }

    private void removeMemberMd(Pool pool, Member item) throws TransactionCommitFailedException {
        final WriteTransaction transaction = getDataBroker().newWriteOnlyTransaction();
        final InstanceIdentifier<Member> iid = createMemberInstanceIdentifier(pool, item);
        transaction.delete(LogicalDatastoreType.CONFIGURATION, iid);
        checkedCommit(transaction);
    }
}
