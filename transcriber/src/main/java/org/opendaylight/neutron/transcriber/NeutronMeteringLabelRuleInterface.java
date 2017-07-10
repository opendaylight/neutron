/*
 * Copyright (c) 2015 IBM Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.transcriber;

import java.util.List;
import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.neutron.spi.INeutronMeteringLabelRuleCRUD;
import org.opendaylight.neutron.spi.NeutronMeteringLabelRule;
import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.inet.types.rev130715.IpPrefix;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.constants.rev150712.NeutronUtils.DirectionMapper;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.metering.rev150712.metering.rules.attributes.MeteringRules;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.metering.rev150712.metering.rules.attributes.metering.rules.MeteringRule;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.metering.rev150712.metering.rules.attributes.metering.rules.MeteringRuleBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.metering.rev150712.metering.rules.attributes.metering.rules.MeteringRuleKey;

public final class NeutronMeteringLabelRuleInterface
        extends AbstractNeutronInterface<MeteringRule, MeteringRules, MeteringRuleKey, NeutronMeteringLabelRule>
        implements INeutronMeteringLabelRuleCRUD {
    NeutronMeteringLabelRuleInterface(DataBroker db) {
        super(MeteringRuleBuilder.class, db);
    }

    // IfNBMeteringLabelRuleCRUD methods
    @Override
    protected List<MeteringRule> getDataObjectList(MeteringRules rules) {
        return rules.getMeteringRule();
    }

    @Override
    protected MeteringRule toMd(NeutronMeteringLabelRule meteringLabelRule) {
        final MeteringRuleBuilder meteringRuleBuilder = new MeteringRuleBuilder();
        if (meteringLabelRule.getID() != null) {
            meteringRuleBuilder.setUuid(toUuid(meteringLabelRule.getID()));
        }
        if (meteringLabelRule.getTenantID() != null) {
            meteringRuleBuilder.setTenantId(toUuid(meteringLabelRule.getTenantID()));
        }
        if (meteringLabelRule.getMeteringLabelRuleLabelID() != null) {
            meteringRuleBuilder.setMeteringLabelId(toUuid(meteringLabelRule.getMeteringLabelRuleLabelID()));
        }
        if (meteringLabelRule.getMeteringLabelRuleDirection() != null) {
            meteringRuleBuilder.setDirection(
                    DirectionMapper.get(meteringLabelRule.getMeteringLabelRuleDirection()));
        }
        if (meteringLabelRule.getMeteringLabelRuleRemoteIpPrefix() != null) {
            final IpPrefix ipPrefix = new IpPrefix(
                    meteringLabelRule.getMeteringLabelRuleRemoteIpPrefix().toCharArray());
            meteringRuleBuilder.setRemoteIpPrefix(ipPrefix);
        }
        meteringRuleBuilder.setExcluded(meteringLabelRule.getMeteringLabelRuleExcluded());
        return meteringRuleBuilder.build();
    }

    @Override
    protected NeutronMeteringLabelRule fromMd(MeteringRule rule) {
        final NeutronMeteringLabelRule answer = new NeutronMeteringLabelRule();
        if (rule.getUuid() != null) {
            answer.setID(rule.getUuid().getValue());
        }
        if (rule.getTenantId() != null) {
            answer.setTenantID(rule.getTenantId());
        }
        if (rule.getMeteringLabelId() != null) {
            answer.setMeteringLabelRuleLabelID(rule.getMeteringLabelId().getValue());
        }
        if (rule.getDirection() != null) {
            answer.setMeteringLabelRuleDirection(DirectionMapper.getName(rule.getDirection()));
        }
        if (rule.getRemoteIpPrefix() != null) {
            answer.setMeteringLabelRuleRemoteIpPrefix(new String(rule.getRemoteIpPrefix().getValue()));
        }
        answer.setMeteringLabelRuleExcluded(rule.isExcluded());
        return answer;
    }
}
