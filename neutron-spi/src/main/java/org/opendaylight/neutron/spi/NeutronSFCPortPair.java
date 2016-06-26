/*
 * Copyright (c) 2016 Brocade Communications Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.neutron.spi;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.yang.types.rev130715.Uuid;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)

public class NeutronSFCPortPair extends Neutron_ID implements Serializable, INeutronObject {

    private static final long serialVersionUID = 1L;

    // See OpenStack Networking SFC (networking-sfc) Port Pair API v1.0 Reference
    // for description of annotated attributes
    @XmlElement(name = "tenant_id")
    String tenantID;

    @XmlElement(name = "name")
    String name;

    @XmlElement(name = "ingress")
    String ingressPortUUID;

    @XmlElement(name = "egress")
    String egressPortUUID;

    @XmlElement(name = "service_function_parameters")
    @XmlJavaTypeAdapter(NeutronPort_VIFAdapter.class)
    Map<String, String> serviceFunctionParameters;

    public NeutronSFCPortPair() {
    }

    @Override
    public String getTenantID() {
        return tenantID;
    }

    @Override
    public void setTenantID(String tenantID) {
        this.tenantID = tenantID;
    }

    @Override @JsonIgnore
    public void setTenantID(Uuid tenantID) {
        this.tenantID = tenantID.getValue().replace("-", "");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIngressPortUUID() {
        return ingressPortUUID;
    }

    public void setIngressPortUUID(String ingressPortUUID) {
        this.ingressPortUUID = ingressPortUUID;
    }

    public String getEgressPortUUID() {
        return egressPortUUID;
    }

    public void setEgressPortUUID(String egressPortUUID) {
        this.egressPortUUID = egressPortUUID;
    }

    public Map<String, String> getServiceFunctionParameters() {
        return serviceFunctionParameters;
    }

    public void setServiceFunctionParameters(Map<String, String> serviceFunctionParameters) {
        this.serviceFunctionParameters = serviceFunctionParameters;
    }

    @Override
    public void initDefaults() {
    }

    /**
     * This method copies selected fields from the object and returns them
     * as a new object, suitable for marshaling.
     *
     * @param fields List of attributes to be extracted
     * @return an OpenStack Neutron SFC Port Pair object with only the selected fields
     * populated
     */

    public NeutronSFCPortPair extractFields(List<String> fields) {
        NeutronSFCPortPair ans = new NeutronSFCPortPair();
        Iterator<String> i = fields.iterator();
        while (i.hasNext()) {
            String s = i.next();
            if (s.equals("id")) {
                ans.setID(this.getID());
            }
            if (s.equals("tenant_id")) {
                ans.setTenantID(this.getTenantID());
            }
            if (s.equals("name")) {
                ans.setName(this.getName());
            }
            if (s.equals("ingress")) {
                ans.setIngressPortUUID(this.getIngressPortUUID());
            }
            if (s.equals("egress")) {
                ans.setEgressPortUUID(this.getEgressPortUUID());
            }
            if (s.equals("service_function_parameters")) {
                ans.setServiceFunctionParameters(new HashMap<String, String>(this.getServiceFunctionParameters()));
            }
        }
        return ans;
    }


    @Override
    public String toString() {
        return "NeutronSFCPortPair[" +
                "tenantID='" + tenantID + '\'' +
                ", name='" + name + '\'' +
                ", ingressPortUUID='" + ingressPortUUID + '\'' +
                ", egressPortUUID='" + egressPortUUID + '\'' +
                ", serviceFunctionParameters=" + serviceFunctionParameters +
                ']';
    }
}
