/*
 * Copyright (c) 2016 Intel Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.transcriber;

import java.util.ArrayList;
import java.util.List;
import org.opendaylight.controller.md.sal.binding.api.DataBroker;
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
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.qos.rev160613.qos.attributes.qos.policies.qos.policy.BandwidthlimitRules;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.qos.rev160613.qos.attributes.qos.policies.qos.policy.BandwidthlimitRulesBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.qos.rev160613.qos.attributes.qos.policies.qos.policy.DscpmarkingRules;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.qos.rev160613.qos.attributes.qos.policies.qos.policy.DscpmarkingRulesBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.qos.rev160613.qos.attributes.qos.policies.qos.policy.MinimumbandwidthRules;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.qos.rev160613.qos.attributes.qos.policies.qos.policy.MinimumbandwidthRulesBuilder;

public final class NeutronQosPolicyInterface
        extends AbstractNeutronInterface<QosPolicy, QosPolicies, QosPolicyKey,NeutronQosPolicy>
        implements INeutronQosPolicyCRUD {
    NeutronQosPolicyInterface(DataBroker db) {
        super(QosPolicyBuilder.class, db);
    }

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
            final List<BandwidthlimitRules> listBandwidth = new ArrayList<>();
            for (final NeutronQosBandwidthLimitRule bandwidthRule : qosPolicy.getBwLimitRules()) {
                final BandwidthlimitRulesBuilder BandwidthlimitRulesBuilder = new BandwidthlimitRulesBuilder();
                BandwidthlimitRulesBuilder.setUuid(toUuid(bandwidthRule.getID()));
                BandwidthlimitRulesBuilder.setTenantId(toUuid(bandwidthRule.getTenantID()));
                BandwidthlimitRulesBuilder.setMaxKbps(bandwidthRule.getMaxKbps());
                BandwidthlimitRulesBuilder.setMaxBurstKbps(bandwidthRule.getMaxBurstKbps());
                listBandwidth.add(BandwidthlimitRulesBuilder.build());
            }
            qosPolicyBuilder.setBandwidthlimitRules(listBandwidth);
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
        if (qosPolicy.getMinBwRules() != null) {
            final List<MinimumbandwidthRules> listMinimumBandwidth = new ArrayList<>();
            for (final NeutronQosMinimumBandwidthRule minimumBandwidthRule : qosPolicy.getMinBwRules()) {
                final MinimumbandwidthRulesBuilder minimumBandwidthRulesBuilder =
                        new MinimumbandwidthRulesBuilder();
                minimumBandwidthRulesBuilder.setUuid(toUuid(minimumBandwidthRule.getID()));
                minimumBandwidthRulesBuilder.setTenantId(toUuid(minimumBandwidthRule.getTenantID()));
                minimumBandwidthRulesBuilder.setMinKbps(minimumBandwidthRule.getMinKbps());
                minimumBandwidthRulesBuilder.setDirection(DirectionMapper.get(minimumBandwidthRule
                    .getDirection()));
                listMinimumBandwidth.add(minimumBandwidthRulesBuilder.build());
            }
            qosPolicyBuilder.setMinimumbandwidthRules(listMinimumBandwidth);
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
        if (qosPolicy.getBandwidthlimitRules() != null) {
            final List<NeutronQosBandwidthLimitRule> BandwidthLimitRule = new ArrayList<>();
            for (final BandwidthlimitRules rule : qosPolicy.getBandwidthlimitRules()) {
                NeutronQosBandwidthLimitRule opt = new NeutronQosBandwidthLimitRule();
                opt.setID(rule.getUuid().getValue());
                opt.setTenantID(rule.getTenantId().getValue());
                opt.setMaxKbps(rule.getMaxKbps());
                opt.setMaxBurstKbps(rule.getMaxBurstKbps());
                BandwidthLimitRule.add(opt);
            }
            result.setQosBwLimitRules(BandwidthLimitRule);
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
        if (qosPolicy.getMinimumbandwidthRules() != null) {
            final List<NeutronQosMinimumBandwidthRule> minBandwidthRules = new ArrayList<>();
            for (final MinimumbandwidthRules rule : qosPolicy.getMinimumbandwidthRules()) {
                NeutronQosMinimumBandwidthRule opt = new NeutronQosMinimumBandwidthRule();
                opt.setID(rule.getTenantId().getValue());
                opt.setTenantID(rule.getTenantId().getValue());
                opt.setMinKbps(rule.getMinKbps());
                opt.setDirection(DirectionMapper.getName(rule.getDirection()));
                minBandwidthRules.add(opt);
            }
            result.setMinBwRules(minBandwidthRules);
        }
        return result;
    }
}
