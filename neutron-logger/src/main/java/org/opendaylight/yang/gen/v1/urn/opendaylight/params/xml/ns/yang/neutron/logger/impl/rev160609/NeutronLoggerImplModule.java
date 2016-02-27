package org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.neutron.logger.impl.rev160609;

import org.opendaylight.neutron.logger.NeutronLogger;

public class NeutronLoggerImplModule extends org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.neutron.logger.impl.rev160609.AbstractNeutronLoggerImplModule {
    public NeutronLoggerImplModule(org.opendaylight.controller.config.api.ModuleIdentifier identifier, org.opendaylight.controller.config.api.DependencyResolver dependencyResolver) {
        super(identifier, dependencyResolver);
    }

    public NeutronLoggerImplModule(org.opendaylight.controller.config.api.ModuleIdentifier identifier, org.opendaylight.controller.config.api.DependencyResolver dependencyResolver, org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.neutron.logger.impl.rev160609.NeutronLoggerImplModule oldModule, java.lang.AutoCloseable oldInstance) {
        super(identifier, dependencyResolver, oldModule, oldInstance);
    }

    @Override
    public void customValidation() {
        // add custom validation form module attributes here.
    }

    @Override
    public java.lang.AutoCloseable createInstance() {
        final NeutronLogger neutronLogger = new NeutronLogger(getDataBrokerDependency());
        return neutronLogger;
    }

}
