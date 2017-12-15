/*
 * Copyright (c) 2015 IBM Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.spi;

import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.util.regex.Pattern;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class NeutronID implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final String UUID_PATTERN_REGEX =
            "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$";
    private static final Pattern UUID_PATTERN = Pattern.compile(UUID_PATTERN_REGEX);

    // See OpenStack Network API v2.0 Reference for description of
    // annotated attributes

    @XmlElement(name = "id")
    String uuid;

    private static void checkUuidPattern(String uuid) {
        Preconditions.checkNotNull(uuid, "Supplied value may not be null");
        Preconditions.checkArgument(UUID_PATTERN.matcher(uuid).matches(),
                "Supplied value \"%s\" does not match uuid pattern \"%s\"", uuid, UUID_PATTERN_REGEX);
    }

    public NeutronID() {
    }

    public NeutronID(String uuid) {
        checkUuidPattern(uuid);
        this.uuid = uuid;
    }

    public String getID() {
        return uuid;
    }

    public void setID(String newUuid) {
        checkUuidPattern(newUuid);
        this.uuid = newUuid;
    }

    @Override
    public String toString() {
        return "NeutronID{" + "id='" + uuid + '\'' + "}";
    }
}
