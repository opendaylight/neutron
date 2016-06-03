/*
 * Copyright (c) 2015 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.transcriber;

import org.opendaylight.controller.sal.binding.api.BindingAwareBroker.ProviderContext;
import org.opendaylight.controller.sal.binding.api.BindingAwareProvider;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NeutronTranscriberProvider implements BindingAwareProvider, AutoCloseable {
    private static final Logger LOGGER = LoggerFactory.getLogger(NeutronTranscriberProvider.class);

    private final BundleContext bundleContext;

    private Activator activator;

    public NeutronTranscriberProvider(BundleContext bundleContext) {
        LOGGER.warn("BundleContext set to: {}",bundleContext);
        this.bundleContext = bundleContext;
    }

    @Override
    public void onSessionInitiated(ProviderContext providerContext) {
        this.activator = new Activator(providerContext);
        try {
            LOGGER.warn("BundleContext found to be: {}",bundleContext);
            this.activator.start(bundleContext);
        } catch (final Exception e) {
            LOGGER.warn("Unable to start Neutron Transcriber because: ",e);
        }
    }

    @Override
    public void close() throws Exception {
        this.activator.stop(bundleContext);
    }
}
