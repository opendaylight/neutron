/*
 * Copyright (c) 2016 Brocade Communications Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.neutron.spi;

import java.util.List;
import java.util.Map;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public final class NeutronSFCPortChain extends NeutronBaseAttributes<NeutronSFCPortChain> {
    private static final long serialVersionUID = 1L;

    // See OpenStack Networking SFC (networking-sfc) Port Chain API v1.0 Reference
    // for description of annotated attributes

    @XmlElement(name = "port_pair_groups")
    List<String> portPairGroupsUUID;

    @XmlElement(name = "flow_classifiers")
    List<String> flowClassifiersUUID;

    @XmlElement(name = "chain_parameters")
    @XmlJavaTypeAdapter(NeutronResourceMapPropertyAdapter.class)
    Map<String, String> chainParameters;

    public NeutronSFCPortChain() {
    }

    public List<String> getPortPairGroupsUUID() {
        return portPairGroupsUUID;
    }

    public void setPortPairGroupsUUID(List<String> portPairGroupsUUID) {
        this.portPairGroupsUUID = portPairGroupsUUID;
    }

    public List<String> getFlowClassifiersUUID() {
        return flowClassifiersUUID;
    }

    public void setFlowClassifiersUUID(List<String> flowClassifiersUUID) {
        this.flowClassifiersUUID = flowClassifiersUUID;
    }

    public Map<String, String> getChainParameters() {
        return chainParameters;
    }

    public void setChainParameters(Map<String, String> chainParameters) {
        this.chainParameters = chainParameters;
    }

    @Override
    protected boolean extractField(String field, NeutronSFCPortChain ans) {
        switch (field) {
            case "port_pair_groups":
                ans.setPortPairGroupsUUID(this.getPortPairGroupsUUID());
                break;
            case "flow_classifiers":
                ans.setFlowClassifiersUUID(this.getFlowClassifiersUUID());
                break;
            case "chain_parameters":
                ans.setChainParameters(this.getChainParameters());
                break;
            default:
                return super.extractField(field, ans);
        }
        return true;
    }

    @Override
    public String toString() {
        return "NeutronSFCPortChain[" + "tenantID='" + tenantID + '\'' + ", name='" + name + '\''
                + ", portPairGroupsUUID=" + portPairGroupsUUID + ", flowClassifiersUUID='" + flowClassifiersUUID + '\''
                + ", chainParameters=" + chainParameters + ']';
    }
}
