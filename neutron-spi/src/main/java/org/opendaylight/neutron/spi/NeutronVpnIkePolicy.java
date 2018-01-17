/*
 * Copyright (c) 2015 IBM Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.spi;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public final class NeutronVpnIkePolicy extends NeutronBaseAttributes<NeutronVpnIkePolicy> {
    private static final long serialVersionUID = 1L;

    // See OpenStack Network API v2.0 Reference for description of
    // annotated attributes

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
    NeutronVpnLifetime lifetime;

    public NeutronVpnIkePolicy() {
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

    public NeutronVpnLifetime getLifetime() {
        return lifetime;
    }

    public void setLifetime(NeutronVpnLifetime lifetime) {
        this.lifetime = lifetime;
    }

    @Override
    protected boolean extractField(String field, NeutronVpnIkePolicy ans) {
        switch (field) {
            case "auth_algorithm":
                ans.setAuthAlgorithm(this.getAuthAlgorithm());
                break;
            case "encryption_algorithm":
                ans.setEncryptionAlgorithm(this.getEncryptionAlgorithm());
                break;
            case "phase1_negotiation_mode":
                ans.setPhase1NegotiationMode(this.getPhase1NegotiationMode());
                break;
            case "pfs":
                ans.setPerfectForwardSecrecy(this.getPerfectForwardSecrecy());
                break;
            case "ike_version":
                ans.setIkeVersion(this.getIkeVersion());
                break;
            default:
                return super.extractField(field, ans);
        }
        return true;
    }
}
