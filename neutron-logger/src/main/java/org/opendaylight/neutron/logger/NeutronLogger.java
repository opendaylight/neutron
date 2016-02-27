/*
 * Copyright (c) 2016 Intel Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.logger;

import com.google.common.base.Preconditions;

import java.util.Collection;
import javax.annotation.Nonnull;
import org.opendaylight.controller.md.sal.binding.api.ClusteredDataTreeChangeListener;
import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.controller.md.sal.binding.api.DataTreeIdentifier;
import org.opendaylight.controller.md.sal.binding.api.DataTreeModification;
import org.opendaylight.controller.md.sal.common.api.data.LogicalDatastoreType;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.rev150712.Neutron;
import org.opendaylight.yangtools.concepts.ListenerRegistration;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NeutronLogger implements AutoCloseable {
    private static final Logger LOG = LoggerFactory.getLogger(NeutronLogger.class);
    private DataBroker db;
    private ClusteredDataTreeChangeListener configurationDataTreeChangeListener;
    private ListenerRegistration<? extends ClusteredDataTreeChangeListener> configurationRegisteredListener;
    private ClusteredDataTreeChangeListener operationalDataTreeChangeListener;
    private ListenerRegistration<? extends ClusteredDataTreeChangeListener> operationalRegisteredListener;

    public NeutronLogger(@Nonnull DataBroker db) {
        this.db = Preconditions.checkNotNull(db, "null db");

        LOG.info("Register listener for model data changes");
        InstanceIdentifier<Neutron> instanceId = Preconditions.checkNotNull(InstanceIdentifier.create(Neutron.class));

        DataTreeIdentifier<Neutron> configurationDataTreeId =
            new DataTreeIdentifier<>(LogicalDatastoreType.CONFIGURATION, instanceId);
        configurationDataTreeChangeListener = new ClusteredDataTreeChangeListener<Neutron>() {
            @Override
            public void onDataTreeChanged(Collection<DataTreeModification<Neutron>> changes) {
                configurationDataTreeChanged(changes);
            }
        };
        configurationRegisteredListener = db.registerDataTreeChangeListener(configurationDataTreeId,
                                                                            configurationDataTreeChangeListener);

        DataTreeIdentifier<Neutron> operationalDataTreeId =
            new DataTreeIdentifier<>(LogicalDatastoreType.OPERATIONAL, instanceId);
        operationalDataTreeChangeListener = new ClusteredDataTreeChangeListener<Neutron>() {
            @Override
            public void onDataTreeChanged(Collection<DataTreeModification<Neutron>> changes) {
                operationalDataTreeChanged(changes);
            }
        };
        operationalRegisteredListener = db.registerDataTreeChangeListener(operationalDataTreeId,
                                                                          operationalDataTreeChangeListener);
    }

    private void configurationDataTreeChanged(@Nonnull Collection<DataTreeModification<Neutron>> changes) {
        LOG.info("Configuration DataTreeChanged {}", changes);
    }

    private void operationalDataTreeChanged(@Nonnull Collection<DataTreeModification<Neutron>> changes) {
        LOG.info("Operational DataTreeChanged {}", changes);
    }

    @Override
    public void close() throws Exception {
        configurationRegisteredListener.close();
        configurationRegisteredListener = null;
        operationalRegisteredListener.close();
        operationalRegisteredListener = null;
    }
}
