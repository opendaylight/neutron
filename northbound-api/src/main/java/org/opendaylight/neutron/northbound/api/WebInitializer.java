/*
 * Copyright (c) 2018 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.neutron.northbound.api;

import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.ServletException;
import org.apache.shiro.web.env.EnvironmentLoaderListener;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.opendaylight.aaa.shiro.filters.AAAShiroFilter;
import org.opendaylight.aaa.shiro.web.env.KarafIniWebEnvironment;
import org.opendaylight.aaa.web.FilterDetails;
import org.opendaylight.aaa.web.ServletDetails;
import org.opendaylight.aaa.web.WebContext;
import org.opendaylight.aaa.web.WebContextRegistration;
import org.opendaylight.aaa.web.WebServer;
import org.ops4j.pax.cdi.api.OsgiService;

/**
 * Initializer for web components.
 *
 * @author Michael Vorburger.ch
 */
@Singleton
public class WebInitializer {

    private final WebContextRegistration registraton;

    @Inject
    public WebInitializer(@OsgiService WebServer webServer) throws ServletException {
        this.registraton = webServer.registerWebContext(WebContext.builder()
            .contextPath("/controller/nb/v2/neutron").supportsSessions(true)
            // TODO confirm through testing that Jersey & Neutron are fine without sessions, and false instead true

            .addServlet(ServletDetails.builder()
                    .servlet(new com.sun.jersey.spi.container.servlet.ServletContainer(
                            new NeutronNorthboundRSApplication()))
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

    @PreDestroy
    public void close() {
        registraton.close();
    }

}
