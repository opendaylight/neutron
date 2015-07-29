/*
 * Copyright (c) 2015 Hewlett-Packard Development Company and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.spi;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "l2gateway")
public class NeutronL2Gateway implements Serializable, INeutronObject{
    private static final long serialVersionUID = 1L;

    @XmlElement (name = "id")
    String l2gatewayUUID;

    @XmlElement(name = "name")
    String gatewayName;

    @XmlElement(name = "tenant_id")
    String tenantUUID;

    @XmlElement(name = "devices")
    List<NeutronDevice> neutronDevices;

    @Override
    public String getID() {
        return l2gatewayUUID;
    }

    @Override
    public void setID(String id) {
        this.l2gatewayUUID=id;
    }

    public String getL2gatewayUUID() {
        return l2gatewayUUID;
    }

    public void setL2gatewayUUID(String l2gatewayUUID) {
        this.l2gatewayUUID = l2gatewayUUID;
    }

    public String getGatewayName() {
        return gatewayName;
    }

    public void setGatewayName(String gatewayName) {
        this.gatewayName = gatewayName;
    }

    public List<NeutronDevice> getNeutronDevices() {
        return neutronDevices;
    }

    public void setNeutronDevices(List<NeutronDevice> neutronDevices) {
        this.neutronDevices = neutronDevices;
    }

    public String getTenantUUID() {
        return tenantUUID;
    }

    public void setTenantUUID(String tenantUUID) {
        this.tenantUUID = tenantUUID;
    }

    public NeutronL2Gateway extractFields(List<String> fields) {
        NeutronL2Gateway ans = new NeutronL2Gateway();
        Iterator<String> i = fields.iterator();
        while (i.hasNext()) {
            String s = i.next();
            if (s.equals("id")) {
                ans.setL2gatewayUUID(this.getL2gatewayUUID());
            }
            if (s.equals("name")) {
                ans.setGatewayName(this.getGatewayName());
            }
            if (s.equals("tenant_id")) {
                ans.setTenantUUID(this.getTenantUUID());
            }
            if (s.equals("devices")) {
                List<NeutronDevice> devices = new ArrayList<NeutronDevice>();
                devices.addAll(this.getNeutronDevices());
                ans.setNeutronDevices(devices);
            }
        }
        return ans;
    }

    @Override
    public String toString() {
        return "NeutronL2Gateway [" +
                "id=" + l2gatewayUUID +
                ", name=" + gatewayName +
                ", tenant_id=" + tenantUUID +
                ", devices=" + neutronDevices +
                "]";
    }

}