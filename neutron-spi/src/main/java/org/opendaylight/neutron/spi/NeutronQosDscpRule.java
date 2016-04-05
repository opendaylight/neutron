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

public class NeutronQosDscpRule extends NeutronObject implements Serializable, INeutronObject {
    private static final long serialVersionUID = 1L;

    @XmlElement(name = "dscp_mark")
    int qosDscpmark;

    @XmlElement(name = "id")
    String qosPolicyID;

    public int getqosDscpmark() { return qosDscpmark ;}

    public void setqosDscpmark(int qosDscpmark) {this.qosDscpmark = qosDscpmark ;}

    public String getqosPolicyID() {return qosPolicyID ;}

    public void setqosPolicyID(String qosPolicyID) {this.qosPolicyID = qosPolicyID ;}

    public NeutronQosDscpRule extractFields(List<String> fields) {
        NeutronQosDscpRule ans = new NeutronQosDscpRule();
        Iterator<String> i = fields.iterator();
        while (i.hasNext()) {
            String s = i.next();
            if (s.equals("id")) {
                ans.setID(this.getID());
            }
            if(s.equals("dscp_mark")) {
                ans.setqosDscpmark(this.getqosDscpmark());
            }
            if (s.equals("id")) {
                ans.setqosPolicyID(this.getqosPolicyID());
            }
        }
        return ans;
    }

    @Override
    public String toString() {
        return "qosPolicyRules{" +
                "qosPolicyUUID='" + uuid + '\'' +
                "qosDscpmark='" + qosDscpmark +
                '}';
    }

    @Override
    public void initDefaults(){
    }
}
