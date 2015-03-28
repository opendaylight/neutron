/*
 * Copyright IBM Corporation, 2015.  All rights reserved.
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

/**
 * Neutron Northbound REST APIs for Metering Lable Rules.<br>
 * This class provides REST APIs for managing neutron metering label rules
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

@Path("/metering/metering-label-rules")
public class NeutronMeteringLabelRulesNorthbound {

    @Context
    UriInfo uriInfo;

    /**
     * Returns a list of all metering label rules */

    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    //@TypeHint(OpenStackNetworks.class)
    @StatusCodes({
        @ResponseCode(code = 501, condition = "Not Implemented") })
    public Response listMeteringLabelRules(
            ) {
        throw new UnimplementedException("Unimplemented");
    }

    /**
     * Returns a specific metering label */

    @Path("{ruleUUID}")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @StatusCodes({
        @ResponseCode(code = 501, condition = "Not Implemented") })
    public Response showMeteringLabelRule(
            @PathParam("ruleUUID") String ruleUUID
            ) {
        throw new UnimplementedException("Unimplemented");
    }

    /**
     * Creates new metering label */
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    //@TypeHint(NeutronNetwork.class)
    @StatusCodes({
        @ResponseCode(code = 501, condition = "Not Implemented") })
    public Response createMeteringLabelRule(final NeutronMeteringLabelRuleRequest input) {
        throw new UnimplementedException("Unimplemented");
    }

    /**
     * Deletes a Metering Label */

    @Path("{ruleUUID}")
    @DELETE
    @StatusCodes({
        @ResponseCode(code = 501, condition = "Not Implemented") })
    public Response deleteMeteringLabelRule(
            @PathParam("ruleUUID") String ruleUUID) {
        throw new UnimplementedException("Unimplemented");
    }
}
