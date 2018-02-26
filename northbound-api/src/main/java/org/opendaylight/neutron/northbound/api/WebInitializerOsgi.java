/*
 * Copyright (c) 2018 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.neutron.northbound.api;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.ServletException;
import org.opendaylight.aaa.web.osgi.PaxWebServer;
import org.ops4j.pax.web.service.WebContainer;

/**
 * Initializer for Neutron's web components in an OSGi environment.
 *
 * @author Michael Vorburger.ch
 */
@Singleton
public class WebInitializerOsgi {

    private final WebInitializer web;

    @Inject
    public WebInitializerOsgi(WebContainer paxWebContainer) throws ServletException {
        this.web = new WebInitializer(new PaxWebServer(paxWebContainer));
    }

    public void close() {
        web.close();
    }
}
