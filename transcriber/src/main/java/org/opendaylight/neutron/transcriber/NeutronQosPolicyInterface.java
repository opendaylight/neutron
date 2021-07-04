/*
 * Copyright (c) 2016, 2018 Intel Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.neutron.transcriber;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.apache.aries.blueprint.annotation.service.Service;
import org.opendaylight.mdsal.binding.api.DataBroker;
import org.opendaylight.neutron.spi.INeutronQosPolicyCRUD;
import org.opendaylight.neutron.spi.NeutronQosBandwidthLimitRule;
import org.opendaylight.neutron.spi.NeutronQosDscpMarkingRule;
import org.opendaylight.neutron.spi.NeutronQosMinimumBandwidthRule;
import org.opendaylight.neutron.spi.NeutronQosPolicy;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.constants.rev150712.NeutronUtils.DirectionMapper;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.qos.rev160613.qos.attributes.QosPolicies;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.qos.rev160613.qos.attributes.qos.policies.QosPolicy;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.qos.rev160613.qos.attributes.qos.policies.QosPolicyBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.qos.rev160613.qos.attributes.qos.policies.QosPolicyKey;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.qos.rev160613.qos.attributes.qos.policies.qos.policy.BandwidthLimitRules;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.qos.rev160613.qos.attributes.qos.policies.qos.policy.BandwidthLimitRulesBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.qos.rev160613.qos.attributes.qos.policies.qos.policy.DscpmarkingRules;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.qos.rev160613.qos.attributes.qos.policies.qos.policy.DscpmarkingRulesBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.qos.rev160613.qos.attributes.qos.policies.qos.policy.MinimumbandwidthRules;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.qos.rev160613.qos.attributes.qos.policies.qos.policy.MinimumbandwidthRulesBuilder;
import org.opendaylight.yangtools.yang.binding.util.BindingMap;
import org.opendaylight.yangtools.yang.common.Uint64;
import org.opendaylight.yangtools.yang.common.Uint8;

@Singleton
@Service(classes = INeutronQosPolicyCRUD.class)
public final class NeutronQosPolicyInterface
        extends AbstractNeutronInterface<QosPolicy, QosPolicies, QosPolicyKey,NeutronQosPolicy>
        implements INeutronQosPolicyCRUD {

    @Inject
    public NeutronQosPolicyInterface(DataBroker db) {
        super(QosPolicyBuilder.class, db);
    }

    @Override
    protected Collection<QosPolicy> getDataObjectList(QosPolicies qosPolicies) {
        return qosPolicies.nonnullQosPolicy().values();
    }

    @Override
    protected QosPolicy toMd(NeutronQosPolicy qosPolicy) {
        final QosPolicyBuilder qosPolicyBuilder = new QosPolicyBuilder();
        toMdBaseAttributes(qosPolicy, qosPolicyBuilder);
        if (qosPolicy.getPolicyIsShared() != null) {
            qosPolicyBuilder.setShared(qosPolicy.getPolicyIsShared());
        }
        if (qosPolicy.getBandwidthLimitRules() != null) {
            qosPolicyBuilder.setBandwidthLimitRules(qosPolicy.getBandwidthLimitRules().stream()
                .map(bandwidthLimitRule -> toMdIds(bandwidthLimitRule, BandwidthLimitRulesBuilder.class)
                    .setMaxKbps(Uint64.valueOf(bandwidthLimitRule.getMaxKbps()))
                    .setMaxBurstKbps(Uint64.valueOf(bandwidthLimitRule.getMaxBurstKbps()))
                    .setDirection(DirectionMapper.get(bandwidthLimitRule.getDirection()))
                    .build())
                .collect(BindingMap.toOrderedMap()));
        }

        if (qosPolicy.getDscpMarkingRules() != null) {
            qosPolicyBuilder.setDscpmarkingRules(qosPolicy.getDscpMarkingRules().stream()
                .map(dscpMarkingRule -> toMdIds(dscpMarkingRule, DscpmarkingRulesBuilder.class)
                    .setDscpMark(Uint8.valueOf(dscpMarkingRule.getDscpMark()))
                    .build())
                .collect(BindingMap.toOrderedMap()));
        }
        if (qosPolicy.getMinimumBandwidthRules() != null) {
            qosPolicyBuilder.setMinimumbandwidthRules(qosPolicy.getMinimumBandwidthRules().stream()
                .map(minimumBandwidthRule -> new MinimumbandwidthRulesBuilder()
                    .setUuid(toUuid(minimumBandwidthRule.getID()))
                    .setTenantId(toUuid(minimumBandwidthRule.getTenantID()))
                    .setMinKbps(Uint64.valueOf(minimumBandwidthRule.getMinKbps()))
                    .setDirection(DirectionMapper.get(minimumBandwidthRule.getDirection()))
                    .build())
                .collect(BindingMap.toOrderedMap()));
        }
        return qosPolicyBuilder.build();
    }

    @Override
    protected NeutronQosPolicy fromMd(QosPolicy qosPolicy) {
        final NeutronQosPolicy result = new NeutronQosPolicy();
        fromMdBaseAttributes(qosPolicy, result);
        if (qosPolicy.getShared() != null) {
            result.setPolicyIsShared(qosPolicy.getShared());
        }
        if (qosPolicy.getBandwidthLimitRules() != null) {
            final List<NeutronQosBandwidthLimitRule> bandwidthLimitRules = new ArrayList<>();
            for (final BandwidthLimitRules rule : qosPolicy.getBandwidthLimitRules().values()) {
                NeutronQosBandwidthLimitRule opt = new NeutronQosBandwidthLimitRule();
                opt.setMaxKbps(rule.getMaxKbps().toJava());
                opt.setMaxBurstKbps(rule.getMaxBurstKbps().toJava());
                opt.setDirection(DirectionMapper.getDirectionString(rule.getDirection()));
                bandwidthLimitRules.add(opt);
            }
            result.setQosBandwidthLimitRules(bandwidthLimitRules);
        }
        if (qosPolicy.getDscpmarkingRules() != null) {
            final List<NeutronQosDscpMarkingRule> dscpMarkingRules = new ArrayList<>();
            for (final DscpmarkingRules rule : qosPolicy.getDscpmarkingRules().values()) {
                NeutronQosDscpMarkingRule opt = new NeutronQosDscpMarkingRule();
                opt.setDscpMark(rule.getDscpMark().toJava());
                dscpMarkingRules.add(opt);
            }
            result.setDscpMarkingRules(dscpMarkingRules);
        }
        if (qosPolicy.getMinimumbandwidthRules() != null) {
            final List<NeutronQosMinimumBandwidthRule> minimumBandwidthRules = new ArrayList<>();
            for (final MinimumbandwidthRules rule : qosPolicy.getMinimumbandwidthRules().values()) {
                NeutronQosMinimumBandwidthRule opt = new NeutronQosMinimumBandwidthRule();
                opt.setID(rule.getTenantId().getValue());
                opt.setTenantID(rule.getTenantId().getValue());
                opt.setMinKbps(rule.getMinKbps().toJava());
                opt.setDirection(DirectionMapper.getDirectionString(rule.getDirection()));
                minimumBandwidthRules.add(opt);
            }
            result.setMinimumBandwidthRules(minimumBandwidthRules);
        }
        return result;
    }
}
