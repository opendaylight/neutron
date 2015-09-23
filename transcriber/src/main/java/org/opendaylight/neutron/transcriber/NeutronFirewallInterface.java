/*
 * Copyright (c) 2014, 2015 Red Hat, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.transcriber;

import org.opendaylight.controller.md.sal.binding.api.BindingTransactionChain;
import org.opendaylight.controller.sal.binding.api.BindingAwareBroker.ProviderContext;
import org.opendaylight.neutron.spi.INeutronFirewallCRUD;
import org.opendaylight.neutron.spi.NeutronFirewall;

import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.yang.types.rev130715.Uuid;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.fwaas.rev141002.firewalls.attributes.Firewalls;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.fwaas.rev141002.firewalls.attributes.firewalls.Firewall;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.fwaas.rev141002.firewalls.attributes.firewalls.FirewallBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.rev150325.Neutron;

import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

public class NeutronFirewallInterface extends AbstractNeutronInterface<Firewall, Firewalls, NeutronFirewall> implements INeutronFirewallCRUD {

    private static final Logger LOGGER = LoggerFactory.getLogger(NeutronFirewallInterface.class);

    NeutronFirewallInterface(ProviderContext providerContext) {
        super(providerContext);
    }

    @Override
    public boolean neutronFirewallExists(String uuid) {
        return exists(uuid, null);
    }

    @Override
    public NeutronFirewall getNeutronFirewall(String uuid) {
        return get(uuid, null);
    }

    @Override
    protected List<Firewall> getDataObjectList(Firewalls firewalls) {
        return firewalls.getFirewall();
    }

    @Override
    public List<NeutronFirewall> getAllNeutronFirewalls() {
        return getAll(null);
    }

    @Override
    public boolean addNeutronFirewall(NeutronFirewall input) {
        return add(input, null);
    }

    @Override
    public boolean removeNeutronFirewall(String uuid) {
        return remove(uuid, null);
    }

    @Override
    public boolean updateNeutronFirewall(String uuid, NeutronFirewall delta) {
        return update(uuid, delta, null);
    }

    @Override
    public boolean neutronFirewallInUse(String firewallUUID) {
        return !exists(firewallUUID, null);
    }

    @Override
    protected InstanceIdentifier<Firewall> createInstanceIdentifier(Firewall item) {
        return InstanceIdentifier.create(Neutron.class)
                .child(Firewalls.class)
                .child(Firewall.class, item.getKey());
    }

    protected InstanceIdentifier<Firewalls> createInstanceIdentifier() {
        return InstanceIdentifier.create(Neutron.class)
                .child(Firewalls.class);
    }

    protected NeutronFirewall fromMd(Firewall firewall) {
        NeutronFirewall answer = new NeutronFirewall();
        if (firewall.getUuid() != null) {
            answer.setID(firewall.getUuid().getValue());
        }
        if (firewall.getName() != null) {
            answer.setFirewallName(firewall.getName());
        }
        if (firewall.getTenantId() != null) {
            answer.setFirewallTenantID(firewall.getTenantId().getValue().replace("-",""));
        }
        answer.setFirewallAdminStateIsUp(firewall.isAdminStateUp());
        if (firewall.getStatus() != null) {
            answer.setFirewallStatus(firewall.getStatus());
        }
        if (firewall.getDescr() != null) {
            answer.setFirewallDescription(firewall.getDescr());
        }
        if (firewall.isShared() != null) {
            answer.setFirewallIsShared(firewall.isShared());
        }
        if (firewall.getFirewallPolicyId() != null) {
             answer.setFirewallPolicyID(firewall.getFirewallPolicyId().getValue());
        }
        return answer;
    }

    @Override
    protected Firewall toMd(NeutronFirewall firewall) {
        FirewallBuilder firewallBuilder = new FirewallBuilder();
        if (firewall.getID() != null) {
            firewallBuilder.setUuid(toUuid(firewall.getID()));
        }
        if (firewall.getFirewallName() != null) {
            firewallBuilder.setName(firewall.getFirewallName());
        }
        if (firewall.getFirewallTenantID() != null) {
            firewallBuilder.setTenantId(toUuid(firewall.getFirewallTenantID()));
        }
        if (firewall.getFirewallAdminStateIsUp() != null) {
            firewallBuilder.setAdminStateUp(firewall.getFirewallAdminStateIsUp());
        }
        if (firewall.getFirewallStatus() != null) {
            firewallBuilder.setStatus(firewall.getFirewallStatus());
        }
        if (firewall.getFirewallDescription() != null) {
            firewallBuilder.setDescr(firewall.getFirewallDescription());
        }
        if (firewall.getFirewallIsShared() != null) {
            firewallBuilder.setShared(firewall.getFirewallIsShared());
        }
        if (firewall.getFirewallPolicyID() != null) {
            firewallBuilder.setFirewallPolicyId(toUuid(firewall.getFirewallPolicyID()));
        }
        return firewallBuilder.build();
    }

    @Override
    protected Firewall toMd(String uuid) {
        FirewallBuilder firewallBuilder = new FirewallBuilder();
        firewallBuilder.setUuid(toUuid(uuid));
        return firewallBuilder.build();
    }

    public static void registerNewInterface(BundleContext context,
                                            ProviderContext providerContext,
                                            List<ServiceRegistration<?>> registrations) {
        NeutronFirewallInterface neutronFirewallInterface = new NeutronFirewallInterface(providerContext);
        ServiceRegistration<INeutronFirewallCRUD> neutronFirewallInterfaceRegistration = context.registerService(INeutronFirewallCRUD.class, neutronFirewallInterface, null);
        if(neutronFirewallInterfaceRegistration != null) {
            registrations.add(neutronFirewallInterfaceRegistration);
        }
    }
}
