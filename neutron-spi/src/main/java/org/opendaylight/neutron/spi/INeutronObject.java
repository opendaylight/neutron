/*
 * Copyright (C) 2014 Red Hat, Inc.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.spi;

import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.yang.types.rev130715.Uuid;

/**
 * This class contains behaviour common to Neutron configuration objects
 */
public interface INeutronObject {

    String getID();

    void setID(String id);

    String getTenantID();

    void setTenantID(String tenantID);

    void setTenantID(Uuid tenantID);
}
