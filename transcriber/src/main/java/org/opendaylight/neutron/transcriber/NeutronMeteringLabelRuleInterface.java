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
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.opendaylight.controller.sal.binding.api.BindingAwareBroker.ProviderContext;
import org.opendaylight.neutron.spi.INeutronMeteringLabelRuleCRUD;
import org.opendaylight.neutron.spi.NeutronMeteringLabelRule;
import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.inet.types.rev100924.IpAddress;
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
    private ConcurrentMap<String, NeutronMeteringLabelRule> meteringLabelRuleDB = new ConcurrentHashMap<String, NeutronMeteringLabelRule>();

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
        return meteringLabelRuleDB.containsKey(uuid);
    }

    @Override
    public NeutronMeteringLabelRule getNeutronMeteringLabelRule(String uuid) {
        if (!neutronMeteringLabelRuleExists(uuid)) {
            return null;
        }
        return meteringLabelRuleDB.get(uuid);
    }

    @Override
    public List<NeutronMeteringLabelRule> getAllNeutronMeteringLabelRules() {
        Set<NeutronMeteringLabelRule> allMeteringLabelRules = new HashSet<NeutronMeteringLabelRule>();
        for (Entry<String, NeutronMeteringLabelRule> entry : meteringLabelRuleDB.entrySet()) {
            NeutronMeteringLabelRule meteringLabelRule = entry.getValue();
            allMeteringLabelRules.add(meteringLabelRule);
        }
        LOGGER.debug("Exiting getAllMeteringLabelRules, Found {} OpenStackMeteringLabelRules",
                allMeteringLabelRules.size());
        List<NeutronMeteringLabelRule> ans = new ArrayList<NeutronMeteringLabelRule>();
        ans.addAll(allMeteringLabelRules);
        return ans;
    }

    @Override
    public boolean addNeutronMeteringLabelRule(NeutronMeteringLabelRule input) {
        if (neutronMeteringLabelRuleExists(input.getMeteringLabelRuleUUID())) {
            return false;
        }
        meteringLabelRuleDB.putIfAbsent(input.getMeteringLabelRuleUUID(), input);
        // TODO: add code to find INeutronMeteringLabelRuleAware services and
        // call newtorkCreated on them
        return true;
    }

    @Override
    public boolean removeNeutronMeteringLabelRule(String uuid) {
        if (!neutronMeteringLabelRuleExists(uuid)) {
            return false;
        }
        meteringLabelRuleDB.remove(uuid);
        // TODO: add code to find INeutronMeteringLabelRuleAware services and
        // call newtorkDeleted on them
        return true;
    }

    @Override
    public boolean updateNeutronMeteringLabelRule(String uuid, NeutronMeteringLabelRule delta) {
        if (!neutronMeteringLabelRuleExists(uuid)) {
            return false;
        }
        NeutronMeteringLabelRule target = meteringLabelRuleDB.get(uuid);
        return overwrite(target, delta);
    }

    @Override
    public boolean neutronMeteringLabelRuleInUse(String netUUID) {
        if (!neutronMeteringLabelRuleExists(netUUID)) {
            return true;
        }
        return false;
    }

    @Override
    protected InstanceIdentifier<MeteringRule> createInstanceIdentifier(MeteringRule item) {
        return InstanceIdentifier.create(Neutron.class).child(MeteringRules.class).child(MeteringRule.class);

    }

    @Override
    protected MeteringRule toMd(NeutronMeteringLabelRule meteringLabelRule) {
        MeteringRuleBuilder meteringRuleBuilder = new MeteringRuleBuilder();
        if (meteringLabelRule.getMeteringLabelRuleLabelID() != null) {
            meteringRuleBuilder.setId((toUuid(meteringLabelRule.getMeteringLabelRuleLabelID())));
        }
        if (meteringLabelRule.getMeteringLabelRuleUUID() != null) {
            meteringRuleBuilder.setMeteringLabelId(toUuid(meteringLabelRule.getMeteringLabelRuleUUID()));
        }
        if (meteringLabelRule.getMeteringLabelRuleDirection() != null) {
            ImmutableBiMap<String, Class<? extends DirectionBase>> mapper =
                    DIRECTION_MAP.inverse();
            meteringRuleBuilder.setDirection((Class<? extends DirectionBase>) mapper.get(meteringLabelRule.getMeteringLabelRuleDirection()));
        }
        if (meteringLabelRule.getMeteringLabelRuleRemoteIPPrefix() != null) {
            IpAddress ipAddress = new IpAddress(meteringLabelRule.getMeteringLabelRuleRemoteIPPrefix().toCharArray());
            meteringRuleBuilder.setRemoteIpPrefix(ipAddress);
        }
        meteringRuleBuilder.setExcluded(meteringLabelRule.getMeteringLabelRuleExcluded());
        return meteringRuleBuilder.build();
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
        if(neutronMeteringLabelRuleInterfaceRegistration != null) {
            registrations.add(neutronMeteringLabelRuleInterfaceRegistration);
        }
    }
}
