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

public interface INeutronRequest<T extends INeutronObject<T>> {
    default T getSingleton() {
        // return this.sinleton
        Class<?> cls = getClass();
        try {
            Field field = cls.getDeclaredField("singleton");
            @SuppressWarnings("unchecked")
            T value = (T) field.get(this);
            return value;
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new IllegalArgumentException(e);
        }
    }

    default boolean isSingleton() {
        // return this.sinleton != null
        Class<?> cls = getClass();
        try {
            Field field = cls.getDeclaredField("singleton");
            return field.get(this) != null;
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new IllegalArgumentException(e);
        }
    }

    default List<T> getBulk() {
        // return this.bulkRequest
        Class<?> cls = getClass();
        try {
            Field field = cls.getDeclaredField("bulkRequest");
            @SuppressWarnings("unchecked")
            List<T> value = (List<T>) field.get(this);
            return value;
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
