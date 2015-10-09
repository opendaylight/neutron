/*
 * Copyright (c) 2015 Intel Corporation  All rights reserved.
 * Copyright (c) 2015 Isaku Yamahata  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.northbound.api;

import java.net.HttpURLConnection;
import java.util.List;

import org.codehaus.enunciate.jaxrs.ResponseCode;
import org.codehaus.enunciate.jaxrs.StatusCodes;
import org.opendaylight.neutron.spi.INeutronCRUD;
import org.opendaylight.neutron.spi.NeutronObject;

import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractNeutronNorthbound<T extends NeutronObject, NeutronRequest extends INeutronRequest<T>, I extends INeutronCRUD<T>, INeutronAware> {
    private static final Logger LOGGER = LoggerFactory
        .getLogger(AbstractNeutronNorthbound.class);

    protected static final int HTTP_OK_BOTTOM = 200;
    protected static final int HTTP_OK_TOP = 299;

    private static final String INTERFACE_NAME_BASE = " CRUD Interface";
    private static final String UUID_NO_EXIST_BASE = " UUID does not exist.";
    protected static final String NO_PROVIDERS = "No providers registered.  Please try again later";
    protected static final String NO_PROVIDER_LIST = "Couldn't get providers list.  Please try again later";

    protected final String serviceUnavailable() {
        return getResourceName() + INTERFACE_NAME_BASE + RestMessages.SERVICEUNAVAILABLE.toString();
    }
    protected final String uuidNoExist() {
        return getResourceName() + UUID_NO_EXIST_BASE;
    }

    protected abstract String getResourceName();
    protected abstract T extractFields(T o, List<String> fields);
    protected abstract NeutronRequest newNeutronRequest(T o);
    protected abstract I getNeutronCRUD();
    protected abstract Object[] getInstances();
    protected abstract int canCreate(Object instance, T singleton);
    protected abstract void created(Object instance, T singleton);
    protected abstract int canUpdate(Object instance, T delta, T original);
    protected abstract void updated(Object instance, T original);
    protected abstract int canDelete(Object instance, T singleton);
    protected abstract void deleted(Object instance, T singleton);

    protected Response show(String uuid,
                            // return fields
                            List<String> fields) {
        I neutronCRUD = getNeutronCRUD();
        if (!neutronCRUD.exists(uuid)) {
            throw new ResourceNotFoundException(uuidNoExist());
        }
        if (fields.size() > 0) {
            T ans = neutronCRUD.get(uuid);
            return Response.status(HttpURLConnection.HTTP_OK).entity(
                    newNeutronRequest(extractFields(ans, fields))).build();
        } else {
            return Response.status(HttpURLConnection.HTTP_OK).entity(newNeutronRequest(neutronCRUD.get(uuid))).build();
        }
    }

    protected Response create(final NeutronRequest input) {
        I neutronCRUD = getNeutronCRUD();
        if (input.isSingleton()) {
            T singleton = input.getSingleton();

            Object[] instances = this.getInstances();
            if (instances != null) {
                if (instances.length > 0) {
                    for (Object instance : instances) {
                        int status = this.canCreate(instance, singleton);
                        if (status < HTTP_OK_BOTTOM || status > HTTP_OK_TOP) {
                            return Response.status(status).build();
                        }
                    }
                } else {
                    throw new ServiceUnavailableException(NO_PROVIDERS);
                }
            } else {
                throw new ServiceUnavailableException(NO_PROVIDER_LIST);
            }
            singleton.initDefaults();
            neutronCRUD.add(singleton);
            if (instances != null) {
                for (Object instance : instances) {
                    created(instance, singleton);
                }
            }
        } else {
            Object[] instances = this.getInstances();
            for (T test : input.getBulk()) {
                if (instances != null) {
                    if (instances.length > 0) {
                        for (Object instance : instances) {
                            int status = canCreate(instance, test);
                            if (status < HTTP_OK_BOTTOM || status > HTTP_OK_TOP) {
                                return Response.status(status).build();
                            }
                        }
                    } else {
                        throw new ServiceUnavailableException(NO_PROVIDERS);
                    }
                } else {
                    throw new ServiceUnavailableException(NO_PROVIDER_LIST);
                }
            }

            /*
             * now, each element of the bulk request can be added to the cache
             */
            for (T test : input.getBulk()) {
                test.initDefaults();
                neutronCRUD.add(test);
                if (instances != null) {
                    for (Object instance : instances) {
                        this.created(instance, test);
                    }
                }
            }
        }
        return Response.status(HttpURLConnection.HTTP_CREATED).entity(input).build();
    }

    protected void updateDelta(String uuid, T delta, T original) {
    }

    protected Response update(String uuid, final NeutronRequest input) {
        I neutronCRUD = getNeutronCRUD();
        if (!input.isSingleton()) {
            throw new BadRequestException("Only singleton edit supported");
        }
        T delta = input.getSingleton();
        T original = neutronCRUD.get(uuid);
        if (original == null) {
            throw new ResourceNotFoundException(getResourceName() + " doesn't Exist");
        }
        updateDelta(uuid, delta, original);

        Object[] instances = getInstances();
        if (instances != null) {
            if (instances.length > 0) {
                for (Object instance : instances) {
                    int status = canUpdate(instance, delta, original);
                    if (status < HTTP_OK_BOTTOM || status > HTTP_OK_TOP) {
                        return Response.status(status).build();
                    }
                }
            } else {
                throw new ServiceUnavailableException(NO_PROVIDERS);
            }
        } else {
            throw new ServiceUnavailableException(NO_PROVIDER_LIST);
        }

        /*
         * update the object and return it
         */
        neutronCRUD.update(uuid, delta);
        T updated = neutronCRUD.get(uuid);
        if (instances != null) {
            for (Object instance : instances) {
                updated(instance, updated);
            }
        }
        return Response.status(HttpURLConnection.HTTP_OK).entity(newNeutronRequest(neutronCRUD.get(uuid))).build();
    }

    protected Response delete(String uuid) {
        final I neutronCRUD = getNeutronCRUD();

        T singleton = neutronCRUD.get(uuid);
        Object[] instances = getInstances();
        if (instances != null) {
            if (instances.length > 0) {
                for (Object instance : instances) {
                    int status = canDelete(instance, singleton);
                    if (status < HTTP_OK_BOTTOM || status > HTTP_OK_TOP) {
                        return Response.status(status).build();
                    }
                }
            } else {
                throw new ServiceUnavailableException(NO_PROVIDERS);
            }
        } else {
            throw new ServiceUnavailableException(NO_PROVIDER_LIST);
        }

        /*
         * remove it and return 204 status
         */
        final String resourceName = getResourceName();
        boolean exist = false;
        try {
            exist = neutronCRUD.remove(uuid);
        } catch (Exception e) {
            LOGGER.debug("exception during remove {} {} {}",
                         resourceName, uuid, e);
            throw new InternalServerErrorException("Could not delete " +
                                                   resourceName);
        }
        if (!exist) {
            throw new ResourceNotFoundException(uuidNoExist());
        }

        if (instances != null) {
            for (Object instance : instances) {
                deleted(instance, singleton);
            }
        }
        return Response.status(HttpURLConnection.HTTP_NO_CONTENT).build();
    }
}
