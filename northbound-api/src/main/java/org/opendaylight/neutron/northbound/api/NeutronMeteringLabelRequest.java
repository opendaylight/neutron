/*
 * Copyright IBM Corporation, 2013.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.northbound.api;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.opendaylight.neutron.spi.NeutronMeteringLabel;


@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)

public class NeutronMeteringLabelRequest {
    // See OpenStack Network API v2.0 Reference for description of
    // annotated attributes

    @XmlElement(name="metering_label")
    NeutronMeteringLabel singletonMeteringLabel;

    @XmlElement(name="metering_labels")
    List<NeutronMeteringLabel> bulkMeteringLabels;

    NeutronMeteringLabelRequest() {
    }

    NeutronMeteringLabelRequest(NeutronMeteringLabel label) {
        singletonMeteringLabel = label;
        bulkMeteringLabels = null;
    }

    NeutronMeteringLabelRequest(List<NeutronMeteringLabel> bulk) {
        bulkMeteringLabels = bulk;
        singletonMeteringLabel = null;
    }

    public NeutronMeteringLabel getSingleton() {
        return singletonMeteringLabel;
    }

    public boolean isSingleton() {
        return (singletonMeteringLabel != null);
    }

    public List<NeutronMeteringLabel> getBulk() {
        return bulkMeteringLabels;
    }
}
