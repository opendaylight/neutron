/*
 * Copyright (c) 2017 Ericsson India Global Services Pvt Ltd. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.spi;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public final class NeutronTrunkSubPort implements Serializable {
    private static final long serialVersionUID = 1L;

    @XmlElement(name = "port_id")
    String portId;

    @XmlElement(name = "segmentation_type")
    String segmentationType;

    @XmlElement(name = "segmentation_id")
    String segmentationId;

    public NeutronTrunkSubPort() {
    }

    public String getPortId() {
        return portId;
    }

    public void setPortId(String portId) {
        this.portId = portId;
    }

    public String getSegmentationType() {
        return segmentationType;
    }

    public void setSegmentationType(String segmentationType) {
        this.segmentationType = segmentationType;
    }

    public String getSegmentationId() {
        return segmentationId;
    }

    public void setSegmentationId(String segmentationId) {
        this.segmentationId = segmentationId;
    }

    @Override
    public String toString() {
        return "subPorts{" + "portId='" + portId + '\'' + ", segmentationType='" + segmentationType + '\''
                + ", segmentationId='" + segmentationId + '\'' + '}';
    }
}
