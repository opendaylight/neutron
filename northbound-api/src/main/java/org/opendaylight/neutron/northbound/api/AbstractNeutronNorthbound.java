/*
 * Copyright (c) 2015 Intel Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.northbound.api;

import static org.opendaylight.neutron.spi.INeutronCRUD.Result.DependencyMissing;
import static org.opendaylight.neutron.spi.INeutronCRUD.Result.DoesNotExist;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.Objects;
import javax.ws.rs.core.Response;
import org.opendaylight.neutron.spi.INeutronCRUD;
import org.opendaylight.neutron.spi.INeutronCRUD.Result;
import org.opendaylight.neutron.spi.INeutronObject;

public abstract class AbstractNeutronNorthbound<T extends INeutronObject<T>, R extends INeutronRequest<T>,
        I extends INeutronCRUD<T>> {

    // T extends INeutronObject<T> as 0th type argument
    private static final int NEUTRON_ARGUMENT_TYPE_INDEX = 0;
    // NeutronRequest extends INeutronRequest<T> as 1st type argument
    private static final int NEUTRON_REQUEST_TYPE_INDEX = 1;

    protected static final int HTTP_OK_BOTTOM = 200;
    protected static final int HTTP_OK_TOP = 299;
    private static final int HTTP_MISSING_DEPENDENCY = 442; // see NEUTRON-158 (also in neutron.e2etest.HttpUtils)

    private static final String INTERFACE_NAME_BASE = " CRUD Interface";
    private static final String UUID_NO_EXIST_BASE = " UUID does not exist.";

    private final I neutronCRUD;

    protected AbstractNeutronNorthbound(I neutronCRUD) {
        this.neutronCRUD = Objects.requireNonNull(neutronCRUD, "neutronCRUD");
    }

    protected final String serviceUnavailable() {
        return getResourceName() + INTERFACE_NAME_BASE + RestMessages.SERVICEUNAVAILABLE.toString();
    }

    protected final String uuidNoExist() {
        return getResourceName() + UUID_NO_EXIST_BASE;
    }

    protected abstract String getResourceName();

    private <K> Class<K> getActualTypeArgument(final int typeIndex) {
        ParameterizedType parameterizedType = (ParameterizedType) getClass().getGenericSuperclass();
        @SuppressWarnings("unchecked")
        Class<K> cls = (Class<K>) parameterizedType.getActualTypeArguments()[typeIndex];
        return cls;
    }

    private R newNeutronRequest(T neutronObject) {
        // return new R(neutronObject)

        // argumentClass = T.class
        Class<T> argumentClass = getActualTypeArgument(NEUTRON_ARGUMENT_TYPE_INDEX);
        // cls = NeturonRequest.class
        Class<R> cls = getActualTypeArgument(NEUTRON_REQUEST_TYPE_INDEX);
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
        return this.neutronCRUD;
    }

    protected Response show(String uuid,
            // return fields
            List<String> fields) {
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
        if (input.isSingleton()) {
            T singleton = input.getSingleton();

            singleton.initDefaults();
            if (neutronCRUD.add(singleton).equals(DependencyMissing)) {
                return Response.status(HTTP_MISSING_DEPENDENCY).entity(input).build();
            }
        } else {
            if (input.getBulk() == null) {
                throw new BadRequestException("Invalid requests");
            }
            for (T test : input.getBulk()) {
                test.initDefaults();
                if (neutronCRUD.add(test).equals(DependencyMissing)) {
                    return Response.status(HTTP_MISSING_DEPENDENCY).entity(input).build();
                }
            }
        }
        return Response.status(HttpURLConnection.HTTP_CREATED).entity(input).build();
    }

    protected void updateDelta(String uuid, T delta, T original) {
    }

    private boolean checkRevisionNumber(T original, T delta) {
        // If new update is null ignore the original revision number
        if (delta.getRevisionNumber() == null) {
            return false;
        }
        // If what is stored is null no need for comparison
        if (original.getRevisionNumber() == null) {
            return false;
        }
        if (original.getRevisionNumber() > delta.getRevisionNumber()) {
            return true;
        }
        return false;
    }

    protected Response update(String uuid, final R input) {
        if (!input.isSingleton()) {
            throw new BadRequestException("Only singleton edit supported");
        }
        T delta = input.getSingleton();
        T original = neutronCRUD.get(uuid);
        if (original == null) {
            throw new ResourceNotFoundException(uuidNoExist());
        }
        if (checkRevisionNumber(original, delta)) {
            return Response.status(HttpURLConnection.HTTP_OK).build();
        }
        updateDelta(uuid, delta, original);
        /*
         * update the object and return it
         */
        Result updateResult = neutronCRUD.update(uuid, delta);
        if (updateResult.equals(DoesNotExist)) {
            throw new ResourceNotFoundException(uuidNoExist());
        } else if (updateResult.equals(DependencyMissing)) {
            return Response.status(HTTP_MISSING_DEPENDENCY).entity(input).build();
        }
        T updated = neutronCRUD.get(uuid);
        return Response.status(HttpURLConnection.HTTP_OK).entity(newNeutronRequest(updated)).build();
    }

    protected Response delete(String uuid) {
        /*
         * remove it and return 204 status
         */
        if (!neutronCRUD.remove(uuid)) {
            throw new ResourceNotFoundException(uuidNoExist());
        }

        return Response.status(HttpURLConnection.HTTP_NO_CONTENT).build();
    }
}
