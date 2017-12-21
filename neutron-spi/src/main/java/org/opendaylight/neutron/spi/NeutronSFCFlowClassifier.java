/*
 * Copyright (c) 2016 Brocade Communications Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.neutron.spi;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public final class NeutronSFCFlowClassifier extends NeutronBaseAttributes<NeutronSFCFlowClassifier>
        implements Serializable {
    private static final long serialVersionUID = 1L;

    // See OpenStack Networking SFC (networking-sfc) API v1.0 Reference for description of
    // annotated attributes
    @XmlElement(defaultValue = "IPv4", name = "ethertype")
    String ethertype;

    @XmlElement(name = "protocol")
    String protocol;

    @XmlElement(name = "source_port_range_min")
    Integer sourcePortRangeMin;

    @XmlElement(name = "source_port_range_max")
    Integer sourcePortRangeMax;

    @XmlElement(name = "destination_port_range_min")
    Integer destinationPortRangeMin;

    @XmlElement(name = "destination_port_range_max")
    Integer destinationPortRangeMax;

    @XmlElement(name = "source_ip_prefix")
    String sourceIpPrefix;

    @XmlElement(name = "destination_ip_prefix")
    String destinationIpPrefix;

    @XmlElement(name = "logical_source_port")
    String logicalSourcePortUUID;

    @XmlElement(name = "logical_destination_port")
    String logicalDestinationPortUUID;

    @XmlElement(name = "l7_parameters")
    @XmlJavaTypeAdapter(NeutronResourceMapPropertyAdapter.class)
    Map<String, String> l7Parameters;

    public NeutronSFCFlowClassifier() {
    }

    public String getEthertype() {
        return ethertype;
    }

    public void setEthertype(String ethertype) {
        this.ethertype = ethertype;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public Integer getSourcePortRangeMin() {
        return sourcePortRangeMin;
    }

    public void setSourcePortRangeMin(Integer sourcePortRangeMin) {
        this.sourcePortRangeMin = sourcePortRangeMin;
    }

    public Integer getSourcePortRangeMax() {
        return sourcePortRangeMax;
    }

    public void setSourcePortRangeMax(Integer sourcePortRangeMax) {
        this.sourcePortRangeMax = sourcePortRangeMax;
    }

    public Integer getDestinationPortRangeMin() {
        return destinationPortRangeMin;
    }

    public void setDestinationPortRangeMin(Integer destinationPortRangeMin) {
        this.destinationPortRangeMin = destinationPortRangeMin;
    }

    public Integer getDestinationPortRangeMax() {
        return destinationPortRangeMax;
    }

    public void setDestinationPortRangeMax(Integer destinationPortRangeMax) {
        this.destinationPortRangeMax = destinationPortRangeMax;
    }

    public String getSourceIpPrefix() {
        return sourceIpPrefix;
    }

    public void setSourceIpPrefix(String sourceIpPrefix) {
        this.sourceIpPrefix = sourceIpPrefix;
    }

    public String getLogicalSourcePortUUID() {
        return logicalSourcePortUUID;
    }

    public void setLogicalSourcePortUUID(String logicalSourcePortUUID) {
        this.logicalSourcePortUUID = logicalSourcePortUUID;
    }

    public String getDestinationIpPrefix() {
        return destinationIpPrefix;
    }

    public void setDestinationIpPrefix(String destinationIpPrefix) {
        this.destinationIpPrefix = destinationIpPrefix;
    }

    public String getLogicalDestinationPortUUID() {
        return logicalDestinationPortUUID;
    }

    public void setLogicalDestinationPortUUID(String logicalDestinationPortUUID) {
        this.logicalDestinationPortUUID = logicalDestinationPortUUID;
    }

    public Map<String, String> getL7Parameters() {
        return l7Parameters;
    }

    public void setL7Parameters(Map<String, String> l7Parameters) {
        this.l7Parameters = l7Parameters;
    }

    @Override
    protected boolean extractField(String field, NeutronSFCFlowClassifier ans) {
        switch (field) {
            case "ethertype":
                ans.setEthertype(this.getEthertype());
                break;
            case "source_port_range_min":
                ans.setSourcePortRangeMin(this.getSourcePortRangeMin());
                break;
            case "source_port_range_max":
                ans.setSourcePortRangeMax(this.getSourcePortRangeMax());
                break;
            case "destination_port_range_min":
                ans.setDestinationPortRangeMin(this.getDestinationPortRangeMin());
                break;
            case "destination_port_range_max":
                ans.setDestinationPortRangeMax(this.getDestinationPortRangeMax());
                break;
            case "source_ip_prefix":
                ans.setSourceIpPrefix(this.getSourceIpPrefix());
                break;
            case "destination_ip_prefix":
                ans.setDestinationIpPrefix(this.getDestinationIpPrefix());
                break;
            case "logical_source_port":
                ans.setLogicalSourcePortUUID(this.getLogicalSourcePortUUID());
                break;
            case "logical_destination_port":
                ans.setLogicalDestinationPortUUID(this.getLogicalDestinationPortUUID());
                break;
            case "l7_parameters":
                ans.setL7Parameters(new HashMap<>(this.getL7Parameters()));
                break;
            default:
                return super.extractField(field, ans);
        }
        return true;
    }

    @Override
    public String toString() {
        return "NeutronSFCFlowClassifier[" + "name='" + name + '\'' + ", ethertype='" + ethertype + '\''
                + ", protocol='" + protocol + '\'' + ", sourcePortRangeMin=" + sourcePortRangeMin
                + ", sourcePortRangeMax=" + sourcePortRangeMax + ", destinationPortRangeMin=" + destinationPortRangeMin
                + ", destinationPortRangeMax=" + destinationPortRangeMax + ", sourceIpPrefix='" + sourceIpPrefix + '\''
                + ", destinationIpPrefix='" + destinationIpPrefix + '\'' + ", logicalSourcePortUUID='"
                + logicalSourcePortUUID + '\'' + ", logicalDestinationPortUUID='" + logicalDestinationPortUUID + '\''
                + ", l7Parameters=" + l7Parameters + ']';
    }
}
