/*
 * Copyright (c) 2020 Ericsson India Global Services Pvt Ltd. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.northbound.api;

import com.webcohesion.enunciate.metadata.rs.ResponseCode;
import com.webcohesion.enunciate.metadata.rs.StatusCodes;
import com.webcohesion.enunciate.metadata.rs.TypeHint;
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
import org.apache.aries.blueprint.annotation.service.Reference;
import org.opendaylight.neutron.spi.INeutronBgpvpnRouterAssociationCRUD;
import org.opendaylight.neutron.spi.NeutronBgpvpnRouterAssociation;

/**
 * Neutron Northbound REST APIs for BgpvpnRouterAssociation.<br>
 * This class provides REST APIs for managing neutron BgpvpnRouterAssociations
 *
 * <br>
 * <br>
 * Authentication scheme : <b>HTTP Basic</b><br>
 * Authentication realm : <b>opendaylight</b><br>
 * Transport : <b>HTTP and HTTPS</b><br>
 * <br>
 * HTTPS Authentication is disabled by default. Administrator can enable it in
 * tomcat-server.xml after adding a proper keystore / SSL certificate from a
 * trusted authority.<br>
 * More info :
 * http://tomcat.apache.org/tomcat-7.0-doc/ssl-howto.html#Configuration
 *
 */
@Singleton
@Path("/bgpvpn/routerassociations")
public final class NeutronBgpvpnRouterAssociationsNorthbound extends
        AbstractNeutronNorthbound<NeutronBgpvpnRouterAssociation,
        NeutronBgpvpnRouterAssociationRequest, INeutronBgpvpnRouterAssociationCRUD> {

    @Context
    UriInfo uriInfo;

    @Inject
    public NeutronBgpvpnRouterAssociationsNorthbound(@Reference INeutronBgpvpnRouterAssociationCRUD neutronCRUD) {
        super(neutronCRUD);
    }

    private static final String RESOURCE_NAME = "Bgpvpn Router Associations";

    @Override
    protected String getResourceName() {
        return RESOURCE_NAME;
    }

    /**
     * Returns a list of all BgpvpnRouterAssociations.
     */

    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @StatusCodes({
        @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
        @ResponseCode(code = HttpURLConnection.HTTP_UNAUTHORIZED, condition = "Unauthorized"),
        @ResponseCode(code = HttpURLConnection.HTTP_NOT_IMPLEMENTED, condition = "Not Implemented"),
        @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available")
    })
    public Response listBgpvpnRouterAssociations(
            // return fields
            @QueryParam("fields") List<String> fields,

            @QueryParam("id") String queryID,
            @QueryParam("bgpvpn_id") String queryBgpvpnId,
            @QueryParam("router_id") String queryRouterId,
            // pagination
            @QueryParam("limit") Integer limit,
            @QueryParam("marker") String marker,
            @DefaultValue("false") @QueryParam("page_reverse") Boolean pageReverse
    ) {
        INeutronBgpvpnRouterAssociationCRUD neutronBgpvpnRouterAssociation = getNeutronCRUD();
        List<NeutronBgpvpnRouterAssociation> allBgpvpnRouteAssos = neutronBgpvpnRouterAssociation.getAll();
        List<NeutronBgpvpnRouterAssociation> ans = new ArrayList<>();
        for (NeutronBgpvpnRouterAssociation bgpvpnRouteAsso : allBgpvpnRouteAssos) {
            //match filters:
            if ((queryID == null || queryID.equals(bgpvpnRouteAsso.getID()))
                    && (queryBgpvpnId == null || queryBgpvpnId.equals(bgpvpnRouteAsso.getBgpvpnId()))
                    && (queryRouterId == null || queryRouterId.equals(bgpvpnRouteAsso.getRouterId()))) {
                if (fields.size() > 0) {
                    ans.add(bgpvpnRouteAsso.extractFields(fields));
                } else {
                    ans.add(bgpvpnRouteAsso);
                }
            }
        }

        if (limit != null && ans.size() > 1) {
            // Return a paginated request
            NeutronBgpvpnRouterAssociationRequest request = (NeutronBgpvpnRouterAssociationRequest)
                    PaginatedRequestFactory.createRequest(limit, marker, pageReverse, uriInfo, ans,
                            NeutronBgpvpnRouterAssociation.class);
            return Response.status(HttpURLConnection.HTTP_OK).entity(request).build();
        }
        return Response.status(HttpURLConnection.HTTP_OK).entity(new NeutronBgpvpnRouterAssociationRequest(ans))
                .build();
    }

    /**
     * Returns a specific BgpvpnRouterAssociation.
     */

    @Path("{routerassociationUUID}")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @StatusCodes({
        @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
        @ResponseCode(code = HttpURLConnection.HTTP_UNAUTHORIZED, condition = "Unauthorized"),
        @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
        @ResponseCode(code = HttpURLConnection.HTTP_NOT_IMPLEMENTED, condition = "Not Implemented"),
        @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available")
    })
    public Response showBgpvpnRouterAssociation(
            @PathParam("routerassociationUUID") String bgpvpnRouterAssociationUUID,
            // return fields
            @QueryParam("fields") List<String> fields) {
        return show(bgpvpnRouterAssociationUUID, fields);
    }

    /**
     * Creates new BgpvpnRouterAssociations.
     */
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    @TypeHint(NeutronBgpvpnRouterAssociation.class)
    @StatusCodes({
        @ResponseCode(code = HttpURLConnection.HTTP_CREATED, condition = "Created"),
        @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available")
    })
    public Response createBgpvpnRouterAssociations(final NeutronBgpvpnRouterAssociationRequest input) {
        return create(input);
    }

    /**
     * Updates a BgpvpnRouterAssociation.
     */

    @Path("{routerassociationUUID}")
    @PUT
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    @StatusCodes({
        @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
        @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
        @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available")
    })
    public Response updateBgpvpnRouterAssociation(
            @PathParam("routerassociationUUID") String bgpvpnRouterAssociationUUID,
            final NeutronBgpvpnRouterAssociationRequest input) {
        return update(bgpvpnRouterAssociationUUID, input);
    }

    /**
     * Deletes a BgpvpnRouterAssociation.
     */

    @Path("{routerassociationUUID}")
    @DELETE
    @StatusCodes({
        @ResponseCode(code = HttpURLConnection.HTTP_NO_CONTENT, condition = "No Content"),
        @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
        @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available")
    })
    public Response deleteBgpvpn(@PathParam("routerassociationUUID") String bgpvpnRouterAssociationUUID) {
        return delete(bgpvpnRouterAssociationUUID);
    }
}
