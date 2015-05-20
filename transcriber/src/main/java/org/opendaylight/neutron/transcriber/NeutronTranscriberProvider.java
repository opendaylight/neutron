package org.opendaylight.neutron.transcriber;

import java.util.List;

import org.opendaylight.controller.sal.binding.api.BindingAwareBroker.ProviderContext;
import org.opendaylight.controller.sal.binding.api.BindingAwareProvider;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NeutronTranscriberProvider implements BindingAwareProvider, AutoCloseable {
    private static final Logger logger = LoggerFactory.getLogger(NeutronTranscriberProvider.class);

    private BundleContext bundleContext;

    private List<ServiceRegistration<?>> registrations;

    private Activator activator;

    public NeutronTranscriberProvider(BundleContext bundleContext) {
        logger.warn("BundleContext set to: {}",bundleContext);
        this.bundleContext = bundleContext;
    }

    @Override
    public void onSessionInitiated(ProviderContext providerContext) {
        this.activator = new Activator();
        this.activator.setProviderContext(providerContext);
        try {
            logger.warn("BundleContext found to be: {}",bundleContext);
            this.activator.start(bundleContext);
        } catch (Exception e) {
            logger.warn("Unable to start Neutron Transcriber because: ",e);
        }
    }

    @Override
    public void close() throws Exception {
        this.activator.stop(bundleContext);
    }
}
