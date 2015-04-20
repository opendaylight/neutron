package org.opendaylight.neutron.transcriber;

import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.controller.sal.binding.api.BindingAwareBroker.ProviderContext;

public abstract class AbstractNeutronInterface implements AutoCloseable {

    private ProviderContext providerContext;
    private DataBroker db;

    AbstractNeutronInterface(ProviderContext providerContext) {
        this.providerContext = providerContext;
        this.db = providerContext.getSALService(DataBroker.class);
    }

    public DataBroker getDataBroker() {
        return db;
    }

    @Override
    public void close() throws Exception {
        // TODO Auto-generated method stub

    }

}
