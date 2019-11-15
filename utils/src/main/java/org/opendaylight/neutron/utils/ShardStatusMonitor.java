/*
 * Copyright (c) 2019 Ericsson India Global Services Pvt Ltd. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.neutron.utils;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShardStatusMonitor {

    private static final Logger LOG = LoggerFactory.getLogger(ShardStatusMonitor.class);

    private static String JMX_OBJECT_NAME_LIST_OF_CONFIG_SHARDS =
            "org.opendaylight.controller:type=DistributedConfigDatastore,"
                    + "Category=ShardManager,name=shard-manager-config";
    private static String JMX_OBJECT_NAME_LIST_OF_OPER_SHARDS =
            "org.opendaylight.controller:type=DistributedOperationalDatastore,"
                    + "Category=ShardManager,name=shard-manager-operational";

    public ShardStatusMonitor() {
        // Do nothing
    }

    public boolean getShardStatus(List<String> shards) {
        boolean status = true;
        for (String shard : shards) {
            String[] params = shard.split(":");
            if (!getDataStoreStatus(params[0], params[1]).equalsIgnoreCase("operational")) {
                status = false;
                break;
            }
        }
        return status;
    }

    @SuppressWarnings("IllegalCatch")
    private static String getDataStoreStatus(String name, String type) {
        boolean statusResult = true;
        try {
            ArrayList listOfShards;
            if (type.equalsIgnoreCase("config")) {
                listOfShards = getAttributeJMXCommand(JMX_OBJECT_NAME_LIST_OF_CONFIG_SHARDS, "LocalShards");
            } else {
                listOfShards = getAttributeJMXCommand(JMX_OBJECT_NAME_LIST_OF_OPER_SHARDS, "LocalShards");
            }
            if (listOfShards != null) {
                for (int i = 0; i < listOfShards.size(); i++) {
                    LOG.info("Shard: {} ", listOfShards.get(i));
                    if (listOfShards.get(i).toString().contains(name)) {
                        String jmxObjectShardStatus;
                        if (type.equalsIgnoreCase("config")) {
                            jmxObjectShardStatus = "org.opendaylight.controller:Category=Shards,name="
                                    + listOfShards.get(i) + ",type=DistributedConfigDatastore";
                        } else {
                            jmxObjectShardStatus = "org.opendaylight.controller:Category=Shards,name="
                                    + listOfShards.get(i) + ",type=DistributedOperationalDatastore";
                        }
                        LOG.info(jmxObjectShardStatus);
                        String leader = getLeaderJMX(jmxObjectShardStatus,"Leader");
                        if (leader != null && leader.length() > 1) {
                            if (type.equalsIgnoreCase("config")) {
                                LOG.info("{} ::Config DS has the Leader as:: {}", listOfShards.get(i), leader);
                            } else {
                                LOG.info("{} ::Oper DS has the Leader as:: {}", listOfShards.get(i), leader);
                            }
                        } else {
                            statusResult = false;
                        }
                    }
                }
            }
        } catch (Exception e) {
            LOG.error("ERROR::", e);
            statusResult = false;
        }
        return (statusResult ?  "OPER" : "ERROR");
    }

    @SuppressWarnings("IllegalCatch")
    private static ArrayList getAttributeJMXCommand(String objectName, String attributeName) {
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        ArrayList listOfShards = new ArrayList();
        if (mbs != null) {
            try {
                listOfShards = (ArrayList) mbs.getAttribute(new ObjectName(objectName), attributeName);
            } catch (MalformedObjectNameException monEx) {
                LOG.error("CRITICAL EXCEPTION : Malformed Object Name Exception");
            } catch (MBeanException mbEx) {
                LOG.error("CRITICAL EXCEPTION : MBean Exception");
            } catch (InstanceNotFoundException infEx) {
                LOG.error("CRITICAL EXCEPTION : Instance Not Found Exception");
            } catch (ReflectionException rEx) {
                LOG.error("CRITICAL EXCEPTION : Reflection Exception");
            } catch (Exception e) {
                LOG.error("Attribute not found");
            }
        }
        return listOfShards;
    }

    @SuppressWarnings("IllegalCatch")
    private static String getLeaderJMX(String objectName, String atrName) {
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        String leader = "";
        if (mbs != null) {
            try {
                leader  = (String) mbs.getAttribute(new ObjectName(objectName), atrName);
            } catch (MalformedObjectNameException monEx) {
                LOG.error("CRITICAL EXCEPTION : Malformed Object Name Exception");
            } catch (MBeanException mbEx) {
                LOG.error("CRITICAL EXCEPTION : MBean Exception");
            } catch (InstanceNotFoundException infEx) {
                LOG.error("CRITICAL EXCEPTION : Instance Not Found Exception");
            } catch (ReflectionException rEx) {
                LOG.error("CRITICAL EXCEPTION : Reflection Exception");
            } catch (Exception e) {
                LOG.error("Attribute not found");
            }
        }
        return leader;
    }
}