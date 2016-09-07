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
import org.opendaylight.controller.md.sal.binding.api.DataObjectModification;
import org.opendaylight.controller.md.sal.binding.api.DataTreeIdentifier;
import org.opendaylight.controller.md.sal.binding.api.DataTreeModification;
import org.opendaylight.controller.md.sal.common.api.data.LogicalDatastoreType;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.rev150712.Neutron;
import org.opendaylight.yangtools.concepts.ListenerRegistration;
import org.opendaylight.yangtools.yang.binding.DataObject;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class NeutronLogger implements AutoCloseable {
    private static final Logger LOG = LoggerFactory.getLogger(NeutronLogger.class);

    private DataBroker db;
    private ClusteredDataTreeChangeListener<Neutron> configurationDataTreeChangeListener;
    private ListenerRegistration<? extends ClusteredDataTreeChangeListener<Neutron>> configurationRegisteredListener;
    private ClusteredDataTreeChangeListener<Neutron> operationalDataTreeChangeListener;
    private ListenerRegistration<? extends ClusteredDataTreeChangeListener<Neutron>> operationalRegisteredListener;

    public NeutronLogger(@Nonnull DataBroker db) {
        LOG.info("Creating NeutronLogger {}", db);
        this.db = Preconditions.checkNotNull(db, "null db");
    }

    private <T extends DataObject> void formatModification(@Nonnull final StringBuilder messageBuilder,
            @Nonnull final DataObjectModification<T> objectModification) {
        final String typeName = objectModification.getDataType().getSimpleName();

        switch (objectModification.getModificationType()) {
            case SUBTREE_MODIFIED:
                for (final DataObjectModification<? extends DataObject> child :
                        objectModification.getModifiedChildren()) {
                    formatModification(messageBuilder, child);
                }
                break;
            case WRITE:
                messageBuilder.append("\n");
                messageBuilder.append("WRITE: type: ").append(typeName).append("\n");
                final T dataAfter = objectModification.getDataAfter();
                messageBuilder.append(dataAfter.toString());
                break;
            case DELETE:
                messageBuilder.append("\n");
                messageBuilder.append("DELETE: ").append(typeName);
                break;
            default:
                LOG.warn("unknown modification type: {}", typeName);
                break;
        }
    }

    private <T extends DataObject> void formatChanges(@Nonnull final StringBuilder messageBuilder,
            @Nonnull final Collection<DataTreeModification<T>> changes) {
        for (DataTreeModification<T> modification : changes) {
            final DataTreeIdentifier<T> identifier = modification.getRootPath();
            final LogicalDatastoreType datastoreType = identifier.getDatastoreType();
            if (datastoreType == LogicalDatastoreType.OPERATIONAL) {
                messageBuilder.append("OPERATIONAL: ");
            } else {
                messageBuilder.append("CONFIGURATION: ");
            }

            final DataObjectModification<T> objectModification = modification.getRootNode();
            formatModification(messageBuilder, objectModification);
        }
    }

    private void logChanges(String prefix, @Nonnull Collection<DataTreeModification<Neutron>> changes) {
        final StringBuilder messageBuilder = new StringBuilder();
        messageBuilder.append(prefix);
        formatChanges(messageBuilder, changes);
        LOG.info(messageBuilder.toString());
    }

    private void configurationDataTreeChanged(@Nonnull Collection<DataTreeModification<Neutron>> changes) {
        logChanges("Configuration DataTreeChanged ", changes);
    }

    private void operationalDataTreeChanged(@Nonnull Collection<DataTreeModification<Neutron>> changes) {
        logChanges("Operational DataTreeChanged ", changes);
    }

    public void init() {
        LOG.info("Register listener for Neutron model data changes");
        InstanceIdentifier<Neutron> instanceId = Preconditions.checkNotNull(InstanceIdentifier.create(Neutron.class));

        DataTreeIdentifier<Neutron> configurationDataTreeId = new DataTreeIdentifier<>(
                LogicalDatastoreType.CONFIGURATION, instanceId);
        configurationDataTreeChangeListener = new ClusteredDataTreeChangeListener<Neutron>() {
            @Override
            public void onDataTreeChanged(Collection<DataTreeModification<Neutron>> changes) {
                configurationDataTreeChanged(changes);
            }
        };
        configurationRegisteredListener = db.registerDataTreeChangeListener(configurationDataTreeId,
                configurationDataTreeChangeListener);

        DataTreeIdentifier<
                Neutron> operationalDataTreeId = new DataTreeIdentifier<>(LogicalDatastoreType.OPERATIONAL, instanceId);
        operationalDataTreeChangeListener = new ClusteredDataTreeChangeListener<Neutron>() {
            @Override
            public void onDataTreeChanged(Collection<DataTreeModification<Neutron>> changes) {
                operationalDataTreeChanged(changes);
            }
        };
        operationalRegisteredListener = db.registerDataTreeChangeListener(operationalDataTreeId,
                operationalDataTreeChangeListener);
    }

    @Override
    public void close() throws Exception {
        configurationRegisteredListener.close();
        configurationRegisteredListener = null;
        operationalRegisteredListener.close();
        operationalRegisteredListener = null;
    }
}
