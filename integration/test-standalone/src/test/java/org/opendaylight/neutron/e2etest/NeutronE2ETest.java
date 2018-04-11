/*
 * Copyright (c) 2018 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.neutron.e2etest;

import java.io.IOException;
import javax.inject.Inject;
import org.junit.Rule;
import org.junit.Test;
import org.opendaylight.aaa.web.WebServer;
import org.opendaylight.infrautils.inject.guice.testutils.AnnotationsModule;
import org.opendaylight.infrautils.inject.guice.testutils.GuiceRule;

/**
 * Neutron "end to end" (component) test.
 *
 * <p>This is similar to the ITNeutronE2E, but does the same without OSGi and Karaf.
 *
 * @author Michael Vorburger.ch
 */
public class NeutronE2ETest {

    public @Rule GuiceRule guice = new GuiceRule(
            NeutronWiring.class, /* TODO WebTestWiring.class, */ AnnotationsModule.class);

    public @Inject WebServer webServer;

    @Test
    public void test() throws IOException, InterruptedException {
        NeutronAllTests.testNeutron(webServer.getBaseURL() + "/controller/nb/v2/neutron");
    }

}
