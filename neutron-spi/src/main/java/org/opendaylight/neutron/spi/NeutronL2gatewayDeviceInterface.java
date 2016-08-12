/*
 * Copyright (c) 2015 Hewlett-Packard Development Company, L.P. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.spi;

import java.io.Serializable;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;

public class NeutronL2gatewayDeviceInterface implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = "name")
    String interfaceName;

    @XmlElement(name = "segmentation_id")
    List<Integer> segmentationId;

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public List<Integer> getSegmentationId() {
        return segmentationId;
    }

    public void setSegmentationId(List<Integer> segmentationId) {
        this.segmentationId = segmentationId;
    }

    @Override
    public String toString() {
        return "NeutronL2gatewayDeviceInterface [interfaceNames=" + interfaceName + ", segmentationId=" + segmentationId
                + "]";
    }
}
