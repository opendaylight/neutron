/*
 * Copyright (C) 2015 IBM, Inc.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.neutron.e2etest;

import static org.ops4j.pax.exam.CoreOptions.maven;
import static org.ops4j.pax.exam.CoreOptions.vmOption;
import static org.ops4j.pax.exam.CoreOptions.when;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.configureConsole;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.keepRuntimeFolder;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.logLevel;

import java.io.IOException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opendaylight.controller.mdsal.it.base.AbstractMdsalTestBase;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.karaf.options.KarafDistributionOption;
import org.ops4j.pax.exam.karaf.options.LogLevelOption.LogLevel;
import org.ops4j.pax.exam.options.MavenUrlReference;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerClass;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerClass.class)
public class ITNeutronE2E extends AbstractMdsalTestBase {

    private static final String KARAF_DEBUG_PORT = "5005";
    private static final String KARAF_DEBUG_PROP = "karaf.debug";

    @Override
    public MavenUrlReference getFeatureRepo() {
        return maven()
            .groupId("org.opendaylight.neutron")
            .artifactId("features-neutron-test")
            .classifier("features")
            .type("xml")
            .versionAsInProject();
    }

    @Override
    public String getFeatureName() {
        return "odl-neutron-logger-test";
    }

    @Override
    @Configuration
    public Option[] config() {
        Option[] options = super.config();
        Option[] otherOptions = getOtherOptions();
        Option[] combinedOptions = new Option[options.length + otherOptions.length];
        System.arraycopy(options, 0, combinedOptions, 0, options.length);
        System.arraycopy(otherOptions, 0, combinedOptions, options.length,
            otherOptions.length);
        return combinedOptions;
    }

    private Option[] getOtherOptions() {
        return new Option[] {
            vmOption("-javaagent:../../pax/jars/org.jacoco.agent.jar=destfile=../../jacoco-it.exec"),
            keepRuntimeFolder(), configureConsole().ignoreLocalConsole(),
            logLevel(LogLevel.INFO),
            when(Boolean.getBoolean(KARAF_DEBUG_PROP)).useOptions(
                KarafDistributionOption.debugConfiguration(KARAF_DEBUG_PORT, true)),};
    }

    @Test
    public void test() throws IOException, InterruptedException {
        NeutronAllTests.testNeutron("http://127.0.0.1:8181/controller/nb/v2/neutron");
    }

}
