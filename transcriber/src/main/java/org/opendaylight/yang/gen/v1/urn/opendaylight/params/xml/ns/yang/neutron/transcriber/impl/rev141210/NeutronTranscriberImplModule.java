package org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.neutron.transcriber.impl.rev141210;

import org.opendaylight.controller.sal.binding.api.BindingAwareBroker;
import org.opendaylight.neutron.transcriber.NeutronTranscriberProvider;
import org.osgi.framework.BundleContext;

public class NeutronTranscriberImplModule extends org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.neutron.transcriber.impl.rev141210.AbstractNeutronTranscriberImplModule {
    private BundleContext bundleContext;

    public NeutronTranscriberImplModule(org.opendaylight.controller.config.api.ModuleIdentifier identifier, org.opendaylight.controller.config.api.DependencyResolver dependencyResolver) {
        super(identifier, dependencyResolver);
    }

    public NeutronTranscriberImplModule(org.opendaylight.controller.config.api.ModuleIdentifier identifier, org.opendaylight.controller.config.api.DependencyResolver dependencyResolver, org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.neutron.transcriber.impl.rev141210.NeutronTranscriberImplModule oldModule, java.lang.AutoCloseable oldInstance) {
        super(identifier, dependencyResolver, oldModule, oldInstance);
    }

    @Override
    public void customValidation() {
        // add custom validation form module attributes here.
    }

    @Override
    public java.lang.AutoCloseable createInstance() {
        final NeutronTranscriberProvider provider = new NeutronTranscriberProvider(bundleContext);
        final BindingAwareBroker localBroker = getBrokerDependency();
        localBroker.registerProvider(provider);
        return provider;
    }

    public void setBundleContext(BundleContext bundleContext) {
        this.bundleContext = bundleContext;
    }

}
