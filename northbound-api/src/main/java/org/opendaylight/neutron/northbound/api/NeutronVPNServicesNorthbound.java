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
import java.util.Iterator;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
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
import org.opendaylight.neutron.spi.INeutronVPNServiceCRUD;
import org.opendaylight.neutron.spi.NeutronCRUDInterfaces;
import org.opendaylight.neutron.spi.NeutronVPNService;

/**
 * Neutron Northbound REST APIs for VPN Service.<br>
 * This class provides REST APIs for managing neutron VPN Services
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

@Path("/vpn/vpnservices")
public class NeutronVPNServicesNorthbound
        extends AbstractNeutronNorthbound<NeutronVPNService, NeutronVPNServiceRequest, INeutronVPNServiceCRUD> {

    private static final String RESOURCE_NAME = "VPNService";

    @Override
    protected String getResourceName() {
        return RESOURCE_NAME;
    }

    @Override
    protected NeutronVPNService extractFields(NeutronVPNService o, List<String> fields) {
        return o.extractFields(fields);
    }

    @Context
    UriInfo uriInfo;

    @Override
    protected NeutronVPNServiceRequest newNeutronRequest(NeutronVPNService o) {
        return new NeutronVPNServiceRequest(o);
    }

    @Override
    protected INeutronVPNServiceCRUD getNeutronCRUD() {
        NeutronCRUDInterfaces answer = new NeutronCRUDInterfaces().fetchINeutronVPNServiceCRUD(this);
        if (answer.getVPNServiceInterface() == null) {
            throw new ServiceUnavailableException(serviceUnavailable());
        }
        return answer.getVPNServiceInterface();
    }

    /**
     * Returns a list of all VPN Services
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
        INeutronVPNServiceCRUD VPNServiceInterface = getNeutronCRUD();
        List<NeutronVPNService> allVPNService = VPNServiceInterface.getAll();
        List<NeutronVPNService> ans = new ArrayList<>();
        Iterator<NeutronVPNService> i = allVPNService.iterator();
        while (i.hasNext()) {
            NeutronVPNService oSS = i.next();
            if ((queryID == null || queryID.equals(oSS.getID()))
                    && (queryName == null || queryName.equals(oSS.getName()))
                    && (queryAdminStateUp == null || queryAdminStateUp.equals(oSS.getAdminStateUp()))
                    && (queryStatus == null || queryStatus.equals(oSS.getStatus()))
                    && (querySubnetID == null || querySubnetID.equals(oSS.getSubnetUUID()))
                    && (queryRouterID == null || queryRouterID.equals(oSS.getRouterUUID()))
                    && (queryTenantID == null || queryTenantID.equals(oSS.getTenantID()))) {
                if (fields.size() > 0) {
                    ans.add(extractFields(oSS, fields));
                } else {
                    ans.add(oSS);
                }
            }
        }

        return Response.status(HttpURLConnection.HTTP_OK).entity(new NeutronVPNServiceRequest(ans)).build();
    }

    /**
     * Returns a specific VPN Service
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
     * Creates new VPN Service
     */
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    @TypeHint(NeutronVPNService.class)
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_CREATED, condition = "Created"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response createVPNService(final NeutronVPNServiceRequest input) {
        return create(input);
    }

    /**
     * Updates a VPN Service
     */
    @Path("{serviceID}")
    @PUT
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response updateVPNService(@PathParam("serviceID") String serviceID, final NeutronVPNServiceRequest input) {
        return update(serviceID, input);
    }

    /**
     * Deletes a VPN Service
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
