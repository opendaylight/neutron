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
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nonnull;
import org.opendaylight.controller.md.sal.binding.api.ClusteredDataTreeChangeListener;
import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.controller.md.sal.binding.api.DataObjectModification;
import org.opendaylight.controller.md.sal.binding.api.DataTreeIdentifier;
import org.opendaylight.controller.md.sal.binding.api.DataTreeModification;
import org.opendaylight.controller.md.sal.common.api.data.LogicalDatastoreType;
import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.yang.types.rev130715.Uuid;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.rev150712.Neutron;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.secgroups.rev150712.security.rules.attributes.SecurityRules;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.secgroups.rev150712.security.rules.attributes.security.rules.SecurityRule;
import org.opendaylight.yangtools.concepts.ListenerRegistration;
import org.opendaylight.yangtools.yang.binding.DataObject;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NeutronLoggerSecurityRule implements AutoCloseable {
    private static final Logger LOG = LoggerFactory.getLogger(NeutronLoggerSecurityRule.class);

    private DataBroker db;
    private ClusteredDataTreeChangeListener rulesTreeListener;
    private ListenerRegistration<? extends ClusteredDataTreeChangeListener> rulesTreeRegistration;
    private ClusteredDataTreeChangeListener ruleListener;
    private Map<Uuid, ListenerRegistration<? extends ClusteredDataTreeChangeListener>> ruleRegistrations =
            new HashMap<>();

    public NeutronLoggerSecurityRule(@Nonnull DataBroker db) {
        LOG.info("Creating NeutronLoggerSecurityRule {}", db);
        this.db = Preconditions.checkNotNull(db, "null db");
    }

    private InstanceIdentifier<SecurityRule> createInstanceIdentifier(SecurityRule securityRule) {
        return InstanceIdentifier.create(Neutron.class)
            .child(SecurityRules.class).child(SecurityRule.class,
                                              securityRule.getKey());
    }

    private InstanceIdentifier<SecurityRules> createInstanceIdentifier() {
        return InstanceIdentifier.create(Neutron.class)
            .child(SecurityRules.class);
    }

    private void formatRule(@Nonnull final StringBuilder builder, @Nonnull final SecurityRule rule) {
        if (rule == null) {
            builder.append("rule: null");
        } else {
            builder.append("rule: ").append(rule.toString());
            if (rule.getProtocol() != null) {
                builder.append(" protocol: ").append(rule.getProtocol().toString());
            }
        }
    }

    private void formatObjectModification(@Nonnull StringBuilder builder,
                                          @Nonnull DataObjectModification<SecurityRule> objectModification) {
        switch (objectModification.getModificationType()) {
            case SUBTREE_MODIFIED:
                builder.append("subtree:");
                for (DataObjectModification<? extends DataObject> child :
                             objectModification.getModifiedChildren()) {
                    builder.append("\n").append(child.getDataType().getName());
                }
                break;
            case WRITE: {
                builder.append("WRITE: ");
                SecurityRule ruleBefore = objectModification.getDataBefore();
                builder.append("\nbefore: ");
                formatRule(builder, ruleBefore);
                SecurityRule ruleAfter = objectModification.getDataAfter();
                builder.append("\nafter: ");
                formatRule(builder, ruleAfter);
                break;
            }
            case DELETE: {
                builder.append("DELETE: ");
                SecurityRule ruleBefore = objectModification.getDataBefore();
                builder.append("\nbefore: ");
                formatRule(builder, ruleBefore);
                SecurityRule ruleAfter = objectModification.getDataAfter();
                builder.append("\nafter: ");
                formatRule(builder, ruleAfter);
                break;
            }
            default:
                LOG.info("default: rule changed {}", objectModification.getModificationType());
                break;
        }
    }

    private void securityRuleChanged(@Nonnull Collection<DataTreeModification<SecurityRule>> changes) {
        LOG.info("rule changed");
        final StringBuilder builder = new StringBuilder();
        builder.append("rule Changed. ");
        for (DataTreeModification<SecurityRule> treeModification : changes) {
            formatObjectModification(builder, treeModification.getRootNode());
        }
        LOG.info(builder.toString());
    }

    private void securityRulesTreeChanged(@Nonnull Collection<DataTreeModification<SecurityRules>> changes) {
        LOG.info("securityRules changed");
        final StringBuilder builder = new StringBuilder();
        builder.append("securityRulesChanged. ");
        for (DataTreeModification<SecurityRules> treeModification : changes) {
            DataObjectModification<SecurityRules> objectModification = treeModification.getRootNode();
            switch (objectModification.getModificationType()) {
                case SUBTREE_MODIFIED:
                    builder.append("SUBTREE:");
                    for (DataObjectModification<? extends DataObject> child :
                                 objectModification.getModifiedChildren()) {
                        builder.append("\n");
                        if (child.getDataType() == SecurityRule.class) {
                            formatObjectModification(builder, (DataObjectModification<SecurityRule>)child);
                        } else {
                            builder.append(child.getDataType().getName());
                        }
                    }
                    break;
                case WRITE:
                    SecurityRules securityRulesBefore = objectModification.getDataBefore();
                    SecurityRules securityRulesAfter = objectModification.getDataAfter();
                    for (SecurityRule rule : securityRulesAfter.getSecurityRule()) {
                        builder.append("write rule: ");
                        formatRule(builder, rule);
                        InstanceIdentifier<SecurityRule> iid = createInstanceIdentifier(rule);
                        DataTreeIdentifier<SecurityRule> dataTreeId =
                                new DataTreeIdentifier<>(LogicalDatastoreType.CONFIGURATION, iid);
                        ListenerRegistration<? extends ClusteredDataTreeChangeListener>
                                registration = db.registerDataTreeChangeListener(dataTreeId, ruleListener);
                        if (ruleRegistrations.putIfAbsent(rule.getUuid(), registration) != null) {
                            registration.close();
                        }
                    }
                    break;
                case DELETE:
                    SecurityRules securityRules = objectModification.getDataBefore();
                    for (SecurityRule rule : securityRules.getSecurityRule()) {
                        builder.append("delete rule: ");
                        formatRule(builder, rule);
                        ListenerRegistration<? extends ClusteredDataTreeChangeListener> registration =
                                ruleRegistrations.remove(rule.getUuid());
                        registration.close();
                    }
                    break;
                default:
                    LOG.info("default: rules changed {}", objectModification.getModificationType());
                    break;
            }
        }
        LOG.info(builder.toString());
    }

    public void init() {
        LOG.info("Register listener for Neutron model data changes");
        InstanceIdentifier<SecurityRules> iid = createInstanceIdentifier();

        DataTreeIdentifier<SecurityRules> dataTreeId =
            new DataTreeIdentifier<>(LogicalDatastoreType.CONFIGURATION, iid);
        rulesTreeListener = new ClusteredDataTreeChangeListener<SecurityRules>() {
            @Override
            public void onDataTreeChanged(Collection<DataTreeModification<SecurityRules>> changes) {
                securityRulesTreeChanged(changes);
            }
        };
        rulesTreeRegistration = db.registerDataTreeChangeListener(dataTreeId, rulesTreeListener);

        ruleListener = new ClusteredDataTreeChangeListener<SecurityRule>() {
            @Override
            public void onDataTreeChanged(Collection<DataTreeModification<SecurityRule>> changes) {
                securityRuleChanged(changes);
            }
        };
    }

    @Override
    public void close() throws Exception {
        for (ListenerRegistration<? extends ClusteredDataTreeChangeListener> registration :
                     ruleRegistrations.values()) {
            registration.close();
        }
        ruleRegistrations.clear();
        ruleListener = null;
        rulesTreeRegistration.close();
        rulesTreeRegistration = null;
    }
}
