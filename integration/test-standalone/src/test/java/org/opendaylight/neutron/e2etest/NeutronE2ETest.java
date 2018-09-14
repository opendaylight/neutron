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
import org.junit.ClassRule;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.opendaylight.aaa.web.WebServer;
import org.opendaylight.infrautils.inject.guice.testutils.AnnotationsModule;
import org.opendaylight.infrautils.inject.guice.testutils.GuiceRule;
import org.opendaylight.infrautils.testutils.ClasspathHellDuplicatesCheckRule;

/**
 * Neutron "end to end" (component) test.
 *
 * <p>This is similar to the ITNeutronE2E, but does the same without OSGi and Karaf.
 *
 * @author Michael Vorburger.ch
 */
@Ignore // TODO NEUTRON-197: remove this again post Neon-MRI, see c/76239
public class NeutronE2ETest {

    public static @ClassRule ClasspathHellDuplicatesCheckRule jHades = new ClasspathHellDuplicatesCheckRule();

    public @Rule GuiceRule guice = new GuiceRule(NeutronTestWiring.class, AnnotationsModule.class);

    public @Inject WebServer webServer;

    @Test
    public void test() throws IOException, InterruptedException {
        String baseURL = webServer.getBaseURL(); // Karaf: "http://localhost:8181"
        NeutronAllTests.testNeutron(baseURL + "/controller/nb/v2/neutron");
    }
}
