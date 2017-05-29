/*
 * Copyright (c) 2015 IBM Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.spi;

import java.io.Serializable;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public final class NeutronVpnIpSecPolicy extends NeutronBaseAttributes<NeutronVpnIpSecPolicy> implements Serializable {
    private static final Logger LOG = LoggerFactory.getLogger(NeutronVpnIpSecPolicy.class);
    private static final long serialVersionUID = 1L;

    // See OpenStack Network API v2.0 Reference for description of
    // annotated attributes

    @XmlElement(name = "transform_protocol")
    String transformProtocol;

    @XmlElement(name = "encapsulation_mode")
    String encapsulationMode;

    @XmlElement(name = "auth_algorithm")
    String authAlgorithm;

    @XmlElement(name = "encryption_algorithm")
    String encryptionAlgorithm;

    @XmlElement(name = "pfs")
    String perfectForwardSecrecy;

    @XmlElement(name = "lifetime")
    NeutronVpnLifetime lifetime;

    public NeutronVpnIpSecPolicy() {
    }

    public String getTransformProtocol() {
        return transformProtocol;
    }

    public void setTransformProtocol(String transformProtocol) {
        this.transformProtocol = transformProtocol;
    }

    public String getEncapsulationMode() {
        return encapsulationMode;
    }

    public void setEncapsulationMode(String encapsulationMode) {
        this.encapsulationMode = encapsulationMode;
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

    public String getPerfectForwardSecrecy() {
        return perfectForwardSecrecy;
    }

    public void setPerfectForwardSecrecy(String perfectForwardSecrecy) {
        this.perfectForwardSecrecy = perfectForwardSecrecy;
    }

    public NeutronVpnLifetime getLifetime() {
        return lifetime;
    }

    public void setLifetime(NeutronVpnLifetime lifetime) {
        this.lifetime = lifetime;
    }

    /**
     * This method copies selected fields from the object and returns them
     * as a new object, suitable for marshaling.
     *
     * @param fields
     *            List of attributes to be extracted
     * @return a NeutronVpnIpSecPolicy object with only the selected fields
     *             populated
     */
    public NeutronVpnIpSecPolicy extractFields(List<String> fields) {
        NeutronVpnIpSecPolicy ans = new NeutronVpnIpSecPolicy();
        for (String s : fields) {
            if (extractField(s, ans)) {
                continue;
            }
            switch (s) {
                case "transform_protocol":
                    ans.setTransformProtocol(this.getTransformProtocol());
                    break;
                case "encapsulation_mode":
                    ans.setEncapsulationMode(this.getEncapsulationMode());
                    break;
                case "auth_algorithm":
                    ans.setAuthAlgorithm(this.getAuthAlgorithm());
                    break;
                case "encryption_algorithm":
                    ans.setEncryptionAlgorithm(this.getEncryptionAlgorithm());
                    break;
                case "pfs":
                    ans.setPerfectForwardSecrecy(this.getPerfectForwardSecrecy());
                    break;
                default:
                    LOG.warn("{} is not an NeutronVpnIpSecPolicy suitable field.", s);
                    break;
            }
        }
        return ans;
    }

}
