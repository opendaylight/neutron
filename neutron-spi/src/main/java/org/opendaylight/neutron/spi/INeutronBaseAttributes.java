/*
 * Copyright (C) 2016 Intel Corporation  All rights reserved.
 * Copyright (c) 2016 Isaku Yamahata  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.neutron.spi;

/**
 * This class contains behaviour common to Neutron configuration objects.
 */
public interface INeutronBaseAttributes<T extends INeutronBaseAttributes<T>> extends INeutronObject<T> {

    String getName();

    void setName(String name);
}
