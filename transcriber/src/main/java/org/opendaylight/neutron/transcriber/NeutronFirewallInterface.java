/*
 * Copyright (c) 2014, 2015 Red Hat, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.transcriber;

import java.util.List;
import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.neutron.spi.INeutronFirewallCRUD;
import org.opendaylight.neutron.spi.NeutronFirewall;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.fwaas.rev150712.firewalls.attributes.Firewalls;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.fwaas.rev150712.firewalls.attributes.firewalls.Firewall;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.fwaas.rev150712.firewalls.attributes.firewalls.FirewallBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.rev150712.Neutron;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class NeutronFirewallInterface extends AbstractNeutronInterface<Firewall, Firewalls, NeutronFirewall>
        implements INeutronFirewallCRUD {

    private static final Logger LOGGER = LoggerFactory.getLogger(NeutronFirewallInterface.class);

    NeutronFirewallInterface(DataBroker db) {
        super(FirewallBuilder.class, db);
    }

    @Override
    protected List<Firewall> getDataObjectList(Firewalls firewalls) {
        return firewalls.getFirewall();
    }

    @Override
    protected InstanceIdentifier<Firewall> createInstanceIdentifier(Firewall item) {
        return InstanceIdentifier.create(Neutron.class).child(Firewalls.class).child(Firewall.class, item.getKey());
    }

    @Override
    protected InstanceIdentifier<Firewalls> createInstanceIdentifier() {
        return InstanceIdentifier.create(Neutron.class).child(Firewalls.class);
    }

    protected NeutronFirewall fromMd(Firewall firewall) {
        final NeutronFirewall answer = new NeutronFirewall();
        fromMdBaseAttributes(firewall, answer);
        answer.setFirewallAdminStateIsUp(firewall.isAdminStateUp());
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
        final FirewallBuilder firewallBuilder = new FirewallBuilder();
        toMdBaseAttributes(firewall, firewallBuilder);
        if (firewall.getFirewallAdminStateIsUp() != null) {
            firewallBuilder.setAdminStateUp(firewall.getFirewallAdminStateIsUp());
        }
        if (firewall.getFirewallIsShared() != null) {
            firewallBuilder.setShared(firewall.getFirewallIsShared());
        }
        if (firewall.getFirewallPolicyID() != null) {
            firewallBuilder.setFirewallPolicyId(toUuid(firewall.getFirewallPolicyID()));
        }
        return firewallBuilder.build();
    }
}
