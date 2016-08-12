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
    NeutronVPNIKEPolicy singletonIKEPolicy;

    @XmlElement(name = "ikepolicies")
    List<NeutronVPNIKEPolicy> bulkIKEPolicies;

    NeutronVPNIKEPolicyRequest() {
    }

    NeutronVPNIKEPolicyRequest(NeutronVPNIKEPolicy policy) {
        singletonIKEPolicy = policy;
        bulkIKEPolicies = null;
    }

    NeutronVPNIKEPolicyRequest(List<NeutronVPNIKEPolicy> policies) {
        singletonIKEPolicy = null;
        bulkIKEPolicies = policies;
    }

    @Override
    public NeutronVPNIKEPolicy getSingleton() {
        return singletonIKEPolicy;
    }

    @Override
    public List<NeutronVPNIKEPolicy> getBulk() {
        return bulkIKEPolicies;
    }

    @Override
    public boolean isSingleton() {
        return (singletonIKEPolicy != null);
    }
}
