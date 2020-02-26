/*
 * Copyright (c) 2020 Ericsson India Global Services Pvt Ltd. and others. All rights reserved.
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
import org.opendaylight.neutron.spi.INeutronBgpvpnRouterAssociationCRUD;
import org.opendaylight.neutron.spi.NeutronBgpvpnRouterAssociation;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.bgpvpn.router.association.rev190502.bgpvpn.router.associations.attributes.BgpvpnRouterAssociations;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.bgpvpn.router.association.rev190502.bgpvpn.router.associations.attributes.bgpvpn.router.associations.BgpvpnRouterAssociation;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.bgpvpn.router.association.rev190502.bgpvpn.router.associations.attributes.bgpvpn.router.associations.BgpvpnRouterAssociationBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.bgpvpn.router.association.rev190502.bgpvpn.router.associations.attributes.bgpvpn.router.associations.BgpvpnRouterAssociationKey;

@Singleton
@Service(classes = INeutronBgpvpnRouterAssociationCRUD.class)
public final class NeutronBgpvpnRouterAssociationInterface extends AbstractNeutronInterface<BgpvpnRouterAssociation,
        BgpvpnRouterAssociations, BgpvpnRouterAssociationKey, NeutronBgpvpnRouterAssociation>
        implements INeutronBgpvpnRouterAssociationCRUD {

    @Inject
    public NeutronBgpvpnRouterAssociationInterface(DataBroker db) {
        super(BgpvpnRouterAssociationBuilder.class, db);
    }

    @Override
    protected Collection<BgpvpnRouterAssociation> getDataObjectList(BgpvpnRouterAssociations bgpvpnRouterAssociations) {
        return bgpvpnRouterAssociations.nonnullBgpvpnRouterAssociation().values();
    }

    @Override
    protected NeutronBgpvpnRouterAssociation fromMd(BgpvpnRouterAssociation bgpvpnRouterAssociation) {
        final NeutronBgpvpnRouterAssociation result = new NeutronBgpvpnRouterAssociation();
        fromMdBaseAttributes(bgpvpnRouterAssociation, result);
        fromMdAdminAttributes(bgpvpnRouterAssociation, result);
        if (bgpvpnRouterAssociation.getBgpvpnId() != null) {
            result.setBgpvpnId(bgpvpnRouterAssociation.getBgpvpnId().getValue());
        }
        if (bgpvpnRouterAssociation.getRouterId() != null) {
            result.setRouterId(bgpvpnRouterAssociation.getRouterId().getValue());
        }
        return result;
    }

    @Override
    protected BgpvpnRouterAssociation toMd(NeutronBgpvpnRouterAssociation bgpvpnRouterAssociation) {
        final BgpvpnRouterAssociationBuilder bgpvpnRouterAssociationBuilder = new BgpvpnRouterAssociationBuilder();
        toMdBaseAttributes(bgpvpnRouterAssociation, bgpvpnRouterAssociationBuilder);
        toMdAdminAttributes(bgpvpnRouterAssociation, bgpvpnRouterAssociationBuilder);
        if (bgpvpnRouterAssociation.getBgpvpnId() != null) {
            bgpvpnRouterAssociationBuilder.setBgpvpnId(toUuid(bgpvpnRouterAssociation.getBgpvpnId()));
        }
        if (bgpvpnRouterAssociation.getRouterId() != null) {
            bgpvpnRouterAssociationBuilder.setRouterId(toUuid(bgpvpnRouterAssociation.getRouterId()));
        }
        return bgpvpnRouterAssociationBuilder.build();
    }

}
