/*
 * Copyright (c) 2015 IBM Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.transcriber;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.opendaylight.controller.sal.binding.api.BindingAwareBroker.ProviderContext;
import org.opendaylight.neutron.spi.INeutronMeteringLabelRuleCRUD;
import org.opendaylight.neutron.spi.NeutronMeteringLabelRule;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.constants.rev160807.DirectionBase;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.constants.rev160807.DirectionEgress;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.constants.rev160807.DirectionIngress;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.metering.rev141002.metering.rules.attributes.MeteringRules;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.metering.rev141002.metering.rules.attributes.metering.rules.MeteringRule;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.metering.rev141002.metering.rules.attributes.metering.rules.MeteringRuleBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.rev150325.Neutron;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableBiMap;

public class NeutronMeteringLabelRuleInterface extends AbstractNeutronInterface<MeteringRule, NeutronMeteringLabelRule>
        implements INeutronMeteringLabelRuleCRUD {
    private static final Logger LOGGER = LoggerFactory.getLogger(NeutronMeteringLabelRuleInterface.class);

    private static final ImmutableBiMap<Class<? extends DirectionBase>,String> DIRECTION_MAP
            = new ImmutableBiMap.Builder<Class<? extends DirectionBase>,String>()
            .put(DirectionEgress.class,"egress")
            .put(DirectionIngress.class,"ingress")
            .build();


    NeutronMeteringLabelRuleInterface(ProviderContext providerContext) {
        super(providerContext);
    }

    // IfNBMeteringLabelRuleCRUD methods

    @Override
    public boolean neutronMeteringLabelRuleExists(String uuid) {
        return exists(uuid);
    }

    @Override
    public NeutronMeteringLabelRule getNeutronMeteringLabelRule(String uuid) {
        return get(uuid);
    }

    @Override
    public List<NeutronMeteringLabelRule> getAll() {
        Set<NeutronMeteringLabelRule> allMeteringLabelRules = new HashSet<NeutronMeteringLabelRule>();
        MeteringRules rules = readMd(createInstanceIdentifier());
        if (rules != null) {
            for (MeteringRule rule: rules.getMeteringRule()) {
                allMeteringLabelRules.add(fromMd(rule));
            }
        }
        LOGGER.debug("Exiting getAllMeteringLabelRules, Found {} OpenStackMeteringLabelRules",
                allMeteringLabelRules.size());
        List<NeutronMeteringLabelRule> ans = new ArrayList<NeutronMeteringLabelRule>();
        ans.addAll(allMeteringLabelRules);
        return ans;
    }

    @Override
    public List<NeutronMeteringLabelRule> getAllNeutronMeteringLabelRules() {
        return getAll();
    }

    @Override
    public boolean addNeutronMeteringLabelRule(NeutronMeteringLabelRule input) {
        return add(input);
    }

    @Override
    public boolean removeNeutronMeteringLabelRule(String uuid) {
        return remove(uuid);
    }

    @Override
    public boolean updateNeutronMeteringLabelRule(String uuid, NeutronMeteringLabelRule delta) {
        return update(uuid, delta);
    }

    @Override
    public boolean neutronMeteringLabelRuleInUse(String netUUID) {
        return !exists(netUUID);
    }

    @Override
    protected InstanceIdentifier<MeteringRule> createInstanceIdentifier(MeteringRule item) {
        return InstanceIdentifier.create(Neutron.class)
            .child(MeteringRules.class)
            .child(MeteringRule.class, item.getKey());
    }

    protected InstanceIdentifier<MeteringRules> createInstanceIdentifier() {
        return InstanceIdentifier.create(Neutron.class)
            .child(MeteringRules.class);
    }

    @Override
    protected MeteringRule toMd(NeutronMeteringLabelRule meteringLabelRule) {
        MeteringRuleBuilder meteringRuleBuilder = new MeteringRuleBuilder();
        if (meteringLabelRule.getID() != null) {
            meteringRuleBuilder.setId(toUuid(meteringLabelRule.getID()));
        }
        if (meteringLabelRule.getMeteringLabelRuleLabelID() != null) {
            meteringRuleBuilder.setMeteringLabelId(toUuid(meteringLabelRule.getMeteringLabelRuleLabelID()));
        }
        if (meteringLabelRule.getMeteringLabelRuleDirection() != null) {
            ImmutableBiMap<String, Class<? extends DirectionBase>> mapper =
                    DIRECTION_MAP.inverse();
            meteringRuleBuilder.setDirection((Class<? extends DirectionBase>) mapper.get(meteringLabelRule.getMeteringLabelRuleDirection()));
        }
        if (meteringLabelRule.getMeteringLabelRuleRemoteIPPrefix() != null) {
            meteringRuleBuilder.setRemoteIpPrefix(meteringLabelRule.getMeteringLabelRuleRemoteIPPrefix());
        }
        meteringRuleBuilder.setExcluded(meteringLabelRule.getMeteringLabelRuleExcluded());
        return meteringRuleBuilder.build();
    }

    protected NeutronMeteringLabelRule fromMd(MeteringRule rule) {
        NeutronMeteringLabelRule answer = new NeutronMeteringLabelRule();
        if (rule.getId() != null) {
            answer.setID(rule.getId().getValue());
        }
        if (rule.getMeteringLabelId() != null) {
            answer.setMeteringLabelRuleLabelID(rule.getMeteringLabelId().getValue());
        }
        if (rule.getDirection() != null) {
            answer.setMeteringLabelRuleDirection(
                DIRECTION_MAP.get(rule.getDirection()));
        }
        if (rule.getRemoteIpPrefix() != null) {
            answer.setMeteringLabelRuleRemoteIPPrefix(rule.getRemoteIpPrefix());
        }
        answer.setMeteringLabelRuleExcluded(rule.isExcluded());
        return answer;
    }

    @Override
    protected MeteringRule toMd(String uuid) {
        MeteringRuleBuilder meteringRuleBuilder = new MeteringRuleBuilder();
        meteringRuleBuilder.setId((toUuid(uuid)));
        return meteringRuleBuilder.build();
    }

    public static void registerNewInterface(BundleContext context,
                                            ProviderContext providerContext,
                                            List<ServiceRegistration<?>> registrations) {
        NeutronMeteringLabelRuleInterface neutronMeteringLabelRuleInterface = new NeutronMeteringLabelRuleInterface(providerContext);
        ServiceRegistration<INeutronMeteringLabelRuleCRUD> neutronMeteringLabelRuleInterfaceRegistration = context.registerService(INeutronMeteringLabelRuleCRUD.class, neutronMeteringLabelRuleInterface, null);
        if (neutronMeteringLabelRuleInterfaceRegistration != null) {
            registrations.add(neutronMeteringLabelRuleInterfaceRegistration);
        }
    }
}
