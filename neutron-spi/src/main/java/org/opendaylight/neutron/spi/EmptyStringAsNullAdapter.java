/*
 * Copyright (c) 2018 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.neutron.spi;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * JAXB adapter to treat (certain) empty String as null.
 *
 * <p>See <a href="https://jira.opendaylight.org/browse/NEUTRON-159">NEUTRON-159</a>.
 *
 * @author Michael Vorburger.ch
 */
public class EmptyStringAsNullAdapter extends XmlAdapter<String, String> {

    @Override
    public String unmarshal(String value) throws Exception {
        if (value != null && value.isEmpty()) {
            return null;
        }
        return value;
    }

    @Override
    public String marshal(String value) throws Exception {
        return value;
    }
}
