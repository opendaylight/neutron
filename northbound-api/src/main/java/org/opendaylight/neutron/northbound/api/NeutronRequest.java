/*
 * Copyright (c) 2018 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.neutron.northbound.api;

import javax.xml.bind.annotation.XmlTransient;
import org.opendaylight.neutron.spi.INeutronObject;

/**
 * Abstract base class a correct {@link #toString()} for all {@link INeutronRequest} implementations.
 *
 * @author Michael Vorburger.ch
 */
@XmlTransient
public abstract class NeutronRequest<T extends INeutronObject<T>> implements INeutronRequest<T> {

    // TODO move the singleton & bulkRequest fields from the subclasses up into this class

    @Override
    public String toString() {
        T singleton = getSingleton();
        return getClass().getSimpleName() + "{"
            + singleton != null ? "singleton=" + singleton
            : "bulkRequest=" + getBulk()
            + "}";
    }
}
