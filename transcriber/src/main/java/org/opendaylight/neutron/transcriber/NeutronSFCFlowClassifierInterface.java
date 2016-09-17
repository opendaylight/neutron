/*
 * Copyright (c) 2016 Brocade Communications Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.neutron.transcriber;

import com.google.common.collect.ImmutableBiMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.neutron.spi.INeutronSFCFlowClassifierCRUD;
import org.opendaylight.neutron.spi.NeutronSFCFlowClassifier;
import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.inet.types.rev130715.IpPrefix;
import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.yang.types.rev130715.Uuid;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.constants.rev150712.EthertypeBase;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.constants.rev150712.EthertypeV4;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.constants.rev150712.EthertypeV6;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.constants.rev150712.ProtocolBase;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.constants.rev150712.ProtocolIcmp;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.constants.rev150712.ProtocolTcp;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.constants.rev150712.ProtocolUdp;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.rev150712.Neutron;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.sfc.flow.classifier.rev160511.flow.classifier.match.attributes.L7Parameter;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.sfc.flow.classifier.rev160511.flow.classifier.match.attributes.L7ParameterBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.sfc.flow.classifier.rev160511.flow.classifier.match.attributes.L7ParameterKey;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.sfc.flow.classifier.rev160511.sfc.flow.classifiers.attributes.SfcFlowClassifiers;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.sfc.flow.classifier.rev160511.sfc.flow.classifiers.attributes.sfc.flow.classifiers.SfcFlowClassifier;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.sfc.flow.classifier.rev160511.sfc.flow.classifiers.attributes.sfc.flow.classifiers.SfcFlowClassifierBuilder;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Anil Vishnoi (avishnoi@Brocade.com) on 6/24/16.
 */
