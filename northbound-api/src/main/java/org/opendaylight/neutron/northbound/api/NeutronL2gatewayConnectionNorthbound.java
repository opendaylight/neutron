/*
 * Copyright (c) 2015 Hewlett-Packard Development Company, L.P. and others.  All rights reserved.
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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.codehaus.enunciate.jaxrs.ResponseCode;
import org.codehaus.enunciate.jaxrs.StatusCodes;
import org.opendaylight.neutron.spi.INeutronL2gatewayConnectionCRUD;
import org.opendaylight.neutron.spi.NeutronCRUDInterfaces;
import org.opendaylight.neutron.spi.NeutronL2gatewayConnection;
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

@Path ("/l2gateway-connections")
public class NeutronL2gatewayConnectionNorthbound
    extends AbstractNeutronNorthbound<NeutronL2gatewayConnection, NeutronL2gatewayConnectionRequest, INeutronL2gatewayConnectionCRUD> {

    static final Logger logger = LoggerFactory.getLogger(NeutronL2gatewayConnectionNorthbound.class);

    @Context
    UriInfo uriInfo;

    private static final String RESOURCE_NAME = "L2gatewayConnection";
    private static final String INTERFACE_NAME = "L2gatewayConnection CRUD Interface";

    @Override
    protected String getResourceName() {
        return RESOURCE_NAME;
    }

    @Override
    protected NeutronL2gatewayConnection extractFields(NeutronL2gatewayConnection o,
                                                     List<String> fields) {
        return o.extractFields(fields);
    }

    @Override
    protected NeutronL2gatewayConnectionRequest newNeutronRequest(NeutronL2gatewayConnection o) {
        return new NeutronL2gatewayConnectionRequest(o);
    }

    @Override
    protected INeutronL2gatewayConnectionCRUD getNeutronCRUD() {
        NeutronCRUDInterfaces answer = new NeutronCRUDInterfaces().fetchINeutronL2gatewayConnectionCRUD(this);
        if (answer.getL2gatewayConnectionInterface() == null) {
            throw new ServiceUnavailableException(serviceUnavailable());
        }
        return answer.getL2gatewayConnectionInterface();
    }

    /**
     * Creates L2gateway Connection
     * @param  input contains connection details
     * @return status
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

    public Response createL2gatewayConnection(final NeutronL2gatewayConnectionRequest input) {
        logger.debug("createL2GatewayConnection   NeutronL2GatewayConnectionRequest");
        return create(input);
    }

    /**
     * Returns a list of all L2gateway Connections.
     *
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
        INeutronL2gatewayConnectionCRUD l2gatewayConnectionInterface = getNeutronInterfaces()
            .getL2gatewayConnectionInterface();
        if (l2gatewayConnectionInterface == null) {
            throw new ServiceUnavailableException(INTERFACE_NAME
                    + RestMessages.SERVICEUNAVAILABLE.toString());
        }
        List<NeutronL2gatewayConnection> allL2gatewayConnections = l2gatewayConnectionInterface.getAll();
        List<NeutronL2gatewayConnection> ans = new ArrayList<NeutronL2gatewayConnection>();
        Iterator<NeutronL2gatewayConnection> i = allL2gatewayConnections
            .iterator();
        while (i.hasNext()) {
            NeutronL2gatewayConnection oSS = i.next();
            if ((queryTenantID == null || queryTenantID.equals(oSS
                .getTenantID()))
                    && (queryConnectionID == null || queryConnectionID
                        .equals(oSS.getID()))
                    && (queryL2gatewayID == null || queryL2gatewayID.equals(oSS
                        .getL2gatewayID()))
                    && (queryNetworkID == null || queryNetworkID.equals(oSS
                        .getNetworkID()))
                    && (querySegmentID == null || querySegmentID.equals(oSS
                        .getSegmentID()))
                    && (queryPortID == null || queryPortID.equals(oSS
                        .getPortID()))) {
                if (fields.size() > 0) {
                    ans.add(extractFields(oSS, fields));
                } else {
                    ans.add(oSS);
                }
            }
        }
        // TODO: apply pagination to results
        return Response.status(HttpURLConnection.HTTP_OK)
            .entity(new NeutronL2gatewayConnectionRequest(ans)).build();
    }

    /**
     * Returns a specific L2gateway Connection.
     * @param l2gatewayConnectionID gateway connectID to fetch
     * @param fields attributes used for querying
     * @return status
     */
    @Path ("{l2gatewayConnectionID}")
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
    public Response showL2gatewayID(@PathParam("l2gatewayConnectionID") String l2gatewayConnectionID,
                                      // return fields
                                      @QueryParam("fields") List<String> fields) {
        return show(l2gatewayConnectionID, fields);
    }

    /**
     * Deletes a L2gateway Connection
     * @param  l2gatewayConnectionID  connection ID to delete
     * @return status
     */
    @Path("{l2gatewayConnectionID}")
    @DELETE
    @StatusCodes({
        @ResponseCode(code = HttpURLConnection.HTTP_NO_CONTENT,
                condition = "No Content"),
        @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE,
        condition = "No providers available") })
    public Response deleteL2gatewayConnection(
            @PathParam("l2gatewayConnectionID") String l2gatewayConnectionID) {
        return delete(l2gatewayConnectionID);
    }

    private NeutronCRUDInterfaces getNeutronInterfaces() {
        logger.debug("Get Neutron interface");
        NeutronCRUDInterfaces answer = new NeutronCRUDInterfaces().fetchINeutronL2gatewayConnectionCRUD(this);
        if (answer.getL2gatewayConnectionInterface() == null) {
            throw new ServiceUnavailableException(INTERFACE_NAME + RestMessages.SERVICEUNAVAILABLE.toString());
        }
        return answer;
    }

    // l2gwconnection API doesn't have update method
}
