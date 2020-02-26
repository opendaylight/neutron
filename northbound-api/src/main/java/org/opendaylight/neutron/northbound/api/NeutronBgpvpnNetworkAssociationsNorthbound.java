/*
 * Copyright (c) 2019 Ericsson India Global Services Pvt Ltd. and others. All rights reserved.
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

import org.opendaylight.neutron.spi.INeutronBgpvpnNetworkAssociationCRUD;
import org.opendaylight.neutron.spi.NeutronBgpvpnNetworkAssociation;

/**
 * Neutron Northbound REST APIs for BgpvpnNetworkAssociation.<br>
 * This class provides REST APIs for managing neutron BgpvpnNetworkAssociations
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

@Path("/bgpvpn/networkassociations")
public class NeutronBgpvpnNetworkAssociationsNorthbound
        extends AbstractNeutronNorthbound<NeutronBgpvpnNetworkAssociation ,
        NeutronBgpvpnNetworkAssociationRequest, INeutronBgpvpnNetworkAssociationCRUD> {

    @Context
    UriInfo uriInfo;

    private static final String RESOURCE_NAME = "Bgpvpn Network Associations";


    public NeutronBgpvpnNetworkAssociationsNorthbound(INeutronBgpvpnNetworkAssociationCRUD neutronCRUD) {
        super(neutronCRUD);
    }

    @Override
    protected String getResourceName() {
        return RESOURCE_NAME;
    }

    /**
     * Returns a list of all BgpvpnNetworkAssociations.
     */

    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAUTHORIZED, condition = "Unauthorized"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_IMPLEMENTED, condition = "Not Implemented"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response listBgpvpnNetworkAssociations(
            // return fields
            @QueryParam("fields") List<String> fields,
            @QueryParam("id") String queryID,
            @QueryParam("bgpvpn_id") String queryBgpvpnId,
            @QueryParam("network_id") String queryNetworkId,
            // pagination
            @QueryParam("limit") Integer limit,
            @QueryParam("marker") String marker,
            @DefaultValue("false") @QueryParam("page_reverse") Boolean pageReverse
    ) {
        INeutronBgpvpnNetworkAssociationCRUD bgpvpnNetAssoInterface = getNeutronCRUD();
        List<NeutronBgpvpnNetworkAssociation> allBgpvpnNetAssos = bgpvpnNetAssoInterface.getAll();
        List<NeutronBgpvpnNetworkAssociation> ans = new ArrayList<>();
        for (NeutronBgpvpnNetworkAssociation bgpvpnNetAsso : allBgpvpnNetAssos) {
            //match filters:
            if ((queryID == null || queryID.equals(bgpvpnNetAsso.getID()))
                    && (queryBgpvpnId == null || queryBgpvpnId.equals(bgpvpnNetAsso.getBgpvpnId()))
                    && (queryNetworkId == null || queryNetworkId.equals(bgpvpnNetAsso.getNetworkIds()))) {
                if (fields.size() > 0) {
                    ans.add(bgpvpnNetAsso.extractFields(fields));
                } else {
                    ans.add(bgpvpnNetAsso);
                }
            }
        }

        if (limit != null && ans.size() > 1) {
            // Return a paginated request
            NeutronBgpvpnNetworkAssociationRequest request = (NeutronBgpvpnNetworkAssociationRequest)
                    PaginatedRequestFactory.createRequest(limit, marker, pageReverse, uriInfo, ans,
                            NeutronBgpvpnNetworkAssociation.class);
            return Response.status(HttpURLConnection.HTTP_OK).entity(request).build();
        }
        return Response.status(HttpURLConnection.HTTP_OK).entity(new NeutronBgpvpnNetworkAssociationRequest(ans))
                .build();
    }

    /**
     * Returns a specific BgpvpnNetworkAssociation.
     */

    @Path("{networkassociationUUID}")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAUTHORIZED, condition = "Unauthorized"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_IMPLEMENTED, condition = "Not Implemented"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response showBgpvpnNetworkAssociation(
            @PathParam("networkassociationUUID") String bgpvpnNetworkAssociationUUID,
            // return fields
            @QueryParam("fields") List<String> fields) {
        return show(bgpvpnNetworkAssociationUUID, fields);
    }

    /**
     * Creates new BgpvpnNetworkAssociations.
     */
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    @TypeHint(NeutronBgpvpnNetworkAssociation.class)
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_CREATED, condition = "Created"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response createBgpvpnNetworkAssociations(final NeutronBgpvpnNetworkAssociationRequest input) {
        return create(input);
    }

    /**
     * Updates a BgpvpnNetworkAssociation.
     */

    @Path("{networkassociationUUID}")
    @PUT
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response updateBgpvpnNetworkAssociation(@PathParam("networkassociationUUID")
                                                   String bgpvpnNetworkAssociationUUID,
                                                   final NeutronBgpvpnNetworkAssociationRequest input) {
        return update(bgpvpnNetworkAssociationUUID, input);
    }

    /**
     * Deletes a Bgpvpn.
     */

    @Path("{networkassociationUUID}")
    @DELETE
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_NO_CONTENT, condition = "No Content"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response deleteBgpvpnNetworkAssociation(
            @PathParam("networkassociationUUID") String bgpvpnNetworkAssociationUUID) {
        return delete(bgpvpnNetworkAssociationUUID);
    }

}
