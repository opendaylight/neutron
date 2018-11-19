/*
 * Copyright (c) 2016 Intel Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.logger;

import com.google.common.base.Preconditions;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.Collection;
import javax.annotation.Nonnull;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Singleton;
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

@Singleton
public final class NeutronLogger {
    private static final Logger LOG = LoggerFactory.getLogger(NeutronLogger.class);

    private final DataBroker db;
    private ClusteredDataTreeChangeListener<Neutron> configurationDataTreeChangeListener;
    private ListenerRegistration<? extends ClusteredDataTreeChangeListener<Neutron>> configurationRegisteredListener;
    private ClusteredDataTreeChangeListener<Neutron> operationalDataTreeChangeListener;
    private ListenerRegistration<? extends ClusteredDataTreeChangeListener<Neutron>> operationalRegisteredListener;

    @Inject
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
                messageBuilder.append("DELETE: type: ").append(typeName).append("\n");
                final T dataBefore = objectModification.getDataBefore();
                messageBuilder.append(dataBefore.toString());
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

    @SuppressFBWarnings("SLF4J_FORMAT_SHOULD_BE_CONST")
    private void logChanges(String prefix, @Nonnull Collection<DataTreeModification<Neutron>> changes) {
        if (LOG.isInfoEnabled()) {
            final StringBuilder messageBuilder = new StringBuilder(prefix);
            formatChanges(messageBuilder, changes);
            LOG.info(messageBuilder.toString());
        }
    }

    @PostConstruct
    public void init() {
        LOG.info("Register listener for Neutron model data changes");
        InstanceIdentifier<Neutron> instanceId = Preconditions.checkNotNull(InstanceIdentifier.create(Neutron.class));

        DataTreeIdentifier<Neutron> configurationDataTreeId = new DataTreeIdentifier<>(
                LogicalDatastoreType.CONFIGURATION, instanceId);
        configurationDataTreeChangeListener = changes -> logChanges("Configuration DataTreeChanged ", changes);
        configurationRegisteredListener = db.registerDataTreeChangeListener(configurationDataTreeId,
                configurationDataTreeChangeListener);

        DataTreeIdentifier<
                Neutron> operationalDataTreeId = new DataTreeIdentifier<>(LogicalDatastoreType.OPERATIONAL, instanceId);
        operationalDataTreeChangeListener = changes -> logChanges("Operational DataTreeChanged ", changes);
        operationalRegisteredListener = db.registerDataTreeChangeListener(operationalDataTreeId,
                operationalDataTreeChangeListener);
    }

    @PreDestroy
    public void close() throws Exception {
        configurationRegisteredListener.close();
        configurationRegisteredListener = null;
        operationalRegisteredListener.close();
        operationalRegisteredListener = null;
    }
}