public final class NeutronSFCFlowClassifierInterface
        extends AbstractNeutronInterface<SfcFlowClassifier, SfcFlowClassifiers, NeutronSFCFlowClassifier>
        implements INeutronSFCFlowClassifierCRUD {

    private static final Logger LOGGER = LoggerFactory.getLogger(NeutronSFCFlowClassifierInterface.class);

    private static final ImmutableBiMap<Class<? extends EthertypeBase>,
            String> ETHERTYPE_MAP = new ImmutableBiMap.Builder<Class<? extends EthertypeBase>, String>()
                    .put(EthertypeV4.class, "IPv4").put(EthertypeV6.class, "IPv6").build();

    private static final ImmutableBiMap<Class<? extends ProtocolBase>,
            String> PROTOCOL_MAP = new ImmutableBiMap.Builder<Class<? extends ProtocolBase>, String>()
                    .put(ProtocolTcp.class, "TCP").put(ProtocolUdp.class, "UDP").put(ProtocolIcmp.class, "ICMP")
                    .build();

    NeutronSFCFlowClassifierInterface(DataBroker db) {
        super(db);
    }

    @Override
    protected List<SfcFlowClassifier> getDataObjectList(SfcFlowClassifiers dataObjects) {
        return dataObjects.getSfcFlowClassifier();
    }

    @Override
    protected InstanceIdentifier<SfcFlowClassifier> createInstanceIdentifier(SfcFlowClassifier classifier) {
        return InstanceIdentifier.create(Neutron.class).child(SfcFlowClassifiers.class).child(SfcFlowClassifier.class,
                classifier.getKey());
    }

    @Override
    protected InstanceIdentifier<SfcFlowClassifiers> createInstanceIdentifier() {
        return InstanceIdentifier.create(Neutron.class).child(SfcFlowClassifiers.class);
    }

    @Override
    protected SfcFlowClassifier toMd(NeutronSFCFlowClassifier neutronClassifier) {

        LOGGER.trace("toMd: REST SFC Flow Classifier data : {}", neutronClassifier);

        SfcFlowClassifierBuilder result = new SfcFlowClassifierBuilder();
        if (neutronClassifier.getID() != null) {
            result.setUuid(new Uuid(neutronClassifier.getID()));
        }
        if (neutronClassifier.getName() != null) {
            result.setName(neutronClassifier.getName());
        }
        if (neutronClassifier.getTenantID() != null) {
            result.setTenantId(toUuid(neutronClassifier.getTenantID()));
        }
        if (neutronClassifier.getEthertype() != null) {
            final ImmutableBiMap<String, Class<? extends EthertypeBase>> mapper = ETHERTYPE_MAP.inverse();

            result.setEthertype(mapper.get(neutronClassifier.getEthertype()));
        }
        if (neutronClassifier.getProtocol() != null) {
            final ImmutableBiMap<String, Class<? extends ProtocolBase>> mapper = PROTOCOL_MAP.inverse();
            result.setProtocol(mapper.get(neutronClassifier.getProtocol()));
        }
        if (neutronClassifier.getSourcePortRangeMin() != null) {
            result.setSourcePortRangeMin(neutronClassifier.getSourcePortRangeMin());
        }
        if (neutronClassifier.getSourcePortRangeMax() != null) {
            result.setSourcePortRangeMax(neutronClassifier.getSourcePortRangeMax());
        }
        if (neutronClassifier.getDestinationPortRangeMin() != null) {
            result.setDestinationPortRangeMin(neutronClassifier.getDestinationPortRangeMin());
        }
        if (neutronClassifier.getDestinationPortRangeMax() != null) {
            result.setDestinationPortRangeMax(neutronClassifier.getDestinationPortRangeMax());
        }
        if (neutronClassifier.getSourceIpPrefix() != null) {
            result.setSourceIpPrefix(new IpPrefix(neutronClassifier.getSourceIpPrefix().toCharArray()));
        }
        if (neutronClassifier.getDestinationIpPrefix() != null) {
            result.setDestinationIpPrefix(new IpPrefix(neutronClassifier.getDestinationIpPrefix().toCharArray()));
        }
        if (neutronClassifier.getLogicalSourcePortUUID() != null) {
            result.setLogicalSourcePort(new Uuid(neutronClassifier.getLogicalSourcePortUUID()));
        }
        if (neutronClassifier.getLogicalDestinationPortUUID() != null) {
            result.setLogicalDestinationPort(new Uuid(neutronClassifier.getLogicalDestinationPortUUID()));
        }
        if (neutronClassifier.getL7Parameters() != null) {
            List<L7Parameter> l7Params = new ArrayList<>();
            for (String paramKey : neutronClassifier.getL7Parameters().keySet()) {
                L7ParameterBuilder param = new L7ParameterBuilder();
                param.setKey(new L7ParameterKey(paramKey));
                param.setMatchParameter(paramKey);
                param.setMatchParameterValue(neutronClassifier.getL7Parameters().get(paramKey));
                l7Params.add(param.build());
            }
            result.setL7Parameter(l7Params);
        }
        LOGGER.trace("toMd: Yang SFC Flow Classifier data : {}", result);
        return result.build();
    }

    @Override
    protected SfcFlowClassifier toMd(String uuid) {
        final SfcFlowClassifierBuilder sfcFlowClassifierBuilder = new SfcFlowClassifierBuilder();
        sfcFlowClassifierBuilder.setUuid(toUuid(uuid));
        return sfcFlowClassifierBuilder.build();
    }

    @Override
    protected NeutronSFCFlowClassifier fromMd(SfcFlowClassifier mdClassifier) {
        LOGGER.trace("fromMd: Yang SFC flow classifier data : {}", mdClassifier);
        NeutronSFCFlowClassifier result = new NeutronSFCFlowClassifier();
        result.setID(mdClassifier.getUuid().getValue());
        result.setName(mdClassifier.getName());
        result.setTenantID(mdClassifier.getTenantId());
        if (mdClassifier.getEthertype() != null) {
            result.setEthertype(ETHERTYPE_MAP.get(mdClassifier.getEthertype()));
        }
        if (mdClassifier.getProtocol() != null) {
            result.setProtocol(PROTOCOL_MAP.get(mdClassifier.getProtocol()));
        }
        if (mdClassifier.getSourcePortRangeMin() != null) {
            result.setSourcePortRangeMin(mdClassifier.getSourcePortRangeMin());
        }
        if (mdClassifier.getSourcePortRangeMax() != null) {
            result.setSourcePortRangeMax(mdClassifier.getSourcePortRangeMax());
        }
        if (mdClassifier.getDestinationPortRangeMin() != null) {
            result.setDestinationPortRangeMin(mdClassifier.getDestinationPortRangeMin());
        }
        if (mdClassifier.getDestinationPortRangeMax() != null) {
            result.setDestinationPortRangeMax(mdClassifier.getDestinationPortRangeMax());
        }
        if (mdClassifier.getSourceIpPrefix() != null) {
            result.setSourceIpPrefix(String.valueOf(mdClassifier.getSourceIpPrefix().getValue()));
        }
        if (mdClassifier.getDestinationIpPrefix() != null) {
            result.setDestinationIpPrefix(String.valueOf(mdClassifier.getDestinationIpPrefix().getValue()));
        }
        if (mdClassifier.getLogicalSourcePort() != null) {
            result.setLogicalSourcePortUUID(mdClassifier.getLogicalSourcePort().getValue());
        }
        if (mdClassifier.getLogicalDestinationPort() != null) {
            result.setLogicalDestinationPortUUID(mdClassifier.getLogicalDestinationPort().getValue());
        }
        if (mdClassifier.getL7Parameter() != null) {
            HashMap<String, String> l7Param = new HashMap<>();
            for (L7Parameter param : mdClassifier.getL7Parameter()) {
                l7Param.put(param.getMatchParameter(), param.getMatchParameterValue());
            }
            result.setL7Parameters(l7Param);
        }
        LOGGER.trace("fromMd: REST SFC Flow Classifier data : {}", result);
        return result;
    }
}
