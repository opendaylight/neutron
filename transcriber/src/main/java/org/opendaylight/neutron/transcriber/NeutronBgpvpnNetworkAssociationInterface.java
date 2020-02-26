/*
 * Copyright (c) 2019 Ericsson India Global Services Pvt Ltd. and others. All rights reserved.
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
import org.opendaylight.neutron.spi.INeutronBgpvpnNetworkAssociationCRUD;
import org.opendaylight.neutron.spi.NeutronBgpvpnNetworkAssociation;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.bgpvpn.network.association.rev190502.bgpvpn.network.associations.attributes.BgpvpnNetworkAssociations;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.bgpvpn.network.association.rev190502.bgpvpn.network.associations.attributes.bgpvpn.network.associations.BgpvpnNetworkAssociation;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.bgpvpn.network.association.rev190502.bgpvpn.network.associations.attributes.bgpvpn.network.associations.BgpvpnNetworkAssociationBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.bgpvpn.network.association.rev190502.bgpvpn.network.associations.attributes.bgpvpn.network.associations.BgpvpnNetworkAssociationKey;

@Singleton
@Service(classes = INeutronBgpvpnNetworkAssociationCRUD.class)
public final class NeutronBgpvpnNetworkAssociationInterface extends AbstractNeutronInterface<BgpvpnNetworkAssociation,
        BgpvpnNetworkAssociations, BgpvpnNetworkAssociationKey, NeutronBgpvpnNetworkAssociation>
        implements INeutronBgpvpnNetworkAssociationCRUD {

    @Inject
    public NeutronBgpvpnNetworkAssociationInterface(DataBroker db) {
        super(BgpvpnNetworkAssociationBuilder.class, db);
    }

    @Override
    protected List<BgpvpnNetworkAssociation> getDataObjectList(BgpvpnNetworkAssociations bgpvpnNetworkAssociations) {
        return bgpvpnNetworkAssociations.getBgpvpnNetworkAssociation();
    }

    @Override
    protected NeutronBgpvpnNetworkAssociation fromMd(BgpvpnNetworkAssociation bgpvpnNetworkAssociation) {
        final NeutronBgpvpnNetworkAssociation result = new NeutronBgpvpnNetworkAssociation();
        fromMdAdminAttributes(bgpvpnNetworkAssociation, result);
        if (bgpvpnNetworkAssociation.getBgpvpnId() != null) {
            result.setBgpvpnId(bgpvpnNetworkAssociation.getBgpvpnId().getValue());
        }
        if (bgpvpnNetworkAssociation.getNetworkId() != null) {
            result.setNetworkIds(bgpvpnNetworkAssociation.getNetworkId().getValue());
        }
        return result;
    }

    @Override
    protected BgpvpnNetworkAssociation toMd(NeutronBgpvpnNetworkAssociation bgpvpnNetworkAssociation) {
        final BgpvpnNetworkAssociationBuilder bgpvpnNetworkAssociationBuilder = new BgpvpnNetworkAssociationBuilder();

        toMdAdminAttributes(bgpvpnNetworkAssociation, bgpvpnNetworkAssociationBuilder);
        if (bgpvpnNetworkAssociation.getBgpvpnId() != null) {
            bgpvpnNetworkAssociationBuilder.setBgpvpnId(toUuid(bgpvpnNetworkAssociation.getBgpvpnId()));
        }
        if (bgpvpnNetworkAssociation.getNetworkIds() != null) {
            bgpvpnNetworkAssociationBuilder.setNetworkId(toUuid(bgpvpnNetworkAssociation.getNetworkIds()));
        }
        return bgpvpnNetworkAssociationBuilder.build();
    }

}
