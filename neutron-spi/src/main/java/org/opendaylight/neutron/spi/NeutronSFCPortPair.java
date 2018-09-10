/*
 * Copyright (c) 2016 Brocade Communications Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.neutron.spi;

import java.util.HashMap;
import java.util.Map;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public final class NeutronSFCPortPair extends NeutronBaseAttributes<NeutronSFCPortPair> {
    private static final long serialVersionUID = 1L;

    // See OpenStack Networking SFC (networking-sfc) Port Pair API v1.0 Reference
    // for description of annotated attributes
    @XmlElement(name = "ingress")
    String ingressPortUUID;

    @XmlElement(name = "egress")
    String egressPortUUID;

    @XmlElement(name = "service_function_parameters")
    @XmlJavaTypeAdapter(NeutronResourceMapPropertyAdapter.class)
    Map<String, String> serviceFunctionParameters;

    public NeutronSFCPortPair() {
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
    protected boolean extractField(String field, NeutronSFCPortPair ans) {
        switch (field) {
            case "ingress":
                ans.setIngressPortUUID(this.getIngressPortUUID());
                break;
            case "egress":
                ans.setEgressPortUUID(this.getEgressPortUUID());
                break;
            case "service_function_parameters":
                ans.setServiceFunctionParameters(new HashMap<>(this.getServiceFunctionParameters()));
                break;
            default:
                return super.extractField(field, ans);
        }
        return true;
    }

    @Override
    public String toString() {
        return "NeutronSFCPortPair[" + "tenantID='" + getTenantID() + '\'' + ", name='" + name + '\''
                + ", ingressPortUUID='" + ingressPortUUID + '\'' + ", egressPortUUID='" + egressPortUUID + '\''
                + ", serviceFunctionParameters=" + serviceFunctionParameters + ']';
    }
}
