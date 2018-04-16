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
import org.opendaylight.neutron.spi.INeutronFloatingIpCRUD;
import org.opendaylight.neutron.spi.NeutronFloatingIp;
import org.ops4j.pax.cdi.api.OsgiService;

/**
 * Neutron Northbound REST APIs for managing Neutron Floating IPs.
 */
@Singleton
@Path("/floatingips")
public final class NeutronFloatingIpsNorthbound
        extends AbstractNeutronNorthbound<NeutronFloatingIp, NeutronFloatingIpRequest, INeutronFloatingIpCRUD> {

    private static final String RESOURCE_NAME = "Floating IP";

    @Inject
    public NeutronFloatingIpsNorthbound(@OsgiService INeutronFloatingIpCRUD neutronCRUD) {
        super(neutronCRUD);
    }

    @Override
    protected String getResourceName() {
        return RESOURCE_NAME;
    }

    /**
     * Returns a list of all FloatingIps.
     */
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAUTHORIZED, condition = "Unauthorized"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_IMPLEMENTED, condition = "Not Implemented"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response listFloatingIps(
            // return fields
            @QueryParam("fields") List<String> fields,
            // note: openstack isn't clear about filtering on lists, so we aren't handling them
            @QueryParam("id") String queryID,
            @QueryParam("floating_network_id") String queryFloatingNetworkId,
            @QueryParam("port_id") String queryPortId,
            @QueryParam("fixed_ip_address") String queryFixedIpAddress,
            @QueryParam("floating_ip_address") String queryFloatingIpAddress,
            @QueryParam("tenant_id") String queryTenantID,
            @QueryParam("router_id") String queryRouterID,
            @QueryParam("status") String queryStatus,
            // pagination
            @QueryParam("limit") String limit,
            @QueryParam("marker") String marker,
            @QueryParam("page_reverse") String pageReverse
    // sorting not supported
    ) {
        INeutronFloatingIpCRUD floatingIpInterface = getNeutronCRUD();
        List<NeutronFloatingIp> allFloatingIps = floatingIpInterface.getAll();
        List<NeutronFloatingIp> ans = new ArrayList<>();
        for (NeutronFloatingIp floatingIp : allFloatingIps) {
            //match filters: TODO provider extension and router extension
            if ((queryID == null || queryID.equals(floatingIp.getID()))
                    && (queryFloatingNetworkId == null
                        || queryFloatingNetworkId.equals(floatingIp.getFloatingNetworkUUID()))
                    && (queryPortId == null || queryPortId.equals(floatingIp.getPortUUID()))
                    && (queryFixedIpAddress == null || queryFixedIpAddress.equals(floatingIp.getFixedIpAddress()))
                    && (queryFloatingIpAddress == null
                        || queryFloatingIpAddress.equals(floatingIp.getFloatingIpAddress()))
                    && (queryStatus == null || queryStatus.equals(floatingIp.getStatus()))
                    && (queryRouterID == null || queryRouterID.equals(floatingIp.getRouterUUID()))
                    && (queryTenantID == null || queryTenantID.equals(floatingIp.getTenantID()))) {
                if (fields.size() > 0) {
                    ans.add(floatingIp.extractFields(fields));
                } else {
                    ans.add(floatingIp);
                }
            }
        }
        //TODO: apply pagination to results
        return Response.status(HttpURLConnection.HTTP_OK).entity(new NeutronFloatingIpRequest(ans)).build();
    }

    /**
     * Returns a specific FloatingIp.
     */
    @Path("{floatingipUUID}")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAUTHORIZED, condition = "Unauthorized"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_IMPLEMENTED, condition = "Not Implemented"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response showFloatingIp(@PathParam("floatingipUUID") String floatingipUUID,
            // return fields
            @QueryParam("fields") List<String> fields) {
        return show(floatingipUUID, fields);
    }

    /**
     * Creates new FloatingIps.
     */
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_CREATED, condition = "Created"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response createFloatingIps(final NeutronFloatingIpRequest input) {
        return create(input);
    }

    /**
     * Updates a FloatingIp.
     */
    @Path("{floatingipUUID}")
    @PUT
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response updateFloatingIp(@PathParam("floatingipUUID") String floatingipUUID,
            NeutronFloatingIpRequest input) {
        return update(floatingipUUID, input);
    }

    /**
     * Deletes a FloatingIp.
     */
    @Path("{floatingipUUID}")
    @DELETE
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_NO_CONTENT, condition = "No Content"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response deleteFloatingIp(@PathParam("floatingipUUID") String floatingipUUID) {
        return delete(floatingipUUID);
    }
}
