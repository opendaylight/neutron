/*
 * Copyright (c) 2017 Intel Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.hostconfig.vpp.init;

import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.neutron.hostconfig.vpp.NeutronHostconfigVppListener;
import org.opendaylight.neutron.hostconfig.vpp.SocketInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

public class NeutronHostconfigVppInitializer implements AutoCloseable {

    private final NeutronHostconfigVppListener neutronHostconfigVppListener;
    
    private static final Logger LOG = LoggerFactory.getLogger(NeutronHostconfigVppInitializer.class);

    public NeutronHostconfigVppInitializer(final DataBroker dataBroker, String socketPath, String socketName) {
        neutronHostconfigVppListener = new NeutronHostconfigVppListener(Preconditions.checkNotNull(dataBroker), new SocketInfo(socketPath, socketName));
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
