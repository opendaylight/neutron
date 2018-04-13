/*
 * Copyright (c) 2013, 2015 IBM Corporation and others.  All rights reserved.
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
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.codehaus.enunciate.jaxrs.ResponseCode;
import org.codehaus.enunciate.jaxrs.StatusCodes;
import org.codehaus.enunciate.jaxrs.TypeHint;
import org.opendaylight.neutron.spi.INeutronCRUD;
import org.opendaylight.neutron.spi.INeutronNetworkCRUD;
import org.opendaylight.neutron.spi.NeutronNetwork;

/**
 * Neutron Northbound REST APIs for Network.
 */
@Singleton
@Path("/networks")
public final class NeutronNetworksNorthbound
        extends AbstractNeutronNorthbound<NeutronNetwork, NeutronNetworkRequest, INeutronNetworkCRUD> {

    private static final String RESOURCE_NAME = "Network";

    @Inject
    public NeutronNetworksNorthbound(INeutronNetworkCRUD neutronNetworkCRUD) {
        super(neutronNetworkCRUD);
    }

    @Context
    UriInfo uriInfo;

    @Override
    protected String getResourceName() {
        return RESOURCE_NAME;
    }

    /**
     * Returns a list of all Networks.
     */
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    //@TypeHint(OpenStackNetworks.class)
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAUTHORIZED, condition = "Unauthorized"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_IMPLEMENTED, condition = "Not Implemented"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response listNetworks(
            // return fields
            @QueryParam("fields") List<String> fields,
            // note: openstack isn't clear about filtering on lists, so we aren't handling them
            @QueryParam("id") String queryID,
            @QueryParam("name") String queryName,
            @QueryParam("admin_state_up") String queryAdminStateUp,
            @QueryParam("status") String queryStatus,
            @QueryParam("shared") String queryShared,
            @QueryParam("tenant_id") String queryTenantID,
            @QueryParam("router_external") String queryRouterExternal,
            @QueryParam("provider_network_type") String queryProviderNetworkType,
            @QueryParam("provider_physical_network") String queryProviderPhysicalNetwork,
            @QueryParam("provider_segmentation_id") String queryProviderSegmentationID,
            @QueryParam("qos_policy_id") String queryQosPolicyId,
            // linkTitle
            @QueryParam("limit") Integer limit,
            @QueryParam("marker") String marker,
            @DefaultValue("false") @QueryParam("page_reverse") Boolean pageReverse
    // sorting not supported
    ) {
        INeutronCRUD<NeutronNetwork> networkInterface = getNeutronCRUD();
        List<NeutronNetwork> allNetworks = networkInterface.getAll();
        List<NeutronNetwork> ans = new ArrayList<>();
        for (NeutronNetwork network : allNetworks) {
            //match filters: TODO provider extension
            Boolean adminStateUp = queryAdminStateUp != null ? Boolean.valueOf(queryAdminStateUp) : null;
            Boolean shared = queryShared != null ? Boolean.valueOf(queryShared) : null;
            Boolean routerExternal = queryRouterExternal != null ? Boolean.valueOf(queryRouterExternal) : null;
            if ((queryID == null || queryID.equals(network.getID()))
                    && (queryName == null || queryName.equals(network.getName()))
                    && (adminStateUp == null || adminStateUp.booleanValue() == network.isAdminStateUp())
                    && (queryStatus == null || queryStatus.equals(network.getStatus()))
                    && (shared == null || shared.booleanValue() == network.isShared())
                    && (routerExternal == null || routerExternal.booleanValue() == network.isRouterExternal())
                    && (queryTenantID == null || queryTenantID.equals(network.getTenantID()))
                    && (queryQosPolicyId == null || queryQosPolicyId.equals(network.getQosPolicyId()))) {
                if (fields.size() > 0) {
                    ans.add(network.extractFields(fields));
                } else {
                    ans.add(network);
                }
            }
        }

        if (limit != null && ans.size() > 1) {
            // Return a paginated request
            NeutronNetworkRequest request = (NeutronNetworkRequest) PaginatedRequestFactory.createRequest(limit, marker,
                    pageReverse, uriInfo, ans, NeutronNetwork.class);
            return Response.status(HttpURLConnection.HTTP_OK).entity(request).build();
        }

        return Response.status(HttpURLConnection.HTTP_OK).entity(new NeutronNetworkRequest(ans)).build();
    }

    /**
     * Returns a specific Network.
     */
    @Path("{netUUID}")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    //@TypeHint(OpenStackNetworks.class)
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAUTHORIZED, condition = "Unauthorized"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_IMPLEMENTED, condition = "Not Implemented"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response showNetwork(@PathParam("netUUID") String netUUID,
            // return fields
            @QueryParam("fields") List<String> fields) {
        return show(netUUID, fields);
    }

    /**
     * Creates new Networks.
     */
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    @TypeHint(NeutronNetwork.class)
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_CREATED, condition = "Created"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response createNetworks(final NeutronNetworkRequest input) {
        return create(input);
    }

    @Override
    protected void updateDelta(String uuid, NeutronNetwork delta, NeutronNetwork original) {
        /*
         *  note: what we get appears to not be a delta but
         * rather an incomplete updated object.  So we need to set
         * the ID to complete the object and then send that down
         * for folks to check
         */

        delta.setID(uuid);
        delta.setTenantID(original.getTenantID());
    }

    /**
     * Updates a Network.
     */
    @Path("{netUUID}")
    @PUT
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    //@TypeHint(OpenStackNetworks.class)
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response updateNetwork(@PathParam("netUUID") String netUUID, final NeutronNetworkRequest input) {
        return update(netUUID, input);
    }

    /**
     * Deletes a Network.
     */
    @Path("{netUUID}")
    @DELETE
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_NO_CONTENT, condition = "No Content"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response deleteNetwork(@PathParam("netUUID") String netUUID) {
        return delete(netUUID);
    }
}
