/*
 * Copyright (c) 2017 Ericsson India Global Services Pvt Ltd. and others. All rights reserved.
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
import org.opendaylight.neutron.spi.INeutronTrunkCRUD;
import org.opendaylight.neutron.spi.NeutronTrunk;
import org.ops4j.pax.cdi.api.OsgiService;

@Singleton
@Path("/trunks")
public final class NeutronTrunksNorthbound
        extends AbstractNeutronNorthbound<NeutronTrunk, NeutronTrunkRequest, INeutronTrunkCRUD> {

    private static final String RESOURCE_NAME = "Trunk";

    @Inject
    public NeutronTrunksNorthbound(@OsgiService INeutronTrunkCRUD neutronCRUD) {
        super(neutronCRUD);
    }

    @Override
    protected String getResourceName() {
        return RESOURCE_NAME;
    }

    /**
     * Returns a list of all Trunks.
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
            // OpenStack trunk attributes
            @QueryParam("id") String queryUUID,
            @QueryParam("tenant_id") String queryTenantID,
            @QueryParam("status") String queryStatus,
            @QueryParam("name") String queryName,
            // pagination
            @QueryParam("limit") String limit,
            @QueryParam("marker") String marker,
            @QueryParam("page_reverse") String pageReverse) {
        INeutronTrunkCRUD trunkInterface = getNeutronCRUD();
        List<NeutronTrunk> ans = new ArrayList<>();
        for (NeutronTrunk nsg : trunkInterface.getAll()) {
            if ((queryUUID == null || queryUUID.equals(nsg.getID()))
                    && (queryTenantID == null || queryTenantID.equals(nsg.getTenantID()))
                    && (queryStatus == null || queryStatus.equals(nsg.getStatus()))
                    && (queryName == null || queryName.equals(nsg.getName()))) {
                if (fields.size() > 0) {
                    ans.add(nsg.extractFields(fields));
                } else {
                    ans.add(nsg);
                }
            }
        }
        return Response.status(HttpURLConnection.HTTP_OK).entity(new NeutronTrunkRequest(ans)).build();
    }

    /**
     * Returns a specific Trunk.
     */
    @Path("{trunkUUID}")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAUTHORIZED, condition = "Unauthorized"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_IMPLEMENTED, condition = "Not Implemented"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response showTrunk(@PathParam("trunkUUID") String trunkUUID,
            @QueryParam("fields") List<String> fields) {
        return show(trunkUUID, fields);
    }

    /**
     * Creates new Trunk.
     */
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_CREATED, condition = "Created"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response createTrunks(final NeutronTrunkRequest input) {
        return create(input);
    }

    /**
     * Updates a Trunk.
     */
    @Path("{trunkUUID}")
    @PUT
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response updateTrunk(@PathParam("trunkUUID") String trunkUUID,
            final NeutronTrunkRequest input) {
        return update(trunkUUID, input);
    }

    /**
     * Deletes a Trunk.
     */
    @Path("{trunkUUID}")
    @DELETE
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_NO_CONTENT, condition = "No Content"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response deleteTrunk(@PathParam("trunkUUID") String trunkUUID) {
        return delete(trunkUUID);
    }
}
