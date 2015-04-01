/*
 * Copyright IBM Corporation, 2013.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.northbound.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
import org.opendaylight.neutron.spi.INeutronVPNServiceAware;
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
public class NeutronVPNServicesNorthbound {

    @Context
    UriInfo uriInfo;

    /**
     * Returns a list of all VPN Services */

    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @StatusCodes({
        @ResponseCode(code = 501, condition = "Not Implemented") })
    public Response listVPNServices(
            ) {
        throw new UnimplementedException("Not Implemented");
    }

    /**
     * Returns a specific VPN Service */

    @Path("{serviceID}")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @StatusCodes({
        @ResponseCode(code = 501, condition = "Not Implemented") })
    public Response showVPNService(
            @PathParam("serviceID") String serviceID,
            // return fields
            @QueryParam("fields") List<String> fields
            ) {
        throw new UnimplementedException("Not Implemented");
    }

    /**
     * Creates new VPN Service */
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    @TypeHint(NeutronVPNService.class)
    @StatusCodes({
        @ResponseCode(code = 501, condition = "Not Implemented") })
    public Response createVPNService(final NeutronVPNServiceRequest input) {
        throw new UnimplementedException("Not Implemented");
    }

    /**
     * Updates a VPN Service */
    @Path("{serviceID}")
    @PUT
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    @StatusCodes({
        @ResponseCode(code = 501, condition = "Not Implemented") })
    public Response updateVPNService(
            @PathParam("serviceID") String serviceID, final NeutronVPNServiceRequest input
            ) {
        throw new UnimplementedException("Not Implemented");
    }

    /**
     * Deletes a VPN Service */

    @Path("{serviceID}")
    @DELETE
    @StatusCodes({
        @ResponseCode(code = 501, condition = "Not Implemented") })
    public Response deleteVPNService(
            @PathParam("serviceID") String serviceID) {
        throw new UnimplementedException("Not Implemented");
    }
}
