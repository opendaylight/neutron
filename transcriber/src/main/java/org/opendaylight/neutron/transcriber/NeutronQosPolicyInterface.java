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
import org.opendaylight.neutron.spi.NeutronQosBandwidthRule;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class NeutronQosPolicyInterface
        extends AbstractNeutronInterface<QosPolicy, QosPolicies, QosPolicyKey,NeutronQosPolicy>
        implements INeutronQosPolicyCRUD {

    private static final Logger LOGGER = LoggerFactory.getLogger(NeutronQosPolicyInterface.class);

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
            final List<BandwidthLimitRules> listBandwith = new ArrayList<>();
            final BandwidthLimitRulesBuilder bandwidthLimitRulesBuilder = new BandwidthLimitRulesBuilder();
            for (final NeutronQosBandwidthRule bandWidthRule : qosPolicy.getBwLimitRules()) {
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
            final DscpmarkingRulesBuilder dscpmarkingRulesBuilder = new DscpmarkingRulesBuilder();
            for (final NeutronQosDscpMarkingRule dscpRule : qosPolicy.getDscpRules()) {
                dscpmarkingRulesBuilder.setUuid(toUuid(dscpRule.getID()));
                dscpmarkingRulesBuilder.setTenantId(toUuid(dscpRule.getTenantID()));
                dscpmarkingRulesBuilder.setDscpMark(dscpRule.getDscpMark());
                listDscp.add(dscpmarkingRulesBuilder.build());
            }
            qosPolicyBuilder.setDscpmarkingRules(listDscp);
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
        return result;
    }
}
