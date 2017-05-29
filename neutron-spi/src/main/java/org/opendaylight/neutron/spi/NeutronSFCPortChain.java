/*
 * Copyright (c) 2016 Brocade Communications Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.neutron.spi;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public final class NeutronSFCPortChain extends NeutronBaseAttributes<NeutronSFCPortChain> implements Serializable {
    private static final Logger LOG = LoggerFactory.getLogger(NeutronSFCPortChain.class);
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

    /**
     * This method copies selected fields from the object and returns them
     * as a new object, suitable for marshaling.
     *
     * @param fields List of attributes to be extracted
     * @return an OpenStack Neutron SFC Port Chain object with only the selected fields
     *             populated
     */

    public NeutronSFCPortChain extractFields(List<String> fields) {
        NeutronSFCPortChain ans = new NeutronSFCPortChain();
        for (String s : fields) {
            if (extractField(s, ans)) {
                continue;
            }
            switch (s) {
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
                    LOG.warn("{} is not a NeutronSFCPortChain suitable field.", s);
                    break;
            }
        }
        return ans;
    }

    @Override
    public String toString() {
        return "NeutronSFCPortChain[" + "tenantID='" + tenantID + '\'' + ", name='" + name + '\''
                + ", portPairGroupsUUID=" + portPairGroupsUUID + ", flowClassifiersUUID='" + flowClassifiersUUID + '\''
                + ", chainParameters=" + chainParameters + ']';
    }
}
