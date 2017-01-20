package org.opendaylight.controller.config.yang.config.neutron_hostconfig_vpp_provider.impl;

import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.neutron.hostconfig.vpp.NeutronHostconfigVppListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

public class NeutronHostconfigVppProviderInstance implements AutoCloseable {

    private final NeutronHostconfigVppListener neutronHostconfigVppListener;
    
    private static final Logger LOG = LoggerFactory.getLogger(NeutronHostconfigVppProviderInstance.class);

    public NeutronHostconfigVppProviderInstance(final DataBroker dataBroker, String socketFile, String socketName) {
        neutronHostconfigVppListener = new NeutronHostconfigVppListener(Preconditions.checkNotNull(dataBroker));
        LOG.info("Initializing Neutron-Hostconfig-Vpp-Listener");
    }

    public void initialize() {
        neutronHostconfigVppListener.start();
    }

    @Override
    public void close() throws Exception {
        neutronHostconfigVppListener.close();
    }

}
