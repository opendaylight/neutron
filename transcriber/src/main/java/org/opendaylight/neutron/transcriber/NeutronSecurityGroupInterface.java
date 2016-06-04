/*
 * Copyright (c) 2014, 2015 Red Hat, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.transcriber;

import java.util.List;

import org.opendaylight.controller.sal.binding.api.BindingAwareBroker.ProviderContext;
import org.opendaylight.neutron.spi.INeutronSecurityGroupCRUD;
import org.opendaylight.neutron.spi.NeutronSecurityGroup;
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
        if (group.getTenantId() != null) {
            answer.setTenantID(group.getTenantId());
        }

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
