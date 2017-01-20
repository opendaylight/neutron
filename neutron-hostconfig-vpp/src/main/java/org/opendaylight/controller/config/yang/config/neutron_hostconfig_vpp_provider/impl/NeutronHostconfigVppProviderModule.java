package org.opendaylight.controller.config.yang.config.neutron_hostconfig_vpp_provider.impl;
public class NeutronHostconfigVppProviderModule extends org.opendaylight.controller.config.yang.config.neutron_hostconfig_vpp_provider.impl.AbstractNeutronHostconfigVppProviderModule {
    public NeutronHostconfigVppProviderModule(org.opendaylight.controller.config.api.ModuleIdentifier identifier, org.opendaylight.controller.config.api.DependencyResolver dependencyResolver) {
        super(identifier, dependencyResolver);
    }

    public NeutronHostconfigVppProviderModule(org.opendaylight.controller.config.api.ModuleIdentifier identifier, org.opendaylight.controller.config.api.DependencyResolver dependencyResolver, org.opendaylight.controller.config.yang.config.neutron_hostconfig_vpp_provider.impl.NeutronHostconfigVppProviderModule oldModule, java.lang.AutoCloseable oldInstance) {
        super(identifier, dependencyResolver, oldModule, oldInstance);
    }

    @Override
    public void customValidation() {
        // add custom validation form module attributes here.
    }

    @Override
    public java.lang.AutoCloseable createInstance() {
        // TODO:implement
        throw new java.lang.UnsupportedOperationException();
    }

}
