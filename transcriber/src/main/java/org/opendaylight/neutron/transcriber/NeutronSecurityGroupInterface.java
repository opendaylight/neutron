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
import org.opendaylight.neutron.spi.INeutronSecurityGroupCRUD;
import org.opendaylight.neutron.spi.INeutronSecurityRuleCRUD;
import org.opendaylight.neutron.spi.NeutronCRUDInterfaces;
import org.opendaylight.neutron.spi.NeutronSecurityGroup;
import org.opendaylight.neutron.spi.NeutronSecurityRule;
import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.yang.types.rev130715.Uuid;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.rev150712.Neutron;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.secgroups.rev150712.security.groups.attributes.SecurityGroups;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.secgroups.rev150712.security.groups.attributes.security.groups.SecurityGroup;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.secgroups.rev150712.security.groups.attributes.security.groups.SecurityGroupBuilder;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class NeutronSecurityGroupInterface extends AbstractNeutronInterface<SecurityGroup, SecurityGroups, NeutronSecurityGroup> implements INeutronSecurityGroupCRUD {
    private static final Logger LOGGER = LoggerFactory.getLogger(NeutronSecurityGroupInterface.class);


    NeutronSecurityGroupInterface(ProviderContext providerContext) {
        super(providerContext);
    }

    @Override
    public boolean neutronSecurityGroupExists(String uuid) {
        return exists(uuid);
    }

    @Override
    public NeutronSecurityGroup getNeutronSecurityGroup(String uuid) {
        return get(uuid);
    }

    @Override
    protected List<SecurityGroup> getDataObjectList(SecurityGroups groups) {
        return groups.getSecurityGroup();
    }

    @Override
    public List<NeutronSecurityGroup> getAllNeutronSecurityGroups() {
        return getAll();
    }

    @Override
    public boolean addNeutronSecurityGroup(NeutronSecurityGroup input) {
        return add(input);
    }

    @Override
    public boolean removeNeutronSecurityGroup(String uuid) {
        return remove(uuid);
    }

    @Override
    public boolean updateNeutronSecurityGroup(String uuid, NeutronSecurityGroup delta) {
        return update(uuid, delta);
    }

    @Override
    public boolean neutronSecurityGroupInUse(String securityGroupUUID) {
        return !exists(securityGroupUUID);
    }

    protected NeutronSecurityGroup fromMd(SecurityGroup group) {
        NeutronSecurityGroup answer = new NeutronSecurityGroup();
        if (group.getName() != null) {
            answer.setSecurityGroupName(group.getName());
        }
        if (group.getDescription() != null) {
            answer.setSecurityGroupDescription(group.getDescription());
        }
        if (group.getTenantId() != null) {
            answer.setTenantID(group.getTenantId());
        }

        // Bug 4550
        // https://bugs.opendaylight.org/show_bug.cgi?id=4550
        // Now SecurityGroup::securityGroupRule isn't updated.
        // always rebuid it from security group rules
        NeutronCRUDInterfaces interfaces = new NeutronCRUDInterfaces()
            .fetchINeutronSecurityRuleCRUD(this);
        INeutronSecurityRuleCRUD srCrud = interfaces.getSecurityRuleInterface();

        List<NeutronSecurityRule> rules = new ArrayList<NeutronSecurityRule>();
        String sgId = group.getUuid().getValue();
        for (NeutronSecurityRule rule: srCrud.getAll()) {
            if (rule.getSecurityRuleGroupID().equals(sgId)) {
                rules.add(rule);
            }
        }
        answer.setSecurityRules(rules);

        if (group.getUuid() != null) {
            answer.setID(group.getUuid().getValue());
        }
        return answer;
    }

    @Override
    protected SecurityGroup toMd(NeutronSecurityGroup securityGroup) {
        SecurityGroupBuilder securityGroupBuilder = new SecurityGroupBuilder();
        if (securityGroup.getSecurityGroupName() != null) {
            securityGroupBuilder.setName(securityGroup.getSecurityGroupName());
        }
        if (securityGroup.getSecurityGroupDescription() != null) {
            securityGroupBuilder.setDescription(securityGroup.getSecurityGroupDescription());
        }
        if (securityGroup.getTenantID() != null) {
            securityGroupBuilder.setTenantId(toUuid(securityGroup.getTenantID()));
        }
        if (securityGroup.getID() != null) {
            securityGroupBuilder.setUuid(toUuid(securityGroup.getID()));
        } else {
            LOGGER.warn("Attempting to write neutron securityGroup without UUID");
        }

        return securityGroupBuilder.build();
    }

    @Override
    protected InstanceIdentifier<SecurityGroup> createInstanceIdentifier(SecurityGroup securityGroup) {
        return InstanceIdentifier.create(Neutron.class)
            .child(SecurityGroups.class).child(SecurityGroup.class,
                                               securityGroup.getKey());
    }

    @Override
    protected InstanceIdentifier<SecurityGroups> createInstanceIdentifier() {
        return InstanceIdentifier.create(Neutron.class)
            .child(SecurityGroups.class);
    }

    @Override
    protected SecurityGroup toMd(String uuid) {
        SecurityGroupBuilder securityGroupBuilder = new SecurityGroupBuilder();
        securityGroupBuilder.setUuid(toUuid(uuid));
        return securityGroupBuilder.build();
    }

    public static void registerNewInterface(BundleContext context,
                                            ProviderContext providerContext,
                                            List<ServiceRegistration<?>> registrations) {
        NeutronSecurityGroupInterface neutronSecurityGroupInterface = new NeutronSecurityGroupInterface(providerContext);
        ServiceRegistration<INeutronSecurityGroupCRUD> neutronSecurityGroupInterfaceRegistration = context.registerService(INeutronSecurityGroupCRUD.class, neutronSecurityGroupInterface, null);
        if(neutronSecurityGroupInterfaceRegistration != null) {
            registrations.add(neutronSecurityGroupInterfaceRegistration);
        }
    }
}
