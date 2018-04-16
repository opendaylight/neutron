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
import org.opendaylight.neutron.spi.INeutronRouterCRUD;
import org.opendaylight.neutron.spi.NeutronRouter;
import org.opendaylight.neutron.spi.NeutronRouterInterface;
import org.ops4j.pax.cdi.api.OsgiService;

/**
 * Neutron Northbound REST APIs for managing neutron routers.
 */
@Singleton
@Path("/routers")
public final class NeutronRoutersNorthbound
        extends AbstractNeutronNorthbound<NeutronRouter, NeutronRouterRequest, INeutronRouterCRUD> {

    private static final String RESOURCE_NAME = "Router";

    @Inject
    public NeutronRoutersNorthbound(@OsgiService INeutronRouterCRUD neutronCRUD) {
        super(neutronCRUD);
    }

    @Override
    protected String getResourceName() {
        return RESOURCE_NAME;
    }

    /**
     * Returns a list of all Routers.
     */
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    //@TypeHint(OpenStackRouters.class)
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAUTHORIZED, condition = "Unauthorized"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_IMPLEMENTED, condition = "Not Implemented"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response listRouters(
            // return fields
            @QueryParam("fields") List<String> fields,
            // note: openstack isn't clear about filtering on lists, so we aren't handling them
            @QueryParam("id") String queryID,
            @QueryParam("name") String queryName,
            @QueryParam("admin_state_up") Boolean queryAdminStateUp,
            @QueryParam("status") String queryStatus,
            @QueryParam("tenant_id") String queryTenantID,
            @QueryParam("external_gateway_info") String queryExternalGatewayInfo,
            // pagination
            @QueryParam("limit") String limit,
            @QueryParam("marker") String marker,
            @QueryParam("page_reverse") String pageReverse
    // sorting not supported
    ) {
        INeutronRouterCRUD routerInterface = getNeutronCRUD();
        if (routerInterface == null) {
            throw new ServiceUnavailableException(serviceUnavailable());
        }
        List<NeutronRouter> allRouters = routerInterface.getAll();
        List<NeutronRouter> ans = new ArrayList<>();
        for (NeutronRouter router : allRouters) {
            if ((queryID == null || queryID.equals(router.getID()))
                    && (queryName == null || queryName.equals(router.getName()))
                    && (queryAdminStateUp == null || queryAdminStateUp.equals(router.getAdminStateUp()))
                    && (queryStatus == null || queryStatus.equals(router.getStatus()))
                    && (queryTenantID == null || queryTenantID.equals(router.getTenantID()))) {
                if (fields.size() > 0) {
                    ans.add(router.extractFields(fields));
                } else {
                    ans.add(router);
                }
            }
        }
        //TODO: apply pagination to results
        return Response.status(HttpURLConnection.HTTP_OK).entity(new NeutronRouterRequest(ans)).build();
    }

    /**
     * Returns a specific Router.
     */
    @Path("{routerUUID}")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    //@TypeHint(OpenStackRouters.class)
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAUTHORIZED, condition = "Unauthorized"),
            @ResponseCode(code = HttpURLConnection.HTTP_FORBIDDEN, condition = "Forbidden"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_IMPLEMENTED, condition = "Not Implemented"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response showRouter(@PathParam("routerUUID") String routerUUID,
            // return fields
            @QueryParam("fields") List<String> fields) {
        return show(routerUUID, fields);
    }

    /**
     * Creates new Routers.
     */
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    //@TypeHint(OpenStackRouters.class)
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_CREATED, condition = "Created"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response createRouters(final NeutronRouterRequest input) {
        return create(input);
    }

    @Override
    protected void updateDelta(String uuid, NeutronRouter delta, NeutronRouter original) {
        delta.setID(uuid);
        delta.setTenantID(original.getTenantID());
    }

    /**
     * Updates a Router.
     */
    @Path("{routerUUID}")
    @PUT
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    //@TypeHint(OpenStackRouters.class)
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response updateRouter(@PathParam("routerUUID") String routerUUID, NeutronRouterRequest input) {
        return update(routerUUID, input);
    }

    /**
     * Deletes a Router.
     */
    @Path("{routerUUID}")
    @DELETE
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_NO_CONTENT, condition = "No Content"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response deleteRouter(@PathParam("routerUUID") String routerUUID) {
        return delete(routerUUID);
    }

    /**
     * Adds an interface to a router.
     */
    @Path("{routerUUID}/add_router_interface")
    @PUT
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    //@TypeHint(OpenStackRouterInterfaces.class)
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful") })
    public Response addRouterInterface(@PathParam("routerUUID") String routerUUID, NeutronRouterInterface input) {
        // Do nothing. Keep this interface for compatibility
        return Response.status(HttpURLConnection.HTTP_OK).entity(input).build();
    }

    @Path("{routerUUID}/remove_router_interface")
    @PUT
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    //@TypeHint(OpenStackRouterInterfaces.class)
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful") })
    public Response removeRouterInterface(@PathParam("routerUUID") String routerUUID, NeutronRouterInterface input) {
        // Do nothing. Keep this interface for compatibility
        return Response.status(HttpURLConnection.HTTP_OK).entity(input).build();
    }
}
