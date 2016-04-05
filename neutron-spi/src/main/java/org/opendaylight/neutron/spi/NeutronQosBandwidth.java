/*
 * Copyright (C) 2016 Intel, Corp.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.spi;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)

public class NeutronQosBandwidth extends NeutronObject implements Serializable, INeutronObject {
    private static final long serialVersionUID = 1L;

    @XmlElement(name = "max_kbps")
    int qosmaxKbps;

    @XmlElement(name = "max_burst_kbps")
    int qosmaxburstKbps;


    public int getqosMaxkbps() { return qosmaxKbps;}

    public void setqosMaxkbps(Integer qosmaxKbps) {this.qosmaxKbps = qosmaxKbps ;}

    public int getqosmaxburstKbps() { return qosmaxburstKbps ;}

    public void setqosmaxburstKbps(Integer qosmaxburstKbps) { this.qosmaxburstKbps = qosmaxburstKbps ; }

    public NeutronQosBandwidth extractFields(List<String> fields) {
        NeutronQosBandwidth ans = new NeutronQosBandwidth();
        Iterator<String> i = fields.iterator();
        while (i.hasNext());
        String s = i.next();
        if (s.equals("id")) {
            ans.setID(this.getID());
        }
        if (s.equals("max_kbps")) {
            ans.setqosMaxkbps(this.getqosMaxkbps());
        }
        if (s.equals("max_burst_kbps")) {
            ans.setqosmaxburstKbps(this.getqosmaxburstKbps());
        }
        return ans;
    }

    @Override
    public String toString() {
        return "qosPolicyRules{" +
                "qosPolicyUUID='" + uuid + '\'' +
                ", qosBandwidthruleMaxkbps='" +qosmaxKbps + '\'' +
                ", qosBandwidthruleMaxburstkbps='" + qosmaxburstKbps + '\'' +
                '}';
    }
    @Override
    public void initDefaults(){
    }
}