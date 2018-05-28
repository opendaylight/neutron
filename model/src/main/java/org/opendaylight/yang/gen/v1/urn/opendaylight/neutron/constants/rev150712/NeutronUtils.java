/*
 * Copyright (c) 2016 Intel corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.constants.rev150712;

import com.google.common.collect.ImmutableBiMap;


public final class NeutronUtils {
    private NeutronUtils() {
        throw new UnsupportedOperationException("Utility class should not be instantiated.");
    }

    // For FWaaS
    public static class FwProtocolMapper {
        private FwProtocolMapper() {
            throw new UnsupportedOperationException("Utility class should not be instantiated.");
        }

        private static final ImmutableBiMap<String, Class<? extends FwProtocolBase>> MAPPER
            = new ImmutableBiMap.Builder<String, Class<? extends FwProtocolBase>>()
            .put("tcp", FwProtocolTcp.class)
            .put("udp", FwProtocolUdp.class)
            .put("icmp", FwProtocolIcmp.class)
            .build();

        public static Class<? extends FwProtocolBase> get(String key) {
            return MAPPER.get(key);
        }

        public static String getName(Class<? extends FwProtocolBase> key) {
            ImmutableBiMap<Class<? extends FwProtocolBase>, String> inverseMapper = MAPPER.inverse();
            return inverseMapper.get(key);
        }
    }

    // For security group
    public static class ProtocolMapper {
        private ProtocolMapper() {
            throw new UnsupportedOperationException("Utility class should not be instantiated.");
        }

        private static final ImmutableBiMap<String, Class<? extends ProtocolBase>> MAPPER
            = new ImmutableBiMap.Builder<String, Class<? extends ProtocolBase>>()
            .put("icmp", ProtocolIcmp.class)
            .put("tcp", ProtocolTcp.class)
            .put("udp", ProtocolUdp.class)
            .put("icmpv6", ProtocolIcmpV6.class)
            .put("igmp", ProtocolIgmp.class)
            .build();

        public static Class<? extends ProtocolBase> get(String key) {
            return MAPPER.get(key);
        }

        public static String getName(Class<? extends ProtocolBase> key) {
            ImmutableBiMap<Class<? extends ProtocolBase>, String> inverseMapper = MAPPER.inverse();
            return inverseMapper.get(key);
        }
    }

    // Direction of the Traffic
    public static class DirectionMapper {
        private DirectionMapper() {
            throw new UnsupportedOperationException("Utility class should not be instantiated.");
        }

        private static final ImmutableBiMap<String, Class<? extends DirectionBase>> MAPPER
            = new ImmutableBiMap.Builder<String, Class<? extends DirectionBase>>()
            .put("egress", DirectionEgress.class)
            .put("ingress", DirectionIngress.class)
            .build();

        public static Class<? extends DirectionBase> get(String key) {
            return MAPPER.get(key);
        }

        public static String getName(Class<? extends DirectionBase> key) {
            ImmutableBiMap<Class<? extends DirectionBase>, String> inverseMapper = MAPPER.inverse();
            return inverseMapper.get(key);
        }

        private static final ImmutableBiMap<String, Class<? extends DirectionMinimumBandwidthRule>>
            MINIMUMBANDWIDTHRULE_MAPPER = new ImmutableBiMap.Builder<String, Class<? extends
            DirectionMinimumBandwidthRule>>()
            .put("egress", DirectionMinimumBandwidthRule.class)
            .build();

        public static Class<? extends DirectionMinimumBandwidthRule>
            getMinimumBandwidthRuleDirection(String minimumBandwidthKey) {
            return MINIMUMBANDWIDTHRULE_MAPPER.get(minimumBandwidthKey);
        }

        public static String getMinimumBandwidthRuleDirectionString(Class<? extends DirectionMinimumBandwidthRule>
            minimumBandwidthKey) {
            ImmutableBiMap<Class<? extends DirectionMinimumBandwidthRule>, String>
            inverseMinimBandwidthRuleDirectionMapper = MINIMUMBANDWIDTHRULE_MAPPER.inverse();
            return inverseMinimBandwidthRuleDirectionMapper.get(minimumBandwidthKey);
        }
        //

        private static final ImmutableBiMap<String, Class<? extends DirectionBandwidthLimitRule>>
                BANDWIDTHLIMITRULE_MAPPER = new ImmutableBiMap.Builder<String, Class<? extends
                DirectionBandwidthLimitRule>>()
                .put("egress", DirectionBandwidthLimitRule.class)
                .build();

        public static Class<? extends DirectionBandwidthLimitRule>
        getBandwidthLimitRuleDirection(String bandwidthLimitKey) {
            return BANDWIDTHLIMITRULE_MAPPER.get(bandwidthLimitKey);
        }

        public static String getBandwidthLimitRuleDirectionString(Class<? extends DirectionBandwidthLimitRule>
                                                                          bandwidthLimitKey) {
            ImmutableBiMap<Class<? extends DirectionBandwidthLimitRule>, String>
                    inverseBandwidthLimitRuleDirectionMapper = BANDWIDTHLIMITRULE_MAPPER.inverse();
            return inverseBandwidthLimitRuleDirectionMapper.get(bandwidthLimitKey);
        }
    }
}
