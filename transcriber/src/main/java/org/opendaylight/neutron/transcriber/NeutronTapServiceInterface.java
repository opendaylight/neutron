/*
 * Copyright (c) 2017 Intel Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.neutron.transcriber;

import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.neutron.spi.INeutronTapServiceCRUD;
import org.opendaylight.neutron.spi.NeutronTapService;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.tapaas.rev171024.tap.services.attributes.TapServices;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.tapaas.rev171024.tap.services.attributes.tap.services.TapService;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.tapaas.rev171024.tap.services.attributes.tap.services.TapServiceBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.tapaas.rev171024.tap.services.attributes.tap.services.TapServiceKey;
import org.ops4j.pax.cdi.api.OsgiServiceProvider;

@Singleton
@OsgiServiceProvider(classes = INeutronTapServiceCRUD.class)
public final class NeutronTapServiceInterface
        extends AbstractNeutronInterface<TapService, TapServices, TapServiceKey, NeutronTapService>
        implements INeutronTapServiceCRUD {

    @Inject
    public NeutronTapServiceInterface(DataBroker db) {
        super(TapServiceBuilder.class, db);
    }

    @Override
    protected List<TapService> getDataObjectList(TapServices services) {
        return services.getTapService();
    }

    @Override
    protected NeutronTapService fromMd(TapService service) {
        final NeutronTapService answer = new NeutronTapService();
        fromMdBaseAttributes(service, answer);
        if (service.getPortId() != null) {
            answer.setTapServicePortID(service.getPortId().getValue());
        }
        return answer;
    }

    @Override
    protected TapService toMd(NeutronTapService service) {
        final TapServiceBuilder serviceBuilder = new TapServiceBuilder();
        toMdBaseAttributes(service, serviceBuilder);
        if (service.getTapServicePortID() != null) {
            serviceBuilder.setPortId(toUuid(service.getTapServicePortID()));
        }
        return serviceBuilder.build();
    }
}
