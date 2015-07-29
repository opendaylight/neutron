/*
 * Copyright (c) 2015 Hewlett-Packard Development Company and others.  All rights reserved.
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
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.enunciate.jaxrs.ResponseCode;
import org.codehaus.enunciate.jaxrs.StatusCodes;
import org.opendaylight.neutron.spi.INeutronL2GatewayConnectionCRUD;
import org.opendaylight.neutron.spi.NeutronCRUDInterfaces;
import org.opendaylight.neutron.spi.NeutronL2GatewayConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Neutron Northbound REST APIs for L2 gateway Connection.<br>
 * This class provides REST APIs for managing L2 gateway Connection
 * * *
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
 */

@Path ("/l2Gatewayconnections")
public class NeutronL2GatewayConnectionNorthbound {

    static final Logger logger = LoggerFactory
            .getLogger(NeutronL2GatewayConnectionNorthbound.class);

    private static final String INTERFACE_NAME = "L2gatewayConnection CRUD Interface";
    private static final String UUID_NO_EXIST = "L2gateway Connection UUID does not exist.";

    private NeutronL2GatewayConnection extractFields(NeutronL2GatewayConnection o,
                                                     List<String> fields) {
        return o.extractFields(fields);
    }

    private NeutronCRUDInterfaces getNeutronInterfaces() {
        logger.debug("Get Neutron interface");
        NeutronCRUDInterfaces answer = new NeutronCRUDInterfaces()
            .fetchINeutronL2GatewayConnectionCRUD(this);
        if (answer.getL2gatewayConnectionInterface() == null) {
            throw new ServiceUnavailableException(INTERFACE_NAME
                    + RestMessages.SERVICEUNAVAILABLE.toString());
        }
        return answer;
    }

