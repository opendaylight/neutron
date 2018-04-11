/*
 * Copyright (c) 2018 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.neutron.e2etest;

import com.google.inject.AbstractModule;
import org.opendaylight.neutron.northbound.api.WebInitializer;

/**
 * Dependency Injection wiring for Neutron.
 *
 * @author Michael Vorburger.ch
 */
public class NeutronWiring extends AbstractModule {

    // TODO Later move this somewhere non-test, so that it can also be used by a opendaylight.simple.

    @Override
    protected void configure() {
        bind(WebInitializer.class);
    }
/*
    @Provides
    @Singleton
    Servlet jaxRSNeutron(WebContextProvider webContextProvider) throws ServletException {
    }
*/
}
