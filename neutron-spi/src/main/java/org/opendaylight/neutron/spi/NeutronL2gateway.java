/*
 * Copyright (c) 2015 Hewlett-Packard Development Company, L.P. and others.  All rights reserved.
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
public class NeutronL2gateway extends NeutronObject implements Serializable, INeutronObject{
    private static final long serialVersionUID = 1L;

    @XmlElement(name = "name")
    String l2gatewayName;

    @XmlElement(name = "devices")
    List<NeutronL2gwDevice> neutronL2gwDevices;

    public String getL2gatewayName() {
        return l2gatewayName;
    }

    public void setL2gatewayName(String l2gatewayName) {
        this.l2gatewayName = l2gatewayName;
    }

    public List<NeutronL2gwDevice> getNeutronL2gwDevices() {
        return neutronL2gwDevices;
    }

    public void setNeutronL2gwDevices(List<NeutronL2gwDevice> neutronL2gwDevices) {
        this.neutronL2gwDevices = neutronL2gwDevices;
    }

    public NeutronL2gateway extractFields(List<String> fields) {
        NeutronL2gateway ans = new NeutronL2gateway();
        Iterator<String> i = fields.iterator();
        while (i.hasNext()) {
            String s = i.next();
            if (s.equals("id")) {
                ans.setID(this.getID());
            }
            if (s.equals("name")) {
                ans.setL2gatewayName(this.getL2gatewayName());
            }
            if (s.equals("tenant_id")) {
                ans.setTenantID(this.getTenantID());
            }
            if (s.equals("devices")) {
                List<NeutronL2gwDevice> devices = new ArrayList<NeutronL2gwDevice>();
                devices.addAll(this.getNeutronL2gwDevices());
                ans.setNeutronL2gwDevices(devices);
            }
        }
        return ans;
    }

    @Override
    public String toString() {
        return "NeutronL2Gateway [" +
                "id = " + uuid +
                ", name = " + l2gatewayName +
                ", tenant_id = " + tenantID +
                ", devices = " + neutronL2gwDevices +
                "]";
    }

}