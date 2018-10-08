/*
 * Copyright (c) 2018 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.neutron.spi;

import org.eclipse.persistence.descriptors.ClassExtractor;
import org.eclipse.persistence.sessions.Record;
import org.eclipse.persistence.sessions.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO.
 *
 * @author Michael Vorburger.ch
 */
public class NoClassExtractor extends ClassExtractor {

    private static final Logger LOG = LoggerFactory.getLogger(NoClassExtractor.class);

    @Override
    public Class<?> extractClassFromRow(Record record, Session session) {
        return null;
    }
}
