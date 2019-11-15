/*
 * Copyright (c) 2019 Ericsson India Global Services Pvt Ltd. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.neutron.utils;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class NeutronUtils {

    private static final Logger LOG = LoggerFactory.getLogger(NeutronUtils.class);

    enum IdManagerShards {
        DefaultConfig("default:config"),
        DefaultOper("default:oper"),
        InventoryConfig("inventory:config"),
        InventoryOper("inventory:oper"),
        TopologyConfig("topology:config"),
        TopologyOper("topology:oper"),
        EntityOper("entity:oper");

        private String shardName;

        IdManagerShards(String shardName) {
            this.shardName = shardName;
        }

        public static List<String> getShardList() {
            List<String> list = new ArrayList<>();
            for (IdManagerShards val : IdManagerShards.values()) {
                list.add(val.shardName);
            }
            return list;
        }
    }

    private NeutronUtils() {
    }

    public static void waitForAllShards() {
        ShardStatusMonitor shardStatusMonitor = new ShardStatusMonitor();
        boolean isDatastoreAvailable = false;
        int retryCount = 0;
        try {
            while (retryCount < 1000) {
                isDatastoreAvailable = shardStatusMonitor.getShardStatus(IdManagerShards.getShardList());
                if (isDatastoreAvailable) {
                    break;
                }
                LOG.error("IdManager: retrying shard status check for the attempt {}", ++retryCount);
                Thread.sleep(2000);
            }
            if (isDatastoreAvailable) {
                LOG.info("IDManager is UP");
            }
        } catch (InterruptedException e) {
            LOG.error("IDManager is DOWN, shard status check failed");
        }

        if (!isDatastoreAvailable) {
            LOG.error("IDManager is DOWN, as shards were not available at bundle bringup");
        }
    }
}