    /**
     * Creates L2gateway Connection
     */
    @POST
    @Produces ({MediaType.APPLICATION_JSON})
    @Consumes ({MediaType.APPLICATION_JSON})
    @StatusCodes({
            @ResponseCode(code = HttpURLConnection.HTTP_CREATED, condition = "Created"),
            @ResponseCode(code = HttpURLConnection.HTTP_BAD_REQUEST, condition = "Bad Request"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAUTHORIZED, condition = "Unauthorized"),
            @ResponseCode(code = HttpURLConnection.HTTP_FORBIDDEN, condition = "Forbidden"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
            @ResponseCode(code = HttpURLConnection.HTTP_CONFLICT, condition = "Conflict"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_IMPLEMENTED, condition = "Not Implemented"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })

    public Response createL2GatewayConnection(final NeutronL2GatewayConnectionRequest input) {
        logger.debug("createL2GatewayConnection   NeutronL2GatewayConnectionRequest");
        INeutronL2GatewayConnectionCRUD l2gatewayConnectionInterface = getNeutronInterfaces()
            .getL2gatewayConnectionInterface();

        if (input.isSingleton()) {
            NeutronL2GatewayConnection singleton = input.getSingleton();
            /* Verify that the L2gateway connection doesn't already exist. */
            if (l2gatewayConnectionInterface
                .neutronL2gatewayConnectionExists(singleton
                    .getL2gatewayConnectionUUID())) {
                throw new BadRequestException("L2gateway Connection already exists");
            }
            if (singleton.getSegmentID() == null) {
                throw new BadRequestException("Invalid/Missing segmentation id");
            }
            /* Add L2gateway connection to MD-SAL */
            l2gatewayConnectionInterface.addNeutronL2gatewayConnection(singleton);

        } else {
            throw new BadRequestException("Only Singlton request allowed.");
        }
        return Response.status(HttpURLConnection.HTTP_CREATED).entity(input)
            .build();
    }

    /**
     * Returns a list of all L2gateway Connections
     */
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @StatusCodes({
        @ResponseCode(code = HttpURLConnection.HTTP_OK,
                condition = "Operation successful"),
                @ResponseCode(code = HttpURLConnection.HTTP_UNAUTHORIZED,
                condition = "Unauthorized"),
                @ResponseCode(code = HttpURLConnection.HTTP_NOT_IMPLEMENTED,
                condition = "Not Implemented"),
                @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE,
                condition = "No providers available") })
    public Response listL2gatewayConnections(
                                // return fields
                                @QueryParam("fields") List<String> fields,
                                @QueryParam("tenant_id") String queryTenantID,
                                @QueryParam("connection_id") String queryConnectionID,
                                @QueryParam("l2gateway_id") String queryL2gatewayID,
                                @QueryParam("network_id") String queryNetworkID,
                                @QueryParam("segment_id") String querySegmentID,
                                @QueryParam("port_id") String queryPortID,
                                @QueryParam ("limit") String limit,
                                @QueryParam ("marker") String marker,
                                @QueryParam ("page_reverse") String pageReverse
                                // sorting not supported
    ) {
        INeutronL2GatewayConnectionCRUD l2gatewayConnectionInterface = getNeutronInterfaces()
            .getL2gatewayConnectionInterface();
        if (l2gatewayConnectionInterface == null) {
            throw new ServiceUnavailableException(INTERFACE_NAME
                    + RestMessages.SERVICEUNAVAILABLE.toString());
        }
        List<NeutronL2GatewayConnection> allL2gatewayConnections = l2gatewayConnectionInterface
            .getAllL2gatewayConnections();
        List<NeutronL2GatewayConnection> ans = new ArrayList<NeutronL2GatewayConnection>();
        Iterator<NeutronL2GatewayConnection> i = allL2gatewayConnections
            .iterator();
        while (i.hasNext()) {
            NeutronL2GatewayConnection oSS = i.next();
            if ((queryTenantID == null || queryTenantID.equals(oSS
                .getTenantUUID()))
                    && (queryConnectionID == null || queryConnectionID
                        .equals(oSS.getL2gatewayConnectionUUID()))
                    && (queryL2gatewayID == null || queryL2gatewayID.equals(oSS
                        .getL2gatewayUUID()))
                    && (queryNetworkID == null || queryNetworkID.equals(oSS
                        .getNetworkUUID()))
                    && (querySegmentID == null || querySegmentID.equals(oSS
                        .getSegmentID()))
                    && (queryPortID == null || queryPortID.equals(oSS
                        .getPortUUID()))) {
                if (fields.size() > 0) {
                    ans.add(extractFields(oSS, fields));
                } else {
                    ans.add(oSS);
                }
            }
        }
        // TODO: apply pagination to results
        return Response.status(HttpURLConnection.HTTP_OK)
            .entity(new NeutronL2GatewayConnectionRequest(ans)).build();
    }

    /**
     * Returns a specific L2gateway Connection.
     */
    @Path ("{l2gatewayConnectionUUID}")
    @GET
    @Produces ({MediaType.APPLICATION_JSON})
    @StatusCodes ({
        @ResponseCode (code = HttpURLConnection.HTTP_OK,
                condition = "Operation successful"),
                @ResponseCode (code = HttpURLConnection.HTTP_UNAUTHORIZED,
                condition = "Unauthorized"),
                @ResponseCode (code = HttpURLConnection.HTTP_NOT_FOUND,
                condition = "Not Found"),
                @ResponseCode(code = HttpURLConnection.HTTP_NOT_IMPLEMENTED,
                condition = "Not Implemented"),
                @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE,
                condition = "No providers available") })
    public Response showL2gatewayUUID(@PathParam("l2gatewayConnectionUUID") String l2gatewayConnectionUUID,
                                      // return fields
                                      @QueryParam("fields") List<String> fields) {
        INeutronL2GatewayConnectionCRUD l2gatewayConnectionInterface = getNeutronInterfaces()
            .getL2gatewayConnectionInterface();
        if (!l2gatewayConnectionInterface
            .neutronL2gatewayConnectionExists(l2gatewayConnectionUUID)) {
            throw new ResourceNotFoundException(UUID_NO_EXIST);
        }
        if (!fields.isEmpty()) {
            NeutronL2GatewayConnection ans = l2gatewayConnectionInterface
                .getNeutronL2gatewayConnection(l2gatewayConnectionUUID);
            return Response
                .status(HttpURLConnection.HTTP_OK)
                .entity(new NeutronL2GatewayConnectionRequest(extractFields(ans,fields))).build();
        }
            return Response.status(HttpURLConnection.HTTP_OK)
                .entity(new NeutronL2GatewayConnectionRequest(l2gatewayConnectionInterface
                       .getNeutronL2gatewayConnection(l2gatewayConnectionUUID))).build();
    }

    /**
     * Deletes a L2gateway Connection
     */
    @Path("{l2gatewayConnectionUUID}")
    @DELETE
    @StatusCodes({
        @ResponseCode(code = HttpURLConnection.HTTP_NO_CONTENT,
                condition = "No Content"),
        @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE,
        condition = "No providers available") })
    public Response deleteL2gatewayConnection(
            @PathParam("l2gatewayConnectionUUID") String l2gatewayConnectionUUID) {
        INeutronL2GatewayConnectionCRUD l2gatewayConnectionInterface =
                getNeutronInterfaces().getL2gatewayConnectionInterface();
        NeutronL2GatewayConnection singleton=l2gatewayConnectionInterface
                .getNeutronL2gatewayConnection(l2gatewayConnectionUUID);

        if(!l2gatewayConnectionInterface.removeL2gatewayConnection(l2gatewayConnectionUUID)){
            throw new InternalServerErrorException("Could not delete l2gateway connection");
        };

        return Response.status(HttpURLConnection.HTTP_NO_CONTENT).build();
    }

}