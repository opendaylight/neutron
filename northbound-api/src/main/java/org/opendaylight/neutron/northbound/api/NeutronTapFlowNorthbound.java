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
import javax.inject.Inject;
import javax.inject.Singleton;
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
import org.opendaylight.neutron.spi.INeutronTapFlowCRUD;
import org.opendaylight.neutron.spi.NeutronTapFlow;
import org.ops4j.pax.cdi.api.OsgiService;

@Singleton
@Path("/tap/flows")
public final class NeutronTapFlowNorthbound
        extends AbstractNeutronNorthbound<NeutronTapFlow, NeutronTapFlowRequest, INeutronTapFlowCRUD> {

    private static final String RESOURCE_NAME = "Tap Flow";

    @Inject
    public NeutronTapFlowNorthbound(@OsgiService INeutronTapFlowCRUD neutronCRUD) {
        super(neutronCRUD);
    }

    @Override
    protected String getResourceName() {
        return RESOURCE_NAME;
    }

    /**
     * Returns a list of all Tap Flows.
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
            // OpenStack Tap Flow attributes
            @QueryParam("id") String queryTapFlowUUID,
            @QueryParam("tenant_id") String queryTapFlowTenantID,
            @QueryParam("name") String queryTapFlowName,
            @QueryParam("source_port") String queryTapFlowSourcePort,
            @QueryParam("tap_service_id") String queryTapServiceID,
            @QueryParam("direction") String queryTapFlowDirection,
            // pagination
            @QueryParam("limit") String limit,
            @QueryParam("marker") String marker,
            @QueryParam("page_reverse") String pageReverse) {

        INeutronTapFlowCRUD tapFlowInterface = getNeutronCRUD();
        List<NeutronTapFlow> ans = new ArrayList<>();
        for (NeutronTapFlow nsg : tapFlowInterface.getAll()) {
            if ((queryTapFlowUUID == null || queryTapFlowUUID.equals(nsg.getID()))
                    && (queryTapFlowTenantID == null || queryTapFlowTenantID.equals(nsg.getTenantID()))
                    && (queryTapFlowName == null || queryTapFlowName.equals(nsg.getName()))
                    && (queryTapServiceID == null || queryTapServiceID.equals(nsg.getTapFlowServiceID()))
                    && (queryTapFlowDirection == null || queryTapFlowDirection.equals(nsg.getTapFlowDirection()))
                    && (queryTapFlowSourcePort == null
                        || queryTapFlowSourcePort.equals(nsg.getTapFlowSourcePort()))) {
                if (fields.size() > 0) {
                    ans.add(nsg.extractFields(fields));
                } else {
                    ans.add(nsg);
                }
            }
        }
        return Response.status(HttpURLConnection.HTTP_OK).entity(new NeutronTapFlowRequest(ans)).build();
    }

    /**
     * Creates new Tap Flow.
     */
    @Path("{tapServiceUUID}/flows")
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_CREATED, condition = "Created"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response createTapFlow(@PathParam("tapServiceUUID") String tapServiceUUID,
                                  final NeutronTapFlowRequest input) {

        INeutronTapFlowCRUD tapFlowInterface = getNeutronCRUD();

        if (input.isSingleton()) {
            NeutronTapFlow singleton = input.getSingleton();
            singleton.setTapFlowServiceID(tapServiceUUID);

            tapFlowInterface.addTapFlow(singleton);
        } else {
            throw new BadRequestException("Only Singleton tapFlow creation supported");
        }

        return Response.status(HttpURLConnection.HTTP_CREATED).entity(input).build();
    }

    /**
     * Returns a specific Tap Flow.
     */
    @Path("{tapServiceUUID}/flows/{tapFlowUUID}")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAUTHORIZED, condition = "Unauthorized"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_IMPLEMENTED, condition = "Not Implemented"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response showTapFlow(@PathParam("tapServiceUUID") String tapServiceUUID,
                                @PathParam("tapFlowUUID") String tapFlowUUID,
                                @QueryParam("fields") List<String> fields) {

        INeutronTapFlowCRUD tapFlowInterface = getNeutronCRUD();
        if (!tapFlowInterface.tapFlowExists(tapServiceUUID, tapFlowUUID)) {
            throw new ResourceNotFoundException("Specified UUID does not Exist");
        }

        NeutronTapFlow tapFlow = tapFlowInterface.getTapFlow(tapServiceUUID, tapFlowUUID);
        if (fields.size() > 0) {
            return Response.status(HttpURLConnection.HTTP_OK)
                    .entity(new NeutronTapFlowRequest(tapFlow.extractFields(fields))).build();
        } else {
            return Response.status(HttpURLConnection.HTTP_OK)
                    .entity(new NeutronTapFlowRequest(tapFlow)).build();
        }
    }

    /**
     * Updates a Tap Flow.
     */
    @Path("{tapServiceUUID}/flows/{tapFlowUUID}")
    @PUT
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response updateTapFlow(@PathParam("tapServiceUUID") String tapServiceUUID,
                                  @PathParam("tapFlowUUID") String tapFlowUUID,
                                  final NeutronTapFlowRequest input) {

        INeutronTapFlowCRUD tapFlowInterface = getNeutronCRUD();

        if (!tapFlowInterface.tapFlowExists(tapServiceUUID, tapFlowUUID)) {
            throw new ResourceNotFoundException("Specified UUID does not Exist");
        }

        NeutronTapFlow singleton = input.getSingleton();
        singleton.setTapFlowServiceID(tapServiceUUID);
        tapFlowInterface.updateTapFlow(singleton);

        return Response.status(HttpURLConnection.HTTP_OK).entity(input).build();
    }

    /**
     * Deletes a Tap Flow.
     */
    @Path("{tapServiceUUID}/flows/{tapFlowUUID}")
    @DELETE
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_NO_CONTENT, condition = "No Content"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response deleteTapFlow(@PathParam("tapServiceUUID") String tapServiceUUID,
                                  @PathParam("tapFlowUUID") String tapFlowUUID) {

        INeutronTapFlowCRUD tapFlowInterface = getNeutronCRUD();

        if (!tapFlowInterface.tapFlowExists(tapServiceUUID, tapFlowUUID)) {
            throw new ResourceNotFoundException("Specified UUID does not Exist");
        }

        tapFlowInterface.deleteTapFlow(tapServiceUUID, tapFlowUUID);
        return Response.status(HttpURLConnection.HTTP_NO_CONTENT).build();
    }
}
