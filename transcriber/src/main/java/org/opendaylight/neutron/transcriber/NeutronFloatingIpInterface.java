/*
 * Copyright (c) 2013, 2015 IBM Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.neutron.transcriber;

import java.util.Collection;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.apache.aries.blueprint.annotation.service.Service;
import org.opendaylight.mdsal.binding.api.DataBroker;
import org.opendaylight.neutron.spi.INeutronFloatingIpCRUD;
import org.opendaylight.neutron.spi.NeutronFloatingIp;
import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.inet.types.rev130715.IpAddressBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.l3.rev150712.floatingips.attributes.Floatingips;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.l3.rev150712.floatingips.attributes.floatingips.Floatingip;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.l3.rev150712.floatingips.attributes.floatingips.FloatingipBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.l3.rev150712.floatingips.attributes.floatingips.FloatingipKey;

@Singleton
@Service(classes = INeutronFloatingIpCRUD.class)
public final class NeutronFloatingIpInterface
        extends AbstractNeutronInterface<Floatingip, Floatingips, FloatingipKey, NeutronFloatingIp>
        implements INeutronFloatingIpCRUD {

    @Inject
    public NeutronFloatingIpInterface(DataBroker db) {
        super(FloatingipBuilder.class, db);
    }

    // IfNBFloatingIpCRUD interface methods

    @Override
    protected Collection<Floatingip> getDataObjectList(Floatingips fips) {
        return fips.nonnullFloatingip().values();
    }

    @Override
    protected Floatingip toMd(NeutronFloatingIp floatingIp) {
        final FloatingipBuilder floatingipBuilder = new FloatingipBuilder();
        toMdIds(floatingIp, floatingipBuilder);
        if (floatingIp.getFixedIpAddress() != null) {
            floatingipBuilder.setFixedIpAddress(IpAddressBuilder.getDefaultInstance(floatingIp.getFixedIpAddress()));
        }
        if (floatingIp.getFloatingIpAddress() != null) {
            floatingipBuilder.setFloatingIpAddress(IpAddressBuilder.getDefaultInstance(
                floatingIp.getFloatingIpAddress()));
        }
        if (floatingIp.getFloatingNetworkUUID() != null) {
            floatingipBuilder.setFloatingNetworkId(toUuid(floatingIp.getFloatingNetworkUUID()));
        }
        if (floatingIp.getPortUUID() != null) {
            floatingipBuilder.setPortId(toUuid(floatingIp.getPortUUID()));
        }
        if (floatingIp.getRouterUUID() != null) {
            floatingipBuilder.setRouterId(toUuid(floatingIp.getRouterUUID()));
        }
        if (floatingIp.getStatus() != null) {
            floatingipBuilder.setStatus(floatingIp.getStatus());
        }
        return floatingipBuilder.build();
    }

    @Override
    protected NeutronFloatingIp fromMd(Floatingip fip) {
        final NeutronFloatingIp result = new NeutronFloatingIp();
        fromMdIds(fip, result);
        if (fip.getFloatingNetworkId() != null) {
            result.setFloatingNetworkUUID(String.valueOf(fip.getFloatingNetworkId().getValue()));
        }
        if (fip.getPortId() != null) {
            result.setPortUUID(String.valueOf(fip.getPortId().getValue()));
        }
        if (fip.getFixedIpAddress() != null) {
            result.setFixedIpAddress(fip.getFixedIpAddress().stringValue());
        }
        if (fip.getFloatingIpAddress() != null) {
            result.setFloatingIpAddress(fip.getFloatingIpAddress().stringValue());
        }
        if (fip.getRouterId() != null) {
            result.setRouterUUID(String.valueOf(fip.getRouterId().getValue()));
        }
        result.setStatus(fip.getStatus());
        return result;
    }
}
