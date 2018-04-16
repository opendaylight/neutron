/*
 * Copyright (c) 2015 Ericsson India Global Services Pvt Ltd. and others. All rights reserved.
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
import org.opendaylight.neutron.spi.INeutronBgpvpnCRUD;
import org.opendaylight.neutron.spi.NeutronBgpvpn;
import org.ops4j.pax.cdi.api.OsgiService;

/**
 * Neutron Northbound REST APIs for Bgpvpn.
 */
@Singleton
@Path("/bgpvpns")
public final class NeutronBgpvpnsNorthbound
        extends AbstractNeutronNorthbound<NeutronBgpvpn, NeutronBgpvpnRequest, INeutronBgpvpnCRUD> {

    private static final String RESOURCE_NAME = "Bgpvpn";

    @Context
    UriInfo uriInfo;

    @Inject
    public NeutronBgpvpnsNorthbound(@OsgiService INeutronBgpvpnCRUD neutronCRUD) {
        super(neutronCRUD);
    }

    @Override
    protected String getResourceName() {
        return RESOURCE_NAME;
    }

    /**
     * Returns a list of all Bgpvpns.
     */
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    //@TypeHint(OpenStackBgpvpns.class)
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAUTHORIZED, condition = "Unauthorized"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_IMPLEMENTED, condition = "Not Implemented"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response listBgpvpns(
            // return fields
            @QueryParam("fields") List<String> fields,
            // note: openstack isn't clear about filtering on lists, so we aren't handling them
            @QueryParam("id") String queryID,
            @QueryParam("name") String queryName,
            @QueryParam("admin_state_up") String queryAdminStateUp,
            @QueryParam("status") String queryStatus,
            @QueryParam("tenant_id") String queryTenantID,
            @QueryParam("type") String queryType,
            @QueryParam("auto_aggregate") String queryAutoAggregate,
            // pagination
            @QueryParam("limit") Integer limit,
            @QueryParam("marker") String marker,
            @DefaultValue("false") @QueryParam("page_reverse") Boolean pageReverse
    // sorting not supported
    ) {
        INeutronBgpvpnCRUD bgpvpnInterface = getNeutronCRUD();
        List<NeutronBgpvpn> allBgpvpns = bgpvpnInterface.getAll();
        List<NeutronBgpvpn> ans = new ArrayList<>();
        for (NeutronBgpvpn bgpvpn : allBgpvpns) {
            //match filters: TODO provider extension
            Boolean adminStateUp = queryAdminStateUp != null ? Boolean.valueOf(queryAdminStateUp) : null;
            Boolean autoAggregate = queryAutoAggregate != null ? Boolean.valueOf(queryAutoAggregate) : null;
            if ((queryID == null || queryID.equals(bgpvpn.getID()))
                    && (queryName == null || queryName.equals(bgpvpn.getName()))
                    && (adminStateUp == null || adminStateUp.booleanValue() == bgpvpn.isAdminStateUp())
                    && (queryStatus == null || queryStatus.equals(bgpvpn.getStatus()))
                    && (autoAggregate == null || autoAggregate.booleanValue() == bgpvpn.isAutoAggregate())
                    && (queryTenantID == null || queryTenantID.equals(bgpvpn.getTenantID()))) {
                if (fields.size() > 0) {
                    ans.add(bgpvpn.extractFields(fields));
                } else {
                    ans.add(bgpvpn);
                }
            }
        }

        if (limit != null && ans.size() > 1) {
            // Return a paginated request
            NeutronBgpvpnRequest request = (NeutronBgpvpnRequest) PaginatedRequestFactory.createRequest(limit, marker,
                    pageReverse, uriInfo, ans, NeutronBgpvpn.class);
            return Response.status(HttpURLConnection.HTTP_OK).entity(request).build();
        }

        return Response.status(HttpURLConnection.HTTP_OK).entity(new NeutronBgpvpnRequest(ans)).build();

    }

    /**
     * Returns a specific Bgpvpn.
     */
    @Path("{bgpvpnUUID}")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    //@TypeHint(OpenStackBgpvpns.class)
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAUTHORIZED, condition = "Unauthorized"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_IMPLEMENTED, condition = "Not Implemented"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response showBgpvpn(@PathParam("bgpvpnUUID") String bgpvpnUUID,
            // return fields
            @QueryParam("fields") List<String> fields) {
        return show(bgpvpnUUID, fields);
    }

    /**
     * Creates new Bgpvpns.
     */
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    @TypeHint(NeutronBgpvpn.class)
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_CREATED, condition = "Created"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response createBgpvpns(final NeutronBgpvpnRequest input) {
        return create(input);
    }

    /**
     * Updates a Bgpvpn.
     */
    @Override
    protected void updateDelta(String uuid, NeutronBgpvpn delta, NeutronBgpvpn original) {
        //Fill in defaults if they're missing in update
        delta.initDefaults();
        delta.setID(uuid);
        delta.setTenantID(original.getTenantID());
    }

    @Path("{bgpvpnUUID}")
    @PUT
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    //@TypeHint(OpenStackBgpvpns.class)
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response updateBgpvpn(@PathParam("bgpvpnUUID") String bgpvpnUUID, final NeutronBgpvpnRequest input) {
        return update(bgpvpnUUID, input);
    }

    /**
     * Deletes a Bgpvpn.
     */
    @Path("{bgpvpnUUID}")
    @DELETE
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_NO_CONTENT, condition = "No Content"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response deleteBgpvpn(@PathParam("bgpvpnUUID") String bgpvpnUUID) {
        return delete(bgpvpnUUID);
    }
}
