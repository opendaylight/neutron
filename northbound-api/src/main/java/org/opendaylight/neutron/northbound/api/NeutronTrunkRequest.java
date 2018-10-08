/*
 * Copyright (c) 2017 Ericsson India Global Services Pvt Ltd. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.northbound.api;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.opendaylight.neutron.spi.NeutronTrunk;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
@SuppressFBWarnings("URF_UNREAD_FIELD")
public final class NeutronTrunkRequest extends NeutronRequest<NeutronTrunk> {

    @XmlElement(name = "trunk")
    NeutronTrunk singleton;

    @XmlElement(name = "trunks")
    List<NeutronTrunk> bulkRequest;

    NeutronTrunkRequest() {
    }

    NeutronTrunkRequest(NeutronTrunk trunk) {
        singleton = trunk;
    }

    NeutronTrunkRequest(List<NeutronTrunk> bulk) {
        bulkRequest = bulk;
    }
}
