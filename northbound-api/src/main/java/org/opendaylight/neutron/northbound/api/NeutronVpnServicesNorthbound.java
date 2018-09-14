/*
 * Copyright (c) 2015 IBM Corporation and others.  All rights reserved.
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
import org.codehaus.enunciate.jaxrs.TypeHint;
import org.opendaylight.neutron.spi.INeutronVpnServiceCRUD;
import org.opendaylight.neutron.spi.NeutronVpnService;

/**
 * Neutron Northbound REST APIs for VPN Service.
 */
@Singleton
@Path("/vpn/vpnservices")
public final class NeutronVpnServicesNorthbound
        extends AbstractNeutronNorthbound<NeutronVpnService, NeutronVpnServiceRequest, INeutronVpnServiceCRUD> {

    private static final String RESOURCE_NAME = "VpnService";

    @Inject
    public NeutronVpnServicesNorthbound(@Reference INeutronVpnServiceCRUD neutronCRUD) {
        super(neutronCRUD);
    }

    @Override
    protected String getResourceName() {
        return RESOURCE_NAME;
    }

    /**
     * Returns a list of all VPN Services.
     */
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAUTHORIZED, condition = "Unauthorized"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_IMPLEMENTED, condition = "Not Implemented"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response listVPNServices(
            // return fields
            @QueryParam("fields") List<String> fields,
            // OpenStack VPNService attributes
            @QueryParam("id") String queryID,
            @QueryParam("tenant_id") String queryTenantID,
            @QueryParam("name") String queryName,
            @QueryParam("admin_state_up") Boolean queryAdminStateUp,
            @QueryParam("router_id") String queryRouterID,
            @QueryParam("status") String queryStatus,
            @QueryParam("subnet_id") String querySubnetID,
            // pagination
            @QueryParam("limit") String limit,
            @QueryParam("marker") String marker,
            @QueryParam("page_reverse") String pageReverse
    // sorting not supported
    ) {
        INeutronVpnServiceCRUD vpnServiceInterface = getNeutronCRUD();
        List<NeutronVpnService> allVPNService = vpnServiceInterface.getAll();
        List<NeutronVpnService> ans = new ArrayList<>();
        for (NeutronVpnService vpnService : allVPNService) {
            if ((queryID == null || queryID.equals(vpnService.getID()))
                    && (queryName == null || queryName.equals(vpnService.getName()))
                    && (queryAdminStateUp == null || queryAdminStateUp.equals(vpnService.getAdminStateUp()))
                    && (queryStatus == null || queryStatus.equals(vpnService.getStatus()))
                    && (querySubnetID == null || querySubnetID.equals(vpnService.getSubnetUUID()))
                    && (queryRouterID == null || queryRouterID.equals(vpnService.getRouterUUID()))
                    && (queryTenantID == null || queryTenantID.equals(vpnService.getTenantID()))) {
                if (fields.size() > 0) {
                    ans.add(vpnService.extractFields(fields));
                } else {
                    ans.add(vpnService);
                }
            }
        }

        return Response.status(HttpURLConnection.HTTP_OK).entity(new NeutronVpnServiceRequest(ans)).build();
    }

    /**
     * Returns a specific VPN Service.
     */
    @Path("{serviceID}")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAUTHORIZED, condition = "Unauthorized"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_IMPLEMENTED, condition = "Not Implemented"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response showVPNService(@PathParam("serviceID") String serviceID,
            // return fields
            @QueryParam("fields") List<String> fields) {
        return show(serviceID, fields);
    }

    /**
     * Creates new VPN Service.
     */
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    @TypeHint(NeutronVpnService.class)
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_CREATED, condition = "Created"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response createVPNService(final NeutronVpnServiceRequest input) {
        return create(input);
    }

    /**
     * Updates a VPN Service.
     */
    @Path("{serviceID}")
    @PUT
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response updateVPNService(@PathParam("serviceID") String serviceID, final NeutronVpnServiceRequest input) {
        return update(serviceID, input);
    }

    /**
     * Deletes a VPN Service.
     */
    @Path("{serviceID}")
    @DELETE
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_NO_CONTENT, condition = "No Content"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response deleteVPNService(@PathParam("serviceID") String serviceID) {
        return delete(serviceID);
    }
}
