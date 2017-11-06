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
import org.opendaylight.neutron.spi.NeutronQosPolicy;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.qos.rev160613.qos.attributes.QosPolicies;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.qos.rev160613.qos.attributes.qos.policies.QosPolicy;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.qos.rev160613.qos.attributes.qos.policies.QosPolicyBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.qos.rev160613.qos.attributes.qos.policies.QosPolicyKey;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.qos.rev160613.qos.attributes.qos.policies.qos.policy.BandwidthLimitRules;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.qos.rev160613.qos.attributes.qos.policies.qos.policy.BandwidthLimitRulesBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.qos.rev160613.qos.attributes.qos.policies.qos.policy.DscpmarkingRules;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.qos.rev160613.qos.attributes.qos.policies.qos.policy.DscpmarkingRulesBuilder;

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
        if (qosPolicy.getBandwidthLimitRules() != null) {
            final List<BandwidthLimitRules> listBandwith = new ArrayList<>();
            for (final NeutronQosBandwidthLimitRule bandwidthLimitRule : qosPolicy.getBandwidthLimitRules()) {
                final BandwidthLimitRulesBuilder bandwidthLimitRulesBuilder =
                        toMdIds(bandwidthLimitRule, BandwidthLimitRulesBuilder.class);
                bandwidthLimitRulesBuilder.setMaxKbps(bandwidthLimitRule.getMaxKbps());
                bandwidthLimitRulesBuilder.setMaxBurstKbps(bandwidthLimitRule.getMaxBurstKbps());
                listBandwith.add(bandwidthLimitRulesBuilder.build());
            }
            qosPolicyBuilder.setBandwidthLimitRules(listBandwith);
        }

        if (qosPolicy.getDscpMarkingRules() != null) {
            final List<DscpmarkingRules> listDscpMarking = new ArrayList<>();
            for (final NeutronQosDscpMarkingRule dscpMarkingRule : qosPolicy.getDscpMarkingRules()) {
                final DscpmarkingRulesBuilder dscpmarkingRulesBuilder =
                        toMdIds(dscpMarkingRule, DscpmarkingRulesBuilder.class);
                dscpmarkingRulesBuilder.setDscpMark(dscpMarkingRule.getDscpMark());
                listDscpMarking.add(dscpmarkingRulesBuilder.build());
            }
            qosPolicyBuilder.setDscpmarkingRules(listDscpMarking);
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
            final List<NeutronQosBandwidthLimitRule> bandwidthLimitRules = new ArrayList<>();
            for (final BandwidthLimitRules rule : qosPolicy.getBandwidthLimitRules()) {
                NeutronQosBandwidthLimitRule opt = new NeutronQosBandwidthLimitRule();
                opt.setMaxKbps(rule.getMaxKbps());
                opt.setMaxBurstKbps(rule.getMaxBurstKbps());
                bandwidthLimitRules.add(opt);
            }
            result.setQosBandwidthLimitRules(bandwidthLimitRules);
        }
        if (qosPolicy.getDscpmarkingRules() != null) {
            final List<NeutronQosDscpMarkingRule> dscpMarkingRules = new ArrayList<>();
            for (final DscpmarkingRules rule : qosPolicy.getDscpmarkingRules()) {
                NeutronQosDscpMarkingRule opt = new NeutronQosDscpMarkingRule();
                opt.setDscpMark(rule.getDscpMark());
                dscpMarkingRules.add(opt);
            }
            result.setDscpMarkingRules(dscpMarkingRules);
        }
        return result;
    }
}
