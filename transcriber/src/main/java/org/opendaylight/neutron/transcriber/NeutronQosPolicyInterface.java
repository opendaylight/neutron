/*
 * Copyright (c) 2016 Intel Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.transcriber;

import com.google.common.collect.ImmutableBiMap;
import java.util.ArrayList;
import java.util.List;
import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.neutron.spi.INeutronQosPolicyCRUD;
import org.opendaylight.neutron.spi.NeutronQosBandwidthRule;
import org.opendaylight.neutron.spi.NeutronQosDscpMarkingRule;
import org.opendaylight.neutron.spi.NeutronQosMinimumBandwidthRule;
import org.opendaylight.neutron.spi.NeutronQosPolicy;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.constants.rev150712.DirectionBase;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.constants.rev150712.DirectionEgress;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.constants.rev150712.DirectionIngress;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.qos.rev160613.qos.attributes.QosPolicies;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.qos.rev160613.qos.attributes.qos.policies.QosPolicy;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.qos.rev160613.qos.attributes.qos.policies.QosPolicyBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.qos.rev160613.qos.attributes.qos.policies.QosPolicyKey;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.qos.rev160613.qos.attributes.qos.policies.qos.policy.BandwidthLimitRules;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.qos.rev160613.qos.attributes.qos.policies.qos.policy.BandwidthLimitRulesBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.qos.rev160613.qos.attributes.qos.policies.qos.policy.DscpmarkingRules;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.qos.rev160613.qos.attributes.qos.policies.qos.policy.DscpmarkingRulesBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.qos.rev160613.qos.attributes.qos.policies.qos.policy.MinimumbandwidthLimitRules;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.qos.rev160613.qos.attributes.qos.policies.qos.policy.MinimumbandwidthLimitRulesBuilder;

public final class NeutronQosPolicyInterface
        extends AbstractNeutronInterface<QosPolicy, QosPolicies, QosPolicyKey,NeutronQosPolicy>
        implements INeutronQosPolicyCRUD {
    NeutronQosPolicyInterface(DataBroker db) {
        super(QosPolicyBuilder.class, db);
    }

    private static final ImmutableBiMap<Class<? extends DirectionBase>,
        String> DIRECTION_MAP = new ImmutableBiMap.Builder<Class<? extends DirectionBase>, String>()
        .put(DirectionEgress.class, "egress").put(DirectionIngress.class, "ingress").build();

    @Override
    protected List<QosPolicy> getDataObjectList(QosPolicies qosPolicies) {
        return qosPolicies.getQosPolicy();
    }

    @Override
    protected QosPolicy toMd(NeutronQosPolicy qosPolicy) {
        final QosPolicyBuilder qosPolicyBuilder = new QosPolicyBuilder();
        toMdBaseAttributes(qosPolicy, qosPolicyBuilder);
        if (qosPolicy.getPolicyIsShared() != null) {
            qosPolicyBuilder.setShared(qosPolicy.getPolicyIsShared());
        }
        if (qosPolicy.getBwLimitRules() != null) {
            final List<BandwidthLimitRules> listBandwith = new ArrayList<>();
            for (final NeutronQosBandwidthRule bandWidthRule : qosPolicy.getBwLimitRules()) {
                final BandwidthLimitRulesBuilder bandwidthLimitRulesBuilder = new BandwidthLimitRulesBuilder();
                bandwidthLimitRulesBuilder.setUuid(toUuid(bandWidthRule.getID()));
                bandwidthLimitRulesBuilder.setTenantId(toUuid(bandWidthRule.getTenantID()));
                bandwidthLimitRulesBuilder.setMaxKbps(bandWidthRule.getMaxKbps());
                bandwidthLimitRulesBuilder.setMaxBurstKbps(bandWidthRule.getMaxBurstKbps());
                listBandwith.add(bandwidthLimitRulesBuilder.build());
            }
            qosPolicyBuilder.setBandwidthLimitRules(listBandwith);
        }
        if (qosPolicy.getDscpRules() != null) {
            final List<DscpmarkingRules> listDscp = new ArrayList<>();
            for (final NeutronQosDscpMarkingRule dscpRule : qosPolicy.getDscpRules()) {
                final DscpmarkingRulesBuilder dscpmarkingRulesBuilder = new DscpmarkingRulesBuilder();
                dscpmarkingRulesBuilder.setUuid(toUuid(dscpRule.getID()));
                dscpmarkingRulesBuilder.setTenantId(toUuid(dscpRule.getTenantID()));
                dscpmarkingRulesBuilder.setDscpMark(dscpRule.getDscpMark());
                listDscp.add(dscpmarkingRulesBuilder.build());
            }
            qosPolicyBuilder.setDscpmarkingRules(listDscp);
        }
        if (qosPolicy.getMinBwLimitRules() != null) {
            final List<MinimumbandwidthLimitRules> listminimumBandwidth = new ArrayList<>();
            final ImmutableBiMap<String, Class<? extends DirectionBase>> mapper = DIRECTION_MAP.inverse();
            for (final NeutronQosMinimumBandwidthRule minimumBandwidthRule : qosPolicy.getMinBwLimitRules()) {
                final MinimumbandwidthLimitRulesBuilder minimumbandwidthLimitRulesBuilder =
                        new MinimumbandwidthLimitRulesBuilder();
                minimumbandwidthLimitRulesBuilder.setDirection(mapper.get(minimumBandwidthRule
                    .getDirection()));
                minimumbandwidthLimitRulesBuilder.setUuid(toUuid(minimumBandwidthRule.getID()));
                minimumbandwidthLimitRulesBuilder.setTenantId(toUuid(minimumBandwidthRule.getTenantID()));
                minimumbandwidthLimitRulesBuilder.setMinKbps(minimumBandwidthRule.getMinKbps());
                listminimumBandwidth.add(minimumbandwidthLimitRulesBuilder.build());
            }
            qosPolicyBuilder.setMinimumbandwidthLimitRules(listminimumBandwidth);
        }
        return qosPolicyBuilder.build();
    }

    @Override
    protected NeutronQosPolicy fromMd(QosPolicy qosPolicy) {
        final NeutronQosPolicy result = new NeutronQosPolicy();
        fromMdBaseAttributes(qosPolicy, result);
        if (qosPolicy.isShared() != null) {
            result.setPolicyIsShared(qosPolicy.isShared());
        }
        if (qosPolicy.getBandwidthLimitRules() != null) {
            final List<NeutronQosBandwidthRule> bandWidthRules = new ArrayList<>();
            for (final BandwidthLimitRules rule : qosPolicy.getBandwidthLimitRules()) {
                NeutronQosBandwidthRule opt = new NeutronQosBandwidthRule();
                opt.setID(rule.getUuid().getValue());
                opt.setTenantID(rule.getTenantId().getValue());
                opt.setMaxKbps(rule.getMaxKbps());
                opt.setMaxBurstKbps(rule.getMaxBurstKbps());
                bandWidthRules.add(opt);
            }
            result.setQosBwLimitRules(bandWidthRules);
        }
        if (qosPolicy.getDscpmarkingRules() != null) {
            final List<NeutronQosDscpMarkingRule> dscpRules = new ArrayList<>();
            for (final DscpmarkingRules rule : qosPolicy.getDscpmarkingRules()) {
                NeutronQosDscpMarkingRule opt = new NeutronQosDscpMarkingRule();
                opt.setID(rule.getUuid().getValue());
                opt.setTenantID(rule.getTenantId().getValue());
                opt.setDscpMark(rule.getDscpMark());
                dscpRules.add(opt);
            }
            result.setDscpRules(dscpRules);
        }
        if (qosPolicy.getMinimumbandwidthLimitRules() != null) {
            final List<NeutronQosMinimumBandwidthRule> minBandwidthRules = new ArrayList<>();
            for (final MinimumbandwidthLimitRules rule : qosPolicy.getMinimumbandwidthLimitRules()) {
                NeutronQosMinimumBandwidthRule opt = new NeutronQosMinimumBandwidthRule();
                opt.setID(rule.getTenantId().getValue());
                opt.setTenantID(rule.getTenantId().getValue());
                opt.setMinKbps(rule.getMinKbps());
                opt.setDirection(DIRECTION_MAP .get(rule.getDirection()));
                minBandwidthRules.add(opt);
            }
            result.setMinBwLimitRules(minBandwidthRules);
        }
        return result;
    }
}
