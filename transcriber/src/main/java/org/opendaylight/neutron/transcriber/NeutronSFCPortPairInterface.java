/*
 * Copyright (c) 2016 Brocade Communications Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.neutron.transcriber;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.neutron.spi.INeutronSFCPortPairCRUD;
import org.opendaylight.neutron.spi.NeutronSFCPortPair;
import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.yang.types.rev130715.Uuid;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.sfc.rev160511.port.pair.attributes.ServiceFunctionParameters;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.sfc.rev160511.port.pair.attributes.ServiceFunctionParametersBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.sfc.rev160511.port.pair.attributes.ServiceFunctionParametersKey;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.sfc.rev160511.sfc.attributes.PortPairs;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.sfc.rev160511.sfc.attributes.port.pairs.PortPair;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.sfc.rev160511.sfc.attributes.port.pairs.PortPairBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.sfc.rev160511.sfc.attributes.port.pairs.PortPairKey;
import org.ops4j.pax.cdi.api.OsgiServiceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Anil Vishnoi (avishnoi@Brocade.com).
 */
@Singleton
@OsgiServiceProvider(classes = INeutronSFCPortPairCRUD.class)
public final class NeutronSFCPortPairInterface
        extends AbstractNeutronInterface<PortPair, PortPairs, PortPairKey, NeutronSFCPortPair>
        implements INeutronSFCPortPairCRUD {

    private static final Logger LOG = LoggerFactory.getLogger(NeutronSFCPortPairInterface.class);

    @Inject
    public NeutronSFCPortPairInterface(DataBroker db) {
        super(PortPairBuilder.class, db);
    }

    @Override
    protected PortPair toMd(NeutronSFCPortPair neutronPortPair) {

        LOG.trace("toMd: REST SFC Port Pair data : {}", neutronPortPair);

        PortPairBuilder result = new PortPairBuilder();
        toMdBaseAttributes(neutronPortPair, result);
        if (neutronPortPair.getIngressPortUUID() != null) {
            result.setIngress(new Uuid(neutronPortPair.getIngressPortUUID()));
        }
        if (neutronPortPair.getEgressPortUUID() != null) {
            result.setEgress(new Uuid(neutronPortPair.getEgressPortUUID()));
        }
        if (neutronPortPair.getServiceFunctionParameters() != null) {
            List<ServiceFunctionParameters> serviceFunctionParams = new ArrayList<>();
            for (String paramKey : neutronPortPair.getServiceFunctionParameters().keySet()) {
                ServiceFunctionParametersBuilder param = new ServiceFunctionParametersBuilder();
                param.withKey(new ServiceFunctionParametersKey(paramKey));
                param.setServiceFunctionParameter(paramKey);
                param.setServiceFunctionParameterValue(neutronPortPair.getServiceFunctionParameters().get(paramKey));
                serviceFunctionParams.add(param.build());
            }
            result.setServiceFunctionParameters(serviceFunctionParams);
        }
        LOG.trace("toMd: Yang SFC Port Pair data : {}", result);
        return result.build();
    }

    @Override
    protected NeutronSFCPortPair fromMd(PortPair mdPortPair) {
        LOG.trace("fromMd: Yang SFC Port Pair data : {}", mdPortPair);
        NeutronSFCPortPair result = new NeutronSFCPortPair();
        fromMdBaseAttributes(mdPortPair, result);
        if (mdPortPair.getIngress() != null) {
            result.setIngressPortUUID(mdPortPair.getIngress().getValue());
        }
        if (mdPortPair.getEgress() != null) {
            result.setEgressPortUUID(mdPortPair.getEgress().getValue());
        }
        if (mdPortPair.getServiceFunctionParameters() != null) {
            HashMap<String, String> serviceFunctionParam = new HashMap<>();
            for (ServiceFunctionParameters param : mdPortPair.getServiceFunctionParameters()) {
                serviceFunctionParam.put(param.getServiceFunctionParameter(), param.getServiceFunctionParameterValue());
            }
            result.setServiceFunctionParameters(serviceFunctionParam);
        }
        LOG.trace("fromMd: REST SFC Port Pair data : {}", result);
        return result;
    }

    @Override
    protected List<PortPair> getDataObjectList(PortPairs dataObjects) {
        return dataObjects.getPortPair();
    }

}
