/*
 * Copyright (c) 2015 Hewlett-Packard Development Company, L.P. and others.  All rights reserved.
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
import org.opendaylight.neutron.spi.INeutronL2gatewayCRUD;
import org.opendaylight.neutron.spi.NeutronL2gateway;
import org.ops4j.pax.cdi.api.OsgiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Neutron Northbound REST APIs for L2 gateway.
 */
@Singleton
@Path("/l2-gateways")
public final class NeutronL2gatewayNorthbound
        extends AbstractNeutronNorthbound<NeutronL2gateway, NeutronL2gatewayRequest, INeutronL2gatewayCRUD> {

    private static final Logger LOG = LoggerFactory.getLogger(NeutronL2gatewayNorthbound.class);

    private static final String RESOURCE_NAME = "L2gateway";

    @Inject
    public NeutronL2gatewayNorthbound(@OsgiService INeutronL2gatewayCRUD neutronCRUD) {
        super(neutronCRUD);
    }

    @Override
    protected String getResourceName() {
        return RESOURCE_NAME;
    }

    /**
     * Creates L2gateway.
     * @param input l2gateway attributes
     * @return success or error code
     */
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_CREATED, condition = "Created"),
            @ResponseCode(code = HttpURLConnection.HTTP_BAD_REQUEST, condition = "Bad Request"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAUTHORIZED, condition = "Unauthorized"),
            @ResponseCode(code = HttpURLConnection.HTTP_FORBIDDEN, condition = "Forbidden"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
            @ResponseCode(code = HttpURLConnection.HTTP_CONFLICT, condition = "Conflict"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_IMPLEMENTED, condition = "Not Implemented"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response createL2gateway(final NeutronL2gatewayRequest input) {
        LOG.debug("CreateL2gateway     NeutronL2gatewayRequest");
        return create(input);
    }

    /**
     * Returns a list of all L2gateways.
     */
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAUTHORIZED, condition = "Unauthorized"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_IMPLEMENTED, condition = "Not Implemented"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response listL2gateways(
            // return fields
            @QueryParam("fields") List<String> fields,
            // OpenStack L2gateway attributes
            @QueryParam("id") String queryID,
            @QueryParam("name") String queryName,
            @QueryParam("tenant_id") String queryTenantID,
            @QueryParam("devices") String queryNeutronL2gatewayDevice,
            // pagination
            @QueryParam("limit") String limit,
            @QueryParam("marker") String marker,
            @QueryParam("page_reverse") String pageReverse
    // sorting not supported
    ) {
        INeutronL2gatewayCRUD l2gatewayInterface = getNeutronCRUD();
        List<NeutronL2gateway> allL2gateways = l2gatewayInterface.getAll();
        List<NeutronL2gateway> ans = new ArrayList<>();
        for (NeutronL2gateway l2gateway : allL2gateways) {
            if ((queryID == null || queryID.equals(l2gateway.getID()))
                    && (queryName == null || queryName.equals(l2gateway.getName()))
                    && (queryTenantID == null || queryTenantID.equals(l2gateway.getTenantID()))) {
                if (fields.size() > 0) {
                    ans.add(l2gateway.extractFields(fields));
                } else {
                    ans.add(l2gateway);
                }
            }
        }
        //TODO: apply pagination to results
        return Response.status(HttpURLConnection.HTTP_OK).entity(new NeutronL2gatewayRequest(ans)).build();
    }

    /**
     * Returns a specific L2gateway.
     * @param l2gatewayID requested l2gateway uuid
     * @param fields l2gateway attributes
     * @return l2gateway details or error.
     */
    @Path("{l2gatewayID}")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAUTHORIZED, condition = "Unauthorized"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_IMPLEMENTED, condition = "Not Implemented"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response showL2gateway(@PathParam("l2gatewayID") String l2gatewayID,
            // return fields
            @QueryParam("fields") List<String> fields) {
        return show(l2gatewayID, fields);
    }

    /**
     * Deletes a L2gateway.
     * @param l2gatewayID l2gateway uuid which should be deleted
     * @return success or error code
     * */

    @Path("{l2gatewayID}")
    @DELETE
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_NO_CONTENT, condition = "No Content"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response deleteL2gateway(@PathParam("l2gatewayID") String l2gatewayID) {
        return delete(l2gatewayID);
    }

    /**
     * Updates a L2gateway.
     * @param l2gatewayID gateway ID that needs to be modified
     * @param input gateway attributes
     * @return status
     * */
    @Path("{l2gatewayID}")
    @PUT
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response updateL2gateway(@PathParam("l2gatewayID") String l2gatewayID, NeutronL2gatewayRequest input) {
        return update(l2gatewayID, input);
    }
}
