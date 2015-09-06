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

import org.opendaylight.controller.sal.binding.api.BindingAwareBroker.ProviderContext;
import org.opendaylight.neutron.spi.INeutronLoadBalancerPoolMemberCRUD;
import org.opendaylight.neutron.spi.NeutronLoadBalancerPoolMember;
import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.inet.types.rev100924.IpAddress;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.lbaasv2.rev141002.lbaas.attributes.pools.pool.Members;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.lbaasv2.rev141002.lbaas.attributes.pools.pool.members.Member;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.lbaasv2.rev141002.lbaas.attributes.pools.pool.members.MemberBuilder;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NeutronLoadBalancerPoolMemberInterface extends
        AbstractNeutronInterface<Member, NeutronLoadBalancerPoolMember> implements INeutronLoadBalancerPoolMemberCRUD {
    private static final Logger LOGGER = LoggerFactory.getLogger(NeutronLoadBalancerPoolMemberInterface.class);

    NeutronLoadBalancerPoolMemberInterface(ProviderContext providerContext) {
        super(providerContext);
    }

    @Override
    public boolean neutronLoadBalancerPoolMemberExists(String uuid) {
        Member member = readMd(createInstanceIdentifier(toMd(uuid)));
        if (member == null) {
            return false;
        }
        return true;
    }

    @Override
    public NeutronLoadBalancerPoolMember getNeutronLoadBalancerPoolMember(String uuid) {
        Member member = readMd(createInstanceIdentifier(toMd(uuid)));
        if (member == null) {
            return null;
        }
        return fromMd(member);
    }

    @Override
    public List<NeutronLoadBalancerPoolMember> getAllNeutronLoadBalancerPoolMembers() {
        Set<NeutronLoadBalancerPoolMember> allLoadBalancerPoolMembers = new HashSet<NeutronLoadBalancerPoolMember>();
        Members members = readMd(createInstanceIdentifier());
        if (members != null) {
            for (Member member: members.getMember()) {
                allLoadBalancerPoolMembers.add(fromMd(member));
            }
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
        addMd(input);
        return true;
    }

    @Override
    public boolean removeNeutronLoadBalancerPoolMember(String uuid) {
        if (!neutronLoadBalancerPoolMemberExists(uuid)) {
            return false;
        }
        return removeMd(toMd(uuid));
    }

    @Override
    public boolean updateNeutronLoadBalancerPoolMember(String uuid, NeutronLoadBalancerPoolMember delta) {
        if (!neutronLoadBalancerPoolMemberExists(uuid)) {
            return false;
        }
        updateMd(delta);
        return true;
    }

    @Override
    public boolean neutronLoadBalancerPoolMemberInUse(String loadBalancerPoolMemberID) {
        return !neutronLoadBalancerPoolMemberExists(loadBalancerPoolMemberID);
    }

    @Override
    protected InstanceIdentifier<Member> createInstanceIdentifier(Member item) {
        return InstanceIdentifier.create(Members.class)
            .child(Member.class, item.getKey());
    }

    protected InstanceIdentifier<Members> createInstanceIdentifier() {
        return InstanceIdentifier.create(Members.class);
    }

    static NeutronLoadBalancerPoolMember fromMd(Member member) {
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

    @Override
    protected Member toMd(NeutronLoadBalancerPoolMember member) {
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

    @Override
    protected Member toMd(String uuid) {
        MemberBuilder memberBuilder = new MemberBuilder();
        memberBuilder.setUuid(toUuid(uuid));
        return memberBuilder.build();
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
