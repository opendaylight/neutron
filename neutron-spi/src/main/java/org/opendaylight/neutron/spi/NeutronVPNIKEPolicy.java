/*
 * Copyright (c) 2015 IBM Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.spi;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class NeutronVPNIKEPolicy extends NeutronObject<NeutronVPNIKEPolicy>
        implements Serializable, INeutronObject<NeutronVPNIKEPolicy> {
    private static final long serialVersionUID = 1L;

    // See OpenStack Network API v2.0 Reference for description of
    // annotated attributes

    @XmlElement(name = "name")
    String name;

    @XmlElement(name = "auth_algorithm")
    String authAlgorithm;

    @XmlElement(name = "encryption_algorithm")
    String encryptionAlgorithm;

    @XmlElement(name = "phase1_negotiation_mode")
    String phase1NegotiationMode;

    @XmlElement(name = "pfs")
    String perfectForwardSecrecy;

    @XmlElement(name = "ike_version")
    String ikeVersion;

    @XmlElement(name = "lifetime")
    NeutronVPNLifetime lifetime;

    public NeutronVPNIKEPolicy() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthAlgorithm() {
        return authAlgorithm;
    }

    public void setAuthAlgorithm(String authAlgorithm) {
        this.authAlgorithm = authAlgorithm;
    }

    public String getEncryptionAlgorithm() {
        return encryptionAlgorithm;
    }

    public void setEncryptionAlgorithm(String encryptionAlgorithm) {
        this.encryptionAlgorithm = encryptionAlgorithm;
    }

    public String getPhase1NegotiationMode() {
        return phase1NegotiationMode;
    }

    public void setPhase1NegotiationMode(String phase1NegotiationMode) {
        this.phase1NegotiationMode = phase1NegotiationMode;
    }

    public String getPerfectForwardSecrecy() {
        return perfectForwardSecrecy;
    }

    public void setPerfectForwardSecrecy(String perfectForwardSecrecy) {
        this.perfectForwardSecrecy = perfectForwardSecrecy;
    }

    public String getIkeVersion() {
        return ikeVersion;
    }

    public void setIkeVersion(String ikeVersion) {
        this.ikeVersion = ikeVersion;
    }

    public NeutronVPNLifetime getLifetime() {
        return lifetime;
    }

    public void setLifetime(NeutronVPNLifetime lifetime) {
        this.lifetime = lifetime;
    }

    /**
     * This method copies selected fields from the object and returns them
     * as a new object, suitable for marshaling.
     *
     * @param fields
     *            List of attributes to be extracted
     * @return a NeutronVPNIKEPolicy object with only the selected fields
     * populated
     */
    public NeutronVPNIKEPolicy extractFields(List<String> fields) {
        NeutronVPNIKEPolicy ans = new NeutronVPNIKEPolicy();
        Iterator<String> i = fields.iterator();
        while (i.hasNext()) {
            String s = i.next();
            if (s.equals("id")) {
                ans.setID(this.getID());
            }
            if (s.equals("name")) {
                ans.setName(this.getName());
            }
            if (s.equals("tenant_id")) {
                ans.setTenantID(this.getTenantID());
            }
            if (s.equals("auth_algorithm")) {
                ans.setAuthAlgorithm(this.getAuthAlgorithm());
            }
            if (s.equals("encryption_algorithm")) {
                ans.setEncryptionAlgorithm(this.getEncryptionAlgorithm());
            }
            if (s.equals("phase1_negotiation_mode")) {
                ans.setPhase1NegotiationMode(this.getPhase1NegotiationMode());
            }
            if (s.equals("pfs")) {
                ans.setPerfectForwardSecrecy(this.getPerfectForwardSecrecy());
            }
            if (s.equals("ike_version")) {
                ans.setIkeVersion(this.getIkeVersion());
            }
        }
        return ans;
    }
}
