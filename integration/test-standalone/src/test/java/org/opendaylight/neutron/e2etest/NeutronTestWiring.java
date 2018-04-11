/*
 * Copyright (c) 2018 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.neutron.e2etest;

import com.google.inject.AbstractModule;
import org.opendaylight.aaa.web.WebContextSecurer;
import org.opendaylight.aaa.web.WebServer;
import org.opendaylight.aaa.web.jetty.JettyWebServer;
import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.controller.md.sal.binding.test.DataBrokerTestModule;
import org.opendaylight.neutron.northbound.api.WebInitializer;
import org.opendaylight.neutron.spi.INeutronNetworkCRUD;
import org.opendaylight.neutron.transcriber.NeutronNetworkInterface;

/**
 * Dependency Injection wiring for Neutron.
 *
 * @author Michael Vorburger.ch
 */
public class NeutronTestWiring extends AbstractModule {

    @Override
    protected void configure() {
        bind(WebServer.class).toInstance(new JettyWebServer(9090));
        bind(WebContextSecurer.class).toInstance((webContextBuilder, urlPatterns) -> { }); // NOOP
        bind(WebInitializer.class);

        DataBrokerTestModule dataBrokerTestModule = new DataBrokerTestModule(true);
        DataBroker dataBroker = dataBrokerTestModule.getDataBroker();
        bind(DataBroker.class).toInstance(dataBroker);

        bind(INeutronNetworkCRUD.class).to(NeutronNetworkInterface.class);
    }

}
