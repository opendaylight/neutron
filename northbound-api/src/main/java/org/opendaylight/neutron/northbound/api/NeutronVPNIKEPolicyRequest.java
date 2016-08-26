/*
 * Copyright (c) 2015 IBM Corporation and others.  All rights reserved.
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
import org.opendaylight.neutron.spi.NeutronVPNIKEPolicy;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class NeutronVPNIKEPolicyRequest implements INeutronRequest<NeutronVPNIKEPolicy> {
    @XmlElement(name = "ikepolicy")
    NeutronVPNIKEPolicy singleton;

    @XmlElement(name = "ikepolicies")
    List<NeutronVPNIKEPolicy> bulkRequest;

    NeutronVPNIKEPolicyRequest() {
    }

    NeutronVPNIKEPolicyRequest(NeutronVPNIKEPolicy policy) {
        singleton = policy;
    }

    NeutronVPNIKEPolicyRequest(List<NeutronVPNIKEPolicy> policies) {
        bulkRequest = policies;
    }
}
