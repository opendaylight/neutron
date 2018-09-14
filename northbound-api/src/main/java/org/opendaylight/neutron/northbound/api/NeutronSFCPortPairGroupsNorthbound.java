/*
 * Copyright (c) 2016 Brocade Communications Systems, Inc. and others.  All rights reserved.
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
import org.apache.aries.blueprint.annotation.service.Reference;
import org.codehaus.enunciate.jaxrs.ResponseCode;
import org.codehaus.enunciate.jaxrs.StatusCodes;
import org.opendaylight.neutron.spi.INeutronSFCPortPairGroupCRUD;
import org.opendaylight.neutron.spi.NeutronSFCPortPairGroup;

/**
 * Neutron Northbound REST APIs for OpenStack SFC Port Pair.
 */
@Singleton
@Path("/sfc/portpairgroups")
public final class NeutronSFCPortPairGroupsNorthbound extends AbstractNeutronNorthbound<NeutronSFCPortPairGroup,
        NeutronSFCPortPairGroupRequest, INeutronSFCPortPairGroupCRUD> {

    private static final String RESOURCE_NAME = "Sfc Port Pair Group";

    @Inject
    public NeutronSFCPortPairGroupsNorthbound(@Reference INeutronSFCPortPairGroupCRUD neutronCRUD) {
        super(neutronCRUD);
    }

    @Override
    protected String getResourceName() {
        return RESOURCE_NAME;
    }

    /**
     * Returns a list of all SFC Port Pair Groups.
     */
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAUTHORIZED, condition = "Unauthorized"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_IMPLEMENTED, condition = "Not Implemented"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response listSFCPortPairGroups(
            // return fields
            @QueryParam("fields") List<String> fields,
            @QueryParam("id") String queryID,
            @QueryParam("name") String queryName,
            @QueryParam("tenant_id") String queryTenantID,
            @QueryParam("port_pairs") List<String> queryPortPairsUUID) {
        INeutronSFCPortPairGroupCRUD sfcPortPairGroupInterface = getNeutronCRUD();
        List<NeutronSFCPortPairGroup> allSFCPortPairGroup = sfcPortPairGroupInterface.getAll();
        List<NeutronSFCPortPairGroup> ans = new ArrayList<>();
        for (NeutronSFCPortPairGroup sfcPortPairGroup : allSFCPortPairGroup) {
            if ((queryID == null || queryID.equals(sfcPortPairGroup.getID()))
                    && (queryName == null || queryName.equals(sfcPortPairGroup.getName()))
                    && (queryTenantID == null || queryTenantID.equals(sfcPortPairGroup.getTenantID()))) {
                if (fields.size() > 0) {
                    ans.add(sfcPortPairGroup.extractFields(fields));
                } else {
                    ans.add(sfcPortPairGroup);
                }
            }
        }

        return Response.status(HttpURLConnection.HTTP_OK).entity(new NeutronSFCPortPairGroupRequest(ans)).build();

    }

    /**
     * Returns a specific SFC Port Pair Group.
     */
    @Path("{portPairGroupUUID}")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAUTHORIZED, condition = "Unauthorized"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_IMPLEMENTED, condition = "Not Implemented"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response showSFCPortPairGroup(@PathParam("portPairGroupUUID") String sfcPortPairGroupUUID,
            // return fields
            @QueryParam("fields") List<String> fields) {
        return show(sfcPortPairGroupUUID, fields);
    }

    /**
     * Creates new SFC Port Pair Group.
     */
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_CREATED, condition = "Created"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response createSFCPortPairGroup(final NeutronSFCPortPairGroupRequest input) {
        return create(input);
    }

    @Override
    protected void updateDelta(String uuid, NeutronSFCPortPairGroup delta, NeutronSFCPortPairGroup original) {
        /*
         * Note: what we get appears to not be a delta but
         * rather an incomplete updated object.  So we need to set
         * the ID to complete the object and then send that down
         * for folks to check
         */
        delta.setID(uuid);
        delta.setTenantID(original.getTenantID());
    }

    /**
     * Updates an existing SFC Port Pair Group.
     */
    @Path("{portPairGroupUUID}")
    @PUT
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response updateSFCPortPairGroup(@PathParam("portPairGroupUUID") String sfcPortPairGroupUUID,
            final NeutronSFCPortPairGroupRequest input) {
        return update(sfcPortPairGroupUUID, input);
    }

    /**
     * Deletes the SFC Port Pair Group.
     */
    @Path("{portPairGroupUUID}")
    @DELETE
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_NO_CONTENT, condition = "No Content"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response deleteSFCPortPairGroup(@PathParam("portPairGroupUUID") String sfcPortPairGroupUUID) {
        return delete(sfcPortPairGroupUUID);
    }
}
