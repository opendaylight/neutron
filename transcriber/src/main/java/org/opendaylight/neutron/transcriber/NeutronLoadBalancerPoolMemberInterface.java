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
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.opendaylight.controller.sal.binding.api.BindingAwareBroker.ProviderContext;
import org.opendaylight.neutron.spi.INeutronLoadBalancerPoolMemberCRUD;
import org.opendaylight.neutron.spi.NeutronLoadBalancerPoolMember;
import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.inet.types.rev100924.IpAddress;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.lbaasv2.rev141002.lbaas.attributes.pool.pools.Member;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.lbaasv2.rev141002.lbaas.attributes.pool.pools.member.Members;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.lbaasv2.rev141002.lbaas.attributes.pool.pools.member.MembersBuilder;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NeutronLoadBalancerPoolMemberInterface extends
        AbstractNeutronInterface<Members, NeutronLoadBalancerPoolMember> implements INeutronLoadBalancerPoolMemberCRUD {
    private static final Logger LOGGER = LoggerFactory.getLogger(NeutronLoadBalancerPoolMemberInterface.class);
    private ConcurrentMap<String, NeutronLoadBalancerPoolMember> loadBalancerPoolMemberDB = new ConcurrentHashMap<String, NeutronLoadBalancerPoolMember>();

    NeutronLoadBalancerPoolMemberInterface(ProviderContext providerContext) {
        super(providerContext);
    }

    @Override
    public boolean neutronLoadBalancerPoolMemberExists(String uuid) {
        return loadBalancerPoolMemberDB.containsKey(uuid);
    }

    @Override
    public NeutronLoadBalancerPoolMember getNeutronLoadBalancerPoolMember(String uuid) {
        if (!neutronLoadBalancerPoolMemberExists(uuid)) {
            LOGGER.debug("No LoadBalancerPoolMember Have Been Defined");
            return null;
        }
        return loadBalancerPoolMemberDB.get(uuid);
    }

    @Override
    public List<NeutronLoadBalancerPoolMember> getAllNeutronLoadBalancerPoolMembers() {
        Set<NeutronLoadBalancerPoolMember> allLoadBalancerPoolMembers = new HashSet<NeutronLoadBalancerPoolMember>();
        for (Map.Entry<String, NeutronLoadBalancerPoolMember> entry : loadBalancerPoolMemberDB.entrySet()) {
            NeutronLoadBalancerPoolMember loadBalancerPoolMember = entry.getValue();
            allLoadBalancerPoolMembers.add(loadBalancerPoolMember);
        }
        LOGGER.debug("Exiting getLoadBalancerPoolMembers, Found {} OpenStackLoadBalancerPoolMember",
                allLoadBalancerPoolMembers.size());
        List<NeutronLoadBalancerPoolMember> ans = new ArrayList<NeutronLoadBalancerPoolMember>();
        ans.addAll(allLoadBalancerPoolMembers);
        return ans;
    }

    @Override
    public boolean addNeutronLoadBalancerPoolMember(NeutronLoadBalancerPoolMember input) {
        if (neutronLoadBalancerPoolMemberExists(input.getID())) {
            return false;
        }
        loadBalancerPoolMemberDB.putIfAbsent(input.getID(), input);
        return true;
    }

    @Override
    public boolean removeNeutronLoadBalancerPoolMember(String uuid) {
        if (!neutronLoadBalancerPoolMemberExists(uuid)) {
            return false;
        }
        loadBalancerPoolMemberDB.remove(uuid);
        return true;
    }

    @Override
    public boolean updateNeutronLoadBalancerPoolMember(String uuid, NeutronLoadBalancerPoolMember delta) {
        if (!neutronLoadBalancerPoolMemberExists(uuid)) {
            return false;
        }
        NeutronLoadBalancerPoolMember target = loadBalancerPoolMemberDB.get(uuid);
        return overwrite(target, delta);
    }

    @Override
    public boolean neutronLoadBalancerPoolMemberInUse(String loadBalancerPoolMemberID) {
        return !neutronLoadBalancerPoolMemberExists(loadBalancerPoolMemberID);
    }

    @Override
    protected InstanceIdentifier<Members> createInstanceIdentifier(Members item) {
        return InstanceIdentifier.create(Member.class).child(Members.class);
    }

    @Override
    protected Members toMd(NeutronLoadBalancerPoolMember member) {
        MembersBuilder membersBuilder = new MembersBuilder();
        membersBuilder.setAdminStateUp(member.getPoolMemberAdminStateIsUp());
        if (member.getPoolID() != null) {
            membersBuilder.setUuid(toUuid(member.getPoolID()));
        }
        if (member.getPoolMemberAddress() != null) {
            IpAddress ipAddress = new IpAddress(member.getPoolMemberAddress().toCharArray());
            membersBuilder.setAddress(ipAddress);
        }
        if (member.getPoolMemberProtoPort() != null) {
            membersBuilder.setProtocolPort(member.getPoolMemberProtoPort());
        }
        if (member.getID() != null) {
            membersBuilder.setUuid(toUuid(member.getID()));
        }
        if (member.getPoolMemberSubnetID() != null) {
            membersBuilder.setSubnetId(toUuid(member.getPoolMemberSubnetID()));
        }
        if (member.getPoolMemberTenantID() != null) {
            membersBuilder.setTenantId(member.getPoolMemberTenantID());
        }
        if (member.getPoolMemberWeight() != null) {
            membersBuilder.setWeight(member.getPoolMemberWeight());
        }
        return membersBuilder.build();
    }

    @Override
    protected Members toMd(String uuid) {
        MembersBuilder membersBuilder = new MembersBuilder();
        membersBuilder.setUuid(toUuid(uuid));
        return membersBuilder.build();
    }

    public static void registerNewInterface(BundleContext context,
                                            ProviderContext providerContext,
                                            List<ServiceRegistration<?>> registrations) {
        NeutronLoadBalancerPoolMemberInterface neutronLoadBalancerPoolMemberInterface = new NeutronLoadBalancerPoolMemberInterface(providerContext);
        ServiceRegistration<INeutronLoadBalancerPoolMemberCRUD> neutronLoadBalancerPoolMemberInterfaceRegistration = context.registerService(INeutronLoadBalancerPoolMemberCRUD.class, neutronLoadBalancerPoolMemberInterface, null);
        if(neutronLoadBalancerPoolMemberInterfaceRegistration != null) {
            registrations.add(neutronLoadBalancerPoolMemberInterfaceRegistration);
        }
    }
}
