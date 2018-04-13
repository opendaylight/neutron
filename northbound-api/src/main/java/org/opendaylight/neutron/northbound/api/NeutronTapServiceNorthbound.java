/*
 * Copyright (c) 2017 Intel, Corp. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.neutron.northbound.api;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.codehaus.enunciate.jaxrs.ResponseCode;
import org.codehaus.enunciate.jaxrs.StatusCodes;
import org.opendaylight.neutron.spi.INeutronTapServiceCRUD;
import org.opendaylight.neutron.spi.NeutronTapService;

@Path("/tap/services")
public final class NeutronTapServiceNorthbound
        extends AbstractNeutronNorthbound<NeutronTapService, NeutronTapServiceRequest, INeutronTapServiceCRUD> {

    private static final String RESOURCE_NAME = "Tap Service";

    @Override
    protected String getResourceName() {
        return RESOURCE_NAME;
    }

    /**
     * Returns a list of all Tap Services.
     */
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAUTHORIZED, condition = "Unauthorized"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_IMPLEMENTED, condition = "Not Implemented"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response listGroups(
            // return fields
            @QueryParam("fields") List<String> fields,
            // OpenStack Tap Service attributes
            @QueryParam("id") String queryTapServiceUUID,
            @QueryParam("tenant_id") String queryTapServiceTenantID,
            @QueryParam("name") String queryTapServiceName,
            @QueryParam("port_id") String queryTapServicePortID,
            // pagination
            @QueryParam("limit") String limit,
            @QueryParam("marker") String marker,
            @QueryParam("page_reverse") String pageReverse) {
        INeutronTapServiceCRUD tapServiceInterface = getNeutronCRUD();
        List<NeutronTapService> ans = new ArrayList<>();
        for (NeutronTapService nsg : tapServiceInterface.getAll()) {
            if ((queryTapServiceUUID == null || queryTapServiceUUID.equals(nsg.getID()))
                    && (queryTapServiceTenantID == null || queryTapServiceTenantID.equals(nsg.getTenantID()))
                    && (queryTapServiceName == null || queryTapServiceName.equals(nsg.getName()))
                    && (queryTapServicePortID == null || queryTapServicePortID.equals(nsg.getTapServicePortID()))) {
                if (fields.size() > 0) {
                    ans.add(nsg.extractFields(fields));
                } else {
                    ans.add(nsg);
                }
            }
        }
        return Response.status(HttpURLConnection.HTTP_OK).entity(new NeutronTapServiceRequest(ans)).build();
    }

    /**
     * Returns a specific Tap Service.
     */
    @Path("{tapServiceUUID}")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAUTHORIZED, condition = "Unauthorized"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_IMPLEMENTED, condition = "Not Implemented"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response showTapService(@PathParam("tapServiceUUID") String tapServiceUUID,
            @QueryParam("fields") List<String> fields) {
        return show(tapServiceUUID, fields);
    }

    /**
     * Creates new Tap Service.
     */
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_CREATED, condition = "Created"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response createTapService(final NeutronTapServiceRequest input) {
        return create(input);
    }

    /**
     * Updates a Tap Service.
     */
    @Path("{tapServiceUUID}")
    @PUT
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response updateTapService(@PathParam("tapServiceUUID") String tapServiceUUID,
            final NeutronTapServiceRequest input) {
        return update(tapServiceUUID, input);
    }

    /**
     * Deletes a Tap Service.
     */
    @Path("{tapServiceUUID}")
    @DELETE
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_NO_CONTENT, condition = "No Content"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response deleteTapService(@PathParam("tapServiceUUID") String tapServiceUUID) {
        return delete(tapServiceUUID);
    }
}
