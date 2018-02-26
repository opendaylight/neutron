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
import org.apache.shiro.web.env.EnvironmentLoaderListener;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.opendaylight.aaa.shiro.filters.AAAShiroFilter;
import org.opendaylight.aaa.shiro.web.env.KarafIniWebEnvironment;
import org.opendaylight.infrautils.web.FilterDetails;
import org.opendaylight.infrautils.web.ServletDetails;
import org.opendaylight.infrautils.web.WebContext;
import org.opendaylight.infrautils.web.WebServer;
import org.ops4j.pax.cdi.api.OsgiService;

/**
 * Initializer for web components.
 *
 * @author Michael Vorburger.ch
 */
@Singleton
public class WebInitializer {

    @Inject
    public WebInitializer(@OsgiService WebServer webServer) {
        // TODO confirm through testing that Jersey & Neutron are fine without sessions
        webServer.registerWebContext(WebContext.builder().contextPath("/controller/nb/v2/neutron").hasSessions(false)

            .addServlet(ServletDetails.builder().servlet(new com.sun.jersey.spi.container.servlet.ServletContainer())
                 // TODO test using javax.ws.rs.core.Application.class.getName() instead; NB .core.
                .putInitParam("javax.ws.rs.Application", NeutronNorthboundRSApplication.class.getName())
                .addUrlPattern("/*").build())

             // TODO factor out this common AAA related web context configuration to somewhere shared
             //   instead of likely copy/pasting it from here to WebInitializer classes which will want to do the same
            .putContextParam("shiroEnvironmentClass", KarafIniWebEnvironment.class.getName())
            .addListener(new EnvironmentLoaderListener())
            .addFilter(FilterDetails.builder().filter(new AAAShiroFilter()).addUrlPattern("/*").build())

            .addFilter(FilterDetails.builder().filter(new CrossOriginFilter()).addUrlPattern("/*")
                .putInitParam("allowedOrigins", "*")
                .putInitParam("allowedMethods", "GET,POST,OPTIONS,DELETE,PUT,HEAD")
                .putInitParam("allowedHeaders", "origin, content-type, accept, authorization")
                .build())

            .build());
    }

}
