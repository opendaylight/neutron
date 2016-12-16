/*
 * Copyright (c) 2015 Intel Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.northbound.api;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.net.HttpURLConnection;
import java.util.List;
import javax.ws.rs.core.Response;
import org.opendaylight.neutron.spi.INeutronCRUD;
import org.opendaylight.neutron.spi.INeutronObject;
import org.opendaylight.neutron.spi.NeutronCRUDInterfaces;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractNeutronNorthbound<T extends INeutronObject<T>, R extends INeutronRequest<T>,
        I extends INeutronCRUD<T>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractNeutronNorthbound.class);

    protected static final int HTTP_OK_BOTTOM = 200;
    protected static final int HTTP_OK_TOP = 299;

    private static final String INTERFACE_NAME_BASE = " CRUD Interface";
    private static final String UUID_NO_EXIST_BASE = " UUID does not exist.";

    protected final String serviceUnavailable() {
        return getResourceName() + INTERFACE_NAME_BASE + RestMessages.SERVICEUNAVAILABLE.toString();
    }

    protected final String uuidNoExist() {
        return getResourceName() + UUID_NO_EXIST_BASE;
    }

    protected abstract String getResourceName();

    private R newNeutronRequest(T neutronObject) {
        // return new R(neutronObject);

        ParameterizedType parameterizedType = (ParameterizedType) getClass().getGenericSuperclass();
        // argumentClass = T.class
        Class<T> argumentClass = (Class<T>) parameterizedType.getActualTypeArguments()[0];
        // cls = NeturonRequest.class
        Class<R> cls = (Class<R>) parameterizedType.getActualTypeArguments()[1];
        try {
            // ctor = R constructor
            Constructor<R> ctor = cls.getDeclaredConstructor(argumentClass);
            return ctor.newInstance(neutronObject);
        } catch (NoSuchMethodException | InstantiationException
                 | IllegalAccessException | InvocationTargetException e) {
            // This case shouldn't happen
            throw new IllegalArgumentException(e);
        }
    }

    protected I getNeutronCRUD() {
        ParameterizedType parameterizedType = (ParameterizedType) getClass().getGenericSuperclass();
        // cls = I.class
        Class<I> cls = (Class<I>) parameterizedType.getActualTypeArguments()[2];
        I neutronCrud = NeutronCRUDInterfaces.fetchINeutronCRUD(cls, (Object) this);
        if (neutronCrud == null) {
            throw new ServiceUnavailableException(serviceUnavailable());
        }
        return neutronCrud;
    }

    protected Response show(String uuid,
            // return fields
            List<String> fields) {
        I neutronCRUD = getNeutronCRUD();
        T ans = neutronCRUD.get(uuid);
        if (ans == null) {
            throw new ResourceNotFoundException(uuidNoExist());
        }

        if (fields.size() > 0) {
            return Response.status(HttpURLConnection.HTTP_OK).entity(newNeutronRequest(ans.extractFields(fields)))
                    .build();
        } else {
            return Response.status(HttpURLConnection.HTTP_OK).entity(newNeutronRequest(ans)).build();
        }
    }

    protected Response create(final R input) {
        I neutronCRUD = getNeutronCRUD();
        if (input.isSingleton()) {
            T singleton = input.getSingleton();

            singleton.initDefaults();
            neutronCRUD.add(singleton);
        } else {
            if (input.getBulk() == null) {
                throw new BadRequestException("Invalid requests");
            }
            for (T test : input.getBulk()) {
                test.initDefaults();
                neutronCRUD.add(test);
            }
        }
        return Response.status(HttpURLConnection.HTTP_CREATED).entity(input).build();
    }

    protected void updateDelta(String uuid, T delta, T original) {
    }

    protected Response update(String uuid, final R input) {
        I neutronCRUD = getNeutronCRUD();
        if (!input.isSingleton()) {
            throw new BadRequestException("Only singleton edit supported");
        }
        T delta = input.getSingleton();
        T original = neutronCRUD.get(uuid);
        if (original == null) {
            throw new ResourceNotFoundException(uuidNoExist());
        }
        updateDelta(uuid, delta, original);

        /*
         * update the object and return it
         */
        if (!neutronCRUD.update(uuid, delta)) {
            throw new ResourceNotFoundException(uuidNoExist());
        }
        T updated = neutronCRUD.get(uuid);
        return Response.status(HttpURLConnection.HTTP_OK).entity(newNeutronRequest(updated)).build();
    }

    protected Response delete(String uuid) {
        final I neutronCRUD = getNeutronCRUD();

        /*
         * remove it and return 204 status
         */
        if (!neutronCRUD.remove(uuid)) {
            throw new ResourceNotFoundException(uuidNoExist());
        }

        return Response.status(HttpURLConnection.HTTP_NO_CONTENT).build();
    }
}
