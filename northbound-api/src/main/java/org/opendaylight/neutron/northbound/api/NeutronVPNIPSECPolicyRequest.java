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
import org.opendaylight.neutron.spi.NeutronVPNIPSECPolicy;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class NeutronVPNIPSECPolicyRequest implements INeutronRequest<NeutronVPNIPSECPolicy> {

    @XmlElement(name = "ipsecpolicy")
    NeutronVPNIPSECPolicy singletonVPNIPSECPolicy;

    @XmlElement(name = "ipsecpolicies")
    List<NeutronVPNIPSECPolicy> bulkVPNIPSECPolicies;

    NeutronVPNIPSECPolicyRequest() {
    }

    NeutronVPNIPSECPolicyRequest(NeutronVPNIPSECPolicy policy) {
        singletonVPNIPSECPolicy = policy;
        bulkVPNIPSECPolicies = null;
    }

    NeutronVPNIPSECPolicyRequest(List<NeutronVPNIPSECPolicy> policies) {
        singletonVPNIPSECPolicy = null;
        bulkVPNIPSECPolicies = policies;
    }

    @Override
    public NeutronVPNIPSECPolicy getSingleton() {
        return singletonVPNIPSECPolicy;
    }

    @Override
    public List<NeutronVPNIPSECPolicy> getBulk() {
        return bulkVPNIPSECPolicies;
    }

    @Override
    public boolean isSingleton() {
        return (singletonVPNIPSECPolicy != null);
    }
}
