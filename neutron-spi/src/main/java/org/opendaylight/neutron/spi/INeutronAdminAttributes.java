/*
 * Copyright (C) 2016 Intel Coporporation  All rights reserved.
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
public interface INeutronAdminAttributes<T extends INeutronAdminAttributes> extends INeutronBaseAttributes<T> {
    Boolean getAdminStateUp();

    void setAdminStateUp(Boolean adminStateUp);

}
