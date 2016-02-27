package org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.neutron.logger.rev160609;

import org.opendaylight.neutron.logger.NeutronLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NeutronLoggerModule extends org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.neutron.logger.rev160609.AbstractNeutronLoggerModule {
    private static final Logger LOG = LoggerFactory.getLogger(NeutronLoggerModule.class);

    public NeutronLoggerModule(org.opendaylight.controller.config.api.ModuleIdentifier identifier, org.opendaylight.controller.config.api.DependencyResolver dependencyResolver) {
        super(identifier, dependencyResolver);
    }

    public NeutronLoggerModule(org.opendaylight.controller.config.api.ModuleIdentifier identifier, org.opendaylight.controller.config.api.DependencyResolver dependencyResolver, org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.neutron.logger.rev160609.NeutronLoggerModule oldModule, java.lang.AutoCloseable oldInstance) {
        super(identifier, dependencyResolver, oldModule, oldInstance);
    }

    @Override
    public void customValidation() {
        // add custom validation form module attributes here.
    }

    @Override
    public java.lang.AutoCloseable createInstance() {
        final NeutronLogger neutronLogger = new NeutronLogger(getDataBrokerDependency());
        LOG.info("NeutronLogger started.");
        return neutronLogger;
    }
}
