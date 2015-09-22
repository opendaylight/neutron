/*
 * Copyright (c) 2015 Intel Corporation and others.  All rights reserved.
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
import org.opendaylight.neutron.spi.INeutronObject;

import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// When INeutron*Aware interfaces are deleted, this class should be deleted for BORON
@Deprecated
public abstract class AbstractNeutronNorthboundIAware<T extends INeutronObject, NeutronRequest extends INeutronRequest<T>, I extends INeutronCRUD<T>, INeutronAware>
    extends AbstractNeutronNorthbound<T, NeutronRequest, I> {
    private static final Logger LOGGER = LoggerFactory
        .getLogger(AbstractNeutronNorthboundIAware.class);

    protected static final String NO_PROVIDERS = "No providers registered.  Please try again later";
    protected static final String NO_PROVIDER_LIST = "Couldn't get providers list.  Please try again later";

    protected abstract Object[] getInstances();
    protected abstract int canCreate(Object instance, T singleton);
    protected abstract void created(Object instance, T singleton);
    protected abstract int canUpdate(Object instance, T delta, T original);
    protected abstract void updated(Object instance, T original);
    protected abstract int canDelete(Object instance, T singleton);
    protected abstract void deleted(Object instance, T singleton);

    @Override
    protected Response create(final NeutronRequest input) {
        I neutronCRUD = getNeutronCRUD();
        if (input.isSingleton()) {
            T singleton = input.getSingleton();

            Object[] instances = this.getInstances();
            if (instances != null) {
                for (Object instance : instances) {
                    int status = this.canCreate(instance, singleton);
                    if (status < HTTP_OK_BOTTOM || status > HTTP_OK_TOP) {
                        return Response.status(status).build();
                    }
                }
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
                    for (Object instance : instances) {
                        int status = canCreate(instance, test);
                        if (status < HTTP_OK_BOTTOM || status > HTTP_OK_TOP) {
                            return Response.status(status).build();
                        }
                    }
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

    @Override
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
            for (Object instance : instances) {
                int status = canUpdate(instance, delta, original);
                if (status < HTTP_OK_BOTTOM || status > HTTP_OK_TOP) {
                    return Response.status(status).build();
                }
            }
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

    @Override
    protected Response delete(String uuid) {
        final I neutronCRUD = getNeutronCRUD();

        T singleton = neutronCRUD.get(uuid);
        Object[] instances = getInstances();
        if (instances != null) {
            for (Object instance : instances) {
                int status = canDelete(instance, singleton);
                if (status < HTTP_OK_BOTTOM || status > HTTP_OK_TOP) {
                    return Response.status(status).build();
                }
            }
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
