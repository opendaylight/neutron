/*
 * Copyright (c) 2015 Hewlett-Packard Development Company, L.P. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.spi;

import java.util.List;
import javax.xml.bind.annotation.XmlElement;

public class NeutronL2gwDeviceInterface {
    @XmlElement(name = "name")
    List<String> interfaceNames;

    @XmlElement(name = "segmentation-id")
    List<Integer> segmentationId;

    public List<String> getInterfaceNames() {
        return interfaceNames;
    }

    public void setInterfaceNames(List<String> interfaceNames) {
        this.interfaceNames = interfaceNames;
    }

    public List<Integer> getSegmentationId() {
        return segmentationId;
    }

    public void setSegmentationId(List<Integer> segmentationId) {
        this.segmentationId = segmentationId;
    }

}
