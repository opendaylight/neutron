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
import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.neutron.spi.INeutronSFCPortChainCRUD;
import org.opendaylight.neutron.spi.NeutronSFCPortChain;
import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.yang.types.rev130715.Uuid;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.rev150712.Neutron;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.sfc.rev160511.port.chain.attributes.ChainParameters;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.sfc.rev160511.port.chain.attributes.ChainParametersBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.sfc.rev160511.port.chain.attributes.ChainParametersKey;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.sfc.rev160511.sfc.attributes.PortChains;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.sfc.rev160511.sfc.attributes.port.chains.PortChain;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.sfc.rev160511.sfc.attributes.port.chains.PortChainBuilder;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Anil Vishnoi (avishnoi@Brocade.com)
 */
public final class NeutronSFCPortChainInterface
        extends AbstractNeutronInterface<PortChain, PortChains, NeutronSFCPortChain>
        implements INeutronSFCPortChainCRUD {

    private static final Logger LOGGER = LoggerFactory.getLogger(NeutronSFCPortChainInterface.class);

    NeutronSFCPortChainInterface(DataBroker db) {
        super(db);
    }

    @Override
    protected List<PortChain> getDataObjectList(PortChains dataObjects) {
        return dataObjects.getPortChain();
    }

    @Override
    protected InstanceIdentifier<PortChain> createInstanceIdentifier(PortChain portChain) {
        return InstanceIdentifier.create(Neutron.class).child(PortChains.class).child(PortChain.class,
                portChain.getKey());
    }

    @Override
    protected InstanceIdentifier<PortChains> createInstanceIdentifier() {
        return InstanceIdentifier.create(Neutron.class).child(PortChains.class);
    }

    @Override
    protected PortChain toMd(NeutronSFCPortChain neutronPortChain) {

        LOGGER.trace("toMd: REST SFC Port Chain data : {}", neutronPortChain);

        PortChainBuilder result = new PortChainBuilder();
        result.setUuid(new Uuid(neutronPortChain.getID()));
        result.setName(neutronPortChain.getName());
        result.setTenantId(toUuid(neutronPortChain.getTenantID()));
        if (neutronPortChain.getPortPairGroupsUUID() != null) {
            List<Uuid> portPairGroups = new ArrayList<>();
            for (String uuid : neutronPortChain.getPortPairGroupsUUID()) {
                portPairGroups.add(new Uuid(uuid));
            }
            result.setPortPairGroups(portPairGroups);
        }
        if (neutronPortChain.getFlowClassifiersUUID() != null) {
            List<Uuid> flowClassifiers = new ArrayList<>();
            for (String uuid : neutronPortChain.getFlowClassifiersUUID()) {
                flowClassifiers.add(new Uuid(uuid));
            }
            result.setFlowClassifiers(flowClassifiers);
        }
        if (neutronPortChain.getChainParameters() != null) {
            List<ChainParameters> chainParams = new ArrayList<>();
            for (String paramKey : neutronPortChain.getChainParameters().keySet()) {
                ChainParametersBuilder param = new ChainParametersBuilder();
                param.setKey(new ChainParametersKey(paramKey));
                param.setChainParameter(paramKey);
                param.setChainParameterValue(neutronPortChain.getChainParameters().get(paramKey));
                chainParams.add(param.build());
            }
            result.setChainParameters(chainParams);
        }
        LOGGER.trace("toMd: Yang SFC Port Chain data : {}", result);
        return result.build();
    }

    @Override
    protected PortChain toMd(String uuid) {
        final PortChainBuilder portChainBuilder = new PortChainBuilder();
        portChainBuilder.setUuid(toUuid(uuid));
        return portChainBuilder.build();
    }

    @Override
    protected NeutronSFCPortChain fromMd(PortChain mdPortChain) {
        LOGGER.trace("fromMd: Yang SFC Port Chain data : {}", mdPortChain);
        NeutronSFCPortChain result = new NeutronSFCPortChain();
        result.setID(mdPortChain.getUuid().getValue());
        result.setName(mdPortChain.getName());
        result.setTenantID(mdPortChain.getTenantId());
        if (mdPortChain.getPortPairGroups() != null) {
            List<String> portPairGroups = new ArrayList<>();
            for (Uuid uuid : mdPortChain.getPortPairGroups()) {
                portPairGroups.add(uuid.getValue());
            }
            result.setPortPairGroupsUUID(portPairGroups);
        }
        if (mdPortChain.getFlowClassifiers() != null) {
            List<String> flowClassifiers = new ArrayList<>();
            for (Uuid uuid : mdPortChain.getFlowClassifiers()) {
                flowClassifiers.add(uuid.getValue());
            }
            result.setFlowClassifiersUUID(flowClassifiers);
        }
        if (mdPortChain.getChainParameters() != null) {
            HashMap<String, String> chainParams = new HashMap<>();
            for (ChainParameters param : mdPortChain.getChainParameters()) {
                chainParams.put(param.getChainParameter(), param.getChainParameterValue());
            }
            result.setChainParameters(chainParams);
        }
        LOGGER.trace("fromMd: REST SFC Port Chain data : {}", result);
        return result;
    }
}
