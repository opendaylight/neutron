/*
 * Copyright (c) 2014, 2015 Red Hat, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.neutron.transcriber;

import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.apache.aries.blueprint.annotation.service.Service;
import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.neutron.spi.INeutronSecurityGroupCRUD;
import org.opendaylight.neutron.spi.NeutronSecurityGroup;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.secgroups.rev150712.security.groups.attributes.SecurityGroups;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.secgroups.rev150712.security.groups.attributes.security.groups.SecurityGroup;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.secgroups.rev150712.security.groups.attributes.security.groups.SecurityGroupBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.secgroups.rev150712.security.groups.attributes.security.groups.SecurityGroupKey;

@Singleton
@Service(classes = INeutronSecurityGroupCRUD.class)
public final class NeutronSecurityGroupInterface
        extends AbstractNeutronInterface<SecurityGroup, SecurityGroups, SecurityGroupKey, NeutronSecurityGroup>
        implements INeutronSecurityGroupCRUD {

    @Inject
    public NeutronSecurityGroupInterface(DataBroker db) {
        super(SecurityGroupBuilder.class, db);
    }

    @Override
    protected List<SecurityGroup> getDataObjectList(SecurityGroups groups) {
        return groups.getSecurityGroup();
    }

    @Override
    protected NeutronSecurityGroup fromMd(SecurityGroup group) {
        final NeutronSecurityGroup answer = new NeutronSecurityGroup();
        fromMdBaseAttributes(group, answer);
        return answer;
    }

    @Override
    protected SecurityGroup toMd(NeutronSecurityGroup securityGroup) {
        final SecurityGroupBuilder securityGroupBuilder = new SecurityGroupBuilder();
        toMdBaseAttributes(securityGroup, securityGroupBuilder);
        return securityGroupBuilder.build();
    }
}
