/*
 * Copyright (c) 2014, 2015 Red Hat, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.northbound.api;

import java.lang.reflect.Field;
import java.util.List;
import org.opendaylight.neutron.spi.INeutronObject;


public interface INeutronRequest<T extends INeutronObject> {
    default T getSingleton() {
        Class aClass = getClass();
        try {
            Field field = aClass.getField("singleton");
            return (T) field.get(this);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            return null;
        }
    }

    default boolean isSingleton() {
        Class aClass = getClass();
        try {
            Field field = aClass.getField("singleton");
            return field.get(this) != null;
        } catch (IllegalAccessException | NoSuchFieldException e) {
            return false;
        }
    }

    default List<T> getBulk() {
        Class aClass = getClass();
        try {
            Field field = aClass.getField("bulkRequest");
            return (List<T>) field.get(this);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            return null;
        }
    }
}
