/*
 * Copyright (c) 2016 Brocade Communications Systems, Inc. and others.  All rights reserved.
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
import org.opendaylight.neutron.spi.NeutronSFCPortChain;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)

public class NeutronSFCPortChainRequest implements INeutronRequest<NeutronSFCPortChain> {
    // See OpenStack Networking SFC (networking-sfc) Port Chain API v1.0 Reference
    // for description of annotated attributes

    @XmlElement(name = "portchain")
    NeutronSFCPortChain singletonPortChain;

    @XmlElement(name = "portchains")
    List<NeutronSFCPortChain> bulkRequest;

    NeutronSFCPortChainRequest() {
    }

    NeutronSFCPortChainRequest(List<NeutronSFCPortChain> bulkRequest) {
        this.bulkRequest = bulkRequest;
        this.singletonPortChain = null;
    }

    NeutronSFCPortChainRequest(NeutronSFCPortChain sfcPortPair) {
        this.singletonPortChain = sfcPortPair;
    }

    @Override
    public NeutronSFCPortChain getSingleton() {
        return this.singletonPortChain;
    }

    @Override
    public boolean isSingleton() {
        return singletonPortChain != null;
    }

    @Override
    public List<NeutronSFCPortChain> getBulk() {
        return this.bulkRequest;
    }
}
