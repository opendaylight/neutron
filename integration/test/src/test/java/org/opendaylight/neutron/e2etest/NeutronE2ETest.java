/*
 * Copyright (C) 2015 IBM, Inc.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.e2etest;

import static org.ops4j.pax.exam.CoreOptions.maven;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.configureConsole;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.debugConfiguration;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.features;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.karafDistributionConfiguration;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.keepRuntimeFolder;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.logLevel;
import static org.ops4j.pax.exam.karaf.options.LogLevelOption.LogLevel;

import java.io.File;
import java.io.OutputStreamWriter;

import java.net.HttpURLConnection;
import java.net.URL;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.junit.PaxExam;

import org.osgi.framework.BundleContext;
import org.osgi.service.cm.ConfigurationAdmin;

@RunWith(PaxExam.class)
public class NeutronE2ETest {

    @Inject
    private BundleContext bundleContext;

    @Inject
    private ConfigurationAdmin configurationAdmin;

    @Configuration
    public Option[] config() {
        return new Option[] {
            // Provision and launch a container based on a distribution of Karaf (Apache ServiceMix).
            // FIXME: need to *NOT* hardcode the version here - it breaks on
            // version bumps
            karafDistributionConfiguration()
                .frameworkUrl(
                    maven()
                        .groupId("org.opendaylight.neutron")
                        .artifactId("neutron-karaf")
                        .type("zip")
                        .version("0.6.0-SNAPSHOT"))
                .karafVersion("3.0.1")
                .name("Neutron")
                .unpackDirectory(new File("target/pax"))
                .useDeployFolder(false),
            // FIXME: need to *NOT* hardcode the version here
            // inject maven bundle with jetty-servlets
            mavenBundle().groupId("org.eclipse.jetty").artifactId("jetty-servlets").version("9.2.10.v20150310"),
       // It is really nice if the container sticks around after the test so you can check the contents
       // of the data directory when things go wrong.
            keepRuntimeFolder(),
       // Don't bother with local console output as it just ends up cluttering the logs
            configureConsole().ignoreLocalConsole(),
       // Force the log level to INFO so we have more details during the test.  It defaults to WARN.
            logLevel(LogLevel.INFO),
       // provision the needed features for this test
       //    features("mvn:org.opendaylight.neutron/features-test/0.5.0-SNAPSHOT/xml/features",
       //        "features-neutron-test"),
       // Remember that the test executes in another process.  If you want to debug it, you need
       // to tell Pax Exam to launch that process with debugging enabled.  Launching the test class itself with
       // debugging enabled (for example in Eclipse) will not get you the desired results.
       //   debugConfiguration("5000", true),
       };
    }

    final String base = "http://127.0.0.1:8080/controller/nb/v2/neutron";

    @Test
    public void test() {
        NeutronNetworkTests network_tester = new NeutronNetworkTests(base);
        network_tester.network_collection_get_test();
        network_tester.singleton_network_create_test();
    }
}
