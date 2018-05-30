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

        private static ImmutableBiMap<Class<? extends DirectionBase>, String> MAPPER_INVERSE = MAPPER.inverse();
        public static String getDirectionString(Class<? extends DirectionBase> direction) {
            return MAPPER_INVERSE.get(direction);
        }
    }
}
