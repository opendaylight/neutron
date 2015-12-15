/*
 * Copyright (C) 2014 Red Hat, Inc.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 */

package org.opendaylight.neutron.transcriber;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.opendaylight.controller.sal.binding.api.BindingAwareBroker.ProviderContext;
import org.opendaylight.neutron.spi.INeutronSecurityGroupCRUD;
import org.opendaylight.neutron.spi.INeutronSecurityRuleCRUD;
import org.opendaylight.neutron.spi.NeutronCRUDInterfaces;
import org.opendaylight.neutron.spi.NeutronSecurityGroup;
import org.opendaylight.neutron.spi.NeutronSecurityRule;
import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.yang.types.rev130715.Uuid;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.rev150325.Neutron;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.secgroups.rev141002.security.groups.attributes.SecurityGroups;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.secgroups.rev141002.security.groups.attributes.security.groups.SecurityGroup;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.secgroups.rev141002.security.groups.attributes.security.groups.SecurityGroupBuilder;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class NeutronSecurityGroupInterface extends AbstractNeutronInterface<SecurityGroup,NeutronSecurityGroup> implements INeutronSecurityGroupCRUD {
    private static final Logger LOGGER = LoggerFactory.getLogger(NeutronSecurityGroupInterface.class);
    private ConcurrentMap<String, NeutronSecurityGroup> securityGroupDB  = new ConcurrentHashMap<String, NeutronSecurityGroup>();


    NeutronSecurityGroupInterface(ProviderContext providerContext) {
        super(providerContext);
    }

    // this method uses reflection to update an object from it's delta.

    private boolean overwrite(Object target, Object delta) {
        Method[] methods = target.getClass().getMethods();

        for(Method toMethod: methods){
            if(toMethod.getDeclaringClass().equals(target.getClass())
                && toMethod.getName().startsWith("set")){

                String toName = toMethod.getName();
                String fromName = toName.replace("set", "get");

                try {
                    Method fromMethod = delta.getClass().getMethod(fromName);
                    Object value = fromMethod.invoke(delta, (Object[])null);
                    if(value != null){
                        toMethod.invoke(target, value);
                    }
                } catch (Exception e) {
                    LOGGER.error(e.getMessage());
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean neutronSecurityGroupExists(String uuid) {
        return securityGroupDB.containsKey(uuid);
    }

    @Override
    public NeutronSecurityGroup getNeutronSecurityGroup(String uuid) {
        if (!neutronSecurityGroupExists(uuid)) {
            LOGGER.debug("No Security Groups Have Been Defined");
            return null;
        }
        return securityGroupDB.get(uuid);
    }

    @Override
    public List<NeutronSecurityGroup> getAllNeutronSecurityGroups() {
        Set<NeutronSecurityGroup> allSecurityGroups = new HashSet<NeutronSecurityGroup>();
        for (Entry<String, NeutronSecurityGroup> entry : securityGroupDB.entrySet()) {
            NeutronSecurityGroup securityGroup = entry.getValue();
            allSecurityGroups.add(securityGroup);
        }
        LOGGER.debug("Exiting getSecurityGroups, Found {} OpenStackSecurityGroup", allSecurityGroups.size());
        List<NeutronSecurityGroup> ans = new ArrayList<NeutronSecurityGroup>();
        ans.addAll(allSecurityGroups);
        return ans;
    }

    @Override
    public boolean addNeutronSecurityGroup(NeutronSecurityGroup input) {
        if (neutronSecurityGroupExists(input.getSecurityGroupUUID())) {
            return false;
        }
        INeutronSecurityRuleCRUD sgrCrud = NeutronCRUDInterfaces.getINeutronSecurityRuleCRUD(this);
        for(NeutronSecurityRule sgr : input.getSecurityRules()) {
            sgrCrud.addNeutronSecurityRule(sgr);
        }
        securityGroupDB.putIfAbsent(input.getSecurityGroupUUID(), input);
        addMd(input);
        return true;
    }

    @Override
    public boolean removeNeutronSecurityGroup(String uuid) {
        if (!neutronSecurityGroupExists(uuid)) {
            return false;
        }
        securityGroupDB.remove(uuid);
        removeMd(toMd(uuid));
        return true;
    }

    @Override
    public boolean updateNeutronSecurityGroup(String uuid, NeutronSecurityGroup delta) {
        if (!neutronSecurityGroupExists(uuid)) {
            return false;
        }
        NeutronSecurityGroup target = securityGroupDB.get(uuid);
        boolean rc = overwrite(target, delta);
        if (rc) {
            updateMd(securityGroupDB.get(uuid));
        }
        return rc;
    }

    @Override
    public boolean neutronSecurityGroupInUse(String securityGroupUUID) {
        return !neutronSecurityGroupExists(securityGroupUUID);
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
        if (securityGroup.getSecurityGroupTenantID() != null && !securityGroup.getSecurityGroupTenantID().isEmpty()) {
            securityGroupBuilder.setTenantId(toUuid(securityGroup.getSecurityGroupTenantID()));
        }
        if (securityGroup.getSecurityRules() != null) {
            List<Uuid> neutronSecurityRule = new ArrayList<>();
            for (NeutronSecurityRule securityRule : securityGroup.getSecurityRules()) {
                if (securityRule.getSecurityRuleUUID() != null) {
                    neutronSecurityRule.add(toUuid(securityRule.getSecurityRuleUUID()));
                }
            }
            securityGroupBuilder.setSecurityRules(neutronSecurityRule);
        }
        if (securityGroup.getSecurityGroupUUID() != null) {
            securityGroupBuilder.setUuid(toUuid(securityGroup.getSecurityGroupUUID()));
        } else {
            LOGGER.warn("Attempting to write neutron securityGroup without UUID");
        }

        return securityGroupBuilder.build();
    }

    @Override
    protected InstanceIdentifier<SecurityGroup> createInstanceIdentifier(SecurityGroup securityGroup) {
        return InstanceIdentifier.create(Neutron.class).child(SecurityGroups.class).child(SecurityGroup.class,
                securityGroup.getKey());
    }

    @Override
    protected SecurityGroup toMd(String uuid) {
        SecurityGroupBuilder securityGroupBuilder = new SecurityGroupBuilder();
        securityGroupBuilder.setUuid(toUuid(uuid));
        return securityGroupBuilder.build();
    }
}
