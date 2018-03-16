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
import org.opendaylight.aaa.web.ServletDetails;
import org.opendaylight.aaa.web.WebContext;
import org.opendaylight.aaa.web.WebContextBuilder;
import org.opendaylight.aaa.web.WebContextRegistration;
import org.opendaylight.aaa.web.WebContextSecurer;
import org.opendaylight.aaa.web.WebServer;

/**
 * Initializer for web components.
 *
 * @author Michael Vorburger.ch
 */
@Singleton
public class WebInitializer {

    private final WebContextRegistration registraton;

    @Inject
    public WebInitializer(WebServer webServer, WebContextSecurer webContextSecurer) throws ServletException {
        WebContextBuilder webContextBuilder = WebContext.builder()
            .contextPath("/controller/nb/v2/neutron").supportsSessions(true)
            // TODO confirm through testing that Jersey & Neutron are fine without sessions, and false instead true

            .addServlet(ServletDetails.builder()
                    .servlet(new com.sun.jersey.spi.container.servlet.ServletContainer(
                            new NeutronNorthboundRSApplication()))
                    .addUrlPattern("/*").build());

        webContextSecurer.requireAuthentication(webContextBuilder, "/*");

        this.registraton = webServer.registerWebContext(webContextBuilder.build());
    }

    @PreDestroy
    public void close() {
        registraton.close();
    }

}
