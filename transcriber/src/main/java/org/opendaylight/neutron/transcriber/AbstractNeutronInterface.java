/*
 * Copyright (c) 2015 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.transcriber;

import java.lang.reflect.Type;
import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.neutron.spi.INeutronObject;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.rev150712.Neutron;
import org.opendaylight.yangtools.concepts.Builder;
import org.opendaylight.yangtools.yang.binding.Augmentable;
import org.opendaylight.yangtools.yang.binding.ChildOf;
import org.opendaylight.yangtools.yang.binding.DataObject;
import org.opendaylight.yangtools.yang.binding.Identifiable;
import org.opendaylight.yangtools.yang.binding.Identifier;


public abstract class AbstractNeutronInterface<
        T extends DataObject & Identifiable<K> & ChildOf<? super U>,
        U extends ChildOf<? super Neutron> & Augmentable<U>,
        K extends Identifier<T>,
        S extends INeutronObject<S>>
        extends AbstractTranscriberInterface<T, U, K, S, Neutron> {

    @Override
    protected Class<Neutron> getMdParentClass(final Type[] types) {
        return Neutron.class;
    }

    protected AbstractNeutronInterface(Class<? extends Builder<T>> builderClass, DataBroker db) {
        super(builderClass, db);
    }
}