/*
 * Copyright (c) 2016 Intel, Corp. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.northbound.api;

import java.net.HttpURLConnection;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.enunciate.jaxrs.ResponseCode;
import org.codehaus.enunciate.jaxrs.StatusCodes;
import org.opendaylight.neutron.spi.INeutronQosPolicyCRUD;
import org.opendaylight.neutron.spi.NeutronCRUDInterfaces;
import org.opendaylight.neutron.spi.NeutronQosPolicy;

@Path("/qos/policies")
public class NeutronQosPolicyNorthbound extends
    AbstractNeutronNorthbound<NeutronQosPolicy, NeutronQosPolicyRequest, INeutronQosPolicyCRUD> {

    private static final String RESOURCE_NAME = "Qos Policy";

    @Override
    protected String getResourceName() {
        return RESOURCE_NAME;
    }

    @Override
    protected NeutronQosPolicy extractFields(NeutronQosPolicy o, List<String> fields) {
        return o.extractFields(fields);
    }

    @Override
    protected NeutronQosPolicyRequest newNeutronRequest(NeutronQosPolicy o) {
        return new NeutronQosPolicyRequest(o);
    }

    @Override
    protected INeutronQosPolicyCRUD getNeutronCRUD() {
        NeutronCRUDInterfaces answer = new NeutronCRUDInterfaces().fetchINeutronQosPolicyCRUD(this);
        if (answer.getQosPolicyInterface() == null) {
            throw new ServiceUnavailableException(serviceUnavailable());
        }
        return answer.getQosPolicyInterface();
    }

    /**
     * Creates new Qos Policy
     */
    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    @StatusCodes({
        @ResponseCode(code = HttpURLConnection.HTTP_CREATED, condition = "Created"),
        @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE,
            condition = "No providers available")})
    public Response createQosPolicies(final NeutronQosPolicyRequest input) {
        return create(input);
    }

    /**
     * Updates a Qos Policy
     */
    @Path("{qosPolicyUUID}")
    @PUT
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON}) @StatusCodes({
        @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
        @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
        @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE,
            condition = "No providers available")})
    public Response updateQosPolicy(@PathParam("qosPolicyUUID") String qosPolicyUUID,
        final NeutronQosPolicyRequest input) {
        return update(qosPolicyUUID, input);
    }

    /**
     * Deletes a Qos Policy
     */
    @Path("{qosPolicyUUID}")
    @DELETE
    @StatusCodes({@ResponseCode(code = HttpURLConnection.HTTP_NO_CONTENT, condition = "No Content"),
        @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
        @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE,
            condition = "No providers available")})
    public Response deleteQosPolicy(@PathParam("qosPolicyUUID") String qosPolicyUUID) {
        return delete(qosPolicyUUID);
    }
}
